package com.qualcomm.ims.vt;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.codeaurora.ims.QtiVideoCallDataUsage;
import org.codeaurora.telephony.utils.SomeArgs;

/* JADX INFO: loaded from: classes.dex */
public class ImsMedia extends Handler {
    public static final int CACHED_MEDIA_EVENT = 100;
    public static final int CAMERA_FRAME_RATE_CHANGE_EVT = 10;
    public static final int CAMERA_PARAM_READY_EVT = 1;
    public static final int DATA_USAGE_EVT = 8;
    public static final int DEVICE_READY_EVENT = 11;
    public static final int DISPLAY_MODE_EVT = 5;
    public static final int DPL_INIT_FAILURE = -1;
    public static final int DPL_INIT_MULTIPLE = -2;
    public static final int DPL_INIT_SUCCESSFUL = 0;
    private static final int LOOPBACK_MODE_FPS = 20;
    private static final int LOOPBACK_MODE_HEIGHT = 144;
    private static final int LOOPBACK_MODE_WIDTH = 176;
    public static final int MEDIA_EVENT = 0;
    public static final int PEER_RESOLUTION_CHANGE_EVT = 6;
    public static final int PLAYER_START_EVENT = 3;
    public static final int PLAYER_STATE_STARTED = 0;
    public static final int PLAYER_STATE_STOPPED = 1;
    public static final int PLAYER_STOP_EVENT = 4;
    private static final int SCREEN_SHARE_DISABLED = 0;
    private static final int SCREEN_SHARE_ENABLED = 1;
    private static final int SCREEN_SHARE_PENDING = 2;
    public static final int START_READY_EVT = 2;
    public static final int STOP_READY_EVT = 9;
    private static final String TAG = "VideoCall_ImsMedia";
    public static final int VIDEO_QUALITY_EVT = 7;
    private static boolean mInitCalledFlag = false;
    private static ImsMedia mInstance;
    private List<Integer> mCachedMediaEvents;
    private final CopyOnWriteArrayList<CameraListener> mCameraListener;
    private boolean mIsMediaLoopback;
    private boolean mIsParamReady;
    private IMediaListener mMediaListener;
    private final List<MediaStateListener> mMediaStateListeners;
    private int mNegotiatedFps;
    private int mNegotiatedHeight;
    private int mNegotiatedWidth;
    private int mPeerHeight;
    private int mPeerWidth;
    private Surface mRecordingSurface;
    private int mScreenShareStatus;
    private int mSharedDisplayHeight;
    private int mSharedDisplayWidth;
    private boolean mShouldCacheMediaEvents;
    private Surface mSurface;
    private int mUIOrientationMode;
    private int mVideoQualityLevel;

    public interface CameraListener {
        void onCameraConfigChanged(int i, int i2, int i3, Surface surface, int i4);

        void onRecordingDisabled();

        void onRecordingEnabled();

        void onUpdateRecorderFrameRate(int i);
    }

    public interface IMediaListener {
        void onDataUsageChanged(int i, QtiVideoCallDataUsage qtiVideoCallDataUsage);

        void onOrientationModeChanged(int i);

        void onPeerResolutionChanged(int i, int i2);

        void onPlayerStateChanged(int i);

        void onRecordingSurfaceChanged(Surface surface, int i, int i2);

        void onVideoQualityEvent(int i);
    }

    public interface MediaStateListener {
        void onMediaDeinitialized();

        void onMediaInitialized();
    }

    private static native void nativeDeInit();

    private static native int nativeGetNegotiatedFPS();

    private static native int nativeGetNegotiatedHeight();

    private static native int nativeGetNegotiatedWidth();

    private static native int nativeGetPeerHeight();

    private static native int nativeGetPeerWidth();

    private static native int nativeGetRecorderFrameRate();

    private static native Surface nativeGetRecordingSurface();

    private static native int nativeGetSurfaceHeight(Surface surface);

    private static native int nativeGetSurfaceWidth(Surface surface);

    private static native int nativeGetUIOrientationMode();

    private static native int nativeGetVideoQualityIndication();

    private static native void nativeHandleRawFrame(byte[] bArr);

    private static native boolean nativeHaveSameParent(Surface surface, Surface surface2);

    private static native int nativeInit();

    private static native void nativeRegisterForMediaEvents(ImsMedia imsMedia);

    private static native int nativeRequestRtpDataUsage(int i);

    private static native void nativeSetCameraFacing(int i);

    private static native void nativeSetCameraInfo(int i, int i2);

    private static native void nativeSetDeviceOrientation(int i);

    private static native int nativeSetSharedDisplayParameters(int i, int i2);

    private static native int nativeSetSurface(Surface surface);

    private static native int nativeSetVideoImageBuffer(int[] iArr, int i, int i2);

    static {
        System.loadLibrary("imsmedia_jni");
    }

    public static synchronized ImsMedia getInstance() {
        if (mInstance == null) {
            mInstance = new ImsMedia();
        }
        return mInstance;
    }

    private ImsMedia() {
        this(Looper.getMainLooper());
    }

    private ImsMedia(Looper looper) {
        super(looper);
        this.mNegotiatedHeight = ImsMediaConstants.DEFAULT_WIDTH;
        this.mNegotiatedWidth = ImsMediaConstants.DEFAULT_HEIGHT;
        this.mUIOrientationMode = 2;
        this.mNegotiatedFps = 20;
        this.mSharedDisplayWidth = ImsMediaConstants.DEFAULT_WIDTH;
        this.mSharedDisplayHeight = ImsMediaConstants.DEFAULT_HEIGHT;
        this.mPeerHeight = ImsMediaConstants.DEFAULT_HEIGHT;
        this.mPeerWidth = ImsMediaConstants.DEFAULT_WIDTH;
        this.mVideoQualityLevel = -1;
        this.mShouldCacheMediaEvents = false;
        this.mCachedMediaEvents = new CopyOnWriteArrayList();
        this.mIsMediaLoopback = false;
        this.mScreenShareStatus = 0;
        this.mIsParamReady = false;
        this.mSurface = null;
        this.mRecordingSurface = null;
        this.mMediaStateListeners = new CopyOnWriteArrayList();
        this.mCameraListener = new CopyOnWriteArrayList<>();
        initializemIsMediaLoopback();
    }

    public void addMediaStateListener(MediaStateListener listener) {
        if (listener != null) {
            this.mMediaStateListeners.add(listener);
        }
    }

    public void removeMediaStateListener(MediaStateListener listener) {
        this.mMediaStateListeners.remove(listener);
    }

    int init() {
        if (mInitCalledFlag) {
            return 0;
        }
        int status = nativeInit();
        log("init called error = " + status);
        switch (status) {
            case DPL_INIT_MULTIPLE /* -2 */:
                mInitCalledFlag = true;
                loge("Dpl init is called multiple times");
                status = 0;
                break;
            case -1:
                mInitCalledFlag = false;
                break;
            case 0:
                mInitCalledFlag = true;
                registerForMediaEvents(this);
                break;
        }
        if (status == 0) {
            notifyOnMediaInitialized();
        }
        return status;
    }

    void deInit() {
        log("deInit called");
        this.mSurface = null;
        notifyOnMediaDeinitialized();
        nativeDeInit();
        mInitCalledFlag = false;
        this.mScreenShareStatus = 0;
        this.mIsParamReady = false;
        clearMediaEventCache();
    }

    private void notifyOnMediaDeinitialized() {
        for (MediaStateListener listener : this.mMediaStateListeners) {
            try {
                listener.onMediaDeinitialized();
            } catch (Exception e) {
                loge("notifyOnMediaDeinitialized: Error=" + e);
            }
        }
    }

    private void notifyOnMediaInitialized() {
        for (MediaStateListener listener : this.mMediaStateListeners) {
            try {
                listener.onMediaInitialized();
            } catch (Exception e) {
                loge("notifyOnMediaInitialized: Error=" + e);
            }
        }
    }

    private void initializemIsMediaLoopback() {
        int property = SystemProperties.getInt("net.lte.VT_LOOPBACK_ENABLE", 0);
        this.mIsMediaLoopback = property == 1;
    }

    public void sendCvoInfo(int orientation) {
        log("sendCvoInfo orientation=" + orientation);
        nativeSetDeviceOrientation(orientation);
    }

    private void doOnCachedMediaEvent() {
        log("doOnCachedMediaEvent: scheduling the cache");
        if (!mInitCalledFlag) {
            logw("VT lib deinitialized. Do not process cached events");
            clearMediaEventCache();
            return;
        }
        synchronized (this) {
            for (Integer eventId : this.mCachedMediaEvents) {
                log("doOnCachedMediaEvent: scheduling event : " + eventId);
                doOnMediaEvent(eventId.intValue());
            }
            clearMediaEventCache();
        }
    }

    public void clearMediaEventCache() {
        this.mShouldCacheMediaEvents = false;
        this.mCachedMediaEvents.clear();
    }

    public void updateMergeStatus(int status) {
        synchronized (this) {
            log("updateMergeStatus status = " + status);
            switch (status) {
                case 1:
                    this.mShouldCacheMediaEvents = true;
                    break;
                case 2:
                    this.mShouldCacheMediaEvents = true;
                    Message msg = obtainMessage(100);
                    int delay = SystemProperties.getInt("persist.vendor.radio.schd.cache", 2300);
                    log("scheduling the cache with delay =" + delay);
                    sendMessageDelayed(msg, delay);
                    break;
                case 3:
                    clearMediaEventCache();
                    break;
            }
        }
    }

    private void initiateScreenShare() {
        calculateSharedDisplayParams(this.mSharedDisplayWidth, this.mSharedDisplayHeight);
        this.mScreenShareStatus = nativeSetSharedDisplayParameters(this.mSharedDisplayWidth, this.mSharedDisplayHeight) == 0 ? 1 : 0;
    }

    public void setSharedDisplayParams(int width, int height) {
        log("setSharedDisplayParams width=" + width + " height=" + height);
        this.mSharedDisplayWidth = width;
        this.mSharedDisplayHeight = height;
        this.mScreenShareStatus = 2;
        if (this.mIsParamReady) {
            initiateScreenShare();
        }
    }

    public void stopScreenShare() {
        this.mScreenShareStatus = nativeSetSharedDisplayParameters(-1, -1) == 0 ? 0 : 1;
        log("Screen Share status after stopScreenShare attempt = " + this.mScreenShareStatus);
    }

    private void calculateSharedDisplayParams(int width, int height) {
        int scalingFactor = (int) Math.max(Math.ceil(((double) width) / ((double) this.mNegotiatedWidth)), Math.ceil(((double) height) / ((double) this.mNegotiatedHeight)));
        if (scalingFactor > 8) {
            scalingFactor = 8;
        }
        this.mSharedDisplayWidth = this.mNegotiatedWidth * scalingFactor;
        this.mSharedDisplayHeight = this.mNegotiatedHeight * scalingFactor;
        log("calculateSharedDisplayParams mSharedDisplayWidth=" + this.mSharedDisplayWidth + " mSharedDisplayHeight=" + this.mSharedDisplayHeight);
    }

    public void setCameraFacing(boolean isFacingFront) {
        int facing = isFacingFront ? 1 : 0;
        log("setCameraFacing isFacingFront=" + isFacingFront);
        nativeSetCameraFacing(facing);
    }

    public void setCameraInfo(int facing, int mount) {
        log("setCameraInfo facing=" + facing + " mount=" + mount);
        nativeSetCameraInfo(facing, mount);
    }

    public void setSurface(Surface st) {
        log("setSurface mSurface=" + this.mSurface + " st=" + st);
        if (!mInitCalledFlag) {
            log("setSurface: init not completed. ignore!");
            return;
        }
        if (!haveSameParent(this.mSurface, st)) {
            if (this.mSurface != null && st != null) {
                nativeSetSurface(null);
            }
            this.mSurface = st;
            nativeSetSurface(st);
        }
    }

    public int getNegotiatedHeight() {
        log("Negotiated Height = " + this.mNegotiatedHeight);
        return this.mNegotiatedHeight;
    }

    public int getNegotiatedWidth() {
        log("Negotiated Width = " + this.mNegotiatedWidth);
        return this.mNegotiatedWidth;
    }

    public int getNegotiatedFps() {
        log("Negotiated Fps = " + this.mNegotiatedFps);
        return this.mNegotiatedFps;
    }

    public Surface getRecordingSurface() {
        log("RecordingSurface= " + this.mRecordingSurface);
        return this.mRecordingSurface;
    }

    public int getUIOrientationMode() {
        log("UI Orientation Mode = " + this.mUIOrientationMode);
        return this.mUIOrientationMode;
    }

    public int getPeerHeight() {
        log("Peer Height = " + this.mPeerHeight);
        return this.mPeerHeight;
    }

    public int getPeerWidth() {
        log("Peer Width = " + this.mPeerWidth);
        return this.mPeerWidth;
    }

    public int getVideoQualityLevel() {
        log("Video Quality Level = " + this.mVideoQualityLevel);
        return this.mVideoQualityLevel;
    }

    public void requestCallDataUsage(int mediaId) {
        log("requestCallDataUsage");
        int status = nativeRequestRtpDataUsage(mediaId);
        log("requestCallDataUsage: status = " + status);
    }

    public boolean setPreviewImage(Bitmap bitmap) {
        int height;
        if (!mInitCalledFlag) {
            log("setPreviewImage: VT lib deinitialized so ignore");
            return false;
        }
        log("setPreviewImage: bitmap = " + bitmap);
        if (bitmap == null) {
            height = nativeSetVideoImageBuffer(null, -1, -1);
        } else {
            int height2 = bitmap.getHeight();
            int width = bitmap.getWidth();
            log("setPreviewImage: bitmap width = " + width + " height = " + height2);
            int[] argb = new int[width * height2];
            bitmap.getPixels(argb, 0, width, 0, 0, width, height2);
            height = nativeSetVideoImageBuffer(argb, width, height2);
        }
        log("setPreviewImage: status = " + height);
        return height == 0;
    }

    private void registerForMediaEvents(ImsMedia instance) {
        log("Registering for Media Callback Events");
        nativeRegisterForMediaEvents(instance);
    }

    public void setMediaListener(IMediaListener listener) {
        log("Registering for Media Listener");
        this.mMediaListener = listener;
    }

    public void addCameraListener(CameraListener listener) {
        if (listener != null) {
            this.mCameraListener.addIfAbsent(listener);
        }
    }

    public void removeCameraListener(CameraListener listener) {
        this.mCameraListener.remove(listener);
    }

    private void doOnMediaEvent(int eventId) {
        switch (eventId) {
            case 1:
                if (updatePreviewParams() && this.mScreenShareStatus == 0) {
                    log("Received PARAM_READY_EVT and not in Screen Share modeUpdating negotiated values");
                    onCameraConfigChanged();
                } else if (this.mScreenShareStatus == 2) {
                    initiateScreenShare();
                }
                this.mIsParamReady = true;
                break;
            case 2:
                log("Received START_READY_EVT. Camera recording can be started");
                for (CameraListener listener : this.mCameraListener) {
                    listener.onRecordingEnabled();
                }
                break;
            case 3:
                log("Received PLAYER_START_EVT");
                IMediaListener iMediaListener = this.mMediaListener;
                if (iMediaListener != null) {
                    iMediaListener.onPlayerStateChanged(0);
                }
                break;
            case 4:
                log("Received PLAYER_STOP_EVT");
                IMediaListener iMediaListener2 = this.mMediaListener;
                if (iMediaListener2 != null) {
                    iMediaListener2.onPlayerStateChanged(1);
                }
                break;
            case 5:
                log("Received DISPLAY_MODE_EVT");
                this.mUIOrientationMode = nativeGetUIOrientationMode();
                log("Received DISPLAY_MODE_EVT mUIOrientationMode=" + this.mUIOrientationMode);
                IMediaListener iMediaListener3 = this.mMediaListener;
                if (iMediaListener3 != null) {
                    iMediaListener3.onOrientationModeChanged(this.mUIOrientationMode);
                }
                break;
            case 6:
                this.mPeerHeight = nativeGetPeerHeight();
                this.mPeerWidth = nativeGetPeerWidth();
                log("Received PEER_RESOLUTION_CHANGE_EVENT. Updating peer values mPeerHeight=" + this.mPeerHeight + " mPeerWidth=" + this.mPeerWidth);
                IMediaListener iMediaListener4 = this.mMediaListener;
                if (iMediaListener4 != null) {
                    iMediaListener4.onPeerResolutionChanged(this.mPeerWidth, this.mPeerHeight);
                }
                break;
            case 7:
                this.mVideoQualityLevel = nativeGetVideoQualityIndication();
                log("Received VIDEO_QUALITY_EVT" + this.mVideoQualityLevel);
                IMediaListener iMediaListener5 = this.mMediaListener;
                if (iMediaListener5 != null) {
                    iMediaListener5.onVideoQualityEvent(this.mVideoQualityLevel);
                }
                break;
            case 8:
            default:
                loge("Received unknown event id=" + eventId);
                break;
            case 9:
                log("Received STOP_READY_EVT");
                for (CameraListener listener2 : this.mCameraListener) {
                    listener2.onRecordingDisabled();
                }
                break;
            case 10:
                int rate = nativeGetRecorderFrameRate();
                log("Received CAMERA_FRAME_RATE_CHANGE_EVT, rate=" + rate);
                if (rate > 0) {
                    for (CameraListener listener3 : this.mCameraListener) {
                        listener3.onUpdateRecorderFrameRate(rate);
                    }
                }
                break;
            case 11:
                log("Received DEVICE_READY_EVENT.");
                int i = this.mScreenShareStatus;
                if (i == 1) {
                    this.mRecordingSurface = nativeGetRecordingSurface();
                    log("mRecordingSurface = " + this.mRecordingSurface);
                    IMediaListener iMediaListener6 = this.mMediaListener;
                    if (iMediaListener6 != null) {
                        iMediaListener6.onRecordingSurfaceChanged(this.mRecordingSurface, this.mSharedDisplayWidth, this.mSharedDisplayHeight);
                    }
                } else if (i == 0) {
                    IMediaListener iMediaListener7 = this.mMediaListener;
                    if (iMediaListener7 != null) {
                        iMediaListener7.onRecordingSurfaceChanged(null, this.mSharedDisplayWidth, this.mSharedDisplayHeight);
                    }
                    onCameraConfigChanged();
                }
                break;
        }
    }

    private void onCameraConfigChanged() {
        log("Negotiated Camera values mNegotiatedWidth = " + this.mNegotiatedWidth + " mNegotiatedHeight = " + this.mNegotiatedHeight + " mRecordingSurface = " + this.mRecordingSurface);
        for (CameraListener listener : this.mCameraListener) {
            listener.onCameraConfigChanged(this.mNegotiatedWidth, this.mNegotiatedHeight, this.mNegotiatedFps, this.mRecordingSurface, this.mUIOrientationMode);
        }
    }

    public void onMediaEvent(int eventId) {
        log("onMediaEvent eventId = " + eventId + " mShouldCacheMediaEvents: " + this.mShouldCacheMediaEvents);
        if (!mInitCalledFlag) {
            logw("VT lib deinitialized. Do not cache events");
            return;
        }
        synchronized (this) {
            if (this.mShouldCacheMediaEvents && eventId != 2 && eventId != 9) {
                this.mCachedMediaEvents.add(Integer.valueOf(eventId));
            } else {
                Message msg = obtainMessage(0, eventId, 0);
                sendMessage(msg);
            }
        }
    }

    private void onDataUsageEvent(int mediaId, long[] dataUsage) {
        log("onDataUsageEvent mediaId = " + mediaId);
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = Integer.valueOf(mediaId);
        args.arg2 = dataUsage;
        Message msg = obtainMessage(8, args);
        sendMessage(msg);
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                doOnMediaEvent(msg.arg1);
                return;
            case 8:
                SomeArgs args = (SomeArgs) msg.obj;
                try {
                    int mediaId = ((Integer) args.arg1).intValue();
                    long[] dataUsage = (long[]) args.arg2;
                    IMediaListener iMediaListener = this.mMediaListener;
                    if (iMediaListener != null) {
                        iMediaListener.onDataUsageChanged(mediaId, new QtiVideoCallDataUsage(dataUsage));
                        break;
                    }
                    return;
                } finally {
                    args.recycle();
                }
            case CACHED_MEDIA_EVENT /* 100 */:
                doOnCachedMediaEvent();
                return;
            default:
                loge("Received unknown msg id = " + msg.what);
                return;
        }
    }

    public static Size getSurfaceSize(Surface surface) {
        if (surface == null) {
            logw("surface is null");
            return new Size(0, 0);
        }
        return new Size(nativeGetSurfaceWidth(surface), nativeGetSurfaceHeight(surface));
    }

    public static boolean haveSameParent(Surface a, Surface b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return nativeHaveSameParent(a, b);
    }

    private synchronized boolean updatePreviewParams() {
        if (this.mIsMediaLoopback) {
            this.mNegotiatedHeight = LOOPBACK_MODE_HEIGHT;
            this.mNegotiatedWidth = LOOPBACK_MODE_WIDTH;
            this.mNegotiatedFps = 20;
            return true;
        }
        int h = nativeGetNegotiatedHeight();
        int w = nativeGetNegotiatedWidth();
        int fps = nativeGetNegotiatedFPS();
        int mode = nativeGetUIOrientationMode();
        Surface surface = nativeGetRecordingSurface();
        if (this.mNegotiatedHeight != h || this.mNegotiatedWidth != w || this.mNegotiatedFps != fps || !haveSameParent(this.mRecordingSurface, surface) || this.mUIOrientationMode != mode) {
            this.mNegotiatedHeight = h;
            this.mNegotiatedWidth = w;
            this.mNegotiatedFps = fps;
            this.mRecordingSurface = surface;
            this.mUIOrientationMode = mode;
            return true;
        }
        return false;
    }

    boolean isMediaInitialized() {
        return mInitCalledFlag;
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }

    private static void logw(String msg) {
        Log.w(TAG, msg);
    }
}
