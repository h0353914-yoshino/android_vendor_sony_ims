package com.qualcomm.ims.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes.dex */
public class Log {
    public static final boolean FORCE_DEBUG = false;
    public static final String TAG_DELIMETER = " : ";
    public static final String TAG = "QImsService";
    public static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    public static final boolean VERBOSE = android.util.Log.isLoggable(TAG, 2);

    public static void d(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.d(TAG, delimit(tag) + msg);
        }
    }

    public static void d(Object obj, String msg) {
        if (DEBUG) {
            android.util.Log.d(TAG, getPrefix(obj) + msg);
        }
    }

    public static void d(Object obj, String str1, Object str2) {
        if (DEBUG) {
            android.util.Log.d(TAG, getPrefix(obj) + str1 + str2);
        }
    }

    public static void v(Object obj, String msg) {
        if (VERBOSE) {
            android.util.Log.v(TAG, getPrefix(obj) + msg);
        }
    }

    public static void v(Object obj, String str1, Object str2) {
        if (VERBOSE) {
            android.util.Log.d(TAG, getPrefix(obj) + str1 + str2);
        }
    }

    public static void e(String tag, String msg, Exception e) {
        android.util.Log.e(TAG, delimit(tag) + msg, e);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(TAG, delimit(tag) + msg);
    }

    public static void e(Object obj, String msg, Exception e) {
        android.util.Log.e(TAG, getPrefix(obj) + msg, e);
    }

    public static void e(Object obj, String msg) {
        android.util.Log.e(TAG, getPrefix(obj) + msg);
    }

    public static void i(String tag, String msg) {
        android.util.Log.i(TAG, delimit(tag) + msg);
    }

    public static void i(Object obj, String msg) {
        android.util.Log.i(TAG, getPrefix(obj) + msg);
    }

    public static void w(Object obj, String msg) {
        android.util.Log.w(TAG, getPrefix(obj) + msg);
    }

    public static void wtf(Object obj, String msg) {
        android.util.Log.wtf(TAG, getPrefix(obj) + msg);
    }

    private static String getPrefix(Object obj) {
        return obj == null ? "" : obj.getClass().getSimpleName() + TAG_DELIMETER;
    }

    private static String delimit(String tag) {
        return tag + TAG_DELIMETER;
    }

    public static String pii(Object pii) {
        if (pii == null || VERBOSE) {
            return String.valueOf(pii);
        }
        return "[" + secureHash(String.valueOf(pii).getBytes()) + "]";
    }

    private static String secureHash(byte[] input) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        messageDigest.update(input);
        byte[] result = messageDigest.digest();
        return encodeHex(result);
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer hex = new StringBuffer(bytes.length * 2);
        for (byte b : bytes) {
            int byteIntValue = b & 255;
            if (byteIntValue < 16) {
                hex.append("0");
            }
            hex.append(Integer.toString(byteIntValue, 16));
        }
        return hex.toString();
    }
}
