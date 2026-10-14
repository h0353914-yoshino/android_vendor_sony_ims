package vendor.qti.hardware.radio.ims.V1_3;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class VoiceInfo {
    public static final int VOICE_INFO_SILENT = 0;
    public static final int VOICE_INFO_SPEECH = 1;
    public static final int VOICE_INFO_UNKNOWN = 2;

    public static final String toString(int o) {
        if (o == 0) {
            return "VOICE_INFO_SILENT";
        }
        if (o == 1) {
            return "VOICE_INFO_SPEECH";
        }
        if (o == 2) {
            return "VOICE_INFO_UNKNOWN";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("VOICE_INFO_SILENT");
        if ((o & 1) == 1) {
            list.add("VOICE_INFO_SPEECH");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("VOICE_INFO_UNKNOWN");
            flipped |= 2;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
