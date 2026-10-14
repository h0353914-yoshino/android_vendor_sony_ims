package org.codeaurora.ims;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsUssdSessionImpl extends QImsSessionBase {
    private static final int EVENT_CANCEL_USSD = 2;
    private static final int EVENT_CLOSE_USSD_SESSION = 4;
    private static final int EVENT_USSD_MESSAGE_RECEIVED = 3;
    private static final int EVENT_USSD_SENT_RESPONSE = 1;
    private ImsCallProfile mCallProfile;
    private Handler mHandler;
    private UssdInfo mUssdInfo;

    public ImsUssdSessionImpl(ImsCallProfile profile, Context context, ImsSenderRxr senderRxr, int phoneId, ImsServiceClassTracker tracker, UssdInfo info) {
        super(context, senderRxr, phoneId, tracker);
        this.mHandler = new ImsSessionHandler();
        this.mUssdInfo = null;
        this.mCallProfile = null;
        this.mUssdInfo = info;
        this.mCallProfile = profile;
        this.mCi.registerForUssdInfo(this.mHandler, 3, null);
    }

    public void start(String callee, ImsCallProfile profile) {
        if (isSessionValid()) {
            if (!this.mTracker.isUssdSupported()) {
                Log.i(this, "start: USSD is not supported");
                this.mCallbackHandler.callSessionInitiatedFailed(new ImsReasonInfo(131, 0, "USSD not supported"));
            } else {
                this.mState = 1;
                this.mCi.sendUssd(callee, this.mHandler.obtainMessage(1, this));
            }
        }
    }

    public void accept(int callType, ImsStreamMediaProfile profile) {
        if (isSessionValid() && this.mUssdInfo != null) {
            this.mState = 4;
            this.mCallbackHandler.callSessionUssdMessageReceived(this.mUssdInfo.getType(), this.mUssdInfo.getMessage());
        }
    }

    public void terminate(int reason) {
        if (isSessionValid()) {
            this.mCi.cancelPendingUssd(this.mHandler.obtainMessage(2, this));
        }
    }

    public void close() {
        Log.i(this, "Close!");
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.obtainMessage(4).sendToTarget();
        }
    }

    public void sendUssd(String ussdMessage) {
        if (isSessionValid()) {
            this.mCi.sendUssd(convertNullToEmptyString(ussdMessage), this.mHandler.obtainMessage(1, this));
        }
    }

    private static String convertNullToEmptyString(String msg) {
        return msg != null ? msg : "";
    }

    private class ImsSessionHandler extends Handler {
        ImsSessionHandler() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(this, "Message received: what = " + msg.what);
            switch (msg.what) {
                case 1:
                    ImsUssdSessionImpl.this.OnUssdSentResponse((AsyncResult) msg.obj);
                    break;
                case 2:
                    ImsUssdSessionImpl.this.onCancelUssd((AsyncResult) msg.obj);
                    break;
                case 3:
                    ImsUssdSessionImpl.this.onUssdMessageReceived((AsyncResult) msg.obj);
                    break;
                case 4:
                    ImsUssdSessionImpl.this.doClose();
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void OnUssdSentResponse(AsyncResult ar) {
        if (ar == null) {
            Log.e(this, "USSD: response result is null");
            return;
        }
        if (this.mState == 4) {
            Log.e(this, "USSD: ongoing USSD session");
            if (ar.exception != null) {
                this.mState = 8;
                this.mCallbackHandler.callSessionTerminated(new ImsReasonInfo(354, 0, "USSD terminated"));
                return;
            }
            return;
        }
        if (ar.exception != null) {
            Log.e(this, "USSD: response exception: " + ar.exception);
            this.mState = 8;
            ImsReasonInfo sipErrorInfo = ImsCallUtils.getImsReasonInfo(ar);
            this.mCallbackHandler.callSessionInitiatedFailed(sipErrorInfo);
            return;
        }
        Log.d(this, "USSD: response received");
        this.mState = 4;
        this.mCallbackHandler.callSessionInitiated(this.mCallProfile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCancelUssd(AsyncResult ar) {
        if (ar == null) {
            Log.e(this, "USSD: response result is null");
        } else {
            if (ar.exception != null) {
                Log.e(this, "Cancel USSD: response exception: " + ar.exception);
                return;
            }
            Log.d(this, "Cancel USSD: response received");
            this.mState = 8;
            this.mCallbackHandler.callSessionTerminated(new ImsReasonInfo(501, 0, "USSD Cancelled"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUssdMessageReceived(AsyncResult ar) {
        if (ar == null) {
            Log.e(this, "USSD: message result is null");
            return;
        }
        if (ar.exception != null) {
            Log.e(this, "USSD: message exception: " + ar.exception);
            if (this.mState == 4 || this.mState == 1) {
                this.mState = 8;
                this.mCallbackHandler.callSessionTerminated(new ImsReasonInfo(354, 0, "USSD Network Error"));
                return;
            }
            return;
        }
        if (this.mState == 4 || this.mState == 1) {
            Log.d(this, "USSD: message received");
            UssdInfo info = (UssdInfo) ar.result;
            int ussdMode = info.getType();
            String ussdMsg = info.getMessage();
            this.mCallbackHandler.callSessionUssdMessageReceived(ussdMode, ussdMsg);
            if (ussdMode != 1 || ussdMsg.isEmpty()) {
                Log.i(this, "USSD: session closed mode: " + ussdMode);
                doClose();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doClose() {
        Log.i(this, "doClose!");
        if (this.mCallbackHandler != null) {
            this.mCallbackHandler.dispose();
            this.mCallbackHandler = null;
        }
        if (this.mHandler != null) {
            this.mCi.unregisterForUssdInfo(this.mHandler);
            this.mHandler = null;
        }
        notifySessionClosed();
        synchronized (this.mListeners) {
            this.mListeners.clear();
        }
        this.mCi = null;
        this.mCallProfile = null;
        this.mPhoneId = -1;
        this.mState = -1;
    }
}
