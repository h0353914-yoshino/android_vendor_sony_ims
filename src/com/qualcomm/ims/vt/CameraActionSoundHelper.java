package com.qualcomm.ims.vt;

import android.content.Context;
import android.media.MediaActionSound;
import com.qualcomm.ims.utils.Config;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.ims.R;

/* JADX INFO: loaded from: classes.dex */
public class CameraActionSoundHelper {
    MediaActionSound mActionSound;
    Context mContext;

    CameraActionSoundHelper(Context context) {
        if (context == null) {
            Log.v(this, "Ctor: Context is null");
        } else {
            this.mContext = context;
        }
    }

    public void open() {
        if (!Config.isConfigEnabled(this.mContext, R.bool.config_enable_camera_sound)) {
            return;
        }
        if (this.mActionSound == null) {
            this.mActionSound = new MediaActionSound();
        }
        Log.v(this, "open: Loading media files.");
        this.mActionSound.load(2);
        this.mActionSound.load(3);
    }

    public void close() {
        if (this.mActionSound == null) {
            return;
        }
        Log.v(this, "close: Releasing resources");
        this.mActionSound.release();
        this.mActionSound = null;
    }

    public void onRecordingStarted() {
        if (this.mActionSound == null) {
            return;
        }
        Log.v(this, "onRecordingStarted:");
        this.mActionSound.play(2);
    }

    public void onRecordingStopped() {
        if (this.mActionSound == null) {
            return;
        }
        Log.v(this, "onRecordingStopped:");
        this.mActionSound.play(3);
    }
}
