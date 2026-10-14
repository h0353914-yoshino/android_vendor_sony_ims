package org.codeaurora.ims;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.stub.ImsConfigImplBase;
import com.qualcomm.ims.utils.Log;
import java.util.ArrayList;
import org.codeaurora.telephony.utils.AsyncResult;
import org.codeaurora.telephony.utils.SomeArgs;

/* JADX INFO: loaded from: classes.dex */
public class ImsConfigImpl extends ImsConfigImplBase {
    private static final int EVENT_ACCESS_PROVISIONED_VAL = 1;
    private static final int EVENT_SET_FEATURE_VALUE = 2;
    private ImsSenderRxr mCi;
    private Context mContext;
    private Handler mHandler;
    private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangeListener;
    private ImsServiceSub mServiceSub;
    private final SubscriptionManager mSubscriptionManager;
    private Uri mWfcModeUri;
    private Uri mWfcRoamingModeUri;
    private int mSubId = -1;
    private ImsMmTelManager mImsMmTelManager = null;
    private boolean mIsContentObserversRegistered = false;
    private boolean mIsCarrierConfigLoaded = false;
    private final String MODIFY_PHONE_STATE = "android.permission.MODIFY_PHONE_STATE";
    private final String READ_PRIVILEGED_PHONE_STATE = "android.permission.READ_PRIVILEGED_PHONE_STATE";
    private final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    private ContentObserver mVoWiFiModeObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: org.codeaurora.ims.ImsConfigImpl.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            ImsConfigImpl.this.updateWFCMode(false);
        }
    };
    private ContentObserver mVoWiFiRoamingModeObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: org.codeaurora.ims.ImsConfigImpl.2
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            ImsConfigImpl.this.updateWFCMode(true);
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: org.codeaurora.ims.ImsConfigImpl.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() == null) {
                Log.e(ImsConfigImpl.this, "onReceive: null extras");
                return;
            }
            int subId = intent.getExtras().getInt("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            String action = intent.getAction();
            Log.i(ImsConfigImpl.this, "mBroadcastReceiver: action " + action + " sub = " + subId + " mSubId = " + ImsConfigImpl.this.mSubId);
            if (SubscriptionManager.isValidSubscriptionId(subId) && "android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action) && subId == ImsConfigImpl.this.mSubId) {
                ImsConfigImpl.this.mIsCarrierConfigLoaded = true;
                ImsConfigImpl.this.updateWfcModeConfigurationsToModem();
            }
        }
    };

    public interface SetCapabilityValueListener {
        void onSetCapabilityValueFailure(int i, int i2, SetCapabilityFailCause setCapabilityFailCause);

        void onSetCapabilityValueSuccess(int i, int i2, int i3);
    }

    public ImsConfigImpl(ImsServiceSub serviceSub, ImsSenderRxr senderRxr, Context context) {
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: org.codeaurora.ims.ImsConfigImpl.4
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                SubscriptionInfo subInfo = ImsConfigImpl.this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(ImsConfigImpl.this.mServiceSub.getPhoneId());
                if (subInfo == null) {
                    Log.e(ImsConfigImpl.this, "onSubscriptionsChanged unable to process due to SubscriptionInfo is null");
                    ImsConfigImpl.this.clearSubscriptionCache();
                    return;
                }
                int subId = subInfo.getSubscriptionId();
                Log.d(ImsConfigImpl.this, "onSubscriptionsChanged oldSubId = " + ImsConfigImpl.this.mSubId + " new SubId " + subId);
                if (ImsConfigImpl.this.mSubId != subId) {
                    ImsConfigImpl.this.clearSubscriptionCache();
                    ImsConfigImpl.this.mSubId = subId;
                    if (!SubscriptionManager.isValidSubscriptionId(subId) || !ImsConfigImpl.this.mSubscriptionManager.isActiveSubscriptionId(subId)) {
                        Log.e(ImsConfigImpl.this, "Invalid subscription id = " + subId);
                        return;
                    }
                    ImsConfigImpl imsConfigImpl = ImsConfigImpl.this;
                    imsConfigImpl.mImsMmTelManager = ImsMmTelManager.createForSubscriptionId(imsConfigImpl.mSubId);
                    ImsConfigImpl.this.mWfcModeUri = Uri.withAppendedPath(SubscriptionManager.WFC_MODE_CONTENT_URI, "" + ImsConfigImpl.this.mSubId);
                    ImsConfigImpl.this.mWfcRoamingModeUri = Uri.withAppendedPath(SubscriptionManager.WFC_ROAMING_MODE_CONTENT_URI, "" + ImsConfigImpl.this.mSubId);
                    ImsConfigImpl.this.mContext.getContentResolver().registerContentObserver(ImsConfigImpl.this.mWfcModeUri, true, ImsConfigImpl.this.mVoWiFiModeObserver);
                    ImsConfigImpl.this.mContext.getContentResolver().registerContentObserver(ImsConfigImpl.this.mWfcRoamingModeUri, true, ImsConfigImpl.this.mVoWiFiRoamingModeObserver);
                    ImsConfigImpl.this.mIsContentObserversRegistered = true;
                }
            }
        };
        this.mOnSubscriptionsChangeListener = onSubscriptionsChangedListener;
        this.mServiceSub = serviceSub;
        this.mCi = senderRxr;
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread("ImsConfigImplHandler");
        handlerThread.start();
        this.mHandler = new ImsConfigImplHandler(handlerThread.getLooper());
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        this.mSubscriptionManager = subscriptionManager;
        if (subscriptionManager != null) {
            subscriptionManager.addOnSubscriptionsChangedListener(onSubscriptionsChangedListener);
        } else {
            Log.e(this, "unable to listen for subscription changed due to subscriptionManager is null");
        }
        IntentFilter filter = new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void dispose() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearSubscriptionCache() {
        this.mIsCarrierConfigLoaded = false;
        this.mSubId = -1;
        if (this.mIsContentObserversRegistered) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mVoWiFiModeObserver);
            this.mContext.getContentResolver().unregisterContentObserver(this.mVoWiFiRoamingModeObserver);
            this.mIsContentObserversRegistered = false;
        }
        this.mImsMmTelManager = null;
        this.mWfcModeUri = null;
        this.mWfcRoamingModeUri = null;
    }

    public void updateWfcModeConfigurationsToModem() {
        Log.d(this, "updateWfcModeConfigurationsToModem: mIsCarrierConfigLoaded = " + this.mIsCarrierConfigLoaded);
        if (this.mServiceSub.IsWfcRoamingConfigurationSupportedByModem() && this.mImsMmTelManager != null && this.mIsCarrierConfigLoaded) {
            updateWFCMode(false);
            updateWFCMode(true);
        }
    }

    public int updateWFCMode(boolean isRoamingMode) {
        int item;
        int value;
        Log.d(this, "updateWFCMode: sub = " + this.mSubId + " isRoamingMode = " + isRoamingMode);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateWFCMode");
        enforceReadPhoneState("updateWFCMode");
        try {
            SubscriptionManager subscriptionManager = this.mSubscriptionManager;
            if (subscriptionManager != null && subscriptionManager.isActiveSubscriptionId(this.mSubId)) {
                if (this.mImsMmTelManager == null) {
                    Log.w(this, "updateWFCMode: mImsMmTelManager null");
                    return 1;
                }
                if (!this.mServiceSub.IsWfcRoamingConfigurationSupportedByModem()) {
                    Log.i(this, "Modem do not support WFC roaming config");
                    return 1;
                }
                if (isRoamingMode) {
                    item = 1003;
                    value = this.mImsMmTelManager.getVoWiFiRoamingModeSetting();
                } else {
                    item = 27;
                    value = this.mImsMmTelManager.getVoWiFiModeSetting();
                }
                sendSetConfigRequestAsync(item, value);
                return -1;
            }
            Log.w(this, "updateWFCMode: invalid sub = " + this.mSubId);
            return 1;
        } catch (Exception e) {
            Log.w(this, "Could not update WFCMode: invalid sub = " + this.mSubId);
            return 1;
        }
    }

    /* JADX INFO: renamed from: org.codeaurora.ims.ImsConfigImpl$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$org$codeaurora$ims$ImsConfigImpl$SetCapabilityFailCause;

        static {
            int[] iArr = new int[SetCapabilityFailCause.values().length];
            $SwitchMap$org$codeaurora$ims$ImsConfigImpl$SetCapabilityFailCause = iArr;
            try {
                iArr[SetCapabilityFailCause.ERROR_GENERIC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codeaurora$ims$ImsConfigImpl$SetCapabilityFailCause[SetCapabilityFailCause.ERROR_SUCCESS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public enum SetCapabilityFailCause {
        ERROR_GENERIC,
        ERROR_SUCCESS;

        @Override // java.lang.Enum
        public String toString() {
            switch (AnonymousClass5.$SwitchMap$org$codeaurora$ims$ImsConfigImpl$SetCapabilityFailCause[ordinal()]) {
                case 1:
                    return "ERROR_GENERIC";
                case 2:
                    return "ERROR_SUCCESS";
                default:
                    return "";
            }
        }
    }

    private class ImsConfigImplHandler extends Handler {
        public ImsConfigImplHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(this, "Message received: what = " + msg.what);
            AsyncResult ar = (AsyncResult) msg.obj;
            switch (msg.what) {
                case 1:
                    ImsConfigImpl.this.onAccessProvisionedValDone(ar, msg.what);
                    break;
                case 2:
                    ImsConfigImpl.this.onSetFeatureResponseDone(ar);
                    break;
                default:
                    Log.e(this, "handleMessage: unhandled message");
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAccessProvisionedValDone(AsyncResult ar, int accessType) {
        SomeArgs response = (SomeArgs) ar.userObj;
        response.arg2 = Integer.valueOf(getOperationStatus(response.arg1, ar.exception));
        response.arg3 = ar.result;
        synchronized (response) {
            Log.i(this, "Notifyall");
            response.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetFeatureResponseDone(AsyncResult ar) {
        if (ar == null) {
            Log.e(this, "onSetFeatureResponseDone :: AsyncResult is null.");
            return;
        }
        SetCapabilityValueListener listener = null;
        ArrayList<CapabilityStatus> capabilityStatusList = null;
        try {
            capabilityStatusList = (ArrayList) ar.result;
            listener = (SetCapabilityValueListener) ar.userObj;
        } catch (ClassCastException ex) {
            Log.e(this, "onSetFeatureResponseDone :: Exception " + ex);
        }
        if (listener == null) {
            Log.i(this, "onSetFeatureResponseDone :: listener is null");
            return;
        }
        if (capabilityStatusList == null) {
            Log.i(this, "onSetFeatureResponseDone :: capabilityStatusList is null");
            return;
        }
        for (CapabilityStatus capabilityStatus : capabilityStatusList) {
            if (ar.exception == null) {
                listener.onSetCapabilityValueSuccess(capabilityStatus.getCapability(), capabilityStatus.getRadioTech(), capabilityStatus.getStatus());
            } else {
                listener.onSetCapabilityValueFailure(capabilityStatus.getCapability(), capabilityStatus.getRadioTech(), SetCapabilityFailCause.ERROR_GENERIC);
            }
        }
    }

    private int getOperationStatus(Object item, Throwable ex) {
        Log.d(this, "getOperationStatus: " + ex);
        if (ex == null) {
            return 0;
        }
        if (item == null || !(item instanceof Integer) || !(ex instanceof ImsRilException)) {
            return 1;
        }
        int config = ((Integer) item).intValue();
        if (config != 1000) {
            return 1;
        }
        ImsRilException imsRilException = (ImsRilException) ex;
        if (imsRilException.getErrorCode() != 6) {
            return 1;
        }
        return 2;
    }

    private void enforceReadPhoneState(String fn) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", fn);
        }
    }

    protected Object sendRequest(int requestType, int item, int event, boolean boolVal, int intVal, String strVal) {
        SomeArgs request = SomeArgs.obtain();
        // 由 JADX 反編譯修正：依 smali，原始碼為 try / catch(IllegalArgumentException) / finally { return request; }
        try {
            request.arg1 = Integer.valueOf(item);
            this.mCi.sendConfigRequest(requestType, ImsCallUtils.convertImsConfigToImsConfigItem(item), boolVal, intVal, strVal, 0, this.mHandler.obtainMessage(event, request));
            synchronized (request) {
                while (request.arg2 == null) {
                    try {
                        request.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (IllegalArgumentException e2) {
            request.arg2 = -1;
        } finally {
            return request;
        }
    }

    public int getConfigInt(int item) {
        Log.d(this, "getConfigInt :: item=" + item);
        enforceReadPhoneState("getConfigInt");
        if (!ImsCallUtils.isConfigRequestValid(item, 1)) {
            Log.e(this, "Invalid API request for item");
            return -1;
        }
        SomeArgs result = (SomeArgs) sendRequest(45, item, 1, false, 0, null);
        int retVal = ((Integer) result.arg2).intValue() == 0 ? ((Integer) result.arg3).intValue() : -1;
        result.recycle();
        return retVal;
    }

    public String getConfigString(int item) {
        Log.d(this, "getConfigString :: item=" + item);
        enforceReadPhoneState("getConfigString");
        if (!ImsCallUtils.isConfigRequestValid(item, 2)) {
            Log.e(this, "Invalid API request for item");
            return null;
        }
        SomeArgs result = (SomeArgs) sendRequest(45, item, 1, false, 0, null);
        String retVal = ((Integer) result.arg2).intValue() == 0 ? (String) result.arg3 : null;
        result.recycle();
        return retVal;
    }

    private int sendSetConfigRequest(int item, int value) {
        SomeArgs result = (SomeArgs) sendRequest(44, item, 1, false, value, null);
        int retVal = ((Integer) result.arg2).intValue();
        result.recycle();
        return retVal;
    }

    protected void sendSetConfigRequestAsync(int item, int value) {
        Log.d(this, "sendSetConfigRequestAsync :: item=" + item + " value=" + value);
        this.mCi.sendConfigRequest(44, ImsCallUtils.convertImsConfigToImsConfigItem(item), false, value, null, 0, null);
    }

    public int setConfig(int item, int value) {
        Log.d(this, "setConfig :: item=" + item + " value=" + value);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setConfig");
        if (item == 26) {
            Log.w(this, "Ignore VoWiFi Roaming enable/disable");
            return 1;
        }
        if (!ImsCallUtils.isConfigRequestValid(item, 1)) {
            Log.e(this, "Invalid API request for item");
            return 1;
        }
        if (item == 27 && this.mServiceSub.IsWfcRoamingConfigurationSupportedByModem()) {
            Log.w(this, "VoWiFi mode: config update is done only when mode preference is changed or when sim is loaded");
            return 1;
        }
        return sendSetConfigRequest(item, value);
    }

    public int setConfig(int item, String value) {
        Log.d(this, "setConfig :: item=" + item + " value=" + value);
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setConfig");
        if (!ImsCallUtils.isConfigRequestValid(item, 2)) {
            Log.e(this, "Invalid API request for item");
            return 1;
        }
        SomeArgs result = (SomeArgs) sendRequest(44, item, 1, false, 0, value);
        int retVal = ((Integer) result.arg2).intValue();
        result.recycle();
        return retVal;
    }

    public void setCapabilityValue(ArrayList<CapabilityStatus> capabilityStatusList, SetCapabilityValueListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setCapabilityValue");
        this.mCi.setServiceStatus(this.mHandler.obtainMessage(2, listener), capabilityStatusList, 0);
    }

    public SubscriptionManager.OnSubscriptionsChangedListener getSubscriptionsChangeListener() {
        return this.mOnSubscriptionsChangeListener;
    }
}
