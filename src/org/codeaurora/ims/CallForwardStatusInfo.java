package org.codeaurora.ims;

import android.telephony.ims.ImsReasonInfo;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class CallForwardStatusInfo {
    private CallForwardStatus[] callForwardStatus;
    private ImsReasonInfo sipErrorInfo;

    public CallForwardStatusInfo(ImsReasonInfo sipError, CallForwardStatus[] cfStatus) {
        this.sipErrorInfo = null;
        this.callForwardStatus = null;
        this.sipErrorInfo = sipError;
        this.callForwardStatus = cfStatus;
    }

    public CallForwardStatusInfo() {
        this.sipErrorInfo = null;
        this.callForwardStatus = null;
    }

    public String toString() {
        return " { CallForwardStatusInfo :: sipErrorInfo:" + this.sipErrorInfo + " Individual status: " + Arrays.toString(this.callForwardStatus) + "}";
    }

    public ImsReasonInfo getSipErrorInfo() {
        return this.sipErrorInfo;
    }

    public CallForwardStatus[] getCallForwardStatus() {
        return this.callForwardStatus;
    }
}
