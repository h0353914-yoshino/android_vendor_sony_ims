package org.codeaurora.ims;

import android.content.Context;
import android.content.res.Resources;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import android.telephony.ims.stub.ImsCallSessionImplBase;
import android.view.Surface;
import android.widget.Toast;
import com.qualcomm.ims.utils.Config;
import com.qualcomm.ims.utils.Log;
import com.qualcomm.ims.vt.ImsVideoCallProviderImpl;
import com.qualcomm.ims.vt.LowBatteryHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.codeaurora.ims.internal.ICrsCrbtListener;
import org.codeaurora.ims.internal.IImsScreenShareListener;
import org.codeaurora.ims.utils.CallComposerInfoUtils;
import org.codeaurora.ims.utils.QtiImsExtUtils;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsCallSessionImpl extends QImsSessionBase {
    private static final int ALERT_HANDOVER = 1;
    private static final int ALERT_TIMES_AFTER_DROP_CALL = 3;
    private static final int DEFAULT_CALL_INDEX = -1;
    private static final int EVENT_ACCEPT = 2;
    private static final int EVENT_ADD_PARTICIPANT = 8;
    private static final int EVENT_ADD_PARTICIPANTS = 17;
    private static final int EVENT_CLOSE_SESSION = 11;
    private static final int EVENT_CONFERENCE = 6;
    private static final int EVENT_DEFLECT = 16;
    private static final int EVENT_DIAL = 1;
    private static final int EVENT_HANGUP = 3;
    private static final int EVENT_HOLD = 4;
    private static final int EVENT_REJECT = 7;
    private static final int EVENT_REMOVE_PARTICIPANT = 10;
    private static final int EVENT_RESUME = 5;
    private static final int EVENT_RINGBACK_TONE = 9;
    private static final int EVENT_RTT_MESSAGE_RECEIVED = 12;
    private static final int EVENT_RTT_UPGRADE_CONFIRM_DONE = 15;
    private static final int EVENT_SEND_DTMF = 19;
    private static final int EVENT_SEND_RTT_MESSAGE = 13;
    private static final int EVENT_SEND_RTT_MODIFY_REQUEST = 14;
    private static final int EVENT_SEND_SIP_DTMF = 21;
    private static final int EVENT_SIP_DTMF_RECEIVED = 22;
    private static final int EVENT_TRANSFER = 20;
    private static final int EVENT_VOICE_INFO_CHANGED = 18;
    public static final String PROPERTY_DBG_ENCRYPTION_OVERRIDE = "persist.dbg.call_encrypt_ovr";
    public static final int PROPERTY_DBG_ENCRYPTION_OVERRIDE_DEFAULT = 0;
    private static final int RESET_ALERT_HANDOVER_TIME = 2;
    private static final long RESET_TIME = 1800000;
    public static final int SIP_FORBIDDEN = 403;
    public static final int SIP_REQUEST_TIMEOUT = 408;
    public static final int SIP_SERVER_BAD_GATEWAY = 502;
    public static final int SIP_SERVER_INTERNAL_ERROR = 500;
    public static final int SIP_SERVER_MESSAGE_TOOLARGE = 513;
    public static final int SIP_SERVER_NOT_IMPLEMENTED = 501;
    public static final int SIP_SERVER_PRECONDITION_FAILURE = 580;
    public static final int SIP_SERVER_VERSION_UNSUPPORTED = 505;
    public static final int SIP_SERVICE_UNAVAILABLE = 503;
    public static final int SIP_TEMPORARILY_UNAVAILABLE = 480;
    public static final int SUPP_NOTIFICATION_TYPE_MO = 0;
    public static final int SUPP_NOTIFICATION_TYPE_MT = 1;
    public static final int SUPP_SVC_CODE_INVALID = -1;
    public static final int SUPP_SVC_CODE_MT_HOLD = 2;
    public static final int SUPP_SVC_CODE_MT_RESUME = 3;
    private static final int VOLUME_FOR_ALERT = 40;
    private int mAnswerOptionTirConfig;
    private int mCallId;
    private QImsCallProfile mCallProfile;
    private String mCallee;
    private org.codeaurora.ims.parser.ConfInfo mConfInfo;
    private List<ICrsCrbtListener> mCrsCrbtListeners;
    private DriverCallIms mDc;
    private int mDisconnCause;
    private Handler mHandler;
    private ImsCallModification mImsCallModification;
    private ImsConferenceState mImsConferenceState;
    ImsVideoCallProviderImpl mImsVideoCallProviderImpl;
    private boolean mInCall;
    private boolean mIsCallTerminatedDueToLowBattery;
    private boolean mIsConfInProgress;
    boolean mIsConferenceHostSession;
    private boolean mIsLowBattery;
    public boolean mIsMergeHostSession;
    private boolean mIsVideoAllowed;
    private boolean mIsVoiceAllowed;
    private QImsCallProfile mLocalCallProfile;
    private int mMergeStatus;
    private int mMtSuppSvcCode;
    private ArrayList<String> mPendingAddParticipantsList;
    private QImsCallProfile mRemoteCallProfile;
    private boolean mRingbackToneRequest;
    private IImsScreenShareListener mScreenSharelistener;
    private String mSipDtmfInfo;
    private boolean mStateChangeReportingAllowed;
    private int mVoWifiQuality;
    private Handler mWifiAlertHandler;
    private ImsCallSessionImpl newSession;

    public ImsCallSessionImpl(Context context, ImsSenderRxr senderRxr, int phoneId, ImsServiceClassTracker tracker, boolean isVideoCapable, ImsCallSessionCallbackHandler handler) {
        super(context, senderRxr, phoneId, tracker, handler);
        this.mDc = null;
        this.mCallId = -1;
        boolean z = true;
        this.mLocalCallProfile = new QImsCallProfile(1, 7);
        this.mRemoteCallProfile = new QImsCallProfile(1, 7);
        this.mCallProfile = new QImsCallProfile();
        this.mHandler = new ImsCallSessionImplHandler();
        this.mCallee = null;
        this.mIsConfInProgress = false;
        this.newSession = null;
        this.mDisconnCause = 0;
        this.mMtSuppSvcCode = -1;
        this.mConfInfo = null;
        this.mImsConferenceState = null;
        this.mRingbackToneRequest = false;
        this.mCrsCrbtListeners = new CopyOnWriteArrayList();
        this.mIsVideoAllowed = false;
        this.mIsVoiceAllowed = false;
        this.mStateChangeReportingAllowed = true;
        this.mIsMergeHostSession = false;
        this.mIsConferenceHostSession = false;
        this.mIsCallTerminatedDueToLowBattery = false;
        this.mIsLowBattery = false;
        this.mVoWifiQuality = 1;
        this.mPendingAddParticipantsList = new ArrayList<>(0);
        this.mAnswerOptionTirConfig = 2;
        this.mMergeStatus = 0;
        this.mSipDtmfInfo = null;
        if (!isVideoCapable && !isConfigEnabled(R.bool.config_ovr_create_video_call_provider)) {
            z = false;
        }
        boolean shallCreateVideoProvider = z;
        maybeCreateVideoProvider(shallCreateVideoProvider);
        this.mIsVideoAllowed = isVideoCapable;
        this.mCi.registerForRttMessage(this.mHandler, 12, null);
        this.mCi.registerForVoiceInfo(this.mHandler, 18, null);
        this.mCi.registerForSipDtmfInfo(this.mHandler, 22, null);
    }

    private void maybeCreateVideoProvider(boolean isVideoCallingEnabled) {
        if ((QtiImsExtUtils.isRttSupported(this.mPhoneId, this.mContext) || isVideoCallingEnabled) && this.mImsCallModification == null) {
            this.mImsCallModification = new ImsCallModification(this, this.mContext, this.mCi);
        }
        if (isVideoCallingEnabled && this.mImsVideoCallProviderImpl == null) {
            Log.i(this, "maybeCreateVideoProvider: Creating VideoCallProvider");
            ImsVideoCallProviderImpl imsVideoCallProviderImpl = new ImsVideoCallProviderImpl(this, this.mImsCallModification);
            this.mImsVideoCallProviderImpl = imsVideoCallProviderImpl;
            addListener(imsVideoCallProviderImpl);
        }
    }

    public ImsCallSessionImpl(ImsCallProfile profile, ImsSenderRxr senderRxr, Context context, ImsServiceClassTracker tracker, boolean isVideoCapable, int phoneId) {
        this(context, senderRxr, phoneId, tracker, isVideoCapable, new ImsCallSessionCallbackHandler());
        this.mCallProfile.updateProfile(profile);
        this.mConfInfo = new org.codeaurora.ims.parser.ConfInfo();
        this.mCi.registerForRingbackTone(this.mHandler, 9, null);
    }

    public ImsCallSessionImpl(DriverCallIms call, ImsSenderRxr senderRxr, Context context, ImsServiceClassTracker tracker, boolean isVideoCapable, int phoneId) {
        this(context, senderRxr, phoneId, tracker, isVideoCapable, new ImsCallSessionCallbackHandler());
        DriverCallIms driverCallIms = new DriverCallIms(call);
        this.mDc = driverCallIms;
        this.mCallId = driverCallIms.index;
        this.mCallee = call.number;
        updateImsCallProfile(this.mDc);
        updateCrsStatus(this.mDc);
        setCapabilitiesInProfiles(this.mDc);
        this.mConfInfo = new org.codeaurora.ims.parser.ConfInfo();
    }

    public ImsVideoCallProviderImpl getImsVideoCallProviderImpl() {
        if (isSessionValid()) {
            return this.mImsVideoCallProviderImpl;
        }
        return null;
    }

    public void onReceivedModifyCall(CallModify callModify) {
        ImsCallModification imsCallModification = this.mImsCallModification;
        if (imsCallModification == null) {
            Log.e(this, "onReceivedModifyCall: Ignoring session modification request.");
        } else {
            imsCallModification.onReceivedModifyCall(callModify);
        }
    }

    ImsCallModification getImsCallModification() {
        if (isSessionValid()) {
            return this.mImsCallModification;
        }
        return null;
    }

    static boolean isServiceAllowed(int srvType, ServiceStatus[] ability) {
        if (ability == null) {
            return false;
        }
        for (ServiceStatus srv : ability) {
            if (srv != null && srv.type == srvType) {
                if (srv.status != 1 && srv.status != 2) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private int getRestrictCause(int srvType, ServiceStatus[] ability) {
        if (ability == null) {
            return 0;
        }
        for (ServiceStatus srv : ability) {
            if (srv != null && srv.type == srvType && srv.accessTechStatus != null && srv.accessTechStatus.length > 0) {
                int cause = srv.accessTechStatus[0].restrictCause;
                return cause;
            }
        }
        return 0;
    }

    private boolean updateLocalDc(DriverCallIms dcUpdate) {
        boolean hasChanged;
        DriverCallIms driverCallIms = this.mDc;
        if (driverCallIms == null) {
            this.mDc = new DriverCallIms(dcUpdate);
            hasChanged = true;
        } else {
            int changedProperties = driverCallIms.update(dcUpdate);
            if (this.mStateChangeReportingAllowed && (changedProperties & 16) != 0) {
                if (this.mCallbackHandler != null) {
                    this.mCallbackHandler.callSessionMultipartyStateChanged(this.mDc.isMpty);
                }
                if (this.mDc.isMpty) {
                    updateMergeStatus(2);
                }
            }
            hasChanged = changedProperties != 0;
        }
        Log.i(this, "updateLocalDc is " + hasChanged);
        return hasChanged;
    }

    public void muteStateReporting() {
        Log.i(this, "Call session state reporting muted! session=" + this);
        this.mStateChangeReportingAllowed = false;
    }

    public void unMuteStateReporting() {
        Log.i(this, "Call session state reporting unmuted. session=" + this);
        this.mStateChangeReportingAllowed = true;
        if (isImsCallSessionAlive()) {
            this.mCallbackHandler.callSessionUpdated(this.mCallProfile.newImsCallProfile());
        }
    }

    public void updateMergeStatus(int status) {
        Log.i(this, "updateMergeStatus status: " + status);
        this.mMergeStatus = status;
        ImsVideoCallProviderImpl imsVideoCallProviderImpl = this.mImsVideoCallProviderImpl;
        if (imsVideoCallProviderImpl != null) {
            imsVideoCallProviderImpl.updateMergeStatus(status);
        }
    }

    public void setAnswerExtras(Bundle extras) {
        if (extras != null) {
            this.mAnswerOptionTirConfig = extras.getInt("tirConfig", this.mAnswerOptionTirConfig);
        }
    }

    public void setEmergencyServiceCategoryInProfile(DriverCallIms dcUpdate) {
        if (dcUpdate == null || dcUpdate.callDetails == null) {
            Log.e(this, "Driver call or call Details is null");
            return;
        }
        String emergencyServiceCategory = dcUpdate.callDetails.getValueForKeyFromExtras(dcUpdate.callDetails.extras, "EmergencyServiceCategory");
        if (emergencyServiceCategory != null) {
            Log.i(this, "Emergency service category:" + emergencyServiceCategory);
            this.mCallProfile.setCallExtra("EmergencyServiceCategory", emergencyServiceCategory);
        }
    }

    public void updateCall(DriverCallIms dcUpdate) {
        int errorCode;
        Log.i(this, "updateCall for " + dcUpdate);
        if (isSessionValid()) {
            updateImsCallProfile(dcUpdate);
            setCapabilitiesInProfiles(dcUpdate);
            maybeNotifyCallTypeChanging(dcUpdate);
            setEmergencyServiceCategoryInProfile(dcUpdate);
            ImsCallModification imsCallModification = this.mImsCallModification;
            if (imsCallModification != null) {
                imsCallModification.update(dcUpdate);
            }
            boolean z = false;
            if (dcUpdate.isMpty) {
                this.mCallProfile.setCallExtraBoolean("incomingConference", false);
            }
            switch (AnonymousClass3.$SwitchMap$org$codeaurora$ims$DriverCallIms$State[dcUpdate.state.ordinal()]) {
                case 1:
                    DriverCallIms driverCallIms = this.mDc;
                    if (driverCallIms == null) {
                        Log.e(this, "Phantom call!");
                        this.mState = 4;
                        this.mCallId = dcUpdate.index;
                        if (this.mStateChangeReportingAllowed) {
                            this.mCallbackHandler.callSessionInitiated(this.mCallProfile.newImsCallProfile());
                        }
                    } else if (driverCallIms.state == DriverCallIms.State.DIALING || this.mDc.state == DriverCallIms.State.ALERTING || this.mDc.state == DriverCallIms.State.INCOMING || this.mDc.state == DriverCallIms.State.WAITING) {
                        this.mState = 4;
                        this.mDc.state = DriverCallIms.State.ACTIVE;
                        extractCallDetailsIntoCallProfile(dcUpdate);
                        if (this.mStateChangeReportingAllowed) {
                            this.mCallbackHandler.callSessionInitiated(this.mCallProfile.newImsCallProfile());
                        }
                    } else if (this.mDc.state == DriverCallIms.State.HOLDING) {
                        Log.i(this, "Call being resumed.");
                        this.mIsConfInProgress = false;
                        if (this.mStateChangeReportingAllowed) {
                            this.mCallbackHandler.callSessionResumed(this.mCallProfile.newImsCallProfile());
                        }
                    } else {
                        Log.i(this, "Call resumed skipped, conf status = " + this.mIsConfInProgress);
                    }
                    break;
                case 2:
                    if (this.mDc.state != DriverCallIms.State.HOLDING) {
                        Log.i(this, "Call being held.");
                        if (this.mStateChangeReportingAllowed) {
                            this.mCallbackHandler.callSessionHeld(this.mCallProfile.newImsCallProfile());
                        }
                    }
                    break;
                case 3:
                    if (this.mDc == null) {
                        Log.i(this, "MO Dialing call!");
                        this.mCallId = dcUpdate.index;
                        ImsStreamMediaProfile mediaProfile = ImsMediaUtils.newImsStreamMediaProfile(3, dcUpdate.callDetails.rttMode);
                        if (this.mStateChangeReportingAllowed) {
                            this.mCallbackHandler.callSessionProgressing(mediaProfile);
                        }
                    }
                    handleRetryErrorNotify(dcUpdate);
                    break;
                case 4:
                    this.mState = 2;
                    if (this.mDc == null) {
                        Log.i(this, "MO Alerting call!");
                        this.mCallId = dcUpdate.index;
                    }
                    if (this.mDc.state != DriverCallIms.State.ALERTING) {
                        extractCallDetailsIntoCallProfile(dcUpdate);
                        int audioDirection = 3;
                        if (this.mRingbackToneRequest) {
                            audioDirection = 0;
                        }
                        ImsStreamMediaProfile mediaProfile2 = ImsMediaUtils.newImsStreamMediaProfile(audioDirection, dcUpdate.callDetails.rttMode);
                        if (this.mStateChangeReportingAllowed) {
                            this.mCallbackHandler.callSessionProgressing(mediaProfile2);
                        }
                    }
                    handleRetryErrorNotify(dcUpdate);
                    break;
                case 5:
                    updateCrsStatus(dcUpdate);
                case 6:
                    extractCallDetailsIntoCallProfile(dcUpdate);
                    break;
                case 7:
                    this.mState = 8;
                    if (this.mIsCallTerminatedDueToLowBattery && !QtiImsExtUtils.allowVideoCallsInLowBattery(this.mPhoneId, this.mContext)) {
                        errorCode = 112;
                    } else {
                        errorCode = dcUpdate.callFailCause.getCode();
                    }
                    this.mCallProfile.setCallExtraInt("CallFailExtraCode", errorCode);
                    if (errorCode == 149) {
                        Log.i(this, "Call was ended as LTE to 3G/2G handover was not feasible.");
                    }
                    Log.i(this, "Sip callFailCause:" + dcUpdate.callFailCause);
                    if (this.mDisconnCause == 0) {
                        if (dcUpdate != null && this.mStateChangeReportingAllowed) {
                            if ((this.mDc.isMT && ((this.mDc.state == DriverCallIms.State.INCOMING || this.mDc.state == DriverCallIms.State.WAITING) && !isMultiEndpointFailCause(dcUpdate.callFailCause.getCode()))) || dcUpdate.callFailCause.getCode() == 501) {
                                this.mCallbackHandler.callSessionTerminated(new ImsReasonInfo(510, dcUpdate.callFailCause.getExtraCode(), dcUpdate.callFailCause.getExtraMessage()));
                            } else {
                                this.mCallbackHandler.callSessionTerminated(dcUpdate.callFailCause);
                            }
                        }
                    } else if (this.mStateChangeReportingAllowed) {
                        this.mCallbackHandler.callSessionTerminated(new ImsReasonInfo(this.mDisconnCause, dcUpdate.callFailCause.getExtraCode(), dcUpdate.callFailCause.getExtraMessage()));
                    }
                    notifySessionDisconnected();
                    break;
            }
            DriverCallIms driverCallIms2 = this.mDc;
            if (driverCallIms2 != null && dcUpdate != null && driverCallIms2.state == dcUpdate.state) {
                z = true;
            }
            boolean areStatesSame = z;
            if (updateLocalDc(dcUpdate)) {
                maybeTriggerCallSessionUpdate(dcUpdate, areStatesSame);
            }
        }
    }

    /* JADX INFO: renamed from: org.codeaurora.ims.ImsCallSessionImpl$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$org$codeaurora$ims$DriverCallIms$State;

        static {
            int[] iArr = new int[DriverCallIms.State.values().length];
            $SwitchMap$org$codeaurora$ims$DriverCallIms$State = iArr;
            try {
                iArr[DriverCallIms.State.ACTIVE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codeaurora$ims$DriverCallIms$State[DriverCallIms.State.HOLDING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codeaurora$ims$DriverCallIms$State[DriverCallIms.State.DIALING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codeaurora$ims$DriverCallIms$State[DriverCallIms.State.ALERTING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codeaurora$ims$DriverCallIms$State[DriverCallIms.State.INCOMING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codeaurora$ims$DriverCallIms$State[DriverCallIms.State.WAITING.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codeaurora$ims$DriverCallIms$State[DriverCallIms.State.END.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private boolean isMultiEndpointFailCause(int code) {
        return code == 1014 || code == 1015 || code == 1016;
    }

    private boolean maybeTriggerCallSessionUpdate(DriverCallIms dcUpdate, boolean areStatesSame) {
        boolean isCallNotEnded = dcUpdate.state != DriverCallIms.State.END;
        if (areStatesSame && isCallNotEnded) {
            Log.d(this, "Call details updated. currentCallDetails= " + this.mDc.callDetails + " to newCallDetails= " + dcUpdate.callDetails);
            this.mCallbackHandler.callSessionUpdated(this.mCallProfile.newImsCallProfile());
        }
        return areStatesSame && isCallNotEnded;
    }

    public boolean maybeTriggerCallSessionUpdate(DriverCallIms dcUpdate) {
        DriverCallIms driverCallIms = this.mDc;
        boolean areStatesSame = (driverCallIms == null || dcUpdate == null || driverCallIms.state != dcUpdate.state) ? false : true;
        return maybeTriggerCallSessionUpdate(dcUpdate, areStatesSame);
    }

    public void updateOrientationMode(int mode) {
        Log.v(this, "updateOrientationMode: orientation mode - " + mode);
        this.mCallProfile.setCallExtraInt("OrientationMode", mode);
        if (this.mDc == null && getInternalState() == DriverCallIms.State.DIALING) {
            Log.v(this, "updateOrientationMode: mDc is null and in dialing state ");
        } else {
            boolean isCallSessionUpdated = maybeTriggerCallSessionUpdate(this.mDc);
            Log.v(this, "updateOrientationMode: isCallSessionUpdated - " + isCallSessionUpdated);
        }
    }

    public void updateRecordingSurface(Surface recordingSurface, int width, int height) {
        IImsScreenShareListener iImsScreenShareListener;
        Log.v(this, "updateRecordingSurface: recording surface - " + recordingSurface + " width - " + width + " height - " + height);
        DriverCallIms driverCallIms = this.mDc;
        boolean isValidCall = (driverCallIms == null || driverCallIms.callDetails == null) ? false : true;
        if (!isValidCall || (iImsScreenShareListener = this.mScreenSharelistener) == null) {
            Log.e(this, "updateRecordingSurface: is not valid call or mScreenSharelistener is NULL");
            return;
        }
        try {
            iImsScreenShareListener.onRecordingSurfaceChanged(this.mPhoneId, recordingSurface, width, height);
        } catch (Throwable th) {
            Log.e(this, "onRecordingSurfaceChanged exception!");
        }
    }

    public void setScreenShareListener(IImsScreenShareListener listener) {
        this.mScreenSharelistener = listener;
    }

    public void startScreenShare(int width, int height) {
        Log.d(this, "startScreenShare: width - " + width + " height - " + height);
        ImsVideoCallProviderImpl imsVideoCallProviderImpl = this.mImsVideoCallProviderImpl;
        if (imsVideoCallProviderImpl != null) {
            imsVideoCallProviderImpl.setSharedDisplayParams(width, height);
        }
    }

    public void stopScreenShare() {
        Log.d(this, "stopScreenShare");
        ImsVideoCallProviderImpl imsVideoCallProviderImpl = this.mImsVideoCallProviderImpl;
        if (imsVideoCallProviderImpl != null) {
            imsVideoCallProviderImpl.stopScreenShare();
        }
    }

    public void updateVideoCallDataUsageInfo(QtiVideoCallDataUsage dataUsage) {
        if (!Config.isConfigEnabled(this.mContext, R.bool.config_video_call_datausage_enable)) {
            return;
        }
        this.mCallProfile.getCallExtras().putParcelable("dataUsage", dataUsage);
        boolean isCallSessionUpdated = maybeTriggerCallSessionUpdate(this.mDc);
        Log.v(this, "updateVideoCallDataUsageInfo: isCallSessionUpdated - " + isCallSessionUpdated);
    }

    private boolean maybeUpdateLowBatteryStatus() {
        if (!this.mStateChangeReportingAllowed) {
            Log.w(this, "merge is in progress so ignore low battery update");
            return false;
        }
        boolean isLowBattery = LowBatteryHandler.getInstance().isLowBattery();
        boolean hasChanged = isLowBattery != this.mIsLowBattery;
        Log.v(this, "maybeUpdateLowBatteryStatus isLowBattery: " + isLowBattery + " mIsLowBattery: " + this.mIsLowBattery);
        if (hasChanged) {
            this.mIsLowBattery = isLowBattery;
            this.mCallProfile.setCallExtraBoolean("LowBattery", isLowBattery);
        }
        return hasChanged;
    }

    private void maybeNotifyCallTypeChanging(DriverCallIms dc) {
        if (isSessionValid() && ImsCallUtils.hasCallTypeChanged(this.mDc, dc)) {
            notifyCallTypeChanging(dc.callDetails.call_type);
        }
    }

    public void updateLowBatteryStatus() {
        Log.v(this, "updateLowBatteryStatus");
        if (maybeUpdateLowBatteryStatus()) {
            maybeTriggerCallSessionUpdate(this.mDc);
        }
    }

    private void setCapabilitiesInProfiles(DriverCallIms dcUpdate) {
        QImsCallProfile qImsCallProfile = this.mLocalCallProfile;
        if (qImsCallProfile != null) {
            ImsStreamMediaProfile localUpdate = qImsCallProfile.getMediaProfile();
            localUpdate.copyFrom(ImsMediaUtils.newImsStreamMediaProfile(mapAudioCodecFromExtras(dcUpdate.callDetails.getValueForKeyFromExtras(dcUpdate.callDetails.extras, CallDetails.EXTRAS_CODEC)), 3, 0));
            setLocalProfileCallType(dcUpdate);
            localUpdate.setRttMode(getRttMode(dcUpdate.callDetails.call_type, dcUpdate.callDetails.localAbility));
            this.mLocalCallProfile.updateMediaProfile(localUpdate);
        }
        if (this.mRemoteCallProfile != null) {
            setRemoteProfileCallType(dcUpdate);
            if (dcUpdate.callDetails.peerAbility != null) {
                ImsStreamMediaProfile remoteUpdate = this.mRemoteCallProfile.getMediaProfile();
                remoteUpdate.copyFrom(ImsMediaUtils.newImsStreamMediaProfile());
                this.mRemoteCallProfile.setCallRestrictCause(getRestrictCause(this.mCallProfile.getCallType() == 4 ? 3 : 0, dcUpdate.callDetails.peerAbility));
                remoteUpdate.setRttMode(getRttMode(dcUpdate.callDetails.call_type, dcUpdate.callDetails.peerAbility));
                this.mRemoteCallProfile.updateMediaProfile(remoteUpdate);
            }
        }
    }

    private void setLocalProfileCallType(DriverCallIms dcUpdate) {
        int callType;
        boolean isLocalVideoServiceAllowed = isServiceAllowed(3, dcUpdate.callDetails.localAbility);
        boolean isLocalVoiceServiceAllowed = isServiceAllowed(0, dcUpdate.callDetails.localAbility);
        if (QtiImsExtUtils.shallRemoveModifyCallCapability(this.mPhoneId, this.mContext) && dcUpdate.state == DriverCallIms.State.HOLDING) {
            isLocalVideoServiceAllowed = false;
            isLocalVoiceServiceAllowed = false;
        }
        if (isLocalVideoServiceAllowed && isLocalVoiceServiceAllowed && this.mIsVideoAllowed && this.mIsVoiceAllowed) {
            callType = 3;
        } else if (isLocalVideoServiceAllowed && this.mIsVideoAllowed) {
            callType = 4;
        } else if (isLocalVoiceServiceAllowed && this.mIsVoiceAllowed) {
            callType = 2;
        } else {
            callType = 7;
        }
        this.mLocalCallProfile.updateCallType(new ImsCallProfile(1, callType));
    }

    private void setRemoteProfileCallType(DriverCallIms dcUpdate) {
        int callType;
        boolean isRemoteVideoServiceAllowed = isServiceAllowed(3, dcUpdate.callDetails.peerAbility);
        boolean isRemoteVoiceServiceAllowed = isServiceAllowed(0, dcUpdate.callDetails.peerAbility);
        if (QtiImsExtUtils.shallRemoveModifyCallCapability(this.mPhoneId, this.mContext) && getMtSuppSvcCode() == 2) {
            isRemoteVideoServiceAllowed = false;
            isRemoteVoiceServiceAllowed = false;
        }
        if (isRemoteVideoServiceAllowed && isRemoteVoiceServiceAllowed) {
            callType = 3;
        } else if (isRemoteVideoServiceAllowed) {
            callType = 4;
        } else if (isRemoteVoiceServiceAllowed) {
            callType = 2;
        } else {
            callType = 7;
        }
        this.mRemoteCallProfile.updateCallType(new ImsCallProfile(1, callType));
    }

    public void updateConfSession(DriverCallIms dc) {
        Log.i(this, "updateConfSession for " + dc);
        if (isSessionValid() && dc.state == DriverCallIms.State.ACTIVE && dc.isMpty) {
            this.mState = 4;
            this.mCallId = this.mDc.index;
        }
    }

    private void setMtSuppSvcCode(int code) {
        this.mMtSuppSvcCode = code;
    }

    private int getMtSuppSvcCode() {
        return this.mMtSuppSvcCode;
    }

    public void updateSuppServiceInfo(ImsSuppServiceNotification suppSvcNotification, boolean startOnHoldLocalTone) {
        Log.i(this, "updateSuppSvcInfo: suppSvcNotification= " + suppSvcNotification + " startOnHoldLocalTone = " + startOnHoldLocalTone);
        if (isSessionValid()) {
            if (suppSvcNotification.notificationType == 0) {
                this.mCallbackHandler.callSessionSuppServiceReceived(suppSvcNotification);
                return;
            }
            if (suppSvcNotification.notificationType == 1) {
                boolean isChanged = false;
                setMtSuppSvcCode(suppSvcNotification.code);
                ImsStreamMediaProfile mediaProfile = this.mCallProfile.getMediaProfile();
                switch (suppSvcNotification.code) {
                    case 2:
                        if (startOnHoldLocalTone) {
                            mediaProfile.copyFrom(ImsMediaUtils.newImsStreamMediaProfile(mediaProfile.getAudioQuality(), 0, mediaProfile.getVideoQuality(), mediaProfile.getVideoDirection(), mediaProfile.getRttMode()));
                        }
                        this.mCallProfile.updateMediaProfile(mediaProfile);
                        this.mCallbackHandler.callSessionHoldReceived(this.mCallProfile.newImsCallProfile());
                        if (QtiImsExtUtils.shallRemoveModifyCallCapability(this.mPhoneId, this.mContext) && this.mRemoteCallProfile.getCallType() != 7) {
                            this.mRemoteCallProfile.updateCallType(new ImsCallProfile(1, 7));
                            isChanged = true;
                        }
                        break;
                    case 3:
                        if (this.mCallProfile.getMediaProfile().getAudioDirection() == 0) {
                            mediaProfile.copyFrom(ImsMediaUtils.newImsStreamMediaProfile(mediaProfile.getAudioQuality(), mediaProfile.getVideoQuality(), mediaProfile.getVideoDirection(), mediaProfile.getRttMode()));
                        }
                        this.mCallProfile.updateMediaProfile(mediaProfile);
                        this.mCallbackHandler.callSessionResumeReceived(this.mCallProfile.newImsCallProfile());
                        if (QtiImsExtUtils.shallRemoveModifyCallCapability(this.mPhoneId, this.mContext) && this.mRemoteCallProfile.getCallType() == 7) {
                            setRemoteProfileCallType(this.mDc);
                            isChanged = true;
                        }
                        break;
                    default:
                        Log.i(this, "Non-Hold/Resume supp svc code received.");
                        this.mCallbackHandler.callSessionSuppServiceReceived(suppSvcNotification);
                        break;
                }
                if (isChanged) {
                    this.mCallbackHandler.callSessionUpdated(this.mCallProfile.newImsCallProfile());
                }
            }
        }
    }

    public void notifyTtyModeChange(int mode) {
        Log.i(this, "TTY mode = " + mode);
        if (isSessionValid()) {
            if (this.mCallbackHandler != null) {
                this.mCallbackHandler.callSessionTtyModeReceived(mode);
            } else {
                Log.e(this, "notifyTtyModeChange ListenerProxy null! ");
            }
        }
    }

    public void handleRetryErrorNotify(DriverCallIms dc) {
        int sipErrorCode = 0;
        switch (dc.mCallFailReason) {
            case 516:
                sipErrorCode = SIP_FORBIDDEN;
                break;
            case 519:
                sipErrorCode = SIP_REQUEST_TIMEOUT;
                break;
            case 520:
                sipErrorCode = SIP_TEMPORARILY_UNAVAILABLE;
                break;
            case 526:
                sipErrorCode = SIP_SERVER_INTERNAL_ERROR;
                break;
            case 527:
                sipErrorCode = 501;
                break;
            case 528:
                sipErrorCode = 502;
                break;
            case 529:
                sipErrorCode = 503;
                break;
            case 531:
                sipErrorCode = 505;
                break;
            case 532:
                sipErrorCode = 513;
                break;
            case 533:
                sipErrorCode = 580;
                break;
        }
        if (sipErrorCode != 0) {
            Log.i(this, "Retry Error Notify " + sipErrorCode);
            String additionalCallInfo = "AdditionalCallInfo=" + sipErrorCode;
            dc.callDetails.addExtra(additionalCallInfo);
            this.mCallProfile.setCallExtraInt("AdditionalCallInfo", sipErrorCode);
            boolean displayCsRetryToast = this.mContext.getResources().getBoolean(R.bool.config_carrier_display_csretry_toast);
            if (displayCsRetryToast) {
                String msg = "LTE HD voice is unavailable. 3G voice call will be connected.SIP Error code: " + sipErrorCode;
                Toast.makeText(this.mContext, msg, 1).show();
            }
        }
    }

    public void handleHandover(int hoType, int srcTech, int targetTech, int extraType, byte[] extraInfo, String errorCode, String errorMessage) {
        Log.i(this, "hoType : " + hoType + "srcTech: " + srcTech + " targetTech: " + targetTech);
        if (isSessionValid()) {
            int error = parseErrorCode(errorCode);
            int rilSrcTech = getRadioTechFromCallMode(srcTech);
            int rilTargetTech = getRadioTechFromCallMode(targetTech);
            switch (hoType) {
                case 0:
                    break;
                case 1:
                    this.mCallbackHandler.callSessionHandover(rilSrcTech, rilTargetTech, new ImsReasonInfo(error, 0, errorMessage));
                    if (rilSrcTech == 18 && rilTargetTech == 13) {
                        maybeUpdateVoWifiCallQualityExtra(0, true);
                        break;
                    }
                    break;
                case 2:
                case 3:
                    Log.i(this, "HO Failure for WWAN->IWLAN " + extraType + extraInfo);
                    if (extraType == 1) {
                        this.mCallProfile.setCallExtraInt(CallDetails.EXTRAS_HANDOVER_INFORMATION, 1);
                    }
                    alertForHandoverFailed();
                    this.mCallbackHandler.callSessionHandoverFailed(rilSrcTech, rilTargetTech, new ImsReasonInfo(error, 0, errorMessage));
                    break;
                case 4:
                    alertForHandoverFailed();
                    this.mCallbackHandler.callSessionHandoverFailed(rilSrcTech, rilTargetTech, new ImsReasonInfo(error, 0, errorMessage));
                    break;
                case 5:
                    this.mCallbackHandler.callSessionMayHandover(rilSrcTech, rilTargetTech);
                    break;
                default:
                    Log.e(this, "Unhandled hoType: " + hoType);
                    break;
            }
        }
    }

    private int parseErrorCode(String errorCode) {
        if ("CD-04".equals(errorCode)) {
            return 1100;
        }
        return 0;
    }

    private class ImsCallSessionImplHandler extends Handler {
        ImsCallSessionImplHandler() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(this, "Message received: what = " + msg.what);
            if (ImsCallSessionImpl.this.isSessionValid()) {
                switch (msg.what) {
                    case 1:
                        AsyncResult ar = (AsyncResult) msg.obj;
                        if (ar != null && ar.exception != null) {
                            Log.i(this, "Dial error");
                            if (ar.userObj != null) {
                                int errorCode = 0;
                                if (ar.exception instanceof QtiImsException) {
                                    errorCode = ((QtiImsException) ar.exception).getCode();
                                }
                                if (errorCode == 112 || errorCode == 150) {
                                    ImsCallSessionImpl.this.mCallProfile.setCallExtraInt("CallFailExtraCode", errorCode);
                                    errorCode = 0;
                                }
                                ImsCallSessionImpl.this.mState = 8;
                                ImsCallSessionImpl.this.mCallbackHandler.callSessionInitiatedFailed(new ImsReasonInfo(errorCode, 0, "Dial Failed"));
                            }
                            break;
                        }
                        break;
                    case 2:
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        if (ar2 != null && ar2.exception != null) {
                            Log.i(this, "Accept error: " + ar2.exception);
                            break;
                        }
                        break;
                    case 3:
                        AsyncResult ar3 = (AsyncResult) msg.obj;
                        if (ar3 != null && ar3.exception != null) {
                            Log.i(this, "Hangup error: " + ar3.exception);
                        }
                        ImsCallSessionImpl.this.mDisconnCause = 501;
                        break;
                    case 4:
                        AsyncResult ar4 = (AsyncResult) msg.obj;
                        if (ar4 != null && ar4.exception != null) {
                            Log.i(this, "Hold error");
                            ImsCallSessionImpl imsCallSessionImpl = ImsCallSessionImpl.this;
                            imsCallSessionImpl.maybeDisplaySuppServiceErrorMsg(imsCallSessionImpl.mContext.getResources().getString(R.string.call_hold_label), (ImsRilException) ar4.exception);
                            if (ar4.userObj != null) {
                                ImsCallSessionImpl.this.mCallbackHandler.callSessionHoldFailed(ImsCallUtils.getImsReasonInfo(ar4));
                            }
                            break;
                        }
                        break;
                    case 5:
                        AsyncResult ar5 = (AsyncResult) msg.obj;
                        if (ar5 != null && ar5.exception != null) {
                            Log.i(this, "Resume error");
                            ImsCallSessionImpl imsCallSessionImpl2 = ImsCallSessionImpl.this;
                            imsCallSessionImpl2.maybeDisplaySuppServiceErrorMsg(imsCallSessionImpl2.mContext.getResources().getString(R.string.call_resume_label), (ImsRilException) ar5.exception);
                            if (ar5.userObj != null) {
                                ImsCallSessionImpl.this.mCallbackHandler.callSessionResumeFailed(ImsCallUtils.getImsReasonInfo(ar5));
                            }
                            break;
                        }
                        break;
                    case 7:
                        AsyncResult ar6 = (AsyncResult) msg.obj;
                        if (ar6 != null && ar6.exception != null) {
                            Log.i(this, "Reject error: " + ar6.exception);
                        } else {
                            ImsCallSessionImpl.this.mDisconnCause = 143;
                        }
                        break;
                    case 8:
                        AsyncResult ar7 = (AsyncResult) msg.obj;
                        if (ar7 != null && ar7.exception != null) {
                            Log.i(this, "Add Participant error");
                            if (ar7.userObj != null) {
                                ImsCallSessionImpl.this.mCallbackHandler.callSessionInviteParticipantsRequestFailed(ImsCallUtils.getImsReasonInfo(ar7));
                            }
                        } else {
                            ImsCallSessionImpl.this.mCallbackHandler.callSessionInviteParticipantsRequestDelivered();
                        }
                        ImsCallSessionImpl.this.processAddParticipantResponse(ar7.exception == null);
                        break;
                    case 9:
                        AsyncResult ar8 = (AsyncResult) msg.obj;
                        if (ar8 != null) {
                            ImsCallSessionImpl.this.mRingbackToneRequest = ((Boolean) ar8.result).booleanValue();
                            Log.i(this, "EVENT_RINGBACK_TONE, playTone = " + ImsCallSessionImpl.this.mRingbackToneRequest);
                            if (ImsCallSessionImpl.this.mDc != null && ImsCallSessionImpl.this.mDc.state == DriverCallIms.State.ALERTING) {
                                int audioDirection = 3;
                                if (ImsCallSessionImpl.this.mRingbackToneRequest) {
                                    audioDirection = 0;
                                }
                                ImsStreamMediaProfile mediaProfile = ImsMediaUtils.newImsStreamMediaProfile(audioDirection, ImsCallSessionImpl.this.mDc.callDetails.rttMode);
                                ImsCallSessionImpl.this.mCallbackHandler.callSessionProgressing(mediaProfile);
                                break;
                            }
                        }
                        break;
                    case 11:
                        ImsCallSessionImpl.this.doClose();
                        break;
                    case 12:
                        AsyncResult ar9 = (AsyncResult) msg.obj;
                        if (ar9 != null && ar9.exception != null) {
                            Log.e(this, "RTT: Message exception: " + ar9.exception);
                        } else {
                            ImsCallSessionImpl.this.notifyReceivedRttMessage(ar9.result);
                        }
                        break;
                    case 13:
                        AsyncResult ar10 = (AsyncResult) msg.obj;
                        if (ar10 != null && ar10.exception != null) {
                            Log.e(this, "RTT: Send message exception: " + ar10.exception);
                        } else {
                            Log.d(this, "RTT: EVENT_SEND_RTT_MESSAGE received");
                        }
                        break;
                    case 14:
                        ImsCallSessionImpl.this.notifyRttModifyResponse((AsyncResult) msg.obj);
                        break;
                    case 15:
                        AsyncResult ar11 = (AsyncResult) msg.obj;
                        if (ar11 != null && ar11.exception != null) {
                            Log.e(this, "RTT:  upgarde response exception: " + ar11.exception);
                        } else {
                            Log.d(this, "RTT: EVENT_RTT_UPGRADE_CONFIRM_DONE received");
                        }
                        break;
                    case 16:
                        AsyncResult ar12 = (AsyncResult) msg.obj;
                        if (ar12 != null && ar12.exception != null) {
                            Log.i(this, "Deflect error");
                        } else {
                            Log.i(this, "Deflect success");
                        }
                        break;
                    case 17:
                        if (msg.obj instanceof String[]) {
                            ImsCallSessionImpl.this.processAddParticipantsList((String[]) msg.obj);
                        }
                        break;
                    case 18:
                        AsyncResult ar13 = (AsyncResult) msg.obj;
                        if (ar13 == null) {
                            Log.e(this, "Voice info: Result is null");
                        } else if (ar13.exception != null) {
                            Log.e(this, "Voice info: Message exception: " + ar13.exception);
                        } else {
                            ImsCallSessionImpl.this.notifyVoiceInfoChanged(((Integer) ar13.result).intValue());
                        }
                        break;
                    case 19:
                        AsyncResult ar14 = (AsyncResult) msg.obj;
                        if (ar14 == null) {
                            Log.e(this, "Send dtmf: Result is null");
                        } else {
                            try {
                                Message result = (Message) ar14.userObj;
                                result.replyTo.send(result);
                            } catch (Exception e) {
                                Log.e(this, "Failed to send result", e);
                                return;
                            }
                        }
                        break;
                    case 20:
                        AsyncResult ar15 = (AsyncResult) msg.obj;
                        if (ar15 != null && ar15.exception != null) {
                            Log.i(this, "Transfer error: " + ar15.exception);
                        } else {
                            Log.i(this, "Transfer success");
                        }
                        break;
                    case 22:
                        ImsCallSessionImpl.this.handleSipDtmfReceived((AsyncResult) msg.obj);
                        break;
                }
            }
        }
    }

    boolean isImsCallSessionAlive() {
        return (this.mState == 8 || this.mState == 7 || this.mState == 0 || this.mState == -1) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doClose() {
        Log.i(this, "doClose!");
        if (isImsCallSessionAlive()) {
            Log.i(this, "Received Session Close request while it is alive");
        }
        if (this.mState != -1) {
            ImsCallModification imsCallModification = this.mImsCallModification;
            if (imsCallModification != null) {
                imsCallModification.close();
            }
            if (!this.mIsMergeHostSession) {
                if (this.mCallbackHandler != null) {
                    this.mCallbackHandler.dispose();
                    this.mCallbackHandler = null;
                }
            } else {
                Log.i(this, "Not clearing listener, ongoing merge.");
            }
            DriverCallIms driverCallIms = this.mDc;
            if (driverCallIms != null && !driverCallIms.isMT && this.mCi != null) {
                this.mCi.unregisterForRingbackTone(this.mHandler);
            }
            notifySessionClosed();
            synchronized (this.mListeners) {
                this.mListeners.clear();
            }
            if (this.mCi != null) {
                this.mCi.deregisterForVoiceInfo(this.mHandler);
                this.mCi.unregisterForSipDtmfInfo(this.mHandler);
                this.mCi = null;
            }
            this.mDc = null;
            this.mCallId = -1;
            this.mLocalCallProfile = null;
            this.mRemoteCallProfile = null;
            this.mCallProfile = null;
            this.mState = -1;
            this.mInCall = false;
            this.mIsConferenceHostSession = false;
            this.mHandler = null;
            this.mIsConfInProgress = false;
            this.newSession = null;
            this.mImsVideoCallProviderImpl = null;
            this.mImsCallModification = null;
            this.mCallee = null;
            this.mConfInfo = null;
            this.mIsLowBattery = false;
        }
    }

    public void close() {
        Log.i(this, "Close!");
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.obtainMessage(11).sendToTarget();
        }
    }

    public String getCallId() {
        return Integer.toString(this.mCallId);
    }

    public int getMediaId() {
        DriverCallIms driverCallIms;
        if (isSessionValid() && (driverCallIms = this.mDc) != null) {
            return driverCallIms.callDetails.callMediaId;
        }
        return -1;
    }

    public boolean hasMediaIdValid() {
        DriverCallIms driverCallIms;
        if (isSessionValid() && (driverCallIms = this.mDc) != null) {
            return driverCallIms.callDetails.hasMediaIdValid();
        }
        return false;
    }

    public ImsCallProfile getCallProfile() {
        if (isSessionValid()) {
            return this.mCallProfile.newImsCallProfile();
        }
        return null;
    }

    public ImsCallProfile getLocalCallProfile() {
        if (isSessionValid()) {
            return this.mLocalCallProfile.newImsCallProfile();
        }
        return null;
    }

    public ImsCallProfile getRemoteCallProfile() {
        if (isSessionValid()) {
            return this.mRemoteCallProfile.newImsCallProfile();
        }
        return null;
    }

    public String getProperty(String name) {
        if (!isSessionValid()) {
            return null;
        }
        QImsCallProfile qImsCallProfile = this.mCallProfile;
        if (qImsCallProfile != null) {
            String value = qImsCallProfile.getCallExtra(name);
            return value;
        }
        Log.e(this, "Call Profile null! ");
        return null;
    }

    public int getState() {
        if (isSessionValid()) {
            return this.mState;
        }
        return -1;
    }

    public DriverCallIms.State getInternalState() {
        if (!isSessionValid()) {
            return DriverCallIms.State.END;
        }
        DriverCallIms driverCallIms = this.mDc;
        if (driverCallIms != null) {
            DriverCallIms.State state = driverCallIms.state;
            return state;
        }
        if (this.mState != 1) {
            return null;
        }
        DriverCallIms.State state2 = DriverCallIms.State.DIALING;
        return state2;
    }

    public int getPhoneId() {
        return this.mPhoneId;
    }

    public int getInternalCallType() {
        if (!isSessionValid()) {
            return 10;
        }
        DriverCallIms driverCallIms = this.mDc;
        if (driverCallIms != null && driverCallIms.callDetails != null) {
            int callType = this.mDc.callDetails.call_type;
            return callType;
        }
        QImsCallProfile qImsCallProfile = this.mCallProfile;
        if (qImsCallProfile == null) {
            return 10;
        }
        int callType2 = ImsCallUtils.convertToInternalCallType(qImsCallProfile.getCallType());
        return callType2;
    }

    public int getCallDomain() {
        DriverCallIms driverCallIms;
        if (!isSessionValid() || (driverCallIms = this.mDc) == null || driverCallIms.callDetails == null) {
            return 3;
        }
        int callDomain = this.mDc.callDetails.call_domain;
        return callDomain;
    }

    public boolean isMultipartyCall() {
        DriverCallIms driverCallIms;
        if (isSessionValid() && (driverCallIms = this.mDc) != null) {
            return driverCallIms.isMpty;
        }
        return false;
    }

    public DriverCallIms.State getDcState() {
        DriverCallIms driverCallIms = this.mDc;
        if (driverCallIms != null) {
            return driverCallIms.state;
        }
        Log.i(this, "Null mDc! Returning null!");
        return null;
    }

    public String getCallee() {
        if (isSessionValid()) {
            return this.mCallee;
        }
        return null;
    }

    public DriverCallIms.State getDriverCallState() {
        return !isSessionValid() ? DriverCallIms.State.END : this.mDc.state;
    }

    public boolean isMultiparty() {
        QImsCallProfile qImsCallProfile;
        if (!isSessionValid()) {
            return false;
        }
        DriverCallIms driverCallIms = this.mDc;
        return (driverCallIms != null && driverCallIms.isMpty) || ((qImsCallProfile = this.mCallProfile) != null && qImsCallProfile.getCallExtraBoolean("android.telephony.ims.extra.CONFERENCE"));
    }

    public boolean isInCall() {
        if (!isSessionValid()) {
            return false;
        }
        switch (AnonymousClass3.$SwitchMap$org$codeaurora$ims$DriverCallIms$State[this.mDc.state.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    public void setMute(boolean muted) {
        isSessionValid();
    }

    public void reportNewConferenceCallSession(ImsCallSessionImpl confCallSession) {
        if (confCallSession != null) {
            Log.i(this, "Calling callSessionMergeStarted");
            this.mCallbackHandler.callSessionMergeStarted(confCallSession, confCallSession.getCallProfile());
        } else {
            Log.e(this, "Null confCallSession! Not calling callSessionMergeStarted");
        }
    }

    private void extractCallDetailsIntoCallProfile(DriverCallIms dcUpdate) {
        String key;
        if (dcUpdate == null) {
            Log.e(this, "Null dcUpdate in extractCallDetailsIntoCallProfile");
            return;
        }
        updateImsCallProfile(dcUpdate);
        if (dcUpdate.callDetails.extras != null && dcUpdate.callDetails.extras.length > 0) {
            for (int i = 0; i < dcUpdate.callDetails.extras.length; i++) {
                if (dcUpdate.callDetails.extras[i] != null) {
                    String[] keyAndValue = dcUpdate.callDetails.extras[i].split("=");
                    if (keyAndValue[0] != null) {
                        if (keyAndValue[0].contains(":")) {
                            String[] namespaceAndKey = keyAndValue[0].split(":");
                            key = namespaceAndKey[1];
                        } else {
                            key = keyAndValue[0];
                        }
                        this.mCallProfile.setCallExtra(key, keyAndValue[1]);
                    } else {
                        Log.e(this, "Bad extra string from lower layers!");
                        return;
                    }
                } else {
                    Log.e(this, "Element " + i + " is null in CallDetails Extras!");
                    return;
                }
            }
        }
    }

    private static int mapAudioCodecFromExtras(String codec) {
        int audioQuality = 0;
        if (codec == null) {
            return 0;
        }
        switch (codec) {
            case "QCELP13K":
                audioQuality = 3;
                break;
            case "EVRC":
                audioQuality = 4;
                break;
            case "EVRC_B":
                audioQuality = 5;
                break;
            case "EVRC_WB":
                audioQuality = 6;
                break;
            case "EVRC_NW":
                audioQuality = 7;
                break;
            case "AMR_NB":
                audioQuality = 1;
                break;
            case "AMR_WB":
                audioQuality = 2;
                break;
            case "GSM_EFR":
                audioQuality = 8;
                break;
            case "GSM_FR":
                audioQuality = 9;
                break;
            case "GSM_HR":
                audioQuality = 10;
                break;
            case "G711U":
                audioQuality = 11;
                break;
            case "G723":
                audioQuality = 12;
                break;
            case "G711A":
                audioQuality = 13;
                break;
            case "G722":
                audioQuality = 14;
                break;
            case "G711AB":
                audioQuality = 15;
                break;
            case "G729":
                audioQuality = 16;
                break;
            case "EVS_NB":
                audioQuality = 17;
                break;
            case "EVS_WB":
                audioQuality = 18;
                break;
            case "EVS_SWB":
                audioQuality = 19;
                break;
            case "EVS_FB":
                audioQuality = 20;
                break;
            default:
                Log.e(ImsCallSessionImpl.class.getSimpleName(), "Unsupported codec " + codec);
                break;
        }
        Log.i(ImsCallSessionImpl.class.getSimpleName(), "AudioQuality is " + audioQuality);
        return audioQuality;
    }

    private int mapCallTypeFromProfile(int callType) {
        switch (callType) {
            case 1:
                return 10;
            case 2:
                return 0;
            case 3:
            default:
                return 0;
            case 4:
                return 3;
            case 5:
                return 1;
            case 6:
                return 2;
            case 7:
                return 4;
        }
    }

    private static int getCallModeFromRadioTech(int radioTech) {
        switch (radioTech) {
            case 13:
                return 14;
            case 18:
                return 18;
            default:
                return 0;
        }
    }

    public int getRadioTechFromCallMode(int callMode) {
        switch (callMode) {
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

    private void updateImsCallProfile(DriverCallIms dc) {
        if (dc == null) {
            Log.e(this, "updateImsCallProfile called with dc null");
            return;
        }
        if (this.mCallProfile == null) {
            this.mCallProfile = new QImsCallProfile();
        }
        int callMode = 0;
        if (dc.callDetails != null && dc.callDetails.localAbility != null) {
            for (int i = 0; i < dc.callDetails.localAbility.length; i++) {
                ServiceStatus servStatus = dc.callDetails.localAbility[i];
                if (servStatus.type == dc.callDetails.call_type) {
                    callMode = servStatus.accessTechStatus[0].networkMode;
                    break;
                }
            }
            int radioTech = getRadioTechFromCallMode(callMode);
            if (dc.state == DriverCallIms.State.DIALING && radioTech == 0 && this.mTracker != null) {
                radioTech = ImsCallUtils.isVoiceCall(dc.callDetails.call_type) ? this.mTracker.isVoiceSupportedOverWifi() ? 18 : 13 : this.mTracker.isVideoSupportedOverWifi() ? 18 : 13;
            }
            this.mCallProfile.setCallExtraInt("android.telephony.ims.extra.CALL_NETWORK_TYPE", radioTech);
            this.mCallProfile.setCallExtraBoolean("isCall5G", dc.callDetails.isRadioTech5G());
        }
        this.mCallProfile.setCallExtra("oi", dc.number);
        this.mCallProfile.setCallExtra("cna", dc.name);
        this.mCallProfile.setCallExtraInt("oir", ImsCallProfile.presentationToOir(dc.numberPresentation));
        this.mCallProfile.setCallExtraInt("cnap", ImsCallProfile.presentationToOir(dc.namePresentation));
        this.mCallProfile.setCallExtraInt("CallSubstate", dc.callDetails.callsubstate);
        this.mCallProfile.setCallExtraBoolean("CallEncryption", dc.isEncrypted);
        Log.i(this, "updateImsCallProfile :: Packing encryption=" + dc.isEncrypted + " in mCallProfile's extras.");
        if (dc.historyInfo != null && !dc.historyInfo.isEmpty()) {
            this.mCallProfile.getCallExtras().putStringArrayList("CallHistoryInfo", extractHistoryInfoDetails(dc.historyInfo));
        }
        if (this.mCallProfile.getCallExtraBoolean("android.telephony.ims.extra.CONFERENCE")) {
            this.mCallProfile.setCallExtraBoolean("android.telephony.ims.extra.CONFERENCE", dc.isMpty);
        }
        if (dc.isConfSupportIndicated()) {
            this.mCallProfile.setCallExtraBoolean("conference_avail", ImsCallUtils.isVideoCall(dc.callDetails.call_type) ? dc.isVideoConfSupported() : true);
        }
        if (ImsCallUtils.hasCallTypeChanged(this.mDc, dc) || hasCauseCodeChanged(this.mDc, dc)) {
            this.mCallProfile.setCallExtraInt("SessionModificationCause", dc.callDetails.causeCode);
        }
        maybeUpdateLowBatteryStatus();
        this.mCallProfile.setCallExtraInt("phoneId", this.mPhoneId);
        int callType = 1;
        int videoDirection = -1;
        switch (dc.callDetails.call_type) {
            case 0:
                callType = 2;
                videoDirection = -1;
                break;
            case 1:
                callType = 5;
                videoDirection = 2;
                break;
            case 2:
                callType = 6;
                videoDirection = 1;
                break;
            case 3:
                callType = 4;
                videoDirection = 3;
                break;
            case 4:
                callType = this.mCallProfile.getCallType();
                videoDirection = 0;
                break;
            case 10:
                callType = 1;
                videoDirection = -1;
                break;
        }
        this.mCallProfile.updateCallType(new ImsCallProfile(1, callType));
        ImsStreamMediaProfile mediaProfile = this.mCallProfile.getMediaProfile();
        mediaProfile.copyFrom(ImsMediaUtils.newImsStreamMediaProfile(mediaProfile.getAudioQuality(), mediaProfile.getAudioDirection(), mediaProfile.getVideoQuality(), videoDirection, mediaProfile.getRttMode()));
        switch (dc.callDetails.rttMode) {
            case 0:
                mediaProfile.setRttMode(0);
                break;
            case 1:
                mediaProfile.setRttMode(1);
                break;
        }
        this.mCallProfile.updateMediaProfile(mediaProfile);
        if (getRadioTechFromCallMode(callMode) == 18) {
            maybeUpdateVoWifiCallQualityExtra(this.mVoWifiQuality, false);
        }
        VerstatInfo verstatInfo = dc.getVerstatInfo();
        if (verstatInfo != null) {
            this.mCallProfile = ImsCallUtils.toImsCallProfile(verstatInfo, this.mCallProfile);
            Log.i(this, "updateImsCallProfile :: " + verstatInfo);
        }
        QImsCallProfile imsCallProfile = toImsCallProfile(dc.mMtMultiLineInfo, this.mCallProfile);
        this.mCallProfile = imsCallProfile;
        imsCallProfile.setCallExtraBoolean("incomingTir", dc.isTirOverwriteAllowed);
        if (QtiImsExtUtils.isVideoCrsSupported(this.mPhoneId, this.mContext)) {
            maybeUpdateCrsExtras(dc);
        }
        if (QtiImsExtUtils.isCallProgressNotificationSupported(this.mPhoneId, this.mContext)) {
            maybeUpdateCallProgressInfoExtras(dc);
        }
    }

    private void maybeUpdateCrsExtras(DriverCallIms dc) {
        if (dc == null || this.mDc == null) {
            return;
        }
        if (dc.crsData == this.mDc.crsData && dc.isPreparatory == this.mDc.isPreparatory) {
            return;
        }
        if (dc.crsData.getCrsType() == 0 && this.mDc.crsData.getCrsType() == 0) {
            return;
        }
        if (dc.crsData.getCrsType() == 0) {
            List<String> extrasToRemove = new ArrayList<>();
            extrasToRemove.add("crsType");
            extrasToRemove.add("originalCallType");
            extrasToRemove.add("isPreparatory");
            this.mCallProfile.removeExtras(extrasToRemove);
            return;
        }
        this.mCallProfile.setCallExtraInt("crsType", dc.crsData.getCrsType());
        this.mCallProfile.setCallExtraInt("originalCallType", dc.crsData.getOriginalCallType());
        this.mCallProfile.setCallExtraBoolean("isPreparatory", dc.isPreparatory);
    }

    private void maybeUpdateCallProgressInfoExtras(DriverCallIms dc) {
        if (dc != null) {
            if (this.mDc != null && dc.callProgressInfo == this.mDc.callProgressInfo) {
                return;
            }
            boolean isTypeInvalid = dc.callProgressInfo.getType() == -1;
            int type = dc.callProgressInfo.getType();
            DriverCallIms driverCallIms = this.mDc;
            if (type == (driverCallIms != null ? driverCallIms.callProgressInfo.getType() : -1) && isTypeInvalid) {
                return;
            }
            if (isTypeInvalid) {
                List<String> extrasToRemove = new ArrayList<>();
                extrasToRemove.add("CallProgInfoType");
                extrasToRemove.add("CallProgReasonCode");
                extrasToRemove.add("CallProgReasonText");
                this.mCallProfile.removeExtras(extrasToRemove);
                return;
            }
            this.mCallProfile.setCallExtraInt("CallProgInfoType", dc.callProgressInfo.getType());
            this.mCallProfile.setCallExtraInt("CallProgReasonCode", dc.callProgressInfo.getReasonCode());
            this.mCallProfile.setCallExtra("CallProgReasonText", dc.callProgressInfo.getReasonText());
        }
    }

    public static QImsCallProfile toImsCallProfile(MultiIdentityLineInfo line, QImsCallProfile profile) {
        String msisdn;
        if (line == null || profile == null || (msisdn = line.getMsisdn()) == null || msisdn.isEmpty()) {
            return profile;
        }
        profile.setCallExtra("terminatingNumber", msisdn);
        profile.setCallExtraInt("lineType", line.getLineType());
        return profile;
    }

    public static boolean hasCauseCodeChanged(DriverCallIms dc, DriverCallIms dcUpdate) {
        return (dc == null || dcUpdate == null || dc.callDetails.causeCode == dcUpdate.callDetails.causeCode) ? false : true;
    }

    public boolean isConfInProgress() {
        if (isSessionValid()) {
            return this.mIsConfInProgress;
        }
        return false;
    }

    public void setNewSession(ImsCallSessionImpl session) {
        if (isSessionValid()) {
            this.newSession = session;
        }
    }

    public static MultiIdentityLineInfo getLineInfo(ImsCallProfile profile) {
        if (profile == null) {
            return MultiIdentityLineInfo.getDefaultLine();
        }
        String originatingNumber = profile.getCallExtra("originatingNumber");
        if (originatingNumber == null || originatingNumber.isEmpty()) {
            return MultiIdentityLineInfo.getDefaultLine();
        }
        int callType = profile.getCallExtraInt("lineType", 1);
        return new MultiIdentityLineInfo(originatingNumber, callType);
    }

    public void start(String callee, ImsCallProfile profile) {
        boolean isEncrypted;
        CallComposerInfo composerInfo;
        EmergencyCallInfo emergencyCallInfo;
        if (isSessionValid()) {
            this.mCallProfile.updateCallType(profile);
            this.mCallProfile.updateMediaProfile(profile);
            this.mCallProfile.getMediaProfile().setRttMode(profile.getMediaProfile().getRttMode());
            this.mState = 1;
            this.mCallee = callee;
            int clir = profile.getCallExtraInt("oir");
            boolean isEncrypted2 = SystemProperties.getInt(PROPERTY_DBG_ENCRYPTION_OVERRIDE, 0) == 1;
            Bundle callExtras = profile.getProprietaryCallExtras();
            if (!isEncrypted2 && callExtras != null) {
                boolean isEncrypted3 = callExtras.getBoolean("CallEncryption");
                callExtras.remove("CallEncryption");
                Bundle callComposerExtra = callExtras.getBundle(QtiCallConstants.EXTRA_CALL_COMPOSER_INFO);
                if (callComposerExtra == null) {
                    isEncrypted = isEncrypted3;
                    composerInfo = null;
                } else {
                    CallComposerInfo composerInfo2 = CallComposerInfoUtils.fromBundle(callComposerExtra);
                    callExtras.remove(QtiCallConstants.EXTRA_CALL_COMPOSER_INFO);
                    isEncrypted = isEncrypted3;
                    composerInfo = composerInfo2;
                }
            } else {
                isEncrypted = isEncrypted2;
                composerInfo = null;
            }
            MultiIdentityLineInfo info = getLineInfo(profile);
            Log.v(this, "MultiIdentity Line info in Dial Request :: " + info);
            CallDetails details = new CallDetails(mapCallTypeFromProfile(profile.getCallType()), 3, null, info);
            extractCallExtrasIntoCallDetails(callExtras, details);
            Log.v(this, "RTT: start rtt mode = " + profile.getMediaProfile().getRttMode());
            details.setRttMode(profile.getMediaProfile().getRttMode());
            details.setCallPull(profile.getCallExtraBoolean("CallPull", false));
            boolean carrierOneDial = isCarrierOneDial(details);
            if (carrierOneDial) {
                this.mCallProfile.setCallExtraInt("oir", ImsCallProfile.presentationToOir(1));
                this.mCallProfile.setCallExtraInt("cnap", ImsCallProfile.presentationToOir(1));
            }
            this.mCallProfile.setCallExtraInt("phoneId", this.mPhoneId);
            this.mCallbackHandler.callSessionUpdated(this.mCallProfile.newImsCallProfile());
            if (carrierOneDial && this.mIsLowBattery) {
                Log.d(this, "defer low battery video call dial request");
                return;
            }
            if (!canDial(details)) {
                failDialRequest(112);
                return;
            }
            if (isConfigEnabled(R.bool.config_regional_number_patterns_video_call) && ImsCallUtils.isVideoCallTypeWithDir(details.call_type) && !ImsCallUtils.isVideoCallNumValid(this.mCallee)) {
                failDialRequest(150);
                return;
            }
            int retryCallFailCause = profile.getCallExtraInt("android.telephony.ims.extra.RETRY_CALL_FAIL_REASON", 0);
            int retryCallFailNetworkType = profile.getCallExtraInt("android.telephony.ims.extra.RETRY_CALL_FAIL_NETWORKTYPE", 0);
            Log.v(this, "start: retryCallFailCause = " + retryCallFailCause + " retryCallFailNetworkType = " + retryCallFailNetworkType);
            RedialInfo redialInfo = new RedialInfo(retryCallFailCause, getCallModeFromRadioTech(retryCallFailNetworkType));
            if (profile.getServiceType() != 2) {
                emergencyCallInfo = null;
            } else {
                EmergencyCallInfo emergencyCallInfo2 = new EmergencyCallInfo(profile.getEmergencyServiceCategories(), profile.getEmergencyUrns(), profile.getEmergencyCallRouting(), profile.isEmergencyCallTesting(), profile.hasKnownUserIntentEmergency());
                emergencyCallInfo = emergencyCallInfo2;
            }
            this.mCi.dial(callee, emergencyCallInfo, clir, details, isEncrypted, composerInfo, redialInfo, this.mHandler.obtainMessage(1, this));
        }
    }

    private ArrayList<String> extractHistoryInfoDetails(String historyInfoString) {
        ArrayList<String> historyInfoStrings = new ArrayList<>();
        String[] historyInfoHops = historyInfoString.split("[\\r\\n]+");
        for (int i = 0; i < historyInfoHops.length; i++) {
            historyInfoStrings.add(historyInfoHops[i]);
            Log.v(this, "extractHistoryInfoDetails :: historyInfoHops[" + i + "]=" + historyInfoHops[i]);
        }
        return historyInfoStrings;
    }

    private boolean isCarrierOneDial(CallDetails details) {
        return ImsCallUtils.isCarrierOneSupported() && details != null && ImsCallUtils.isVideoCallTypeWithDir(details.call_type) && maybeUpdateLowBatteryStatus();
    }

    private void failDialRequest(int reason) {
        Message newMsg = this.mHandler.obtainMessage(1, this);
        AsyncResult.forMessage(newMsg, (Object) null, new QtiImsException("Dial Failed", reason));
        newMsg.sendToTarget();
    }

    private boolean canDial(CallDetails details) {
        return (LowBatteryHandler.getInstance().isLowBattery() && !ImsCallUtils.isCarrierOneSupported() && ImsCallUtils.isNotCsVideoCall(details)) ? false : true;
    }

    public void resumePendingCall(int videoState) {
        Log.d(this, "resumePendingCall VideoState = " + videoState);
        this.mCallProfile.updateCallType(new ImsCallProfile(1, ImsCallProfile.getCallTypeFromVideoState(videoState)));
        start(this.mCallee, this.mCallProfile.newImsCallProfile());
    }

    private void extractCallExtrasIntoCallDetails(Bundle callExtras, CallDetails details) {
        if (callExtras != null) {
            String[] extras = new String[callExtras.size()];
            int i = 0;
            for (String key : callExtras.keySet()) {
                String extraString = new String(key + "=" + (callExtras.get(key) == null ? "" : callExtras.get(key).toString()));
                extras[i] = extraString;
                i++;
            }
            details.setExtras(extras);
            return;
        }
        Log.i(this, "No extras in ImsCallProfile to map into CallDetails.");
    }

    public void startConference(String[] participants, ImsCallProfile profile) {
        if (isSessionValid()) {
            this.mCallProfile.updateProfile(profile);
            this.mState = 1;
            String callee = "";
            int i = 0;
            for (String participant : participants) {
                i++;
                if (!participant.isEmpty()) {
                    if (i == participants.length) {
                        callee = callee + participant;
                    } else {
                        callee = callee + participant + ";";
                    }
                }
            }
            if (!callee.isEmpty()) {
                this.mCallee = callee;
            }
            Log.i(this, "startConference mCallee = " + this.mCallee);
            Map<String, String> extrasMap = new HashMap<>();
            extrasMap.put(CallDetails.EXTRAS_IS_CONFERENCE_URI, Boolean.toString(true));
            String[] mMoExtras = CallDetails.getExtrasFromMap(extrasMap);
            this.mIsConferenceHostSession = true;
            CallDetails details = new CallDetails(mapCallTypeFromProfile(profile.getCallType()), 3, mMoExtras);
            this.mCi.dial(this.mCallee, null, 0, details, false, this.mHandler.obtainMessage(1, this));
        }
    }

    public void accept(int callType, ImsStreamMediaProfile profile) {
        if (isSessionValid()) {
            Log.v(this, "RTT: rttMode: " + mapRttModeFromProfile(profile.getRttMode()));
            accept(callType, this.mAnswerOptionTirConfig, profile);
        }
    }

    public void accept(int callType, int presentation, ImsStreamMediaProfile profile) {
        if (isSessionValid()) {
            Log.v(this, "RTT: rttMode: " + mapRttModeFromProfile(profile.getRttMode()));
            Log.v(this, "TIR presentation: " + presentation);
            this.mCi.acceptCall(this.mHandler.obtainMessage(2, this), mapCallTypeFromProfile(callType), ImsRadioUtilsV15.mapTirPresentationToIpPresentation(presentation), mapRttModeFromProfile(profile.getRttMode()));
        }
    }

    public void reject(int reason) {
        int reason2;
        if (isSessionValid()) {
            Log.i(this, "reject " + reason);
            boolean zIsLowBatteryVideoCall = isLowBatteryVideoCall();
            this.mIsCallTerminatedDueToLowBattery = zIsLowBatteryVideoCall;
            if (zIsLowBatteryVideoCall && !QtiImsExtUtils.allowVideoCallsInLowBattery(this.mPhoneId, this.mContext)) {
                reason2 = 505;
            } else {
                reason2 = maybeOverrideReason(reason);
            }
            this.mCi.hangupWithReason(this.mCallId, null, null, false, reason2, null, this.mHandler.obtainMessage(7, this));
        }
    }

    public boolean isCallActive() {
        return isSessionValid() && getInternalState() == DriverCallIms.State.ACTIVE;
    }

    public void deflect(String deflectNumber) {
        if (isSessionValid()) {
            this.mCi.deflectCall(this.mCallId, deflectNumber, this.mHandler.obtainMessage(16, this));
        }
    }

    public void transfer(String number, boolean isConfirmationRequired) {
        if (isSessionValid()) {
            int type = isConfirmationRequired ? 1 : 0;
            this.mCi.explicitCallTransfer(this.mCallId, type, number, 0, this.mHandler.obtainMessage(20, this));
        }
    }

    public void transfer(ImsCallSessionImplBase otherSession) {
        if (isSessionValid() && otherSession != null) {
            try {
                if (otherSession.getServiceImpl() != null) {
                    int otherCallId = Integer.parseInt(otherSession.getServiceImpl().getCallId());
                    this.mCi.explicitCallTransfer(this.mCallId, 2, null, otherCallId, this.mHandler.obtainMessage(20, this));
                }
            } catch (RemoteException e) {
                Log.i(this, "RemoteException caught = " + e);
            }
        }
    }

    public boolean hasUserMarkedCallUnwanted() {
        int ret = SystemProperties.getInt("persist.vendor.radio.debug.mark_unwanted_call", 0);
        return ret != 0;
    }

    private boolean isCallMarkedUnwanted() {
        VerstatInfo verstatInfo;
        DriverCallIms driverCallIms = this.mDc;
        return driverCallIms != null && (verstatInfo = driverCallIms.getVerstatInfo()) != null && verstatInfo.canMarkUnwantedCall() && hasUserMarkedCallUnwanted();
    }

    private int maybeOverrideReason(int reason) {
        if (isCallMarkedUnwanted()) {
            Log.i(this, "Overriden Reason : 365");
            return 365;
        }
        return reason;
    }

    private boolean isTerminateLowBatteryCall() {
        DriverCallIms driverCallIms;
        return ImsCallUtils.isCarrierOneSupported() && this.mIsLowBattery && getInternalCallType() == 3 && ((driverCallIms = this.mDc) == null || driverCallIms.state != DriverCallIms.State.DIALING) && this.mState == 1;
    }

    private boolean isLowBatteryVideoCall() {
        return this.mIsLowBattery && ImsCallUtils.isVideoCall(getInternalCallType());
    }

    public void terminate(int reason) {
        if (isSessionValid()) {
            if (isTerminateLowBatteryCall()) {
                Log.i(this, "terminate: fail deferred low battery video call with reason = " + reason);
                failDialRequest(reason);
                return;
            }
            Log.i(this, "terminate " + reason);
            if (reason == 505) {
                this.mIsCallTerminatedDueToLowBattery = true;
            } else {
                reason = maybeOverrideReason(reason);
            }
            this.mCi.hangupWithReason(this.mCallId, null, null, false, reason, null, this.mHandler.obtainMessage(3, this));
        }
    }

    public void hold(ImsStreamMediaProfile profile) {
        if (isSessionValid()) {
            Log.i(this, "hold requested.");
            this.mCi.hold(this.mHandler.obtainMessage(4, this), this.mDc.index);
        }
    }

    public void resume(ImsStreamMediaProfile profile) {
        if (isSessionValid()) {
            Log.i(this, "resume requested.");
            this.mCi.resume(this.mHandler.obtainMessage(5, this), this.mDc.index);
        }
    }

    public void merge() {
        if (isSessionValid()) {
            if (!this.mStateChangeReportingAllowed) {
                Log.w(this, "merge request is already in progress, ignore this merge request");
            } else {
                this.mTracker.sendConferenceRequest(this);
            }
        }
    }

    public void update(int callType, ImsStreamMediaProfile profile) {
        isSessionValid();
    }

    public void extendToConference(String[] participants) {
        isSessionValid();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAddParticipantsList(String[] participantsArr) {
        boolean initAdding = false;
        int numOfParticipants = participantsArr == null ? 0 : participantsArr.length;
        Log.d(this, "processAddParticipantsList: no of particpants = " + numOfParticipants + " pending = " + this.mPendingAddParticipantsList.size());
        if (numOfParticipants > 0) {
            if (this.mPendingAddParticipantsList.size() == 0) {
                initAdding = true;
            }
            for (String participant : participantsArr) {
                this.mPendingAddParticipantsList.add(participant);
            }
            if (initAdding) {
                processNextParticipant();
            }
        }
    }

    private void processNextParticipant() {
        if (this.mPendingAddParticipantsList.size() > 0) {
            inviteParticipant(this.mPendingAddParticipantsList.get(0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAddParticipantResponse(boolean success) {
        Log.d(this, "processAddParticipantResponse: success = " + success + " pending = " + (this.mPendingAddParticipantsList.size() - 1));
        if (this.mPendingAddParticipantsList.size() > 0) {
            this.mPendingAddParticipantsList.remove(0);
            processNextParticipant();
        }
    }

    public void inviteParticipants(String[] participants) {
        if (isSessionValid()) {
            if (participants == null || participants.length == 0) {
                Log.d(this, "inviteParticipants: empty participants");
            } else {
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(17, participants));
            }
        }
    }

    private void inviteParticipant(String participant) {
        Log.d(this, "inviteParticipant participant: " + participant);
        this.mCi.addParticipant(participant, 0, null, this.mHandler.obtainMessage(8, this));
    }

    public void removeParticipants(String[] participants) {
        if (isSessionValid()) {
            this.mCallee = participants[0];
            this.mCi.hangupWithReason(0, this.mCallee, null, true, 501, null, this.mHandler.obtainMessage(10, this));
        }
    }

    public void sendDtmf(char c, Message result) {
        if (isSessionValid()) {
            this.mCi.sendDtmf(c, this.mHandler.obtainMessage(19, result));
        }
    }

    public void startDtmf(char c) {
        if (isSessionValid()) {
            this.mCi.startDtmf(c, null);
        }
    }

    public void stopDtmf() {
        if (isSessionValid()) {
            this.mCi.stopDtmf(null);
        }
    }

    public ImsVideoCallProviderImpl getImsVideoCallProvider() {
        if (!isSessionValid()) {
            return null;
        }
        ImsVideoCallProviderImpl imsVideoCallProviderImpl = this.mImsVideoCallProviderImpl;
        if (imsVideoCallProviderImpl == null) {
            Log.i(this, "getImsVideoCallProvider: Video call provider is null");
            return null;
        }
        return imsVideoCallProviderImpl;
    }

    public void setCrsCrbtListener(ICrsCrbtListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        synchronized (this.mCrsCrbtListeners) {
            if (!this.mCrsCrbtListeners.contains(listener)) {
                this.mCrsCrbtListeners.add(listener);
            } else {
                Log.w(this, "setCrsCrbtListener error: Duplicate listener, " + listener);
            }
            this.mCrsCrbtListeners.forEach(new Consumer() { // from class: org.codeaurora.ims.ImsCallSessionImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ImsCallSessionImpl.this.m76lambda$setCrsCrbtListener$0$orgcodeauroraimsImsCallSessionImpl((ICrsCrbtListener) obj);
                }
            });
            this.mCrsCrbtListeners.forEach(new Consumer() { // from class: org.codeaurora.ims.ImsCallSessionImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ImsCallSessionImpl.this.m77lambda$setCrsCrbtListener$1$orgcodeauroraimsImsCallSessionImpl((ICrsCrbtListener) obj);
                }
            });
        }
    }

    /* JADX INFO: renamed from: lambda$setCrsCrbtListener$0$org-codeaurora-ims-ImsCallSessionImpl, reason: not valid java name */
    /* synthetic */ void m76lambda$setCrsCrbtListener$0$orgcodeauroraimsImsCallSessionImpl(ICrsCrbtListener crsListener) {
        try {
            crsListener.onCrsDataUpdated(this.mPhoneId, this.mDc.crsData.getCrsType(), this.mDc.isPreparatory);
        } catch (Throwable th) {
            Log.e(this, "onCrsDataUpdate exception");
        }
    }

    /* JADX INFO: renamed from: lambda$setCrsCrbtListener$1$org-codeaurora-ims-ImsCallSessionImpl, reason: not valid java name */
    /* synthetic */ void m77lambda$setCrsCrbtListener$1$orgcodeauroraimsImsCallSessionImpl(ICrsCrbtListener sipDtmfListener) {
        try {
            sipDtmfListener.onSipDtmfReceived(this.mPhoneId, this.mSipDtmfInfo);
        } catch (Throwable th) {
            Log.e(this, "onSipDtmfReceived exception");
        }
    }

    public void removeCrsCrbtListener(ICrsCrbtListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        synchronized (this.mCrsCrbtListeners) {
            if (this.mCrsCrbtListeners.contains(listener)) {
                this.mCrsCrbtListeners.remove(listener);
            } else {
                Log.w(this, "removeCrsCrbtListener error: Duplicate listener, " + listener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSipDtmfReceived(AsyncResult ar) {
        if (ar == null || ar.result == null) {
            Log.e(this, "handleSipDtmfReceived invalid info");
            return;
        }
        this.mSipDtmfInfo = (String) ar.result;
        synchronized (this.mCrsCrbtListeners) {
            this.mCrsCrbtListeners.forEach(new Consumer() { // from class: org.codeaurora.ims.ImsCallSessionImpl$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ImsCallSessionImpl.this.m75lambda$handleSipDtmfReceived$2$orgcodeauroraimsImsCallSessionImpl((ICrsCrbtListener) obj);
                }
            });
        }
    }

    /* JADX INFO: renamed from: lambda$handleSipDtmfReceived$2$org-codeaurora-ims-ImsCallSessionImpl, reason: not valid java name */
    /* synthetic */ void m75lambda$handleSipDtmfReceived$2$orgcodeauroraimsImsCallSessionImpl(ICrsCrbtListener listener) {
        try {
            listener.onSipDtmfReceived(this.mPhoneId, this.mSipDtmfInfo);
        } catch (Throwable th) {
            Log.e(this, "onSipDtmfReceived exception");
        }
    }

    public void sendSipDtmf(String requestCode) {
        if (isSessionValid()) {
            this.mCi.sendSipDtmf(requestCode, this.mHandler.obtainMessage(21));
        }
    }

    public boolean isPreparatorySession(String callId) {
        DriverCallIms driverCallIms;
        if (getCallId() == callId && (driverCallIms = this.mDc) != null) {
            return driverCallIms.isPreparatory;
        }
        return false;
    }

    private void updateCrsStatus(final DriverCallIms dcUpdate) {
        if (dcUpdate == null) {
            Log.e(this, "Null dcUpdate in updateCrsStatus");
            return;
        }
        DriverCallIms driverCallIms = this.mDc;
        if (driverCallIms == null) {
            Log.e(this, "Null mDc in updateCrsStatus");
            return;
        }
        boolean changed = driverCallIms.isPreparatory != dcUpdate.isPreparatory;
        this.mDc.isPreparatory = dcUpdate.isPreparatory;
        if (dcUpdate.crsData != null && !dcUpdate.crsData.equals(this.mDc.crsData)) {
            this.mDc.crsData.setCrsType(dcUpdate.crsData.getCrsType());
            this.mDc.crsData.setOriginalCallType(dcUpdate.crsData.getOriginalCallType());
            changed = true;
        }
        final int type = dcUpdate.crsData != null ? dcUpdate.crsData.getCrsType() : 0;
        if (changed) {
            synchronized (this.mCrsCrbtListeners) {
                this.mCrsCrbtListeners.forEach(new Consumer() { // from class: org.codeaurora.ims.ImsCallSessionImpl$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ImsCallSessionImpl.this.m78lambda$updateCrsStatus$3$orgcodeauroraimsImsCallSessionImpl(type, dcUpdate, (ICrsCrbtListener) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: renamed from: lambda$updateCrsStatus$3$org-codeaurora-ims-ImsCallSessionImpl, reason: not valid java name */
    /* synthetic */ void m78lambda$updateCrsStatus$3$orgcodeauroraimsImsCallSessionImpl(int type, DriverCallIms dcUpdate, ICrsCrbtListener listener) {
        try {
            listener.onCrsDataUpdated(this.mPhoneId, type, dcUpdate.isPreparatory);
        } catch (Throwable th) {
            Log.e(this, "onCrsDataUpdate exception");
        }
    }

    public void notifyConfInfo(byte[] confInfoBytes) {
        if (isSessionValid()) {
            this.mConfInfo.updateConfXmlBytes(confInfoBytes);
            this.mImsConferenceState = this.mConfInfo.getConfUriList();
            if (this.mCallbackHandler != null) {
                this.mCallbackHandler.callSessionConferenceStateUpdated(this.mImsConferenceState);
            }
        }
    }

    public void setConfInfo(org.codeaurora.ims.parser.ConfInfo confInfo) {
        if (isSessionValid()) {
            this.mConfInfo = confInfo;
        }
    }

    public org.codeaurora.ims.parser.ConfInfo getConfInfo() {
        if (isSessionValid()) {
            return this.mConfInfo;
        }
        return null;
    }

    @Override // org.codeaurora.ims.QImsSessionBase
    public void updateFeatureCapabilities(boolean isVideo, boolean isVoice) {
        Log.i(this, "updateFeatureCapabilities video " + isVideo + " voice " + isVoice);
        if (isSessionValid()) {
            if (this.mIsVideoAllowed != isVideo || this.mIsVoiceAllowed != isVoice) {
                this.mIsVideoAllowed = isVideo;
                this.mIsVoiceAllowed = isVoice;
                DriverCallIms driverCallIms = this.mDc;
                if (driverCallIms != null && driverCallIms.state != DriverCallIms.State.END) {
                    setCapabilitiesInProfiles(this.mDc);
                    maybeCreateVideoProvider(isVideo);
                    if (this.mStateChangeReportingAllowed) {
                        this.mCallbackHandler.callSessionUpdated(this.mCallProfile.newImsCallProfile());
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeDisplaySuppServiceErrorMsg(String header, ImsRilException ex) {
        if (isSuppSvcToastMsgEnabled()) {
            String msg = header + getSuppSvcErrorMessage(ImsCallUtils.toUiErrorCode(ex));
            Toast.makeText(this.mContext, msg, 0).show();
        }
    }

    private boolean isSuppSvcToastMsgEnabled() {
        int toastMsgEnabled = Settings.Global.getInt(this.mContext.getContentResolver(), "call.toast.supp_svc_fail", 0);
        return toastMsgEnabled == 1;
    }

    private String getSuppSvcErrorMessage(int errorCode) {
        int resId;
        switch (errorCode) {
            case 2:
                resId = R.string.error_msg_cancelled;
                break;
            case 3:
                resId = R.string.error_msg_reinvite_collision;
                break;
            default:
                resId = R.string.error_msg_failed;
                break;
        }
        return this.mContext.getResources().getString(resId);
    }

    public String toString() {
        return " callid= " + this.mCallId + " mediaId=" + getMediaId() + " mState= " + this.mState + " mDc=" + this.mDc + " mCallProfile=" + this.mCallProfile + " mLocalCallProfile=" + this.mLocalCallProfile + " mRemoteCallProfile=" + this.mRemoteCallProfile;
    }

    public String toSimpleString() {
        return super/*java.lang.Object*/.toString();
    }

    private boolean getResBoolean(String strResName, String strPackage) {
        Resources res;
        try {
            Context resCtx = this.mContext.createPackageContext(strPackage, 2);
            if (resCtx == null || (res = resCtx.getResources()) == null) {
                return false;
            }
            int resID = res.getIdentifier(strResName, "bool", strPackage);
            return res.getBoolean(resID);
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }

    private void alertForHandoverFailed() {
        boolean shouldAlert = getResBoolean("config_regional_wifi_calling_notificaion_enable", "com.android.settings");
        if (shouldAlert) {
            startAlert();
        }
    }

    private void startAlert() {
        if (this.mWifiAlertHandler == null) {
            this.mWifiAlertHandler = new Handler() { // from class: org.codeaurora.ims.ImsCallSessionImpl.1
                private int numberOfAlerts = 0;

                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            if (this.numberOfAlerts != 3) {
                                removeMessages(2);
                                sendEmptyMessageDelayed(2, ImsCallSessionImpl.RESET_TIME);
                                ImsCallSessionImpl.this.startBeepForAlert();
                                this.numberOfAlerts++;
                                break;
                            }
                            break;
                        case 2:
                            this.numberOfAlerts = 0;
                            break;
                    }
                }
            };
        }
        this.mWifiAlertHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v0, types: [org.codeaurora.ims.ImsCallSessionImpl$2] */
    public void startBeepForAlert() {
        new Thread() { // from class: org.codeaurora.ims.ImsCallSessionImpl.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                ToneGenerator mTone = new ToneGenerator(0, 40);
                try {
                    try {
                        mTone.startTone(25);
                        Thread.sleep(1000L);
                        mTone.stopTone();
                    } catch (Exception e) {
                        Log.i(this, "Exception caught when generator sleep " + e);
                    }
                } finally {
                    mTone.release();
                }
            }
        }.start();
        Toast.makeText(this.mContext, "Due to network conditions, the call may be dropped", 1).show();
    }

    private boolean isConfigEnabled(int resId) {
        return this.mContext.getResources().getBoolean(resId);
    }

    private void maybeUpdateVoWifiCallQualityExtra(int quality, boolean canNotify) {
        if (!QtiImsExtUtils.isVoWiFiCallQualityEnabled(this.mPhoneId, this.mContext) || getState() != 4) {
            return;
        }
        Log.d(this, "maybeUpdateVoWifiCallQualityExtra Quality : " + quality);
        this.mCallProfile.setCallExtraInt("VoWiFiCallQuality", quality);
        if (canNotify) {
            maybeTriggerCallSessionUpdate(this.mDc);
        }
    }

    public void updateVoWiFiCallQuality(int quality) {
        if (quality != this.mVoWifiQuality) {
            this.mVoWifiQuality = quality;
            maybeUpdateVoWifiCallQualityExtra(quality, true);
        } else {
            Log.v(this, "updateVoWiFiCallQuality Unchanged : " + quality);
        }
    }

    public void sendRttMessage(String rttMessage) {
        if (!isCallActive()) {
            Log.e(this, "RTT: sendRttMessage not allowed.");
        } else {
            this.mCi.sendRttMessage(rttMessage, this.mHandler.obtainMessage(13, this));
        }
    }

    public void sendRttModifyRequest(ImsCallProfile toProfile) {
        if (isSessionValid()) {
            CallDetails details = new CallDetails(mapCallTypeFromProfile(toProfile.getCallType()), 3, null);
            details.setRttMode(toProfile.getMediaProfile().getRttMode());
            Log.v(this, "RTT: sendRttModifyRequest mode = " + toProfile.getMediaProfile().getRttMode());
            if (toProfile.getMediaProfile().getRttMode() != 1 && toProfile.getMediaProfile().getRttMode() != 0) {
                throw new IllegalArgumentException("mRttMode is invalid.");
            }
            this.mCi.modifyCallInitiate(this.mHandler.obtainMessage(14, this), new CallModify(details, this.mCallId));
        }
    }

    public void sendRttModifyResponse(boolean response) {
        if (isSessionValid()) {
            CallDetails callDetails = new CallDetails(getInternalCallType(), getCallDomain(), null);
            CallModify callModify = new CallModify();
            callModify.call_index = Integer.parseInt(getCallId());
            callModify.call_details = new CallDetails(callDetails);
            Log.v(this, "RTT: sendRttModifyResponse response = " + response);
            callModify.call_details.setRttMode(mapResponseToMode(response));
            this.mCi.modifyCallConfirm(this.mHandler.obtainMessage(15, this), callModify);
        }
    }

    private int mapRttModeFromProfile(int mode) {
        int rttMode = 0;
        switch (mode) {
            case 0:
                rttMode = 0;
                break;
            case 1:
                rttMode = 1;
                break;
        }
        Log.v(this, "RTT: mapRttModeFromProfile mode = " + rttMode);
        return rttMode;
    }

    private int mapResponseToMode(boolean z) {
        return z ? 1 : 0;
    }

    public int getMergeStatus() {
        return this.mMergeStatus;
    }

    private int getRttMode(int callType, ServiceStatus[] list) {
        int mode = 0;
        if (list != null) {
            for (ServiceStatus srv : list) {
                if (srv != null && srv.type == callType) {
                    mode = srv.rttMode;
                    break;
                }
            }
        }
        Log.v(this, "RTT: getRttMode rtt mode = " + mode);
        return mode;
    }

    public void notifyRttModifyRequest(CallDetails callDetails) {
        if (isSessionValid()) {
            if (this.mCallbackHandler == null) {
                Log.e(this, "RTT: notifyRttModifyRequest ListenerProxy null");
                return;
            }
            Log.v(this, "RTT: notifyRttModifyRequest rttMode = " + callDetails.getRttMode());
            ImsCallProfile profile = new ImsCallProfile();
            profile.getMediaProfile().setRttMode(callDetails.getRttMode());
            this.mCallbackHandler.callSessionRttModifyRequestReceived(profile);
        }
    }

    public void notifyReceivedRttMessage(Object result) {
        if (!isCallActive()) {
            Log.e(this, "RTT: notifyReceivedRttMessage not allowed.");
            return;
        }
        String message = (String) result;
        if (message == null) {
            Log.d(this, "notifyReceivedRttMessage rtt msg null");
        } else if (this.mCallbackHandler == null) {
            Log.e(this, "notifyReceivedRttMessage ListenerProxy null");
        } else {
            Log.v(this, "RTT: notifyReceivedRttMessage rttMessage = " + message);
            this.mCallbackHandler.callSessionRttMessageReceived(message);
        }
    }

    public void notifyRttModifyResponse(AsyncResult ar) {
        int status;
        if (isSessionValid()) {
            if (ar != null && ar.exception != null) {
                Log.e(this, "RTT: modify request exception: " + ar.exception);
                status = ImsCallUtils.getUiErrorCode(ar.exception);
            } else {
                status = 1;
            }
            if (this.mCallbackHandler == null) {
                Log.e(this, "notifyRttModifyResponse ListenerProxy null");
            } else {
                Log.v(this, "RTT: notifyRttModifyResponse status = " + status);
                this.mCallbackHandler.callSessionRttModifyResponseReceived(status);
            }
        }
    }

    public void notifyVoiceInfoChanged(int voiceInfo) {
        if (!isSessionValid() || !isCallActive() || this.mCallbackHandler == null) {
            Log.e(this, "notifyVoiceInfoChanged Session invalid/not active/mCallbackHandler null Return");
            return;
        }
        ImsStreamMediaProfile mediaProfile = ImsMediaUtils.newImsStreamMediaProfile();
        mediaProfile.setReceivingRttAudio(voiceInfo == 1);
        Log.v(this, "RTT: notifyVoiceInfoChanged voiceInfo = " + voiceInfo);
        this.mCallbackHandler.callSessionRttAudioIndicatorChanged(mediaProfile);
    }

    public void setImsVideoCallProviderImpl(ImsVideoCallProviderImpl videoCallProvider) {
        this.mImsVideoCallProviderImpl = videoCallProvider;
    }

    public Handler getHandler() {
        return this.mHandler;
    }
}
