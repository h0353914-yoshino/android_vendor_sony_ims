package vendor.qti.hardware.radio.ims.V1_2;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ImsSmsSendFailureReason {
    public static final int RESULT_ERROR_GENERIC_FAILURE = 1;
    public static final int RESULT_ERROR_LIMIT_EXCEEDED = 5;
    public static final int RESULT_ERROR_NONE = 0;
    public static final int RESULT_ERROR_NO_SERVICE = 4;
    public static final int RESULT_ERROR_NULL_PDU = 3;
    public static final int RESULT_ERROR_RADIO_OFF = 2;
    public static final int RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED = 7;
    public static final int RESULT_ERROR_SHORT_CODE_NOT_ALLOWED = 6;

    public static final String toString(int o) {
        if (o == 0) {
            return "RESULT_ERROR_NONE";
        }
        if (o == 1) {
            return "RESULT_ERROR_GENERIC_FAILURE";
        }
        if (o == 2) {
            return "RESULT_ERROR_RADIO_OFF";
        }
        if (o == 3) {
            return "RESULT_ERROR_NULL_PDU";
        }
        if (o == 4) {
            return "RESULT_ERROR_NO_SERVICE";
        }
        if (o == 5) {
            return "RESULT_ERROR_LIMIT_EXCEEDED";
        }
        if (o == 6) {
            return "RESULT_ERROR_SHORT_CODE_NOT_ALLOWED";
        }
        if (o == 7) {
            return "RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("RESULT_ERROR_NONE");
        if ((o & 1) == 1) {
            list.add("RESULT_ERROR_GENERIC_FAILURE");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("RESULT_ERROR_RADIO_OFF");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("RESULT_ERROR_NULL_PDU");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("RESULT_ERROR_NO_SERVICE");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("RESULT_ERROR_LIMIT_EXCEEDED");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("RESULT_ERROR_SHORT_CODE_NOT_ALLOWED");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED");
            flipped |= 7;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
