package vendor.qti.hardware.radio.ims.V1_0;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class SsServiceType {
    public static final int SS_ALL_BARRING = 18;
    public static final int SS_BAIC = 16;
    public static final int SS_BAIC_ROAMING = 17;
    public static final int SS_BAOC = 13;
    public static final int SS_BAOIC = 14;
    public static final int SS_BAOIC_EXC_HOME = 15;
    public static final int SS_CFU = 0;
    public static final int SS_CFUT = 6;
    public static final int SS_CF_ALL = 4;
    public static final int SS_CF_ALL_CONDITIONAL = 5;
    public static final int SS_CF_BUSY = 1;
    public static final int SS_CF_NOT_REACHABLE = 3;
    public static final int SS_CF_NO_REPLY = 2;
    public static final int SS_CLIP = 7;
    public static final int SS_CLIR = 8;
    public static final int SS_CNAP = 11;
    public static final int SS_COLP = 9;
    public static final int SS_COLR = 10;
    public static final int SS_INCOMING_BARRING = 20;
    public static final int SS_INCOMING_BARRING_ANONYMOUS = 22;
    public static final int SS_INCOMING_BARRING_DN = 21;
    public static final int SS_INVALID = 23;
    public static final int SS_OUTGOING_BARRING = 19;
    public static final int SS_WAIT = 12;

    public static final String toString(int o) {
        if (o == 0) {
            return "SS_CFU";
        }
        if (o == 1) {
            return "SS_CF_BUSY";
        }
        if (o == 2) {
            return "SS_CF_NO_REPLY";
        }
        if (o == 3) {
            return "SS_CF_NOT_REACHABLE";
        }
        if (o == 4) {
            return "SS_CF_ALL";
        }
        if (o == 5) {
            return "SS_CF_ALL_CONDITIONAL";
        }
        if (o == 6) {
            return "SS_CFUT";
        }
        if (o == 7) {
            return "SS_CLIP";
        }
        if (o == 8) {
            return "SS_CLIR";
        }
        if (o == 9) {
            return "SS_COLP";
        }
        if (o == 10) {
            return "SS_COLR";
        }
        if (o == 11) {
            return "SS_CNAP";
        }
        if (o == 12) {
            return "SS_WAIT";
        }
        if (o == 13) {
            return "SS_BAOC";
        }
        if (o == 14) {
            return "SS_BAOIC";
        }
        if (o == 15) {
            return "SS_BAOIC_EXC_HOME";
        }
        if (o == 16) {
            return "SS_BAIC";
        }
        if (o == 17) {
            return "SS_BAIC_ROAMING";
        }
        if (o == 18) {
            return "SS_ALL_BARRING";
        }
        if (o == 19) {
            return "SS_OUTGOING_BARRING";
        }
        if (o == 20) {
            return "SS_INCOMING_BARRING";
        }
        if (o == 21) {
            return "SS_INCOMING_BARRING_DN";
        }
        if (o == 22) {
            return "SS_INCOMING_BARRING_ANONYMOUS";
        }
        if (o == 23) {
            return "SS_INVALID";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("SS_CFU");
        if ((o & 1) == 1) {
            list.add("SS_CF_BUSY");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("SS_CF_NO_REPLY");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("SS_CF_NOT_REACHABLE");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("SS_CF_ALL");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("SS_CF_ALL_CONDITIONAL");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("SS_CFUT");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("SS_CLIP");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("SS_CLIR");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("SS_COLP");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("SS_COLR");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("SS_CNAP");
            flipped |= 11;
        }
        if ((o & 12) == 12) {
            list.add("SS_WAIT");
            flipped |= 12;
        }
        if ((o & 13) == 13) {
            list.add("SS_BAOC");
            flipped |= 13;
        }
        if ((o & 14) == 14) {
            list.add("SS_BAOIC");
            flipped |= 14;
        }
        if ((o & 15) == 15) {
            list.add("SS_BAOIC_EXC_HOME");
            flipped |= 15;
        }
        if ((o & 16) == 16) {
            list.add("SS_BAIC");
            flipped |= 16;
        }
        if ((o & 17) == 17) {
            list.add("SS_BAIC_ROAMING");
            flipped |= 17;
        }
        if ((o & 18) == 18) {
            list.add("SS_ALL_BARRING");
            flipped |= 18;
        }
        if ((o & 19) == 19) {
            list.add("SS_OUTGOING_BARRING");
            flipped |= 19;
        }
        if ((o & 20) == 20) {
            list.add("SS_INCOMING_BARRING");
            flipped |= 20;
        }
        if ((o & 21) == 21) {
            list.add("SS_INCOMING_BARRING_DN");
            flipped |= 21;
        }
        if ((o & 22) == 22) {
            list.add("SS_INCOMING_BARRING_ANONYMOUS");
            flipped |= 22;
        }
        if ((o & 23) == 23) {
            list.add("SS_INVALID");
            flipped |= 23;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
