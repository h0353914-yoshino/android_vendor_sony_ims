package org.codeaurora.ims;

import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface ImsPhoneCommandsInterface {
    public static final String CB_FACILITY_BAIC = "AI";
    public static final String CB_FACILITY_BAICr = "IR";
    public static final String CB_FACILITY_BAOC = "AO";
    public static final String CB_FACILITY_BAOIC = "OI";
    public static final String CB_FACILITY_BAOICxH = "OX";
    public static final String CB_FACILITY_BA_ALL = "AB";
    public static final String CB_FACILITY_BA_FD = "FD";
    public static final String CB_FACILITY_BA_MO = "AG";
    public static final String CB_FACILITY_BA_MT = "AC";
    public static final String CB_FACILITY_BA_SIM = "SC";
    public static final int CF_ACTION_DISABLE = 0;
    public static final int CF_ACTION_ENABLE = 1;
    public static final int CF_ACTION_ERASURE = 4;
    public static final int CF_ACTION_REGISTRATION = 3;
    public static final int CF_REASON_ALL = 4;
    public static final int CF_REASON_ALL_CONDITIONAL = 5;
    public static final int CF_REASON_BUSY = 1;
    public static final int CF_REASON_NOT_REACHABLE = 3;
    public static final int CF_REASON_NO_REPLY = 2;
    public static final int CF_REASON_UNCONDITIONAL = 0;
    public static final int CLIR_DEFAULT = 0;
    public static final int CLIR_INVOCATION = 1;
    public static final int CLIR_SUPPRESSION = 2;
    public static final int SERVICE_CLASS_DATA = 2;
    public static final int SERVICE_CLASS_DATA_ASYNC = 32;
    public static final int SERVICE_CLASS_DATA_SYNC = 16;
    public static final int SERVICE_CLASS_FAX = 4;
    public static final int SERVICE_CLASS_MAX = 128;
    public static final int SERVICE_CLASS_NONE = 0;
    public static final int SERVICE_CLASS_PACKET = 64;
    public static final int SERVICE_CLASS_PAD = 128;
    public static final int SERVICE_CLASS_SMS = 8;
    public static final int SERVICE_CLASS_VOICE = 1;
    public static final int USSD_MODE_LOCAL_CLIENT = 3;
    public static final int USSD_MODE_NOTIFY = 0;
    public static final int USSD_MODE_NOT_SUPPORTED = 4;
    public static final int USSD_MODE_NW_RELEASE = 2;
    public static final int USSD_MODE_NW_TIMEOUT = 5;
    public static final int USSD_MODE_REQUEST = 1;

    void conference(Message message);

    void deregisterForCallComposerInfo(Handler handler);

    void deregisterForRttMessage(Handler handler);

    void deregisterForVoiceInfo(Handler handler);

    void exitEmergencyCallbackMode(Message message);

    void explicitCallTransfer(int i, int i2, String str, int i3, Message message);

    void explicitCallTransfer(Message message);

    void getCLIR(Message message);

    void getCOLR(Message message);

    void getCurrentCalls(Message message);

    void getImsRegistrationState(Message message);

    void getLastCallFailCause(Message message);

    RadioState getRadioState();

    void getSuppSvc(String str, Message message);

    void hangupConnection(int i, Message message);

    void hangupForegroundResumeBackground(Message message);

    void hangupWaitingOrBackground(Message message);

    void queryCLIP(Message message);

    void queryCallForwardStatus(int i, int i2, String str, Message message);

    void queryCallWaiting(int i, Message message);

    void queryFacilityLock(String str, String str2, int i, Message message);

    void queryIncomingCallBarring(String str, int i, Message message);

    void queryServiceStatus(Message message);

    void registerForAvailable(Handler handler, int i, Object obj);

    void registerForCallComposerInfo(Handler handler, int i, Object obj);

    void registerForCallStateChanged(Handler handler, int i, Object obj);

    void registerForExitEmergencyCallbackMode(Handler handler, int i, Object obj);

    void registerForImsNetworkStateChanged(Handler handler, int i, Object obj);

    void registerForInCallVoicePrivacyOff(Handler handler, int i, Object obj);

    void registerForInCallVoicePrivacyOn(Handler handler, int i, Object obj);

    void registerForMwi(Handler handler, int i, Object obj);

    void registerForNotAvailable(Handler handler, int i, Object obj);

    void registerForOffOrNotAvailable(Handler handler, int i, Object obj);

    void registerForOn(Handler handler, int i, Object obj);

    void registerForRadioStateChanged(Handler handler, int i, Object obj);

    void registerForRefreshConfInfo(Handler handler, int i, Object obj);

    void registerForRingbackTone(Handler handler, int i, Object obj);

    void registerForRttMessage(Handler handler, int i, Object obj);

    void registerForSipDtmfInfo(Handler handler, int i, Object obj);

    void registerForSrvStatusUpdate(Handler handler, int i, Object obj);

    void registerForUssdInfo(Handler handler, int i, Object obj);

    void registerForViceRefreshInfo(Handler handler, int i, Object obj);

    void registerForVoiceInfo(Handler handler, int i, Object obj);

    void rejectCall(Message message);

    void sendDtmf(char c, Message message);

    void setCLIR(int i, Message message);

    void setCOLR(int i, Message message);

    void setCallForward(int i, int i2, int i3, String str, int i4, Message message);

    void setCallWaiting(boolean z, int i, Message message);

    void setEmergencyCallbackMode(Handler handler, int i, Object obj);

    void setFacilityLock(String str, boolean z, String str2, int i, Message message);

    void setIncomingCallBarring(int i, String str, String[] strArr, int i2, Message message);

    void setOnCallRing(Handler handler, int i, Object obj);

    void setOnSuppServiceNotification(Handler handler, int i, Object obj);

    void setPhoneType(int i);

    void setServiceStatus(Message message, ArrayList<CapabilityStatus> arrayList, int i);

    void setSuppServiceNotifications(boolean z, Message message);

    void setSuppSvc(String str, boolean z, Message message);

    void setUiTTYMode(int i, Message message);

    void startDtmf(char c, Message message);

    void stopDtmf(Message message);

    void switchWaitingOrHoldingAndActive(Message message);

    void unSetOnCallRing(Handler handler);

    void unSetOnSuppServiceNotification(Handler handler);

    void unregisterForAvailable(Handler handler);

    void unregisterForCallStateChanged(Handler handler);

    void unregisterForExitEmergencyCallbackMode(Handler handler);

    void unregisterForImsNetworkStateChanged(Handler handler);

    void unregisterForInCallVoicePrivacyOff(Handler handler);

    void unregisterForInCallVoicePrivacyOn(Handler handler);

    void unregisterForNotAvailable(Handler handler);

    void unregisterForOffOrNotAvailable(Handler handler);

    void unregisterForOn(Handler handler);

    void unregisterForRadioStateChanged(Handler handler);

    void unregisterForRingbackTone(Handler handler);

    void unregisterForSipDtmfInfo(Handler handler);

    void unregisterForUssdInfo(Handler handler);

    public enum RadioState {
        RADIO_OFF,
        RADIO_UNAVAILABLE,
        RADIO_ON;

        public boolean isOn() {
            return this == RADIO_ON;
        }

        public boolean isAvailable() {
            return this != RADIO_UNAVAILABLE;
        }
    }
}
