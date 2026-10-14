package vendor.qti.hardware.radio.ims.V1_2;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ImsSmsSendStatusResult {
    public static final int SEND_STATUS_ERROR = 1;
    public static final int SEND_STATUS_ERROR_FALLBACK = 3;
    public static final int SEND_STATUS_ERROR_RETRY = 2;
    public static final int SEND_STATUS_OK = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return "SEND_STATUS_OK";
        }
        if (o == 1) {
            return "SEND_STATUS_ERROR";
        }
        if (o == 2) {
            return "SEND_STATUS_ERROR_RETRY";
        }
        if (o == 3) {
            return "SEND_STATUS_ERROR_FALLBACK";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("SEND_STATUS_OK");
        if ((o & 1) == 1) {
            list.add("SEND_STATUS_ERROR");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("SEND_STATUS_ERROR_RETRY");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("SEND_STATUS_ERROR_FALLBACK");
            flipped |= 3;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
