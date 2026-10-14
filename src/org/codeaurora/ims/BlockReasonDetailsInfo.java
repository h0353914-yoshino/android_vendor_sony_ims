package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class BlockReasonDetailsInfo {
    public static final int REG_FAILURE_REASON_TYPE_3GPP_SPEC_DEFINED = 5;
    public static final int REG_FAILURE_REASON_TYPE_CALL_MANAGER_DEFINED = 4;
    public static final int REG_FAILURE_REASON_TYPE_EHRPD = 7;
    public static final int REG_FAILURE_REASON_TYPE_HANDOFF = 10;
    public static final int REG_FAILURE_REASON_TYPE_INTERNAL = 3;
    public static final int REG_FAILURE_REASON_TYPE_INVALID = -1;
    public static final int REG_FAILURE_REASON_TYPE_IPV6 = 8;
    public static final int REG_FAILURE_REASON_TYPE_IWLAN = 9;
    public static final int REG_FAILURE_REASON_TYPE_MOBILE_IP = 2;
    public static final int REG_FAILURE_REASON_TYPE_PPP = 6;
    public static final int REG_FAILURE_REASON_TYPE_UNSPECIFIED = 1;
    private int mRegFailureReason;
    private int mRegFailureReasonType;

    public BlockReasonDetailsInfo() {
        this(-1, RadioTech.RADIO_TECH_INVALID);
    }

    public BlockReasonDetailsInfo(int reasonType, int reason) {
        this.mRegFailureReasonType = reasonType;
        this.mRegFailureReason = reason;
    }

    public void setRegFailureReasonType(int value) {
        this.mRegFailureReasonType = value;
    }

    public int getRegFailureReasonType() {
        return this.mRegFailureReasonType;
    }

    public void setRegFailureReason(int value) {
        this.mRegFailureReason = value;
    }

    public int getRegFailureReason() {
        return this.mRegFailureReason;
    }

    public String toString() {
        return "BlockReasonDetailsInfo regFailureReasonType: " + this.mRegFailureReasonType + " regFailureReason: " + this.mRegFailureReason;
    }
}
