package org.codeaurora.ims;

import android.location.Address;
import android.telephony.PhoneNumberUtils;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsReasonInfo;
import com.qualcomm.ims.utils.Log;
import java.util.ArrayList;
import java.util.Arrays;
import org.codeaurora.ims.sms.IncomingSms;
import org.codeaurora.ims.sms.SmsResponse;
import org.codeaurora.ims.sms.StatusReport;
import vendor.qti.hardware.radio.ims.V1_0.BlockReasonDetails;
import vendor.qti.hardware.radio.ims.V1_0.BlockStatus;
import vendor.qti.hardware.radio.ims.V1_0.CallForwardInfo;
import vendor.qti.hardware.radio.ims.V1_0.CallFwdTimerInfo;
import vendor.qti.hardware.radio.ims.V1_0.CallModifyInfo;
import vendor.qti.hardware.radio.ims.V1_0.CbNumInfo;
import vendor.qti.hardware.radio.ims.V1_0.CbNumListInfo;
import vendor.qti.hardware.radio.ims.V1_0.ConfigInfo;
import vendor.qti.hardware.radio.ims.V1_0.ExplicitCallTransferInfo;
import vendor.qti.hardware.radio.ims.V1_0.ImsSubConfigInfo;
import vendor.qti.hardware.radio.ims.V1_0.MessageDetails;
import vendor.qti.hardware.radio.ims.V1_0.MessageSummary;
import vendor.qti.hardware.radio.ims.V1_0.MessageWaitingIndication;
import vendor.qti.hardware.radio.ims.V1_0.ParticipantStatusInfo;
import vendor.qti.hardware.radio.ims.V1_0.SipErrorInfo;
import vendor.qti.hardware.radio.ims.V1_0.SuppServiceNotification;
import vendor.qti.hardware.radio.ims.V1_2.AddressInfo;
import vendor.qti.hardware.radio.ims.V1_2.ImsSmsMessage;
import vendor.qti.hardware.radio.ims.V1_2.ImsSmsSendStatusReport;
import vendor.qti.hardware.radio.ims.V1_2.IncomingImsSms;
import vendor.qti.hardware.radio.ims.V1_3.HangupRequestInfo;
import vendor.qti.hardware.radio.ims.V1_3.SuppServiceStatus;
import vendor.qti.hardware.radio.ims.V1_4.DialRequest;
import vendor.qti.hardware.radio.ims.V1_6.HandoverInfo;
import vendor.qti.hardware.radio.ims.V1_6.RegistrationInfo;
import vendor.qti.hardware.radio.ims.V1_6.ServiceStatusInfo;
import vendor.qti.hardware.radio.ims.V1_6.StatusForAccessTech;
import vendor.qti.hardware.radio.ims.V1_7.CallInfo;

/* JADX INFO: loaded from: classes.dex */
public class ImsRadioUtils {
    private static final int INVALID_CONNID = 0;
    private static String TAG = "ImsRadioUtils";

    public static void callDetailsToHal(vendor.qti.hardware.radio.ims.V1_0.CallDetails imsRadioCallDetails, CallDetails callDetails) {
        imsRadioCallDetails.callType = callTypeToHal(callDetails.call_type);
        imsRadioCallDetails.callDomain = callDomainToHal(callDetails.call_domain);
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

    public static int getIpPresentation(int clirMode) {
        switch (clirMode) {
            case 0:
            case 1:
                return 0;
            case 2:
                return 1;
            default:
                return 3;
        }
    }

    public static boolean getIsConferenceUri(CallDetails callDetails) {
        String value;
        if (callDetails == null || callDetails.extras == null || (value = callDetails.getValueForKeyFromExtras(callDetails.extras, CallDetails.EXTRAS_IS_CONFERENCE_URI)) == null || !Boolean.valueOf(value).booleanValue()) {
            return false;
        }
        return true;
    }

    public static DriverCallIms buildDriverCallImsFromHal(CallInfo call) {
        int i;
        DriverCallIms dc = new DriverCallIms(ImsRadioUtilsV13.createVerstatInfo(call.verstatInfo));
        if (call.state != 7) {
            dc.state = callStateFromHal(call.state);
        }
        if (call.index != Integer.MAX_VALUE) {
            dc.index = call.index;
        }
        if (call.toa != Integer.MAX_VALUE) {
            dc.TOA = call.toa;
        }
        if (call.hasIsMpty) {
            dc.isMpty = call.isMpty;
        }
        if (call.hasIsMT) {
            dc.isMT = call.isMT;
        }
        if (call.als != Integer.MAX_VALUE) {
            dc.als = call.als;
        }
        if (call.hasIsVoice) {
            dc.isVoice = call.isVoice;
        }
        if (call.hasIsVoicePrivacy) {
            dc.isVoicePrivacy = call.isVoicePrivacy;
        }
        dc.numberPresentation = DriverCallIms.presentationFromCLIP(call.numberPresentation);
        dc.name = call.name;
        dc.namePresentation = DriverCallIms.presentationFromCLIP(call.namePresentation);
        if (call.hasIsEncrypted) {
            dc.isEncrypted = call.isEncrypted;
        }
        dc.isTirOverwriteAllowed = isTirOverwriteAllowed(call.tirMode);
        crsDataFromHal(call.crsData, dc.crsData);
        dc.isPreparatory = call.isPreparatory;
        dc.historyInfo = call.historyInfo;
        if (call.hasIsVideoConfSupported) {
            if (call.isVideoConfSupported) {
                i = 2;
            } else {
                i = 0;
            }
            dc.mConfSupported = i | 1;
        }
        if (call.hasCallDetails) {
            dc.callDetails = getCallDetails(call.callDetails);
        }
        dc.number = call.number;
        dc.number = PhoneNumberUtils.stringFromStringAndTOA(dc.number, dc.TOA);
        callProgressInfoFromHal(call.callProgInfo, dc.callProgressInfo);
        int imsReasonCode = 0;
        int imsReasonExtraCode = 0;
        String imsReasonExtraMessage = null;
        if (call.hasFailCause) {
            String networkError = null;
            if (call.failCause.hasErrorDetails) {
                networkError = call.failCause.errorDetails.errorString;
            }
            int failCause = call.failCause.failCause;
            if (failCause != 549) {
                imsReasonCode = getImsReasonForCallFailCause(failCause);
                if (call.failCause.hasErrorDetails && call.failCause.errorDetails.errorCode != Integer.MAX_VALUE) {
                    imsReasonExtraCode = call.failCause.errorDetails.errorCode;
                } else {
                    log("CallFailCauseResponse has no int error code!");
                }
                dc.mCallFailReason = failCause;
                if (networkError != null) {
                    imsReasonExtraMessage = networkError;
                }
            } else {
                log("CallFailCauseResponse failCause is Invalid");
            }
        }
        dc.callFailCause = new ImsReasonInfo(imsReasonCode, imsReasonExtraCode, imsReasonExtraMessage);
        dc.mMtMultiLineInfo = ImsRadioUtilsV14.fromMultiIdentityLineInfoHal(call.mtMultiLineInfo);
        return dc;
    }

    public static ServiceStatusInfo buildServiceStatusInfo(int srvType, int rat, int enabled, int restrictCause) {
        Log.i(TAG, "buildServiceStatusInfo srvType = " + srvType + " rat = " + rat + " enabled = " + enabled);
        StatusForAccessTech statusForAccessTech = new StatusForAccessTech();
        statusForAccessTech.networkMode = mapRadioTechToHal(rat);
        statusForAccessTech.status = statusTypeToHal(enabled);
        statusForAccessTech.restrictCause = restrictCause;
        statusForAccessTech.hasRegistration = false;
        ServiceStatusInfo serviceStatusInfo = new ServiceStatusInfo();
        serviceStatusInfo.hasIsValid = true;
        serviceStatusInfo.isValid = true;
        serviceStatusInfo.callType = callTypeToHal(srvType);
        serviceStatusInfo.accTechStatus.add(statusForAccessTech);
        return serviceStatusInfo;
    }

    public static ArrayList<ServiceStatusInfo> buildServiceStatusInfoList(ArrayList<CapabilityStatus> capabilityStatusList, int restrictCause) {
        ArrayList<ServiceStatusInfo> serviceStatusInfoList = new ArrayList<>();
        for (CapabilityStatus capabilityStatus : capabilityStatusList) {
            Log.v(TAG, "buildServiceStatusInfoList capabilityStatus = " + capabilityStatus);
            ServiceStatusInfo serviceStatusInfo = buildServiceStatusInfo(mapCapabilityToSrvType(capabilityStatus.getCapability()), mapRadioTechToHidlRadioTech(capabilityStatus.getRadioTech()), mapValueToServiceStatus(capabilityStatus.getStatus()), restrictCause);
            serviceStatusInfoList.add(serviceStatusInfo);
        }
        return serviceStatusInfoList;
    }

    private static int getImsReasonForCallFailCause(int failCause) {
        log("Call fail cause= " + failCause);
        switch (failCause) {
            case 1:
                return 501;
            case 2:
                return 338;
            case 7:
                return 241;
            case 8:
                return 243;
            case 9:
                return 244;
            case 10:
                return 245;
            case 11:
                return 246;
            case 12:
                return 363;
            case 13:
                return 364;
            case 14:
                return 149;
            case 503:
                return 505;
            case 505:
                return 146;
            case 506:
                return 336;
            case 510:
                return 1014;
            case 511:
                return 1015;
            case 512:
                return 1016;
            case 513:
                return 1512;
            case 514:
                return 321;
            case 515:
                return 331;
            case 516:
                return 332;
            case 517:
                return 333;
            case 518:
                return 334;
            case 519:
                return 335;
            case 520:
                return 336;
            case 521:
                return 337;
            case 522:
                return 338;
            case 523:
                return 339;
            case 524:
                return 340;
            case 525:
                return 341;
            case 526:
                return 351;
            case 527:
            case 528:
            case 531:
            case 532:
            case 533:
                return 354;
            case 529:
                return 352;
            case 530:
                return 353;
            case 534:
                return 361;
            case 535:
                return 362;
            case 536:
                return 401;
            case 537:
                return 402;
            case 538:
                return ImsCallSessionImpl.SIP_FORBIDDEN;
            case 539:
                return 404;
            case 543:
                return 247;
            case 544:
                return 248;
            case 545:
                return 249;
            case 546:
                return 250;
            case 547:
                return 251;
            case 550:
                return 1514;
            default:
                return ImsRadioUtilsV12.getImsReasonForCallFailCause(failCause);
        }
    }

    public static CallDetails getCallDetails(vendor.qti.hardware.radio.ims.V1_6.CallDetails inCallDetails) {
        CallDetails outCallDetails = new CallDetails();
        if (inCallDetails.callType != 12) {
            outCallDetails.call_type = callTypeFromHal(inCallDetails.callType);
        }
        if (inCallDetails.callDomain != 5) {
            outCallDetails.call_domain = callDomainFromHal(inCallDetails.callDomain);
        }
        if (inCallDetails.callSubstate != Integer.MAX_VALUE) {
            outCallDetails.callsubstate = toCallSubstateConstants(inCallDetails.callSubstate);
        }
        if (inCallDetails.mediaId != Integer.MAX_VALUE) {
            outCallDetails.callMediaId = inCallDetails.mediaId;
        }
        outCallDetails.extras = new String[inCallDetails.extras.size()];
        outCallDetails.extras = (String[]) inCallDetails.extras.toArray(outCallDetails.extras);
        outCallDetails.localAbility = copySrvStatusList(inCallDetails.localAbility);
        outCallDetails.peerAbility = copySrvStatusList(inCallDetails.peerAbility);
        if (inCallDetails.causeCode != Integer.MAX_VALUE) {
            outCallDetails.causeCode = inCallDetails.causeCode;
        }
        if (inCallDetails.rttMode != 2) {
            outCallDetails.rttMode = inCallDetails.rttMode;
        }
        if (!inCallDetails.sipAlternateUri.isEmpty()) {
            outCallDetails.sipAlternateUri = inCallDetails.sipAlternateUri;
        }
        log("Call Details = " + outCallDetails);
        return outCallDetails;
    }

    private static byte[] getSmsPdu(ArrayList<Byte> hidlPdu) {
        byte[] pdu = new byte[hidlPdu.size()];
        for (int i = 0; i < pdu.length; i++) {
            pdu[i] = hidlPdu.get(i).byteValue();
        }
        return pdu;
    }

    public static StatusReport statusReportfromHidl(ImsSmsSendStatusReport report) {
        return new StatusReport(report.messageRef, report.format, getSmsPdu(report.pdu));
    }

    private static int mapHidlToFrameworkResponseResult(int hidlResult) {
        switch (hidlResult) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                return 2;
        }
    }

    private static int mapHidlToFrameworkResponseReason(int hidlReason) {
        switch (hidlReason) {
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
            case 5:
                return 5;
            case 6:
                return 7;
            case 7:
                return 8;
            default:
                return ImsRadioUtilsV15.mapHidlToFrameworkResponseReason(hidlReason);
        }
    }

    public static SmsResponse imsSmsResponsefromHidl(int messageRef, int smsStatusResult, int hidlReason) {
        int statusResult = mapHidlToFrameworkResponseResult(smsStatusResult);
        int reason = mapHidlToFrameworkResponseReason(hidlReason);
        return new SmsResponse(messageRef, statusResult, reason);
    }

    private static int mapHidlToFrameworkVerstat(int verstat) {
        switch (verstat) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    public static IncomingSms incomingSmsfromHidl(IncomingImsSms imsSms) {
        int verstat = mapHidlToFrameworkVerstat(imsSms.verstat);
        return new IncomingSms(imsSms.format, getSmsPdu(imsSms.pdu), verstat);
    }

    private static ServiceStatus[] copySrvStatusList(ArrayList<ServiceStatusInfo> fromList) {
        ServiceStatus[] toList = null;
        if (fromList != null) {
            int listLen = fromList.size();
            log("Num of SrvUpdates = " + listLen);
            toList = new ServiceStatus[listLen];
            for (int i = 0; i < listLen; i++) {
                ServiceStatusInfo fromInfo = fromList.get(i);
                if (fromInfo != null) {
                    toList[i] = new ServiceStatus();
                    if (fromInfo.hasIsValid) {
                        toList[i].isValid = fromInfo.isValid;
                    }
                    toList[i].type = ImsRadioUtilsV16.callTypeFromHal(fromInfo.callType);
                    if (fromInfo.accTechStatus.size() > 0) {
                        toList[i].accessTechStatus = unpackAccTechStatus(fromInfo);
                    } else {
                        toList[i].accessTechStatus = new ServiceStatus.StatusForAccessTech[1];
                        toList[i].accessTechStatus[0] = new ServiceStatus.StatusForAccessTech();
                        ServiceStatus.StatusForAccessTech act = toList[i].accessTechStatus[0];
                        act.networkMode = 14;
                        if (fromInfo.status != 4) {
                            act.status = statusTypeFromHal(fromInfo.status);
                        }
                        if (fromInfo.restrictCause != Integer.MAX_VALUE) {
                            act.restrictCause = fromInfo.restrictCause;
                        }
                    }
                    if (fromInfo.status == 2 && fromInfo.restrictCause != 0) {
                        log("Partially Enabled Status due to Restrict Cause");
                        toList[i].status = 1;
                    } else if (fromInfo.status != 4) {
                        toList[i].status = statusTypeFromHal(fromInfo.status);
                    }
                    if (2 != fromInfo.rttMode) {
                        toList[i].rttMode = fromInfo.rttMode;
                    } else {
                        toList[i].rttMode = 0;
                    }
                    log("RTT: copySrvStatusList rtt mode = " + fromInfo.rttMode);
                    int userdataLen = fromInfo.userdata.size();
                    if (userdataLen > 0) {
                        toList[i].userdata = new byte[userdataLen];
                        for (int j = 0; j < userdataLen; j++) {
                            toList[i].userdata[j] = fromInfo.userdata.get(j).byteValue();
                        }
                    }
                } else {
                    log("Null service status in list");
                }
            }
        }
        return toList;
    }

    private static ServiceStatus.StatusForAccessTech[] unpackAccTechStatus(ServiceStatusInfo info) {
        int statusListLen = info.accTechStatus.size();
        ServiceStatus.StatusForAccessTech[] statusForAccessTech = new ServiceStatus.StatusForAccessTech[statusListLen];
        for (int j = 0; j < statusListLen; j++) {
            statusForAccessTech[j] = new ServiceStatus.StatusForAccessTech();
            statusForAccessTech[j] = statusForAccessTechFromHal(info.accTechStatus.get(j));
            log(" networkMode = " + statusForAccessTech[j].networkMode + " status = " + statusForAccessTech[j].status + " restrictCause = " + statusForAccessTech[j].restrictCause + " registered = " + statusForAccessTech[j].registered);
        }
        return statusForAccessTech;
    }

    public static ServiceStatus.StatusForAccessTech statusForAccessTechFromHal(StatusForAccessTech inStatus) {
        ServiceStatus.StatusForAccessTech outStatus = null;
        if (inStatus != null) {
            outStatus = new ServiceStatus.StatusForAccessTech();
            if (inStatus.networkMode != 21) {
                outStatus.networkMode = radioTechFromHal(inStatus.networkMode);
            }
            if (inStatus.status != 4) {
                outStatus.status = statusTypeFromHal(inStatus.status);
            }
            if (inStatus.restrictCause != Integer.MAX_VALUE) {
                outStatus.restrictCause = inStatus.restrictCause;
            }
            if (inStatus.hasRegistration) {
                if (inStatus.registration != null) {
                    outStatus.registered = regStateFromHal(inStatus.registration.state);
                } else {
                    outStatus.registered = 2;
                    Log.e(ImsRadioUtils.class, "Registered not sent");
                }
            }
        }
        return outStatus;
    }

    public static ImsRegistrationInfo registrationFromHal(RegistrationInfo inRegistration) {
        ImsRegistrationInfo outRegistration = null;
        if (inRegistration != null) {
            outRegistration = new ImsRegistrationInfo();
            if (inRegistration.state != 3) {
                outRegistration.setState(regStateFromHal(inRegistration.state));
            }
            if (inRegistration.errorCode != Integer.MAX_VALUE) {
                outRegistration.setErrorCode(inRegistration.errorCode);
            }
            outRegistration.setErrorMessage(inRegistration.errorMessage);
            if (inRegistration.radioTech != 21) {
                outRegistration.setRadioTech(radioTechFromHal(inRegistration.radioTech));
            }
            outRegistration.setPAssociatedUris(inRegistration.pAssociatedUris);
        }
        return outRegistration;
    }

    private static int toCallSubstateConstants(int callSubstate) {
        switch (callSubstate) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 4:
                return 4;
            case 8:
                return 8;
            default:
                return 0;
        }
    }

    public static int regStateFromHal(int state) {
        switch (state) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            default:
                return -1;
        }
    }

    public static int regStateToHal(int state) {
        switch (state) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                return 3;
        }
    }

    public static int radioTechFromHal(int radioTech) {
        switch (radioTech) {
            case 0:
                return -1;
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            case 8:
                return 7;
            case 9:
                return 8;
            case 10:
                return 9;
            case 11:
                return 10;
            case 12:
                return 11;
            case 13:
                return 12;
            case 14:
                return 13;
            case 15:
                return 14;
            case 16:
                return 15;
            case 17:
                return 16;
            case 18:
                return 17;
            case 19:
                return 18;
            case 20:
                return 19;
            case 21:
            default:
                return RadioTech.RADIO_TECH_INVALID;
            case 22:
                return 20;
        }
    }

    public static int callTypeToHal(int callType) {
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
                return 4;
            case 5:
                return 10;
            case 21:
                return 5;
            case 22:
                return 6;
            case 23:
                return 7;
            case 24:
                return 8;
            case 25:
                return 11;
            default:
                return 9;
        }
    }

    public static int callTypeFromHal(int callType) {
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
                return 4;
            case 5:
                return 21;
            case 6:
                return 22;
            case 7:
                return 23;
            case 8:
                return 24;
            case 9:
            default:
                return 10;
            case 10:
                return 5;
            case 11:
                return 25;
        }
    }

    public static int ipPresentationToHal(int presentation) {
        switch (presentation) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 3;
        }
    }

    public static ImsReasonInfo sipErrorFromHal(SipErrorInfo errorInfo) {
        int imsReasonExtraCode = 0;
        String imsReasonExtraMessage = errorInfo.errorString;
        if (errorInfo.errorCode != Integer.MAX_VALUE) {
            imsReasonExtraCode = errorInfo.errorCode;
        }
        return new ImsReasonInfo(0, imsReasonExtraCode, imsReasonExtraMessage);
    }

    public static DialRequest buildDialRequest(String address, int clirMode, CallDetails callDetails, boolean isEncrypted) {
        return buildDialRequest(address, clirMode, callDetails, isEncrypted, null);
    }

    public static DialRequest buildDialRequest(String address, int clirMode, CallDetails callDetails, boolean isEncrypted, RedialInfo redialInfo) {
        DialRequest dialRequest = new DialRequest();
        if (address != null) {
            dialRequest.address = address;
        }
        dialRequest.clirMode = clirMode;
        dialRequest.presentation = getIpPresentation(clirMode);
        if (callDetails != null) {
            dialRequest.hasCallDetails = true;
            if (redialInfo != null && redialInfo.getRetryCallFailCause() != 0) {
                callDetails.addExtra("RetryCallFailReason=" + getCallFailCauseForImsReason(redialInfo.getRetryCallFailCause()));
            }
            if (redialInfo != null && redialInfo.getRetryCallFailRadioTech() != 0) {
                callDetails.addExtra("RetryCallFailRadioTech=" + mapRadioTechToHal(redialInfo.getRetryCallFailRadioTech()));
            }
            callDetailsToHal(dialRequest.callDetails, callDetails);
        }
        boolean isConferenceUri = getIsConferenceUri(callDetails);
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
        return dialRequest;
    }

    public static vendor.qti.hardware.radio.ims.V1_0.DialRequest migrateFromDialRequestV14(DialRequest from) {
        vendor.qti.hardware.radio.ims.V1_0.DialRequest to = new vendor.qti.hardware.radio.ims.V1_0.DialRequest();
        to.address = from.address;
        to.clirMode = from.clirMode;
        to.presentation = from.presentation;
        to.hasCallDetails = from.hasCallDetails;
        to.callDetails.callType = from.callDetails.callType;
        to.callDetails.callDomain = from.callDetails.callDomain;
        to.callDetails.extrasLength = from.callDetails.extrasLength;
        for (String extra : from.callDetails.extras) {
            to.callDetails.extras.add(extra);
        }
        for (vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo localAbility : from.callDetails.localAbility) {
            to.callDetails.localAbility.add(localAbility);
        }
        for (vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo peerAbility : from.callDetails.peerAbility) {
            to.callDetails.peerAbility.add(peerAbility);
        }
        to.callDetails.callSubstate = from.callDetails.callSubstate;
        to.callDetails.mediaId = from.callDetails.mediaId;
        to.callDetails.causeCode = from.callDetails.causeCode;
        to.callDetails.rttMode = from.callDetails.rttMode;
        to.callDetails.sipAlternateUri = from.callDetails.sipAlternateUri;
        to.hasIsConferenceUri = from.hasIsConferenceUri;
        to.isConferenceUri = from.isConferenceUri;
        to.hasIsCallPull = from.hasIsCallPull;
        to.isCallPull = from.isCallPull;
        to.hasIsEncrypted = from.hasIsEncrypted;
        to.isEncrypted = from.isEncrypted;
        return to;
    }

    public static HangupRequestInfo buildHangupRequest(int connectionId, String userUri, String confUri, boolean mpty, int failCause, String errorInfo) {
        HangupRequestInfo hangup = new HangupRequestInfo();
        if (connectionId != 0) {
            hangup.connIndex = connectionId;
        } else {
            hangup.connIndex = RadioTech.RADIO_TECH_INVALID;
        }
        if (userUri != null) {
            hangup.connUri = userUri;
        }
        hangup.hasMultiParty = true;
        hangup.multiParty = mpty;
        hangup.conf_id = RadioTech.RADIO_TECH_INVALID;
        if (failCause != Integer.MAX_VALUE) {
            hangup.hasFailCauseResponse = true;
            if (errorInfo != null && !errorInfo.isEmpty()) {
                Log.v(TAG, "hangupWithReason errorInfo = " + errorInfo);
                for (byte b : errorInfo.getBytes()) {
                    Byte b2 = Byte.valueOf(b);
                    hangup.failCauseResponse.errorinfo.add(0, b2);
                }
            }
            int callFailCause = getCallFailCauseForImsReason(failCause);
            hangup.failCauseResponse.failCause = callFailCause;
            Log.v(TAG, "hangupWithReason callFailCause=" + callFailCause);
            if (callFailCause == 509) {
                for (byte b3 : Integer.toString(failCause).getBytes()) {
                    Byte b4 = Byte.valueOf(b3);
                    hangup.failCauseResponse.errorinfo.add(0, b4);
                }
            }
            Log.v(TAG, "hangupWithReason MISC callFailCause, errorInfo=" + failCause);
        }
        return hangup;
    }

    public static int getCallFailCauseForImsReason(int imsReason) {
        Log.i(TAG, "imsReason= " + imsReason);
        switch (imsReason) {
            case 501:
                return 501;
            case 502:
            case 503:
            default:
                int failCause = ImsRadioUtilsV13.getCallFailCauseForImsReason(imsReason);
                return failCause;
            case 504:
                return 502;
            case 505:
                return 503;
            case 506:
                return 504;
        }
    }

    public static int statusTypeToHal(int status) {
        switch (status) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 4;
        }
    }

    public static int statusTypeFromHal(int status) {
        switch (status) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 3;
        }
    }

    public static int mapCapabilityToSrvType(int capability) {
        switch (capability) {
            case 1:
                return 0;
            case 2:
                return 3;
            case 4:
                return 25;
            case 8:
                return 5;
            default:
                return 10;
        }
    }

    public static int mapRadioTechToHidlRadioTech(int rat) {
        switch (rat) {
            case 0:
                return 14;
            case 1:
                return 19;
            default:
                return 0;
        }
    }

    public static int mapValueToServiceStatus(int value) {
        switch (value) {
            case 0:
                return 0;
            case 1:
                return 2;
            default:
                return 0;
        }
    }

    public static int mapRadioTechToHal(int radioTech) {
        switch (radioTech) {
            case -1:
                return 0;
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
                return 18;
            case 18:
                return 19;
            case 19:
                return 20;
            case 20:
                return 22;
            default:
                return 21;
        }
    }

    public static int callDomainFromHal(int callDomain) {
        switch (callDomain) {
            case 0:
                return 11;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 4;
        }
    }

    public static int callDomainToHal(int callDomain) {
        switch (callDomain) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 11:
                return 0;
            default:
                return 4;
        }
    }

    public static int ringbackToneFromHal(int tone) {
        switch (tone) {
            case 1:
                return 1;
            default:
                return 0;
        }
    }

    public static void log(String msg) {
        Log.i(ImsRadioUtils.class, msg);
    }

    protected static Object handleSrvStatus(ArrayList<ServiceStatusInfo> inList) {
        if (inList == null) {
            log("inList is null.");
            return null;
        }
        ServiceStatus[] outList = copySrvStatusList(inList);
        ArrayList<ServiceStatus> response = new ArrayList<>(Arrays.asList(outList));
        return response;
    }

    public static HoInfo handoverFromHal(HandoverInfo inHandover) {
        if (inHandover == null) {
            return null;
        }
        HoInfo outHandover = new HoInfo();
        if (inHandover.type != 6) {
            outHandover.setType(handoverTypeFromHal(inHandover.type));
        }
        if (inHandover.srcTech != 21) {
            outHandover.setSrcTech(radioTechFromHal(inHandover.srcTech));
        }
        if (inHandover.targetTech != 21) {
            outHandover.setTargetTech(radioTechFromHal(inHandover.targetTech));
        }
        if (inHandover.hasHoExtra) {
            outHandover.setExtra(extraTypeFromHal(inHandover.hoExtra.type), getByteArray(inHandover.hoExtra.extraInfo));
        }
        outHandover.setErrorCode(inHandover.errorCode);
        outHandover.setErrorMessage(inHandover.errorMessage);
        return outHandover;
    }

    public static int handoverTypeFromHal(int inType) {
        switch (inType) {
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
            case 5:
                return 5;
            default:
                return -1;
        }
    }

    private static DriverCallIms.State callStateFromHal(int inCallState) {
        switch (inCallState) {
            case 0:
                return DriverCallIms.State.ACTIVE;
            case 1:
                return DriverCallIms.State.HOLDING;
            case 2:
                return DriverCallIms.State.DIALING;
            case 3:
                return DriverCallIms.State.ALERTING;
            case 4:
                return DriverCallIms.State.INCOMING;
            case 5:
                return DriverCallIms.State.WAITING;
            default:
                return DriverCallIms.State.END;
        }
    }

    private static int extraTypeFromHal(int extraHo) {
        switch (extraHo) {
            case 0:
                return 1;
            default:
                return -1;
        }
    }

    public static CallForwardInfo buildCallForwardInfo(int cfReason, int serviceClass, String number, int action, int timeSeconds) {
        CallForwardInfo cfInfo = new CallForwardInfo();
        cfInfo.status = action;
        cfInfo.reason = cfReason;
        cfInfo.serviceClass = serviceClass;
        cfInfo.toa = PhoneNumberUtils.toaFromString(number);
        if (number != null) {
            cfInfo.number = number;
        }
        cfInfo.hasCallFwdTimerStart = false;
        cfInfo.hasCallFwdTimerEnd = false;
        cfInfo.timeSeconds = timeSeconds;
        return cfInfo;
    }

    public static ImsSmsMessage buildImsSms(int messageRef, String format, String smsc, boolean isRetry, byte[] pdu) {
        ImsSmsMessage imsSms = new ImsSmsMessage();
        imsSms.messageRef = messageRef;
        imsSms.format = format;
        imsSms.shallRetry = isRetry;
        imsSms.smsc = smsc == null ? "" : smsc;
        for (byte a : pdu) {
            imsSms.pdu.add(Byte.valueOf(a));
        }
        return imsSms;
    }

    public static int imsSmsDeliverStatusToHidl(int status) {
        switch (status) {
            case 1:
                return 0;
            case 2:
                return 1;
            default:
                return ImsRadioUtilsV15.imsSmsDeliverStatusToHidl(status);
        }
    }

    public static int imsSmsStatusReportStatusToHidl(int report) {
        switch (report) {
            case 1:
                return 0;
            case 2:
                return 1;
            default:
                return 1;
        }
    }

    public static void buildCallFwdTimerInfo(CallFwdTimerInfo callFwdTimerInfo, int hour, int minute) {
        callFwdTimerInfo.year = RadioTech.RADIO_TECH_INVALID;
        callFwdTimerInfo.month = RadioTech.RADIO_TECH_INVALID;
        callFwdTimerInfo.day = RadioTech.RADIO_TECH_INVALID;
        callFwdTimerInfo.hour = hour;
        callFwdTimerInfo.minute = minute;
        callFwdTimerInfo.second = RadioTech.RADIO_TECH_INVALID;
        callFwdTimerInfo.timezone = RadioTech.RADIO_TECH_INVALID;
    }

    public static Object buildCFStatusResponseFromHal(ArrayList<CallForwardInfo> inCfInfoList) {
        if (inCfInfoList != null) {
            int infoListSize = inCfInfoList.size();
            org.codeaurora.telephony.utils.CallForwardInfo[] outCfInfoList = new org.codeaurora.telephony.utils.CallForwardInfo[infoListSize];
            ImsCallForwardTimerInfo[] timerInfo = new ImsCallForwardTimerInfo[infoListSize];
            for (int i = 0; i < infoListSize; i++) {
                outCfInfoList[i] = new org.codeaurora.telephony.utils.CallForwardInfo();
                CallForwardInfo inCfInfo = inCfInfoList.get(i);
                if (inCfInfo.status != Integer.MAX_VALUE) {
                    outCfInfoList[i].status = inCfInfo.status;
                }
                if (inCfInfo.reason != Integer.MAX_VALUE) {
                    outCfInfoList[i].reason = inCfInfo.reason;
                }
                if (inCfInfo.serviceClass != Integer.MAX_VALUE) {
                    outCfInfoList[i].serviceClass = inCfInfo.serviceClass;
                }
                if (inCfInfo.toa != Integer.MAX_VALUE) {
                    outCfInfoList[i].toa = inCfInfo.toa;
                }
                outCfInfoList[i].number = inCfInfo.number;
                if (inCfInfo.timeSeconds != Integer.MAX_VALUE) {
                    outCfInfoList[i].timeSeconds = inCfInfo.timeSeconds;
                }
                if (inCfInfo.hasCallFwdTimerStart && inCfInfo.hasCallFwdTimerEnd) {
                    log("responseQueryCallForward for Timer");
                    CallFwdTimerInfo startCallTimerInfo = inCfInfo.callFwdTimerStart;
                    timerInfo[i] = new ImsCallForwardTimerInfo();
                    if (inCfInfo.status != Integer.MAX_VALUE) {
                        timerInfo[i].status = inCfInfo.status;
                    }
                    if (inCfInfo.reason != Integer.MAX_VALUE) {
                        timerInfo[i].reason = inCfInfo.reason;
                    }
                    if (inCfInfo.serviceClass != Integer.MAX_VALUE) {
                        timerInfo[i].serviceClass = inCfInfo.serviceClass;
                    }
                    timerInfo[i].number = inCfInfo.number;
                    if (startCallTimerInfo.hour != Integer.MAX_VALUE) {
                        timerInfo[i].startHour = startCallTimerInfo.hour;
                    }
                    if (startCallTimerInfo.minute != Integer.MAX_VALUE) {
                        timerInfo[i].startMinute = startCallTimerInfo.minute;
                    }
                    CallFwdTimerInfo endCallTimerInfo = inCfInfo.callFwdTimerEnd;
                    if (endCallTimerInfo.hour != Integer.MAX_VALUE) {
                        timerInfo[i].endHour = endCallTimerInfo.hour;
                    }
                    if (endCallTimerInfo.minute != Integer.MAX_VALUE) {
                        timerInfo[i].endMinute = endCallTimerInfo.minute;
                    }
                    return timerInfo;
                }
            }
            return outCfInfoList;
        }
        org.codeaurora.telephony.utils.CallForwardInfo[] outCfInfoList2 = new org.codeaurora.telephony.utils.CallForwardInfo[0];
        log("inCfInfoList is null.");
        return outCfInfoList2;
    }

    public static int serviceClassStatusFromHal(int inServiceStatus) {
        switch (inServiceStatus) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }
    }

    public static int serviceClassProvisionStatusFromHal(int inServiceProvisionStatus) {
        switch (inServiceProvisionStatus) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return 2;
        }
    }

    public static SuppNotifyInfo suppServiceNotificationFromHal(SuppServiceNotification inNotification) {
        if (inNotification == null) {
            return null;
        }
        SuppNotifyInfo outNotification = new SuppNotifyInfo();
        if (inNotification.notificationType != 2) {
            outNotification.setNotificationType(notificationTypeFromHal(inNotification.notificationType));
        }
        if (inNotification.code != Integer.MAX_VALUE) {
            outNotification.setCode(inNotification.code);
        }
        if (inNotification.index != Integer.MAX_VALUE) {
            outNotification.setIndex(inNotification.index);
        }
        if (inNotification.type != Integer.MAX_VALUE) {
            outNotification.setType(inNotification.type);
        }
        if (inNotification.connId != Integer.MAX_VALUE) {
            outNotification.setConnId(inNotification.connId);
        }
        outNotification.setNumber(inNotification.number);
        outNotification.setHistoryInfo(inNotification.historyInfo);
        if (inNotification.hasHoldTone) {
            outNotification.setHoldTone(inNotification.holdTone);
        }
        return outNotification;
    }

    private static int notificationTypeFromHal(int inNotificationType) {
        switch (inNotificationType) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return RadioTech.RADIO_TECH_INVALID;
        }
    }

    public static byte[] getByteArray(ArrayList<Byte> inList) {
        if (inList == null) {
            return null;
        }
        byte[] outArray = new byte[inList.size()];
        for (int i = 0; i < outArray.length; i++) {
            outArray[i] = inList.get(i).byteValue();
        }
        return outArray;
    }

    public static int FacilityTypeFromHal(int facilityType) {
        switch (facilityType) {
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
            default:
                return 8;
        }
    }

    public static int facilityTypeToHal(int facilityType) {
        switch (facilityType) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            case 8:
                return 7;
            case 9:
                return 8;
            case 10:
                return 9;
            case 11:
                return 10;
            case 12:
                return 11;
            default:
                return 12;
        }
    }

    public static SuppSvcResponse suppSvcStatusResponseFromHal(SuppServiceStatus suppServiceStatus) {
        SuppSvcResponse suppSvcResponse = new SuppSvcResponse();
        suppSvcResponse.setStatus(serviceClassStatusFromHal(suppServiceStatus.status));
        suppSvcResponse.setProvisionStatus(serviceClassProvisionStatusFromHal(suppServiceStatus.provisionStatus));
        if (suppServiceStatus.facilityType != 12) {
            suppSvcResponse.setFacilityType(FacilityTypeFromHal(suppServiceStatus.facilityType));
        }
        if (suppServiceStatus.failureCause != null && !suppServiceStatus.failureCause.isEmpty()) {
            suppSvcResponse.setFailureCause(suppServiceStatus.failureCause);
        }
        for (CbNumListInfo numList : suppServiceStatus.cbNumListInfo) {
            if (numList.serviceClass != Integer.MAX_VALUE) {
                SuppSvcResponse.BarredLines lines = new SuppSvcResponse.BarredLines(numList.serviceClass);
                if (numList.cbNumInfo != null) {
                    for (CbNumInfo numInfo : numList.cbNumInfo) {
                        SuppSvcResponse.LineStatus lineStatus = new SuppSvcResponse.LineStatus(numInfo.status, numInfo.number);
                        lines.addLine(lineStatus);
                    }
                }
                suppSvcResponse.addBarredLines(lines);
            }
        }
        if (suppServiceStatus.hasErrorDetails) {
            suppSvcResponse.setErrorDetails(sipErrorFromHal(suppServiceStatus.errorDetails));
        }
        return suppSvcResponse;
    }

    public static int ipPresentationFromHal(int presentation) {
        switch (presentation) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return 2;
        }
    }

    public static int ttyModeFromHal(int ttyMode) {
        switch (ttyMode) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    public static int ttyModeToHal(int ttyMode) {
        switch (ttyMode) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 4;
        }
    }

    public static int clipStatusFromHal(int clipStatus) {
        switch (clipStatus) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return 2;
        }
    }

    public static int conferenceCallStateFromHal(int conferenceCallState) {
        switch (conferenceCallState) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return 2;
        }
    }

    public static CallModifyInfo buildCallModifyInfo(CallModify callModify) {
        log("buildCallModifyInfo callModify= " + callModify);
        CallModifyInfo callModifyInfo = new CallModifyInfo();
        callModifyInfo.callIndex = callModify.call_index;
        if (callModify.call_details != null) {
            callModifyInfo.hasCallDetails = true;
            callDetailsToHal(callModifyInfo.callDetails, callModify.call_details);
        }
        callModifyInfo.failCause = 13;
        return callModifyInfo;
    }

    public static CallModify callModifyFromHal(CallModifyInfo callModifyInfo) {
        CallModify callModify = new CallModify();
        if (callModifyInfo.hasCallDetails) {
            callModify.call_details = getCallDetails(ImsRadioUtilsV16.migrateCallDetails(callModifyInfo.callDetails));
        }
        if (callModifyInfo.callIndex != Integer.MAX_VALUE) {
            callModify.call_index = callModifyInfo.callIndex;
        }
        callModify.error = 0;
        if (callModifyInfo.failCause != 13) {
            callModify.error = callModifyFailCauseFromHal(callModifyInfo.failCause);
        }
        return callModify;
    }

    private static int callModifyFailCauseFromHal(int failCause) {
        switch (failCause) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 6;
            case 4:
                return 7;
            case 5:
                return 16;
            case 6:
                return 27;
            case 7:
                return 28;
            case 8:
                return 29;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 32;
            case 12:
                return 33;
            default:
                return 0;
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
        configInfo.errorCause = configFailureCauseToHal(errorCause);
        return configInfo;
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
            config.setErrorCause(configFailureCauseFromHal(configInfo.errorCause));
        }
        return config;
    }

    public static int configInfoItemToHal(int item) {
        switch (item) {
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
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
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
            case 25:
                return 25;
            case 26:
                return 26;
            case 27:
                return 27;
            case 28:
                return 28;
            case 29:
                return 29;
            case 30:
                return 30;
            case 31:
                return 31;
            case 32:
                return 32;
            case 33:
                return 33;
            case 34:
                return 34;
            case 35:
                return 35;
            case 36:
                return 36;
            case 37:
                return 37;
            case 38:
                return 38;
            case 39:
                return 39;
            case 40:
                return 40;
            case 41:
                return 41;
            case 42:
                return 42;
            case 43:
                return 43;
            case 44:
                return 44;
            case 45:
                return 45;
            case 46:
                return 46;
            case 47:
                return 47;
            case 48:
                return 48;
            case 49:
                return 49;
            case 50:
                return 50;
            case 51:
                return 51;
            case 52:
                return 52;
            case 53:
                return 53;
            case 54:
                return 54;
            case 55:
                return 55;
            case 56:
                return 56;
            case 57:
                return 57;
            case 58:
                return 58;
            case 59:
                return 59;
            case 60:
                return 60;
            case 61:
                return 61;
            case 62:
                return 62;
            case 63:
                return 63;
            case 64:
                return 64;
            case 65:
                return 65;
            case 66:
                return 66;
            case 67:
                return 67;
            case 68:
                return 68;
            case 69:
                return 69;
            case 70:
                return 70;
            case 71:
                return 71;
            case 72:
                return 72;
            default:
                return 73;
        }
    }

    public static int configInfoItemFromHal(int item) {
        switch (item) {
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
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
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
            case 25:
                return 25;
            case 26:
                return 26;
            case 27:
                return 27;
            case 28:
                return 28;
            case 29:
                return 29;
            case 30:
                return 30;
            case 31:
                return 31;
            case 32:
                return 32;
            case 33:
                return 33;
            case 34:
                return 34;
            case 35:
                return 35;
            case 36:
                return 36;
            case 37:
                return 37;
            case 38:
                return 38;
            case 39:
                return 39;
            case 40:
                return 40;
            case 41:
                return 41;
            case 42:
                return 42;
            case 43:
                return 43;
            case 44:
                return 44;
            case 45:
                return 45;
            case 46:
                return 46;
            case 47:
                return 47;
            case 48:
                return 48;
            case 49:
                return 49;
            case 50:
                return 50;
            case 51:
                return 51;
            case 52:
                return 52;
            case 53:
                return 53;
            case 54:
                return 54;
            case 55:
                return 55;
            case 56:
                return 56;
            case 57:
                return 57;
            case 58:
                return 58;
            case 59:
                return 59;
            case 60:
                return 60;
            case 61:
                return 61;
            case 62:
                return 62;
            case 63:
                return 63;
            case 64:
                return 64;
            case 65:
                return 65;
            case 66:
                return 66;
            case 67:
                return 67;
            case 68:
                return 68;
            case 69:
                return 69;
            case 70:
            default:
                return 0;
            case 71:
                return 71;
            case 72:
                return 72;
        }
    }

    public static int configFailureCauseToHal(int errorCause) {
        switch (errorCause) {
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
            case 5:
                return 5;
            default:
                return 6;
        }
    }

    public static int configFailureCauseFromHal(int errorCause) {
        switch (errorCause) {
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
                return 5;
        }
    }

    public static CbNumListInfo buildCbNumListInfo(String[] inCbNumList, int serviceClass) {
        CbNumListInfo outCbNumListInfo = new CbNumListInfo();
        if (serviceClass != Integer.MAX_VALUE) {
            outCbNumListInfo.serviceClass = serviceClass;
        }
        if (inCbNumList != null) {
            for (int i = 0; i < inCbNumList.length; i++) {
                CbNumInfo cbNumInfo = new CbNumInfo();
                cbNumInfo.status = 2;
                if (inCbNumList[i] != null) {
                    cbNumInfo.number = inCbNumList[i];
                }
                outCbNumListInfo.cbNumInfo.add(cbNumInfo);
            }
        }
        return outCbNumListInfo;
    }

    public static int messageTypeFromHal(int type) {
        switch (type) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            default:
                return -1;
        }
    }

    public static int messagePriorityFromHal(int type) {
        switch (type) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                return -1;
        }
    }

    public static Mwi messageWaitingIndicationFromHal(MessageWaitingIndication messageWaitingIndication) {
        if (messageWaitingIndication == null) {
            return null;
        }
        Mwi mwi = new Mwi();
        Log.i(TAG, "messageWaitingIndicationFromHal summaryCount = " + messageWaitingIndication.messageSummary.size());
        for (MessageSummary summary : messageWaitingIndication.messageSummary) {
            if (summary != null) {
                mwi.mwiMsgSummary.add(messageSummaryFromHal(summary));
            }
        }
        if (!messageWaitingIndication.ueAddress.isEmpty()) {
            mwi.mUeAddress = messageWaitingIndication.ueAddress;
        }
        Log.i(TAG, "messageWaitingIndicationFromHal detailsCount = " + messageWaitingIndication.messageDetails.size());
        for (MessageDetails details : messageWaitingIndication.messageDetails) {
            if (details != null) {
                mwi.mwiMsgDetails.add(messageDetailsFromHal(details));
            }
        }
        return mwi;
    }

    private static Mwi.MwiMessageSummary messageSummaryFromHal(MessageSummary summary) {
        if (summary == null) {
            return null;
        }
        Mwi.MwiMessageSummary mwiMessageSummary = new Mwi.MwiMessageSummary();
        if (summary.type != 7) {
            mwiMessageSummary.mMessageType = messageTypeFromHal(summary.type);
        }
        if (summary.newMessageCount != Integer.MAX_VALUE) {
            mwiMessageSummary.mNewMessage = summary.newMessageCount;
        }
        if (summary.oldMessageCount != Integer.MAX_VALUE) {
            mwiMessageSummary.mOldMessage = summary.oldMessageCount;
        }
        if (summary.newUrgentMessageCount != Integer.MAX_VALUE) {
            mwiMessageSummary.mNewUrgent = summary.newUrgentMessageCount;
        }
        if (summary.oldUrgentMessageCount != Integer.MAX_VALUE) {
            mwiMessageSummary.mOldUrgent = summary.oldUrgentMessageCount;
        }
        return mwiMessageSummary;
    }

    private static Mwi.MwiMessageDetails messageDetailsFromHal(MessageDetails details) {
        if (details == null) {
            return null;
        }
        Mwi.MwiMessageDetails mwiMessageDetails = new Mwi.MwiMessageDetails();
        if (!details.toAddress.isEmpty()) {
            mwiMessageDetails.mToAddress = details.toAddress;
        }
        if (!details.fromAddress.isEmpty()) {
            mwiMessageDetails.mFromAddress = details.fromAddress;
        }
        if (!details.subject.isEmpty()) {
            mwiMessageDetails.mSubject = details.subject;
        }
        if (!details.date.isEmpty()) {
            mwiMessageDetails.mDate = details.date;
        }
        if (details.priority != 4) {
            mwiMessageDetails.mPriority = messagePriorityFromHal(details.priority);
        }
        if (!details.id.isEmpty()) {
            mwiMessageDetails.mMessageId = details.id;
        }
        if (details.type != 7) {
            mwiMessageDetails.mMessageType = messageTypeFromHal(details.type);
        }
        return mwiMessageDetails;
    }

    public static ExplicitCallTransferInfo buildExplicitCallTransferInfo(int srcCallId, int type, String number, int destCallId) {
        ExplicitCallTransferInfo ectInfo = new ExplicitCallTransferInfo();
        ectInfo.callId = srcCallId;
        ectInfo.ectType = ectTypeToHal(type);
        if (number != null) {
            ectInfo.targetAddress = number;
        }
        if (destCallId > 0) {
            ectInfo.targetCallId = destCallId;
        } else {
            ectInfo.targetCallId = RadioTech.RADIO_TECH_INVALID;
        }
        return ectInfo;
    }

    private static int ectTypeToHal(int type) {
        switch (type) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 3;
        }
    }

    public static GeoLocationInfo geolocationIndicationFromHal(double lat, double lon) {
        return new GeoLocationInfo(lat, lon);
    }

    public static ImsSubConfigDetails imsSubconfigFromHal(ImsSubConfigInfo subConfigInfo) {
        if (subConfigInfo == null) {
            return null;
        }
        ImsSubConfigDetails subConfig = new ImsSubConfigDetails();
        if (subConfigInfo.simultStackCount != Integer.MAX_VALUE) {
            subConfig.setSimultStackCount(subConfigInfo.simultStackCount);
        }
        for (int i = 0; i < subConfigInfo.imsStackEnabled.size(); i++) {
            subConfig.addImsStackEnabled(subConfigInfo.imsStackEnabled.get(i).booleanValue());
        }
        return subConfig;
    }

    public static int operationFromHal(int type) {
        switch (type) {
            case 0:
                return 1;
            case 1:
                return 0;
            default:
                return -1;
        }
    }

    public static ParticipantStatusDetails participantStatusFromHal(ParticipantStatusInfo participantStatusInfo) {
        if (participantStatusInfo == null) {
            return null;
        }
        ParticipantStatusDetails participantStatus = new ParticipantStatusDetails();
        if (participantStatusInfo.callId != Integer.MAX_VALUE) {
            participantStatus.setCallId(participantStatusInfo.callId);
        }
        if (participantStatusInfo.operation != 2) {
            participantStatus.setOperation(operationFromHal(participantStatusInfo.operation));
        }
        participantStatus.setParticipantUri(participantStatusInfo.participantUri);
        if (participantStatusInfo.sipStatus != Integer.MAX_VALUE) {
            participantStatus.setSipStatus(participantStatusInfo.sipStatus);
        }
        if (participantStatusInfo.hasIsEct) {
            participantStatus.setIsEct(participantStatusInfo.isEct);
        }
        return participantStatus;
    }

    public static RegistrationBlockStatusInfo registrationBlockStatusFromHal(boolean hasBlockStatusOnWwan, BlockStatus blockStatusOnWwan, boolean hasBlockStatusOnWlan, BlockStatus blockStatusOnWlan) {
        RegistrationBlockStatusInfo regBlockStatus = new RegistrationBlockStatusInfo();
        if (hasBlockStatusOnWwan) {
            regBlockStatus.setStatusOnWwan(blockStatusFromHal(blockStatusOnWwan));
        }
        if (hasBlockStatusOnWlan) {
            regBlockStatus.setStatusOnWlan(blockStatusFromHal(blockStatusOnWlan));
        }
        return regBlockStatus;
    }

    public static BlockStatusInfo blockStatusFromHal(BlockStatus inBlockStatus) {
        if (inBlockStatus == null) {
            return null;
        }
        BlockStatusInfo outBlockStatus = new BlockStatusInfo();
        if (inBlockStatus.blockReason != 4) {
            outBlockStatus.setReason(blockReasonTypeFromHal(inBlockStatus.blockReason));
        }
        if (inBlockStatus.hasBlockReasonDetails) {
            outBlockStatus.setReasonDetails(blockReasonDetailsFromHal(inBlockStatus.blockReasonDetails));
        }
        return outBlockStatus;
    }

    public static int blockReasonTypeFromHal(int inBlockReason) {
        switch (inBlockReason) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                return -1;
        }
    }

    public static BlockReasonDetailsInfo blockReasonDetailsFromHal(BlockReasonDetails inBlockReasonDetails) {
        BlockReasonDetailsInfo outBlockReasonDetails = new BlockReasonDetailsInfo();
        if (inBlockReasonDetails.regFailureReasonType != 10) {
            outBlockReasonDetails.setRegFailureReasonType(regFailureReasonTypeFromHal(inBlockReasonDetails.regFailureReasonType));
        }
        if (inBlockReasonDetails.regFailureReason != Integer.MAX_VALUE) {
            outBlockReasonDetails.setRegFailureReason(inBlockReasonDetails.regFailureReason);
        }
        return outBlockReasonDetails;
    }

    public static int regFailureReasonTypeFromHal(int inRegFailureReasonType) {
        switch (inRegFailureReasonType) {
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
            default:
                return -1;
        }
    }

    public static Object voWiFiCallQualityFromHal(int voWiFiCallQuality) {
        int[] ret = new int[1];
        switch (voWiFiCallQuality) {
            case 1:
                ret[0] = 1;
                return ret;
            case 2:
                ret[0] = 2;
                return ret;
            case 3:
                ret[0] = 4;
                return ret;
            default:
                ret[0] = 0;
                return ret;
        }
    }

    public static UssdInfo ussdInfoFromHal(int type, String msg, SipErrorInfo errorDetails) {
        return new UssdInfo(ussdTypeFromHal(type), msg, errorDetails.errorCode, errorDetails.errorString);
    }

    public static int ussdTypeFromHal(int type) {
        switch (type) {
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
            case 5:
                return 5;
            default:
                return -1;
        }
    }

    public static int crsTypeFromHal(int type) {
        switch (type) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    public static void crsDataFromHal(vendor.qti.hardware.radio.ims.V1_7.CrsData from, CrsData to) {
        to.setCrsType(crsTypeFromHal(from.type));
        to.setOriginalCallType(callTypeFromHal(from.originalCallType));
    }

    public static int callProgressInfoTypeFromHal(int type) {
        switch (type) {
            case 0:
                return -1;
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            default:
                return -1;
        }
    }

    public static void callProgressInfoFromHal(vendor.qti.hardware.radio.ims.V1_7.CallProgressInfo from, CallProgressInfo to) {
        to.setType(callProgressInfoTypeFromHal(from.type));
        if (from.reasonCode != Short.MAX_VALUE) {
            to.setReasonCode(from.reasonCode);
        }
        if (from.reasonText != null && !from.reasonText.isEmpty()) {
            to.setReasonText(from.reasonText);
        }
    }

    public static int serviceTypeFromRILServiceType(int serviceType) {
        switch (serviceType) {
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
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
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
            default:
                return -1;
        }
    }

    public static int requestTypeFromRILRequestType(int requestType) {
        switch (requestType) {
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
                return -1;
        }
    }

    public static int teleserviceTypeFromRILTeleserviceType(int teleservice) {
        switch (teleservice) {
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
            case 5:
                return 5;
            default:
                return -1;
        }
    }

    public static int getUTInterfaceCFReasonFromCommandsInterfaceCFReason(int commandsInterfaceCFReason) {
        switch (commandsInterfaceCFReason) {
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
            case 5:
                return 5;
            default:
                return -1;
        }
    }

    private static int migrateCallFailCauseToV11(int callFailCause) {
        if (callFailCause == 549) {
            return 549;
        }
        return callFailCause;
    }

    private static vendor.qti.hardware.radio.ims.V1_1.CallInfo migrateCallInfoToV11(vendor.qti.hardware.radio.ims.V1_0.CallInfo callInfo) {
        vendor.qti.hardware.radio.ims.V1_1.CallInfo callInfoV11 = new vendor.qti.hardware.radio.ims.V1_1.CallInfo();
        callInfoV11.state = callInfo.state;
        callInfoV11.index = callInfo.index;
        callInfoV11.toa = callInfo.toa;
        callInfoV11.hasIsMpty = callInfo.hasIsMpty;
        callInfoV11.isMpty = callInfo.isMpty;
        callInfoV11.hasIsMT = callInfo.hasIsMT;
        callInfoV11.isMT = callInfo.isMT;
        callInfoV11.als = callInfo.als;
        callInfoV11.hasIsVoice = callInfo.hasIsVoice;
        callInfoV11.isVoice = callInfo.isVoice;
        callInfoV11.hasIsVoicePrivacy = callInfo.hasIsVoicePrivacy;
        callInfoV11.isVoicePrivacy = callInfo.isVoicePrivacy;
        callInfoV11.number = callInfo.number;
        callInfoV11.numberPresentation = callInfo.numberPresentation;
        callInfoV11.name = callInfo.name;
        callInfoV11.namePresentation = callInfo.namePresentation;
        callInfoV11.hasCallDetails = callInfo.hasCallDetails;
        callInfoV11.callDetails.callType = callInfo.callDetails.callType;
        callInfoV11.callDetails.callDomain = callInfo.callDetails.callDomain;
        callInfoV11.callDetails.extrasLength = callInfo.callDetails.extrasLength;
        for (int i = 0; i < callInfo.callDetails.extras.size(); i++) {
            callInfoV11.callDetails.extras.add(callInfo.callDetails.extras.get(i));
        }
        for (int i2 = 0; i2 < callInfo.callDetails.localAbility.size(); i2++) {
            callInfoV11.callDetails.localAbility.add(callInfo.callDetails.localAbility.get(i2));
        }
        for (int i3 = 0; i3 < callInfo.callDetails.peerAbility.size(); i3++) {
            callInfoV11.callDetails.peerAbility.add(callInfo.callDetails.peerAbility.get(i3));
        }
        callInfoV11.callDetails.callSubstate = callInfo.callDetails.callSubstate;
        callInfoV11.callDetails.mediaId = callInfo.callDetails.mediaId;
        callInfoV11.callDetails.causeCode = callInfo.callDetails.causeCode;
        callInfoV11.callDetails.rttMode = callInfo.callDetails.rttMode;
        callInfoV11.callDetails.sipAlternateUri = callInfo.callDetails.sipAlternateUri;
        callInfoV11.hasFailCause = callInfo.hasFailCause;
        callInfoV11.failCause.failCause = migrateCallFailCauseToV11(callInfo.failCause.failCause);
        for (int i4 = 0; i4 < callInfo.failCause.errorinfo.size(); i4++) {
            callInfoV11.failCause.errorinfo.add(callInfo.failCause.errorinfo.get(i4));
        }
        callInfoV11.failCause.networkErrorString = callInfo.failCause.networkErrorString;
        callInfoV11.failCause.hasErrorDetails = callInfo.failCause.hasErrorDetails;
        callInfoV11.failCause.errorDetails.errorCode = callInfo.failCause.errorDetails.errorCode;
        callInfoV11.failCause.errorDetails.errorString = callInfo.failCause.errorDetails.errorString;
        callInfoV11.hasIsEncrypted = callInfo.hasIsEncrypted;
        callInfoV11.isEncrypted = callInfo.isEncrypted;
        callInfoV11.hasIsCalledPartyRinging = callInfo.hasIsCalledPartyRinging;
        callInfoV11.isCalledPartyRinging = callInfo.isCalledPartyRinging;
        callInfoV11.historyInfo = callInfo.historyInfo;
        callInfoV11.hasIsVideoConfSupported = callInfo.hasIsVideoConfSupported;
        callInfoV11.isVideoConfSupported = callInfo.isVideoConfSupported;
        return callInfoV11;
    }

    public static ArrayList<vendor.qti.hardware.radio.ims.V1_1.CallInfo> migrateCallListToV11(ArrayList<vendor.qti.hardware.radio.ims.V1_0.CallInfo> callList) {
        if (callList == null) {
            return null;
        }
        ArrayList<vendor.qti.hardware.radio.ims.V1_1.CallInfo> list = new ArrayList<>();
        for (vendor.qti.hardware.radio.ims.V1_0.CallInfo callInfo : callList) {
            vendor.qti.hardware.radio.ims.V1_1.CallInfo info = migrateCallInfoToV11(callInfo);
            list.add(info);
        }
        return list;
    }

    public static AddressInfo getHidlAddressInfo(double lat, double lon, Address address) {
        AddressInfo addressInfo = new AddressInfo();
        if (address == null) {
            Log.v(TAG, "Null Address!");
            return addressInfo;
        }
        String info = address.getLocality();
        if (info != null) {
            addressInfo.city = info;
        }
        String info2 = address.getAdminArea();
        if (info2 != null) {
            addressInfo.state = info2;
        }
        String info3 = address.getCountryName();
        if (info3 != null) {
            addressInfo.country = info3;
        }
        String info4 = address.getPostalCode();
        if (info4 != null) {
            addressInfo.postalCode = info4;
        }
        String info5 = address.getCountryCode();
        if (info5 != null) {
            addressInfo.countryCode = info5;
        }
        String info6 = address.getThoroughfare();
        if (info6 != null) {
            addressInfo.street = info6;
        }
        String info7 = address.getSubThoroughfare();
        if (info7 != null) {
            addressInfo.houseNumber = info7;
        }
        Log.v(TAG, "address=" + address + ",houseNumber=" + address.getSubThoroughfare());
        return addressInfo;
    }

    public static vendor.qti.hardware.radio.ims.V1_0.AddressInfo migrateAddressToV10(AddressInfo addressInfo) {
        vendor.qti.hardware.radio.ims.V1_0.AddressInfo addressInfoV10 = new vendor.qti.hardware.radio.ims.V1_0.AddressInfo();
        addressInfoV10.city = addressInfo.city;
        addressInfoV10.state = addressInfo.state;
        addressInfoV10.country = addressInfo.country;
        addressInfoV10.postalCode = addressInfo.postalCode;
        addressInfoV10.countryCode = addressInfo.countryCode;
        return addressInfoV10;
    }

    public static vendor.qti.hardware.radio.ims.V1_0.HangupRequestInfo migrateHangupRequestInfoFromV13(HangupRequestInfo from) {
        vendor.qti.hardware.radio.ims.V1_0.HangupRequestInfo to = new vendor.qti.hardware.radio.ims.V1_0.HangupRequestInfo();
        to.connIndex = from.connIndex;
        to.hasMultiParty = from.hasMultiParty;
        to.multiParty = from.multiParty;
        to.connUri = from.connUri;
        to.conf_id = from.conf_id;
        to.hasFailCauseResponse = from.hasFailCauseResponse;
        to.failCauseResponse.failCause = migrateCallFailCausefromV13(from.failCauseResponse.failCause);
        for (Byte errorInfo : from.failCauseResponse.errorinfo) {
            to.failCauseResponse.errorinfo.add(errorInfo);
        }
        to.failCauseResponse.networkErrorString = from.failCauseResponse.networkErrorString;
        to.failCauseResponse.hasErrorDetails = from.failCauseResponse.hasErrorDetails;
        to.failCauseResponse.errorDetails.errorCode = from.failCauseResponse.errorDetails.errorCode;
        to.failCauseResponse.errorDetails.errorString = from.failCauseResponse.errorDetails.errorString;
        return to;
    }

    private static int migrateCallFailCausefromV13(int failCause) {
        if (ImsRadioUtilsV13.isFailCauseIntroducedInV13(failCause).booleanValue()) {
            return 548;
        }
        return failCause;
    }

    public static ConfigInfo migrateConfigInfoFromV15(vendor.qti.hardware.radio.ims.V1_5.ConfigInfo from) throws ImsException {
        ConfigInfo to = new ConfigInfo();
        if (ImsRadioUtilsV15.isConfigItemIntroducedInV15(from.item).booleanValue()) {
            throw new ImsException("Config item not supported in current HAL");
        }
        to.item = from.item;
        to.hasBoolValue = from.hasBoolValue;
        to.boolValue = from.boolValue;
        to.intValue = from.intValue;
        if (from.stringValue != null) {
            to.stringValue = from.stringValue;
        }
        to.errorCause = configFailureCauseToHal(from.errorCause);
        return to;
    }

    private static boolean isTirOverwriteAllowed(int tirMode) {
        return tirMode == 1;
    }

    public static vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo migrateServiceStatusInfoFromV16(ServiceStatusInfo from) {
        vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo to = new vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo();
        to.hasIsValid = from.hasIsValid;
        to.isValid = from.isValid;
        to.type = from.type;
        to.callType = from.callType;
        to.status = from.status;
        to.userdata = from.userdata;
        to.restrictCause = from.restrictCause;
        for (StatusForAccessTech status : from.accTechStatus) {
            to.accTechStatus.add(migrateAccTechStatusFromV16(status));
        }
        to.rttMode = from.rttMode;
        return to;
    }

    public static vendor.qti.hardware.radio.ims.V1_0.StatusForAccessTech migrateAccTechStatusFromV16(StatusForAccessTech from) {
        vendor.qti.hardware.radio.ims.V1_0.StatusForAccessTech to = new vendor.qti.hardware.radio.ims.V1_0.StatusForAccessTech();
        to.networkMode = migrateRadioTechTypeFromV16(from.networkMode);
        to.status = from.status;
        to.restrictCause = from.restrictCause;
        to.hasRegistration = from.hasRegistration;
        to.registration = migrateRegistrationInfo(from.registration);
        return to;
    }

    public static vendor.qti.hardware.radio.ims.V1_0.RegistrationInfo migrateRegistrationInfo(RegistrationInfo from) {
        vendor.qti.hardware.radio.ims.V1_0.RegistrationInfo to = new vendor.qti.hardware.radio.ims.V1_0.RegistrationInfo();
        to.state = from.state;
        to.errorCode = from.errorCode;
        to.errorMessage = from.errorMessage;
        to.radioTech = migrateRadioTechTypeFromV16(from.radioTech);
        to.pAssociatedUris = from.pAssociatedUris;
        return to;
    }

    private static int migrateRadioTechTypeFromV16(int from) {
        if (from == 22) {
            return 15;
        }
        return from;
    }
}
