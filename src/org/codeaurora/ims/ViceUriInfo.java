package org.codeaurora.ims;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ViceUriInfo {
    private final byte[] mViceInfoUri;

    public ViceUriInfo() {
        this.mViceInfoUri = null;
    }

    public ViceUriInfo(ArrayList<Byte> uri) {
        this.mViceInfoUri = ImsRadioUtils.getByteArray(uri);
    }

    public byte[] getViceInfoUri() {
        return this.mViceInfoUri;
    }
}
