package org.codeaurora.ims;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class CallProgressInfo {
    private int mReasonCode;
    private String mReasonText;
    private int mType;

    public CallProgressInfo() {
        this.mType = -1;
        this.mReasonCode = -1;
        this.mReasonText = null;
    }

    public CallProgressInfo(CallProgressInfo callProgInfo) {
        this(callProgInfo.getType(), callProgInfo.getReasonCode(), callProgInfo.getReasonText());
    }

    public CallProgressInfo(int type, int reasonCode, String reasonText) {
        this.mType = -1;
        this.mReasonCode = -1;
        this.mReasonText = null;
        this.mType = type;
        this.mReasonCode = reasonCode;
        this.mReasonText = reasonText;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CallProgressInfo)) {
            return false;
        }
        CallProgressInfo in = (CallProgressInfo) obj;
        return this.mType == in.getType() && this.mReasonCode == in.getReasonCode() && Objects.equals(this.mReasonText, in.getReasonText());
    }

    public int getType() {
        return this.mType;
    }

    public int getReasonCode() {
        return this.mReasonCode;
    }

    public String getReasonText() {
        return this.mReasonText;
    }

    public void setType(int callProgType) {
        this.mType = callProgType;
    }

    public void setReasonCode(int reasonCode) {
        this.mReasonCode = reasonCode;
    }

    public void setReasonText(String reasonText) {
        this.mReasonText = reasonText;
    }

    public String toString() {
        return "CallProgressInfo Type: " + this.mType + " Reason code: " + this.mReasonCode + " Reason Text: " + this.mReasonText;
    }
}
