package org.codeaurora.ims;

import android.telephony.ims.ImsReasonInfo;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SuppSvcResponse {
    public static final int ACTIVATE = 1;
    public static final int DEACTIVATE = 2;
    public static final int DISABLED = 0;
    public static final int ENABLED = 1;
    public static final int ERASURE = 5;
    public static final int FACILITY_BAIC = 6;
    public static final int FACILITY_BAICa = 12;
    public static final int FACILITY_BAICr = 7;
    public static final int FACILITY_BAOC = 3;
    public static final int FACILITY_BAOIC = 4;
    public static final int FACILITY_BAOICxH = 5;
    public static final int FACILITY_BA_ALL = 8;
    public static final int FACILITY_BA_MO = 9;
    public static final int FACILITY_BA_MT = 10;
    public static final int FACILITY_BS_MT = 11;
    public static final int FACILITY_CLIP = 1;
    public static final int FACILITY_COLP = 2;
    public static final int INVALID = -1;
    public static final int QUERY = 3;
    public static final int REGISTER = 4;
    private int mStatus = -1;
    private int mProvisionStatus = 2;
    private int mFacilityType = 0;
    private String mFailureCause = "";
    private List<BarredLines> mBarredLines = new ArrayList();
    private ImsReasonInfo mErrorDetails = null;

    public static class LineStatus {
        private String mNumber;
        private int mStatus;

        public LineStatus(int status, String lineNumber) {
            this.mStatus = status;
            this.mNumber = lineNumber;
        }

        public String getNumber() {
            return this.mNumber;
        }

        public int getStatus() {
            return this.mStatus;
        }
    }

    public static class BarredLines {
        private List<LineStatus> mLines = new ArrayList();
        private int mServiceClass;

        public BarredLines(int serviceClass) {
            this.mServiceClass = serviceClass;
        }

        public void addLine(LineStatus line) {
            this.mLines.add(line);
        }

        public List<LineStatus> getLines() {
            return this.mLines;
        }
    }

    public void setStatus(int value) {
        this.mStatus = value;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setProvisionStatus(int status) {
        this.mProvisionStatus = status;
    }

    public int getProvisionStatus() {
        return this.mProvisionStatus;
    }

    public void setFacilityType(int value) {
        this.mFacilityType = value;
    }

    public int getFacilityType() {
        return this.mFacilityType;
    }

    public void setFailureCause(String value) {
        this.mFailureCause = value;
    }

    public String getFailureCause() {
        return this.mFailureCause;
    }

    public void addBarredLines(BarredLines lines) {
        this.mBarredLines.add(lines);
    }

    public List<BarredLines> getBarredLines() {
        return this.mBarredLines;
    }

    public void setErrorDetails(ImsReasonInfo value) {
        this.mErrorDetails = value;
    }

    public ImsReasonInfo getErrorDetails() {
        return this.mErrorDetails;
    }

    public String toString() {
        return "SuppSvcResponse status: " + this.mStatus + " facilityType: " + this.mFacilityType + " failureCause: " + this.mFailureCause + " provisionStatus: " + this.mProvisionStatus;
    }
}
