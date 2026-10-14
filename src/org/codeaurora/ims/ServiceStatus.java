package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class ServiceStatus {
    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ENABLED = 2;
    public static final int STATUS_NOT_SUPPORTED = 3;
    public static final int STATUS_PARTIALLY_ENABLED = 1;
    public StatusForAccessTech[] accessTechStatus;
    public boolean isValid = false;
    public int rttMode;
    public int status;
    public int type;
    public byte[] userdata;

    public boolean isRadioTech5G() {
        StatusForAccessTech statusForAccessTech;
        StatusForAccessTech[] statusForAccessTechArr = this.accessTechStatus;
        return (statusForAccessTechArr == null || (statusForAccessTech = statusForAccessTechArr[0]) == null || statusForAccessTech.networkMode != 20) ? false : true;
    }

    public static class StatusForAccessTech {
        public int networkMode;
        public int registered;
        public int restrictCause;
        public int status;

        public String toString() {
            return " mode = " + this.networkMode + " Status = " + this.status + " restrictCause = " + this.restrictCause + " registered = " + this.registered;
        }
    }
}
