package vendor.qti.hardware.radio.ims.V1_6;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class GeoLocationDataStatus {
    public static final int ENGINE_LOCK = 2;
    public static final int NO_CIVIC_ADDRESS = 1;
    public static final int RESOLVED = 3;
    public static final int TIMEOUT = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return "TIMEOUT";
        }
        if (o == 1) {
            return "NO_CIVIC_ADDRESS";
        }
        if (o == 2) {
            return "ENGINE_LOCK";
        }
        if (o == 3) {
            return "RESOLVED";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("TIMEOUT");
        if ((o & 1) == 1) {
            list.add("NO_CIVIC_ADDRESS");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("ENGINE_LOCK");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("RESOLVED");
            flipped |= 3;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
