package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class ImsRilException extends RuntimeException {
    private int mErrorCode;

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public ImsRilException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.mErrorCode = errorCode;
    }
}
