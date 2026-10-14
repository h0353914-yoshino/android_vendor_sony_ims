package org.codeaurora.ims;

import android.telephony.ims.ImsReasonInfo;

/* JADX INFO: loaded from: classes.dex */
public class CallForwardStatus {
    private final int FAILED;
    private final int INVALID;
    private int reason;
    private ImsReasonInfo sipErrorInfo;
    private int status;

    public CallForwardStatus(int reason, int status, ImsReasonInfo sipError) {
        this.sipErrorInfo = null;
        this.INVALID = -1;
        this.FAILED = 1;
        this.reason = reason;
        this.status = status;
        this.sipErrorInfo = sipError;
    }

    public CallForwardStatus() {
        this.sipErrorInfo = null;
        this.INVALID = -1;
        this.FAILED = 1;
        this.reason = -1;
        this.status = 1;
        this.sipErrorInfo = null;
    }

    public String toString() {
        return "{ CallForwardStatus : reason: " + this.reason + " status: " + this.status + " ImsReasonInfo : " + this.sipErrorInfo + " }";
    }

    public int getReason() {
        return this.reason;
    }

    public int getStatus() {
        return this.status;
    }

    public ImsReasonInfo getSipErrorInfo() {
        return this.sipErrorInfo;
    }
}
