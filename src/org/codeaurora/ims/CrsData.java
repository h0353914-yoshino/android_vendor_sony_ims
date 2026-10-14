package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class CrsData {
    private int mCrsType;
    private int mOriginalCallType;

    public CrsData() {
        this(0, 10);
    }

    public CrsData(CrsData crsData) {
        this(crsData.getCrsType(), crsData.getOriginalCallType());
    }

    public CrsData(int crsType, int originalCallType) {
        this.mCrsType = 0;
        this.mOriginalCallType = 10;
        this.mCrsType = crsType;
        this.mOriginalCallType = originalCallType;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CrsData)) {
            return false;
        }
        CrsData in = (CrsData) obj;
        return this.mCrsType == in.getCrsType() && this.mOriginalCallType == in.getOriginalCallType();
    }

    public int getCrsType() {
        return this.mCrsType;
    }

    public int getOriginalCallType() {
        return this.mOriginalCallType;
    }

    public void setCrsType(int crsType) {
        this.mCrsType = crsType;
    }

    public void setOriginalCallType(int originalCallType) {
        this.mOriginalCallType = originalCallType;
    }

    public boolean update(CrsData update) {
        boolean hasChanged = false;
        if (update == null) {
            return false;
        }
        if (this.mCrsType != update.getCrsType()) {
            this.mCrsType = update.getCrsType();
            hasChanged = true;
        }
        if (this.mOriginalCallType != update.getOriginalCallType()) {
            this.mOriginalCallType = update.getOriginalCallType();
            return true;
        }
        return hasChanged;
    }

    public String toString() {
        return "CrsData crsType: " + this.mCrsType + " originalCallType: " + this.mOriginalCallType;
    }
}
