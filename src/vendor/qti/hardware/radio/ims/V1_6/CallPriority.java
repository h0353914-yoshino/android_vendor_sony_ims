package vendor.qti.hardware.radio.ims.V1_6;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class CallPriority {
    public static final int NORMAL = 1;
    public static final int URGENT = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return "URGENT";
        }
        if (o == 1) {
            return "NORMAL";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("URGENT");
        if ((o & 1) == 1) {
            list.add("NORMAL");
            flipped = 0 | 1;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
