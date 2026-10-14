package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class ParticipantStatusDetails {
    public static final int ADD = 1;
    public static final int INVALID = -1;
    public static final int REMOVE = 0;
    private int mCallId = RadioTech.RADIO_TECH_INVALID;
    private int mOperation = -1;
    private int mSipStatus = RadioTech.RADIO_TECH_INVALID;
    private String mParticipantUri = "";
    private boolean mIsExplicitTransfer = false;

    public void setCallId(int id) {
        this.mCallId = id;
    }

    public int getCallId() {
        return this.mCallId;
    }

    public void setOperation(int operation) {
        this.mOperation = operation;
    }

    public int getOperation() {
        return this.mOperation;
    }

    public void setSipStatus(int status) {
        this.mSipStatus = status;
    }

    public int getSipStatus() {
        return this.mSipStatus;
    }

    public void setParticipantUri(String uri) {
        this.mParticipantUri = uri;
    }

    public String getParticipantUri() {
        return this.mParticipantUri;
    }

    public void setIsEct(boolean isTransfer) {
        this.mIsExplicitTransfer = isTransfer;
    }

    public boolean getIsEct() {
        return this.mIsExplicitTransfer;
    }

    public String toString() {
        return "ParticipantStatusDetails callId: " + this.mCallId + " operation: " + this.mOperation + " sipStatus: " + this.mSipStatus + " participantUri: " + this.mParticipantUri + " isExplicitTransfer: " + this.mIsExplicitTransfer;
    }
}
