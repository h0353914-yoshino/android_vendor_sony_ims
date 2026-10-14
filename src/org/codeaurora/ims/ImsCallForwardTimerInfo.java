package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public class ImsCallForwardTimerInfo {
    public int endHour;
    public int endMinute;
    public String number;
    public int reason;
    public int serviceClass;
    public int startHour;
    public int startMinute;
    public int status;

    public String toString() {
        return super.toString() + (this.status == 0 ? " not active " : " active ") + " reason: " + this.reason + " serviceClass: " + this.serviceClass + "number:" + this.number + " startHour:" + this.startHour + " startMinute: " + this.startMinute + " endHour: " + this.endHour + " endMinute: " + this.endMinute;
    }
}
