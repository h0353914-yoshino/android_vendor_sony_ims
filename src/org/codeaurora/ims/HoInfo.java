package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class HoInfo {
    public static final int CANCEL = 3;
    public static final int COMPLETE_FAIL = 2;
    public static final int COMPLETE_SUCCESS = 1;
    public static final int INVALID = -1;
    public static final int NOT_TRIGGERED = 4;
    public static final int NOT_TRIGGERED_MOBILE_DATA_OFF = 5;
    public static final int START = 0;
    private int mType = -1;
    private int mSrcTech = RadioTech.RADIO_TECH_INVALID;
    private int mTargetTech = RadioTech.RADIO_TECH_INVALID;
    private int mExtraType = -1;
    private byte[] mExtraInfo = null;
    private String mErrorCode = "";
    private String mErrorMessage = "";

    public void setType(int value) {
        this.mType = value;
    }

    public int getType() {
        return this.mType;
    }

    public void setSrcTech(int value) {
        this.mSrcTech = value;
    }

    public int getSrcTech() {
        return this.mSrcTech;
    }

    public void setTargetTech(int value) {
        this.mTargetTech = value;
    }

    public int getTargetTech() {
        return this.mTargetTech;
    }

    public void setExtraType(int value) {
        this.mExtraType = value;
    }

    public int getExtraType() {
        return this.mExtraType;
    }

    public void setExtraInfo(byte[] value) {
        this.mExtraInfo = value;
    }

    public byte[] getExtraInfo() {
        return this.mExtraInfo;
    }

    public void setExtra(int type, byte[] info) {
        setExtraType(type);
        setExtraInfo(info);
    }

    public void setErrorCode(String value) {
        this.mErrorCode = value;
    }

    public String getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorMessage(String value) {
        this.mErrorMessage = value;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }
}
