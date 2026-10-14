package com.qualcomm.ims.utils;

import android.content.Context;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class Config {
    private static final String LOG_TAG = "Config";

    private Config() {
    }

    public static boolean isConfigEnabled(Context context, int config) {
        try {
            return context.getResources().getBoolean(config);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "isConfigEnabled :: Null context! config=" + config);
            return false;
        } catch (Exception exc) {
            Log.e(LOG_TAG, "isConfigEnabled :: Error getting boolean value for config=" + config + "\n" + exc);
            return false;
        }
    }

    public static boolean isCarrierConfigEnabled(Context context, int subId, String config) {
        CarrierConfigManager configManager;
        PersistableBundle b;
        if (TextUtils.isEmpty(config) || subId == -1 || (configManager = (CarrierConfigManager) context.getSystemService("carrier_config")) == null || (b = configManager.getConfigForSubId(subId)) == null) {
            return false;
        }
        return b.getBoolean(config, false);
    }
}
