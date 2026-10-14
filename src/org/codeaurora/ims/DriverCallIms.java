package org.codeaurora.ims;

import android.telephony.ims.ImsReasonInfo;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class DriverCallIms implements Comparable<DriverCallIms> {
    public static final int CONF_SUPPORT_INDICATED = 1;
    public static final int CONF_SUPPORT_NONE = 0;
    public static final int CONF_VIDEO_SUPPORTED = 2;
    static final String LOG_TAG = "DRIVERCALL-IMS";
    public static final int UPDATE_CALL_DETAILS = 32;
    public static final int UPDATE_CALL_PROGRESS_INFO = 2048;
    public static final int UPDATE_CONF_SUPPORT = 512;
    public static final int UPDATE_CRS_INFO = 1024;
    public static final int UPDATE_ENCRYPTION = 128;
    public static final int UPDATE_HISTORY_INFO = 256;
    public static final int UPDATE_INDEX = 2;
    public static final int UPDATE_IS_MPTY = 16;
    public static final int UPDATE_IS_MT = 8;
    public static final int UPDATE_NONE = 0;
    public static final int UPDATE_NUMBER = 4;
    public static final int UPDATE_STATE = 1;
    public int TOA;
    public int als;
    public CallDetails callDetails;
    public ImsReasonInfo callFailCause;
    public CallProgressInfo callProgressInfo;
    public CrsData crsData;
    public String historyInfo;
    public int index;
    public boolean isEncrypted;
    public boolean isMT;
    public boolean isMpty;
    public boolean isPreparatory;
    public boolean isTirOverwriteAllowed;
    public boolean isVoice;
    public boolean isVoicePrivacy;
    private CallComposerInfo mCallComposerInfo;
    public int mCallFailReason;
    public int mConfSupported;
    public MultiIdentityLineInfo mMtMultiLineInfo;
    private final VerstatInfo mVerstatInfo;
    public String name;
    public int namePresentation;
    public String number;
    public int numberPresentation;
    public State state;

    public enum State {
        ACTIVE,
        HOLDING,
        DIALING,
        ALERTING,
        INCOMING,
        WAITING,
        END
    }

    public DriverCallIms(DriverCallIms dc) {
        this.mConfSupported = 0;
        this.isPreparatory = false;
        this.callDetails = new CallDetails(dc.callDetails);
        this.callFailCause = new ImsReasonInfo(dc.callFailCause.getCode(), dc.callFailCause.getExtraCode(), dc.callFailCause.getExtraMessage());
        this.crsData = new CrsData(dc.crsData);
        this.state = dc.state;
        this.index = dc.index;
        this.number = dc.number;
        this.isMT = dc.isMT;
        this.TOA = dc.TOA;
        this.isMpty = dc.isMpty;
        this.als = dc.als;
        this.isVoice = dc.isVoice;
        this.isVoicePrivacy = dc.isVoicePrivacy;
        this.numberPresentation = dc.numberPresentation;
        this.name = dc.name;
        this.namePresentation = dc.namePresentation;
        this.isEncrypted = dc.isEncrypted;
        this.historyInfo = dc.historyInfo;
        this.mConfSupported = dc.mConfSupported;
        this.mVerstatInfo = dc.getVerstatInfo();
        this.mMtMultiLineInfo = dc.mMtMultiLineInfo;
        this.isTirOverwriteAllowed = dc.isTirOverwriteAllowed;
        this.mCallComposerInfo = dc.getCallComposerInfo();
        this.isPreparatory = dc.isPreparatory;
        this.callProgressInfo = new CallProgressInfo(dc.callProgressInfo);
    }

    public DriverCallIms() {
        this.mConfSupported = 0;
        this.isPreparatory = false;
        this.callDetails = new CallDetails();
        this.mVerstatInfo = new VerstatInfo();
        this.crsData = new CrsData();
        this.callProgressInfo = new CallProgressInfo();
    }

    public DriverCallIms(VerstatInfo verstatInfo) {
        this.mConfSupported = 0;
        this.isPreparatory = false;
        this.callDetails = new CallDetails();
        this.crsData = new CrsData();
        this.mVerstatInfo = verstatInfo;
        this.callProgressInfo = new CallProgressInfo();
    }

    public DriverCallIms(CallComposerInfo callComposerInfo, VerstatInfo verstatInfo) {
        this(verstatInfo);
        this.crsData = new CrsData();
        this.mCallComposerInfo = callComposerInfo;
        this.callProgressInfo = new CallProgressInfo();
    }

    public int update(DriverCallIms update) {
        if (update == null) {
            return 0;
        }
        int changed = 0;
        State state = this.state;
        State state2 = update.state;
        if (state != state2) {
            this.state = state2;
            changed = 0 | 1;
        }
        int i = this.index;
        int i2 = update.index;
        if (i != i2) {
            this.index = i2;
            changed |= 2;
        }
        String str = this.number;
        String str2 = update.number;
        if (str != str2) {
            this.number = str2;
            changed |= 4;
        }
        boolean z = this.isMT;
        boolean z2 = update.isMT;
        if (z != z2) {
            this.isMT = z2;
            changed |= 8;
        }
        boolean z3 = this.isMpty;
        boolean z4 = update.isMpty;
        if (z3 != z4) {
            this.isMpty = z4;
            changed |= 16;
        }
        if (update.callFailCause != null) {
            ImsReasonInfo imsReasonInfo = this.callFailCause;
            if (imsReasonInfo == null) {
                this.callFailCause = new ImsReasonInfo(update.callFailCause.getCode(), update.callFailCause.getExtraCode(), update.callFailCause.getExtraMessage());
            } else {
                int imsReasonCode = imsReasonInfo.getCode();
                int imsReasonExtraCode = this.callFailCause.getExtraCode();
                String imsReasonExtraMessage = this.callFailCause.getExtraMessage();
                if (this.callFailCause.getCode() != update.callFailCause.getCode()) {
                    imsReasonCode = update.callFailCause.getCode();
                }
                if (this.callFailCause.getExtraCode() != update.callFailCause.getExtraCode()) {
                    imsReasonExtraCode = update.callFailCause.getExtraCode();
                }
                if (this.callFailCause.getExtraMessage() != update.callFailCause.getExtraMessage()) {
                    imsReasonExtraMessage = update.callFailCause.getExtraMessage();
                }
                this.callFailCause = new ImsReasonInfo(imsReasonCode, imsReasonExtraCode, imsReasonExtraMessage);
            }
        }
        if (this.callDetails.update(update.callDetails)) {
            changed |= 32;
        }
        boolean z5 = this.isEncrypted;
        boolean z6 = update.isEncrypted;
        if (z5 != z6) {
            this.isEncrypted = z6;
            changed |= 128;
        }
        if (!Objects.equals(update.historyInfo, this.historyInfo)) {
            this.historyInfo = update.historyInfo;
            changed |= UPDATE_HISTORY_INFO;
        }
        int i3 = this.mConfSupported;
        int i4 = update.mConfSupported;
        if (i3 != i4) {
            this.mConfSupported = i4;
            changed |= 512;
        }
        if (this.crsData.update(update.crsData)) {
            changed |= UPDATE_CRS_INFO;
        }
        boolean z7 = this.isPreparatory;
        boolean z8 = update.isPreparatory;
        if (z7 != z8) {
            this.isPreparatory = z8;
            changed |= UPDATE_CRS_INFO;
        }
        if (!Objects.equals(update.callProgressInfo, this.callProgressInfo)) {
            this.callProgressInfo = update.callProgressInfo;
            return changed | UPDATE_CALL_PROGRESS_INFO;
        }
        return changed;
    }

    public VerstatInfo getVerstatInfo() {
        return this.mVerstatInfo;
    }

    public CallComposerInfo getCallComposerInfo() {
        return this.mCallComposerInfo;
    }

    public boolean isConfSupportIndicated() {
        return (this.mConfSupported & 1) == 1;
    }

    public boolean isVideoConfSupported() {
        return (this.mConfSupported & 2) == 2;
    }

    public static int presentationFromCLIP(int cli) throws RuntimeException {
        switch (cli) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                throw new RuntimeException("illegal presentation " + cli);
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(DriverCallIms dc) {
        int i = this.index;
        int i2 = dc.index;
        if (i < i2) {
            return -1;
        }
        if (i == i2) {
            return 0;
        }
        return 1;
    }

    public String toString() {
        return "id=" + this.index + "," + this.state + ",toa=" + this.TOA + "," + (this.isMpty ? "conf" : "norm") + "," + (this.isMT ? "mt" : "mo") + "," + this.als + "," + (this.isVoice ? "voc" : "nonvoc") + "," + (this.isVoicePrivacy ? "evp" : "noevp") + ",,cli=" + this.numberPresentation + ",," + this.namePresentation + "Call Details =" + this.callDetails + ",CallFailCause Code= " + this.callFailCause.getCode() + ",CallFailCause String= " + this.callFailCause.getExtraMessage() + ", isEncrypted=" + this.isEncrypted + ", historyInfo=" + this.historyInfo + ", Conf. Support=" + this.mConfSupported + ", " + this.mMtMultiLineInfo + ", isTirOverwriteAllowed=" + this.isTirOverwriteAllowed + ", isPreparatory=" + this.isPreparatory + ", CRS data=" + this.crsData + ", Call progress Info = " + this.callProgressInfo;
    }
}
