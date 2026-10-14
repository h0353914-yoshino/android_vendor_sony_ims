package com.qualcomm.ims.vt;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.VideoProfile;
import android.telephony.ims.ImsVideoCallProvider;
import android.util.Log;
import android.view.Surface;
import org.codeaurora.ims.CallModify;
import org.codeaurora.ims.ImsCallModification;
import org.codeaurora.ims.ImsCallSessionImpl;
import org.codeaurora.ims.ImsCallUtils;
import org.codeaurora.ims.QImsSessionBase;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsVideoCallProviderImpl extends ImsVideoCallProvider implements QImsSessionBase.Listener {
    private static final boolean DBG = true;
    private static final int EVENT_SEND_SESSION_MODIFY_CALL_CONFIRM_DONE = 1;
    private static final int EVENT_SEND_SESSION_MODIFY_REQUEST_DONE = 0;
    private static final String TAG = "VideoCall_ImsVideoCallProviderImpl";
    private ImsCallSessionImpl mCallSession;
    private CameraController mCamera;
    private Handler mHandler;
    private ImsCallModification mImsCallModification;
    private boolean mIsOpen;
    private MediaController mMedia;
    VideoProfile mRequestProfile;
    VideoProfile mResponseProfile;

    public ImsVideoCallProviderImpl(ImsCallSessionImpl callSession, ImsCallModification imsCallMod) {
        log("ImsVideocallProviderImpl instance created callSession=" + callSession + " imsCallMod=" + imsCallMod);
        this.mCallSession = callSession;
        this.mCamera = CameraController.getInstance();
        this.mMedia = MediaController.getInstance();
        this.mImsCallModification = imsCallMod;
        this.mIsOpen = DBG;
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.qualcomm.ims.vt.ImsVideoCallProviderImpl.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                ImsVideoCallProviderImpl.this.log("Message received: what = " + msg.what);
                switch (msg.what) {
                    case 0:
                        ImsVideoCallProviderImpl.this.handleSessionModifyDone(msg);
                        break;
                    case 1:
                        ImsVideoCallProviderImpl.this.handleSessionModifyConfirmDone(msg);
                        break;
                    default:
                        ImsVideoCallProviderImpl.this.log("Unknown message = " + msg.what);
                        break;
                }
            }
        };
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void updateMergeStatus(int status) {
        log("updateMergeStatus = " + status);
        this.mMedia.updateMergeStatus(status);
    }

    public void setSharedDisplayParams(int width, int height) {
        log("setSharedDisplayParams: width = " + width + " height = " + height);
        this.mMedia.setSharedDisplayParams(width, height);
    }

    public void stopScreenShare() {
        log("stopScreenShare");
        this.mMedia.stopScreenShare();
    }

    boolean isOpen() {
        log(" isOpen " + this.mIsOpen);
        return this.mIsOpen;
    }

    public void onSetCamera(String cameraId) {
        log("onSetCamera, " + cameraId);
        if (isSessionValid()) {
            if (cameraId != null) {
                this.mCamera.open(cameraId, getCallSession());
            } else {
                this.mCamera.close(getCallSession());
            }
        }
    }

    public void onSetPreviewSurface(Surface surface) {
        log("onSetPreviewSurface, " + surface);
        if (isSessionValid()) {
            this.mCamera.setPreviewSurface(surface, getCallSession());
        }
    }

    public void onSetDisplaySurface(Surface surface) {
        log("onSetDisplaySurface, " + surface);
        if (isSessionValid()) {
            this.mMedia.setDisplaySurface(surface);
        }
    }

    public void onSetDeviceOrientation(int rotation) {
        log("onSetDeviceOrientation, " + rotation);
        if (isSessionValid()) {
            this.mMedia.setDeviceOrientation(rotation);
        }
    }

    public void onSetZoom(float value) {
        log("onSetZoom, " + value);
        if (isSessionValid()) {
            this.mCamera.setZoom(value);
        }
    }

    public void onSendSessionModifyRequest(VideoProfile fromProfile, VideoProfile toProfile) {
        log("onSendSessionModifyRequest, videoState=" + toProfile.getVideoState() + " quality= " + toProfile.getQuality());
        this.mRequestProfile = toProfile;
        if (isSessionValid()) {
            if (isVideoPauseRequested(toProfile)) {
                this.mImsCallModification.changeConnectionType(null, 6, null);
            } else {
                if (this.mImsCallModification.isVideoPauseRequested()) {
                    this.mImsCallModification.changeConnectionType(null, 7, null);
                    return;
                }
                Message newMsg = this.mHandler.obtainMessage(0);
                int callType = ImsCallUtils.convertVideoStateToCallType(toProfile.getVideoState());
                this.mImsCallModification.changeConnectionType(newMsg, callType, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSessionModifyConfirmDone(Message msg) {
        log("handleSessionModifyConfirmDone msg.what=" + msg.what);
        AsyncResult ar = (AsyncResult) msg.obj;
        int status = 2;
        VideoProfile responseProfile = null;
        if (ar != null) {
            responseProfile = (VideoProfile) ar.userObj;
            if (ar.exception == null) {
                log("Session modify confirm success");
                status = 1;
                this.mRequestProfile = responseProfile;
            } else {
                loge("Session modify confirm error");
                status = ImsCallUtils.getUiErrorCode(ar.exception);
                responseProfile = ImsCallUtils.convertToVideoProfile(this.mImsCallModification.mImsCallSessionImpl.getInternalCallType(), 4);
            }
        } else {
            loge("handleSessionModifyConfirmDone: null message object");
        }
        receiveSessionModifyResponse(status, this.mRequestProfile, responseProfile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSessionModifyDone(Message msg) {
        log("handleSessionModifyDone msg.what=" + msg.what);
        AsyncResult ar = (AsyncResult) msg.obj;
        int status = 2;
        if (ar != null) {
            if (ar.exception == null) {
                log("Session modify success");
                status = 1;
                this.mResponseProfile = this.mRequestProfile;
            } else {
                loge("Session modify error");
                status = ImsCallUtils.getUiErrorCode(ar.exception);
                this.mResponseProfile = ImsCallUtils.convertToVideoProfile(this.mImsCallModification.mImsCallSessionImpl.getInternalCallType(), 4);
            }
        } else {
            loge("handleSessionModifyDone:null message object");
        }
        receiveSessionModifyResponse(status, this.mRequestProfile, this.mResponseProfile);
    }

    private boolean isVideoPauseRequested(VideoProfile requestProfile) {
        log("isVideoPauseRequested requestProfile=" + requestProfile);
        return VideoProfile.isPaused(requestProfile.getVideoState());
    }

    public void onSendSessionModifyResponse(VideoProfile responseProfile) {
        log("onSendSessionModifyResponse, responseProfile state=" + responseProfile.getVideoState() + " quality= " + responseProfile.getQuality());
        if (isSessionValid()) {
            int callType = ImsCallUtils.convertVideoStateToCallType(responseProfile.getVideoState());
            Message newMsg = this.mHandler.obtainMessage(1, responseProfile);
            this.mImsCallModification.acceptConnectionTypeChange(callType, null, newMsg);
        }
    }

    public void onRequestCameraCapabilities() {
        log("onRequestCameraCapabilities");
        if (isSessionValid()) {
            VideoProfile.CameraCapabilities cc = this.mCamera.getCameraCapabilities();
            if (cc != null) {
                changeCameraCapabilities(cc);
            } else {
                loge("Error onRequestCameraCapabilities camera capabilities is null");
            }
        }
    }

    public void onCallDataUsageChanged(long dataUsage) {
        log("onCallDataUsageChanged: dataUsage = " + dataUsage);
        changeCallDataUsage((int) dataUsage);
    }

    public void onUpdatePeerDimensions(int width, int height) {
        log("onUpdatePeerDimensions width=" + width + " height= " + height);
        if (isSessionValid()) {
            changePeerDimensions(width, height);
        }
    }

    public void sendCameraStatus(boolean hasFailed) {
        log("sendCameraFailure");
        int status = hasFailed ? 5 : 6;
        handleCallSessionEvent(status);
    }

    public void onUpdateVideoQuality(int videoQuality) {
        log("onUpdateVideoQuality video quality is=" + videoQuality);
        if (isSessionValid()) {
            changeVideoQuality(videoQuality);
        }
    }

    public void onUpdateOrientationMode(int orientationMode) {
        log("onUpdateOrientationMode orientation mode = " + orientationMode);
        if (isSessionValid()) {
            this.mCallSession.updateOrientationMode(orientationMode);
        }
    }

    public void onUpdateRecordingSurface(Surface recordingSurface, int width, int height) {
        log("onUpdateRecordingSurface recording surface = " + recordingSurface);
        if (isSessionValid()) {
            this.mCallSession.updateRecordingSurface(recordingSurface, width, height);
        }
    }

    public void onRequestCallDataUsage() {
        log("onRequestCallDataUsage");
        if (isSessionValid()) {
            int mediaId = this.mCallSession.getMediaId();
            if (this.mCallSession.hasMediaIdValid()) {
                this.mMedia.requestCallDataUsage(mediaId);
            } else {
                loge("onRequestCallDataUsage: Invalid MediaId = " + mediaId);
            }
        }
    }

    public void onSetPauseImage(Uri uri) {
        log("onSetPauseImage, " + uri);
        if (isSessionValid()) {
            this.mMedia.setPauseImage(uri != null ? uri.toString() : null);
        }
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onDisconnected(QImsSessionBase session) {
        log("onDisconnected session" + session);
        this.mIsOpen = false;
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onClosed(QImsSessionBase session) {
        this.mIsOpen = false;
        this.mMedia = null;
        this.mCallSession = null;
        this.mCamera = null;
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onUnsolCallModify(QImsSessionBase session, CallModify callModify) {
        log("onUnsolCallModify session=" + session + " callModify= " + callModify);
        int newVideoState = ImsCallUtils.convertCallTypeToVideoState(callModify.call_details.call_type);
        VideoProfile vcp = new VideoProfile(newVideoState, 4);
        this.mRequestProfile = vcp;
        if (callModify.error()) {
            int uiError = ImsCallUtils.convertImsErrorToUiError(callModify.error);
            receiveSessionModifyResponse(uiError, vcp, null);
        } else {
            receiveSessionModifyRequest(vcp);
        }
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onCallModifyInitiated(QImsSessionBase session, CallModify callModify) {
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onCallTypeChanging(QImsSessionBase session, int newCallType) {
    }

    ImsCallSessionImpl getCallSession() {
        return this.mCallSession;
    }

    private String logString(String msg) {
        StringBuilder sbAppend = new StringBuilder().append("(");
        ImsCallSessionImpl imsCallSessionImpl = this.mCallSession;
        return sbAppend.append(imsCallSessionImpl != null ? imsCallSessionImpl.getCallId() : "null").append(") ").append(msg).toString();
    }

    private boolean isSessionValid() {
        boolean isValid = this.mIsOpen;
        if (!this.mIsOpen) {
            loge("Session is closed." + this);
        }
        return isValid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String msg) {
        Log.d(TAG, logString(msg));
    }

    private void loge(String msg) {
        Log.e(TAG, logString(msg));
    }
}
