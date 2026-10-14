package org.codeaurora.ims;

import android.content.Context;
import android.os.Bundle;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.ims.internal.ICrsCrbtController;
import org.codeaurora.ims.internal.IImsMultiIdentityInterface;
import org.codeaurora.ims.internal.IImsScreenShareController;
import org.codeaurora.ims.internal.IQtiImsExtListener;

/* JADX INFO: loaded from: classes.dex */
public class QtiImsExt extends QtiImsExtBase {
    private final String MODIFY_PHONE_STATE = "android.permission.MODIFY_PHONE_STATE";
    private final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    private Context mContext;
    private ImsServiceSub[] mServiceSub;

    public QtiImsExt(Context context, ImsServiceSub[] serviceSub) {
        this.mContext = context;
        this.mServiceSub = serviceSub;
    }

    protected void onSetCallForwardUncondTimer(int phoneId, int startHour, int startMinute, int endHour, int endMinute, int action, int condition, int serviceClass, String number, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setCallForwardUncondTimer");
        this.mServiceSub[phoneId].setCallForwardUncondTimer(startHour, startMinute, endHour, endMinute, action, condition, serviceClass, number, listener);
    }

    protected void onGetCallForwardUncondTimer(int phoneId, int reason, int serviceClass, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "getCallForwardUncondTimer");
        this.mServiceSub[phoneId].getCallForwardUncondTimer(reason, serviceClass, listener);
    }

    protected void onResumePendingCall(int phoneId, int videoState) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "resumePendingCall");
        this.mServiceSub[phoneId].resumePendingCall(videoState);
    }

    protected void onSendCancelModifyCall(int phoneId, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "sendCancelModifyCall");
        this.mServiceSub[phoneId].sendCancelModifyCall(listener);
    }

    protected void onSetUssdInfoListener(int phoneId, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setUssdInfoListener");
        this.mServiceSub[phoneId].setUssdInfoListener(listener);
    }

    protected void onQueryVopsStatus(int phoneId, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "queryVopsStatus");
        this.mServiceSub[phoneId].queryVopsStatus(listener);
    }

    protected void onQuerySsacStatus(int phoneId, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "querySsacStatus");
        this.mServiceSub[phoneId].querySsacStatus(listener);
    }

    protected void onRegisterForParticipantStatusInfo(int phoneId, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "registerForParticipantStatusInfo");
        this.mServiceSub[phoneId].registerForParticipantStatusInfo(listener);
    }

    protected void onUpdateVoltePreference(int phoneId, int preference, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateVoltePreference");
        this.mServiceSub[phoneId].updateVoltePreference(preference, listener);
    }

    protected void onQueryVoltePreference(int phoneId, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "queryVoltePreference");
        this.mServiceSub[phoneId].queryVoltePreference(listener);
    }

    protected void onGetHandoverConfig(int phoneId, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "getHandoverConfig");
        this.mServiceSub[phoneId].getHandoverConfig(listener);
    }

    protected void onSetHandoverConfig(int phoneId, int hoConfig, IQtiImsExtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setHandoverConfig");
        this.mServiceSub[phoneId].setHandoverConfig(hoConfig, listener);
    }

    protected int onGetVvmAppConfig(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "getVvmAppConfig");
        try {
            return this.mServiceSub[phoneId].getConfigInterface().getConfigInt(1002);
        } catch (Exception re) {
            Log.e(this, "onGetVvmAppConfig :: Exception : " + re.getMessage());
            return -1;
        }
    }

    protected int onSetVvmAppConfig(int phoneId, int defaultVvmApp) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setVvmAppConfig");
        try {
            return this.mServiceSub[phoneId].getConfigInterface().setConfig(1002, defaultVvmApp);
        } catch (Exception re) {
            Log.e(this, "onSetVvmAppConfig :: Exception : " + re.getMessage());
            return -1;
        }
    }

    protected int onGetRcsAppConfig(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "getRcsAppConfig");
        try {
            return this.mServiceSub[phoneId].getConfigInterface().getConfigInt(1001);
        } catch (Exception re) {
            Log.e(this, "onGetRcsAppConfig :: Exception : " + re.getMessage());
            return -1;
        }
    }

    protected int onSetRcsAppConfig(int phoneId, int defaultSmsApp) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setRcsAppConfig");
        try {
            return this.mServiceSub[phoneId].getConfigInterface().setConfig(1001, defaultSmsApp);
        } catch (Exception re) {
            Log.e(this, "onSetRcsAppConfig :: Exception : " + re.getMessage());
            return -1;
        }
    }

    protected IImsMultiIdentityInterface onGetMultiIdentityInterface(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "onGetMultiIdentityInterface");
        ImsMultiIdentityControllerBase v = this.mServiceSub[phoneId].getMultiIdentityImpl();
        if (v != null) {
            return v.getBinder();
        }
        return null;
    }

    protected IImsScreenShareController onGetScreenShareController(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "onGetScreenShareController");
        ImsScreenShareControllerBase v = this.mServiceSub[phoneId].getScreenShareController();
        if (v != null) {
            return v.getBinder();
        }
        return null;
    }

    protected int onGetImsFeatureState(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "getImsFeatureState");
        return this.mServiceSub[phoneId].getImsFeatureState();
    }

    protected void onSetAnswerExtras(int phoneId, Bundle extras) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "onSetAnswerExtras");
        this.mServiceSub[phoneId].setAnswerExtras(extras);
    }

    protected boolean onIsCallComposerEnabled(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "onIsCallComposerEnabled");
        return this.mServiceSub[phoneId].isCallComposerSupported();
    }

    protected ICrsCrbtController onGetCrsCrbtController(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "onGetCrsCrbtController");
        CrsCrbtControllerBase v = this.mServiceSub[phoneId].getCrsCrbtController();
        if (v != null) {
            return v.getBinder();
        }
        return null;
    }
}
