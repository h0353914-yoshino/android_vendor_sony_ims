package org.codeaurora.ims.sms;

/* JADX INFO: loaded from: classes.dex */
public class StatusReport {
    private final String mFormat;
    private final int mMessageRef;
    private final byte[] mPdu;

    public StatusReport(int messageRef, String format, byte[] pdu) {
        this.mMessageRef = messageRef;
        this.mFormat = format;
        this.mPdu = pdu;
    }

    public int getMsgRef() {
        return this.mMessageRef;
    }

    public String getFormat() {
        return this.mFormat;
    }

    public byte[] getPdu() {
        return this.mPdu;
    }

    public String toString() {
        return "{ mMessageRef = " + this.mMessageRef + ", mFormat = " + this.mFormat + "}";
    }
}
