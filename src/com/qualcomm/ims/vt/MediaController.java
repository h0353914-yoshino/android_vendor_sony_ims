package com.qualcomm.ims.vt;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.telecom.VideoProfile;
import android.util.Log;
import android.view.Surface;
import org.codeaurora.ims.CallModify;
import org.codeaurora.ims.ICallListListener;
import org.codeaurora.ims.ImsCallSessionImpl;
import org.codeaurora.ims.ImsCallUtils;
import org.codeaurora.ims.ImsUssdSessionImpl;
import org.codeaurora.ims.QImsSessionBase;
import org.codeaurora.ims.QtiVideoCallDataUsage;
import org.codeaurora.ims.R;
import org.codeaurora.ims.utils.QtiImsExtUtils;

/* JADX INFO: loaded from: classes.dex */
public class MediaController implements ImsMedia.CameraListener, ImsMedia.IMediaListener, ICallListListener, QImsSessionBase.Listener {
    private static String TAG = "VideoCall_MediaController";
    private static MediaController sInstance;
    private Context mContext;
    private ImsMedia mMedia;
    private ImsMedia.IMediaListener mMediaEventListener;
    private PauseImageTask mPauseImageTask;
    private int mNumberOfImsCallSessions = 0;
    private boolean mIsPauseImageMode = false;

    private MediaController(Context context, ImsMedia media) {
        this.mContext = context;
        this.mMedia = media;
        media.setMediaListener(this);
    }

    public static synchronized void init(Context context, ImsMedia media) {
        if (sInstance == null) {
            sInstance = new MediaController(context, media);
        } else {
            throw new RuntimeException("MediaController: Multiple initialization");
        }
    }

    public static synchronized MediaController getInstance() {
        MediaController mediaController = sInstance;
        if (mediaController != null) {
            return mediaController;
        }
        loge("getInstance sInstance= null");
        return sInstance;
    }

    public void dispose() {
        log("dispose()");
        synchronized (MediaController.class) {
            maybeDeInitializeMedia(null);
            this.mMedia.setMediaListener(null);
            sInstance = null;
        }
    }

    public void setMediaEventListener(ImsMedia.IMediaListener listener) {
        this.mMediaEventListener = listener;
    }

    public void updateMergeStatus(int status) {
        log("updateMergeStatus status = " + status);
        this.mMedia.updateMergeStatus(status);
    }

    public void setSharedDisplayParams(int width, int height) {
        log("setSharedDisplayParams width = " + width + " height = " + height);
        this.mMedia.setSharedDisplayParams(width, height);
    }

    public void stopScreenShare() {
        log("stopScreenShare");
        this.mMedia.stopScreenShare();
    }

    public void setDisplaySurface(Surface surface) {
        this.mMedia.setSurface(surface);
    }

    public void setDeviceOrientation(int rotation) {
        this.mMedia.sendCvoInfo(CvoUtil.toOrientation(rotation));
    }

    public void requestCallDataUsage(int mediaId) {
        log("requestCallDataUsage: mediaID = " + mediaId);
        this.mMedia.requestCallDataUsage(mediaId);
    }

    private class PauseImageTask extends AsyncTask<String, Void, Bitmap> {
        int height;
        int width;

        PauseImageTask(int w, int h) {
            this.width = w;
            this.height = h;
        }

        private Bitmap reconfigureAndDecodeImage(String uri) {
            Bitmap b;
            if (uri.isEmpty()) {
                b = QtiImsExtUtils.decodeImage(MediaController.this.mContext.getResources(), R.drawable.img_no_image, this.width, this.height);
            } else {
                b = QtiImsExtUtils.decodeImage(uri, MediaController.this.mContext, this.width, this.height);
            }
            if (b != null) {
                b.reconfigure(this.width, this.height, b.getConfig());
                MediaController.log("setPreviewImageAsync reconfigureAndDecodeImage bitmap b = " + b + " width = " + b.getWidth() + " height = " + b.getHeight());
            }
            return b;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(String... params) {
            String uri = params[0];
            MediaController.log("setPreviewImageAsync: width = " + this.width + " height = " + this.height + " uri = " + uri);
            if (isCancelled()) {
                MediaController.log("setPreviewImageAsync: PauseImageTask is cancelled");
                return null;
            }
            return reconfigureAndDecodeImage(uri);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            MediaController.log("setPreviewImageAsync bitmap = " + bitmap);
            if (bitmap == null) {
                return;
            }
            MediaController.this.setPauseImage(bitmap);
        }
    }

    public void setPauseImage(String uri) {
        PauseImageTask pauseImageTask = this.mPauseImageTask;
        if (pauseImageTask != null && pauseImageTask.getStatus() != AsyncTask.Status.FINISHED) {
            boolean isCancelled = this.mPauseImageTask.cancel(true);
            log("setPauseImage cancelling old pauseImageTask isCancelled = " + isCancelled);
        }
        if (uri == null) {
            setPauseImage((Bitmap) null);
        } else {
            PauseImageTask pauseImageTask2 = new PauseImageTask(this.mMedia.getNegotiatedWidth(), this.mMedia.getNegotiatedHeight());
            this.mPauseImageTask = pauseImageTask2;
            pauseImageTask2.execute(uri);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPauseImage(Bitmap bitmap) {
        synchronized (this) {
            boolean isPauseImageMode = this.mIsPauseImageMode;
            if (this.mMedia.setPreviewImage(bitmap)) {
                isPauseImageMode = bitmap != null;
            }
            log("setPauseImage isPauseImageMode = " + isPauseImageMode + " mIsPauseImageMode = " + this.mIsPauseImageMode);
            if (this.mIsPauseImageMode == isPauseImageMode) {
                return;
            }
            this.mIsPauseImageMode = isPauseImageMode;
            if (isPauseImageMode) {
                this.mMedia.addCameraListener(this);
            } else {
                this.mMedia.removeCameraListener(this);
            }
        }
    }

    private boolean isPauseImageState() {
        boolean z;
        synchronized (this) {
            z = this.mIsPauseImageMode;
        }
        return z;
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.CameraListener
    public void onCameraConfigChanged(int w, int h, int fps, Surface surface, int orientationMode) {
        if (!isPauseImageState()) {
            logw("received camera config changed when not in pause image mode");
            return;
        }
        ImsVideoCallProviderImpl provider = ImsVideoGlobals.getInstance().getActiveOrBackgroundCallVideoProvider();
        if (provider == null) {
            logw("onCameraConfigChanged: ImsVideoCallProvider is not available");
            return;
        }
        VideoProfile.CameraCapabilities cc = new VideoProfile.CameraCapabilities(w, h);
        log("onCameraConfigChanged in hide me mode New capabilities = " + cc);
        provider.changeCameraCapabilities(cc);
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.CameraListener
    public void onUpdateRecorderFrameRate(int rate) {
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.CameraListener
    public void onRecordingEnabled() {
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.CameraListener
    public void onRecordingDisabled() {
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.IMediaListener
    public void onOrientationModeChanged(int orientationMode) {
        log("onOrientationModeChanged to orientation mode- " + orientationMode);
        ImsVideoCallProviderImpl videoProvider = ImsVideoGlobals.getInstance().getIncomingCallVideoProvider();
        if (videoProvider == null) {
            videoProvider = ImsVideoGlobals.getInstance().getActiveOrBackgroundCallVideoProvider();
        }
        if (videoProvider == null) {
            videoProvider = ImsVideoGlobals.getInstance().getOutgoingCallVideoProvider();
        }
        if (videoProvider != null) {
            videoProvider.onUpdateOrientationMode(orientationMode);
        } else {
            logw("Call session video provider is null. Can't propagate onOrientationModeChanged event");
        }
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.IMediaListener
    public void onRecordingSurfaceChanged(Surface recordingSurface, int width, int height) {
        log("onRecordingSurfaceChanged to surface- " + recordingSurface + " width- " + width + " height- " + height);
        ImsVideoCallProviderImpl videoProvider = ImsVideoGlobals.getInstance().getActiveCallVideoProvider();
        if (videoProvider != null) {
            videoProvider.onUpdateRecordingSurface(recordingSurface, width, height);
        } else {
            logw("Call session video provider is null. Can't propagate onRecordingSurfaceChanged event");
        }
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.IMediaListener
    public void onPeerResolutionChanged(int width, int height) {
        log("onPeerResolutionChangeEvent width=" + width + " height=" + height);
        ImsVideoCallProviderImpl videoProvider = ImsVideoGlobals.getInstance().getActiveOrOutgoingCallVideoProvider();
        if (videoProvider != null) {
            videoProvider.onUpdatePeerDimensions(width, height);
        } else {
            logw("Active or Outgoing  call session video provider is null. Can't propagate OnPeerResolutionChanged event");
        }
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.IMediaListener
    public void onPlayerStateChanged(int state) {
        int status;
        log("onPlayerStateChanged state = " + state);
        ImsVideoGlobals imsVideoGlobals = ImsVideoGlobals.getInstance();
        ImsVideoCallProviderImpl videoProvider = imsVideoGlobals.getActiveOrOutgoingCallVideoProvider();
        if (videoProvider == null && (videoProvider = imsVideoGlobals.getBackgroundCallVideoProvider()) == null) {
            videoProvider = imsVideoGlobals.getIncomingCallVideoProvider();
        }
        if (videoProvider != null) {
            if (state == 0) {
                status = 2;
            } else {
                status = 1;
            }
            videoProvider.handleCallSessionEvent(status);
            return;
        }
        logw("All call session video providers are null. Can't propagate onPlayerStateChanged event");
    }

    private int convertVideoQuality(int videoQuality) {
        switch (videoQuality) {
            case 0:
                return 3;
            case 1:
                return 2;
            case 2:
                return 1;
            default:
                return 4;
        }
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.IMediaListener
    public void onVideoQualityEvent(int videoQuality) {
        ImsVideoCallProviderImpl videoProvider = ImsVideoGlobals.getInstance().getActiveCallVideoProvider();
        if (videoProvider != null) {
            videoProvider.onUpdateVideoQuality(convertVideoQuality(videoQuality));
        } else {
            logw("Active call session video provider is null. Can't propagate OnVideoQualityChanged event");
        }
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.IMediaListener
    public void onDataUsageChanged(int mediaId, QtiVideoCallDataUsage dataUsage) {
        ImsVideoCallProviderImpl videoProvider = ImsVideoGlobals.getInstance().findVideoCallProviderbyMediaId(mediaId);
        if (videoProvider != null) {
            log("onDataUsageChanged dataUsage = " + dataUsage);
            videoProvider.onCallDataUsageChanged(dataUsage.getLteDataUsage());
            ImsCallSessionImpl callSession = videoProvider.getCallSession();
            if (callSession != null) {
                callSession.updateVideoCallDataUsageInfo(dataUsage);
                return;
            } else {
                loge("onDataUsageChanged: call session is null");
                return;
            }
        }
        loge("onDataUsageChanged: Call session video provider is null. Received mediaId = " + mediaId);
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionAdded(ImsCallSessionImpl callSession) {
        log("onSessionAdded callSession=" + callSession + " no:of call sessions = " + this.mNumberOfImsCallSessions);
        this.mNumberOfImsCallSessions++;
        callSession.addListener(this);
        if (ImsCallUtils.isVideoCall(callSession)) {
            maybeInitializeMedia();
        }
    }

    private void maybeInitializeMedia() {
        if (!isMediaInitialized()) {
            log("maybeInitializeMedia: Initializing media");
            this.mMedia.init();
        }
    }

    private void maybeDeInitializeMedia(ImsCallSessionImpl callSession) {
        if (isMediaInitialized()) {
            log("maybeInitializeMedia: De-initializing media");
            if (callSession != null && QtiImsExtUtils.shallTransmitStaticImage(callSession.getPhoneId(), this.mContext)) {
                setPauseImage((Bitmap) null);
            }
            this.mMedia.deInit();
        }
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionRemoved(ImsCallSessionImpl callSession) {
        log("onSessionRemoved callSession=" + callSession + " no:of call sessions = " + this.mNumberOfImsCallSessions);
        int i = this.mNumberOfImsCallSessions;
        if (i == 0) {
            loge("onCallSessionRemoved: Unknown session has been removed, Session=" + callSession);
            return;
        }
        this.mNumberOfImsCallSessions = i - 1;
        callSession.removeListener(this);
        if (this.mNumberOfImsCallSessions == 0) {
            maybeDeInitializeMedia(callSession);
        }
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionAdded(ImsUssdSessionImpl ussdSession) {
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionRemoved(ImsUssdSessionImpl ussdSession) {
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onDisconnected(QImsSessionBase session) {
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onClosed(QImsSessionBase session) {
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onUnsolCallModify(QImsSessionBase session, CallModify callModify) {
        if (ImsCallUtils.isVideoCall(callModify)) {
            maybeInitializeMedia();
        }
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onCallModifyInitiated(QImsSessionBase session, CallModify callModify) {
        if (ImsCallUtils.isVideoCall(callModify)) {
            maybeInitializeMedia();
        }
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onCallTypeChanging(QImsSessionBase session, int newCallType) {
        if (!ImsCallUtils.isVideoCall((ImsCallSessionImpl) session) && ImsCallUtils.isVideoCall(newCallType)) {
            maybeInitializeMedia();
        }
    }

    private boolean isMediaInitialized() {
        ImsMedia imsMedia = this.mMedia;
        return imsMedia != null && imsMedia.isMediaInitialized();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }

    private static void logw(String msg) {
        Log.w(TAG, msg);
    }
}
