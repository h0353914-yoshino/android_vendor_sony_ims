package org.codeaurora.ims;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.VideoProfile;
import android.telephony.ims.ImsCallProfile;
import com.qualcomm.ims.utils.Log;
import com.qualcomm.ims.vt.LowBatteryHandler;
import java.util.Map;
import org.codeaurora.ims.utils.QtiImsExtUtils;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsCallModification {
    static final int EVENT_AVP_UPGRADE_DONE = 5;
    static final int EVENT_MODIFY_CALL_CONFIRM_DONE = 8;
    static final int EVENT_MODIFY_CALL_INITIATE_DONE = 6;
    static final int EVENT_VIDEO_PAUSE_DONE = 7;
    static final int EVENT_VIDEO_PAUSE_RETRY = 9;
    private static final int MAX_MULTITASK_RETRIES = 1;
    static final int PAUSE_DELAY_MILLIS = 3000;
    private int mAvpCallType;
    private boolean mAvpRetryAllowed;
    private CallModify mCallModifyRequest;
    private ImsSenderRxr mCi;
    private Context mContext;
    Handler mHandler;
    public ImsCallSessionImpl mImsCallSessionImpl;
    public int mIndex;
    private boolean mIsLocallyPaused;
    private boolean mIsVideoPauseRequested;
    private int mMultiTaskRetryCount;
    private PauseState mPendingVTMultitask;
    private int mPrevVideoCallType;

    public enum PauseState {
        NONE,
        PAUSE,
        RESUME
    }

    public boolean isLocallyPaused() {
        return this.mIsLocallyPaused;
    }

    public ImsCallModification(ImsCallSessionImpl imsCallSessionImpl, Context context, ImsSenderRxr senderRxr) {
        this(imsCallSessionImpl, context, senderRxr, Looper.getMainLooper());
    }

    public ImsCallModification(ImsCallSessionImpl imsCallSessionImpl, Context context, ImsSenderRxr senderRxr, Looper looper) {
        this.mCallModifyRequest = null;
        this.mIndex = -1;
        this.mPrevVideoCallType = 10;
        this.mAvpRetryAllowed = true;
        this.mAvpCallType = 10;
        this.mMultiTaskRetryCount = 0;
        this.mPendingVTMultitask = PauseState.NONE;
        this.mIsLocallyPaused = false;
        this.mIsVideoPauseRequested = false;
        Log.v(this, "ImsCallModification instance created imsCallSessionImpl=" + imsCallSessionImpl);
        this.mCi = senderRxr;
        this.mContext = context;
        this.mImsCallSessionImpl = imsCallSessionImpl;
        this.mHandler = new ImsCallModificationHandler(looper);
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    private boolean isVTMultitaskRequest(int callType) {
        return callType == 6 || callType == 7;
    }

    private void triggerOrQueueVTMultitask(int callType) {
        Log.i(this, "triggerOrQueueVTMultitask callType= " + callType + " conn= " + this);
        boolean isPauseRequested = callType == 6;
        this.mPendingVTMultitask = isPauseRequested ? PauseState.PAUSE : PauseState.RESUME;
        this.mIsVideoPauseRequested = isPauseRequested;
        if (this.mCallModifyRequest == null) {
            processPendingVTMultitask();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createAndSendMultiTaskRequest(int rilCallType) {
        this.mHandler.removeMessages(9);
        Message message = this.mHandler.obtainMessage(7);
        modifyCallInitiate(message, rilCallType, null);
    }

    private int pendingPauseStatetoRilCallType() {
        if (this.mPendingVTMultitask == PauseState.PAUSE) {
            return 4;
        }
        if (this.mPendingVTMultitask != PauseState.RESUME) {
            return 10;
        }
        int rilCallType = this.mPrevVideoCallType;
        return rilCallType;
    }

    private boolean areCallTypesSame(int newCallType) {
        return newCallType == this.mImsCallSessionImpl.getInternalCallType();
    }

    private boolean isIndexValid() {
        int index = Integer.parseInt(this.mImsCallSessionImpl.getCallId());
        return index >= 0;
    }

    private boolean validateOutgoingModifyConnectionType(Message msg, int newCallType) {
        Log.i(this, "validateOutgoingModifyConnectionType newCallType=" + newCallType);
        boolean modifyToCurrCallType = areCallTypesSame(newCallType);
        boolean isIndexValid = isIndexValid();
        boolean isLowBattery = LowBatteryHandler.getInstance().isLowBattery();
        boolean isRttCallModificationAllowed = canModifyRttCallType(newCallType);
        boolean isValid = isIndexValid && !modifyToCurrCallType && isRttCallModificationAllowed;
        if (!ImsCallUtils.isCarrierOneSupported()) {
            isValid &= !isLowBattery;
        }
        Log.i(this, "validateOutgoingModifyConnectionType modifyToCurrCallType = " + modifyToCurrCallType + " isIndexValid = " + isIndexValid + " isLowBattery = " + isLowBattery + " isRttCallModificationAllowed = " + isRttCallModificationAllowed);
        if (msg != null && !isValid) {
            Exception exception = null;
            if (isLowBattery) {
                exception = new ImsRilException(50, "Low Battery");
            } else if (!modifyToCurrCallType) {
                exception = new Exception("Can't modify call.");
            }
            msg.obj = AsyncResult.forMessage(msg, (Object) null, exception);
            msg.sendToTarget();
        }
        return isValid;
    }

    private boolean validateIncomingModifyConnectionType(int newCallType) {
        Log.i(this, "validateIncomingModifyConnectionType newCallType = " + newCallType);
        boolean modifyToCurrCallType = areCallTypesSame(newCallType);
        boolean isIndexValid = isIndexValid();
        boolean isLowBattery = false;
        if (!QtiImsExtUtils.allowVideoCallsInLowBattery(this.mImsCallSessionImpl.getPhoneId(), this.mContext)) {
            isLowBattery = LowBatteryHandler.getInstance().isLowBattery();
        }
        boolean isVideoCRBT = false;
        if (this.mImsCallSessionImpl.getInternalState() == DriverCallIms.State.ALERTING && newCallType == 2 && QtiImsExtUtils.isCarrierConfigEnabled(this.mImsCallSessionImpl.getPhoneId(), this.mContext, "config_enable_video_crbt")) {
            Log.i(this, "validateIncomingModifyConnectionType isVideoCRBT = true");
            isVideoCRBT = true;
        }
        boolean isValid = (isLowBattery || !isIndexValid || modifyToCurrCallType || isVideoCRBT) ? false : true;
        Log.i(this, "validateIncomingModifyConnectionType modifyToCurrCallType = " + modifyToCurrCallType + " isIndexValid = " + isIndexValid + " isLowBattery = " + isLowBattery);
        return isValid;
    }

    public static boolean hasError(CallModify callModify) {
        return callModify != null && callModify.error();
    }

    public void onReceivedModifyCall(CallModify callModify) {
        boolean ret;
        Log.i(this, "onReceivedCallModify(" + callModify + ")");
        boolean rttChanged = false;
        if (callModify.error()) {
            CallModify callModify2 = this.mCallModifyRequest;
            if (callModify2 != null && callModify2.call_index == callModify.call_index && this.mCallModifyRequest.call_details.call_type == callModify.call_details.call_type && this.mCallModifyRequest.call_details.call_domain == callModify.call_details.call_domain && !this.mCallModifyRequest.error()) {
                this.mCallModifyRequest.error = callModify.error;
                ret = true;
            } else {
                Log.e(this, "onReceivedModifyCall: Call Modify request not found.Dropping the Modify Call Request Failed. Cached Request: " + this.mCallModifyRequest + ", Received Request:" + callModify);
                ret = false;
            }
        } else {
            rttChanged = isUpgradeRequestForRtt(callModify.call_details);
            ret = validateIncomingModifyConnectionType(callModify.call_details.call_type);
            if (this.mCallModifyRequest == null) {
                this.mCallModifyRequest = callModify;
            } else {
                Log.e(this, "videocall onReceivedModifyCall: not notifying user about incoming modify call request as there is pending callModifyRequest=" + this.mCallModifyRequest);
                ret = false;
            }
            boolean callModified = ret || rttChanged;
            if (!callModified) {
                rejectConnectionTypeChange(null);
            }
        }
        boolean isLowBattery = LowBatteryHandler.getInstance().isLowBattery();
        if (isLowBattery && !ImsCallUtils.isCarrierOneSupported()) {
            callModify.error = 50;
        }
        if (rttChanged) {
            if (QtiImsExtUtils.isRttAutoUpgradeSupported(this.mImsCallSessionImpl.getPhoneId(), this.mContext)) {
                this.mImsCallSessionImpl.sendRttModifyResponse(true);
                clearPendingModify();
                return;
            } else {
                this.mImsCallSessionImpl.notifyRttModifyRequest(callModify.call_details);
                clearPendingModify();
            }
        } else {
            Log.d(this, "RTT: onReceivedModifyCall: No RTT modification");
        }
        if (ret || isLowBattery) {
            this.mImsCallSessionImpl.notifyUnsolCallModify(callModify);
        } else {
            Log.d(this, "onReceivedModifyCall: No notify for CallModify ");
        }
        if (hasError(callModify)) {
            clearPendingModify();
        }
    }

    private boolean isOldAndNewPauseRequestSame() {
        if (this.mCallModifyRequest != null) {
            Log.e(this, "isOldAndNewPauseRequestSame Unexpectedly callModifyRequest:" + this.mCallModifyRequest);
            return false;
        }
        boolean ret = (this.mIsLocallyPaused && this.mPendingVTMultitask == PauseState.PAUSE) || (!this.mIsLocallyPaused && this.mPendingVTMultitask == PauseState.RESUME);
        Log.i(this, "isOldAndNewPauseRequestSame " + ret);
        return ret;
    }

    private void clearPendingVTMultiTask() {
        Log.i(this, "clearPendingVTMultiTask imsconn=" + this);
        this.mPendingVTMultitask = PauseState.NONE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldRetryVideoPause() {
        return this.mMultiTaskRetryCount <= 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPendingVTMultitask() {
        Log.i(this, "processPendingVTMultitask mPendingVTMultitask=" + this.mPendingVTMultitask);
        if (isOldAndNewPauseRequestSame()) {
            Log.i(this, "Old and new Pause Request is Same so clearing Pending VT multitask");
            clearPendingVTMultiTask();
        } else if (this.mPendingVTMultitask != PauseState.NONE) {
            if (this.mCallModifyRequest == null) {
                createAndSendMultiTaskRequest(pendingPauseStatetoRilCallType());
                clearPendingVTMultiTask();
            } else {
                Log.e(this, "processPendingVTMultitask callModifyRequest not null");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearPendingModify() {
        Log.i(this, "clearPendingModify imsconn=" + this);
        this.mCallModifyRequest = null;
    }

    public CallModify getPendingModify() {
        return this.mCallModifyRequest;
    }

    public void setPendingModify(CallModify callModify) {
        this.mCallModifyRequest = callModify;
    }

    public int getPrevVideoCallType() {
        return this.mPrevVideoCallType;
    }

    public int getMultiTaskRetryCount() {
        return this.mMultiTaskRetryCount;
    }

    public void setMultiTaskRetryCount(int multiTaskRetryCount) {
        this.mMultiTaskRetryCount = multiTaskRetryCount;
    }

    public PauseState getPendingVTMultitask() {
        return this.mPendingVTMultitask;
    }

    public boolean getIsVideoPauseRequested() {
        return this.mIsVideoPauseRequested;
    }

    public void setIsLocallyPaused(boolean isLocallyPaused) {
        this.mIsLocallyPaused = isLocallyPaused;
    }

    public boolean isAvpRetryAllowed() {
        return this.mAvpRetryAllowed;
    }

    public void update(DriverCallIms dc) {
        Log.i(this, "update dc=" + dc);
        if (ImsCallUtils.isActive(dc) && ImsCallUtils.isVideoCallTypeWithDir(dc.callDetails.call_type)) {
            this.mAvpRetryAllowed = false;
        }
        if (!ImsCallUtils.isVideoCall(dc.callDetails.call_type)) {
            Log.i(this, "videocall: update: Not a videocall CASE");
            clearPendingVTMultiTask();
            this.mMultiTaskRetryCount = 0;
            this.mHandler.removeMessages(9);
        }
        boolean isAvpRetryDialingCase = ImsCallUtils.isAvpRetryDialing(this.mImsCallSessionImpl, dc);
        if (isAvpRetryDialingCase) {
            this.mAvpCallType = this.mImsCallSessionImpl.getInternalCallType();
        }
        boolean isAvpRetryUpgradeCase = ImsCallUtils.isAvpRetryUpgrade(this.mImsCallSessionImpl, dc);
        if (isAvpRetryDialingCase || isAvpRetryUpgradeCase) {
            Log.i(this, "videocall AVP RETRY CASE dc= " + dc + " conn= " + this);
            retryAvpUpgrade(this.mImsCallSessionImpl.getInternalState() == DriverCallIms.State.ACTIVE);
        } else if (ImsCallUtils.isVideoPaused(this.mImsCallSessionImpl, dc)) {
            Log.i(this, "videocall Video Paused CASE");
        } else if (ImsCallUtils.isVideoResumed(this.mImsCallSessionImpl, dc)) {
            Log.i(this, "videocall Video Resumed CASE");
        }
        updatePreviousVTCallType(dc.callDetails.call_type);
        new CallDetails(dc.callDetails);
        if (!ImsCallUtils.canVideoPause(this.mImsCallSessionImpl)) {
            this.mIsLocallyPaused = false;
            this.mIsVideoPauseRequested = false;
        }
    }

    private void notifyVideoPaused(int videoState) {
        VideoProfile vcp = new VideoProfile(videoState, 4);
        this.mImsCallSessionImpl.getImsVideoCallProviderImpl().receiveSessionModifyRequest(vcp);
    }

    private void retryAvpUpgrade(boolean shouldNotifyUser) {
        Log.i(this, "retryAvpUpgrade: AVPF failed so retrying using AVP. mAvpCallType=" + this.mAvpCallType + " shouldNotifyUser=" + shouldNotifyUser);
        this.mIndex = Integer.parseInt(this.mImsCallSessionImpl.getCallId());
        Message msg = this.mHandler.obtainMessage(5, Boolean.valueOf(shouldNotifyUser));
        modifyCallInitiate(msg, this.mAvpCallType, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePreviousVTCallType(int callType) {
        if (callType == 3 || callType == 1 || callType == 2) {
            this.mPrevVideoCallType = callType;
            Log.i(this, "Updating mPrevVideoCallType to " + this.mPrevVideoCallType);
        }
    }

    public void changeConnectionType(Message msg, int newCallType, Map<String, String> newExtras) {
        Log.i(this, "changeConnectionType  newCallType=" + newCallType + " newExtras= " + newExtras);
        this.mIndex = Integer.parseInt(this.mImsCallSessionImpl.getCallId());
        if (isVTMultitaskRequest(newCallType)) {
            triggerOrQueueVTMultitask(newCallType);
            return;
        }
        if (isAvpRetryAllowed() && ImsCallUtils.isVideoCallTypeWithDir(newCallType)) {
            this.mAvpCallType = newCallType;
        }
        Message newMsg = this.mHandler.obtainMessage(6, msg);
        if (this.mCallModifyRequest == null) {
            if (validateOutgoingModifyConnectionType(newMsg, newCallType)) {
                modifyCallInitiate(newMsg, newCallType, newExtras);
            }
        } else {
            Log.e(this, "videocall changeConnectionType: not invoking modifyCallInitiate as there is pending callModifyRequest=" + this.mCallModifyRequest);
            sendErrorResponse(msg, "Pending callModifyRequest so not sending modify request down");
        }
    }

    private void modifyCallInitiate(Message newMsg, int newCallType, Map<String, String> newExtras) {
        if (!ImsCallUtils.isValidRilModifyCallType(newCallType)) {
            Log.e(this, "modifyCallInitiate not a Valid RilCallType" + newCallType);
            return;
        }
        CallDetails callDetails = new CallDetails(newCallType, this.mImsCallSessionImpl.getCallDomain(), CallDetails.getExtrasFromMap(newExtras));
        ImsCallProfile callProfile = this.mImsCallSessionImpl.getCallProfile();
        if (callProfile != null) {
            callDetails.setRttMode(callProfile.getMediaProfile().getRttMode());
        }
        CallModify callModify = new CallModify(callDetails, this.mIndex);
        if (this.mCallModifyRequest != null) {
            Log.i(this, "Overwriting callModifyRequest: " + this.mCallModifyRequest + " with callModify:" + callModify);
        }
        this.mCallModifyRequest = callModify;
        this.mImsCallSessionImpl.notifyCallModifyInitiate(callModify);
        this.mCi.modifyCallInitiate(newMsg, callModify);
    }

    private boolean isValidCallModifyConfirmRequest(int callType) {
        if (this.mCallModifyRequest == null) {
            Log.e(this, "callModifyRequest is null");
            return false;
        }
        if ((this.mImsCallSessionImpl.getInternalCallType() != 0 || this.mCallModifyRequest.call_details.call_type != 3 || (callType != 2 && callType != 1 && callType != 3 && callType != 0)) && callType != this.mCallModifyRequest.call_details.call_type) {
            return false;
        }
        return true;
    }

    public void acceptConnectionTypeChange(int callType, Map<String, String> newExtras, Message msg) {
        Log.i(this, "Confirming call type change request: " + this.mCallModifyRequest);
        if (!isValidCallModifyConfirmRequest(callType)) {
            Log.e(this, "acceptConnectionTypeChange: MODIFY_CALL_CONFIRM with invalid calltype " + callType);
            rejectConnectionTypeChange(msg);
            return;
        }
        CallModify callModify = this.mCallModifyRequest;
        if (callModify != null) {
            callModify.call_details.call_type = callType;
            if (newExtras != null) {
                this.mCallModifyRequest.call_details.setExtrasFromMap(newExtras);
            }
            Message newMsg = this.mHandler.obtainMessage(8, msg);
            this.mCi.modifyCallConfirm(newMsg, this.mCallModifyRequest);
        }
    }

    private void sendErrorResponse(Message msg, String errStr) {
        RuntimeException exception = new RuntimeException(errStr);
        if (msg != null) {
            msg.obj = AsyncResult.forMessage(msg, (Object) null, exception);
            msg.sendToTarget();
        }
    }

    public void rejectConnectionTypeChange(Message msg) {
        CallModify callModify = this.mCallModifyRequest;
        if (callModify == null) {
            Log.e(this, "rejectConnectionTypeChange callModifyRequest is null");
            sendErrorResponse(msg, "no existing modify request");
            return;
        }
        if (callModify.error()) {
            Log.i(this, "rejectConnectionTypeChange callModifyRequest timed out.");
            clearPendingModify();
            sendErrorResponse(msg, "modify request timed out");
            return;
        }
        CallDetails callDetails = new CallDetails(this.mImsCallSessionImpl.getInternalCallType(), this.mImsCallSessionImpl.getCallDomain(), null);
        CallModify callModify2 = new CallModify();
        callModify2.call_index = Integer.parseInt(this.mImsCallSessionImpl.getCallId());
        callModify2.call_details = new CallDetails(callDetails);
        Log.i(this, "Rejecting Change request: " + this.mCallModifyRequest + " keep as " + callModify2);
        Message newMsg = this.mHandler.obtainMessage(8, msg);
        this.mCi.modifyCallConfirm(newMsg, callModify2);
    }

    class ImsCallModificationHandler extends Handler {
        public ImsCallModificationHandler() {
        }

        public ImsCallModificationHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 5:
                    onAvpRetry(msg);
                    break;
                case 6:
                case 8:
                    onModifyCallDone(msg);
                    break;
                case 7:
                    onVideoPauseDone(msg);
                    break;
                case 9:
                    onVideoPauseRetry();
                    break;
            }
        }

        private void onModifyCallDone(Message msg) {
            Log.i(this, "onModifyCallDone for event: " + msg.what);
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar != null) {
                if (ar.exception == null && ImsCallModification.this.mCallModifyRequest != null) {
                    ImsCallModification imsCallModification = ImsCallModification.this;
                    imsCallModification.updatePreviousVTCallType(imsCallModification.mCallModifyRequest.call_details.call_type);
                } else {
                    Log.e(this, "videocall error during modifyCall");
                }
            }
            Message onComplete = (Message) ar.userObj;
            if (onComplete != null) {
                AsyncResult.forMessage(onComplete, ar.result, ar.exception);
                onComplete.sendToTarget();
            }
            ImsCallModification.this.clearPendingModify();
            ImsCallModification.this.processPendingVTMultitask();
        }

        private void onVideoPauseDone(Message msg) {
            Log.i(this, "EVENT_VIDEO_PAUSE_DONE received");
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar != null) {
                if (ar.exception == null) {
                    if (ImsCallModification.this.mCallModifyRequest != null) {
                        ImsCallModification imsCallModification = ImsCallModification.this;
                        imsCallModification.mIsLocallyPaused = imsCallModification.mCallModifyRequest.call_details.call_type == 4;
                    }
                    ImsCallModification.this.clearPendingModify();
                    clearMultiTaskRetryCount();
                    ImsCallModification.this.processPendingVTMultitask();
                    return;
                }
                if (ImsCallModification.this.shouldRetryVideoPause()) {
                    Log.e(this, "Error during video pause so retry");
                    ImsCallModification.this.mHandler.sendMessageDelayed(ImsCallModification.this.mHandler.obtainMessage(9), 3000L);
                    ImsCallModification.this.mMultiTaskRetryCount++;
                    return;
                }
                Log.i(this, "Video Pause retry limit reached.");
                clearMultiTaskRetryCount();
                ImsCallModification.this.clearPendingModify();
                ImsCallModification.this.processPendingVTMultitask();
                return;
            }
            Log.e(this, "Error EVENT_VIDEO_PAUSE_DONE ar is null");
        }

        private void onVideoPauseRetry() {
            Log.i(this, "EVENT_VIDEO_PAUSE_RETRY received mMultiTaskRetryCount=" + ImsCallModification.this.mMultiTaskRetryCount);
            if (ImsCallModification.this.mPendingVTMultitask == PauseState.NONE) {
                if (ImsCallModification.this.mCallModifyRequest != null) {
                    ImsCallModification imsCallModification = ImsCallModification.this;
                    imsCallModification.createAndSendMultiTaskRequest(imsCallModification.mCallModifyRequest.call_details.call_type);
                    return;
                }
                return;
            }
            Log.i(this, "User pressed home/resume during retry sending new multitask request");
            ImsCallModification.this.clearPendingModify();
            clearMultiTaskRetryCount();
            ImsCallModification.this.processPendingVTMultitask();
        }

        private void onAvpRetry(Message msg) {
            Log.i(this, "EVENT_AVP_UPGRADE received");
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar != null && ar.exception != null && (ar.userObj instanceof Boolean)) {
                boolean shouldNotifyUser = ((Boolean) ar.userObj).booleanValue();
                if (shouldNotifyUser) {
                    Log.e(this, "AVP Retry error when Voice call was upgraded to video call");
                } else {
                    Log.e(this, "AVP Retry error when Video call was dialed");
                }
            }
            ImsCallModification.this.clearPendingModify();
            ImsCallModification.this.processPendingVTMultitask();
        }

        private int clearMultiTaskRetryCount() {
            Log.i(this, "Clearing MultiTaskRetryCount from " + ImsCallModification.this.mMultiTaskRetryCount + " to 0");
            ImsCallModification.this.mMultiTaskRetryCount = 0;
            return 0;
        }
    }

    public boolean isVideoPauseRequested() {
        return this.mIsVideoPauseRequested;
    }

    private boolean isUpgradeRequestForRtt(CallDetails callDetails) {
        ImsCallProfile callProfile = this.mImsCallSessionImpl.getCallProfile();
        if (callDetails == null || callProfile.getMediaProfile().isRttCall()) {
            return false;
        }
        Log.i(this, "RTT: isUpgradeRequestForRtt rttMode = " + callDetails.rttMode);
        return callDetails.rttMode == 1 && isIndexValid();
    }

    private boolean canModifyRttCallType(int toCallType) {
        int fromCallType = this.mImsCallSessionImpl.getInternalCallType();
        boolean isVideoRttSupported = QtiImsExtUtils.isRttSupportedOnVtCalls(this.mImsCallSessionImpl.getPhoneId(), this.mContext);
        boolean isRttCall = this.mImsCallSessionImpl.getCallProfile().getMediaProfile().isRttCall();
        boolean isVideoUpgradeRequest = !VideoProfile.isVideo(fromCallType) && VideoProfile.isVideo(toCallType);
        Log.d(this, "fromCallType: " + fromCallType + ", isRttCall: " + isRttCall + ", isVideoRttSupported: " + isVideoRttSupported + ", isVideoUpgradeRequest" + isVideoUpgradeRequest);
        return (isVideoUpgradeRequest && isRttCall && !isVideoRttSupported) ? false : true;
    }

    public void close() {
        this.mHandler.removeMessages(9);
        CallModify callModify = this.mCallModifyRequest;
        if (callModify != null) {
            callModify.error = CallModify.E_CANCELLED;
            Log.i(this, "Cancel pending call modification before being closed");
            this.mImsCallSessionImpl.notifyUnsolCallModify(this.mCallModifyRequest);
            clearPendingModify();
        }
    }
}
