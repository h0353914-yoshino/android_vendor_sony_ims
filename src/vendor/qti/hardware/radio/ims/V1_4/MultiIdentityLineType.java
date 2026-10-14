package vendor.qti.hardware.radio.ims.V1_4;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class MultiIdentityLineType {
    public static final int LINE_TYPE_PRIMARY = 1;
    public static final int LINE_TYPE_SECONDARY = 2;
    public static final int LINE_TYPE_UNKNOWN = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return "LINE_TYPE_UNKNOWN";
        }
        if (o == 1) {
            return "LINE_TYPE_PRIMARY";
        }
        if (o == 2) {
            return "LINE_TYPE_SECONDARY";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("LINE_TYPE_UNKNOWN");
        if ((o & 1) == 1) {
            list.add("LINE_TYPE_PRIMARY");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("LINE_TYPE_SECONDARY");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
