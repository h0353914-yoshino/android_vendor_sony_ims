package org.codeaurora.ims;

import android.os.SystemProperties;
import android.telecom.VideoProfile;
import android.telephony.ims.ImsReasonInfo;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsCallUtils {
    public static final int CLIR_RESPONSE_LENGTH = 2;
    public static final int CONFIG_ITEM_INVALID = -1;
    public static final int CONFIG_TYPE_INT = 1;
    public static final int CONFIG_TYPE_INVALID = -1;
    public static final int CONFIG_TYPE_STRING = 2;
    private static final int MIN_VIDEO_CALL_PHONE_NUMBER_LENGTH = 7;
    public static final int SESSION_MERGE_COMPLETED = 2;
    public static final int SESSION_MERGE_FAILED = 3;
    public static final int SESSION_MERGE_NOT_INITIATED = 0;
    public static final int SESSION_MERGE_STARTED = 1;
    private static String TAG = "ImsCallUtils";

    public static boolean isVideoCall(int callType) {
        return callType == 3 || callType == 1 || callType == 2 || callType == 6 || callType == 7 || callType == 4;
    }

    public static boolean isVoiceCall(int callType) {
        return callType == 0;
    }

    public static boolean isValidRilModifyCallType(int callType) {
        return callType == 3 || callType == 1 || callType == 2 || callType == 0 || callType == 4;
    }

    public static boolean isVideoPaused(ImsCallSessionImpl conn, DriverCallIms dc) {
        int currCallType = conn.getInternalCallType();
        DriverCallIms.State currCallState = conn.getInternalState();
        int nextCallType = dc.callDetails.call_type;
        DriverCallIms.State nextCallState = dc.state;
        boolean isHoldingPaused = isVideoCall(currCallType) && currCallState == DriverCallIms.State.HOLDING && isVideoCallTypeWithoutDir(nextCallType) && nextCallState == DriverCallIms.State.ACTIVE;
        boolean isActivePaused = isVideoCall(currCallType) && currCallState == DriverCallIms.State.ACTIVE && isVideoCallTypeWithoutDir(nextCallType) && nextCallState == DriverCallIms.State.ACTIVE;
        return isHoldingPaused || isActivePaused;
    }

    public static boolean canVideoPause(ImsCallSessionImpl conn) {
        return isVideoCall(conn.getInternalCallType()) && conn.isCallActive();
    }

    public static boolean isVideoResumed(ImsCallSessionImpl conn, DriverCallIms dc) {
        int currCallType = conn.getInternalCallType();
        DriverCallIms.State currCallState = conn.getInternalState();
        int nextCallType = dc.callDetails.call_type;
        DriverCallIms.State nextCallState = dc.state;
        return isVideoCallTypeWithoutDir(currCallType) && currCallState == DriverCallIms.State.ACTIVE && isVideoCall(nextCallType) && nextCallState == DriverCallIms.State.ACTIVE;
    }

    public static boolean isAvpRetryDialing(ImsCallSessionImpl conn, DriverCallIms dc) {
        int currCallType = conn.getInternalCallType();
        DriverCallIms.State currCallState = conn.getInternalState();
        int nextCallType = dc.callDetails.call_type;
        DriverCallIms.State nextCallState = dc.state;
        boolean dialingAvpRetry = isVideoCall(currCallType) && (currCallState == DriverCallIms.State.DIALING || currCallState == DriverCallIms.State.ALERTING) && isVideoCallTypeWithoutDir(nextCallType) && nextCallState == DriverCallIms.State.ACTIVE;
        return conn.getImsCallModification().isAvpRetryAllowed() && dialingAvpRetry;
    }

    public static boolean isAvpRetryUpgrade(ImsCallSessionImpl conn, DriverCallIms dc) {
        int currCallType = conn.getInternalCallType();
        DriverCallIms.State currCallState = conn.getInternalState();
        int nextCallType = dc.callDetails.call_type;
        DriverCallIms.State nextCallState = dc.state;
        boolean upgradeAvpRetry = currCallType == 0 && currCallState == DriverCallIms.State.ACTIVE && isVideoCallTypeWithoutDir(nextCallType) && nextCallState == DriverCallIms.State.ACTIVE;
        return conn.getImsCallModification().isAvpRetryAllowed() && upgradeAvpRetry;
    }

    public static boolean isVideoCallTypeWithDir(int callType) {
        return callType == 3 || callType == 2 || callType == 1;
    }

    public static boolean isIncomingVideoCall(ImsCallSessionImpl callSession) {
        return isVideoCall(callSession.getInternalCallType()) && (callSession.getInternalState() == DriverCallIms.State.INCOMING || callSession.getInternalState() == DriverCallIms.State.WAITING);
    }

    public static boolean isIncomingCall(ImsCallSessionImpl callSession) {
        return callSession.getInternalState() == DriverCallIms.State.INCOMING || callSession.getInternalState() == DriverCallIms.State.WAITING;
    }

    public static boolean isNotCsVideoCall(CallDetails details) {
        return isVideoCall(details.call_type) && details.call_domain != 1;
    }

    public static boolean isVideoCallTypeWithoutDir(int callType) {
        return callType == 4;
    }

    public static int convertVideoStateToCallType(int videoState) {
        switch (videoState) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return 0;
        }
    }

    public static int convertCallTypeToVideoState(int callType) {
        switch (callType) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
            case 6:
                return 4;
            case 5:
            default:
                return 0;
        }
    }

    public static int convertToInternalCallType(int imsCallProfileCallType) {
        switch (imsCallProfileCallType) {
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 3;
            case 5:
                return 1;
            case 6:
                return 2;
            case 7:
                return 4;
            default:
                Log.e(TAG, "convertToInternalCallType invalid calltype " + imsCallProfileCallType);
                return 10;
        }
    }

    public static VideoProfile convertToVideoProfile(int callType, int callQuality) {
        VideoProfile videoCallProfile = new VideoProfile(convertCallTypeToVideoState(callType), callQuality);
        return videoCallProfile;
    }

    public static int convertImsErrorToUiError(int error) {
        if (error == CallModify.E_CANCELLED) {
            return 4;
        }
        if (error == CallModify.E_SUCCESS || error == CallModify.E_UNUSED) {
            return 1;
        }
        if (error == 50) {
            return 50;
        }
        return 2;
    }

    public static boolean hasCallTypeChanged(DriverCallIms dc, DriverCallIms dcUpdate) {
        return (dc == null || dcUpdate == null || dc.callDetails == null || dcUpdate.callDetails == null || dc.callDetails.call_type == dcUpdate.callDetails.call_type) ? false : true;
    }

    public static int getUiErrorCode(Throwable ex) {
        if (!(ex instanceof ImsRilException)) {
            return 2;
        }
        ImsRilException imsRilException = (ImsRilException) ex;
        int status = getUiErrorCode(imsRilException.getErrorCode());
        return status;
    }

    public static int getUiErrorCode(int imsErrorCode) {
        switch (imsErrorCode) {
            case 0:
            case 16:
                return 1;
            case 7:
                return 4;
            case 27:
                return 3;
            case 28:
                return 5;
            case 50:
                return 50;
            default:
                return 2;
        }
    }

    public static boolean isConfigRequestValid(int item, int requestType) {
        int configType = -1;
        try {
            if (convertImsConfigToImsConfigItem(item) != -1) {
                switch (item) {
                    case 0:
                    case 1:
                    case 12:
                    case 31:
                    case 54:
                        configType = 2;
                        break;
                    default:
                        configType = 1;
                        break;
                }
            }
            return configType == requestType;
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Invalid config - setting CONFIG_TYPE_INVALID");
            return false;
        }
    }

    public static int convertImsConfigToImsConfigItem(int config) {
        switch (config) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            case 8:
                return 9;
            case 9:
                return 10;
            case 10:
                return 11;
            case 11:
                return 12;
            case 12:
                return 13;
            case 13:
                return 14;
            case 14:
                return 15;
            case 15:
                return 16;
            case 16:
                return 17;
            case 17:
                return 30;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            case 21:
                return 21;
            case 22:
                return 22;
            case 23:
                return 23;
            case 24:
                return 24;
            case 25:
                return 25;
            case 26:
                return 28;
            case 27:
                return 29;
            case 28:
                return 68;
            case 29:
                return 26;
            case 30:
                return 33;
            case 31:
                return 34;
            case 32:
                return 35;
            case 33:
                return 36;
            case 34:
                return 37;
            case 35:
                return 38;
            case 36:
                return 39;
            case 37:
                return 40;
            case 38:
                return 41;
            case 39:
                return 42;
            case 40:
                return 43;
            case 41:
                return 44;
            case 42:
                return 45;
            case 43:
                return 46;
            case 44:
                return 47;
            case 45:
                return 48;
            case 46:
                return 49;
            case 47:
                return 50;
            case 48:
                return 51;
            case 49:
                return 52;
            case 50:
                return 53;
            case 51:
                return 54;
            case 52:
                return 55;
            case 53:
                return 56;
            case 54:
                return 57;
            case 55:
                return 58;
            case 56:
                return 59;
            case 57:
                return 60;
            case 58:
                return 61;
            case 59:
                return 62;
            case 60:
                return 63;
            case 61:
                return 64;
            case 62:
                return 65;
            case 63:
                return 66;
            case 64:
                return 67;
            case 65:
                return 69;
            case 66:
                return 70;
            case 1000:
                return 74;
            case 1001:
                return 71;
            case 1002:
                return 72;
            case 1003:
                return 73;
            case 1004:
                return 75;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean isActive(DriverCallIms dc) {
        return dc != null && dc.state == DriverCallIms.State.ACTIVE;
    }

    public static int toUiErrorCode(ImsRilException ex) {
        switch (ex.getErrorCode()) {
            case 31:
                return 1;
            case 32:
                return 2;
            case 33:
                return 3;
            default:
                return -1;
        }
    }

    public static int toImsErrorCode(ImsRilException ex) {
        switch (ex.getErrorCode()) {
            case 31:
                return 1201;
            case 32:
                return 1202;
            case 33:
                return 1203;
            case 47:
                return 131;
            default:
                return 0;
        }
    }

    public static boolean isCarrierOneSupported() {
        String property = SystemProperties.get("persist.vendor.radio.atel.carrier");
        return "405854".equals(property);
    }

    public static boolean isVideoCallNumValid(String number) {
        if (number == null || number.isEmpty() || number.length() < 7) {
            Log.w(TAG, "Phone number invalid!");
            return false;
        }
        if (number.startsWith("+")) {
            number = number.substring(1, number.length());
        }
        return (number.contains("#") || number.contains("+") || number.contains(",") || number.contains(";") || number.contains("*")) ? false : true;
    }

    public static ImsReasonInfo getImsReasonInfo(AsyncResult ar) {
        ImsReasonInfo sipErrorInfo = null;
        int code = 0;
        if (ar != null) {
            sipErrorInfo = (ImsReasonInfo) ar.result;
            if (ar.exception != null) {
                code = toImsErrorCode((ImsRilException) ar.exception);
            }
        }
        if (sipErrorInfo == null) {
            return new ImsReasonInfo(code, 0, null);
        }
        Log.d(TAG, "Sip error code:" + sipErrorInfo.getExtraCode() + " error string :" + sipErrorInfo.getExtraMessage());
        return new ImsReasonInfo(code, sipErrorInfo.getExtraCode(), sipErrorInfo.getExtraMessage());
    }

    public static boolean isVideoCall(ImsCallSessionImpl session) {
        return session != null && isVideoCall(session.getInternalCallType());
    }

    public static boolean isVideoCall(CallModify callModify) {
        return (callModify == null || callModify.call_details == null || !isVideoCall(callModify.call_details.call_type)) ? false : true;
    }

    public static QImsCallProfile toImsCallProfile(VerstatInfo verstatInfo, QImsCallProfile profile) {
        if (verstatInfo == null || profile == null) {
            return profile;
        }
        if (verstatInfo.canMarkUnwantedCall()) {
            profile.setCallExtraBoolean("CanMarkUnwantedCall", true);
        }
        int verstatVerificationStatus = verstatInfo.getVerstatVerificationStatus();
        if (verstatVerificationStatus == 0) {
            profile.setCallerNumberVerificationStatus(0);
        } else if (verstatVerificationStatus == 1) {
            profile.setCallerNumberVerificationStatus(1);
        } else {
            profile.setCallerNumberVerificationStatus(2);
        }
        if (verstatVerificationStatus != 0) {
            profile.setCallExtraInt("VerstatVerificationStatus", verstatVerificationStatus);
        }
        return profile;
    }
}
