package org.codeaurora.ims;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ImsSubConfigDetails {
    private int mSimultStackCount = 0;
    private final List<Boolean> mImsStackEnabled = new ArrayList();

    public void setSimultStackCount(int count) {
        this.mSimultStackCount = count;
    }

    public int getSimultStackCount() {
        return this.mSimultStackCount;
    }

    public void addImsStackEnabled(boolean value) {
        this.mImsStackEnabled.add(Boolean.valueOf(value));
    }

    public List<Boolean> getImsStackEnabledList() {
        return this.mImsStackEnabled;
    }

    public String toString() {
        return "ImsSubConfigDetails simultStackCount: " + this.mSimultStackCount + " mImsStackEnabled: " + Arrays.toString(this.mImsStackEnabled.toArray());
    }
}
