package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class RegistrationBlockStatusInfo {
    private BlockStatusInfo mIsOnWlan;
    private BlockStatusInfo mIsOnWwan;

    public RegistrationBlockStatusInfo() {
        this(null, null);
    }

    public RegistrationBlockStatusInfo(BlockStatusInfo onWwan, BlockStatusInfo onWlan) {
        this.mIsOnWwan = onWwan;
        this.mIsOnWlan = onWlan;
    }

    public void setStatusOnWwan(BlockStatusInfo value) {
        this.mIsOnWwan = value;
    }

    public BlockStatusInfo getStatusOnWwan() {
        return this.mIsOnWwan;
    }

    public void setStatusOnWlan(BlockStatusInfo value) {
        this.mIsOnWlan = value;
    }

    public BlockStatusInfo getStatusOnWlan() {
        return this.mIsOnWlan;
    }
}
