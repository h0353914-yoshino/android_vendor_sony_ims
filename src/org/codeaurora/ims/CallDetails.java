package org.codeaurora.ims;

import java.util.Arrays;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class CallDetails {
    public static final int CALL_DOMAIN_AUTOMATIC = 3;
    public static final int CALL_DOMAIN_CS = 1;
    public static final int CALL_DOMAIN_NOT_SET = 4;
    public static final int CALL_DOMAIN_PS = 2;
    public static final int CALL_DOMAIN_UNKNOWN = 11;
    public static final int CALL_RESTRICT_CAUSE_DISABLED = 2;
    public static final int CALL_RESTRICT_CAUSE_NONE = 0;
    public static final int CALL_RESTRICT_CAUSE_RAT = 1;
    public static final int CALL_SUBSTATE_AUDIO_CONNECTED_SUSPENDED = 1;
    public static final int CALL_SUBSTATE_AVP_RETRY = 4;
    public static final int CALL_SUBSTATE_MEDIA_PAUSED = 8;
    public static final int CALL_SUBSTATE_NONE = 0;
    public static final int CALL_SUBSTATE_VIDEO_CONNECTED_SUSPENDED = 2;
    public static final int CALL_TYPE_CALLCOMPOSER = 26;
    public static final int CALL_TYPE_CS_VS_RX = 22;
    public static final int CALL_TYPE_CS_VS_TX = 21;
    public static final int CALL_TYPE_PS_VS_RX = 24;
    public static final int CALL_TYPE_PS_VS_TX = 23;
    public static final int CALL_TYPE_SMS = 5;
    public static final int CALL_TYPE_UNKNOWN = 10;
    public static final int CALL_TYPE_USSD = 27;
    public static final int CALL_TYPE_UT = 25;
    public static final int CALL_TYPE_VOICE = 0;
    public static final int CALL_TYPE_VT = 3;
    public static final int CALL_TYPE_VT_NODIR = 4;
    public static final int CALL_TYPE_VT_PAUSE = 6;
    public static final int CALL_TYPE_VT_RESUME = 7;
    public static final int CALL_TYPE_VT_RX = 2;
    public static final int CALL_TYPE_VT_TX = 1;
    public static final int CAUSE_CODE_UNSPECIFIED = 0;
    public static final String EXTRAS_CODEC = "Codec";
    public static final String EXTRAS_HANDOVER_INFORMATION = "handoverInfo";
    public static final String EXTRAS_IS_CONFERENCE_URI = "isConferenceUri";
    public static final String EXTRAS_PARENT_CALL_ID = "parentCallId";
    public static final int EXTRA_TYPE_LTE_TO_IWLAN_HO_FAIL = 1;
    public static final int MEDIA_ID_UNKNOWN = -1;
    public static final int RINGBACK_TONE_START = 1;
    public static final int RINGBACK_TONE_STOP = 0;
    public static final int TTY_MODE_FULL = 1;
    public static final int TTY_MODE_HCO = 2;
    public static final int TTY_MODE_OFF = 0;
    public static final int TTY_MODE_VCO = 3;
    public static final int VIDEO_PAUSE_STATE_PAUSED = 1;
    public static final int VIDEO_PAUSE_STATE_RESUMED = 2;
    public int callMediaId;
    public int call_domain;
    public int call_type;
    public int callsubstate;
    public int causeCode;
    public String[] extras;
    public ServiceStatus[] localAbility;
    private boolean mIsCallPull;
    private MultiIdentityLineInfo mLineInfo;
    private int mVideoPauseState;
    public ServiceStatus[] peerAbility;
    public int rttMode;
    public String sipAlternateUri;

    public CallDetails() {
        this.callsubstate = 0;
        this.callMediaId = -1;
        this.mVideoPauseState = 2;
        this.mIsCallPull = false;
        this.causeCode = 0;
        this.rttMode = 0;
        this.call_type = 10;
        this.call_domain = 4;
        this.extras = null;
    }

    public CallDetails(int callType, int callDomain, String[] extraparams) {
        this(callType, callDomain, extraparams, null);
    }

    public CallDetails(int callType, int callDomain, String[] extraparams, MultiIdentityLineInfo info) {
        this.callsubstate = 0;
        this.callMediaId = -1;
        this.mVideoPauseState = 2;
        this.mIsCallPull = false;
        this.causeCode = 0;
        this.rttMode = 0;
        this.call_type = callType;
        this.call_domain = callDomain;
        this.extras = extraparams;
        this.mLineInfo = info;
    }

    public CallDetails(CallDetails srcCall) {
        this.callsubstate = 0;
        this.callMediaId = -1;
        this.mVideoPauseState = 2;
        this.mIsCallPull = false;
        this.causeCode = 0;
        this.rttMode = 0;
        if (srcCall != null) {
            this.call_type = srcCall.call_type;
            this.call_domain = srcCall.call_domain;
            this.callsubstate = srcCall.callsubstate;
            this.callMediaId = srcCall.callMediaId;
            this.extras = srcCall.extras;
            this.localAbility = srcCall.localAbility;
            this.peerAbility = srcCall.peerAbility;
            this.causeCode = srcCall.causeCode;
            this.rttMode = srcCall.rttMode;
            this.mLineInfo = srcCall.mLineInfo;
        }
    }

    public boolean update(CallDetails update) {
        boolean hasChanged = false;
        if (update == null) {
            return false;
        }
        int i = this.call_type;
        int i2 = update.call_type;
        if (i != i2) {
            this.call_type = i2;
            this.causeCode = update.causeCode;
            hasChanged = true;
        }
        int i3 = this.call_domain;
        int i4 = update.call_domain;
        if (i3 != i4) {
            this.call_domain = i4;
            hasChanged = true;
        }
        int i5 = this.callsubstate;
        int i6 = update.callsubstate;
        if (i5 != i6) {
            this.callsubstate = i6;
            hasChanged = true;
        }
        int i7 = this.rttMode;
        int i8 = update.rttMode;
        if (i7 != i8) {
            this.rttMode = i8;
            hasChanged = true;
        }
        String str = this.sipAlternateUri;
        String str2 = update.sipAlternateUri;
        if (str != str2) {
            this.sipAlternateUri = str2;
            hasChanged = true;
        }
        this.localAbility = update.localAbility;
        this.peerAbility = update.peerAbility;
        int i9 = 0;
        while (true) {
            String[] strArr = update.extras;
            if (strArr == null || i9 >= strArr.length) {
                break;
            }
            String[] currKeyValuePair = strArr[i9].split("=");
            if (currKeyValuePair.length == 2) {
                String oldVal = getValueForKeyFromExtras(this.extras, currKeyValuePair[0]);
                if (oldVal != null) {
                    if (!oldVal.equals(currKeyValuePair[1])) {
                        this.extras = setValueForKeyInExtras(this.extras, currKeyValuePair[0], currKeyValuePair[1]);
                        hasChanged = true;
                    }
                } else {
                    hasChanged = true;
                    addExtra(update.extras[i9]);
                }
            }
            i9++;
        }
        setVideoPauseState(update.getVideoPauseState());
        return hasChanged;
    }

    public void setExtras(String[] extraparams) {
        this.extras = extraparams;
    }

    public void addExtra(String extra) {
        String[] strArr = this.extras;
        if (strArr != null) {
            String[] strArr2 = (String[]) Arrays.copyOf(strArr, strArr.length + 1);
            this.extras = strArr2;
            strArr2[strArr2.length - 1] = new String(extra);
            return;
        }
        String[] strArr3 = new String[1];
        this.extras = strArr3;
        strArr3[0] = new String(extra);
    }

    public void setCallPull(boolean value) {
        this.mIsCallPull = value;
    }

    public boolean getCallPull() {
        return this.mIsCallPull;
    }

    public static String[] getExtrasFromMap(Map<String, String> newExtras) {
        if (newExtras == null) {
            return null;
        }
        String[] extras = new String[newExtras.size()];
        for (Map.Entry<String, String> entry : newExtras.entrySet()) {
            extras[0] = "" + entry.getKey() + "=" + entry.getValue();
        }
        return extras;
    }

    public void setExtrasFromMap(Map<String, String> newExtras) {
        this.extras = getExtrasFromMap(newExtras);
    }

    public void setVideoPauseState(int videoPauseState) {
        switch (videoPauseState) {
            case 1:
            case 2:
                this.mVideoPauseState = videoPauseState;
                break;
        }
    }

    public int getVideoPauseState() {
        return this.mVideoPauseState;
    }

    public String getValueForKeyFromExtras(String[] extras, String key) {
        for (int i = 0; extras != null && i < extras.length; i++) {
            if (extras[i] != null) {
                String[] currKey = extras[i].split("=");
                if (currKey.length == 2 && currKey[0].equals(key)) {
                    return currKey[1];
                }
            }
        }
        return null;
    }

    public String[] setValueForKeyInExtras(String[] extras, String key, String value) {
        if (extras != null) {
            for (int i = 0; extras != null && i < extras.length; i++) {
                if (extras[i] != null) {
                    String[] currKey = extras[i].split("=");
                    if (currKey.length == 2 && currKey[0].equals(key)) {
                        extras[i] = key + "=" + value;
                        break;
                    }
                }
            }
        }
        return extras;
    }

    public boolean hasMediaIdValid() {
        int i = this.callMediaId;
        return i != -1 && i >= 0;
    }

    public void setRttMode(int mode) {
        this.rttMode = mode;
    }

    public int getRttMode() {
        return this.rttMode;
    }

    public MultiIdentityLineInfo getMultiIdentityLineInfo() {
        return this.mLineInfo;
    }

    public boolean isRadioTech5G() {
        ServiceStatus[] serviceStatusArr = this.localAbility;
        if (serviceStatusArr == null) {
            return false;
        }
        for (ServiceStatus srv : serviceStatusArr) {
            if (srv != null && srv.type == this.call_type) {
                return srv.isRadioTech5G();
            }
        }
        return false;
    }

    public String toString() {
        String peerSrvAbility;
        String peerSrvAbility2;
        String extrasResult = "";
        String localSrvAbility = "";
        String peerSrvAbility3 = "";
        String[] strArr = this.extras;
        if (strArr != null) {
            for (String s : strArr) {
                extrasResult = extrasResult + s;
            }
        }
        ServiceStatus[] serviceStatusArr = this.localAbility;
        if (serviceStatusArr != null) {
            int length = serviceStatusArr.length;
            int i = 0;
            while (i < length) {
                ServiceStatus srv = serviceStatusArr[i];
                if (srv != null) {
                    localSrvAbility = localSrvAbility + "isValid = " + srv.isValid + " type = " + srv.type + " status = " + srv.status + " rttMode = " + this.rttMode;
                    if (srv.accessTechStatus != null) {
                        ServiceStatus.StatusForAccessTech[] statusForAccessTechArr = srv.accessTechStatus;
                        int length2 = statusForAccessTechArr.length;
                        int i2 = 0;
                        while (i2 < length2) {
                            String extrasResult2 = extrasResult;
                            ServiceStatus.StatusForAccessTech at = statusForAccessTechArr[i2];
                            localSrvAbility = localSrvAbility + " accTechStatus " + at;
                            i2++;
                            extrasResult = extrasResult2;
                            peerSrvAbility3 = peerSrvAbility3;
                        }
                    }
                }
                i++;
                extrasResult = extrasResult;
                peerSrvAbility3 = peerSrvAbility3;
            }
            peerSrvAbility = peerSrvAbility3;
        } else {
            peerSrvAbility = "";
        }
        ServiceStatus[] serviceStatusArr2 = this.peerAbility;
        if (serviceStatusArr2 == null) {
            peerSrvAbility2 = peerSrvAbility;
        } else {
            int length3 = serviceStatusArr2.length;
            String peerSrvAbility4 = peerSrvAbility;
            int i3 = 0;
            while (i3 < length3) {
                ServiceStatus srv2 = serviceStatusArr2[i3];
                if (srv2 != null) {
                    peerSrvAbility4 = peerSrvAbility4 + "isValid = " + srv2.isValid + " type = " + srv2.type + " status = " + srv2.status + " rttMode = " + this.rttMode;
                    if (srv2.accessTechStatus != null) {
                        ServiceStatus.StatusForAccessTech[] statusForAccessTechArr2 = srv2.accessTechStatus;
                        int length4 = statusForAccessTechArr2.length;
                        int i4 = 0;
                        while (i4 < length4) {
                            ServiceStatus.StatusForAccessTech at2 = statusForAccessTechArr2[i4];
                            peerSrvAbility4 = peerSrvAbility4 + " accTechStatus " + at2;
                            i4++;
                            serviceStatusArr2 = serviceStatusArr2;
                        }
                    }
                }
                i3++;
                serviceStatusArr2 = serviceStatusArr2;
            }
            peerSrvAbility2 = peerSrvAbility4;
        }
        return " " + this.call_type + " " + this.call_domain + " callSubState " + this.callsubstate + " videoPauseState" + this.mVideoPauseState + " mediaId" + this.callMediaId + " Rtt mode " + this.rttMode + " Local Ability " + localSrvAbility + " Peer Ability " + peerSrvAbility2 + " Cause code " + this.causeCode + " sipAlternateUri " + this.sipAlternateUri + ", " + this.mLineInfo;
    }
}
