package org.codeaurora.ims;

import java.util.ArrayList;
import vendor.qti.hardware.radio.ims.V1_7.CallInfo;

/* JADX INFO: loaded from: classes.dex */
public class ImsRadioUtilsV17 {
    private ImsRadioUtilsV17() {
    }

    private static CallInfo migrateCallInfoFrom(vendor.qti.hardware.radio.ims.V1_6.CallInfo from) {
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
        to.callDetails = from.callDetails;
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
        to.verstatInfo = from.verstatInfo;
        to.mtMultiLineInfo = from.mtMultiLineInfo;
        to.tirMode = from.tirMode;
        return to;
    }

    public static ArrayList<CallInfo> migrateCallListFrom(ArrayList<vendor.qti.hardware.radio.ims.V1_6.CallInfo> callList) {
        if (callList == null) {
            return null;
        }
        ArrayList<CallInfo> list = new ArrayList<>();
        for (vendor.qti.hardware.radio.ims.V1_6.CallInfo from : callList) {
            CallInfo to = migrateCallInfoFrom(from);
            list.add(to);
        }
        return list;
    }
}
