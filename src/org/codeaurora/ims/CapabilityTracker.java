package org.codeaurora.ims;

import android.telephony.ims.feature.CapabilityChangeRequest;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class CapabilityTracker {
    private boolean mIsCallComposerSupported = false;
    private boolean mIsUssdSupported = false;
    private CopyOnWriteArrayList<CapabilityChangeRequest.CapabilityPair> mCapabilityContainer = new CopyOnWriteArrayList<>();

    public void addCapability(int capability, int radioTech) {
        CapabilityChangeRequest.CapabilityPair cp = new CapabilityChangeRequest.CapabilityPair(capability, radioTech);
        if (!this.mCapabilityContainer.contains(cp)) {
            this.mCapabilityContainer.add(cp);
        }
    }

    public void removeCapability(int capability, int radioTech) {
        CapabilityChangeRequest.CapabilityPair cp = new CapabilityChangeRequest.CapabilityPair(capability, radioTech);
        if (this.mCapabilityContainer.contains(cp)) {
            this.mCapabilityContainer.remove(cp);
        }
    }

    public void clear() {
        this.mCapabilityContainer.clear();
        this.mIsCallComposerSupported = false;
        this.mIsUssdSupported = false;
    }

    public boolean isVideoSupported() {
        return isSupported(2);
    }

    public boolean isVoiceSupported() {
        return isSupported(1);
    }

    public boolean isVideoSupportedOverWifi() {
        return isSupportedOnRadioTech(2, 1);
    }

    public boolean isVoiceSupportedOverWifi() {
        return isSupportedOnRadioTech(1, 1);
    }

    public CopyOnWriteArrayList<CapabilityChangeRequest.CapabilityPair> getEnabledCapabilities() {
        return this.mCapabilityContainer;
    }

    public boolean isSupported(int capability) {
        for (CapabilityChangeRequest.CapabilityPair cp : this.mCapabilityContainer) {
            if (cp.getCapability() == capability) {
                return true;
            }
        }
        return false;
    }

    public boolean isSupportedOnRadioTech(int capability, int radioTech) {
        for (CapabilityChangeRequest.CapabilityPair cp : this.mCapabilityContainer) {
            if (cp.getCapability() == capability && cp.getRadioTech() == radioTech) {
                return true;
            }
        }
        return false;
    }

    public void setIsCallComposerSupported(boolean isSupported) {
        this.mIsCallComposerSupported = isSupported;
    }

    public boolean isCallComposerSupported() {
        return this.mIsCallComposerSupported;
    }

    public void setIsUssdSupported(boolean enabled) {
        this.mIsUssdSupported = enabled;
    }

    public boolean isUssdSupported() {
        return this.mIsUssdSupported;
    }
}
