package vendor.qti.hardware.radio.ims.V1_5;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class TirMode {
    public static final int INVALID = 0;
    public static final int PERMANENT = 2;
    public static final int TEMPORARY = 1;

    public static final String toString(int o) {
        if (o == 0) {
            return "INVALID";
        }
        if (o == 1) {
            return "TEMPORARY";
        }
        if (o == 2) {
            return "PERMANENT";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("INVALID");
        if ((o & 1) == 1) {
            list.add("TEMPORARY");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("PERMANENT");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
