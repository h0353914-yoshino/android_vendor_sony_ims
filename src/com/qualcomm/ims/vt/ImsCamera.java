package com.qualcomm.ims.vt;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;
import com.qualcomm.ims.utils.Log;

/* JADX INFO: loaded from: classes.dex */
public class ImsCamera extends Camera {
    private static final boolean DBG = true;
    private static final short IMS_CAMERA_OPERATION_SUCCESS = 0;
    private static final Size INVALID_SIZE = new Size(-1, -1);
    private static final String TAG = "ImsCamera";
    private android.hardware.camera2.CameraManager mCameraManager;
    private Camera.ConfigIms mConfigIms;
    private boolean mIsOpen;
    private boolean mIsPreviewStarted;
    private boolean mIsRecordingStarted;
    private String mPackageName;
    private Surface mPreviewSurface;
    private WindowManager mWindowManager;

    public static native short native_open(int i);

    public static native short native_open(int i, String str);

    public native int native_getMaxZoom();

    public native boolean native_isZoomSupported();

    public native short native_release();

    public native short native_setDisplayOrientation(int i);

    public native short native_setPreviewFpsRange(short s);

    public native short native_setPreviewSize(int i, int i2);

    public native short native_setPreviewTexture(Surface surface);

    public native void native_setZoom(int i);

    public native short native_startPreview();

    public native short native_startRecording();

    public native short native_stopPreview();

    public native short native_stopRecording();

    static {
        System.loadLibrary("imscamera_jni");
    }

    ImsCamera(Context context, String id, Camera.Listener listener) throws CameraAccessException {
        super(context, id, listener);
        this.mPackageName = context.getPackageName();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mCameraManager = (android.hardware.camera2.CameraManager) this.mContext.getSystemService("camera");
        this.mIsOpen = false;
        this.mIsPreviewStarted = false;
        this.mIsRecordingStarted = false;
        this.mPreviewSurface = null;
        this.mConfigIms = new Camera.ConfigIms(getDefaultPreviewSize(), 20, 2);
    }

    @Override // com.qualcomm.ims.vt.Camera
    public Size getPreviewSize() throws CameraAccessException {
        return this.mConfigIms.getSize();
    }

    private Size getDefaultPreviewSize() throws CameraAccessException {
        StreamConfigurationMap map = (StreamConfigurationMap) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        return map.getOutputSizes(SurfaceTexture.class)[0];
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void open() throws CameraAccessException {
        Log.i(this, "open");
        if (isOpen()) {
            Log.i(this, "open: Camera is already open.");
            return;
        }
        int id = Integer.parseInt(getId());
        short error = native_open(id, this.mPackageName);
        if (error != 0) {
            Log.v(this, "open: error=" + ((int) error));
            throw new CameraAccessException(3);
        }
        this.mIsOpen = DBG;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void close() {
        Log.i(this, "close");
        if (!isOpen()) {
            Log.i(this, "close: Camera is already closed.");
            return;
        }
        try {
            stopPreview();
        } catch (Exception e) {
            Log.e(this, "close: Failed to close camera preview/recording, exception=" + e);
        }
        short error = native_release();
        logIfError("release", error);
        this.mIsOpen = false;
        this.mIsPreviewStarted = false;
        this.mIsRecordingStarted = false;
        this.mPreviewSurface = null;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void setZoom(float v) throws CameraAccessException {
        Log.i(this, "setZoom " + v);
        if (!isOpen()) {
            throw new CameraAccessException(3);
        }
        if (v < getMinZoom() || v > getMaxZoom()) {
            throw new CameraAccessException(3);
        }
        native_setZoom((int) v);
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isRestartNeededOnPreviewSurfaceSizeChanged(Surface surface) {
        return false;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean canReconfigureWithActiveSession(Camera.ConfigIms cfg) {
        return false;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isRestartNeededOnRecordingSurfaceSizeChanged(Surface surface) {
        return false;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void reconfigure(Camera.ConfigIms cfg) throws CameraAccessException {
        Log.i(this, "reconfigure " + cfg);
        if (!isOpen()) {
            throw new CameraAccessException(3);
        }
        setPreviewFps(cfg.getFps());
        setFrameDimension(cfg.getWidth(), cfg.getHeight());
        this.mConfigIms = cfg;
    }

    private void setFrameDimension(int w, int h) throws CameraAccessException {
        Log.i(this, "setPreviewSize");
        if (!isOpen() || isPreviewStarted() || isRecordingStarted()) {
            throw new CameraAccessException(3);
        }
        short error = native_setPreviewSize(w, h);
        logIfError("setPreviewSize", error);
    }

    private void setPreviewFps(int fps) throws CameraAccessException {
        Log.i(this, "setPreviewFps");
        if (!isOpen() || isPreviewStarted() || isRecordingStarted()) {
            throw new CameraAccessException(3);
        }
        short error = native_setPreviewFpsRange((short) fps);
        logIfError("setPreviewFpsRange", error);
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isZoomSupported() throws CameraAccessException {
        if (!isOpen()) {
            throw new CameraAccessException(3);
        }
        boolean result = native_isZoomSupported();
        Log.v(this, "isZoomSupported result=" + result);
        return result;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public float getMaxZoom() throws CameraAccessException {
        if (!isOpen()) {
            throw new CameraAccessException(3);
        }
        int result = native_getMaxZoom();
        Log.v(this, "getMaxZoom result = " + result);
        return result;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public float getMinZoom() throws CameraAccessException {
        if (!isOpen()) {
            throw new CameraAccessException(3);
        }
        return 0.0f;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isFacingFront() throws CameraAccessException {
        if (!isOpen()) {
            throw new CameraAccessException(3);
        }
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Integer.parseInt(getId()), info);
        Log.v(this, "isFacingFront info.facing=" + info.facing);
        if (info.facing == 1) {
            return DBG;
        }
        return false;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void startPreview(Surface surface) throws CameraAccessException {
        Log.i(this, "startPreview: Surface=" + surface);
        if (!isOpen()) {
            Log.e(this, "startPreview: Error camera is closed");
            throw new CameraAccessException(3);
        }
        if (isPreviewStarted()) {
            Log.i(this, "startPreview: Camera preview already started.");
            return;
        }
        this.mPreviewSurface = surface;
        short error = native_setPreviewTexture(surface);
        logIfError("setPreviewTexture", error);
        if (error == 0) {
            error = native_startPreview();
            logIfError("startPreview", error);
        }
        setDisplayOrientation();
        if (error == 0) {
            this.mIsPreviewStarted = DBG;
            return;
        }
        throw new CameraAccessException(3);
    }

    private void doStopPreview() throws CameraAccessException {
        Log.i(this, "doStopPreview");
        short error = native_stopPreview();
        logIfError("doStopPreview", error);
        if (error != 0) {
            throw new CameraAccessException(3);
        }
        this.mIsPreviewStarted = false;
    }

    private void doStopRecording() throws CameraAccessException {
        Log.i(this, "doStopRecording");
        short error = native_stopRecording();
        logIfError("doStopRecording", error);
        if (error != 0) {
            throw new CameraAccessException(3);
        }
        this.mIsRecordingStarted = false;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void stopPreview() throws CameraAccessException {
        if (!isPreviewStarted()) {
            Log.i(this, "stopPreview: Camera preview already stopped.");
            return;
        }
        Log.i(this, "stopPreview");
        if (isRecordingStarted()) {
            doStopRecording();
        }
        doStopPreview();
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void startRecording(Surface previewSurface, Surface recordingSurface) throws CameraAccessException {
        Log.i(this, "startRecording: PreviewSurface=" + previewSurface + " RecordingSurface=" + recordingSurface);
        if (isRecordingStarted()) {
            Log.i(this, "startRecording: Camera recording already started.");
            return;
        }
        this.mPreviewSurface = previewSurface;
        if (previewSurface == null) {
            Log.e(this, "startRecording: Preview surface is null.");
            throw new CameraAccessException(3);
        }
        if (!isPreviewStarted()) {
            startPreview(this.mPreviewSurface);
        }
        short error = native_startRecording();
        logIfError("startRecording", error);
        if (error != 0) {
            throw new CameraAccessException(3);
        }
        this.mIsRecordingStarted = DBG;
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void stopRecording() throws CameraAccessException {
        if (!isRecordingStarted()) {
            Log.i(this, "stopRecording: Camera recording already stopped.");
            return;
        }
        Log.i(this, "stopRecording");
        doStopRecording();
        if (isPreviewStarted()) {
            doStopPreview();
        }
    }

    @Override // com.qualcomm.ims.vt.Camera
    public boolean isOpen() {
        return this.mIsOpen;
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
        try {
            return ((Integer) getCameraCharacteristic(this.mCameraId, CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
        } catch (CameraAccessException e) {
            Log.e(this, "getSensorOrientation: Failed to retrieve sensor orientation, " + e);
            throw e;
        }
    }

    @Override // com.qualcomm.ims.vt.Camera
    public void setRecorderFrameRate(int frameRate) throws CameraAccessException {
        Log.d(this, "setRecorderFrameRate: Not supported.");
    }

    private <T> T getCameraCharacteristic(String str, CameraCharacteristics.Key<T> key) throws CameraAccessException {
        return (T) this.mCameraManager.getCameraCharacteristics(this.mCameraId).get(key);
    }

    private void setDisplayOrientation() {
        int result;
        if (this.mWindowManager == null) {
            Log.e(this, "WindowManager not available");
            return;
        }
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        int degrees = 0;
        int rotation = 0;
        switch (rotation) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
                break;
            default:
                Log.e(this, "setDisplayOrientation: Unexpected rotation: 0");
                break;
        }
        int id = Integer.parseInt(getId());
        android.hardware.Camera.getCameraInfo(id, info);
        if (info.facing == 1) {
            int result2 = (info.orientation + degrees) % 360;
            result = (360 - result2) % 360;
        } else {
            result = ((info.orientation - degrees) + 360) % 360;
        }
        Log.i(this, "setDisplayOrientation rotation=" + result);
        short error = native_setDisplayOrientation(result);
        logIfError("setDisplayOrientation", error);
    }

    private void logIfError(String methodName, short error) {
        if (error != 0) {
            Log.e(this, methodName + " failed with error=" + ((int) error));
        }
    }
}
