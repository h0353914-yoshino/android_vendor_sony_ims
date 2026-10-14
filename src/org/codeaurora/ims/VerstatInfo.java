package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class VerstatInfo {
    public static final int VERSTAT_VERIFICATION_FAIL = 2;
    public static final int VERSTAT_VERIFICATION_NONE = 0;
    public static final int VERSTAT_VERIFICATION_PASS = 1;
    private boolean canMarkUnwantedCall;
    private int verstatVerificationStatus;

    public VerstatInfo() {
        this.canMarkUnwantedCall = false;
        this.verstatVerificationStatus = 0;
    }

    public VerstatInfo(boolean canMarkUnwantedCall, int verstatVerificationStatus) {
        this.canMarkUnwantedCall = canMarkUnwantedCall;
        this.verstatVerificationStatus = verstatVerificationStatus;
    }

    public int getVerstatVerificationStatus() {
        return this.verstatVerificationStatus;
    }

    public boolean canMarkUnwantedCall() {
        return this.canMarkUnwantedCall;
    }

    public String toString() {
        return "VerstatInfo : canMarkUnwantedCall = " + this.canMarkUnwantedCall + " , verstatVerificationStatus = " + this.verstatVerificationStatus;
    }
}
