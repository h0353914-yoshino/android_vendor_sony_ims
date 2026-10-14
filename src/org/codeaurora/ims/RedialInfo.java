package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class RedialInfo {
    private int retryCallFailCause;
    private int retryCallFailRadioTech;

    public RedialInfo(int retryCallFailCause, int retryCallFailRadioTech) {
        this.retryCallFailCause = retryCallFailCause;
        this.retryCallFailRadioTech = retryCallFailRadioTech;
    }

    public int getRetryCallFailCause() {
        return this.retryCallFailCause;
    }

    public int getRetryCallFailRadioTech() {
        return this.retryCallFailRadioTech;
    }

    public String toString() {
        return "RedialInfo: retryCallFailCause = " + this.retryCallFailCause + " retryCallFailRadioTech = " + this.retryCallFailRadioTech;
    }
}
