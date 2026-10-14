package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class BlockStatusInfo {
    public static final int REASON_HANDOVER_FAILURE = 3;
    public static final int REASON_INVALID = -1;
    public static final int REASON_OTHER_FAILURE = 4;
    public static final int REASON_PDP_FAILURE = 1;
    public static final int REASON_REGISTRATION_FAILURE = 2;
    private int mReason;
    private BlockReasonDetailsInfo mReasonDetails;

    public BlockStatusInfo() {
        this(-1, null);
    }

    public BlockStatusInfo(int reason, BlockReasonDetailsInfo info) {
        this.mReason = reason;
        this.mReasonDetails = info;
    }

    public void setReason(int value) {
        this.mReason = value;
    }

    public int getReason() {
        return this.mReason;
    }

    public void setReasonDetails(BlockReasonDetailsInfo value) {
        this.mReasonDetails = value;
    }

    public BlockReasonDetailsInfo getReasonDetails() {
        return this.mReasonDetails;
    }

    public String toString() {
        return "BlockStatusInfo blockReason: " + this.mReason;
    }
}
