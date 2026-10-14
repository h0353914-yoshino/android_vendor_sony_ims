package org.codeaurora.ims;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import com.qualcomm.ims.utils.Log;

/* JADX INFO: loaded from: classes.dex */
public class ImsServiceStateReceiver extends BroadcastReceiver {
    private static String CHANNEL_ID = "ims_services_channel_";
    private static String CHANNEL_ID_VOWIFI = "ims_services_channel_vowifi_";
    private static final int IMS_HD_ICON = 1000;
    private static final int IMS_VOWIFI_ICON = 1001;
    private static final String LOG_TAG = "ImsServiceStateReceiver";
    private static final String SHOW_HD_ICON = "config_update_service_status";
    private static final String SHOW_VOLTE_ICON = "config_update_volte_icon";
    private static final String SHOW_VOWIFI_ICON = "config_update_vowifi_icon";
    private Context mContext;
    private int mPhoneId;
    private ImsServiceSub mServiceSub;
    private boolean mShowHDIcon = true;
    private boolean mShowVOLTEIcon = false;
    private NotificationManager mNotificationMgr = null;
    private boolean mShowVoWifiIcon = false;

    public ImsServiceStateReceiver(ImsServiceSub serviceSub, Context context, int phoneId) {
        this.mPhoneId = -1;
        this.mServiceSub = serviceSub;
        this.mContext = context;
        this.mPhoneId = phoneId;
    }

    public void updateHDIcon(boolean isVideoCapable, boolean isVoiceCapable) {
        Log.i(LOG_TAG, "updateHDIcon isVideo : " + isVideoCapable + " isVoice : " + isVoiceCapable + " phoneId: " + this.mPhoneId + " show HD Icon: " + this.mShowHDIcon);
        if (ImsCallUtils.isCarrierOneSupported()) {
            return;
        }
        if (this.mServiceSub.getFeatureState() == 2) {
            if (shallShowHDIcon()) {
                showHDIcon(isVideoCapable || isVoiceCapable);
                return;
            } else {
                showHDIcon(false);
                return;
            }
        }
        showHDIcon(false);
    }

    public static void overrideNotificationAppName(Context context, Notification.Builder n) {
        Bundle extras = new Bundle();
        extras.putString("android.substName", context.getResources().getString(R.string.hd_icon_header_app_name));
        n.addExtras(extras);
    }

    private void showHDIcon(boolean showHDIcon) {
        if (this.mShowHDIcon == showHDIcon) {
            return;
        }
        if (this.mNotificationMgr == null) {
            try {
                Context context = this.mContext;
                this.mNotificationMgr = (NotificationManager) context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(ActivityManager.getCurrentUser())).getSystemService("notification");
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(LOG_TAG, "showHDIcon Package name not found: " + e.getMessage());
            }
        }
        NotificationManager notificationManager = this.mNotificationMgr;
        if (notificationManager == null) {
            Log.e(LOG_TAG, "showHDIcon unable to show HD icon due to NotificationManager is null");
            return;
        }
        this.mShowHDIcon = showHDIcon;
        if (showHDIcon) {
            NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID + this.mPhoneId);
            if (channel == null) {
                CharSequence name = this.mContext.getResources().getString(R.string.ims_channel_name);
                channel = new NotificationChannel(CHANNEL_ID + this.mPhoneId, name, 2);
                this.mNotificationMgr.createNotificationChannel(channel);
            }
            Notification.Builder builder = new Notification.Builder(this.mContext);
            builder.setChannelId(channel.getId());
            if (this.mShowVOLTEIcon) {
                builder.setContentTitle(this.mContext.getResources().getString(R.string.device_is_volte_capable, Integer.valueOf(this.mPhoneId + 1)));
                builder.setSmallIcon(R.drawable.volte_icon);
            } else {
                builder.setContentTitle(this.mContext.getResources().getString(R.string.device_is_hd_capable, Integer.valueOf(this.mPhoneId + 1)));
                builder.setSmallIcon(R.drawable.ims_state);
            }
            builder.setShowWhen(false);
            overrideNotificationAppName(this.mContext, builder);
            Notification notification = builder.build();
            notification.flags |= 32;
            this.mNotificationMgr.notify(this.mPhoneId + IMS_HD_ICON, notification);
            return;
        }
        notificationManager.cancel(this.mPhoneId + IMS_HD_ICON);
    }

    private boolean shallShowHDIcon() {
        boolean showHDIcon = false;
        SubscriptionManager subManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        if (subManager == null) {
            Log.i(LOG_TAG, "shallShowHDIcon SubscriptionManager is null");
            return false;
        }
        SubscriptionInfo subInfo = subManager.getActiveSubscriptionInfoForSimSlotIndex(this.mPhoneId);
        if (subInfo == null) {
            Log.i(LOG_TAG, "shallShowHDIcon SubscriptionInfo is null");
            return false;
        }
        int mcc = 0;
        int mnc = 0;
        if (subInfo != null) {
            mcc = subInfo.getMcc();
            mnc = subInfo.getMnc();
        }
        Log.d(LOG_TAG, "mcc =" + mcc + " mnc=" + mnc);
        int subId = subInfo.getSubscriptionId();
        if (!subManager.isActiveSubscriptionId(subId)) {
            Log.i(LOG_TAG, "shallShowHDIcon subId is not active");
            return false;
        }
        CarrierConfigManager mgr = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        PersistableBundle b = null;
        if (mgr != null) {
            b = mgr.getConfigForSubId(subId);
        }
        if (b != null) {
            showHDIcon = b.getBoolean(SHOW_HD_ICON, false);
            this.mShowVOLTEIcon = b.getBoolean(SHOW_VOLTE_ICON, false);
        }
        Log.i(LOG_TAG, "shallShowHDIcon config showHDIcon : " + showHDIcon + " phoneId : " + this.mPhoneId);
        return showHDIcon;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        SubscriptionManager subManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        SubscriptionInfo subInfo = subManager.getActiveSubscriptionInfoForSimSlotIndex(this.mPhoneId);
        if (subInfo == null) {
            Log.w(LOG_TAG, "SimStateReceiver onReceive subId is not yet active");
            return;
        }
        int subId = subInfo.getSubscriptionId();
        if ("android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(intent.getAction())) {
            int simStatus = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
            int intentSubId = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            Log.d(LOG_TAG, "SimStateReceiver sub id from intent : " + intentSubId + " simStatus : " + simStatus);
            if (intentSubId == subId && 10 == simStatus) {
                updateHDIcon(this.mServiceSub.isVideoSupported(), this.mServiceSub.isVoiceSupported());
                updateVoWifiIcon(this.mServiceSub.isVideoSupportedOverWifi(), this.mServiceSub.isVoiceSupportedOverWifi());
                this.mServiceSub.onIccLoaded();
                return;
            }
            return;
        }
        if (intent != null && intent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
            int intentSubId2 = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            Log.d(LOG_TAG, "received carrier config change, sub id from intent : " + intentSubId2);
            if (subId == intentSubId2) {
                updateHDIcon(this.mServiceSub.isVideoSupported(), this.mServiceSub.isVoiceSupported());
            }
        }
    }

    public void updateVoWifiIcon(boolean isVideoCapable, boolean isVoiceCapable) {
        Log.i(LOG_TAG, "updateVoWifiIcon isVideo : " + isVideoCapable + " isVoice : " + isVoiceCapable + " phoneId: " + this.mPhoneId + " show VoWifi Icon: " + this.mShowVoWifiIcon);
        if (this.mServiceSub.getFeatureState() == 2) {
            if (shallShowVoWifiIcon()) {
                Log.i(LOG_TAG, "updateVoWifiIcon shallShowVoWifiIcon = true");
                showVoWifiIcon(isVideoCapable || isVoiceCapable);
                return;
            } else {
                showVoWifiIcon(false);
                return;
            }
        }
        showVoWifiIcon(false);
    }

    private void showVoWifiIcon(boolean showVoWifiIcon) {
        if (this.mShowVoWifiIcon == showVoWifiIcon) {
            return;
        }
        SubscriptionManager subManager = SubscriptionManager.from(this.mContext);
        if (subManager == null) {
            Log.i(LOG_TAG, "shallShowVoWifiIcon SubscriptionManager is null");
            return;
        }
        this.mShowVoWifiIcon = showVoWifiIcon;
        Intent voWifiIntent = new Intent("arima.intent.action.VOWIFI_STATE_CHANGED");
        voWifiIntent.putExtra("phoneId", this.mPhoneId);
        if (this.mShowVoWifiIcon) {
            voWifiIntent.putExtra("showVOWIFIIcon", true);
        } else {
            voWifiIntent.putExtra("showVOWIFIIcon", false);
        }
        this.mContext.sendBroadcastAsUser(voWifiIntent, UserHandle.ALL);
        if (this.mNotificationMgr == null) {
            this.mNotificationMgr = (NotificationManager) this.mContext.getSystemService("notification");
        }
        if (!this.mShowVoWifiIcon) {
            this.mNotificationMgr.cancelAsUser(null, this.mPhoneId + IMS_VOWIFI_ICON, UserHandle.ALL);
        }
    }

    private boolean shallShowVoWifiIcon() {
        boolean showVoWifiIcon = false;
        SubscriptionManager subManager = SubscriptionManager.from(this.mContext);
        if (subManager == null) {
            Log.i(LOG_TAG, "shallShowVoWifiIcon SubscriptionManager is null");
            return false;
        }
        SubscriptionInfo subInfo = subManager.getActiveSubscriptionInfoForSimSlotIndex(this.mPhoneId);
        if (subInfo == null) {
            Log.i(LOG_TAG, "shallShowVoWifiIcon SubscriptionInfo is null");
            return false;
        }
        int subId = subInfo.getSubscriptionId();
        if (!subManager.isActiveSubId(subId)) {
            Log.i(LOG_TAG, "shallShowVoWifiIcon subId is not active");
            return false;
        }
        CarrierConfigManager mgr = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        PersistableBundle b = null;
        if (mgr != null) {
            b = mgr.getConfigForSubId(subId);
        }
        if (b != null) {
            showVoWifiIcon = b.getBoolean(SHOW_VOWIFI_ICON, true);
        }
        Log.i(LOG_TAG, "shallShowVoWifiIcon config showVoWifiIcon : " + showVoWifiIcon + " phoneId : " + this.mPhoneId);
        return showVoWifiIcon;
    }
}
