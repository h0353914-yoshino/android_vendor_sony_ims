package org.codeaurora.ims.sms;

/* JADX INFO: loaded from: classes.dex */
public class SmsResponse {
    private final int mMessageRef;
    private final int mSendSmsReason;
    private final int mSendSmsResult;

    public SmsResponse(int msgRef, int result, int reason) {
        this.mMessageRef = msgRef;
        this.mSendSmsResult = result;
        this.mSendSmsReason = reason;
    }

    public int getMsgRef() {
        return this.mMessageRef;
    }

    public int getResult() {
        return this.mSendSmsResult;
    }

    public int getReason() {
        return this.mSendSmsReason;
    }

    public String toString() {
        return "{ mMessageRef = " + this.mMessageRef + ", mSendSmsResult = " + this.mSendSmsResult + ", mSendSmsReason = " + this.mSendSmsReason + "}";
    }
}
