package vendor.qti.hardware.radio.ims.V1_4;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class MultiIdentityRegistrationStatus {
    public static final int STATUS_DISABLE = 1;
    public static final int STATUS_ENABLE = 2;
    public static final int STATUS_UNKNOWN = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return "STATUS_UNKNOWN";
        }
        if (o == 1) {
            return "STATUS_DISABLE";
        }
        if (o == 2) {
            return "STATUS_ENABLE";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("STATUS_UNKNOWN");
        if ((o & 1) == 1) {
            list.add("STATUS_DISABLE");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("STATUS_ENABLE");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
