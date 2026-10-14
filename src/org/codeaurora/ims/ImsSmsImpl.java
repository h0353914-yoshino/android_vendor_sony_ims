package org.codeaurora.ims;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.stub.ImsSmsImplBase;
import com.qualcomm.ims.utils.Config;
import com.qualcomm.ims.utils.Log;
import java.util.HashMap;
import org.codeaurora.ims.sms.IncomingSms;
import org.codeaurora.ims.sms.SmsResponse;
import org.codeaurora.ims.sms.StatusReport;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsSmsImpl extends ImsSmsImplBase {
    static final String LOG_TAG = "ImsSmsImpl";
    public static final int MT_IMS_STATUS_VALIDATION_FAIL = 2;
    public static final int MT_IMS_STATUS_VALIDATION_NONE = 0;
    public static final int MT_IMS_STATUS_VALIDATION_PASS = 1;
    private Context mContext;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private int mPhoneId;
    private ImsSenderRxr mSmsCi;
    private TelephonyManager mTelephonyManager;
    private final Object mLock = new Object();
    private HashMap<Integer, Integer> mSmsMap = new HashMap<>();
    private boolean mSmsFwkReady = false;
    private int mIncomingToken = 0;
    private final int EVENT_SEND_SMS_RESPONSE = 1;
    private final int EVENT_UNSOL_INCOMING_SMS = 2;
    private final int EVENT_UNSOL_STATUS_REPORT = 3;

    public ImsSmsImpl(Context context, int phoneId, ImsSenderRxr ci) {
        this.mPhoneId = 0;
        this.mContext = context;
        this.mSmsCi = ci;
        this.mPhoneId = phoneId;
        HandlerThread handlerThread = new HandlerThread(ImsSmsImpl.class.getSimpleName());
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        ImsSmsHandler imsSmsHandler = new ImsSmsHandler(this.mHandlerThread.getLooper());
        this.mHandler = imsSmsHandler;
        this.mSmsCi.setOnIncomingImsSms(imsSmsHandler, 2, null);
        this.mSmsCi.setOnImsSmsStatusReport(this.mHandler, 3, null);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
    }

    private void sendStatusReportErrorToRIL(int msgRef) {
        this.mSmsCi.acknowledgeSmsReport(msgRef, 2, null);
    }

    private void sendDeliveryErrorToRIL() {
        this.mSmsCi.acknowledgeSms(0, 2, null);
    }

    private int getSubId() {
        int[] subIds;
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        if (subscriptionManager == null || (subIds = subscriptionManager.getSubscriptionIds(this.mPhoneId)) == null || subIds.length == 0) {
            return -1;
        }
        return subIds[0];
    }

    private boolean isNetworkRoaming() {
        int subId = getSubId();
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null || subId == -1) {
            return false;
        }
        return telephonyManager.createForSubscriptionId(subId).isNetworkRoaming();
    }

    private int maybeAdjustSendStatus(int status) {
        if (status != 4) {
            return status;
        }
        int subId = getSubId();
        if (Config.isCarrierConfigEnabled(this.mContext, subId, "config_retry_sms_over_ims")) {
            Log.d(this, "maybeAdjustSendStatus: retry SMS over IMS");
            return 3;
        }
        if (isNetworkRoaming() && Config.isCarrierConfigEnabled(this.mContext, subId, "fallback_sms_not_allowed_in_roaming")) {
            Log.d(this, "maybeAdjustSendStatus: Roaming network, fallback not allowed.");
            return 2;
        }
        return status;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResponseToFramework(AsyncResult ar) {
        SmsResponse smsResponse = (SmsResponse) ar.result;
        int token = ((Integer) ar.userObj).intValue();
        if (smsResponse == null || !this.mSmsFwkReady) {
            Log.w(this, "smsResponse =" + smsResponse + " isFrameworkReady=" + this.mSmsFwkReady);
            return;
        }
        int ref = smsResponse.getMsgRef();
        int result = maybeAdjustSendStatus(smsResponse.getResult());
        int reason = smsResponse.getReason();
        Log.i(this, "onSendSmsResult:: token:" + token + " smsResponse:" + smsResponse);
        synchronized (this.mLock) {
            this.mSmsMap.put(Integer.valueOf(ref), Integer.valueOf(token));
        }
        try {
            if (result == 1) {
                onSendSmsResultSuccess(token, ref);
            } else {
                onSendSmsResultError(token, ref, result, reason, -1);
            }
        } catch (RuntimeException ex) {
            Log.e(this, "onSendSmsResult: Ex:" + ex.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSmsToFramework(AsyncResult ar) {
        IncomingSms incomingSms = (IncomingSms) ar.result;
        if (incomingSms == null || !this.mSmsFwkReady) {
            Log.w(this, "incomingSms =" + incomingSms + " isFrameworkRead=" + this.mSmsFwkReady);
            sendDeliveryErrorToRIL();
            return;
        }
        if (this.mIncomingToken == Integer.MAX_VALUE) {
            this.mIncomingToken = 0;
        }
        Log.i(this, "onSmsReceived:: token:" + this.mIncomingToken + " incomingSms:" + incomingSms);
        try {
            int i = this.mIncomingToken;
            this.mIncomingToken = i + 1;
            onSmsReceived(i, incomingSms.getFormat(), incomingSms.getPdu());
        } catch (RuntimeException ex) {
            Log.e(this, "onSmsReceived: Ex:" + ex.getMessage());
            sendDeliveryErrorToRIL();
        }
    }

    private boolean isSmsDeliverCompleted(StatusReport report) {
        if (report == null) {
            return false;
        }
        if ("3gpp2".equals(report.getFormat())) {
            return true;
        }
        SmsMessage msg = SmsMessage.createFromPdu(report.getPdu(), report.getFormat());
        if (msg == null) {
            return false;
        }
        int status = msg.getStatus();
        Log.d(this, "isSmsDeliverCompleted : status = " + status);
        return status >= 64 || status < 32;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendStatusReportToFramework(AsyncResult ar) {
        int token;
        StatusReport statusReport = (StatusReport) ar.result;
        if (statusReport == null || !this.mSmsFwkReady) {
            Log.w(this, "statusReport =" + statusReport + " isFrameworkRead=" + this.mSmsFwkReady);
            sendStatusReportErrorToRIL(0);
            return;
        }
        int ref = statusReport.getMsgRef();
        String format = statusReport.getFormat();
        synchronized (this.mLock) {
            Integer tokenObj = this.mSmsMap.get(Integer.valueOf(ref));
            token = tokenObj == null ? -1 : tokenObj.intValue();
        }
        Log.i(this, "onSmsStatusReportReceived:: token: " + token + " statusReport:" + statusReport);
        try {
            onSmsStatusReportReceived(token, ref, format, statusReport.getPdu());
        } catch (RuntimeException ex) {
            Log.e(this, "onSmsStatusReportReceived: Ex:" + ex.getMessage());
            sendStatusReportErrorToRIL(ref);
        }
        if (isSmsDeliverCompleted(statusReport)) {
            synchronized (this.mLock) {
                this.mSmsMap.remove(Integer.valueOf(ref));
            }
        }
    }

    private class ImsSmsHandler extends Handler {
        public ImsSmsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(this, "Message received: what = " + msg.what);
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar == null) {
                Log.e(this, "msg.obg is null");
            }
            switch (msg.what) {
                case 1:
                    ImsSmsImpl.this.sendResponseToFramework(ar);
                    break;
                case 2:
                    ImsSmsImpl.this.sendSmsToFramework(ar);
                    break;
                case 3:
                    ImsSmsImpl.this.sendStatusReportToFramework(ar);
                    break;
                default:
                    Log.i(ImsSmsImpl.LOG_TAG, "Invalid Response");
                    break;
            }
        }
    }

    public void sendSms(int token, int messageRef, String format, String smsc, boolean isRetry, byte[] pdu) {
        Log.i(LOG_TAG, "sendSms:: token:" + token + " msgRef:" + messageRef + " format:" + format + " isRetry:" + isRetry);
        this.mSmsCi.sendSms(messageRef, format, smsc, isRetry, pdu, this.mHandler.obtainMessage(1, Integer.valueOf(token)));
    }

    public void acknowledgeSms(int token, int messageRef, int result) {
        Log.i(LOG_TAG, "acknowledgeSms:: token:" + token + " msgRef:" + messageRef + " result:" + result);
        this.mSmsCi.acknowledgeSms(messageRef, result, null);
    }

    public void acknowledgeSmsReport(int token, int messageRef, int result) {
        Log.i(LOG_TAG, "acknowledgeSmsReport:: token:" + token + " msgRef:" + messageRef + " result:" + result);
        this.mSmsCi.acknowledgeSmsReport(messageRef, result, null);
    }

    public String getSmsFormat() {
        return this.mSmsCi.getSmsFormat();
    }

    public void onReady() {
        this.mSmsFwkReady = true;
    }
}
