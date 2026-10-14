package org.codeaurora.ims;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ConfInfo {
    public static final int BACKGROUND = 2;
    public static final int FOREGROUND = 1;
    public static final int INVALID = -1;
    public static final int RINGING = 0;
    private byte[] mConfInfoUri = null;
    private int mConfCallState = -1;

    public byte[] getConfInfoUri() {
        return this.mConfInfoUri;
    }

    public int getConfCallState() {
        return this.mConfCallState;
    }

    public void setConfInfoUri(ArrayList<Byte> uri) {
        this.mConfInfoUri = ImsRadioUtils.getByteArray(uri);
    }

    public void setConfCallState(int state) {
        this.mConfCallState = state;
    }

    public String toString() {
        return "Conf call state: " + this.mConfCallState + " Conf info uri: " + this.mConfInfoUri;
    }
}
