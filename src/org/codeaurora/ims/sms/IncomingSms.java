package org.codeaurora.ims.sms;

/* JADX INFO: loaded from: classes.dex */
public class IncomingSms {
    private final String mFormat;
    private final byte[] mPdu;
    private final int mVerstat;

    public IncomingSms(String format, byte[] pdu, int verstat) {
        this.mFormat = format;
        this.mPdu = pdu;
        this.mVerstat = verstat;
    }

    public String getFormat() {
        return this.mFormat;
    }

    public byte[] getPdu() {
        return this.mPdu;
    }

    public int getVerstat() {
        return this.mVerstat;
    }

    public String toString() {
        return "{ mFormat = " + this.mFormat + " verstat = " + this.mVerstat + "}";
    }
}
