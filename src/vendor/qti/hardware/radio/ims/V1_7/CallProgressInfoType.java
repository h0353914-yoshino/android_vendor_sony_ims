package vendor.qti.hardware.radio.ims.V1_7;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class CallProgressInfoType {
    public static final int CALL_FORWARDING = 3;
    public static final int CALL_REJ_Q850 = 1;
    public static final int CALL_WAITING = 2;
    public static final int INVALID = 0;
    public static final int REMOTE_AVAILABLE = 4;

    public static final String toString(int o) {
        if (o == 0) {
            return "INVALID";
        }
        if (o == 1) {
            return "CALL_REJ_Q850";
        }
        if (o == 2) {
            return "CALL_WAITING";
        }
        if (o == 3) {
            return "CALL_FORWARDING";
        }
        if (o == 4) {
            return "REMOTE_AVAILABLE";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("INVALID");
        if ((o & 1) == 1) {
            list.add("CALL_REJ_Q850");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("CALL_WAITING");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("CALL_FORWARDING");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("REMOTE_AVAILABLE");
            flipped |= 4;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
