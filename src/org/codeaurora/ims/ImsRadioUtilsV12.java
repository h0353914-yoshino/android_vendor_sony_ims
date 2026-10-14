package org.codeaurora.ims;

import java.util.ArrayList;
import vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo;
import vendor.qti.hardware.radio.ims.V1_2.CallInfo;

/* JADX INFO: loaded from: classes.dex */
public class ImsRadioUtilsV12 {
    private static int migrateCallFailCauseFromV11(int callFailCause) {
        if (callFailCause == 549) {
            return 549;
        }
        return callFailCause;
    }

    public static int getImsReasonForCallFailCause(int failCause) {
        switch (failCause) {
            case 551:
                return 1500;
            case 552:
                return 121;
            case 553:
                return 1501;
            case 554:
                return 1502;
            case 555:
                return 1503;
            case 556:
                return 1504;
            case 557:
                return 1505;
            case 558:
                return 1506;
            case 559:
                return 1507;
            case 560:
                return 1508;
            case 561:
                return 1509;
            case 562:
                return 1510;
            case 563:
                return 1511;
            case 564:
                return 1513;
            case 565:
                return 61441;
            case 566:
                return 61442;
            case 567:
                return 61443;
            case 568:
                return 61444;
            case 569:
                return 61445;
            case 570:
                return 61446;
            case 571:
                return 61447;
            case 572:
                return 61448;
            case 573:
                return 61449;
            case 574:
                return 61450;
            case 575:
                return 61451;
            case 576:
                return 61452;
            case 577:
                return 61453;
            case 578:
                return 61454;
            case 579:
                return 61455;
            case 580:
                return 1516;
            default:
                return ImsRadioUtilsV13.getImsReasonForCallFailCause(failCause);
        }
    }

    private static CallInfo migrateCallInfoFromV11(vendor.qti.hardware.radio.ims.V1_1.CallInfo from) {
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
        to.failCause.failCause = migrateCallFailCauseFromV11(from.failCause.failCause);
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

    public static ArrayList<CallInfo> migrateCallListFromV11(ArrayList<vendor.qti.hardware.radio.ims.V1_1.CallInfo> callList) {
        if (callList == null) {
            return null;
        }
        ArrayList<CallInfo> list = new ArrayList<>();
        for (vendor.qti.hardware.radio.ims.V1_1.CallInfo from : callList) {
            CallInfo to = migrateCallInfoFromV11(from);
            list.add(to);
        }
        return list;
    }
}
