package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class ExtraInfo {
    public static final int INVALID = -1;
    public static final int LTE_TO_IWLAN_HO_FAIL = 1;
    private final byte[] mExtraInfo;
    private final int mExtraType;

    public ExtraInfo(int type, byte[] info) {
        this.mExtraType = type;
        this.mExtraInfo = info;
    }

    public String toString() {
        return "ExtraInfo extraType: " + this.mExtraType;
    }
}
