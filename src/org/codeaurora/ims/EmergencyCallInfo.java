package org.codeaurora.ims;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class EmergencyCallInfo {
    private int mEmergencyCallRouting;
    private boolean mEmergencyCallTesting;
    private int mEmergencyServiceCategories;
    private List<String> mEmergencyUrns;
    private boolean mHasKnownUserIntentEmergency;

    public EmergencyCallInfo() {
        this(0, new ArrayList(), 0, false, false);
    }

    public EmergencyCallInfo(int categories, List<String> urns, int routing, boolean isTesting, boolean hasIntentEmergency) {
        this.mEmergencyServiceCategories = categories;
        this.mEmergencyUrns = urns;
        this.mEmergencyCallRouting = routing;
        this.mEmergencyCallTesting = isTesting;
        this.mHasKnownUserIntentEmergency = hasIntentEmergency;
    }

    public void setEmergencyServiceCategories(int categories) {
        this.mEmergencyServiceCategories = categories;
    }

    public void setEmergencyUrns(List<String> urns) {
        this.mEmergencyUrns = urns;
    }

    public void setEmergencyCallRouting(int routing) {
        this.mEmergencyCallRouting = routing;
    }

    public void setEmergencyCallTesting(boolean isTesting) {
        this.mEmergencyCallTesting = isTesting;
    }

    public void setHasKnownUserIntentEmergency(boolean hasIntentEmergency) {
        this.mHasKnownUserIntentEmergency = hasIntentEmergency;
    }

    public int getEmergencyServiceCategories() {
        return this.mEmergencyServiceCategories;
    }

    public List<String> getEmergencyUrns() {
        return this.mEmergencyUrns;
    }

    public int getEmergencyCallRouting() {
        return this.mEmergencyCallRouting;
    }

    public boolean isEmergencyCallTesting() {
        return this.mEmergencyCallTesting;
    }

    public boolean hasKnownUserIntentEmergency() {
        return this.mHasKnownUserIntentEmergency;
    }

    public String toString() {
        return "EmergencyCallInfo mEmergencyServiceCategories " + this.mEmergencyServiceCategories + " mEmergencyUrns " + this.mEmergencyUrns + " mEmergencyCallRouting " + this.mEmergencyCallRouting + " mEmergencyCallTesting " + this.mEmergencyCallTesting + " mHasKnownUserIntentEmergency " + this.mHasKnownUserIntentEmergency;
    }
}
