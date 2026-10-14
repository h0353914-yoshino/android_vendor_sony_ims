package vendor.qti.hardware.radio.ims.V1_2;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ImsSmsStatusReportResult {
    public static final int STATUS_REPORT_STATUS_ERROR = 1;
    public static final int STATUS_REPORT_STATUS_OK = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return "STATUS_REPORT_STATUS_OK";
        }
        if (o == 1) {
            return "STATUS_REPORT_STATUS_ERROR";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("STATUS_REPORT_STATUS_OK");
        if ((o & 1) == 1) {
            list.add("STATUS_REPORT_STATUS_ERROR");
            flipped = 0 | 1;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
