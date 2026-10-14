package org.codeaurora.ims;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class VirtualLineInfo {
    public String mMsisdn;
    public ArrayList<String> mVirtualInfo;

    public VirtualLineInfo(String msisdn, ArrayList<String> info) {
        this.mMsisdn = msisdn;
        this.mVirtualInfo = info;
    }

    public String toString() {
        return "{VirtualLineInfo : msisdn = " + this.mMsisdn + " , p-associated URIs = " + this.mVirtualInfo + "}";
    }
}
