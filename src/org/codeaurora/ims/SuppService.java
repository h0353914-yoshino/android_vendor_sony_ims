package org.codeaurora.ims;

import android.telephony.ims.ImsReasonInfo;

/* JADX INFO: loaded from: classes.dex */
public class SuppService {
    public static final int IP_PRESENTATION_NUM_ALLOWED = 0;
    public static final int IP_PRESENTATION_NUM_DEFAULT = 2;
    public static final int IP_PRESENTATION_NUM_RESTRICTED = 1;
    public static final int NOT_PROVISIONED = 0;
    public static final int PROVISIONED = 1;
    public static final int STATUS_UNKNOWN = 2;
    private ImsReasonInfo mErrorDetails;
    private int mPresentation;
    private int mProvisionStatus;
    private int mServiceClassStatus;
    private int mStatus;

    public SuppService() {
        this(-1, 2, 0, 2, null);
    }

    public SuppService(int serviceClassStatus, int provisionStatus, int status, int presentation, ImsReasonInfo errorInfo) {
        this.mServiceClassStatus = serviceClassStatus;
        this.mProvisionStatus = provisionStatus;
        this.mStatus = status;
        this.mPresentation = presentation;
        this.mErrorDetails = errorInfo;
    }

    public void setServiceClassStatus(int status) {
        this.mServiceClassStatus = status;
    }

    public int getServiceClassStatus() {
        return this.mServiceClassStatus;
    }

    public void setProvisionStatus(int status) {
        this.mProvisionStatus = status;
    }

    public int getProvisionStatus() {
        return this.mProvisionStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setPresentation(int presentation) {
        this.mPresentation = presentation;
    }

    public int getPresentation() {
        return this.mPresentation;
    }

    public void setErrorDetails(ImsReasonInfo errorInfo) {
        this.mErrorDetails = errorInfo;
    }

    public ImsReasonInfo getErrorDetails() {
        return this.mErrorDetails;
    }

    public String toString() {
        return "SuppService status: " + this.mStatus + (this.mErrorDetails != null ? " errorCode: " + this.mErrorDetails.getExtraCode() + " errorMessage: " + this.mErrorDetails.getExtraMessage() : "");
    }
}
