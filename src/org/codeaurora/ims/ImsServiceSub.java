package org.codeaurora.ims;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.feature.CapabilityChangeRequest;
import android.telephony.ims.feature.ImsFeature;
import android.telephony.ims.feature.MmTelFeature;
import android.telephony.ims.stub.ImsCallSessionImplBase;
import android.telephony.ims.stub.ImsEcbmImplBase;
import android.telephony.ims.stub.ImsMultiEndpointImplBase;
import android.telephony.ims.stub.ImsSmsImplBase;
import android.util.Pair;
import android.widget.Toast;
import com.qualcomm.ims.utils.Log;
import com.qualcomm.ims.vt.ImsVideoGlobals;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;
import org.codeaurora.ims.internal.IQtiImsExtListener;
import org.codeaurora.ims.utils.CallComposerInfoUtils;
import org.codeaurora.ims.utils.QtiImsExtUtils;
import org.codeaurora.telephony.utils.AsyncResult;
import org.codeaurora.telephony.utils.CallForwardInfo;
import org.codeaurora.telephony.utils.RegistrantList;
import org.codeaurora.telephony.utils.SomeArgs;

/* JADX INFO: loaded from: classes.dex */
public class ImsServiceSub extends MmTelFeature implements ImsSubController.ImsStackConfigListener, ICallListListener {
    static final int CF_REASON_UNCONDITIONAL = 0;
    private static final int IKEv2_AUTH_FAILURE = 5;
    private static final int INVALID_CALL_TYPE = -2;
    private static final int INVALID_RAT_VALUE = -2;
    private static final String LOG_TAG = "ImsServiceSub";
    private static ExecutorService sTaskExecutor = Executors.newCachedThreadPool();
    private ImsFeature.CapabilityCallbackProxy mCapabilityCallback;
    protected ImsSenderRxr mCi;
    private ImsServiceClassTracker mConferenceRequestedbyTracker;
    private Context mContext;
    private CrsCrbtControllerImpl mCrsCrbtController;
    private Handler mFeatureCallbackHandler;
    private HandlerThread mFeatureHandlerThread;
    private Handler mHandler;
    private ImsConfigImpl mImsConfigImpl;
    private ImsEcbmImpl mImsEcbmImpl;
    private ImsMultiEndpointImpl mImsMultiEndpointImpl;
    private ImsRegistrationImpl mImsReg;
    private ImsServiceStateReceiver mImsServiceStateReceiver;
    private ImsSmsImpl mImsSms;
    private ImsUtImpl mImsUtImpl;
    private ImsMultiIdentityImpl mMultiIdentityImpl;
    private Mwi mMwiResponse;
    protected int mPhoneId;
    private ImsScreenShareControllerImpl mScreenShareController;
    private ImsSubController mSubController;
    private ImsServiceClassTracker mTracker;
    private ServiceStatus mDefaultServiceStatus = null;
    private HashSet<Uri> mSelfIndentityUris = null;
    private boolean mFeatureIsOpen = false;
    private boolean mIsVopsEnabled = false;
    private boolean mIsSsacVoiceBarred = false;
    private final int SSAC_VOICE_BARRING_ZERO = 0;
    private boolean mModemSupportForWfcRoamingConfiguration = false;
    private CopyOnWriteArrayList<IQtiImsExtListener> mQtiImsInterfaceListeners = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<IQtiImsExtListener> mQtiImsParticipantStatusListeners = new CopyOnWriteArrayList<>();
    private List<ICallListListener> mCallListListeners = new CopyOnWriteArrayList();
    private Object mMultiIdentityLock = new Object();
    private Object mScreenShareLock = new Object();
    private Object mCrsCrbtLock = new Object();
    private final int EVENT_CALL_STATE_CHANGE = 1;
    private final int EVENT_IMS_STATE_CHANGED = 2;
    private final int EVENT_IMS_STATE_DONE = 3;
    private final int EVENT_SRV_STATUS_UPDATE = 4;
    private final int EVENT_GET_SRV_STATUS = 5;
    private final int EVENT_SET_SRV_STATUS = 6;
    private final int EVENT_GET_CURRENT_CALLS = 7;
    private final int EVENT_SUPP_SRV_UPDATE = 8;
    private final int EVENT_SET_IMS_STATE = 9;
    private final int EVENT_TTY_STATE_CHANGED = 10;
    private final int EVENT_REFRESH_CONF_INFO = 11;
    private final int EVENT_HANDOVER_STATE_CHANGED = 12;
    private final int EVENT_CALL_MODIFY = 13;
    private final int EVENT_MWI = 14;
    private final int EVENT_CONFERENCE = 15;
    private final int EVENT_SET_CALL_FORWARD_TIMER = 16;
    private final int EVENT_GET_CALL_FORWARD_TIMER = 17;
    private final int EVENT_GEOLOCATION_INFO_REQUEST = 21;
    private final int EVENT_GEOLOCATION_RESPONSE = 22;
    private final int EVENT_VOWIFI_CALL_QUALITY_UPDATE = 23;
    private final int EVENT_VOPS_CHANGED = 25;
    private final int EVENT_SSAC_CHANGED = 26;
    private final int EVENT_VOPS_RESPONSE = 27;
    private final int EVENT_SSAC_RESPONSE = 28;
    private final int EVENT_PARTICIPANT_STATUS_INFO = 29;
    private final int EVENT_SET_VOLTE_PREF = 30;
    private final int EVENT_GET_VOLTE_PREF = 31;
    private final int EVENT_GET_HANDOVER_CONFIG = 32;
    private final int EVENT_SET_HANDOVER_CONFIG = 33;
    private final int EVENT_REGISTRATION_BLOCK_STATUS = 34;
    private final int EVENT_CANCEL_MODIFY_CALL = 35;
    private final int EVENT_CALL_AUTO_REJECT = 36;
    private final int EVENT_WFC_ROAMING_CONFIGURATION = 37;
    private final int EVENT_USSD_MESSAGE_RECEIVED = 38;
    private final int EVENT_SEND_AUTO_REJECT = 39;
    private final int EVENT_SEND_UI_TTY_MODE = 40;
    private final int EVENT_CALL_COMPOSER = 41;
    private final int EVENT_GEO_LOCATION_DATA_STATUS = 42;
    private int mSubId = -1;
    private MmTelFeature.MmTelCapabilities mMmtelCapabilities = new MmTelFeature.MmTelCapabilities(0);
    private CapabilityTracker mCapabilityTracker = new CapabilityTracker();
    private IQtiImsExtListener mUssdInfoListener = null;
    private RegistrantList mCapabilitiesChangedRegistrants = new RegistrantList();

    public ImsServiceSub(Context context, int phoneId, ImsSenderRxr senderRxr, ImsSubController subController) {
        this.mCi = null;
        this.mImsMultiEndpointImpl = null;
        this.mImsServiceStateReceiver = null;
        this.mPhoneId = -1;
        setFeatureState(1);
        this.mPhoneId = phoneId;
        this.mContext = context;
        this.mCi = senderRxr;
        this.mSubController = subController;
        logi("[phoneId=" + this.mPhoneId + "] Constructor :: Registering with Sub Controller.");
        this.mHandler = new ImsServiceSubHandler(this);
        this.mImsReg = new ImsRegistrationImpl();
        HandlerThread handlerThread = new HandlerThread(this + "FeatureHandlerThread");
        this.mFeatureHandlerThread = handlerThread;
        handlerThread.start();
        this.mFeatureCallbackHandler = new Handler(this.mFeatureHandlerThread.getLooper());
        this.mSubController.registerListener(this, phoneId);
        this.mCi.registerForImsNetworkStateChanged(this.mHandler, 2, null);
        this.mCi.registerForGeoLocationDataStatus(this.mHandler, 42, null);
        this.mCi.registerForSrvStatusUpdate(this.mHandler, 4, null);
        this.mCi.registerForCallStateChanged(this.mHandler, 1, null);
        this.mCi.registerForRefreshConfInfo(this.mHandler, 11, null);
        this.mCi.registerForHandoverStatusChanged(this.mHandler, 12, null);
        this.mCi.registerForGeolocationRequest(this.mHandler, 21, null);
        this.mCi.registerForVopsStatusChanged(this.mHandler, 25, null);
        this.mCi.registerForSsacStatusChanged(this.mHandler, 26, null);
        this.mCi.registerForParticipantStatusInfo(this.mHandler, 29, null);
        this.mCi.registerForRegistrationBlockStatus(this.mHandler, 34, null);
        this.mCi.registerForCallAutoRejection(this.mHandler, 36, null);
        this.mCi.registerForUssdInfo(this.mHandler, 38, null);
        this.mCi.registerForVoWiFiCallQualityUpdate(this.mHandler, 23, null);
        this.mCi.registerForCallComposerInfo(this.mHandler, 41, null);
        this.mImsEcbmImpl = new ImsEcbmImpl(this, this.mCi);
        this.mImsUtImpl = new ImsUtImpl(this, this.mCi, this.mContext);
        this.mImsConfigImpl = new ImsConfigImpl(this, this.mCi, this.mContext);
        this.mCi.registerForWfcRoamingModeFeatureSupport(this.mHandler, 37, null);
        this.mImsMultiEndpointImpl = new ImsMultiEndpointImpl(this.mCi, this.mContext, this);
        this.mImsSms = new ImsSmsImpl(this.mContext, this.mPhoneId, this.mCi);
        this.mCi.setOnSuppServiceNotification(this.mHandler, 8, null);
        this.mCi.registerForMwi(this.mHandler, 14, null);
        this.mMwiResponse = new Mwi();
        this.mCi.registerForTtyStatusChanged(this.mHandler, 10, null);
        this.mCi.registerForModifyCall(this.mHandler, 13, null);
        initServiceStatus();
        this.mImsServiceStateReceiver = new ImsServiceStateReceiver(this, this.mContext, this.mPhoneId);
        IntentFilter intentFilter = new IntentFilter("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mContext.registerReceiver(this.mImsServiceStateReceiver, intentFilter);
        this.mImsServiceStateReceiver.updateHDIcon(false, false);
        this.mImsServiceStateReceiver.updateVoWifiIcon(false, false);
        this.mCi.queryServiceStatus(this.mHandler.obtainMessage(5));
    }

    public void turnOnIms() {
        this.mCi.sendImsRegistrationState(1, this.mHandler.obtainMessage(9));
    }

    public void turnOffIms() {
        this.mCi.sendImsRegistrationState(2, this.mHandler.obtainMessage(9));
    }

    public void dispose() {
        setFeatureState(0);
        logd("dispose");
        HandlerThread handlerThread = this.mFeatureHandlerThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            this.mFeatureHandlerThread = null;
            this.mFeatureCallbackHandler = null;
        }
        this.mImsMultiEndpointImpl = null;
        this.mContext.unregisterReceiver(this.mImsServiceStateReceiver);
        this.mImsServiceStateReceiver = null;
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        if (imsServiceClassTracker != null) {
            imsServiceClassTracker.removeListener(this);
            this.mTracker = null;
        }
        ImsConfigImpl imsConfigImpl = this.mImsConfigImpl;
        if (imsConfigImpl != null) {
            imsConfigImpl.dispose();
        }
        this.mCi.deregisterForCallComposerInfo(this.mHandler);
        this.mCi.unregisterForGeoLocationDataStatus(this.mHandler);
        this.mCi.unregisterForUssdInfo(this.mHandler);
        this.mCi.unregisterForImsNetworkStateChanged(this.mHandler);
        this.mCi.unregisterForSrvStatusUpdate(this.mHandler);
        this.mCi.unregisterForCallStateChanged(this.mHandler);
        this.mCi.unregisterForRefreshConfInfo(this.mHandler);
        this.mCi.unregisterForHandoverStatusChanged(this.mHandler);
        this.mCi.unregisterForGeolocationRequest(this.mHandler);
        this.mCi.unregisterForVopsStatusChanged(this.mHandler);
        this.mCi.unregisterForSsacStatusChanged(this.mHandler);
        this.mCi.unregisterForParticipantStatusInfo(this.mHandler);
        this.mCi.unregisterForRegistrationBlockStatus(this.mHandler);
        this.mCi.unregisterForCallAutoRejection(this.mHandler);
        this.mCi.unregisterForVoWiFiCallQualityUpdate(this.mHandler);
        this.mCi.unRegisterForWfcRoamingModeFeatureSupport(this.mHandler);
        this.mCi.unSetOnSuppServiceNotification(this.mHandler);
        this.mCi.unregisterForMwi(this.mHandler);
        this.mCi.unregisterForTtyStatusChanged(this.mHandler);
        this.mCi.unregisterForModifyCall(this.mHandler);
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionAdded(ImsCallSessionImpl callSession) {
        for (ICallListListener listener : this.mCallListListeners) {
            listener.onSessionAdded(callSession);
        }
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionRemoved(ImsCallSessionImpl callSession) {
        for (ICallListListener listener : this.mCallListListeners) {
            listener.onSessionRemoved(callSession);
        }
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionAdded(final ImsUssdSessionImpl ussdSession) {
        this.mCallListListeners.forEach(new Consumer() { // from class: org.codeaurora.ims.ImsServiceSub$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ICallListListener) obj).onSessionAdded(ussdSession);
            }
        });
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionRemoved(final ImsUssdSessionImpl ussdSession) {
        this.mCallListListeners.forEach(new Consumer() { // from class: org.codeaurora.ims.ImsServiceSub$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ICallListListener) obj).onSessionRemoved(ussdSession);
            }
        });
    }

    @Override // org.codeaurora.ims.ImsSubController.ImsStackConfigListener
    public void onStackConfigChanged(boolean[] activeStacks, int phoneId) {
        if (this.mPhoneId != phoneId && this.mSubController.isDsdv()) {
            logv("onStackConfigChanged safely ignore the indication");
            return;
        }
        try {
            logi("onStackConfigChanged :: activeStacks[" + this.mPhoneId + "]=" + activeStacks[this.mPhoneId]);
            int featureState = activeStacks[this.mPhoneId] ? 2 : 0;
            int oldFeatureState = getFeatureState();
            logi("oldFeatureState = " + oldFeatureState);
            if (oldFeatureState == 2 && featureState == 0) {
                resetCapabilities();
                ImsRegistrationInfo imsRegInfo = new ImsRegistrationInfo();
                imsRegInfo.setState(2);
                Message msg = this.mHandler.obtainMessage(2);
                AsyncResult.forMessage(msg, imsRegInfo, (Throwable) null);
                msg.sendToTarget();
                ImsServiceStateReceiver imsServiceStateReceiver = this.mImsServiceStateReceiver;
                if (imsServiceStateReceiver != null) {
                    imsServiceStateReceiver.updateHDIcon(false, false);
                    this.mImsServiceStateReceiver.updateVoWifiIcon(false, false);
                } else {
                    loge("onStackConfigChanged :: mImsServiceStateReceiver null");
                }
            } else if (oldFeatureState != 2 && featureState == 2) {
                this.mCi.sendConfigRequest(44, 74, false, QtiImsExtUtils.getAutoRejectMode(this.mContext.getContentResolver(), this.mPhoneId), null, 0, this.mHandler.obtainMessage(39));
            }
            setFeatureState(featureState);
        } catch (ArrayIndexOutOfBoundsException e) {
            loge("onStackConfigChanged :: Invalid activeStacks length!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initServiceStatus() {
        ServiceStatus serviceStatus = new ServiceStatus();
        this.mDefaultServiceStatus = serviceStatus;
        serviceStatus.accessTechStatus = new ServiceStatus.StatusForAccessTech[1];
        this.mDefaultServiceStatus.accessTechStatus[0] = new ServiceStatus.StatusForAccessTech();
        this.mDefaultServiceStatus.accessTechStatus[0].networkMode = 14;
        this.mDefaultServiceStatus.accessTechStatus[0].status = 3;
        this.mDefaultServiceStatus.accessTechStatus[0].registered = 2;
        this.mDefaultServiceStatus.status = 3;
    }

    public void sendConferenceRequest(ImsServiceClassTracker sct) {
        this.mConferenceRequestedbyTracker = sct;
        this.mCi.conference(this.mHandler.obtainMessage(15, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConferenceResponse(AsyncResult ar) {
        this.mConferenceRequestedbyTracker.handleConferenceResult(ar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object getCallsListToClear() {
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        if (imsServiceClassTracker == null) {
            return null;
        }
        Object mmTelCallsList = imsServiceClassTracker.getCallsListToClear();
        return mmTelCallsList;
    }

    public void onFeatureReady() {
        logv("onFeatureReady");
        if (this.mTracker == null) {
            logv("onFeatureReady :: creating ImsServiceClassTracker");
            ImsServiceClassTracker imsServiceClassTracker = new ImsServiceClassTracker(this.mCi, this.mContext, this);
            this.mTracker = imsServiceClassTracker;
            imsServiceClassTracker.updateFeatureCapabilities(isVideoSupported(), isVoiceSupported());
            this.mTracker.addListener(this);
        }
        notifyFeatureCapabilityChange();
        this.mCi.getImsRegistrationState(this.mHandler.obtainMessage(3));
        this.mFeatureIsOpen = true;
    }

    public void onFeatureRemoved() {
        logd("onFeatureRemoved");
        this.mFeatureIsOpen = false;
    }

    public ImsCallProfile createCallProfile(int callSessionType, int callType) {
        if (this.mTracker == null) {
            loge("createCallProfile :: Null ImsServiceClassTracker!");
            return null;
        }
        ImsCallProfile profile = new ImsCallProfile(callSessionType, callType);
        return profile;
    }

    public ImsCallSessionImplBase createCallSession(ImsCallProfile profile) {
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        if (imsServiceClassTracker == null) {
            loge("createCallSession :: Null ImsServiceClassTracker!");
            return null;
        }
        ImsCallSessionImplBase session = imsServiceClassTracker.createCallSession(profile, null);
        return session;
    }

    public ImsRegistrationImpl getImsRegistrationInterface() {
        return this.mImsReg;
    }

    public void setUiTtyMode(int mode, Message onCompleteMessage) {
        this.mCi.setUiTTYMode(mode, this.mHandler.obtainMessage(40, onCompleteMessage));
    }

    public ImsSmsImplBase getSmsImplementation() {
        return this.mImsSms;
    }

    public ImsUtImpl getUt() {
        return this.mImsUtImpl;
    }

    public ImsConfigImpl getConfigInterface() {
        return this.mImsConfigImpl;
    }

    public ImsEcbmImplBase getEcbm() {
        return this.mImsEcbmImpl;
    }

    public boolean queryCapabilityConfiguration(int capability, int radioTech) {
        logi("queryCapabilityConfiguration :: capability=" + capability + " radioTech=" + radioTech);
        if (radioTech == -1) {
            return false;
        }
        return this.mCapabilityTracker.isSupportedOnRadioTech(capability, radioTech);
    }

    private void notifyFeatureCapabilityChange() {
        final MmTelFeature.MmTelCapabilities c = new MmTelFeature.MmTelCapabilities(this.mMmtelCapabilities);
        Handler handler = this.mFeatureCallbackHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: org.codeaurora.ims.ImsServiceSub$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImsServiceSub.this.m179lambda$notifyFeatureCapabilityChange$2$orgcodeauroraimsImsServiceSub(c);
                }
            });
        }
    }

    /* JADX INFO: renamed from: lambda$notifyFeatureCapabilityChange$2$org-codeaurora-ims-ImsServiceSub, reason: not valid java name */
    /* synthetic */ void m179lambda$notifyFeatureCapabilityChange$2$orgcodeauroraimsImsServiceSub(MmTelFeature.MmTelCapabilities c) {
        logd("notifyFeatureCapabilityChange :: Capabilities = " + c);
        notifyCapabilitiesStatusChanged(c);
    }

    public void changeEnabledCapabilities(CapabilityChangeRequest request, ImsFeature.CapabilityCallbackProxy c) {
        if (request == null || c == null) {
            loge("changeEnabledCapabilities :: Invalid argument(s).");
            return;
        }
        List<CapabilityChangeRequest.CapabilityPair> capsToEnable = request.getCapabilitiesToEnable();
        List<CapabilityChangeRequest.CapabilityPair> capsToDisable = request.getCapabilitiesToDisable();
        if (capsToEnable.isEmpty() && capsToDisable.isEmpty()) {
            loge("changeEnabledCapabilities :: No CapabilityPair objects to process!");
            return;
        }
        this.mCapabilityCallback = c;
        ArrayList<CapabilityStatus> capabilityStatusList = new ArrayList<>();
        for (CapabilityChangeRequest.CapabilityPair cp : capsToEnable) {
            CapabilityStatus cs = new CapabilityStatus(cp.getCapability(), cp.getRadioTech(), 1);
            capabilityStatusList.add(cs);
        }
        for (CapabilityChangeRequest.CapabilityPair cp2 : capsToDisable) {
            CapabilityStatus cs2 = new CapabilityStatus(cp2.getCapability(), cp2.getRadioTech(), 0);
            capabilityStatusList.add(cs2);
        }
        this.mImsConfigImpl.setCapabilityValue(capabilityStatusList, new SetCapabilityListener());
    }

    private class SetCapabilityListener implements ImsConfigImpl.SetCapabilityValueListener {
        private SetCapabilityListener() {
        }

        @Override // org.codeaurora.ims.ImsConfigImpl.SetCapabilityValueListener
        public void onSetCapabilityValueSuccess(int capability, int radioTech, int value) {
            ImsServiceSub.this.logd("onSetCapabilityValueSuccess :: capability=" + capability + " radioTech=" + radioTech + " value=" + value);
        }

        @Override // org.codeaurora.ims.ImsConfigImpl.SetCapabilityValueListener
        public void onSetCapabilityValueFailure(int capability, int radioTech, ImsConfigImpl.SetCapabilityFailCause reason) {
            ImsServiceSub.this.logd("onSetCapabilityValueFailure :: capability=" + capability + " radioTech=" + radioTech + " reason=" + reason);
            if (ImsServiceSub.this.mCapabilityCallback == null) {
                ImsServiceSub.this.loge("onSetCapabilityValueFailure :: Null mCapabilityCallback!");
            } else {
                ImsServiceSub.this.mCapabilityCallback.onChangeCapabilityConfigurationError(capability, radioTech, ImsServiceSub.getSetCapabilityFailError(reason));
            }
        }
    }

    /* JADX INFO: renamed from: org.codeaurora.ims.ImsServiceSub$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$org$codeaurora$ims$ImsConfigImpl$SetCapabilityFailCause;

        static {
            int[] iArr = new int[ImsConfigImpl.SetCapabilityFailCause.values().length];
            $SwitchMap$org$codeaurora$ims$ImsConfigImpl$SetCapabilityFailCause = iArr;
            try {
                iArr[ImsConfigImpl.SetCapabilityFailCause.ERROR_GENERIC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codeaurora$ims$ImsConfigImpl$SetCapabilityFailCause[ImsConfigImpl.SetCapabilityFailCause.ERROR_SUCCESS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getSetCapabilityFailError(ImsConfigImpl.SetCapabilityFailCause reason) {
        switch (AnonymousClass2.$SwitchMap$org$codeaurora$ims$ImsConfigImpl$SetCapabilityFailCause[reason.ordinal()]) {
            case 1:
                return -1;
            case 2:
                return 0;
            default:
                return -1;
        }
    }

    void onIccLoaded() {
        logd("onIccLoaded...");
        notifyFeatureCapabilityChange();
    }

    private boolean isSrvTypeValid(int type) {
        switch (type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 25:
            case 26:
            case 27:
                return true;
            default:
                return false;
        }
    }

    private void resetCapabilities() {
        this.mCapabilityTracker.clear();
        this.mMmtelCapabilities = new MmTelFeature.MmTelCapabilities(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSrvStatusUpdate(ArrayList<ServiceStatus> updateList) {
        resetCapabilities();
        for (ServiceStatus update : updateList) {
            logi("type = " + update.type + " status = " + update.status + " isValid = " + update.isValid);
            if (update != null && update.isValid && isSrvTypeValid(update.type)) {
                ServiceStatus srvSt = this.mDefaultServiceStatus;
                srvSt.isValid = update.isValid;
                srvSt.type = update.type;
                if (update.userdata != null) {
                    srvSt.userdata = new byte[update.userdata.length];
                    srvSt.userdata = Arrays.copyOf(update.userdata, update.userdata.length);
                }
                if (update.accessTechStatus != null && update.accessTechStatus.length > 0) {
                    srvSt.accessTechStatus = new ServiceStatus.StatusForAccessTech[update.accessTechStatus.length];
                    logi("Call Type " + srvSt.type + "has num updates = " + update.accessTechStatus.length);
                    ServiceStatus.StatusForAccessTech[] actSt = srvSt.accessTechStatus;
                    for (int i = 0; i < update.accessTechStatus.length; i++) {
                        ServiceStatus.StatusForAccessTech actUpdate = update.accessTechStatus[i];
                        logi("StatusForAccessTech networkMode = " + actUpdate.networkMode + " registered = " + actUpdate.registered + " status = " + actUpdate.status + " restrictCause = " + actUpdate.restrictCause);
                        actSt[i] = new ServiceStatus.StatusForAccessTech();
                        actSt[i].networkMode = actUpdate.networkMode;
                        actSt[i].registered = actUpdate.registered;
                        boolean z = true;
                        if (actUpdate.status == 2 && actUpdate.restrictCause != 0) {
                            actSt[i].status = 1;
                        } else {
                            actSt[i].status = actUpdate.status;
                        }
                        srvSt.status = actSt[i].status;
                        actSt[i].restrictCause = actUpdate.restrictCause;
                        int registrationTech = mapToImsRegistrationTech(actSt[i].networkMode);
                        int capability = mapToMmTelCapability(update.type);
                        if (canAddCapability(capability, actSt[i].status, registrationTech)) {
                            this.mCapabilityTracker.addCapability(capability, registrationTech);
                        } else if (update.type == 26 && registrationTech != -1) {
                            if (actSt[i].status != 1 && actSt[i].status != 2) {
                                z = false;
                            }
                            boolean status = z;
                            this.mCapabilityTracker.setIsCallComposerSupported(status);
                        } else if (update.type == 27 && registrationTech != -1) {
                            if (actSt[i].status != 1 && actSt[i].status != 2) {
                                z = false;
                            }
                            boolean status2 = z;
                            this.mCapabilityTracker.setIsUssdSupported(status2);
                        }
                    }
                }
            }
        }
        logd("handleSrvStatusUpdate ::  Call Composer updated to: " + isCallComposerSupported() + " USSD capability updated to: " + isUssdSupported());
        for (CapabilityChangeRequest.CapabilityPair cp : this.mCapabilityTracker.getEnabledCapabilities()) {
            this.mMmtelCapabilities.addCapabilities(cp.getCapability());
        }
        logd("handleSrvStatusUpdate :: mMmtelCapabilities updated to: " + this.mMmtelCapabilities);
        notifyFeatureCapabilityChange();
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        if (imsServiceClassTracker == null) {
            loge("handleSrvStatusUpdate :: tracker null; not updating global VT capability");
        } else {
            imsServiceClassTracker.updateFeatureCapabilities(isVideoSupported(), isVoiceSupported());
        }
        ImsServiceStateReceiver imsServiceStateReceiver = this.mImsServiceStateReceiver;
        if (imsServiceStateReceiver == null) {
            loge("handleSrvStatusUpdate :: mImsServiceStateReceiver null");
        } else {
            imsServiceStateReceiver.updateHDIcon(isVideoSupported(), isVoiceSupported());
            logi("updateVoWifiIcon");
            this.mImsServiceStateReceiver.updateVoWifiIcon(isVideoSupportedOverWifi(), isVoiceSupportedOverWifi());
        }
        if (this.mCapabilitiesChangedRegistrants == null) {
            loge("handleSrvStatusUpdate :: mCapabilitiesChangedRegistrants null");
        } else {
            Pair<Boolean, Boolean> result = new Pair<>(Boolean.valueOf(isVideoSupported()), Boolean.valueOf(isVoiceSupported()));
            this.mCapabilitiesChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, result, (Throwable) null));
        }
    }

    private static int mapToImsRegistrationTech(int radioTech) {
        switch (radioTech) {
            case -1:
            case 14:
            case 20:
                return 0;
            case 18:
            case 19:
                return 1;
            default:
                return -1;
        }
    }

    private static int mapToMmTelCapability(int callType) {
        switch (callType) {
            case 0:
                return 1;
            case 1:
            case 2:
            case 3:
                return 2;
            case 5:
                return 8;
            case 25:
                return 4;
            default:
                return -2;
        }
    }

    private boolean canAddCapability(int capability, int status, int registrationTech) {
        logd("canAddCapability capability: " + capability + " , registrationTech: " + registrationTech);
        if (capability == -2 || registrationTech == -1) {
            return false;
        }
        logd("canAddCapability status: " + status);
        if (capability == 8) {
            return status == 2 && this.mCi.isSmsSupported();
        }
        return status == 2 || status == 1;
    }

    public boolean isVideoSupported() {
        return this.mCapabilityTracker.isVideoSupported();
    }

    public boolean isVoiceSupported() {
        return this.mCapabilityTracker.isVoiceSupported();
    }

    public boolean isVideoSupportedOverWifi() {
        return this.mCapabilityTracker.isVideoSupportedOverWifi();
    }

    public boolean isVoiceSupportedOverWifi() {
        return this.mCapabilityTracker.isVoiceSupportedOverWifi();
    }

    public boolean isCallComposerSupported() {
        return this.mCapabilityTracker.isCallComposerSupported();
    }

    public boolean isUssdSupported() {
        return this.mCapabilityTracker.isUssdSupported();
    }

    public void updateLowBatteryStatus() {
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        if (imsServiceClassTracker != null) {
            imsServiceClassTracker.updateLowBatteryStatus();
        }
    }

    public void setCallForwardUncondTimer(int startHour, int startMinute, int endHour, int endMinute, int action, int condition, int serviceClass, String number, IQtiImsExtListener listener) {
        logi("setCallForwardUncondTimer");
        this.mCi.setCallForwardUncondTimer(startHour, startMinute, endHour, endMinute, action, condition, serviceClass, number, this.mHandler.obtainMessage(16, listener));
    }

    public void getCallForwardUncondTimer(int reason, int serviceClass, IQtiImsExtListener listener) {
        logi("getCallForwardUncondTimer reason is" + reason + "serviceClass = " + serviceClass);
        this.mCi.queryCallForwardStatus(reason, serviceClass, null, this.mHandler.obtainMessage(17, listener));
    }

    public void sendCancelModifyCall(IQtiImsExtListener listener) {
        List<ImsCallSessionImpl> sessionList = getCallSessionByState(DriverCallIms.State.ACTIVE);
        if (sessionList.isEmpty()) {
            Log.i(this, "sendCancelModifyCall: no call is available to cancel modify call");
            if (listener != null) {
                try {
                    listener.receiveCancelModifyCallResponse(this.mPhoneId, 1);
                    return;
                } catch (Throwable th) {
                    loge("sendCancelModifyCall exception!");
                    return;
                }
            }
            Log.i(this, "sendCancelModifyCall: no listener is available");
            return;
        }
        int nCallId = Integer.parseInt(sessionList.get(0).getCallId());
        Log.i(this, "sendCancelModifyCall: call ID " + nCallId);
        this.mCi.cancelModifyCall(this.mHandler.obtainMessage(35, listener), nCallId);
    }

    public void setUssdInfoListener(IQtiImsExtListener listener) {
        logv("setUssdInfoListener");
        this.mUssdInfoListener = listener;
    }

    public void resumePendingCall(int videoState) {
        List<ImsCallSessionImpl> sessionList = getCallSessionByState(DriverCallIms.State.DIALING);
        if (sessionList.isEmpty()) {
            logd("resumePendingCall: no call is available");
        } else {
            sessionList.get(0).resumePendingCall(videoState);
        }
    }

    public void getHandoverConfig(IQtiImsExtListener listener) {
        this.mCi.getHandoverConfig(this.mHandler.obtainMessage(32, listener));
    }

    public void setHandoverConfig(int hoConfig, IQtiImsExtListener listener) {
        this.mCi.setHandoverConfig(hoConfig, this.mHandler.obtainMessage(33, listener));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isImsExceptionRadioNotAvailable(Throwable e) {
        return e != null && (e instanceof RuntimeException) && ((RuntimeException) e).getMessage().equals(ImsSenderRxr.errorIdToString(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCalls(AsyncResult ar) {
        ArrayList<DriverCallIms> callList;
        logi(">handleCalls");
        new HashMap();
        if (ar.exception == null) {
            callList = (ArrayList) ar.result;
        } else if (isImsExceptionRadioNotAvailable(ar.exception)) {
            callList = new ArrayList<>();
        } else {
            return;
        }
        ArrayList<DriverCallIms> mmTelList = new ArrayList<>();
        if (callList != null) {
            for (DriverCallIms dc : callList) {
                logi("handleCalls: dc = " + dc);
                mmTelList.add(dc);
            }
        }
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        if (imsServiceClassTracker == null) {
            loge("handleCalls :: Null mTracker!");
        } else {
            imsServiceClassTracker.handleCalls(mmTelList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHandover(AsyncResult ar) {
        logi("handleHandover");
        if (ar.exception == null) {
            HoInfo handover = (HoInfo) ar.result;
            ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
            if (imsServiceClassTracker != null) {
                imsServiceClassTracker.handleHandover(handover);
                return;
            }
            return;
        }
        loge("AsyncResult exception in handleHandover- " + ar.exception);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSuppSvc(AsyncResult ar) {
        logi("handleSuppSvc");
        if (ar.exception == null) {
            SuppNotifyInfo supp_svc_unsol = (SuppNotifyInfo) ar.result;
            ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
            if (imsServiceClassTracker != null) {
                imsServiceClassTracker.handleSuppSvcUnsol(supp_svc_unsol);
                return;
            }
            return;
        }
        loge("AsyncResult exception in handleSuppSvc.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTtyModeChange(AsyncResult ar) {
        logi("handleTtyModeChange");
        if (ar != null && ar.result != null && ar.exception == null) {
            int[] mode = (int[]) ar.result;
            loge("Received EVENT_TTY_STATE_CHANGED mode= " + mode[0]);
            ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
            if (imsServiceClassTracker != null) {
                imsServiceClassTracker.handleTtyModeChangeUnsol(mode[0]);
                return;
            }
            return;
        }
        loge("Error EVENT_TTY_STATE_CHANGED AsyncResult ar= " + ar);
    }

    public List<ImsCallSessionImpl> getCallSessionByState(DriverCallIms.State state) {
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        return imsServiceClassTracker == null ? Collections.EMPTY_LIST : imsServiceClassTracker.getCallSessionByState(state);
    }

    public ImsCallSessionImpl findSessionByMediaId(int mediaId) {
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        if (imsServiceClassTracker == null) {
            return null;
        }
        return imsServiceClassTracker.findSessionByMediaId(mediaId);
    }

    public int getPhoneId() {
        return this.mPhoneId;
    }

    public void addListener(ICallListListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("addListener error: listener is null.");
        }
        if (!this.mCallListListeners.contains(listener)) {
            this.mCallListListeners.add(listener);
        } else {
            Log.w(this, "addListener error: Duplicate listener, " + listener);
        }
    }

    public void removeListener(ICallListListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("removeListener error: listener is null.");
        }
        if (this.mCallListListeners.contains(listener)) {
            this.mCallListListeners.remove(listener);
        } else {
            Log.w(this, "removeListener error: Listener not found, " + listener);
        }
    }

    public void handleRefreshConfInfo(AsyncResult ar) {
        logi("handleRefreshConfInfo");
        if (ar != null && ar.exception == null && ar.result != null) {
            ConfInfo confInfo = (ConfInfo) ar.result;
            ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
            if (imsServiceClassTracker != null) {
                imsServiceClassTracker.handleRefreshConfInfo(confInfo);
                return;
            }
            return;
        }
        if (ar != null) {
            loge("Failed @handleRefreshConfInfo --> " + ar.exception + "with result = " + ar.result);
        } else {
            loge("Failed @handleRefreshConfInfo --> AsyncResult is null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleModifyCallRequest(CallModify cm) {
        logi("handleCallModifyRequest(" + cm + ")");
        ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
        if (imsServiceClassTracker != null) {
            imsServiceClassTracker.handleModifyCallRequest(cm);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBroadcastForDisconnected(ImsReasonInfo imsReasonInfo, int regState) {
        Intent intent = new Intent("com.android.imsconnection.DISCONNECTED");
        intent.putExtra("result", imsReasonInfo);
        intent.putExtra("stateChanged", regState);
        this.mContext.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
        logi("sendBroadcastForDisconnected");
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public ImsServiceClassTracker getImsServiceClassTracker() {
        return this.mTracker;
    }

    public boolean getFeatureIsOpen() {
        return this.mFeatureIsOpen;
    }

    private class ImsServiceSubHandler extends Handler {
        ImsServiceSubHandler(ImsServiceSub imsServiceSub) {
            this(Looper.getMainLooper());
        }

        ImsServiceSubHandler(Looper looper) {
            super(looper);
        }

        private void notifyRegChange(final int registrationState, final ImsReasonInfo imsReasonInfo, final int imsRadioTech) {
            Runnable r = new Runnable() { // from class: org.codeaurora.ims.ImsServiceSub.ImsServiceSubHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    switch (registrationState) {
                        case 1:
                            ImsServiceSub.this.mImsReg.registeredWithRadioTech(imsRadioTech);
                            break;
                        case 2:
                            ImsServiceSub.this.mImsReg.registrationDisconnected(imsReasonInfo);
                            break;
                        case 3:
                            ImsServiceSub.this.mImsReg.registeringWithRadioTech(imsRadioTech);
                            break;
                    }
                }
            };
            if (ImsServiceSub.this.mFeatureCallbackHandler != null) {
                ImsServiceSub.this.mFeatureCallbackHandler.post(r);
            }
        }

        private void notifyRegAssociatedUriChange(final Uri[] self_identity) {
            Runnable r = new Runnable() { // from class: org.codeaurora.ims.ImsServiceSub.ImsServiceSubHandler.2
                @Override // java.lang.Runnable
                public void run() {
                    ImsServiceSub.this.mImsReg.registrationAssociatedUriChanged(self_identity);
                }
            };
            if (ImsServiceSub.this.mFeatureCallbackHandler != null) {
                ImsServiceSub.this.mFeatureCallbackHandler.post(r);
            }
        }

        private void handleImsStateChanged(AsyncResult ar) {
            ImsServiceSub.this.logi("handleImsStateChanged");
            int errorCode = 0;
            String errorMessage = null;
            int regState = 2;
            int imsRat = 0;
            Uri[] selfIdentityUris = null;
            if (ar != null && ar.exception == null && (ar.result instanceof ImsRegistrationInfo)) {
                ImsRegistrationInfo registration = (ImsRegistrationInfo) ar.result;
                errorCode = registration.getErrorCode();
                errorMessage = registration.getErrorMessage();
                String selfIdentityUrisCombined = registration.getPAssociatedUris();
                regState = registration.getState();
                imsRat = getRilRadioTech(registration);
                if (regState != 2) {
                    ImsServiceSub.this.mCi.queryServiceStatus(ImsServiceSub.this.mHandler.obtainMessage(5));
                }
                if (selfIdentityUrisCombined != null) {
                    selfIdentityUris = extractUrisFromPipeSeparatedUriStrings(selfIdentityUrisCombined);
                }
            } else {
                ImsServiceSub.this.loge("handleImsStateChanged error");
            }
            ImsReasonInfo imsReasonInfo = new ImsReasonInfo(1000, errorCode, errorMessage);
            int wfcRegState = regState;
            if (imsRat == 13) {
                wfcRegState = 2;
                ImsServiceSub.this.logi("VOLTE ims registered, WFC change to Not  registered");
            }
            ImsServiceSub.this.sendBroadcastForDisconnected(imsReasonInfo, wfcRegState);
            notifyRegChange(regState, imsReasonInfo, imsRat);
            if (selfIdentityUris != null && selfIdentityUrisHaveChanged(selfIdentityUris)) {
                updateSelfIdentityUriCache(selfIdentityUris);
                notifyRegAssociatedUriChange(selfIdentityUris);
            }
        }

        private int getRilRadioTech(ImsRegistrationInfo registration) {
            if (registration.getRadioTech() == Integer.MAX_VALUE) {
                return 0;
            }
            switch (registration.getRadioTech()) {
                case 14:
                case 20:
                    return 13;
                case 15:
                case 16:
                case 17:
                default:
                    return 0;
                case 18:
                case 19:
                    return 18;
            }
        }

        private Uri[] extractUrisFromPipeSeparatedUriStrings(String combinedUris) {
            if (combinedUris == null || combinedUris.length() <= 1) {
                return null;
            }
            String[] uriStrings = combinedUris.split("\\|");
            Uri[] uris = new Uri[uriStrings.length];
            for (int i = 0; i < uriStrings.length; i++) {
                uris[i] = Uri.parse(uriStrings[i]);
            }
            return uris;
        }

        private boolean selfIdentityUrisHaveChanged(Uri[] new_uris) {
            if (ImsServiceSub.this.mSelfIndentityUris == null || new_uris.length != ImsServiceSub.this.mSelfIndentityUris.size() || ImsServiceSub.this.mSelfIndentityUris == null) {
                return true;
            }
            for (Uri uri : new_uris) {
                if (!ImsServiceSub.this.mSelfIndentityUris.contains(uri)) {
                    return true;
                }
            }
            return false;
        }

        private void updateSelfIdentityUriCache(Uri[] new_uris) {
            if (ImsServiceSub.this.mSelfIndentityUris == null) {
                ImsServiceSub.this.mSelfIndentityUris = new HashSet();
            } else {
                ImsServiceSub.this.mSelfIndentityUris.clear();
            }
            for (int i = 0; i < new_uris.length; i++) {
                ImsServiceSub.this.mSelfIndentityUris.add(new_uris[i]);
                Log.i(this, "updateSelfIdentityUriCache :: new self-identity host URI=" + new_uris[i]);
            }
        }

        private void handleGeoLocationDataStatus(AsyncResult ar) {
            ImsServiceSub.this.logi("handleGeoLocationDataStatus");
            if (ar == null || ar.result == null) {
                ImsServiceSub.this.loge("Error EVENT_GEO_LOCATION_DATA_STATUS AsyncResult ar = " + ar);
                return;
            }
            int geoLocationStatus = ((Integer) ar.result).intValue();
            ImsReasonInfo imsReasonInfo = new ImsReasonInfo(1000, geoLocationStatus, "");
            notifyRegChange(2, imsReasonInfo, 0);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(this, "Message received: what = " + msg.what);
            switch (msg.what) {
                case 1:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar.result != null) {
                        ImsServiceSub.this.handleCalls(ar);
                    } else if (ImsServiceSub.this.isImsExceptionRadioNotAvailable(ar.exception)) {
                        ImsServiceSub.this.loge("EVENT_CALL_STATE_CHANGE when Radio is Unavailable");
                        ar.exception = null;
                        ar.result = ImsServiceSub.this.getCallsListToClear();
                        if (ar.result != null) {
                            ImsServiceSub.this.handleCalls(ar);
                        } else {
                            ImsServiceSub.this.loge("EVENT_CALL_STATE_CHANGE with no calls ignored!");
                        }
                    } else {
                        ImsServiceSub.this.loge("EVENT_CALL_STATE_CHANGE exception " + ar.exception);
                    }
                    break;
                case 2:
                    AsyncResult ar2 = (AsyncResult) msg.obj;
                    if (ar2.result == null) {
                        ImsServiceSub.this.mCi.getImsRegistrationState(obtainMessage(3));
                    } else {
                        handleImsStateChanged(ar2);
                    }
                    break;
                case 3:
                    handleImsStateChanged((AsyncResult) msg.obj);
                    break;
                case 4:
                    ImsServiceSub.this.logi("Received event: EVENT_SRV_STATUS_UPDATE");
                    AsyncResult arResult = (AsyncResult) msg.obj;
                    if (arResult.exception == null && arResult.result != null) {
                        ArrayList<ServiceStatus> responseArray = (ArrayList) arResult.result;
                        ImsServiceSub.this.handleSrvStatusUpdate(responseArray);
                    } else {
                        ImsServiceSub.this.loge("IMS Service Status Update failed!");
                        ImsServiceSub.this.initServiceStatus();
                    }
                    break;
                case 5:
                    ImsServiceSub.this.logi("Received event: EVENT_GET_STATUS_UPDATE");
                    AsyncResult arResultSrv = (AsyncResult) msg.obj;
                    if (arResultSrv.exception == null && arResultSrv.result != null) {
                        ArrayList<ServiceStatus> responseArray2 = (ArrayList) arResultSrv.result;
                        ImsServiceSub.this.handleSrvStatusUpdate(responseArray2);
                    } else {
                        ImsServiceSub.this.loge("IMS Service Status Update failed!");
                        ImsServiceSub.this.initServiceStatus();
                    }
                    break;
                case 6:
                    break;
                case 7:
                    ImsServiceSub.this.handleCalls((AsyncResult) msg.obj);
                    break;
                case 8:
                    ImsServiceSub.this.handleSuppSvc((AsyncResult) msg.obj);
                    break;
                case 9:
                    if (((AsyncResult) msg.obj).exception != null) {
                        ImsServiceSub.this.logi("Request turn on/off IMS failed");
                    }
                    break;
                case 10:
                    ImsServiceSub.this.handleTtyModeChange((AsyncResult) msg.obj);
                    break;
                case 11:
                    ImsServiceSub.this.handleRefreshConfInfo((AsyncResult) msg.obj);
                    break;
                case 12:
                    ImsServiceSub.this.handleHandover((AsyncResult) msg.obj);
                    break;
                case 13:
                    AsyncResult ar3 = (AsyncResult) msg.obj;
                    if (ar3 != null && ar3.result != null && ar3.exception == null) {
                        ImsServiceSub.this.handleModifyCallRequest((CallModify) ar3.result);
                    } else {
                        ImsServiceSub.this.loge("Error EVENT_MODIFY_CALL AsyncResult ar= " + ar3);
                    }
                    break;
                case 14:
                    ImsServiceSub.this.handleMwiNotification(msg);
                    break;
                case 15:
                    ImsServiceSub.this.handleConferenceResponse((AsyncResult) msg.obj);
                    break;
                case 16:
                    AsyncResult ar4 = (AsyncResult) msg.obj;
                    ImsServiceSub imsServiceSub = ImsServiceSub.this;
                    imsServiceSub.onSetCallForwardTimerDone(imsServiceSub.getImsInterfaceListener(ar4), ar4);
                    break;
                case 17:
                    AsyncResult ar5 = (AsyncResult) msg.obj;
                    ImsServiceSub imsServiceSub2 = ImsServiceSub.this;
                    imsServiceSub2.onGetCallForwardTimerDone(imsServiceSub2.getImsInterfaceListener(ar5), ar5);
                    break;
                case 18:
                case 19:
                case 20:
                case 24:
                default:
                    ImsServiceSub.this.logi("Unknown message = " + msg.what);
                    break;
                case 21:
                    ImsServiceSub.this.handleGeolocationRequest((AsyncResult) msg.obj);
                    break;
                case 22:
                    ImsServiceSub.this.handleGeolocationResponse((AsyncResult) msg.obj);
                    break;
                case 23:
                    ImsServiceSub.this.handleVoWiFiCallQuality((AsyncResult) msg.obj);
                    break;
                case 25:
                    ImsServiceSub.this.handleVops((AsyncResult) msg.obj);
                    break;
                case 26:
                    ImsServiceSub.this.handleSsac((AsyncResult) msg.obj);
                    break;
                case 27:
                    AsyncResult ar6 = (AsyncResult) msg.obj;
                    ImsServiceSub imsServiceSub3 = ImsServiceSub.this;
                    imsServiceSub3.handleVopsResponse(imsServiceSub3.getImsInterfaceListener(ar6), ar6);
                    break;
                case 28:
                    AsyncResult ar7 = (AsyncResult) msg.obj;
                    ImsServiceSub imsServiceSub4 = ImsServiceSub.this;
                    imsServiceSub4.handleSsacResponse(imsServiceSub4.getImsInterfaceListener(ar7), ar7);
                    break;
                case 29:
                    ImsServiceSub.this.handleParticipantStatusInfo((AsyncResult) msg.obj);
                    break;
                case 30:
                    ImsServiceSub.this.handleUpdateVoltePrefResponse((AsyncResult) msg.obj);
                    break;
                case 31:
                    ImsServiceSub.this.handleQueryVoltePrefResponse((AsyncResult) msg.obj);
                    break;
                case 32:
                    AsyncResult ar8 = (AsyncResult) msg.obj;
                    ImsServiceSub imsServiceSub5 = ImsServiceSub.this;
                    imsServiceSub5.onGetHandoverConfigDone(imsServiceSub5.getImsInterfaceListener(ar8), ar8);
                    break;
                case 33:
                    AsyncResult ar9 = (AsyncResult) msg.obj;
                    ImsServiceSub imsServiceSub6 = ImsServiceSub.this;
                    imsServiceSub6.onSetHandoverConfigDone(imsServiceSub6.getImsInterfaceListener(ar9), ar9);
                    break;
                case 34:
                    ImsServiceSub.this.handleRegistrationBlockStatus((AsyncResult) msg.obj);
                    break;
                case 35:
                    ImsServiceSub.this.handleCancelModifyCallResponse((AsyncResult) msg.obj);
                    break;
                case 36:
                    ImsServiceSub.this.handleCallAutoReject((AsyncResult) msg.obj);
                    break;
                case 37:
                    ImsServiceSub.this.handleWfcRoamingConfiguration((AsyncResult) msg.obj);
                    break;
                case 38:
                    ImsServiceSub.this.handleUssdReceived((AsyncResult) msg.obj);
                    break;
                case 39:
                    AsyncResult ar10 = (AsyncResult) msg.obj;
                    if (ar10.exception != null) {
                        ImsServiceSub.this.loge("IMS Service auto reject update failed!" + ar10.exception);
                    }
                    break;
                case 40:
                    AsyncResult ar11 = (AsyncResult) msg.obj;
                    if (ar11 == null) {
                        ImsServiceSub.this.loge("Send UI tty mode: Result is null");
                    } else {
                        try {
                            Message result = (Message) ar11.userObj;
                            result.replyTo.send(result);
                        } catch (Exception e) {
                            ImsServiceSub.this.loge("Failed to send result " + e);
                            return;
                        }
                    }
                    break;
                case 41:
                    ImsServiceSub.this.handleCallComposer((AsyncResult) msg.obj);
                    break;
                case 42:
                    handleGeoLocationDataStatus((AsyncResult) msg.obj);
                    break;
            }
        }
    }

    private static int getOperationStatus(boolean status) {
        return status ? 0 : 1;
    }

    private int getUtErrorCode(AsyncResult ar) {
        ImsReasonInfo err = this.mImsUtImpl.getImsReasonInfoFromResponseError(ar);
        if (err == null) {
            return 0;
        }
        return err.getCode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetCallForwardTimerDone(IQtiImsExtListener listener, AsyncResult ar) {
        if (listener == null || ar == null) {
            loge("onSetCallForwardTimerDone: listener : " + listener + " AsyncResult: " + ar);
            return;
        }
        if (ar.exception != null) {
            loge("set CF Timer error!");
            CallForwardStatusInfo cfStatusInfo = (CallForwardStatusInfo) ar.result;
            ImsReasonInfo sipError = cfStatusInfo != null ? cfStatusInfo.getSipErrorInfo() : null;
            String failCause = sipError != null ? sipError.getExtraMessage() : null;
            loge("onSetCallForwardTimerDone Failure cause: " + failCause);
            if (failCause == null || failCause.isEmpty()) {
                try {
                    listener.onUTReqFailed(this.mPhoneId, getUtErrorCode(ar), (String) null);
                    return;
                } catch (Throwable t) {
                    loge("onUTReqFailed exception!" + t);
                    return;
                }
            }
            try {
                listener.onUTReqFailed(this.mPhoneId, getUtErrorCode(ar), failCause);
                return;
            } catch (Throwable t2) {
                loge("onUTReqFailed exception!" + t2);
                return;
            }
        }
        logi("set CF Timer success!");
        try {
            listener.onSetCallForwardUncondTimer(this.mPhoneId, 0);
        } catch (Throwable t3) {
            loge("onSetCallForwardTimerDone exception!" + t3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:26:0x00b8  */
    public void onGetCallForwardTimerDone(IQtiImsExtListener listener, AsyncResult ar) {
        int i;
        int endHour;
        int startMinute;
        int endMinute;
        int reason;
        int status;
        String number;
        int serviceClass;
        int startHour = 0;
        int endHour2 = 0;
        int startMinute2 = 0;
        int endMinute2 = 0;
        int reason2 = 0;
        int status2 = 0;
        String number2 = null;
        int serviceClass2 = 0;
        if (ar.exception != null) {
            loge("get CF Timer error!");
            try {
                listener.onUTReqFailed(this.mPhoneId, getUtErrorCode(ar), (String) null);
                return;
            } catch (Throwable t) {
                loge("onUTReqFailed exception!" + t);
                return;
            }
        }
        if (ar.result != null) {
            if (ar.result instanceof ImsCallForwardTimerInfo[]) {
                logi("onGetCallForwardTimerDone ImsCallForwardTimerInfo instance! ");
                ImsCallForwardTimerInfo[] cfTimerInfo = (ImsCallForwardTimerInfo[]) ar.result;
                for (int i2 = 0; i2 < cfTimerInfo.length; i2++) {
                    startHour = cfTimerInfo[i2].startHour;
                    endHour2 = cfTimerInfo[i2].endHour;
                    startMinute2 = cfTimerInfo[i2].startMinute;
                    endMinute2 = cfTimerInfo[i2].endMinute;
                    reason2 = cfTimerInfo[i2].reason;
                    status2 = cfTimerInfo[i2].status;
                    number2 = cfTimerInfo[i2].number;
                    serviceClass2 = cfTimerInfo[i2].serviceClass;
                }
                i = startHour;
                endHour = endHour2;
                startMinute = startMinute2;
                endMinute = endMinute2;
                reason = reason2;
                status = status2;
                number = number2;
                serviceClass = serviceClass2;
            } else if (ar.result instanceof CallForwardInfo[]) {
                logi("onGetCallForwardTimerDone CallForwardInfo instance! ");
                CallForwardInfo[] cfInfo = (CallForwardInfo[]) ar.result;
                for (int i3 = 0; i3 < cfInfo.length; i3++) {
                    reason2 = cfInfo[i3].reason;
                    status2 = cfInfo[i3].status;
                    number2 = cfInfo[i3].number;
                    serviceClass2 = cfInfo[i3].serviceClass;
                }
                i = 0;
                endHour = 0;
                startMinute = 0;
                endMinute = 0;
                reason = reason2;
                status = status2;
                number = number2;
                serviceClass = serviceClass2;
            } else {
                i = 0;
                endHour = 0;
                startMinute = 0;
                endMinute = 0;
                reason = 0;
                status = 0;
                number = null;
                serviceClass = 0;
            }
        } else {
            i = 0;
            endHour = 0;
            startMinute = 0;
            endMinute = 0;
            reason = 0;
            status = 0;
            number = null;
            serviceClass = 0;
        }
        if (listener != null) {
            if (reason != 0) {
                try {
                    listener.onUTReqFailed(this.mPhoneId, getUtErrorCode(ar), (String) null);
                    return;
                } catch (Throwable t2) {
                    loge("onUTReqFailed exception!" + t2);
                    return;
                }
            }
            try {
                listener.onGetCallForwardUncondTimer(this.mPhoneId, i, endHour, startMinute, endMinute, reason, status, number, serviceClass);
            } catch (Throwable th) {
                loge("onGetCallForwardTimerDone exception!");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCancelModifyCallResponse(AsyncResult ar) {
        IQtiImsExtListener listener = getImsInterfaceListener(ar);
        int nStatus = 0;
        if (ar != null && ar.exception != null) {
            nStatus = 1;
            Toast.makeText(this.mContext, "Cancel upgrade failed", 0).show();
        }
        logi("handleCancelModifyCallResponse: Result " + nStatus);
        if (listener != null) {
            try {
                listener.receiveCancelModifyCallResponse(this.mPhoneId, nStatus);
                return;
            } catch (Throwable th) {
                loge("handleCancelModifyCallResponse exception!");
                return;
            }
        }
        logi("handleCancelModifyCallResponse: no listener is available");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGeolocationResponse(AsyncResult ar) {
        if (ar != null && ar.exception != null) {
            logi("handleGeolocationResponse :: Error response!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGeolocationRequest(AsyncResult ar) {
        GeoLocationInfo geoInfo = (GeoLocationInfo) ar.result;
        if (geoInfo == null) {
            loge("handleGeolocationRequest :: Null AsyncResult!");
            return;
        }
        try {
            sTaskExecutor.execute(new GeocoderTask(geoInfo.getLat(), geoInfo.getLon()));
        } catch (RejectedExecutionException rex) {
            loge("handleGeolocationRequest :: Failure to start GeocoderTask thread for [lat=" + geoInfo.getLat() + " | long=" + geoInfo.getLon() + "]!\n");
            rex.printStackTrace();
        }
    }

    private class GeocoderTask implements Runnable {
        final int MAX_RESULTS = 1;
        private double latitude;
        private double longitude;

        public GeocoderTask(double lat, double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }

        public String toString() {
            return "GeocoderTask [lat=" + this.latitude + " | long=" + this.longitude + "]";
        }

        @Override // java.lang.Runnable
        public void run() {
            ImsServiceSub.this.logi("Executing " + this);
            Address address = null;
            List<Address> addresses = null;
            if (!Geocoder.isPresent()) {
                ImsServiceSub.this.loge("handleGeolocationRequest :: Geocoder not present!");
            } else {
                Geocoder gc = new Geocoder(ImsServiceSub.this.mContext);
                try {
                    addresses = gc.getFromLocation(this.latitude, this.longitude, 1);
                } catch (IOException e) {
                    ImsServiceSub.this.loge(this + " :: Error getting Addresses from GeoCoder!");
                } catch (IllegalArgumentException e2) {
                    ImsServiceSub.this.loge(this + " :: Invalid latitude or longitude value!");
                }
                if (addresses == null || addresses.isEmpty()) {
                    ImsServiceSub.this.loge(this + " :: Error getting Addresses from GeoCoder!");
                } else {
                    address = addresses.get(0);
                }
            }
            ImsServiceSub.this.mCi.sendGeolocationInfo(this.latitude, this.longitude, address, ImsServiceSub.this.mHandler.obtainMessage(22));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IQtiImsExtListener getImsInterfaceListener(AsyncResult ar) {
        if (ar != null && (ar.userObj instanceof IQtiImsExtListener)) {
            return (IQtiImsExtListener) ar.userObj;
        }
        logi("getImsInterfaceListener returns null");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMwiNotification(Message msg) {
        if (msg != null && msg.obj != null) {
            AsyncResult arMwiUpdate = (AsyncResult) msg.obj;
            if (arMwiUpdate.exception == null) {
                if (arMwiUpdate.result != null) {
                    this.mMwiResponse = (Mwi) arMwiUpdate.result;
                    updateVoiceMail();
                    return;
                } else {
                    loge("handleMwiNotification arMwiUpdate.result null");
                    return;
                }
            }
            loge("handleMwiNotification arMwiUpdate exception");
            return;
        }
        loge("handleMwiNotification msg null");
    }

    private void updateVoiceMail() {
        int count = 0;
        for (Mwi.MwiMessageSummary msgSummary : this.mMwiResponse.mwiMsgSummary) {
            if (msgSummary.mMessageType == 0) {
                count = msgSummary.mNewMessage + 0 + msgSummary.mNewUrgent;
                break;
            }
        }
        logi("updateVoiceMail count = " + count);
        notifyVoiceMessageCountUpdate(count);
    }

    public void registerForParticipantStatusInfo(IQtiImsExtListener listener) {
        this.mQtiImsParticipantStatusListeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVoWiFiCallQuality(AsyncResult ar) {
        if (ar != null && ar.exception == null && ar.result != null) {
            int[] voWifiCallQuality = (int[]) ar.result;
            if (voWifiCallQuality[0] == 0) {
                loge("handleVoWiFiCallQuality received VoWiFIQuality : " + voWifiCallQuality[0]);
                return;
            }
            ImsServiceClassTracker imsServiceClassTracker = this.mTracker;
            if (imsServiceClassTracker != null) {
                imsServiceClassTracker.updateVoWiFiCallQuality(voWifiCallQuality[0]);
                return;
            } else {
                loge("Wifi Quality Error -- tracker is null");
                return;
            }
        }
        loge("handleVoWiFiCallQuality response is not valid");
    }

    public void registerForCapabilitiesChanged(Handler h, int what, Object obj) {
        this.mCapabilitiesChangedRegistrants.addUnique(h, what, obj);
    }

    public void unregisterForCapabilitiesChanged(Handler h) {
        this.mCapabilitiesChangedRegistrants.remove(h);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVops(AsyncResult ar) {
        if (ar != null && ar.exception == null && ar.result != null) {
            this.mIsVopsEnabled = ((Boolean) ar.result).booleanValue();
            broadcastVopsSsacIntent();
        } else {
            loge("handleVops exception");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSsac(AsyncResult ar) {
        if (ar != null && ar.exception == null && ar.result != null) {
            SsacInfo ssacInd = (SsacInfo) ar.result;
            logi("handleSsac voice = " + ssacInd.getBarringFactorVoice());
            this.mIsSsacVoiceBarred = ssacInd.getBarringFactorVoice() == 0;
            broadcastVopsSsacIntent();
            return;
        }
        loge("handleSsac exception");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWfcRoamingConfiguration(AsyncResult ar) {
        if (ar != null && ar.exception == null && ar.result != null) {
            this.mModemSupportForWfcRoamingConfiguration = ((Boolean) ar.result).booleanValue();
        } else {
            loge("handleWfcRoamingConfiguration exception");
        }
        if (this.mModemSupportForWfcRoamingConfiguration) {
            this.mImsConfigImpl.updateWfcModeConfigurationsToModem();
        }
    }

    public boolean IsWfcRoamingConfigurationSupportedByModem() {
        return this.mModemSupportForWfcRoamingConfiguration;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUssdReceived(AsyncResult ar) {
        if (ar == null || ar.result == null) {
            loge("handleUssdReceived invalid info");
            return;
        }
        UssdInfo info = (UssdInfo) ar.result;
        if (this.mUssdInfoListener != null && ar.exception != null) {
            try {
                this.mUssdInfoListener.onUssdFailed(this.mPhoneId, info.getType(), info.getErrorCode(), info.getErrorMessage());
            } catch (Exception e) {
                Log.e(this, "handleUssd exception!");
            }
        }
        this.mTracker.onUssdMessageReceived(info);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCallComposer(AsyncResult ar) {
        if (ar != null && ar.exception == null && ar.result != null) {
            SomeArgs args = (SomeArgs) ar.result;
            if (this.mTracker != null) {
                Bundle b = CallComposerInfoUtils.toBundle((CallComposerInfo) args.arg1);
                this.mTracker.onCallComposerDataAvailable(args.argi1, b);
            }
            args.recycle();
            return;
        }
        loge("handleCallComposer exception");
    }

    private void broadcastVopsSsacIntent() {
        Intent intent = new Intent("org.codeaurora.VOIP_VOPS_SSAC_STATUS");
        intent.putExtra("Vops", this.mIsVopsEnabled);
        intent.putExtra("Ssac", this.mIsSsacVoiceBarred);
        intent.putExtra("phoneId", this.mPhoneId);
        logi("broadcastVopsSsacIntent Vops = " + this.mIsVopsEnabled + " , Ssac = " + this.mIsSsacVoiceBarred + " , PhoneId = " + this.mPhoneId);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public void queryVopsStatus(IQtiImsExtListener listener) {
        logi("queryVopsStatus");
        this.mCi.queryVopsStatus(this.mHandler.obtainMessage(27, listener));
    }

    public void querySsacStatus(IQtiImsExtListener listener) {
        logi("querySsacStatus");
        this.mCi.querySsacStatus(this.mHandler.obtainMessage(28, listener));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVopsResponse(IQtiImsExtListener listener, AsyncResult ar) {
        if (ar != null && ar.exception == null && ar.result != null) {
            VopsInfo vops = (VopsInfo) ar.result;
            this.mIsVopsEnabled = vops.isVopsEnabled();
            logi("Vops Response = " + this.mIsVopsEnabled);
            if (listener != null) {
                try {
                    listener.notifyVopsStatus(this.mPhoneId, this.mIsVopsEnabled);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            return;
        }
        loge("handleVopsResponse exception");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSsacResponse(IQtiImsExtListener listener, AsyncResult ar) {
        if (ar != null && ar.exception == null && ar.result != null) {
            SsacInfo ssacInd = (SsacInfo) ar.result;
            logi("handleSsacResponse voice = " + ssacInd.getBarringFactorVoice());
            boolean z = ssacInd.getBarringFactorVoice() == 0;
            this.mIsSsacVoiceBarred = z;
            if (listener != null) {
                try {
                    listener.notifySsacStatus(this.mPhoneId, z);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            return;
        }
        loge("handleSsacResponse exception");
    }

    public void handleParticipantStatusInfo(AsyncResult ar) {
        ImsServiceClassTracker imsServiceClassTracker;
        if (ar != null && ar.exception == null && ar.result != null) {
            ParticipantStatusDetails participantInfo = (ParticipantStatusDetails) ar.result;
            if (this.mQtiImsParticipantStatusListeners.size() == 0 || (imsServiceClassTracker = this.mTracker) == null || imsServiceClassTracker.getCallSession(Integer.toString(participantInfo.getCallId())) == null) {
                logi("participant listeners size= " + this.mQtiImsParticipantStatusListeners.size() + " isTrackerPresent = " + (this.mTracker != null));
                return;
            }
            logi("handleParticipantStatusInfo operation = " + participantInfo.getOperation() + " status = " + participantInfo.getSipStatus() + " participant = " + participantInfo.getParticipantUri() + " ect = " + participantInfo.getIsEct());
            for (IQtiImsExtListener listener : this.mQtiImsParticipantStatusListeners) {
                try {
                    listener.notifyParticipantStatusInfo(this.mPhoneId, participantInfo.getOperation(), participantInfo.getSipStatus(), participantInfo.getParticipantUri(), participantInfo.getIsEct());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        loge("ParticipantStatusInfo exception");
    }

    public void handleRegistrationBlockStatus(AsyncResult ar) {
        if (ar != null && ar.exception == null && ar.result != null) {
            RegistrationBlockStatusInfo regBlockStatus = (RegistrationBlockStatusInfo) ar.result;
            if (regBlockStatus.getStatusOnWlan() != null) {
                BlockStatusInfo blockStatus = regBlockStatus.getStatusOnWlan();
                if (isIKEv2Error(blockStatus)) {
                    Runnable r = new Runnable() { // from class: org.codeaurora.ims.ImsServiceSub.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ImsServiceSub.this.logi("Permanent IWLAN reg failure (IKEv2 auth failure).");
                            ImsReasonInfo imsReasonInfo = new ImsReasonInfo(1400, 1408, null);
                            ImsServiceSub.this.mImsReg.registrationChangeFailed(18, imsReasonInfo);
                        }
                    };
                    Handler handler = this.mFeatureCallbackHandler;
                    if (handler != null) {
                        handler.post(r);
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        loge("Async result is null or exception is not null.");
    }

    public void updateVoltePreference(int preference, IQtiImsExtListener listener) {
        this.mCi.updateVoltePref(preference, this.mHandler.obtainMessage(30, listener));
    }

    public void queryVoltePreference(IQtiImsExtListener listener) {
        this.mCi.queryVoltePref(this.mHandler.obtainMessage(31, listener));
    }

    private int getQtiImsExtOperationStatus(AsyncResult ar) {
        if (ar == null || ar.exception == null) {
            return 0;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateVoltePrefResponse(AsyncResult ar) {
        IQtiImsExtListener listener = getImsInterfaceListener(ar);
        int result = getQtiImsExtOperationStatus(ar);
        logi("handleUpdateVoltePrefResponse: result " + result);
        if (listener != null) {
            try {
                listener.onVoltePreferenceUpdated(this.mPhoneId, result);
                return;
            } catch (Throwable th) {
                loge("handleUpdateVoltePrefResponse exception!");
                return;
            }
        }
        loge("handleUpdateVoltePrefResponse: no listener is available");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleQueryVoltePrefResponse(AsyncResult ar) {
        IQtiImsExtListener listener = getImsInterfaceListener(ar);
        int result = getQtiImsExtOperationStatus(ar);
        int preference = 2;
        if (result == 0) {
            int[] pref = (int[]) ar.result;
            preference = pref[0];
        }
        logi("handleQueryVoltePrefResponse: result-" + result + " mode-" + preference);
        if (listener != null) {
            try {
                listener.onVoltePreferenceQueried(this.mPhoneId, result, preference);
                return;
            } catch (Throwable th) {
                loge("handleQueryVoltePrefResponse exception!");
                return;
            }
        }
        loge("handleQueryVoltePrefResponse: no listener is available");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGetHandoverConfigDone(IQtiImsExtListener listener, AsyncResult ar) {
        int result;
        if (listener != null) {
            try {
                if (ar.result == null) {
                    result = 0;
                } else {
                    result = ((Integer) ar.result).intValue();
                }
                listener.onGetHandoverConfig(this.mPhoneId, getOperationStatus(ar.exception == null), result);
                return;
            } catch (Throwable t) {
                loge("onGetHandoverConfigDone " + t);
                return;
            }
        }
        loge("onGetHandoverConfigDone listener is not valid !!!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetHandoverConfigDone(IQtiImsExtListener listener, AsyncResult ar) {
        if (listener != null) {
            try {
                listener.onSetHandoverConfig(this.mPhoneId, getOperationStatus(ar.exception == null));
                return;
            } catch (Throwable t) {
                loge("onSetHandoverConfigDone " + t);
                return;
            }
        }
        loge("onSetHandoverConfigDone listener is not valid !!!");
    }

    private static boolean isIKEv2Error(BlockStatusInfo blockStatus) {
        if (blockStatus.getReasonDetails() == null) {
            return false;
        }
        BlockReasonDetailsInfo blockReasonDetails = blockStatus.getReasonDetails();
        return blockReasonDetails.getRegFailureReasonType() != -1 && blockReasonDetails.getRegFailureReason() != Integer.MAX_VALUE && blockReasonDetails.getRegFailureReasonType() == 9 && blockReasonDetails.getRegFailureReason() == 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCallAutoReject(AsyncResult ar) {
        if (ar == null || ar.result == null) {
            Log.e(this, "handleCallAutoReject :: Error parsing DriverCallIms");
            return;
        }
        DriverCallIms rejectedCall = (DriverCallIms) ar.result;
        if (rejectedCall.callDetails == null) {
            Log.e(this, "handleCallAutoReject :: No call details in DriverCallIms");
            return;
        }
        Bundle extras = new Bundle();
        if (rejectedCall.number != null) {
            extras.putString("oi", rejectedCall.number);
        }
        extras.putInt("phoneId", this.mPhoneId);
        CallComposerInfo ccInfo = rejectedCall.getCallComposerInfo();
        if (ccInfo != null) {
            Bundle ccBundle = CallComposerInfoUtils.toBundle(ccInfo);
            extras.putBundle(QtiCallConstants.EXTRA_CALL_COMPOSER_INFO, ccBundle);
        }
        QImsCallProfile rejCallProfile = new QImsCallProfile(1, rejectedCall.callDetails.call_type, extras, ImsMediaUtils.newImsStreamMediaProfile());
        VerstatInfo verstatInfo = rejectedCall.getVerstatInfo();
        QImsCallProfile rejCallProfile2 = ImsCallUtils.toImsCallProfile(verstatInfo, rejCallProfile);
        Log.i(this, "handleCallAutoReject :: rejCallProfile=" + rejCallProfile2 + " ImsReasonInfo=" + rejectedCall.callFailCause + " extras: " + extras);
        notifyRejectedCall(rejCallProfile2.newImsCallProfile(), rejectedCall.callFailCause);
    }

    public ImsMultiEndpointImplBase getMultiEndpoint() {
        return this.mImsMultiEndpointImpl;
    }

    public ImsMultiIdentityImpl getMultiIdentityImpl() {
        synchronized (this.mMultiIdentityLock) {
            if (this.mMultiIdentityImpl == null) {
                this.mMultiIdentityImpl = new ImsMultiIdentityImpl(this.mPhoneId, this.mCi, this.mContext, this.mHandler.getLooper());
            }
        }
        return this.mMultiIdentityImpl;
    }

    public ImsScreenShareControllerImpl getScreenShareController() {
        synchronized (this.mScreenShareLock) {
            if (this.mScreenShareController == null) {
                this.mScreenShareController = new ImsScreenShareControllerImpl(this, this.mContext);
            }
        }
        return this.mScreenShareController;
    }

    public CrsCrbtControllerImpl getCrsCrbtController() {
        synchronized (this.mCrsCrbtLock) {
            if (this.mCrsCrbtController == null && this.mCi.isCrsSupported()) {
                this.mCrsCrbtController = new CrsCrbtControllerImpl(this, this.mContext);
            }
        }
        return this.mCrsCrbtController;
    }

    public int getImsFeatureState() {
        return getFeatureState();
    }

    private List<ImsCallSessionImpl> getIncomingOrWaitingCallSession() {
        List<ImsCallSessionImpl> session = getCallSessionByState(DriverCallIms.State.INCOMING);
        if (session.isEmpty()) {
            return getCallSessionByState(DriverCallIms.State.WAITING);
        }
        return session;
    }

    public void setAnswerExtras(Bundle extras) {
        List<ImsCallSessionImpl> sessionList = getIncomingOrWaitingCallSession();
        if (sessionList.isEmpty()) {
            logd("setAnswerExtras: no incoming/waiting call available");
        } else {
            sessionList.get(0).setAnswerExtras(extras);
        }
    }

    public void onNewCall() {
        ImsVideoGlobals.getInstance().setPhoneIdWithActiveCall(this.mPhoneId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String s) {
        Log.i(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }

    private void logv(String s) {
        Log.v(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String s) {
        Log.d(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String s) {
        Log.e(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }
}
