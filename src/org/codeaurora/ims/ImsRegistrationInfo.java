package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class ImsRegistrationInfo {
    public static final int INVALID = -1;
    public static final int NOT_REGISTERED = 2;
    public static final int REGISTERED = 1;
    public static final int REGISTERING = 3;
    private int mState = -1;
    private int mErrorCode = 0;
    private String mErrorMessage = null;
    private int mRadioTech = RadioTech.RADIO_TECH_INVALID;
    private String mPAssociatedUris = null;

    public void setState(int value) {
        this.mState = value;
    }

    public int getState() {
        if (this.mState == -1) {
            this.mState = 2;
        }
        return this.mState;
    }

    public void setErrorCode(int value) {
        this.mErrorCode = value;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorMessage(String value) {
        this.mErrorMessage = value;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public void setRadioTech(int value) {
        this.mRadioTech = value;
    }

    public int getRadioTech() {
        return this.mRadioTech;
    }

    public void setPAssociatedUris(String value) {
        this.mPAssociatedUris = value;
    }

    public String getPAssociatedUris() {
        return this.mPAssociatedUris;
    }

    public String toString() {
        return "ImsRegistrationInfo state: " + this.mState + " errorCode: " + this.mErrorCode + " errorMessage: " + this.mErrorMessage + " radioTech: " + this.mRadioTech + " pAssociatedUris" + this.mPAssociatedUris;
    }
}
