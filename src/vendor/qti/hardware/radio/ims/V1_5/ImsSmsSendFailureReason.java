package vendor.qti.hardware.radio.ims.V1_5;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ImsSmsSendFailureReason {
    public static final int RESULT_CANCELLED = 23;
    public static final int RESULT_ENCODING_ERROR = 18;
    public static final int RESULT_ERROR_FDN_CHECK_FAILURE = 8;
    public static final int RESULT_ERROR_GENERIC_FAILURE = 1;
    public static final int RESULT_ERROR_LIMIT_EXCEEDED = 5;
    public static final int RESULT_ERROR_NONE = 0;
    public static final int RESULT_ERROR_NO_SERVICE = 4;
    public static final int RESULT_ERROR_NULL_PDU = 3;
    public static final int RESULT_ERROR_RADIO_OFF = 2;
    public static final int RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED = 7;
    public static final int RESULT_ERROR_SHORT_CODE_NOT_ALLOWED = 6;
    public static final int RESULT_INTERNAL_ERROR = 21;
    public static final int RESULT_INVALID_ARGUMENTS = 11;
    public static final int RESULT_INVALID_SMSC_ADDRESS = 19;
    public static final int RESULT_INVALID_SMS_FORMAT = 14;
    public static final int RESULT_INVALID_STATE = 12;
    public static final int RESULT_MODEM_ERROR = 16;
    public static final int RESULT_NETWORK_ERROR = 17;
    public static final int RESULT_NETWORK_REJECT = 10;
    public static final int RESULT_NO_MEMORY = 13;
    public static final int RESULT_NO_RESOURCES = 22;
    public static final int RESULT_OPERATION_NOT_ALLOWED = 20;
    public static final int RESULT_RADIO_NOT_AVAILABLE = 9;
    public static final int RESULT_REQUEST_NOT_SUPPORTED = 24;
    public static final int RESULT_SYSTEM_ERROR = 15;

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
        if (o == 8) {
            return "RESULT_ERROR_FDN_CHECK_FAILURE";
        }
        if (o == 9) {
            return "RESULT_RADIO_NOT_AVAILABLE";
        }
        if (o == 10) {
            return "RESULT_NETWORK_REJECT";
        }
        if (o == 11) {
            return "RESULT_INVALID_ARGUMENTS";
        }
        if (o == 12) {
            return "RESULT_INVALID_STATE";
        }
        if (o == 13) {
            return "RESULT_NO_MEMORY";
        }
        if (o == 14) {
            return "RESULT_INVALID_SMS_FORMAT";
        }
        if (o == 15) {
            return "RESULT_SYSTEM_ERROR";
        }
        if (o == 16) {
            return "RESULT_MODEM_ERROR";
        }
        if (o == 17) {
            return "RESULT_NETWORK_ERROR";
        }
        if (o == 18) {
            return "RESULT_ENCODING_ERROR";
        }
        if (o == 19) {
            return "RESULT_INVALID_SMSC_ADDRESS";
        }
        if (o == 20) {
            return "RESULT_OPERATION_NOT_ALLOWED";
        }
        if (o == 21) {
            return "RESULT_INTERNAL_ERROR";
        }
        if (o == 22) {
            return "RESULT_NO_RESOURCES";
        }
        if (o == 23) {
            return "RESULT_CANCELLED";
        }
        if (o == 24) {
            return "RESULT_REQUEST_NOT_SUPPORTED";
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
        if ((o & 8) == 8) {
            list.add("RESULT_ERROR_FDN_CHECK_FAILURE");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("RESULT_RADIO_NOT_AVAILABLE");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("RESULT_NETWORK_REJECT");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("RESULT_INVALID_ARGUMENTS");
            flipped |= 11;
        }
        if ((o & 12) == 12) {
            list.add("RESULT_INVALID_STATE");
            flipped |= 12;
        }
        if ((o & 13) == 13) {
            list.add("RESULT_NO_MEMORY");
            flipped |= 13;
        }
        if ((o & 14) == 14) {
            list.add("RESULT_INVALID_SMS_FORMAT");
            flipped |= 14;
        }
        if ((o & 15) == 15) {
            list.add("RESULT_SYSTEM_ERROR");
            flipped |= 15;
        }
        if ((o & 16) == 16) {
            list.add("RESULT_MODEM_ERROR");
            flipped |= 16;
        }
        if ((o & 17) == 17) {
            list.add("RESULT_NETWORK_ERROR");
            flipped |= 17;
        }
        if ((o & 18) == 18) {
            list.add("RESULT_ENCODING_ERROR");
            flipped |= 18;
        }
        if ((o & 19) == 19) {
            list.add("RESULT_INVALID_SMSC_ADDRESS");
            flipped |= 19;
        }
        if ((o & 20) == 20) {
            list.add("RESULT_OPERATION_NOT_ALLOWED");
            flipped |= 20;
        }
        if ((o & 21) == 21) {
            list.add("RESULT_INTERNAL_ERROR");
            flipped |= 21;
        }
        if ((o & 22) == 22) {
            list.add("RESULT_NO_RESOURCES");
            flipped |= 22;
        }
        if ((o & 23) == 23) {
            list.add("RESULT_CANCELLED");
            flipped |= 23;
        }
        if ((o & 24) == 24) {
            list.add("RESULT_REQUEST_NOT_SUPPORTED");
            flipped |= 24;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
