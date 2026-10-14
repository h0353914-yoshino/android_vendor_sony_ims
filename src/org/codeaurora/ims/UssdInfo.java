package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class UssdInfo {
    public static final int INVALID = -1;
    private int mErrorCode;
    private String mErrorMessage;
    private String mMessage;
    private int mType;

    public UssdInfo(int type, String msg, int errorCode, String errMsg) {
        this.mType = type;
        this.mMessage = msg;
        this.mErrorCode = errorCode == Integer.MAX_VALUE ? -1 : errorCode;
        this.mErrorMessage = errMsg;
    }

    public int getType() {
        return this.mType;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public String toString() {
        return "UssdInfo type: " + this.mType + " message: " + this.mMessage + " errorCode: " + this.mErrorCode + " errorMessage: " + this.mErrorMessage;
    }
}
