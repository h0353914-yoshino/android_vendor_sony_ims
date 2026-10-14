package com.qualcomm.ims.vt;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.telecom.VideoProfile;
import android.util.Size;
import android.view.Surface;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.ims.CallModify;
import org.codeaurora.ims.ImsCallSessionImpl;
import org.codeaurora.ims.QImsSessionBase;

/* JADX INFO: loaded from: classes.dex */
public class CameraController implements ImsMedia.CameraListener, QImsSessionBase.Listener, Camera.Listener, ImsMedia.MediaStateListener {
    private static String TAG = "CameraController";
    private static boolean mIsRecordingStarted = false;
    private static CameraController sInstance;
    private Camera mCamera;
    private Camera.ConfigIms mCameraConfigIms;
    private ImsCallSessionImpl mCameraOwner;
    private Context mContext;
    private boolean mIsRecordingEnabled = false;
    private ImsMedia mMedia;
    private Surface mPreviewSurface;
    private int mRecorderFrameRate;
    private Surface mRecordingSurface;
    private final Object mSyncObject;

    private CameraController(Context context, ImsMedia media) {
        Camera.ConfigIms configIms = new Camera.ConfigIms(ImsMediaConstants.DEFAULT_WIDTH, ImsMediaConstants.DEFAULT_HEIGHT, 20, 2);
        this.mCameraConfigIms = configIms;
        this.mRecorderFrameRate = configIms.getFps();
        this.mSyncObject = new Object();
        this.mContext = context;
        this.mMedia = media;
        media.addCameraListener(this);
        this.mMedia.addMediaStateListener(this);
        CameraManager.init(context);
    }

    private void doOpen(String cameraId, ImsCallSessionImpl session) throws CameraAccessException {
        Camera.ConfigIms configIms;
        Log.i(this, "doOpen mCamera =" + this.mCamera + " cameraId=" + cameraId + " Session=" + getSessionInfo(session));
        releaseCurrentOwner();
        Camera camera = this.mCamera;
        if (camera != null && !camera.getId().equals(cameraId)) {
            closeCamera();
        }
        boolean reOpen = this.mCamera == null;
        if (reOpen) {
            this.mCamera = CameraManager.open(cameraId, this);
        }
        setOwner(session);
        if (reOpen && (configIms = this.mCameraConfigIms) != null) {
            this.mCamera.reconfigure(configIms);
            this.mCamera.setRecorderFrameRate(this.mRecorderFrameRate);
        }
        int mountAngle = this.mCamera.getSensorOrientation();
        int cameraFacing = this.mCamera.isFacingFront() ? 1 : 0;
        this.mMedia.setCameraInfo(cameraFacing, mountAngle);
    }

    private void closeCamera() {
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.close();
        }
        this.mCamera = null;
    }

    public static synchronized CameraController init(Context context, ImsMedia media) {
        CameraController cameraController;
        if (sInstance == null) {
            cameraController = new CameraController(context, media);
            sInstance = cameraController;
        } else {
            throw new RuntimeException("CameraController: Multiple initialization");
        }
        return cameraController;
    }

    public void dispose() {
        Log.i(this, "dispose()");
        synchronized (CameraController.class) {
            this.mMedia.removeCameraListener(this);
            this.mMedia.removeMediaStateListener(this);
            sInstance = null;
        }
    }

    public static synchronized CameraController getInstance() {
        CameraController cameraController;
        cameraController = sInstance;
        if (cameraController == null) {
            throw new RuntimeException("CameraController: Not initialized");
        }
        return cameraController;
    }

    public void open(String cameraId, ImsCallSessionImpl session) {
        Log.i(this, "open: cameraId=" + cameraId + " session=" + getSessionInfo(session));
        if (cameraId == null) {
            Log.e(this, "open: Invalid camera id, " + cameraId);
            throw new RuntimeException("CameraController: Invalid camera id.");
        }
        try {
            doOpen(cameraId, session);
        } catch (CameraAccessException e) {
            Log.e(this, "open: Failed to open, cameraId=" + cameraId + " Exception=" + e);
            sendCameraStatus(session, e.getReason());
        }
    }

    public void close(ImsCallSessionImpl session) {
        Log.i(this, "close");
        doClose(session);
    }

    public void setPreviewSurface(Surface surface, ImsCallSessionImpl session) {
        Camera camera;
        synchronized (this.mSyncObject) {
            Log.i(this, "setPreviewSurface, surface=" + surface + " Session=" + getSessionInfo(session));
            if (!isOwner(session)) {
                Log.i(this, "setPreviewSurface: " + getSessionInfo(session) + " is not a camera owner. Camera owner=" + getSessionInfo(this.mCameraOwner));
                return;
            }
            // 由 JADX 反編譯修正：依 smali，try 涵蓋到最後一次 updateCameraSession()
            try {
                if (surface != null) {
                    Surface surface2 = this.mPreviewSurface;
                    if ((surface2 != null && !ImsMedia.haveSameParent(surface2, surface)) || ((camera = this.mCamera) != null && (camera.isRestartNeededOnPreviewSurfaceSizeChanged(surface) || this.mCamera.isRestartNeededOnRecordingSurfaceSizeChanged(this.mRecordingSurface)))) {
                        Log.i(this, "setPreviewSurface: Restarting camera preview...");
                        this.mPreviewSurface = null;
                        updateCameraSession();
                    }
                }
                this.mPreviewSurface = surface;
                updateCameraSession();
            } catch (CameraAccessException e) {
                sendCameraStatus(e.getReason());
            }
        }
    }

    public void setZoom(float value) {
        Camera camera = this.mCamera;
        if (camera == null || !camera.isOpen()) {
            Log.w(this, "setZoom: Camera is not in correct state, camera=" + this.mCamera);
            return;
        }
        try {
            float maxZoom = this.mCamera.getMaxZoom();
            float minZoom = this.mCamera.getMinZoom();
            Log.v(this, "setZoom: maxZoom=" + maxZoom + " minZoom=" + minZoom + " value=" + value);
            this.mCamera.setZoom((((maxZoom - minZoom) * value) / 100.0f) + minZoom);
        } catch (CameraAccessException e) {
            Log.v(this, "Failed to change zoom, value=" + value + "Exception=" + e);
        }
    }

    private Size getPreviewSize() throws CameraAccessException {
        int mountAngle = this.mCamera.getSensorOrientation();
        Size previewSize = this.mCamera.getPreviewSize();
        Log.v(this, "getPreviewSize mountAngle = " + mountAngle + " previewSize = " + previewSize);
        int width = previewSize.getWidth();
        int height = previewSize.getHeight();
        boolean isPortrait = previewSize.getWidth() < previewSize.getHeight();
        if (!isPortrait && mountAngle % 180 != 0) {
            return new Size(height, width);
        }
        if (isPortrait && mountAngle % 180 == 0) {
            return new Size(width, height);
        }
        if (isPortrait) {
            return new Size(height, width);
        }
        if (!isPortrait) {
            return new Size(width, height);
        }
        return previewSize;
    }

    VideoProfile.CameraCapabilities getCameraCapabilities() {
        Log.v(this, "getCameraCapabilities mCamera=" + this.mCamera);
        if (this.mCamera == null) {
            return null;
        }
        try {
            Size size = getPreviewSize();
            Boolean isZoomSupported = Boolean.valueOf(this.mCamera.isZoomSupported());
            float maxZoom = this.mCamera.getMaxZoom();
            Log.i(this, "getCameraCapabilities: PreviewSize=" + size + " isZoomSupported=" + isZoomSupported + " maxZoom=" + maxZoom);
            return new VideoProfile.CameraCapabilities(size.getWidth(), size.getHeight(), isZoomSupported.booleanValue(), maxZoom);
        } catch (CameraAccessException e) {
            sendCameraStatus(e.getReason());
            return null;
        }
    }

    private void setOwner(ImsCallSessionImpl session) {
        synchronized (this.mSyncObject) {
            Log.i(this, "setOwner: Session=" + getSessionInfo(session));
            if (!isOwner(session)) {
                releaseCurrentOwner();
                this.mCameraOwner = session;
                session.addListener(this);
            } else {
                Log.i(this, "setOwner: The session is already registered as camera owner, session=" + session);
            }
        }
    }

    private void removeOwner(ImsCallSessionImpl session) {
        synchronized (this.mSyncObject) {
            Log.i(this, "removeOwner: Session=" + getSessionInfo(session));
            if (isOwner(session)) {
                releaseCurrentOwner();
            } else {
                Log.w(this, "removeOwner: The session is not the correct owner. cameraOwner=" + getSessionInfo(session));
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:10:0x000d  */
    public boolean isOwner(ImsCallSessionImpl session) {
        boolean z;
        synchronized (this.mSyncObject) {
            if (session == null) {
                z = false;
            } else {
                try {
                    if (session == this.mCameraOwner) {
                        z = true;
                    } else {
                        z = false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return z;
    }

    private void releaseCurrentOwner() {
        synchronized (this.mSyncObject) {
            Log.i(this, "releaseCurrentOwner: Session=" + getSessionInfo(this.mCameraOwner));
            ImsCallSessionImpl imsCallSessionImpl = this.mCameraOwner;
            if (imsCallSessionImpl != null) {
                imsCallSessionImpl.removeListener(this);
                this.mCameraOwner = null;
            }
        }
    }

    private void doClose(ImsCallSessionImpl session) {
        Log.i(this, "doClose: Camera=" + this.mCamera + " Session=" + getSessionInfo(session));
        synchronized (this.mSyncObject) {
            removeOwner(session);
            if (this.mCameraOwner == null) {
                closeCamera();
                this.mPreviewSurface = null;
            } else {
                Log.w(this, "doClose: Not closing camera=" + this.mCamera + " another session still requires it, session=" + getSessionInfo(this.mCameraOwner));
            }
        }
    }

    private void updateCameraSession() throws CameraAccessException {
        Log.i(this, "updateCameraSession");
        Camera camera = this.mCamera;
        if (camera == null || !camera.isOpen()) {
            Log.e(this, "updateCameraSession: Camera is not in correct state, camera=" + this.mCamera);
            return;
        }
        boolean isPreviewStarted = this.mCamera.isPreviewStarted();
        boolean isRecordingStarted = this.mCamera.isRecordingStarted();
        boolean canStartPreview = this.mPreviewSurface != null;
        boolean canStartRecording = this.mIsRecordingEnabled && canStartPreview;
        Log.v(this, "updateCameraSession mPreviewSurface=" + this.mPreviewSurface + " mIsRecordingEnabled=" + this.mIsRecordingEnabled + " mRecordingSurface=" + this.mRecordingSurface);
        Log.v(this, "updateCameraSession canStartRecording=" + canStartRecording + " isRecordingStarted=" + isRecordingStarted);
        if (canStartRecording && !isRecordingStarted) {
            this.mCamera.startRecording(this.mPreviewSurface, this.mRecordingSurface);
            return;
        }
        if (canStartPreview && !isPreviewStarted) {
            this.mCamera.startPreview(this.mPreviewSurface);
        } else if (!canStartPreview && isPreviewStarted) {
            this.mCamera.stopPreview();
        }
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.CameraListener
    public void onRecordingEnabled() {
        Log.i(this, "onRecordingEnabled");
        try {
            this.mIsRecordingEnabled = true;
            updateCameraSession();
        } catch (CameraAccessException e) {
            sendCameraStatus(e.getReason());
        }
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.CameraListener
    public void onRecordingDisabled() {
        Log.i(this, "onStopReadyEvent");
        this.mIsRecordingEnabled = false;
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.MediaStateListener
    public void onMediaDeinitialized() {
        Log.i(this, "onMediaDeinitialized. closing Camera");
        ImsCallSessionImpl imsCallSessionImpl = this.mCameraOwner;
        if (imsCallSessionImpl != null) {
            close(imsCallSessionImpl);
        }
        closeCamera();
        this.mIsRecordingEnabled = false;
        this.mRecordingSurface = null;
        this.mPreviewSurface = null;
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.MediaStateListener
    public void onMediaInitialized() {
    }

    public Surface getRecordingSurface() {
        return this.mRecordingSurface;
    }

    public Surface getPreviewSurface() {
        return this.mPreviewSurface;
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.CameraListener
    public void onCameraConfigChanged(int w, int h, int fps, Surface surface, int orientationMode) {
        this.mCameraConfigIms = new Camera.ConfigIms(w, h, fps, orientationMode);
        this.mRecorderFrameRate = fps;
        Surface prevRecordingSurface = this.mRecordingSurface;
        this.mRecordingSurface = surface;
        Log.i(this, "onCameraConfigChanged, cameraConfig=" + this.mCameraConfigIms);
        Camera camera = this.mCamera;
        if (camera == null || !camera.isOpen()) {
            Log.i(this, "onCameraConfigChanged: Camera is not open deferring configuration...");
            return;
        }
        try {
            if (!this.mCamera.canReconfigureWithActiveSession(this.mCameraConfigIms) && this.mCamera.isPreviewStarted()) {
                this.mCamera.stopPreview();
            } else if (this.mCamera.isRecordingStarted() && !ImsMedia.haveSameParent(this.mRecordingSurface, prevRecordingSurface)) {
                Log.i(this, "onCameraConfigChanged, recording surface changed. Stop recording");
                this.mCamera.stopRecording();
            }
            this.mCamera.reconfigure(this.mCameraConfigIms);
            onUpdateRecorderFrameRate(this.mRecorderFrameRate);
            updateCameraSession();
            sendCameraCapabilities();
        } catch (CameraAccessException e) {
            sendCameraStatus(e.getReason());
        }
    }

    @Override // com.qualcomm.ims.vt.ImsMedia.CameraListener
    public void onUpdateRecorderFrameRate(int rate) {
        Log.i(this, "onUpdateRecorderFrameRate, rate=" + rate);
        this.mRecorderFrameRate = rate;
        Camera camera = this.mCamera;
        if (camera == null || !camera.isOpen()) {
            Log.i(this, "onUpdateRecorderFrameRate: Camera is not open deferring...");
            return;
        }
        try {
            this.mCamera.setRecorderFrameRate(rate);
        } catch (CameraAccessException e) {
            sendCameraStatus(e.getReason());
        }
    }

    private void sendCameraCapabilities() {
        ImsCallSessionImpl imsCallSessionImpl;
        VideoProfile.CameraCapabilities cc = getCameraCapabilities();
        if (cc == null || (imsCallSessionImpl = this.mCameraOwner) == null) {
            Log.e(this, "sendCameraCapabilities: Camera capabilities or camera owner is null");
            return;
        }
        ImsVideoCallProviderImpl provider = imsCallSessionImpl.getImsVideoCallProviderImpl();
        if (provider == null) {
            Log.e(this, "sendCameraCapabilities: ImsVideoCallProvider is not available (null).");
        } else {
            Log.i(this, "sendCameraCapabilities: New capabilities= " + cc);
            provider.changeCameraCapabilities(cc);
        }
    }

    private String getSessionInfo(ImsCallSessionImpl session) {
        if (session == null) {
            return null;
        }
        return session.toSimpleString();
    }

    private void sendCameraStatus(ImsCallSessionImpl session, int error) {
        if (session == null) {
            Log.e(this, "sendCameraStatus: session is null. CameraStatus=" + error);
            return;
        }
        ImsVideoCallProviderImpl provider = session.getImsVideoCallProviderImpl();
        if (provider == null) {
            Log.e(this, "sendCameraStatus: ImsVideoCallProvider is not available (null).");
        } else {
            Log.v(this, "sendCameraStatus: Notifying Session=" + session.getCallId());
            provider.sendCameraStatus(true);
        }
    }

    private void sendCameraStatus(int error) {
        synchronized (this.mSyncObject) {
            ImsCallSessionImpl imsCallSessionImpl = this.mCameraOwner;
            if (imsCallSessionImpl != null) {
                sendCameraStatus(imsCallSessionImpl, error);
            }
        }
    }

    @Override // com.qualcomm.ims.vt.Camera.Listener
    public void onError(Camera camera, int error) {
        Log.e(this, "CameraFailed: cameraId=" + camera.getId() + " Error=" + error);
        sendCameraStatus(error);
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onDisconnected(QImsSessionBase session) {
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onClosed(QImsSessionBase session) {
        ImsCallSessionImpl callSession = (ImsCallSessionImpl) session;
        Log.i(this, "onClosed: Session=" + getSessionInfo(callSession));
        close(callSession);
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onUnsolCallModify(QImsSessionBase session, CallModify callModify) {
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onCallModifyInitiated(QImsSessionBase session, CallModify callModify) {
    }

    @Override // org.codeaurora.ims.QImsSessionBase.Listener
    public void onCallTypeChanging(QImsSessionBase session, int newCallType) {
    }
}
