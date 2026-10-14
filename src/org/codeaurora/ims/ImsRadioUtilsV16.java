package org.codeaurora.ims;

import android.net.Uri;
import android.telephony.ims.ImsReasonInfo;
import java.util.ArrayList;
import vendor.qti.hardware.radio.ims.V1_0.StatusForAccessTech;
import vendor.qti.hardware.radio.ims.V1_4.MultiIdentityLineInfoHal;
import vendor.qti.hardware.radio.ims.V1_6.AutoCallRejectionInfo;
import vendor.qti.hardware.radio.ims.V1_6.CallInfo;
import vendor.qti.hardware.radio.ims.V1_6.CallLocation;
import vendor.qti.hardware.radio.ims.V1_6.ConfigInfo;
import vendor.qti.hardware.radio.ims.V1_6.ConfigItem;
import vendor.qti.hardware.radio.ims.V1_6.DialRequest;
import vendor.qti.hardware.radio.ims.V1_6.HandoverInfo;
import vendor.qti.hardware.radio.ims.V1_6.RegistrationInfo;
import vendor.qti.hardware.radio.ims.V1_6.ServiceStatusInfo;

/* JADX INFO: loaded from: classes.dex */
public class ImsRadioUtilsV16 {
    private ImsRadioUtilsV16() {
    }

    public static ImsConfigItem configInfoFromHal(ConfigInfo configInfo) {
        if (configInfo == null) {
            return null;
        }
        ImsConfigItem config = new ImsConfigItem();
        config.setItem(configInfoItemFromHal(configInfo.item));
        if (configInfo.hasBoolValue) {
            config.setBoolValue(configInfo.boolValue);
        }
        if (configInfo.intValue != Integer.MAX_VALUE) {
            config.setIntValue(configInfo.intValue);
        }
        config.setStringValue(configInfo.stringValue);
        if (configInfo.errorCause != 6) {
            config.setErrorCause(ImsRadioUtils.configFailureCauseFromHal(configInfo.errorCause));
        }
        return config;
    }

    public static int configInfoItemFromHal(int item) {
        switch (item) {
            case 75:
                return 74;
            case ConfigItem.CONFIG_ITEM_VOWIFI_FQDN /* 76 */:
            default:
                return ImsRadioUtilsV15.configInfoItemFromHal(item);
            case ConfigItem.CONFIG_ITEM_MMTEL_CALL_COMPOSER_CONFIG /* 77 */:
                return 75;
        }
    }

    public static int configInfoItemToHal(int item) {
        switch (item) {
            case 74:
                return 75;
            case 75:
                return 77;
            default:
                return ImsRadioUtilsV15.configInfoItemToHal(item);
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

    public static boolean isConfigItemIntroducedInV16(int item) {
        switch (item) {
            case 75:
            case ConfigItem.CONFIG_ITEM_MMTEL_CALL_COMPOSER_CONFIG /* 77 */:
                return true;
            case ConfigItem.CONFIG_ITEM_VOWIFI_FQDN /* 76 */:
            default:
                return false;
        }
    }

    public static ConfigInfo migrateConfigInfoFrom(vendor.qti.hardware.radio.ims.V1_0.ConfigInfo from) {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.item = from.item;
        configInfo.hasBoolValue = from.hasBoolValue;
        configInfo.boolValue = from.boolValue;
        configInfo.intValue = from.intValue;
        if (from.stringValue != null) {
            configInfo.stringValue = from.stringValue;
        }
        configInfo.errorCause = from.errorCause;
        return configInfo;
    }

    private static CallInfo migrateCallInfoFrom(vendor.qti.hardware.radio.ims.V1_5.CallInfo from) {
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
        to.callDetails = migrateCallDetails(from.callDetails);
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
        to.tirMode = from.tirMode;
        return to;
    }

    public static vendor.qti.hardware.radio.ims.V1_6.CallDetails migrateCallDetails(vendor.qti.hardware.radio.ims.V1_0.CallDetails from) {
        if (from == null) {
            return null;
        }
        vendor.qti.hardware.radio.ims.V1_6.CallDetails to = new vendor.qti.hardware.radio.ims.V1_6.CallDetails();
        to.callType = from.callType;
        to.callDomain = from.callDomain;
        to.extrasLength = from.extrasLength;
        for (String extra : from.extras) {
            to.extras.add(extra);
        }
        migrateServiceStatusInfo(from.localAbility, to.localAbility);
        migrateServiceStatusInfo(from.peerAbility, to.peerAbility);
        to.callSubstate = from.callSubstate;
        to.mediaId = from.mediaId;
        to.causeCode = from.causeCode;
        to.rttMode = from.rttMode;
        to.sipAlternateUri = from.sipAlternateUri;
        return to;
    }

    public static ArrayList<ServiceStatusInfo> migrateServiceStatusInfo(ArrayList<vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo> from) {
        if (from == null || from.isEmpty()) {
            return null;
        }
        ArrayList<ServiceStatusInfo> to = new ArrayList<>(from.size());
        migrateServiceStatusInfo(from, to);
        return to;
    }

    public static void migrateServiceStatusInfo(ArrayList<vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo> from, ArrayList<ServiceStatusInfo> to) {
        if (from == null || from.isEmpty()) {
            return;
        }
        for (vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo info : from) {
            to.add(migrateServiceStatusInfo(info));
        }
    }

    private static ServiceStatusInfo migrateServiceStatusInfo(vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo from) {
        ServiceStatusInfo to = new ServiceStatusInfo();
        to.hasIsValid = from.hasIsValid;
        to.isValid = from.isValid;
        to.type = from.type;
        to.callType = from.callType;
        to.status = from.status;
        for (Byte data : from.userdata) {
            to.userdata.add(data);
        }
        migrateAccTechStatus(from.accTechStatus, to.accTechStatus);
        to.restrictCause = from.restrictCause;
        to.rttMode = from.rttMode;
        return to;
    }

    private static void migrateAccTechStatus(ArrayList<StatusForAccessTech> from, ArrayList<vendor.qti.hardware.radio.ims.V1_6.StatusForAccessTech> to) {
        for (StatusForAccessTech status : from) {
            to.add(migrateAccTechStatus(status));
        }
    }

    private static vendor.qti.hardware.radio.ims.V1_6.StatusForAccessTech migrateAccTechStatus(StatusForAccessTech from) {
        vendor.qti.hardware.radio.ims.V1_6.StatusForAccessTech to = new vendor.qti.hardware.radio.ims.V1_6.StatusForAccessTech();
        to.networkMode = from.networkMode;
        to.status = from.status;
        to.restrictCause = from.restrictCause;
        to.hasRegistration = from.hasRegistration;
        to.registration = migrateRegistrationInfo(from.registration);
        return to;
    }

    public static RegistrationInfo migrateRegistrationInfo(vendor.qti.hardware.radio.ims.V1_0.RegistrationInfo from) {
        if (from == null) {
            return null;
        }
        RegistrationInfo to = new RegistrationInfo();
        to.state = from.state;
        to.errorCode = from.errorCode;
        to.errorMessage = from.errorMessage;
        to.radioTech = from.radioTech;
        to.pAssociatedUris = from.pAssociatedUris;
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

    public static ArrayList<CallInfo> migrateCallListFrom(ArrayList<vendor.qti.hardware.radio.ims.V1_5.CallInfo> callList) {
        if (callList == null) {
            return null;
        }
        ArrayList<CallInfo> list = new ArrayList<>();
        for (vendor.qti.hardware.radio.ims.V1_5.CallInfo from : callList) {
            CallInfo to = migrateCallInfoFrom(from);
            list.add(to);
        }
        return list;
    }

    public static HandoverInfo migrateHandoverInfo(vendor.qti.hardware.radio.ims.V1_0.HandoverInfo from) {
        if (from == null) {
            return null;
        }
        HandoverInfo to = new HandoverInfo();
        to.type = from.type;
        to.srcTech = from.srcTech;
        to.targetTech = from.targetTech;
        to.hasHoExtra = from.hasHoExtra;
        to.hoExtra = from.hoExtra;
        to.errorCode = from.errorCode;
        to.errorMessage = from.errorMessage;
        return to;
    }

    public static vendor.qti.hardware.radio.ims.V1_6.CallComposerInfo buildCallComposerInfoHal(CallComposerInfo from) {
        if (from == null) {
            return null;
        }
        vendor.qti.hardware.radio.ims.V1_6.CallComposerInfo info = new vendor.qti.hardware.radio.ims.V1_6.CallComposerInfo();
        info.priority = convertToHalPriority(from.getPriority());
        if (from.getSubject() != null) {
            String subject = from.getSubject();
            for (int i = 0; i < subject.length(); i++) {
                info.subject.add(Short.valueOf((short) subject.charAt(i)));
            }
        }
        if (from.getLocation() != null) {
            info.location = buildLocationHal(from.getLocation());
        }
        if (from.getImageUrl() != null) {
            info.imageUrl = from.getImageUrl().toString();
        }
        return info;
    }

    private static int convertToHalPriority(int priority) {
        switch (priority) {
            case 0:
                return 0;
            default:
                return 1;
        }
    }

    private static CallLocation buildLocationHal(CallComposerInfo.Location from) {
        CallLocation location = new CallLocation();
        location.radius = from.getRadius();
        location.latitude = from.getLatitude();
        location.longitude = from.getLongitude();
        return location;
    }

    public static DialRequest buildDialRequest(String address, int clirMode, CallDetails callDetails, boolean isEncrypted, RedialInfo redialInfo) {
        DialRequest dialRequest = new DialRequest();
        if (address != null) {
            dialRequest.address = address;
        }
        dialRequest.clirMode = clirMode;
        dialRequest.presentation = ImsRadioUtils.getIpPresentation(clirMode);
        if (callDetails != null) {
            dialRequest.hasCallDetails = true;
            callDetailsToHal(dialRequest.callDetails, callDetails);
        }
        boolean isConferenceUri = ImsRadioUtils.getIsConferenceUri(callDetails);
        if (isConferenceUri) {
            dialRequest.isConferenceUri = isConferenceUri;
            dialRequest.hasIsConferenceUri = isConferenceUri;
        }
        boolean isCallPull = callDetails != null ? callDetails.getCallPull() : false;
        if (isCallPull) {
            dialRequest.isCallPull = isCallPull;
            dialRequest.hasIsCallPull = isCallPull;
        }
        dialRequest.hasIsEncrypted = true;
        dialRequest.isEncrypted = isEncrypted;
        MultiIdentityLineInfo multiIdentityLineInfo = callDetails != null ? callDetails.getMultiIdentityLineInfo() : null;
        ImsRadioUtilsV14.toMultiIdentityLineInfoHal(multiIdentityLineInfo, dialRequest.multiLineInfo);
        if (redialInfo == null) {
            redialInfo = new RedialInfo(0, 0);
        }
        dialRequest.redialInfo.callFailReason = ImsRadioUtilsV15.getCallFailCauseForImsReason(redialInfo.getRetryCallFailCause());
        dialRequest.redialInfo.callFailRadioTech = ImsRadioUtils.mapRadioTechToHal(redialInfo.getRetryCallFailRadioTech());
        return dialRequest;
    }

    public static void callDetailsToHal(vendor.qti.hardware.radio.ims.V1_6.CallDetails imsRadioCallDetails, CallDetails callDetails) {
        imsRadioCallDetails.callType = ImsRadioUtils.callTypeToHal(callDetails.call_type);
        imsRadioCallDetails.callDomain = ImsRadioUtils.callDomainToHal(callDetails.call_domain);
        if (2 != callDetails.getRttMode()) {
            imsRadioCallDetails.rttMode = callDetails.getRttMode();
        } else {
            imsRadioCallDetails.rttMode = 0;
        }
        imsRadioCallDetails.extrasLength = callDetails.extras != null ? callDetails.extras.length : 0;
        for (int i = 0; i < imsRadioCallDetails.extrasLength; i++) {
            imsRadioCallDetails.extras.add(i, callDetails.extras[i]);
        }
    }

    public static int callTypeFromHal(int callType) {
        switch (callType) {
            case 13:
                return 27;
            case 14:
                return 26;
            default:
                return ImsRadioUtils.callTypeFromHal(callType);
        }
    }

    public static CallComposerInfo buildCallComposerInfoFromHal(vendor.qti.hardware.radio.ims.V1_6.CallComposerInfo from) {
        if (from == null) {
            return null;
        }
        int priority = convertPriorityFromHal(from.priority);
        ArrayList<Short> subjectHal = from.subject;
        StringBuilder subject = new StringBuilder();
        for (int i = 0; i < subjectHal.size(); i++) {
            subject.append((char) subjectHal.get(i).shortValue());
        }
        CallComposerInfo.Location location = CallComposerInfo.Location.UNKNOWN;
        if (from.location.radius != -1.0f) {
            location = buildLocationFromHal(from.location);
        }
        Uri imageUrl = Uri.parse(from.imageUrl);
        return new CallComposerInfo(priority, subject.toString(), imageUrl, location);
    }

    private static CallComposerInfo.Location buildLocationFromHal(CallLocation from) {
        CallComposerInfo.Location location = new CallComposerInfo.Location(from.radius, from.latitude, from.longitude);
        return location;
    }

    private static int convertPriorityFromHal(int priority) {
        switch (priority) {
            case 0:
                return 0;
            default:
                return 1;
        }
    }

    public static DriverCallIms toDriverCallIms(AutoCallRejectionInfo rejectInfo, vendor.qti.hardware.radio.ims.V1_6.CallComposerInfo callComposerInfo) {
        CallComposerInfo ccInfo;
        VerstatInfo verstatInfo = new VerstatInfo(false, rejectInfo.verificationStatus);
        if (callComposerInfo == null) {
            ccInfo = null;
        } else {
            ccInfo = buildCallComposerInfoFromHal(callComposerInfo);
        }
        DriverCallIms dc = new DriverCallIms(ccInfo, verstatInfo);
        int imsReasonInfoCode = ImsRadioUtilsV13.getImsReasonForCallFailCause(rejectInfo.autoRejectionCause);
        dc.callFailCause = new ImsReasonInfo(imsReasonInfoCode == 0 ? 1600 : imsReasonInfoCode, rejectInfo.sipErrorCode, null);
        dc.callDetails = new CallDetails();
        dc.callDetails.call_type = ImsRadioUtils.callTypeFromHal(rejectInfo.callType);
        dc.number = rejectInfo.number;
        return dc;
    }

    public static AutoCallRejectionInfo migrateAutoCallRejectionInfoFrom(vendor.qti.hardware.radio.ims.V1_5.AutoCallRejectionInfo from) {
        AutoCallRejectionInfo to = new AutoCallRejectionInfo();
        to.callType = from.callType;
        to.autoRejectionCause = from.autoRejectionCause;
        to.sipErrorCode = from.sipErrorCode;
        to.number = from.number;
        return to;
    }

    public static int geoLocationDataStatusFromHal(int geoLocationDataStatus) {
        switch (geoLocationDataStatus) {
            case 0:
                return 2000;
            case 1:
                return 2001;
            case 2:
                return 2002;
            case 3:
                return 2003;
            default:
                return 2003;
        }
    }
}
