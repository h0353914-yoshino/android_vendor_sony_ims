package org.codeaurora.ims;

import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import com.qualcomm.ims.utils.Log;

/* JADX INFO: loaded from: classes.dex */
public class ImsServiceStateReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "ImsServiceStateReceiver";
    // 狀態列槽位名稱後接 phoneId；框架 config_statusBarIcons（device overlay）已登記，會排在蜂窩 / Wi-Fi 圖示旁
    private static final String SLOT_VOLTE = "volte";
    private static final String SLOT_VOWIFI = "vowifi";
    private static final String SHOW_HD_ICON = "config_update_service_status";
    private static final String SHOW_VOLTE_ICON = "config_update_volte_icon";
    private static final String SHOW_VOWIFI_ICON = "config_update_vowifi_icon";
    private Context mContext;
    private int mPhoneId;
    private ImsServiceSub mServiceSub;
    // 與原版不同：預設 false。shallShowHDIcon 已改為預設 true，若此處也是 true，第一次顯示會被提前略過
    private boolean mShowHDIcon = false;
    private boolean mShowVOLTEIcon = false;
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

    private void showHDIcon(boolean showHDIcon) {
        if (this.mShowHDIcon == showHDIcon) {
            return;
        }
        this.mShowHDIcon = showHDIcon;
        updateStatusBarIcons();
    }

    // VoWiFi 與 VoLTE 只顯示一個：VoWiFi 啟用時只顯示 VoWiFi 圖示，否則才顯示 VoLTE（或 HD）圖示
    private void updateStatusBarIcons() {
        StatusBarManager statusBarManager = (StatusBarManager) this.mContext.getSystemService("statusbar");
        if (statusBarManager == null) {
            Log.e(LOG_TAG, "updateStatusBarIcons unable to update icons due to StatusBarManager is null");
            return;
        }
        String volteSlot = SLOT_VOLTE + this.mPhoneId;
        String vowifiSlot = SLOT_VOWIFI + this.mPhoneId;
        if (this.mShowVoWifiIcon) {
            statusBarManager.removeIcon(volteSlot);
            statusBarManager.setIcon(vowifiSlot, R.drawable.ic_qs_wifi_calling_on, 0, this.mContext.getResources().getString(R.string.wifi_calling_on));
            return;
        }
        statusBarManager.removeIcon(vowifiSlot);
        if (!this.mShowHDIcon) {
            statusBarManager.removeIcon(volteSlot);
            return;
        }
        if (this.mShowVOLTEIcon) {
            statusBarManager.setIcon(volteSlot, R.drawable.volte_icon, 0, this.mContext.getResources().getString(R.string.device_is_volte_capable, Integer.valueOf(this.mPhoneId + 1)));
        } else {
            statusBarManager.setIcon(volteSlot, R.drawable.ims_state, 0, this.mContext.getResources().getString(R.string.device_is_hd_capable, Integer.valueOf(this.mPhoneId + 1)));
        }
    }

    private boolean shallShowHDIcon() {
        // 與原版不同：電信商未設定時預設顯示（原版預設 false），任何電信開啟 VoLTE 都會有圖示
        boolean showHDIcon = true;
        this.mShowVOLTEIcon = true;
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
            showHDIcon = b.getBoolean(SHOW_HD_ICON, true);
            this.mShowVOLTEIcon = b.getBoolean(SHOW_VOLTE_ICON, true);
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
        // 與原版不同：原版只送 arima 廣播，AOSP/Lineage 沒有接收端，所以這裡改用狀態列圖示顯示
        updateStatusBarIcons();
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
