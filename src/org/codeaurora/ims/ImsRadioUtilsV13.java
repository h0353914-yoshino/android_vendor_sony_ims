package org.codeaurora.ims;

import java.util.ArrayList;
import vendor.qti.hardware.radio.ims.V1_0.CbNumInfo;
import vendor.qti.hardware.radio.ims.V1_0.CbNumListInfo;
import vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo;
import vendor.qti.hardware.radio.ims.V1_3.CallInfo;
import vendor.qti.hardware.radio.ims.V1_3.ColrInfo;
import vendor.qti.hardware.radio.ims.V1_3.SuppServiceStatus;

/* JADX INFO: loaded from: classes.dex */
public class ImsRadioUtilsV13 {
    private ImsRadioUtilsV13() {
    }

    public static VerstatInfo createVerstatInfo(vendor.qti.hardware.radio.ims.V1_3.VerstatInfo verstatInfo) {
        return new VerstatInfo(verstatInfo.canMarkUnwantedCall, verstatInfo.verificationStatus);
    }

    public static int getCallFailCauseForImsReason(int imsReason) {
        switch (imsReason) {
            case 365:
                return 602;
            default:
                return ImsRadioUtilsV14.getCallFailCauseForImsReason(imsReason);
        }
    }

    public static Boolean isFailCauseIntroducedInV13(int failCause) {
        switch (failCause) {
            case 581:
            case 582:
            case 583:
            case 584:
            case 585:
            case 586:
            case 587:
            case 588:
            case 589:
            case 590:
            case 591:
            case 592:
            case 593:
            case 594:
            case 595:
            case 596:
            case 597:
            case 598:
            case 599:
            case 600:
            case 601:
            case 602:
                return true;
            default:
                return false;
        }
    }

    private static int migrateCallFailCauseFromV12(int callFailCause) {
        if (callFailCause == 549) {
            return 549;
        }
        return callFailCause;
    }

    public static int getImsReasonForCallFailCause(int failCause) {
        switch (failCause) {
            case 581:
                return 1604;
            case 582:
                return 1605;
            case 583:
                return 1606;
            case 584:
                return 1607;
            case 585:
                return 1608;
            case 586:
                return 1609;
            case 587:
                return 1611;
            case 588:
                return 1612;
            case 589:
                return 1613;
            case 590:
                return 1614;
            case 591:
                return 1615;
            case 592:
                return 1616;
            case 593:
                return 1617;
            case 594:
                return 1618;
            case 595:
                return 1619;
            case 596:
                return 1620;
            case 597:
                return 1601;
            case 598:
                return 1602;
            case 599:
                return 1603;
            case 600:
                return 1612;
            case 601:
                return 1621;
            default:
                return ImsRadioUtilsV15.getImsReasonForCallFailCause(failCause);
        }
    }

    private static CallInfo migrateCallInfoFromV12(vendor.qti.hardware.radio.ims.V1_2.CallInfo from) {
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
        to.failCause.failCause = migrateCallFailCauseFromV12(from.failCause.failCause);
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
        return to;
    }

    public static ArrayList<CallInfo> migrateCallListFromV12(ArrayList<vendor.qti.hardware.radio.ims.V1_2.CallInfo> callList) {
        if (callList == null) {
            return null;
        }
        ArrayList<CallInfo> list = new ArrayList<>();
        for (vendor.qti.hardware.radio.ims.V1_2.CallInfo from : callList) {
            CallInfo to = migrateCallInfoFromV12(from);
            list.add(to);
        }
        return list;
    }

    public static ColrInfo migrateColrInfoFromV10(vendor.qti.hardware.radio.ims.V1_0.ColrInfo from) {
        ColrInfo to = new ColrInfo();
        to.status = 2;
        to.provisionStatus = 2;
        to.presentation = from.presentation;
        to.hasErrorDetails = from.hasErrorDetails;
        to.errorDetails.errorCode = from.errorDetails.errorCode;
        to.errorDetails.errorString = from.errorDetails.errorString;
        return to;
    }

    public static SuppServiceStatus migrateSuppServiceStatusFromV10(vendor.qti.hardware.radio.ims.V1_0.SuppServiceStatus from) {
        SuppServiceStatus to = new SuppServiceStatus();
        to.status = from.status;
        to.provisionStatus = 2;
        to.facilityType = from.facilityType;
        to.failureCause = from.failureCause;
        for (CbNumListInfo numList : from.cbNumListInfo) {
            CbNumListInfo info = new CbNumListInfo();
            info.serviceClass = numList.serviceClass;
            for (CbNumInfo numInfo : numList.cbNumInfo) {
                info.cbNumInfo.add(numInfo);
            }
            to.cbNumListInfo.add(info);
        }
        to.hasErrorDetails = from.hasErrorDetails;
        to.errorDetails.errorCode = from.errorDetails.errorCode;
        to.errorDetails.errorString = from.errorDetails.errorString;
        return to;
    }
}
