package com.qualcomm.ims.vt;

import android.content.Context;
import android.util.Log;
import java.util.List;
import org.codeaurora.ims.DriverCallIms;
import org.codeaurora.ims.ImsCallSessionImpl;
import org.codeaurora.ims.ImsServiceSub;

/* JADX INFO: loaded from: classes.dex */
public class ImsVideoGlobals {
    private static String TAG = "VideoCall_ImsVideoGlobals";
    private static ImsVideoGlobals sInstance = null;
    private int mActivePhoneId = 0;
    private CameraController mCameraController;
    private Context mContext;
    private LowBatteryHandler mLowBatteryHandler;
    private MediaController mMediaController;
    private ImsServiceSub[] mServiceSubs;

    // 由 JADX 反編譯修正：JADX 漏掉 return，使 init 一律丟例外；依 smali 還原
    public static synchronized void init(ImsServiceSub[] serviceSubArr, Context context) {
        if (serviceSubArr == null || serviceSubArr.length < 1 || serviceSubArr[0] == null) {
            throw new IllegalArgumentException("Default subscription is null.");
        }
        if (sInstance != null) {
            throw new RuntimeException("ImsVideoGlobals: Multiple initializaiton.");
        }
        sInstance = new ImsVideoGlobals(serviceSubArr, context);
    }

    public static synchronized ImsVideoGlobals getInstance() {
        ImsVideoGlobals imsVideoGlobals;
        imsVideoGlobals = sInstance;
        if (imsVideoGlobals == null) {
            throw new RuntimeException("ImsVideoGlobals: getInstance called before init.");
        }
        return imsVideoGlobals;
    }

    public void dispose() {
        log("dispose()");
        synchronized (ImsVideoGlobals.class) {
            for (ImsServiceSub sub : this.mServiceSubs) {
                sub.removeListener(this.mMediaController);
            }
            getActiveServiceSub().removeListener(this.mLowBatteryHandler);
            this.mCameraController.dispose();
            this.mMediaController.dispose();
            this.mLowBatteryHandler.dispose();
            sInstance = null;
        }
    }

    public void setPhoneIdWithActiveCall(int phoneId) {
        log("setPhoneIdWithActiveCall, phoneId # " + phoneId);
        if (phoneId < 0 || phoneId >= this.mServiceSubs.length) {
            throw new IllegalArgumentException("Invalid phoneId.");
        }
        if (getActiveServiceSub() != null) {
            getActiveServiceSub().removeListener(this.mLowBatteryHandler);
        }
        setActivePhoneId(phoneId);
        this.mLowBatteryHandler.onActiveSubChanged(getActiveServiceSub());
        getActiveServiceSub().addListener(this.mLowBatteryHandler);
    }

    private ImsVideoGlobals(ImsServiceSub[] serviceSubs, Context context) {
        this.mServiceSubs = serviceSubs;
        setActivePhoneId(0);
        this.mContext = context;
        CameraController.init(context, ImsMedia.getInstance());
        MediaController.init(context, ImsMedia.getInstance());
        LowBatteryHandler.init(getActiveServiceSub(), context);
        this.mCameraController = CameraController.getInstance();
        this.mMediaController = MediaController.getInstance();
        this.mLowBatteryHandler = LowBatteryHandler.getInstance();
        for (ImsServiceSub sub : this.mServiceSubs) {
            sub.addListener(this.mMediaController);
        }
    }

    private ImsServiceSub getActiveServiceSub() {
        return this.mServiceSubs[this.mActivePhoneId];
    }

    private void setActivePhoneId(int phoneId) {
        this.mActivePhoneId = phoneId;
    }

    ImsCallSessionImpl getActiveCallSession() {
        List<ImsCallSessionImpl> sessionList = getActiveServiceSub().getCallSessionByState(DriverCallIms.State.ACTIVE);
        if (sessionList.size() > 1) {
            log("Multiple Active Calls: " + sessionList);
        }
        if (sessionList.isEmpty()) {
            return null;
        }
        return sessionList.get(0);
    }

    ImsCallSessionImpl getOutgoingCallSession() {
        List<ImsCallSessionImpl> sessionList = getActiveServiceSub().getCallSessionByState(DriverCallIms.State.ALERTING);
        if (sessionList.isEmpty()) {
            sessionList = getActiveServiceSub().getCallSessionByState(DriverCallIms.State.DIALING);
        }
        if (sessionList.size() > 1) {
            log("Multiple Outgoing Calls: " + sessionList);
        }
        if (sessionList.isEmpty()) {
            return null;
        }
        return sessionList.get(0);
    }

    ImsCallSessionImpl getIncomingCallSession() {
        List<ImsCallSessionImpl> sessionList = getActiveServiceSub().getCallSessionByState(DriverCallIms.State.INCOMING);
        if (sessionList.size() > 1) {
            log("Multiple Incoming Calls: " + sessionList);
        }
        if (sessionList.isEmpty()) {
            return null;
        }
        return sessionList.get(0);
    }

    ImsCallSessionImpl getBackgroundCallSession() {
        List<ImsCallSessionImpl> sessionList = getActiveServiceSub().getCallSessionByState(DriverCallIms.State.HOLDING);
        if (sessionList.size() > 1) {
            log("Multiple Background Calls: " + sessionList);
        }
        if (sessionList.isEmpty()) {
            return null;
        }
        return sessionList.get(0);
    }

    ImsCallSessionImpl getActiveOrOutgoingCallSession() {
        ImsCallSessionImpl session = getActiveCallSession();
        if (session == null) {
            return getOutgoingCallSession();
        }
        return session;
    }

    ImsCallSessionImpl getActiveOrBackgroundCallSession() {
        ImsCallSessionImpl session = getActiveCallSession();
        if (session == null) {
            return getBackgroundCallSession();
        }
        return session;
    }

    private ImsVideoCallProviderImpl getImsVideoCallProviderImpl(ImsCallSessionImpl session) {
        if (session == null) {
            return null;
        }
        return session.getImsVideoCallProviderImpl();
    }

    ImsVideoCallProviderImpl getActiveCallVideoProvider() {
        return getImsVideoCallProviderImpl(getActiveCallSession());
    }

    ImsVideoCallProviderImpl getOutgoingCallVideoProvider() {
        return getImsVideoCallProviderImpl(getOutgoingCallSession());
    }

    ImsVideoCallProviderImpl getIncomingCallVideoProvider() {
        return getImsVideoCallProviderImpl(getIncomingCallSession());
    }

    ImsVideoCallProviderImpl getBackgroundCallVideoProvider() {
        return getImsVideoCallProviderImpl(getBackgroundCallSession());
    }

    ImsVideoCallProviderImpl getActiveOrOutgoingCallVideoProvider() {
        return getImsVideoCallProviderImpl(getActiveOrOutgoingCallSession());
    }

    ImsVideoCallProviderImpl getActiveOrBackgroundCallVideoProvider() {
        return getImsVideoCallProviderImpl(getActiveOrBackgroundCallSession());
    }

    ImsCallSessionImpl findSessionbyMediaId(int mediaId) {
        return getActiveServiceSub().findSessionByMediaId(mediaId);
    }

    ImsVideoCallProviderImpl findVideoCallProviderbyMediaId(int mediaId) {
        return getImsVideoCallProviderImpl(getActiveServiceSub().findSessionByMediaId(mediaId));
    }

    CameraController getCameraController() {
        return this.mCameraController;
    }

    MediaController getMediaController() {
        return this.mMediaController;
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }
}
