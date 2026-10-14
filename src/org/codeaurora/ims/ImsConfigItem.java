package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class ImsConfigItem {
    public static final int AMR_BANDWIDTH_EFFICIENT_PT = 53;
    public static final int AMR_DEFAULT_MODE = 56;
    public static final int AMR_OCTET_ALIGNED_PT = 52;
    public static final int AMR_WB_BANDWIDTH_EFFICIENT_PT = 51;
    public static final int AMR_WB_OCTET_ALIGNED_PT = 50;
    public static final int AUTO_REJECT_CALL_MODE = 74;
    public static final int AVAILABILITY_CACHE_EXPIRATION = 19;
    public static final int CALL_COMPOSER_MODE = 75;
    public static final int CANCELLATION_TIMER = 5;
    public static final int CAPABILITIES_CACHE_EXPIRATION = 18;
    public static final int CAPABILITIES_POLL_INTERVAL = 20;
    public static final int CAPABILITY_DISCOVERY_ENABLED = 30;
    public static final int CAPAB_POLL_LIST_SUB_EXP = 23;
    public static final int DOMAIN_NAME = 13;
    public static final int DTMF_NB_PT = 55;
    public static final int DTMF_WB_PT = 54;
    public static final int EAB_SETTING_ENABLED = 25;
    public static final int EMERGENCY_CALL_TIMER = 31;
    public static final int FILE_NOT_AVAILABLE = 2;
    public static final int GZIP_FLAG = 24;
    public static final int IMS_NOT_READY = 1;
    public static final int KEEP_ALIVE_ENABLED = 35;
    public static final int LBO_PCSCF_ADDRESS = 34;
    public static final int LVC_SETTING_ENABLED = 12;
    public static final int MAX_NUM_ENTRIES_IN_RCL = 22;
    public static final int MIN_SESSION_EXPIRY = 4;
    public static final int MOBILE_DATA_ENABLED = 26;
    public static final int NONE = 0;
    public static final int NO_ERR = 0;
    public static final int OTHER_INTERNAL_ERR = 5;
    public static final int PUBLISH_TIMER = 16;
    public static final int PUBLISH_TIMER_EXTENDED = 17;
    public static final int READ_FAILED = 3;
    public static final int REGISTRATION_RETRY_BASE_TIME_SEC = 36;
    public static final int REGISTRATION_RETRY_MAX_TIME_SEC = 37;
    public static final int RTT_SETTING_ENABLED = 70;
    public static final int SILENT_REDIAL_ENABLE = 7;
    public static final int SIP_ACK_RECEIPT_WAIT_TIME_MSEC = 46;
    public static final int SIP_ACK_RETX_WAIT_TIME_MSEC = 47;
    public static final int SIP_INVITE_REQ_RETX_INTERVAL_MSEC = 40;
    public static final int SIP_INVITE_RSP_RETX_INTERVAL_MSEC = 45;
    public static final int SIP_INVITE_RSP_RETX_WAIT_TIME_MSEC = 42;
    public static final int SIP_INVITE_RSP_WAIT_TIME_MSEC = 41;
    public static final int SIP_NON_INVITE_REQ_RETX_INTERVAL_MSEC = 43;
    public static final int SIP_NON_INVITE_REQ_RETX_WAIT_TIME_MSEC = 48;
    public static final int SIP_NON_INVITE_RSP_RETX_WAIT_TIME_MSEC = 49;
    public static final int SIP_NON_INVITE_TXN_TIMEOUT_TIMER_MSEC = 44;
    public static final int SIP_SESSION_TIMER = 3;
    public static final int SIP_T1_TIMER = 8;
    public static final int SIP_T2_TIMER = 9;
    public static final int SIP_TF_TIMER = 10;
    public static final int SMS_APP = 71;
    public static final int SMS_FORMAT = 14;
    public static final int SMS_OVER_IP = 15;
    public static final int SMS_PSI = 57;
    public static final int SOURCE_THROTTLE_PUBLISH = 21;
    public static final int SPEECH_END_PORT = 39;
    public static final int SPEECH_START_PORT = 38;
    public static final int SSAC_HYSTERESIS_TIMER = 32;
    public static final int THRESHOLD_1x = 62;
    public static final int THRESHOLD_LTE1 = 59;
    public static final int THRESHOLD_LTE2 = 60;
    public static final int THRESHOLD_LTE3 = 61;
    public static final int THRESHOLD_WIFI_A = 63;
    public static final int THRESHOLD_WIFI_B = 64;
    public static final int T_DELAY = 6;
    public static final int T_EPDG_1x = 67;
    public static final int T_EPDG_LTE = 65;
    public static final int T_EPDG_WIFI = 66;
    public static final int VCE_SETTING_ENABLED = 69;
    public static final int VIDEO_QUALITY = 58;
    public static final int VLT_SETTING_ENABLED = 11;
    public static final int VOCODER_AMRMODESET = 1;
    public static final int VOCODER_AMRWBMODESET = 2;
    public static final int VOICE_OVER_WIFI_ENABLED = 27;
    public static final int VOICE_OVER_WIFI_MODE = 29;
    public static final int VOICE_OVER_WIFI_ROAMING = 28;
    public static final int VOICE_OVER_WIFI_ROAMING_MODE = 73;
    public static final int VOLTE_USER_OPT_IN_STATUS = 33;
    public static final int VVM_APP = 72;
    public static final int VWF_SETTING_ENABLED = 68;
    public static final int WRITE_FAILED = 4;
    private int mItem = 0;
    private boolean mBoolValue = false;
    private int mIntValue = 0;
    private String mStringValue = "";
    private int mErrorCause = 0;

    public void setItem(int value) {
        this.mItem = value;
    }

    public int getItem() {
        return this.mItem;
    }

    public void setBoolValue(boolean value) {
        this.mBoolValue = value;
    }

    public boolean getBoolValue() {
        return this.mBoolValue;
    }

    public void setIntValue(int value) {
        this.mIntValue = value;
    }

    public int getIntValue() {
        return this.mIntValue;
    }

    public void setStringValue(String value) {
        this.mStringValue = value;
    }

    public String getStringValue() {
        return this.mStringValue;
    }

    public void setErrorCause(int value) {
        this.mErrorCause = value;
    }

    public int getErrorCause() {
        return this.mErrorCause;
    }

    public String toString() {
        return "ImsConfigItem item: " + this.mItem + " boolValue: " + this.mBoolValue + " intValue: " + this.mIntValue + " stringValue: " + this.mStringValue + " errorCause" + this.mErrorCause;
    }
}
