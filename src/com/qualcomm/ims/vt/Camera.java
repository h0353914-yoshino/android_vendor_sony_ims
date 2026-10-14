package com.qualcomm.ims.vt;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

/* JADX INFO: loaded from: classes.dex */
public abstract class Camera {
    private static final String TAG = "VideoCall_Camera";
    protected String mCameraId;
    protected Context mContext;
    Listener mListener;

    public interface Listener {
        void onError(Camera camera, int i);
    }

    public abstract boolean canReconfigureWithActiveSession(ConfigIms configIms);

    public abstract void close();

    public abstract float getMaxZoom() throws CameraAccessException;

    public abstract float getMinZoom() throws CameraAccessException;

    public abstract Size getPreviewSize() throws CameraAccessException;

    public abstract int getSensorOrientation() throws CameraAccessException;

    public abstract boolean isFacingFront() throws CameraAccessException;

    public abstract boolean isOpen();

    public abstract boolean isPreviewStarted();

    public abstract boolean isRecordingStarted();

    public abstract boolean isRestartNeededOnPreviewSurfaceSizeChanged(Surface surface);

    public abstract boolean isRestartNeededOnRecordingSurfaceSizeChanged(Surface surface);

    public abstract boolean isZoomSupported() throws CameraAccessException;

    public abstract void open() throws CameraAccessException;

    public abstract void reconfigure(ConfigIms configIms) throws CameraAccessException;

    public abstract void setRecorderFrameRate(int i) throws CameraAccessException;

    public abstract void setZoom(float f) throws CameraAccessException;

    public abstract void startPreview(Surface surface) throws CameraAccessException;

    public abstract void startRecording(Surface surface, Surface surface2) throws CameraAccessException;

    public abstract void stopPreview() throws CameraAccessException;

    public abstract void stopRecording() throws CameraAccessException;

    public Camera(Context context, String id, Listener listener) {
        this.mContext = context;
        this.mCameraId = id;
        this.mListener = listener;
    }

    public static class ConfigIms {
        int fps;
        int orientationMode;
        Size size;

        public ConfigIms(int w, int h, int f, int mode) {
            this.size = new Size(w, h);
            this.fps = f;
            this.orientationMode = mode;
        }

        public ConfigIms(Size s, int f, int mode) {
            if (s == null) {
                throw new NullPointerException("Size passed to ConfigIms is null.");
            }
            this.size = s;
            this.fps = f;
            this.orientationMode = mode;
        }

        public String toString() {
            return "Camera.ConfigIms(width=" + getWidth() + ", height=" + getHeight() + ", fps=" + getFps() + ", orientationMode=" + this.orientationMode + ")";
        }

        public Size getSize() {
            return this.size;
        }

        public int getWidth() {
            return this.size.getWidth();
        }

        public int getHeight() {
            return this.size.getHeight();
        }

        public int getFps() {
            return this.fps;
        }

        public int getOrientationMode() {
            return this.orientationMode;
        }
    }

    public static class CameraException extends CameraAccessException {
        public static final int CAMERA_ERROR = 3;

        public CameraException(int problem) {
            super(problem);
        }

        public CameraException(int problem, String message) {
            super(problem, message);
        }

        public CameraException(String message) {
            this(3, null);
        }
    }

    public String getId() {
        return this.mCameraId;
    }

    public String toString() {
        return super.toString() + " - CameraId=" + getId();
    }

    protected void notifyOnError(int error) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onError(this, error);
        }
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }

    protected static void logNotSupported(String fn) {
        Log.d(TAG, fn + ": Not supported");
    }
}
