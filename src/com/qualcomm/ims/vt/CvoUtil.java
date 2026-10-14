package com.qualcomm.ims.vt;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class CvoUtil {
    private static final boolean DBG = true;
    public static final int ORIENTATION_ANGLE_0 = 0;
    public static final int ORIENTATION_ANGLE_180 = 2;
    public static final int ORIENTATION_ANGLE_270 = 1;
    public static final int ORIENTATION_ANGLE_90 = 3;
    public static final int ORIENTATION_THRESHOLD = 45;
    public static final int ORIENTATION_UNKNOWN = -1;
    private static final String TAG = "VideoCall_CvoUtil";

    private CvoUtil() {
    }

    public static int toOrientation(int angle) {
        if (isInRange2(angle, 90, 45)) {
            return 3;
        }
        if (isInRange2(angle, 180, 45)) {
            return 2;
        }
        if (isInRange2(angle, 270, 45)) {
            return 1;
        }
        return 0;
    }

    static boolean isInRange(int v, int left, int right) {
        if (v < left || v >= right) {
            return false;
        }
        return DBG;
    }

    static boolean isInRange2(int v, int p, int radius) {
        return isInRange(v, p - radius, p + radius);
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }
}
