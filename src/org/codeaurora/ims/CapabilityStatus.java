package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class CapabilityStatus {
    private static final int INVALID_CAPABILITY = -1;
    private int mCapability;
    private int mRadioTech;
    private int mStatus;

    public CapabilityStatus(int capability, int radioTech, int status) {
        this.mCapability = capability;
        this.mRadioTech = radioTech;
        this.mStatus = status;
    }

    public int getRadioTech() {
        return this.mRadioTech;
    }

    public int getCapability() {
        return this.mCapability;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public String toString() {
        return " " + this.mCapability + " " + this.mRadioTech + " " + this.mStatus;
    }
}
