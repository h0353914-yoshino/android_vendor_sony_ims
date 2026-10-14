package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class SuppNotifyInfo {
    public static final int MO = 0;
    public static final int MT = 1;
    private boolean hasHoldTone = false;
    private int mNotificationType = 0;
    private int mCode = 0;
    private int mIndex = 0;
    private int mType = 0;
    private String mNumber = "";
    private int mConnId = 0;
    private String mHistoryInfo = "";
    private boolean mHoldTone = false;

    public void setNotificationType(int value) {
        this.mNotificationType = value;
    }

    public int getNotificationType() {
        return this.mNotificationType;
    }

    public void setCode(int value) {
        this.mCode = value;
    }

    public int getCode() {
        return this.mCode;
    }

    public void setIndex(int value) {
        this.mIndex = value;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void setType(int value) {
        this.mType = value;
    }

    public int getType() {
        return this.mType;
    }

    public void setNumber(String value) {
        this.mNumber = value;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public void setConnId(int value) {
        this.mConnId = value;
    }

    public int getConnId() {
        return this.mConnId;
    }

    public void setHistoryInfo(String value) {
        this.mHistoryInfo = value;
    }

    public String getHistoryInfo() {
        return this.mHistoryInfo;
    }

    public void setHoldTone(boolean value) {
        this.mHoldTone = value;
        this.hasHoldTone = true;
    }

    public boolean getHoldTone() {
        return this.mHoldTone;
    }

    public boolean hasHoldTone() {
        return this.hasHoldTone;
    }

    public String toString() {
        return "SuppNotifyInfo notificationType: " + this.mNotificationType + " code: " + this.mCode + " index: " + this.mIndex + " type: " + this.mType + " number: " + this.mNumber + " connId: " + this.mConnId + " historyInfo: " + this.mHistoryInfo + " holdTone: " + this.mHoldTone;
    }
}
