package com.qualcomm.ims.vt;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import com.qualcomm.ims.utils.Config;
import com.qualcomm.ims.utils.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.codeaurora.ims.R;

/* JADX INFO: loaded from: classes.dex */
public class Camera2 extends Camera {
    public static final CaptureRequest.Key<Byte> AV_TIMER = new CaptureRequest.Key<>("org.codeaurora.qcamera3.av_timer.use_av_timer", Byte.TYPE);
    public static final int CAMERA_REQUEST_START_NONE = 0;
    public static final int CAMERA_REQUEST_START_PREVIEW = 1;
    public static final int CAMERA_REQUEST_START_RECORDING = 2;
    public static final int CAMERA_STATE_CLOSED = 0;
    public static final int CAMERA_STATE_OPEN = 2;
    public static final int CAMERA_STATE_OPENING = 1;
    public static final byte ENABLE_AV_TIMER = 1;
    private static final int GENERIC_CONSTANT_INVALID_VALUE = -1;
    private static final String TAG = "Camera2";
    private static final int ZOOM_CONSTANT_NOT_SUPPORTED = 1;
    private CameraActionSoundHelper mActionSoundHelper;
    private Camera.ConfigIms mCameraConfigIms;
    private CameraDevice mCameraDevice;
    private int mCameraFacing;
    private android.hardware.camera2.CameraManager mCameraManager;
    private CameraCaptureSession mCameraSession;
    private CaptureRequest.Builder mCaptureBuilder;
    private Rect mCropRegion;
    private Range<Integer> mFps;
    private int mFrameSkippingRate;
    private boolean mIsCreateSessionPending;
    private boolean mIsPreviewStarted;
    private boolean mIsRecordingStarted;
    private int mOpenState;
    private int mPendingStartRequest;
    private Size mPreviewSize;
    private Surface mPreviewSurface;
    private Size mRecordingSize;
    private Surface mRecordingSurface;
    private int mSensorOrientation;
    private int mSessionId;
    private CameraDevice.StateCallback mStateListener;

    Camera2(Context context, String id, Camera.Listener listener) {
        super(context, id, listener);
        this.mPreviewSize = null;
        this.mRecordingSize = null;
        this.mStateListener = new CameraDevice.StateCallback() { // from class: com.qualcomm.ims.vt.Camera2.1
            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onOpened(CameraDevice cameraDevice) {
                Log.i(Camera2.TAG, "onOpened: CameraDevice is opened, id=" + Camera2.this.getId());
                Camera2.this.mCameraDevice = cameraDevice;
                if (Camera2.this.isClosed()) {
                    Log.i(Camera2.TAG, "onOpened: Camera open completed after close requested.");
                    Camera2.this.close();
                } else {
                    Camera2.this.mOpenState = 2;
                    Camera2.this.exectutePendingRequests();
                }
            }

            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onDisconnected(CameraDevice cameraDevice) {
                Log.i(Camera2.TAG, "onDisconnected: CameraDevice is disconnected, id=" + Camera2.this.getId());
                Camera2.this.close();
            }

            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onError(CameraDevice cameraDevice, int error) {
                Log.e(Camera2.TAG, "onError: CameraDevice is disconnected, id=" + Camera2.this.getId() + "error=" + error);
                Camera2.this.close();
            }
        };
        Log.i(this, "Ctor, id=" + id);
        this.mCameraManager = (android.hardware.camera2.CameraManager) this.mContext.getSystemService("camera");
        try {
            this.mSensorOrientation = ((Integer) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
            this.mCameraFacing = ((Integer) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.LENS_FACING)).intValue();
            Log.i(this, "Ctor: Camera sensor orientation = " + this.mSensorOrientation + " cameraFacing = " + this.mCameraFacing);
            this.mActionSoundHelper = new CameraActionSoundHelper(context);
            this.mCameraConfigIms = new Camera.ConfigIms(getDefaultPreviewSize(), 20, 2);
        } catch (CameraAccessException e) {
            notifyOnError(e.getReason());
        }
        reset();
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void open() throws CameraAccessException {
        Log.i(this, "open");
        List<String> ids = Arrays.asList(this.mCameraManager.getCameraIdList());
        Log.v(this, "open: Available camera IDs=" + ids);
        if (!ids.contains(this.mCameraId)) {
            throw new Camera.CameraException("Incorrect id, " + this.mCameraId);
        }
        if (!isClosed()) {
            Log.i(this, "open: camera is already opened, state" + this.mOpenState);
            return;
        }
        try {
            this.mOpenState = 1;
            this.mCameraManager.openCamera(this.mCameraId, this.mStateListener, (Handler) null);
            this.mActionSoundHelper.open();
        } catch (CameraAccessException e) {
            Log.e(this, "open: Failed to open camera, id=" + this.mCameraId + "Exception=" + e);
            this.mOpenState = 0;
            throw e;
        }
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void close() {
        Log.i(this, "close");
        try {
            stopPreview();
        } catch (Exception e) {
            Log.e(this, "close: Failed to close camera preview/recording, exception=" + e);
        }
        CameraCaptureSession cameraCaptureSession = this.mCameraSession;
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
        }
        CameraDevice cameraDevice = this.mCameraDevice;
        if (cameraDevice != null) {
            cameraDevice.close();
        }
        this.mActionSoundHelper.close();
        reset();
    }

    private void reset() {
        Log.v(this, "reset");
        this.mCameraDevice = null;
        this.mOpenState = 0;
        this.mIsPreviewStarted = false;
        this.mIsRecordingStarted = false;
        this.mPendingStartRequest = 0;
        this.mPreviewSurface = null;
        this.mRecordingSurface = null;
        this.mFps = null;
        this.mCropRegion = null;
        this.mCameraSession = null;
        this.mSessionId = 0;
        this.mIsCreateSessionPending = false;
        this.mPreviewSize = null;
        this.mRecordingSize = null;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void setZoom(float v) throws CameraAccessException {
        Log.i(this, "setZoom " + v);
        failIfClosed();
        if (v < getMinZoom() || v > getMaxZoom()) {
            Log.e(this, "setZoom: incorrect zoom value. v=" + v);
            throw new CameraAccessException(3);
        }
        Rect activeRegion = (Rect) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        Log.v(this, "setZoom: ActiveArraySize=" + activeRegion);
        Rect cropRegion = new Rect();
        int xCenter = activeRegion.width() / 2;
        int yCenter = activeRegion.height() / 2;
        int xDelta = (int) (activeRegion.width() / (v * 2.0f));
        int yDelta = (int) (activeRegion.height() / (2.0f * v));
        cropRegion.set(xCenter - xDelta, yCenter - yDelta, xCenter + xDelta, yCenter + yDelta);
        Log.v(this, "setZoom: CropRegionSize=" + cropRegion);
        this.mCropRegion = cropRegion;
        if (this.mCameraSession != null) {
            updateParams(this.mCaptureBuilder);
            restartRepeating(build(this.mCaptureBuilder));
        } else {
            Log.i(this, "Setting zoom is deferred.");
        }
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isRestartNeededOnPreviewSurfaceSizeChanged(Surface surface) {
        Size size = ImsMedia.getSurfaceSize(surface);
        Log.i(this, "new preview size = " + size + " cached previewSize = " + this.mPreviewSize);
        Size size2 = this.mPreviewSize;
        return size2 == null || !size2.equals(size);
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isRestartNeededOnRecordingSurfaceSizeChanged(Surface surface) {
        if (surface == null || this.mRecordingSize == null || !isRecordingStarted()) {
            return false;
        }
        Size size = ImsMedia.getSurfaceSize(surface);
        Log.i(this, "new recording size = " + size + " cached recording size = " + this.mRecordingSize);
        return !this.mRecordingSize.equals(size);
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean canReconfigureWithActiveSession(Camera.ConfigIms cfg) {
        return true;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void reconfigure(Camera.ConfigIms cfg) throws CameraAccessException {
        Log.i(this, "reconfigure " + cfg);
        failIfClosed();
        this.mCameraConfigIms = cfg;
        this.mFps = computeBestFps(cfg.fps);
        if (this.mCameraSession != null) {
            updateParams(this.mCaptureBuilder);
            restartRepeating(build(this.mCaptureBuilder));
        } else {
            Log.i(this, "Reconfiguration is deferred.");
        }
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isZoomSupported() throws CameraAccessException {
        return getMaxZoom() > 1.0f;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isFacingFront() throws CameraAccessException {
        return this.mCameraFacing == 0;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public float getMaxZoom() throws CameraAccessException {
        try {
            float maxZoom = ((Float) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)).floatValue();
            Log.v(this, "getMaxZoom: maxZoom=" + maxZoom);
            return maxZoom;
        } catch (CameraAccessException e) {
            Log.e(this, "getMaxZoom: Failed to retrieve Max Zoom, " + e);
            throw e;
        }
    }

    @Override // com.qualcomm.ims.vt.Camera
    public float getMinZoom() throws CameraAccessException {
        if (!isOpen()) {
            throw new CameraAccessException(3);
        }
        return 1.0f;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public Size getPreviewSize() throws CameraAccessException {
        return this.mCameraConfigIms.getSize();
    }

    private Size getDefaultPreviewSize() throws CameraAccessException {
        StreamConfigurationMap map = (StreamConfigurationMap) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        return map.getOutputSizes(SurfaceTexture.class)[0];
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void startPreview(Surface surface) throws CameraAccessException {
        Log.i(this, "startPreview, surface=" + surface);
        if (isPreviewStarted()) {
            Log.i(this, "startPreview: Preview has already started.");
            return;
        }
        failIfClosed();
        failIfSurfaceNull(surface, "Error preview surface is null");
        this.mPreviewSize = ImsMedia.getSurfaceSize(surface);
        Log.v(this, "startPreview: cached preview size = " + this.mPreviewSize);
        this.mPreviewSurface = surface;
        this.mIsPreviewStarted = true;
        this.mSessionId = genSessionId();
        if (shallDefer()) {
            Log.i(this, "startPreview: Deferring startPreview request, currRequest=" + this.mPendingStartRequest);
            this.mPendingStartRequest = 1;
        } else {
            doStartPreview();
        }
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void stopPreview() throws CameraAccessException {
        Log.i(this, "stopPreview");
        boolean isRecordingStarted = isRecordingStarted();
        if (!isRecordingStarted && !isPreviewStarted()) {
            return;
        }
        this.mIsRecordingStarted = false;
        this.mIsPreviewStarted = false;
        clearStartRequest();
        cancelSession();
        if (isRecordingStarted) {
            this.mActionSoundHelper.onRecordingStopped();
        }
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void startRecording(Surface previewSurface, Surface recordingSurface) throws CameraAccessException {
        Log.i(this, "startRecording: PreviewSurface=" + previewSurface + " RecordingSurface=" + recordingSurface);
        if (isRecordingStarted()) {
            Log.i(this, "startRecording: Recording has already started.");
            return;
        }
        failIfClosed();
        failIfSurfaceNull(previewSurface, "Error preview surface is null");
        failIfSurfaceNull(recordingSurface, "Error recording surface is null");
        this.mPreviewSize = ImsMedia.getSurfaceSize(previewSurface);
        this.mRecordingSize = ImsMedia.getSurfaceSize(recordingSurface);
        Log.i(this, "startRecording: preview size = " + this.mPreviewSize + " recording size = " + this.mRecordingSize);
        this.mRecordingSurface = recordingSurface;
        this.mPreviewSurface = previewSurface;
        this.mIsPreviewStarted = true;
        this.mIsRecordingStarted = true;
        this.mSessionId = genSessionId();
        if (shallDefer()) {
            Log.i(this, "startRecording: Deferring startRecording request, currRequest=" + this.mPendingStartRequest);
            this.mPendingStartRequest = 2;
        } else {
            doStartRecording();
        }
        this.mActionSoundHelper.onRecordingStarted();
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void stopRecording() throws CameraAccessException {
        Log.i(this, "stopRecording");
        if (!isRecordingStarted()) {
            return;
        }
        this.mIsRecordingStarted = false;
        if (this.mIsPreviewStarted) {
            this.mIsPreviewStarted = false;
            Log.i(this, "stopRecording: reset preview flag");
            startPreview(this.mPreviewSurface);
        } else {
            cancelSession();
        }
        this.mActionSoundHelper.onRecordingStopped();
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isOpen() {
        return this.mOpenState != 0;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isPreviewStarted() {
        return this.mIsPreviewStarted;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isRecordingStarted() {
        return this.mIsRecordingStarted;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public int getSensorOrientation() throws CameraAccessException {
        return ((Integer) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void setRecorderFrameRate(int rate) throws CameraAccessException {
        if (rate > this.mCameraConfigIms.getFps()) {
            Log.e(this, "setRecorderFrameRate: Frame rate is greater than max allowed.");
            return;
        }
        this.mFrameSkippingRate = this.mCameraConfigIms.getFps() / rate;
        if (shallDefer()) {
            Log.d(this, "setRecorderFrameRate: Deferred.");
            return;
        }
        if (!isRecordingStarted()) {
            Log.d(this, "setRecorderFrameRate: Deferred. Recording has not started.");
        } else if (this.mCameraSession != null) {
            updateParams(this.mCaptureBuilder);
            restartRepeating(build(this.mCaptureBuilder));
        } else {
            Log.i(this, "setRecorderFrameRate: Deferred.");
        }
    }

    private List<CaptureRequest> build(CaptureRequest.Builder builder) {
        builder.addTarget(this.mPreviewSurface);
        if (!isRecordingStarted()) {
            return Arrays.asList(builder.build());
        }
        builder.removeTarget(this.mRecordingSurface);
        List<CaptureRequest> l = new ArrayList<>();
        for (int i = 1; i < this.mFrameSkippingRate; i++) {
            l.add(builder.build());
        }
        builder.addTarget(this.mRecordingSurface);
        l.add(builder.build());
        return l;
    }

    private Range<Integer> computeBestFps(int v) throws CameraAccessException {
        int delta;
        Log.v(this, "computeBestFps " + v);
        Range<Integer>[] fpsRange = (Range[]) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
        Log.v(this, "computeBestFps: Available FPS Ranges: " + fpsRange);
        if (fpsRange == null || fpsRange.length < 1) {
            throw new CameraAccessException(3);
        }
        Range<Integer> bestFps = fpsRange[0];
        int minDelta = Math.abs(v - ((Integer) bestFps.getUpper()).intValue());
        for (Range<Integer> curr : fpsRange) {
            int delta2 = Math.abs(v - ((Integer) curr.getUpper()).intValue());
            if (delta2 < minDelta) {
                bestFps = curr;
                minDelta = delta2;
            }
        }
        int minDelta2 = Math.abs(((Integer) bestFps.getUpper()).intValue() - ((Integer) bestFps.getLower()).intValue());
        for (Range<Integer> curr2 : fpsRange) {
            if (curr2.getUpper() == bestFps.getUpper() && (delta = Math.abs(((Integer) curr2.getUpper()).intValue() - ((Integer) curr2.getLower()).intValue())) < minDelta2) {
                bestFps = curr2;
                minDelta2 = delta;
            }
        }
        return bestFps;
    }

    private void updateParams(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, 1);
        CaptureRequest.Key<Byte> key = AV_TIMER;
        if (isSupported(builder, key)) {
            Log.v(this, "Enabling AV timer...");
            builder.set(key, (byte) 1);
        }
        if (this.mFps != null) {
            Log.v(this, "Setting FPS_RANGE=" + this.mFps);
            builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.mFps);
        }
        if (this.mCropRegion != null) {
            Log.v(this, "Setting CROP_REGION=" + this.mCropRegion);
            builder.set(CaptureRequest.SCALER_CROP_REGION, this.mCropRegion);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exectutePendingRequests() {
        Log.i(this, "exectutePendingRequests: request=" + this.mPendingStartRequest);
        try {
            switch (this.mPendingStartRequest) {
                case 1:
                    doStartPreview();
                    break;
                case 2:
                    doStartRecording();
                    break;
            }
        } catch (CameraAccessException e) {
        }
    }

    private <T> T getCameraCharacteristic(String str, CameraCharacteristics.Key<T> key) throws CameraAccessException {
        return (T) this.mCameraManager.getCameraCharacteristics(this.mCameraId).get(key);
    }

    private boolean isSupported(CaptureRequest.Builder builder, CaptureRequest.Key<?> key) {
        try {
            builder.get(key);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void abortCaptures() {
        Log.i(this, "abortCaptures: Aborting captures..., id=" + getId());
        try {
            CameraCaptureSession cameraCaptureSession = this.mCameraSession;
            if (cameraCaptureSession != null) {
                cameraCaptureSession.abortCaptures();
                this.mCameraSession.close();
                this.mCameraSession = null;
            }
        } catch (CameraAccessException e) {
            Log.e(this, "abortCaptures: Failed to abort capture, " + e);
            close();
        }
    }

    private void cancelSession() {
        Log.i(this, "cancelSession: Canceling session..., id=" + getId());
        try {
            this.mSessionId = genSessionId();
            CameraCaptureSession cameraCaptureSession = this.mCameraSession;
            if (cameraCaptureSession != null) {
                cameraCaptureSession.stopRepeating();
                this.mCameraSession.close();
                this.mCameraSession = null;
            }
        } catch (CameraAccessException e) {
            Log.e(this, "stopSession: Failed to stop repeating session, " + e);
            close();
        }
    }

    private void doStartRepeating(List<CaptureRequest> request) throws CameraAccessException {
        if (request.isEmpty()) {
            throw new Camera.CameraException("Empty CaptureRequest");
        }
        int size = request.size();
        Log.d(this, "doStartRepeating: Size=" + size);
        if (size == 1) {
            this.mCameraSession.setRepeatingRequest(request.get(0), null, null);
        } else {
            this.mCameraSession.setRepeatingBurst(request, null, null);
        }
    }

    private void startRepeating(List<CaptureRequest> request) {
        Log.i(this, "startRepeating: Starting session..., id=" + getId());
        if (this.mCameraSession == null) {
            Log.e(this, "startRepeating: Session is null.");
            return;
        }
        try {
            Log.v(this, "startRepeating: session=" + this.mCameraSession);
            doStartRepeating(request);
        } catch (CameraAccessException e) {
            Log.e(this, "startRepeating: Failed to start repeating session, " + e);
            close();
        }
    }

    private void restartRepeating(List<CaptureRequest> request) {
        Log.i(this, "restartRepeating: Re-starting session... id=" + getId());
        try {
            if (this.mCameraSession != null) {
                Log.v(this, "restartRepeating: Stopping repeating session...");
                this.mCameraSession.stopRepeating();
                Log.v(this, "restartRepeating: Starting repeating session...");
                doStartRepeating(request);
            } else {
                Log.e(this, "restartRepeating: Session is null.");
            }
        } catch (CameraAccessException e) {
            Log.e(this, "restartRepeating: Failed to start restart repeating session Ex=" + e);
            close();
        }
    }

    private void doStartPreview() throws CameraAccessException {
        Log.i(this, "doStartPreview: surface=" + this.mPreviewSurface);
        failIfSurfaceNull(this.mPreviewSurface, "Error preview surface is null");
        try {
            clearStartRequest();
            this.mCaptureBuilder = this.mCameraDevice.createCaptureRequest(1);
            List<OutputConfiguration> surfaces = Arrays.asList(new OutputConfiguration(this.mPreviewSurface));
            this.mCameraSession = null;
            doCreateCaptureSession(surfaces, new CaptureSessionListener(this.mSessionId));
        } catch (CameraAccessException e) {
            Log.e(this, "startPreview: Failed to start preview, " + e);
            close();
        }
    }

    private void doStartRecording() throws CameraAccessException {
        int rotation;
        Log.i(this, "doStartRecording: Surface=" + this.mRecordingSurface);
        failIfSurfaceNull(this.mPreviewSurface, "Error preview surface is null");
        failIfSurfaceNull(this.mRecordingSurface, "Error recording surface is null");
        try {
            clearStartRequest();
            this.mCaptureBuilder = this.mCameraDevice.createCaptureRequest(3);
            boolean shallComputeRotation = Config.isConfigEnabled(this.mContext, R.bool.config_device_allow_telephony_calculate_rotation);
            if (shallComputeRotation) {
                rotation = calculateRotation(this.mSensorOrientation, this.mCameraConfigIms, isFacingFront());
            } else {
                rotation = 0;
            }
            int rotation2 = adjustRotationCounterclockwise(rotation);
            Log.i(this, "doStartRecording: shallComputeRotation=" + shallComputeRotation + " Camera2 API rotation=" + rotation2);
            OutputConfiguration recordingOutputConfig = new OutputConfiguration(this.mRecordingSurface, rotation2);
            List<OutputConfiguration> surfaces = Arrays.asList(new OutputConfiguration(this.mPreviewSurface), recordingOutputConfig);
            this.mCameraSession = null;
            doCreateCaptureSession(surfaces, new CaptureSessionListener(this.mSessionId));
        } catch (Exception e) {
            Log.e(this, "startRecording: Failed to start recording, " + e);
            close();
            throw e;
        }
    }

    static int adjustRotationCounterclockwise(int rotation) {
        switch (rotation) {
            case 0:
            case 2:
                return rotation;
            case 1:
                return 3;
            case 3:
                return 1;
            default:
                Log.e(TAG, "adjustRotationCounterclockwise: Invalid rotation value " + rotation);
                return rotation;
        }
    }

    static int calculateRotation(int sensorOrientation, Camera.ConfigIms cameraConfigIms, boolean isFacingFront) {
        Log.i(TAG, "calculateRotation mSensorOrientation =" + sensorOrientation + " cameraConfigIms=" + cameraConfigIms);
        int orientationMode = cameraConfigIms.getOrientationMode();
        if (orientationMode == 2) {
            Log.i(TAG, "calculateRotation Portrait mode so rotating by " + sensorOrientation);
            return getOutputConfigurationRotation(sensorOrientation);
        }
        if (orientationMode == 3) {
            return getCvoRotation(sensorOrientation, cameraConfigIms, isFacingFront);
        }
        if (orientationMode == 1) {
            Log.i(TAG, "calculateRotation Landscape mode no rotation");
            return 0;
        }
        return 0;
    }

    static int getCvoRotation(int sensorOrientation, Camera.ConfigIms cameraConfigIms, boolean isFacingFront) {
        if (cameraConfigIms.getWidth() < cameraConfigIms.getHeight()) {
            Log.i(TAG, "calculateRotation CVO mode and w<h so rotating by " + sensorOrientation);
            return getOutputConfigurationRotation(sensorOrientation);
        }
        int rotationFront = (sensorOrientation + 90) % 360;
        int rotationBack = (sensorOrientation + 270) % 360;
        int rotation = isFacingFront ? rotationFront : rotationBack;
        Log.i(TAG, "calculateRotation CVO mode and w>=h so rotating by " + rotation);
        return getOutputConfigurationRotation(rotation);
    }

    static int getOutputConfigurationRotation(int angle) {
        switch (angle) {
            case 0:
                return 0;
            case 90:
                return 1;
            case 180:
                return 2;
            case 270:
                return 3;
            default:
                return 0;
        }
    }

    private void clearStartRequest() {
        Log.i(this, "clearStartRequest: request=" + this.mPendingStartRequest);
        this.mPendingStartRequest = 0;
    }

    private boolean shallDefer() {
        return this.mCameraDevice == null || this.mIsCreateSessionPending;
    }

    private void doCreateCaptureSession(List<OutputConfiguration> o, CameraCaptureSession.StateCallback l) throws CameraAccessException {
        if (this.mIsCreateSessionPending) {
            throw new CameraAccessException(3, "Only one session request is allowed.");
        }
        CameraDevice cameraDevice = this.mCameraDevice;
        if (cameraDevice == null) {
            throw new CameraAccessException(3, "Camera device is not ready (null).");
        }
        this.mIsCreateSessionPending = true;
        cameraDevice.createCaptureSessionByOutputConfigurations(o, l, null);
    }

    private boolean isOpened() {
        return this.mOpenState == 2;
    }

    private class CaptureSessionListener extends CameraCaptureSession.StateCallback {
        private int id;

        public CaptureSessionListener(int v) {
            this.id = v;
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession session) {
            Log.i(this, "onConfigured: SessionId=" + this.id + " GlobalSessionId=" + Camera2.this.mSessionId);
            Camera2.this.mIsCreateSessionPending = false;
            if (!Camera2.this.isRequestPending()) {
                if (this.id == Camera2.this.mSessionId) {
                    Camera2.this.onConfigured(session);
                    return;
                }
                return;
            }
            Camera2.this.exectutePendingRequests();
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession session) {
            Log.e(this, "onConfigureFailed: SessionId=" + this.id + " GlobalSessionId=" + Camera2.this.mSessionId);
            Camera2.this.mIsCreateSessionPending = false;
            if (!Camera2.this.isRequestPending()) {
                if (this.id == Camera2.this.mSessionId) {
                    Camera2.this.onConfigureFailed(session);
                    return;
                }
                return;
            }
            Camera2.this.exectutePendingRequests();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfigured(CameraCaptureSession session) {
        Log.i(this, "onConfigured: session=" + session);
        this.mCameraSession = session;
        CaptureRequest.Builder builder = this.mCaptureBuilder;
        if (builder != null) {
            updateParams(builder);
            startRepeating(build(this.mCaptureBuilder));
        } else {
            Log.e(this, "onConfigured: Builder is null.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfigureFailed(CameraCaptureSession session) {
        Log.e(this, "onConfigureFailed: onConfigureFailed.");
        close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRequestPending() {
        return this.mPendingStartRequest != 0;
    }

    private int genSessionId() {
        int i = this.mSessionId + 1;
        this.mSessionId = i;
        if (i == Integer.MAX_VALUE) {
            this.mSessionId = 0;
        }
        Log.d(this, "generateSessionId: SessionId=" + this.mSessionId);
        return this.mSessionId;
    }

    private boolean isOpening() {
        return this.mOpenState == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isClosed() {
        return this.mOpenState == 0;
    }

    private void failIfClosed() throws CameraAccessException {
        if (isClosed()) {
            Log.e(this, "failIfClosed: Camera is closed...no operation is allowed.");
            throw new CameraAccessException(3);
        }
    }

    private void failIfSurfaceNull(Surface surface, String msg) throws CameraAccessException {
        if (surface != null) {
            return;
        }
        Log.e(this, "failIfSurfaceNull: " + msg);
        throw new CameraAccessException(3, msg);
    }
}
