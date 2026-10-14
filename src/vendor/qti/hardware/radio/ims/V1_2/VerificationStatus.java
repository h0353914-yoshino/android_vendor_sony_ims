package vendor.qti.hardware.radio.ims.V1_2;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class VerificationStatus {
    public static final int STATUS_VALIDATION_FAIL = 2;
    public static final int STATUS_VALIDATION_NONE = 0;
    public static final int STATUS_VALIDATION_PASS = 1;

    public static final String toString(int o) {
        if (o == 0) {
            return "STATUS_VALIDATION_NONE";
        }
        if (o == 1) {
            return "STATUS_VALIDATION_PASS";
        }
        if (o == 2) {
            return "STATUS_VALIDATION_FAIL";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("STATUS_VALIDATION_NONE");
        if ((o & 1) == 1) {
            list.add("STATUS_VALIDATION_PASS");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("STATUS_VALIDATION_FAIL");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
