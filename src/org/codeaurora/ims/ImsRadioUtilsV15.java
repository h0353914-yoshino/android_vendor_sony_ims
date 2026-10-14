package org.codeaurora.ims;

import android.telephony.ims.ImsException;
import com.qualcomm.ims.utils.Log;
import java.util.ArrayList;
import vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo;
import vendor.qti.hardware.radio.ims.V1_4.MultiIdentityLineInfoHal;
import vendor.qti.hardware.radio.ims.V1_5.AutoCallRejectionInfo;
import vendor.qti.hardware.radio.ims.V1_5.CallFailCause;
import vendor.qti.hardware.radio.ims.V1_5.CallInfo;
import vendor.qti.hardware.radio.ims.V1_5.ConfigInfo;

/* JADX INFO: loaded from: classes.dex */
public class ImsRadioUtilsV15 {
    private ImsRadioUtilsV15() {
    }

    public static int getCallFailCauseForImsReason(int imsReason) {
        switch (imsReason) {
            case 366:
                return CallFailCause.CALL_FAIL_SIP_METHOD_NOT_ALLOWED;
            case 367:
                return CallFailCause.CALL_FAIL_SIP_PROXY_AUTHENTICATION_REQUIRED;
            case 368:
                return CallFailCause.CALL_FAIL_SIP_REQUEST_ENTITY_TOO_LARGE;
            case 369:
                return CallFailCause.CALL_FAIL_SIP_REQUEST_URI_TOO_LARGE;
            case 370:
                return CallFailCause.CALL_FAIL_SIP_EXTENSION_REQUIRED;
            case 371:
                return CallFailCause.CALL_FAIL_SIP_INTERVAL_TOO_BRIEF;
            case 372:
                return CallFailCause.CALL_FAIL_SIP_CALL_OR_TRANS_DOES_NOT_EXIST;
            case 373:
                return CallFailCause.CALL_FAIL_SIP_LOOP_DETECTED;
            case 374:
                return CallFailCause.CALL_FAIL_SIP_TOO_MANY_HOPS;
            case 376:
                return CallFailCause.CALL_FAIL_SIP_AMBIGUOUS;
            case 377:
                return CallFailCause.CALL_FAIL_SIP_REQUEST_PENDING;
            case 378:
                return CallFailCause.CALL_FAIL_SIP_UNDECIPHERABLE;
            case 511:
                return CallFailCause.CALL_FAIL_USER_REJECTED_SESSION_MODIFICATION;
            case 512:
                return CallFailCause.CALL_FAIL_USER_CANCELLED_SESSION_MODIFICATION;
            case 1017:
                return CallFailCause.CALL_FAIL_REJECTED_ELSEWHERE;
            case 1517:
                return CallFailCause.CALL_FAIL_SESSION_MODIFICATION_FAILED;
            case 3001:
                return CallFailCause.CALL_FAIL_RETRY_ON_IMS_WITHOUT_RTT;
            default:
                Log.i(ImsRadioUtilsV15.class, "Unsupported imsReason for ending call. Passing end cause as 'misc'.");
                return 548;
        }
    }

    public static int getImsReasonForCallFailCause(int failCause) {
        switch (failCause) {
            case CallFailCause.CALL_FAIL_REJECTED_ELSEWHERE /* 603 */:
                return 1017;
            case CallFailCause.CALL_FAIL_USER_REJECTED_SESSION_MODIFICATION /* 604 */:
                return 511;
            case CallFailCause.CALL_FAIL_USER_CANCELLED_SESSION_MODIFICATION /* 605 */:
                return 512;
            case CallFailCause.CALL_FAIL_SESSION_MODIFICATION_FAILED /* 606 */:
                return 1517;
            case CallFailCause.CALL_FAIL_SIP_METHOD_NOT_ALLOWED /* 607 */:
                return 366;
            case CallFailCause.CALL_FAIL_SIP_PROXY_AUTHENTICATION_REQUIRED /* 608 */:
                return 367;
            case CallFailCause.CALL_FAIL_SIP_REQUEST_ENTITY_TOO_LARGE /* 609 */:
                return 368;
            case CallFailCause.CALL_FAIL_SIP_REQUEST_URI_TOO_LARGE /* 610 */:
                return 369;
            case CallFailCause.CALL_FAIL_SIP_EXTENSION_REQUIRED /* 611 */:
                return 370;
            case CallFailCause.CALL_FAIL_SIP_INTERVAL_TOO_BRIEF /* 612 */:
                return 371;
            case CallFailCause.CALL_FAIL_SIP_CALL_OR_TRANS_DOES_NOT_EXIST /* 613 */:
                return 372;
            case CallFailCause.CALL_FAIL_SIP_LOOP_DETECTED /* 614 */:
                return 373;
            case CallFailCause.CALL_FAIL_SIP_TOO_MANY_HOPS /* 615 */:
                return 374;
            case CallFailCause.CALL_FAIL_SIP_AMBIGUOUS /* 616 */:
                return 376;
            case CallFailCause.CALL_FAIL_SIP_REQUEST_PENDING /* 617 */:
                return 377;
            case CallFailCause.CALL_FAIL_SIP_UNDECIPHERABLE /* 618 */:
                return 378;
            case CallFailCause.CALL_FAIL_RETRY_ON_IMS_WITHOUT_RTT /* 619 */:
                return 3001;
            default:
                return 0;
        }
    }

    private static CallInfo migrateCallInfoFrom(vendor.qti.hardware.radio.ims.V1_4.CallInfo from) {
        CallInfo to = new CallInfo();
        to.state = from.state;
        to.index = from.index;
        to.toa = from.toa;
        to.hasIsMpty = from.hasIsMpty;
        to.isMpty = from.isMpty;
        to.hasIsMT = from.hasIsMT;
        to.isMT = from.isMT;
        to.als = from.als;
        to.hasIsVoice = from.hasIsVoice;
        to.isVoice = from.isVoice;
        to.hasIsVoicePrivacy = from.hasIsVoicePrivacy;
        to.isVoicePrivacy = from.isVoicePrivacy;
        to.number = from.number;
        to.numberPresentation = from.numberPresentation;
        to.name = from.name;
        to.namePresentation = from.namePresentation;
        to.hasCallDetails = from.hasCallDetails;
        to.callDetails.callType = from.callDetails.callType;
        to.callDetails.callDomain = from.callDetails.callDomain;
        to.callDetails.extrasLength = from.callDetails.extrasLength;
        for (String extra : from.callDetails.extras) {
            to.callDetails.extras.add(extra);
        }
        for (ServiceStatusInfo localAbility : from.callDetails.localAbility) {
            to.callDetails.localAbility.add(localAbility);
        }
        for (ServiceStatusInfo peerAbility : from.callDetails.peerAbility) {
            to.callDetails.peerAbility.add(peerAbility);
        }
        to.callDetails.callSubstate = from.callDetails.callSubstate;
        to.callDetails.mediaId = from.callDetails.mediaId;
        to.callDetails.causeCode = from.callDetails.causeCode;
        to.callDetails.rttMode = from.callDetails.rttMode;
        to.callDetails.sipAlternateUri = from.callDetails.sipAlternateUri;
        to.hasFailCause = from.hasFailCause;
        to.failCause.failCause = from.failCause.failCause;
        for (Byte errorinfo : from.failCause.errorinfo) {
            to.failCause.errorinfo.add(errorinfo);
        }
        to.failCause.networkErrorString = from.failCause.networkErrorString;
        to.failCause.hasErrorDetails = from.failCause.hasErrorDetails;
        to.failCause.errorDetails.errorCode = from.failCause.errorDetails.errorCode;
        to.failCause.errorDetails.errorString = from.failCause.errorDetails.errorString;
        to.hasIsEncrypted = from.hasIsEncrypted;
        to.isEncrypted = from.isEncrypted;
        to.hasIsCalledPartyRinging = from.hasIsCalledPartyRinging;
        to.isCalledPartyRinging = from.isCalledPartyRinging;
        to.historyInfo = from.historyInfo;
        to.hasIsVideoConfSupported = from.hasIsVideoConfSupported;
        to.isVideoConfSupported = from.isVideoConfSupported;
        migarateVerstatInfo(from.verstatInfo, to.verstatInfo);
        migrateMultiIdentityLineInfo(from.mtMultiLineInfo, to.mtMultiLineInfo);
        return to;
    }

    private static void migarateVerstatInfo(vendor.qti.hardware.radio.ims.V1_3.VerstatInfo from, vendor.qti.hardware.radio.ims.V1_3.VerstatInfo to) {
        to.canMarkUnwantedCall = from.canMarkUnwantedCall;
        to.verificationStatus = from.verificationStatus;
    }

    private static void migrateMultiIdentityLineInfo(MultiIdentityLineInfoHal from, MultiIdentityLineInfoHal to) {
        to.msisdn = from.msisdn;
        to.registrationStatus = from.registrationStatus;
        to.lineType = from.lineType;
    }

    public static AutoCallRejectionInfo migrateAutoCallRejectionInfoFrom(vendor.qti.hardware.radio.ims.V1_3.AutoCallRejectionInfo from) {
        AutoCallRejectionInfo to = new AutoCallRejectionInfo();
        to.callType = from.callType;
        to.autoRejectionCause = from.autoRejectionCause;
        to.sipErrorCode = from.sipErrorCode;
        to.number = from.number;
        return to;
    }

    public static ArrayList<CallInfo> migrateCallListFrom(ArrayList<vendor.qti.hardware.radio.ims.V1_4.CallInfo> callList) {
        if (callList == null) {
            return null;
        }
        ArrayList<CallInfo> list = new ArrayList<>();
        for (vendor.qti.hardware.radio.ims.V1_4.CallInfo from : callList) {
            CallInfo to = migrateCallInfoFrom(from);
            list.add(to);
        }
        return list;
    }

    public static int configInfoItemToHal(int item) {
        switch (item) {
            case 73:
                return 74;
            default:
                return ImsRadioUtils.configInfoItemToHal(item);
        }
    }

    public static int configInfoItemFromHal(int item) {
        switch (item) {
            case 74:
                return 73;
            default:
                return ImsRadioUtils.configInfoItemFromHal(item);
        }
    }

    public static ConfigInfo buildConfigInfo(int item, boolean boolValue, int intValue, String stringValue, int errorCause) {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.item = configInfoItemToHal(item);
        configInfo.hasBoolValue = true;
        configInfo.boolValue = boolValue;
        configInfo.intValue = intValue;
        if (stringValue != null) {
            configInfo.stringValue = stringValue;
        }
        configInfo.errorCause = ImsRadioUtils.configFailureCauseToHal(errorCause);
        return configInfo;
    }

    public static Boolean isConfigItemIntroducedInV15(int item) {
        switch (item) {
            case 74:
                return true;
            default:
                return false;
        }
    }

    public static ConfigInfo migrateConfigInfoFromV16(vendor.qti.hardware.radio.ims.V1_6.ConfigInfo from) throws ImsException {
        ConfigInfo to = new ConfigInfo();
        if (ImsRadioUtilsV16.isConfigItemIntroducedInV16(from.item)) {
            throw new ImsException("Config item not supported in current HAL");
        }
        to.item = from.item;
        to.hasBoolValue = from.hasBoolValue;
        to.boolValue = from.boolValue;
        to.intValue = from.intValue;
        if (from.stringValue != null) {
            to.stringValue = from.stringValue;
        }
        to.errorCause = ImsRadioUtils.configFailureCauseToHal(from.errorCause);
        return to;
    }

    public static int mapEmergencyServiceCategoryToHal(int categories) {
        int toHalCategories = 0;
        if ((categories & 1) == 1) {
            toHalCategories = 0 | 1;
        }
        if ((categories & 2) == 2) {
            toHalCategories |= 2;
        }
        if ((categories & 4) == 4) {
            toHalCategories |= 4;
        }
        if ((categories & 8) == 8) {
            toHalCategories |= 8;
        }
        if ((categories & 16) == 16) {
            toHalCategories |= 16;
        }
        if ((categories & 32) == 32) {
            toHalCategories |= 32;
        }
        if ((categories & 64) == 64) {
            return toHalCategories | 64;
        }
        return toHalCategories;
    }

    public static int mapEmergencyCallRoutingToHal(int routing) {
        switch (routing) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    public static int mapTirPresentationToIpPresentation(int presentation) {
        switch (presentation) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return 2;
        }
    }

    public static CallForwardStatusInfo callForwardStatusInfoFromHal(vendor.qti.hardware.radio.ims.V1_5.CallForwardStatusInfo imsRadioCFStatusInfo) {
        return new CallForwardStatusInfo(ImsRadioUtils.sipErrorFromHal(imsRadioCFStatusInfo.errorDetails), toCallForwardStatus(imsRadioCFStatusInfo.status));
    }

    private static CallForwardStatus[] toCallForwardStatus(ArrayList<vendor.qti.hardware.radio.ims.V1_5.CallForwardStatus> fromList) {
        if (fromList == null) {
            return null;
        }
        int size = fromList.size();
        CallForwardStatus[] toList = new CallForwardStatus[size];
        for (int i = 0; i < size; i++) {
            vendor.qti.hardware.radio.ims.V1_5.CallForwardStatus fromCfStatus = fromList.get(i);
            toList[i] = new CallForwardStatus(fromCfStatus.reason, fromCfStatus.status, ImsRadioUtils.sipErrorFromHal(fromCfStatus.errorDetails));
        }
        return toList;
    }

    public static int mapHidlToFrameworkResponseReason(int hidlReason) {
        switch (hidlReason) {
            case 8:
                return 6;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
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
            default:
                Log.e(ImsRadioUtilsV15.class, "Failure reason is unknown");
                return 1;
        }
    }

    public static int imsSmsDeliverStatusToHidl(int status) {
        switch (status) {
            case 3:
                return 2;
            case 4:
                return 3;
            default:
                Log.e(ImsRadioUtilsV15.class, "unknown deliver status");
                return 1;
        }
    }
}
