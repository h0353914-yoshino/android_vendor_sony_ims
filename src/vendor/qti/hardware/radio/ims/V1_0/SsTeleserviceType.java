package vendor.qti.hardware.radio.ims.V1_0;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class SsTeleserviceType {
    public static final int SS_ALL_DATA_TELESERVICES = 3;
    public static final int SS_ALL_TELESERVICES_EXCEPT_SMS = 5;
    public static final int SS_ALL_TELESEVICES = 1;
    public static final int SS_ALL_TELE_AND_BEARER_SERVICES = 0;
    public static final int SS_INVALID = 6;
    public static final int SS_SMS_SERVICES = 4;
    public static final int SS_TELEPHONY = 2;

    public static final String toString(int o) {
        if (o == 0) {
            return "SS_ALL_TELE_AND_BEARER_SERVICES";
        }
        if (o == 1) {
            return "SS_ALL_TELESEVICES";
        }
        if (o == 2) {
            return "SS_TELEPHONY";
        }
        if (o == 3) {
            return "SS_ALL_DATA_TELESERVICES";
        }
        if (o == 4) {
            return "SS_SMS_SERVICES";
        }
        if (o == 5) {
            return "SS_ALL_TELESERVICES_EXCEPT_SMS";
        }
        if (o == 6) {
            return "SS_INVALID";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("SS_ALL_TELE_AND_BEARER_SERVICES");
        if ((o & 1) == 1) {
            list.add("SS_ALL_TELESEVICES");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("SS_TELEPHONY");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("SS_ALL_DATA_TELESERVICES");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("SS_SMS_SERVICES");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("SS_ALL_TELESERVICES_EXCEPT_SMS");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("SS_INVALID");
            flipped |= 6;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
