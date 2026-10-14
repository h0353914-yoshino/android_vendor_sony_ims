package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class SsacInfo {
    private int mBarringFactorVoice = 0;
    private int mBarringTimeVoice = 0;
    private int mBarringFactorVideo = 0;
    private int mBarringTimeVideo = 0;
    private int mBarringFactorVoiceSib = 0;
    private int mBarringTimeVoiceSib = 0;
    private int mBarringFactorVideoSib = 0;
    private int mBarringTimeVideoSib = 0;

    public int getBarringFactorVoice() {
        return this.mBarringFactorVoice;
    }

    public String toString() {
        return "SsacInfo barringFactorVoice: " + this.mBarringFactorVoice + " barringTimeVoice: " + this.mBarringTimeVoice + " barringFactorVideo: " + this.mBarringFactorVideo + " barringTimeVideo: " + this.mBarringTimeVideo + " barringFactorVoiceSib: " + this.mBarringFactorVoiceSib + " barringTimeVoiceSib: " + this.mBarringTimeVoiceSib + " barringFactorVideoSib: " + this.mBarringFactorVideoSib + " barringTimeVideoSib: " + this.mBarringTimeVideoSib;
    }
}
