package org.codeaurora.ims.parser;

/* JADX INFO: loaded from: classes.dex */
public class InvalidConfVersionException extends Exception {
    String mMessage;

    public InvalidConfVersionException(String message) {
        super(message);
        this.mMessage = message;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "InvalidConfVersionException" + this.mMessage;
    }
}
