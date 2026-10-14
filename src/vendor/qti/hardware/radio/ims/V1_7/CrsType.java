package vendor.qti.hardware.radio.ims.V1_7;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class CrsType {
    public static final int AUDIO = 1;
    public static final int INVALID = 0;
    public static final int VIDEO = 2;

    public static final String toString(int o) {
        if (o == 0) {
            return "INVALID";
        }
        if (o == 1) {
            return "AUDIO";
        }
        if (o == 2) {
            return "VIDEO";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("INVALID");
        if ((o & 1) == 1) {
            list.add("AUDIO");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("VIDEO");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
