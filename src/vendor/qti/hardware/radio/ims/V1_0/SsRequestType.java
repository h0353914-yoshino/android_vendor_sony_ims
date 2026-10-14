package vendor.qti.hardware.radio.ims.V1_0;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class SsRequestType {
    public static final int SS_ACTIVATION = 0;
    public static final int SS_DEACTIVATION = 1;
    public static final int SS_ERASURE = 4;
    public static final int SS_INTERROGATION = 2;
    public static final int SS_INVALID = 5;
    public static final int SS_REGISTRATION = 3;

    public static final String toString(int o) {
        if (o == 0) {
            return "SS_ACTIVATION";
        }
        if (o == 1) {
            return "SS_DEACTIVATION";
        }
        if (o == 2) {
            return "SS_INTERROGATION";
        }
        if (o == 3) {
            return "SS_REGISTRATION";
        }
        if (o == 4) {
            return "SS_ERASURE";
        }
        if (o == 5) {
            return "SS_INVALID";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("SS_ACTIVATION");
        if ((o & 1) == 1) {
            list.add("SS_DEACTIVATION");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("SS_INTERROGATION");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("SS_REGISTRATION");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("SS_ERASURE");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("SS_INVALID");
            flipped |= 5;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
