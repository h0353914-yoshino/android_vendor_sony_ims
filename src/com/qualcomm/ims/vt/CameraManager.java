package com.qualcomm.ims.vt;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.os.SystemProperties;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.telephony.utils.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class CameraManager {
    private static final String TAG = "CameraManager";
    static IFactory mFactory;
    private static CameraManager sCameraManager;
    private final Context mContext;
    private DevicePolicyManager mDpm;

    public interface IFactory {
        Camera create(String str, Camera.Listener listener) throws CameraAccessException;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private CameraManager(Context context) {
        this.mContext = context;
        Preconditions.checkNotNull(context);
        setFactory(shallUseCamera2() ? new Camera2Factory() : new Camera1Factory());
        this.mDpm = (DevicePolicyManager) context.getSystemService("device_policy");
    }

    private class Camera2Factory implements IFactory {
        private Camera2Factory() {
        }

        @Override // com.qualcomm.ims.vt.CameraManager.IFactory
        public Camera create(String id, Camera.Listener listener) throws CameraAccessException {
            return new Camera2(CameraManager.this.mContext, id, listener);
        }
    }

    private class Camera1Factory implements IFactory {
        private Camera1Factory() {
        }

        @Override // com.qualcomm.ims.vt.CameraManager.IFactory
        public Camera create(String id, Camera.Listener listener) throws CameraAccessException {
            return new ImsCamera(CameraManager.this.mContext, id, listener);
        }
    }

    public static synchronized void init(Context context) {
        if (sCameraManager == null) {
            sCameraManager = new CameraManager(context);
        }
    }

    public static void setFactory(IFactory factory) {
        mFactory = factory;
    }

    public static Camera open(String id, Camera.Listener listener) throws CameraAccessException {
        Log.v(TAG, "open cameraid= " + id + " listener= " + listener);
        Preconditions.checkNotNull(sCameraManager.mDpm);
        Preconditions.checkNotNull(listener);
        if (sCameraManager.mDpm.getCameraDisabled(null)) {
            throw new CameraAccessException(3, "Camera is disabled.");
        }
        Camera camera = mFactory.create(id, listener);
        Preconditions.checkNotNull(camera);
        camera.open();
        return camera;
    }

    private boolean shallUseCamera2() {
        int cameraInterface = SystemProperties.getInt("persist.vendor.qti.telephony.vt_cam_interface", 2);
        if (cameraInterface != 2 && cameraInterface != 1) {
            Log.w(TAG, "cameraInterface = " + cameraInterface);
        }
        return cameraInterface != 1;
    }
}
