package vendor.qti.hardware.radio.ims.V1_0;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ExtraType {
    public static final int INVALID = 1;
    public static final int LTE_TO_IWLAN_HO_FAIL = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return "LTE_TO_IWLAN_HO_FAIL";
        }
        if (o == 1) {
            return "INVALID";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("LTE_TO_IWLAN_HO_FAIL");
        if ((o & 1) == 1) {
            list.add("INVALID");
            flipped = 0 | 1;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
