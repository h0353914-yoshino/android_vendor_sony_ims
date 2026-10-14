package org.codeaurora.ims;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;
import android.telephony.ims.ImsUtListener;
import android.telephony.ims.stub.ImsUtImplBase;
import com.qualcomm.ims.utils.Log;
import java.util.ArrayList;
import java.util.List;
import org.codeaurora.telephony.utils.AsyncResult;
import org.codeaurora.telephony.utils.CallForwardInfo;

/* JADX INFO: loaded from: classes.dex */
public class ImsUtImpl extends ImsUtImplBase {
    private static final int CF_DISABLED = 0;
    private static final int CF_ENABLED = 1;
    static final int CF_REASON_ALL = 4;
    static final int CF_REASON_ALL_CONDITIONAL = 5;
    static final int CF_REASON_BUSY = 1;
    static final int CF_REASON_NOT_REACHABLE = 3;
    static final int CF_REASON_NO_REPLY = 2;
    static final int CF_REASON_UNCONDITIONAL = 0;
    private static final int EVENT_QUERY_CB = 13;
    private static final int EVENT_QUERY_CF = 1;
    private static final int EVENT_QUERY_CLIP = 7;
    private static final int EVENT_QUERY_CLIR = 5;
    private static final int EVENT_QUERY_COLP = 11;
    private static final int EVENT_QUERY_COLR = 9;
    private static final int EVENT_QUERY_CW = 3;
    private static final int EVENT_UNSOL_ON_SS = 15;
    private static final int EVENT_UPDATE_CB = 14;
    private static final int EVENT_UPDATE_CF = 2;
    private static final int EVENT_UPDATE_CLIP = 8;
    private static final int EVENT_UPDATE_CLIR = 6;
    private static final int EVENT_UPDATE_COLP = 12;
    private static final int EVENT_UPDATE_COLR = 10;
    private static final int EVENT_UPDATE_CW = 4;
    private static final String LOG_TAG = "ImsUtImpl";
    private static final int MAX_REQUESTS_PENDING = 50;
    static final int SERVICE_CLASS_DATA = 2;
    static final int SERVICE_CLASS_DATA_ASYNC = 32;
    static final int SERVICE_CLASS_DATA_SYNC = 16;
    static final int SERVICE_CLASS_FAX = 4;
    static final int SERVICE_CLASS_MAX = 128;
    static final int SERVICE_CLASS_NONE = 0;
    static final int SERVICE_CLASS_PACKET = 64;
    static final int SERVICE_CLASS_PAD = 128;
    static final int SERVICE_CLASS_SMS = 8;
    static final int SERVICE_CLASS_VOICE = 1;
    static final String UT_ERROR_FDN_FAILURE = "E_FDN_CHECK_FAILURE";
    static final String UT_ERROR_GENERIC = "E_GENERIC_FAILURE";
    static final String UT_ERROR_NETWORK_UNSUPPORTED = "E_NETWORK_NOT_SUPPORTED";
    static final String UT_ERROR_SS_MODIFIED_TO_DIAL = "E_SS_MODIFIED_TO_DIAL";
    static final String UT_ERROR_SS_MODIFIED_TO_DIAL_VIDEO = "E_SS_MODIFIED_TO_DIAL_VIDEO";
    static final String UT_ERROR_SS_MODIFIED_TO_SS = "E_SS_MODIFIED_TO_SS";
    static final String UT_ERROR_SS_MODIFIED_TO_USSD = "E_SS_MODIFIED_TO_USSD";
    static final String UT_PASSWORD_INCORRECT = "E_PASSWORD_INCORRECT";
    private static int requestId = -1;
    private ImsSenderRxr mCi;
    private Context mContext;
    private Handler mHandler;
    private ImsServiceSub mServiceSub;
    private final String MODIFY_PHONE_STATE = "android.permission.MODIFY_PHONE_STATE";
    private final String READ_PRIVILEGED_PHONE_STATE = "android.permission.READ_PRIVILEGED_PHONE_STATE";
    private final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    private ImsUtListenerImpl mListenerImpl = new ImsUtListenerImpl();

    public ImsUtImpl(ImsServiceSub serviceSub, ImsSenderRxr senderRxr, Context context) {
        ImsUtImplHandler imsUtImplHandler = new ImsUtImplHandler(this);
        this.mHandler = imsUtImplHandler;
        this.mServiceSub = serviceSub;
        this.mCi = senderRxr;
        this.mContext = context;
        senderRxr.registerForSuppServiceIndication(imsUtImplHandler, 15, null);
    }

    private void enforceReadPhoneState(String fn) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", fn);
        }
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void close() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "close");
        this.mCi.unregisterForSuppServiceIndication(this.mHandler);
        this.mCi = null;
        this.mListenerImpl = null;
        this.mHandler = null;
    }

    public int queryCallBarring(int cbType) {
        return queryCallBarringForServiceClass(cbType, 0);
    }

    public int queryCallBarringForServiceClass(int cbType, int serviceClass) {
        enforceReadPhoneState("queryCallBarring");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for queryCallBarring.");
            return -1;
        }
        int facility = getFacilityFromCbType(cbType);
        if (facility == -1) {
            Log.e(this, "Unsupported call barring facility code in queryCallBarring.");
            return -1;
        }
        if (facility == 11) {
            this.mCi.suppSvcStatus(3, facility, null, 1, this.mHandler.obtainMessage(13, id, 0, this));
        } else {
            this.mCi.suppSvcStatus(3, facility, null, serviceClass, this.mHandler.obtainMessage(13, id, 0, this));
        }
        return id;
    }

    private int getFacilityFromCbType(int cbType) {
        if (cbType == 2) {
            return 3;
        }
        if (cbType == 3) {
            return 4;
        }
        if (cbType == 4) {
            return 5;
        }
        if (cbType == 1) {
            return 6;
        }
        if (cbType == 5) {
            return 7;
        }
        if (cbType == 7) {
            return 8;
        }
        if (cbType == 8) {
            return 9;
        }
        if (cbType == 9) {
            return 10;
        }
        if (cbType == 10) {
            return 11;
        }
        if (cbType == 6) {
            return 12;
        }
        return -1;
    }

    public int queryCallForward(int condition, String number) {
        return queryCFForServiceClass(condition, number, 1);
    }

    public int queryCFForServiceClass(int condition, String number, int serviceClass) {
        int reason;
        enforceReadPhoneState("queryCallForward");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for queryCallForward.");
            return -1;
        }
        if (condition == 0) {
            reason = 0;
        } else if (condition == 1) {
            reason = 1;
        } else if (condition == 2) {
            reason = 2;
        } else if (condition == 3) {
            reason = 3;
        } else if (condition == 4) {
            reason = 4;
        } else if (condition == 5) {
            reason = 5;
        } else if (condition == 6) {
            reason = -1;
        } else {
            Log.e(this, "Invalid condition for queryCallForward.");
            return -1;
        }
        this.mCi.queryCallForwardStatus(reason, serviceClass, number, this.mHandler.obtainMessage(1, id, 0, this));
        return id;
    }

    public int queryCallWaiting() {
        enforceReadPhoneState("queryCallWaiting");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for queryCallWaiting.");
            return -1;
        }
        this.mCi.queryCallWaiting(0, this.mHandler.obtainMessage(3, id, 0, this));
        return id;
    }

    public int queryClir() {
        enforceReadPhoneState("queryCLIR");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for queryCLIR.");
            return -1;
        }
        this.mCi.getCLIR(this.mHandler.obtainMessage(5, id, 0, this));
        return id;
    }

    public int queryClip() {
        enforceReadPhoneState("queryCLIP");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for queryCLIP.");
            return -1;
        }
        this.mCi.queryCLIP(this.mHandler.obtainMessage(7, id, 0, this));
        return id;
    }

    public int queryColr() {
        enforceReadPhoneState("queryCOLR");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for queryCOLR.");
            return -1;
        }
        this.mCi.getCOLR(this.mHandler.obtainMessage(9, id, 0, this));
        return id;
    }

    public int queryColp() {
        enforceReadPhoneState("queryCOLP");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for queryCLIP.");
            return -1;
        }
        this.mCi.getSuppSvc("COLP", this.mHandler.obtainMessage(11, id, 0, this));
        return id;
    }

    public int getIcbAction(int action) {
        if (action == 0) {
            return 2;
        }
        if (action == 1) {
            return 1;
        }
        if (action == 4) {
            return 5;
        }
        if (action == 3) {
            return 4;
        }
        return -1;
    }

    public int updateCallBarring(int cbType, int action, String[] barrList) {
        return updateCallBarringForServiceClass(cbType, action, barrList, 0);
    }

    public int updateCallBarringForServiceClass(int cbType, int action, String[] barrList, int serviceClass) {
        return updateCallBarringWithPassword(cbType, action, barrList, serviceClass, "");
    }

    public int updateCallBarringWithPassword(int cbType, int action, String[] barrList, int serviceClass, String password) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateCallBarring");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for updateCallBarring.");
            return -1;
        }
        int facility = getFacilityFromCbType(cbType);
        if (facility == -1) {
            Log.e(this, "Unsupported call barring facility code in updateCallBarring.");
            return -1;
        }
        int cbAction = getIcbAction(action);
        if (facility == 11) {
            this.mCi.suppSvcStatus(cbAction, facility, barrList, password, 1, this.mHandler.obtainMessage(14, id, 0, this));
        } else {
            this.mCi.suppSvcStatus(cbAction, facility, null, password, serviceClass, this.mHandler.obtainMessage(14, id, 0, this));
        }
        return id;
    }

    public int updateCallForward(int action, int condition, String number, int serviceClass, int timeSeconds) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateCallForward");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for updateCallForward.");
            return -1;
        }
        this.mCi.setCallForward(action, condition, serviceClass, number, timeSeconds, this.mHandler.obtainMessage(2, id, 0, this));
        return id;
    }

    public int updateCallForwardUncondTimer(int startHour, int startMinute, int endHour, int endMinute, int action, int condition, String number) {
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for updateCallForwardUncondTimer.");
            return -1;
        }
        this.mCi.setCallForwardUncondTimer(startHour, startMinute, endHour, endMinute, action, condition, 1, number, this.mHandler.obtainMessage(2, id, 0, this));
        return id;
    }

    public int updateCallWaiting(boolean enable, int serviceClass) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateCallWaiting");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for updateCallForward.");
            return -1;
        }
        this.mCi.setCallWaiting(enable, serviceClass, this.mHandler.obtainMessage(4, id, 0, this));
        return id;
    }

    public int updateClir(int clirMode) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateCLIR");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for updateCLIR.");
            return -1;
        }
        this.mCi.setCLIR(clirMode, this.mHandler.obtainMessage(6, id, 0, this));
        return id;
    }

    public int updateClip(boolean enable) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateCLIP");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for updateCLIP.");
            return -1;
        }
        this.mCi.setSuppSvc("CLIP", enable, this.mHandler.obtainMessage(8, id, 0, this));
        return id;
    }

    public int updateColr(int presentation) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateCOLR");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for updateCOLR.");
            return -1;
        }
        this.mCi.setCOLR(presentation, this.mHandler.obtainMessage(10, id, 0, this));
        return id;
    }

    public int updateColp(boolean enable) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateCOLP");
        int id = getIdForRequest();
        if (id < 0) {
            Log.e(this, "Invalid request id for updateCOLP.");
            return -1;
        }
        this.mCi.setSuppSvc("COLP", enable, this.mHandler.obtainMessage(12, id, 0, this));
        return id;
    }

    public void setListener(ImsUtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setListener");
        this.mListenerImpl.mListener = listener;
    }

    private int getIdForRequest() {
        int i = requestId + 1;
        requestId = i;
        if (i >= 50) {
            requestId = 0;
        }
        return requestId;
    }

    public void setIdForRequest(int id) {
        requestId = id;
    }

    public ImsReasonInfo getImsReasonInfoFromResponseError(AsyncResult ar) {
        int code;
        if (ar == null) {
            Log.i(this, "getImsReasonInfoFromResponseError :: Null AsyncResult!");
            return null;
        }
        if (ar.exception == null) {
            Log.i(this, "getImsReasonInfoFromResponseError :: No exception in AsyncResult!");
            return null;
        }
        if (!(ar.exception instanceof RuntimeException)) {
            Log.i(this, "getImsReasonInfoFromResponseError :: Improper exception type in AsyncResult!");
            return null;
        }
        ImsReasonInfo genericError = new ImsReasonInfo(0, 0, null);
        RuntimeException ex = (RuntimeException) ar.exception;
        String error = ex.getMessage();
        if (error == null) {
            Log.i(this, "getImsReasonInfoFromResponseError :: Null message string in exception!");
            return genericError;
        }
        if (error.compareTo(UT_ERROR_GENERIC) == 0) {
            return genericError;
        }
        if (error.compareTo(UT_PASSWORD_INCORRECT) == 0) {
            code = 821;
        } else if (error.compareTo(UT_ERROR_NETWORK_UNSUPPORTED) == 0) {
            code = 802;
        } else if (error.compareTo(UT_ERROR_FDN_FAILURE) == 0) {
            code = 241;
        } else if (error.compareTo(UT_ERROR_SS_MODIFIED_TO_DIAL) == 0) {
            code = 822;
        } else if (error.compareTo(UT_ERROR_SS_MODIFIED_TO_USSD) == 0) {
            code = 823;
        } else if (error.compareTo(UT_ERROR_SS_MODIFIED_TO_SS) == 0) {
            code = 824;
        } else if (error.compareTo(UT_ERROR_SS_MODIFIED_TO_DIAL_VIDEO) == 0) {
            code = 825;
        } else {
            Log.i(this, "getImsReasonInfoFromResponseError :: Unrecognized exception message string!");
            return genericError;
        }
        Log.i(this, "getImsReasonInfoFromResponseError :: ImsReasonInfo code=" + code);
        return new ImsReasonInfo(code, 0, null);
    }

    private class ImsUtImplHandler extends Handler {
        ImsUtImplHandler(ImsUtImpl imsUtImpl) {
            this(Looper.getMainLooper());
        }

        ImsUtImplHandler(Looper looper) {
            super(looper);
        }

        private void handleCFUTResponse(AsyncResult ar, Message msg) {
            ImsCallForwardTimerInfo[] cfInfoList = (ImsCallForwardTimerInfo[]) ar.result;
            String str = null;
            if (cfInfoList.length < 1) {
                Log.e(this, "ImsCallForwardTimerInfo[] has no elements!");
                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, 0, null));
                return;
            }
            boolean badCfResponse = false;
            ImsCallForwardInfo[] callForwardInfoList = new ImsCallForwardInfo[cfInfoList.length];
            int callForwardStatus = 0;
            int condition = -1;
            int i = 0;
            while (i < cfInfoList.length) {
                ImsCallForwardTimerInfo cfInfo = cfInfoList[i];
                if (cfInfo.status == 1) {
                    callForwardStatus = 1;
                } else if (cfInfo.status == 0) {
                    callForwardStatus = 0;
                } else {
                    badCfResponse = true;
                    Log.e(this, "Bad status in Query CFUT response.");
                }
                if (cfInfo.reason == 0) {
                    condition = 0;
                } else {
                    badCfResponse = true;
                    Log.e(this, "Bad reason in Query CFUT response.");
                }
                if (badCfResponse) {
                    ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, 0, str));
                    return;
                }
                callForwardInfoList[i] = new ImsCallForwardInfo(condition, callForwardStatus, 0, cfInfo.serviceClass, new String(cfInfo.number), 0);
                i++;
                str = null;
            }
            ImsUtImpl.this.mListenerImpl.utConfigurationCallForwardQueried(msg.arg1, callForwardInfoList);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int status;
            Log.i(this, "Message received: what = " + msg.what);
            int i = 0;
            int i2 = 804;
            switch (msg.what) {
                case 1:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar.exception != null) {
                            Log.e(this, "Query CF error");
                            if (ar.userObj != null) {
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar));
                            }
                        } else if (ar.result == null) {
                            Log.e(this, "Null response received for Query CF!");
                            ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, 0, null));
                        } else if (ar.result instanceof ImsCallForwardTimerInfo[]) {
                            Log.i(this, "Handle CFUT response");
                            handleCFUTResponse(ar, msg);
                        } else {
                            CallForwardInfo[] cfInfoList = (CallForwardInfo[]) ar.result;
                            if (cfInfoList.length < 1) {
                                Log.e(this, "CallForwardInfo[] has no elements!");
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, 0, null));
                            } else {
                                boolean badCfResponse = false;
                                ImsCallForwardInfo[] callForwardInfoList = new ImsCallForwardInfo[cfInfoList.length];
                                int callForwardStatus = 0;
                                int condition = -1;
                                int timeSeconds = 0;
                                int i3 = 0;
                                while (i3 < cfInfoList.length) {
                                    CallForwardInfo cfInfo = cfInfoList[i3];
                                    if (cfInfo.status == 1) {
                                        callForwardStatus = 1;
                                    } else if (cfInfo.status == 0) {
                                        callForwardStatus = 0;
                                    } else {
                                        badCfResponse = true;
                                        Log.e(this, "Bad status in Query CF response.");
                                    }
                                    if (cfInfo.reason == 0) {
                                        condition = 0;
                                    } else if (cfInfo.reason == 1) {
                                        condition = 1;
                                    } else if (cfInfo.reason == 2) {
                                        condition = 2;
                                        timeSeconds = cfInfo.timeSeconds;
                                    } else if (cfInfo.reason == 3) {
                                        condition = 3;
                                    } else if (cfInfo.reason == 4) {
                                        condition = 4;
                                    } else if (cfInfo.reason == 5) {
                                        condition = 5;
                                    } else {
                                        badCfResponse = true;
                                        Log.e(this, "Bad reason in Query CF response.");
                                    }
                                    if (badCfResponse) {
                                        // 由 JADX 反編譯修正：失敗時通知後結束，不可 break 掉 while 後繼續通知成功
                                        ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(i2, i, null));
                                        return;
                                    }
                                    callForwardInfoList[i3] = new ImsCallForwardInfo(condition, callForwardStatus, cfInfo.toa, cfInfo.serviceClass, new String(cfInfo.number), timeSeconds);
                                    i3++;
                                    ar = ar;
                                    i2 = 804;
                                    i = 0;
                                }
                                ImsUtImpl.this.mListenerImpl.utConfigurationCallForwardQueried(msg.arg1, callForwardInfoList);
                            }
                        }
                    }
                    break;
                case 2:
                    AsyncResult ar2 = (AsyncResult) msg.obj;
                    if (ar2 == null || msg.arg1 < 0) {
                        Log.e(this, "Invalid response: ar = " + ar2 + " msgId = " + msg.arg1);
                    } else if (ar2.exception != null) {
                        Log.e(this, "Update CF error");
                        CallForwardStatusInfo cfStatusInfo = (CallForwardStatusInfo) ar2.result;
                        ImsReasonInfo sipError = cfStatusInfo != null ? cfStatusInfo.getSipErrorInfo() : null;
                        String failCause = sipError != null ? sipError.getExtraMessage() : null;
                        if (failCause != null && !failCause.isEmpty()) {
                            ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, new ImsReasonInfo(804, sipError.getExtraCode(), failCause));
                        } else {
                            Log.e(ImsUtImpl.LOG_TAG, "Update CF failure: ar = " + ar2 + " sipError: " + sipError);
                            ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar2));
                        }
                    } else {
                        Log.i(this, "Success callback called for msg.what= " + msg.what);
                        ImsUtImpl.this.mListenerImpl.utConfigurationUpdated(msg.arg1);
                    }
                    break;
                case 3:
                    AsyncResult ar3 = (AsyncResult) msg.obj;
                    if (ar3 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar3.exception != null) {
                            Log.e(this, "Query CW error");
                            if (ar3.userObj != null) {
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar3));
                            }
                        } else if (ar3.result == null) {
                            Log.e(this, "Null response received for Query CW!");
                            ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, 0, null));
                        } else {
                            int[] cwResponse = (int[]) ar3.result;
                            ImsSsInfo[] callWaitingInfoList = new ImsSsInfo[1];
                            if (cwResponse[0] == 1) {
                                if ((cwResponse[1] & 1) == 1) {
                                    status = 1;
                                } else {
                                    status = 0;
                                }
                            } else {
                                int status2 = cwResponse[0];
                                if (status2 != 0) {
                                    Log.e(this, "No service status received for CallWaitingInfo.");
                                    ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, 0, null));
                                } else {
                                    status = 0;
                                }
                                break;
                            }
                            ImsSsInfo.Builder imsSsInfoBuilder = new ImsSsInfo.Builder(status);
                            callWaitingInfoList[0] = imsSsInfoBuilder.build();
                            ImsUtImpl.this.mListenerImpl.utConfigurationCallWaitingQueried(msg.arg1, callWaitingInfoList);
                        }
                    }
                    break;
                case 4:
                    AsyncResult ar4 = (AsyncResult) msg.obj;
                    if (ar4 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar4.exception != null) {
                            Log.e(this, "Update CW error");
                            if (ar4.result != null) {
                                SuppSvcResponse response = (SuppSvcResponse) ar4.result;
                                ImsReasonInfo sipError2 = response.getErrorDetails();
                                if (sipError2 != null) {
                                    Log.d(this, "SuppSvcResponse has failure for msg.what= " + msg.what);
                                    ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, new ImsReasonInfo(804, sipError2.getExtraCode(), sipError2.getExtraMessage()));
                                } else if (response.getFailureCause().length() > 0) {
                                    ImsReasonInfo error = new ImsReasonInfo(804, 0, response.getFailureCause());
                                    Log.e(ImsUtImpl.LOG_TAG, "SuppSvc " + msg.what + " failed with error = " + error);
                                    ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, error);
                                } else {
                                    Log.e(ImsUtImpl.LOG_TAG, "SuppSvcResponse failure with neither errordetails nor failurecause");
                                    ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar4));
                                }
                            } else if (ar4.userObj != null) {
                                ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar4));
                            } else {
                                Log.e(ImsUtImpl.LOG_TAG, "SuppSvcResponse failure with neither ar.result nor userObj");
                                ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar4));
                            }
                        } else {
                            Log.i(this, "Success callback called for msg.what= " + msg.what);
                            ImsUtImpl.this.mListenerImpl.utConfigurationUpdated(msg.arg1);
                        }
                    }
                    break;
                case 5:
                    AsyncResult ar5 = (AsyncResult) msg.obj;
                    if (ar5 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar5.exception != null) {
                            if (msg.what == 5) {
                                Log.e(this, "Query CLIR error");
                            }
                            if (ar5.userObj != null) {
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar5));
                            }
                        } else if (ar5.result != null) {
                            int[] clirResp = (int[]) ar5.result;
                            if (clirResp == null || clirResp.length != 2) {
                                Log.e(this, "Received invalid response for Query CLIR.");
                            } else {
                                ImsSsInfo info = new ImsSsInfo.Builder(-1).setClirOutgoingState(clirResp[0]).setClirInterrogationStatus(clirResp[1]).build();
                                Log.i(this, "Calling success callback for Query CLIR.");
                                ImsUtImpl.this.mListenerImpl.lineIdentificationSupplementaryServiceResponse(msg.arg1, info);
                            }
                        }
                    }
                    break;
                case 6:
                case 8:
                case 10:
                case 12:
                    AsyncResult ar6 = (AsyncResult) msg.obj;
                    if (ar6 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar6.exception == null) {
                            Log.i(this, "Success callback called for msg.what= " + msg.what);
                            ImsUtImpl.this.mListenerImpl.utConfigurationUpdated(msg.arg1);
                        } else {
                            if (msg.what == 6) {
                                Log.e(this, "Update CLIR error");
                            } else if (msg.what == 8) {
                                Log.e(this, "Update CLIP error");
                            } else if (msg.what == 10) {
                                Log.e(this, "Update COLR error");
                            } else if (msg.what == 12) {
                                Log.e(this, "Update COLP error");
                            }
                            if (ar6.result != null) {
                                SuppSvcResponse response2 = (SuppSvcResponse) ar6.result;
                                ImsReasonInfo sipError3 = response2.getErrorDetails();
                                if (sipError3 != null) {
                                    Log.e(this, "SuppSvcResponse has failure for CLIP/COLP update");
                                    ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, new ImsReasonInfo(804, sipError3.getExtraCode(), sipError3.getExtraMessage()));
                                } else if (!response2.getFailureCause().isEmpty()) {
                                    ImsReasonInfo error2 = new ImsReasonInfo(804, 0, response2.getFailureCause());
                                    Log.e(ImsUtImpl.LOG_TAG, "SuppSvc " + msg.what + " failed, error: " + error2);
                                    ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, error2);
                                } else {
                                    Log.e(ImsUtImpl.LOG_TAG, "SuppSvcResponse failure with neither errordetails nor failurecause");
                                    ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar6));
                                }
                            } else if (ar6.userObj != null) {
                                Log.e(ImsUtImpl.LOG_TAG, "SuppSvcResponse failure with valid userObj");
                                ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar6));
                            } else {
                                Log.e(ImsUtImpl.LOG_TAG, "SuppSvcResponse failure with neither ar.result nor userObj");
                                ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar6));
                            }
                        }
                    }
                    break;
                case 7:
                    AsyncResult ar7 = (AsyncResult) msg.obj;
                    SuppService clipStatus = (SuppService) ar7.result;
                    if (ar7 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar7.exception != null) {
                            Log.e(this, "Error for Query Event= " + msg.what);
                            if (ar7.userObj != null) {
                                ImsReasonInfo sipError4 = clipStatus.getErrorDetails();
                                if (sipError4 != null) {
                                    ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, sipError4.getExtraCode(), sipError4.getExtraMessage()));
                                } else {
                                    ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar7));
                                }
                            }
                        } else if (clipStatus != null) {
                            ImsSsInfo.Builder imsSsInfoBuilder2 = new ImsSsInfo.Builder(clipStatus.getStatus());
                            Log.d(this, "Success callback on Query event= " + msg.what);
                            ImsUtImpl.this.mListenerImpl.lineIdentificationSupplementaryServiceResponse(msg.arg1, imsSsInfoBuilder2.build());
                        }
                    }
                    break;
                case 9:
                    AsyncResult ar8 = (AsyncResult) msg.obj;
                    SuppService colr = (SuppService) ar8.result;
                    if (ar8 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar8.exception != null) {
                            Log.e(this, "Error for Query Event= " + msg.what);
                            if (ar8.userObj != null) {
                                ImsReasonInfo sipError5 = colr.getErrorDetails();
                                if (sipError5 != null) {
                                    ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, sipError5.getExtraCode(), sipError5.getExtraMessage()));
                                } else {
                                    ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar8));
                                }
                            }
                        } else if (colr != null) {
                            ImsSsInfo.Builder ssInfoBuilder = new ImsSsInfo.Builder(colr.getStatus());
                            ImsSsInfo ssInfo = ssInfoBuilder.build();
                            Log.i(this, "Service= " + msg.what + " status= " + ssInfo.getStatus());
                            ssInfoBuilder.setProvisionStatus(colr.getProvisionStatus());
                            Log.i(this, "Service= " + msg.what + " Provision Status= " + ssInfo.getProvisionStatus());
                            Log.d(this, "Success callback on Query event= " + msg.what);
                            ImsUtImpl.this.mListenerImpl.lineIdentificationSupplementaryServiceResponse(msg.arg1, ssInfo);
                        }
                    }
                    break;
                case 11:
                    AsyncResult ar9 = (AsyncResult) msg.obj;
                    if (ar9 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar9.exception != null) {
                            Log.e(this, "Query COLP error");
                            if (ar9.userObj != null) {
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar9));
                            }
                        } else if (ar9.result != null) {
                            SuppSvcResponse response3 = (SuppSvcResponse) ar9.result;
                            ImsReasonInfo sipError6 = response3.getErrorDetails();
                            if (sipError6 != null) {
                                Log.e(this, "SuppSvcResponse has failure for COLP query.");
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, sipError6.getExtraCode(), sipError6.getExtraMessage()));
                            } else if (response3.getFailureCause().length() > 0) {
                                ImsReasonInfo error3 = new ImsReasonInfo(804, 0, response3.getFailureCause());
                                Log.e(ImsUtImpl.LOG_TAG, "COLP query failed with error = " + error3);
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, error3);
                            } else {
                                SuppSvcResponse response4 = (SuppSvcResponse) ar9.result;
                                ImsSsInfo.Builder ssInfoBuilder2 = new ImsSsInfo.Builder(response4.getStatus());
                                ImsSsInfo ssInfo2 = ssInfoBuilder2.build();
                                ssInfoBuilder2.setProvisionStatus(response4.getProvisionStatus());
                                Log.i(this, "Service= " + msg.what + " provision Status= " + ssInfo2.getProvisionStatus() + " status = " + ssInfo2.getStatus());
                                Log.i(this, "Success callback called for Query COLP.");
                                ImsUtImpl.this.mListenerImpl.lineIdentificationSupplementaryServiceResponse(msg.arg1, ssInfo2);
                            }
                        }
                    }
                    break;
                case 13:
                    AsyncResult ar10 = (AsyncResult) msg.obj;
                    if (ar10 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar10.exception != null) {
                            Log.e(this, "Query CB error");
                            if (ar10.userObj != null) {
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar10));
                            }
                        } else if (ar10.result == null) {
                            Log.e(this, "Null response received for Query CB!");
                            ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, 0, null));
                        } else {
                            SuppSvcResponse response5 = (SuppSvcResponse) ar10.result;
                            ImsReasonInfo sipError7 = response5.getErrorDetails();
                            if (sipError7 != null) {
                                Log.e(this, "SuppSvcResponse has failure for CB query.");
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, sipError7.getExtraCode(), sipError7.getExtraMessage()));
                            } else if (response5.getFailureCause().length() > 0) {
                                ImsReasonInfo error4 = new ImsReasonInfo(804, 0, response5.getFailureCause());
                                Log.e(ImsUtImpl.LOG_TAG, "CB query failed with error = " + error4);
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, error4);
                            } else if (response5.getStatus() == -1) {
                                Log.e(this, "No service status info in response for CB query.");
                                ImsUtImpl.this.mListenerImpl.utConfigurationQueryFailed(msg.arg1, new ImsReasonInfo(804, 0, null));
                            } else if (!response5.getBarredLines().isEmpty()) {
                                List<ImsSsInfo> ssInfoArray = new ArrayList<>();
                                List<SuppSvcResponse.BarredLines> barredLines = response5.getBarredLines();
                                for (SuppSvcResponse.BarredLines lines : barredLines) {
                                    if (!barredLines.isEmpty()) {
                                        List<SuppSvcResponse.LineStatus> line = lines.getLines();
                                        for (SuppSvcResponse.LineStatus lineStatus : line) {
                                            ImsSsInfo.Builder ssInfoBuilder3 = new ImsSsInfo.Builder(lineStatus.getStatus());
                                            ssInfoBuilder3.setIncomingCommunicationBarringNumber(lineStatus.getNumber());
                                            ssInfoArray.add(ssInfoBuilder3.build());
                                        }
                                    }
                                }
                                ImsUtImpl.this.mListenerImpl.utConfigurationCallBarringQueried(msg.arg1, (ImsSsInfo[]) ssInfoArray.toArray(new ImsSsInfo[ssInfoArray.size()]));
                            } else {
                                ImsSsInfo[] ssInfoStatus = new ImsSsInfo[1];
                                int status3 = 0;
                                if (response5.getStatus() == 1) {
                                    status3 = 1;
                                }
                                ImsSsInfo.Builder imsSsInfoBuilder3 = new ImsSsInfo.Builder(status3);
                                ImsSsInfo ssInfo3 = imsSsInfoBuilder3.build();
                                ssInfoStatus[0] = ssInfo3;
                                Log.i(this, "success callback Query Anonymous CB, status= " + ssInfo3.getStatus());
                                ImsUtImpl.this.mListenerImpl.utConfigurationCallBarringQueried(msg.arg1, ssInfoStatus);
                            }
                        }
                    }
                    break;
                case 14:
                    AsyncResult ar11 = (AsyncResult) msg.obj;
                    if (ar11 != null) {
                        if (msg.arg1 < 0) {
                            Log.e(this, "Invalid message id received in handleMessage.");
                        } else if (ar11.exception != null) {
                            Log.e(this, "Update CB error");
                            if (ar11.userObj != null) {
                                ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, ImsUtImpl.this.getImsReasonInfoFromResponseError(ar11));
                            }
                        } else if (ar11.result != null) {
                            SuppSvcResponse response6 = (SuppSvcResponse) ar11.result;
                            ImsReasonInfo sipError8 = response6.getErrorDetails();
                            if (sipError8 != null) {
                                Log.e(this, "SuppSvcResponse has failure for CB update.");
                                ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, new ImsReasonInfo(804, sipError8.getExtraCode(), sipError8.getExtraMessage()));
                            } else if (response6.getFailureCause().length() > 0) {
                                ImsReasonInfo error5 = new ImsReasonInfo(804, 0, response6.getFailureCause());
                                Log.e(ImsUtImpl.LOG_TAG, "CB update failed with error = " + error5);
                                ImsUtImpl.this.mListenerImpl.utConfigurationUpdateFailed(msg.arg1, error5);
                            } else {
                                ImsUtImpl.this.mListenerImpl.utConfigurationUpdated(msg.arg1);
                            }
                        } else {
                            ImsUtImpl.this.mListenerImpl.utConfigurationUpdated(msg.arg1);
                        }
                    }
                    break;
                case 15:
                    AsyncResult ar12 = (AsyncResult) msg.obj;
                    if (ar12.exception != null) {
                        Log.e(this, "exception in handling UNSOL_ON_SS");
                    } else {
                        ImsSsData ssData = (ImsSsData) ar12.result;
                        ImsUtImpl.this.mListenerImpl.onSupplementaryServiceIndication(ssData);
                    }
                    break;
            }
        }
    }
}
