package com.qualcomm.ims.vt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import java.util.List;
import org.codeaurora.ims.DriverCallIms;
import org.codeaurora.ims.ICallListListener;
import org.codeaurora.ims.ImsCallSessionImpl;
import org.codeaurora.ims.ImsCallUtils;
import org.codeaurora.ims.ImsServiceSub;
import org.codeaurora.ims.ImsUssdSessionImpl;
import org.codeaurora.ims.utils.QtiImsExtUtils;

/* JADX INFO: loaded from: classes.dex */
public class LowBatteryHandler implements ICallListListener {
    private static String TAG = "VideoCall_LowBattery";
    private static LowBatteryHandler sInstance;
    private Context mContext;
    private int mPhoneId;
    private ImsServiceSub mServiceSub;
    private boolean mIsLowBattery = false;
    private final boolean isCarrierOneSupported = ImsCallUtils.isCarrierOneSupported();
    private BroadcastReceiver mBatteryLevel = new BroadcastReceiver() { // from class: com.qualcomm.ims.vt.LowBatteryHandler.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                boolean allowVideoCallsInLowBattery = QtiImsExtUtils.allowVideoCallsInLowBattery(LowBatteryHandler.this.mPhoneId, LowBatteryHandler.this.mContext);
                LowBatteryHandler.this.mIsLowBattery = intent.getBooleanExtra("battery_low", false);
                if (LowBatteryHandler.this.mIsLowBattery && !allowVideoCallsInLowBattery) {
                    Log.d(LowBatteryHandler.TAG, "disconnectVideoCalls on low battery");
                    LowBatteryHandler.this.disconnectVideoCalls();
                }
                if (!allowVideoCallsInLowBattery || LowBatteryHandler.this.isCarrierOneSupported) {
                    LowBatteryHandler.this.updateLowBatteryStatus();
                }
            }
        }
    };

    private LowBatteryHandler(ImsServiceSub serviceSub, Context context) {
        this.mPhoneId = -1;
        this.mContext = context;
        this.mServiceSub = serviceSub;
        this.mPhoneId = serviceSub.getPhoneId();
        this.mContext.registerReceiver(this.mBatteryLevel, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    public void onActiveSubChanged(ImsServiceSub serviceSub) {
        if (serviceSub == null) {
            Log.e(TAG, "setActiveSub: serviceSub is null");
        } else if (!this.mServiceSub.equals(serviceSub)) {
            this.mContext.unregisterReceiver(this.mBatteryLevel);
            this.mServiceSub = serviceSub;
            this.mPhoneId = serviceSub.getPhoneId();
            this.mContext.registerReceiver(this.mBatteryLevel, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        }
    }

    public static synchronized LowBatteryHandler init(ImsServiceSub serviceSub, Context context) {
        LowBatteryHandler lowBatteryHandler;
        if (sInstance == null) {
            lowBatteryHandler = new LowBatteryHandler(serviceSub, context);
            sInstance = lowBatteryHandler;
        } else {
            throw new RuntimeException("LowBatteryHandler: Multiple initialization");
        }
        return lowBatteryHandler;
    }

    public void dispose() {
        Log.i(TAG, "dispose()");
        synchronized (LowBatteryHandler.class) {
            this.mContext.unregisterReceiver(this.mBatteryLevel);
            sInstance = null;
        }
    }

    public static synchronized LowBatteryHandler getInstance() {
        LowBatteryHandler lowBatteryHandler;
        lowBatteryHandler = sInstance;
        if (lowBatteryHandler == null) {
            throw new RuntimeException("LowBatteryHandler: Not initialized");
        }
        return lowBatteryHandler;
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionAdded(ImsCallSessionImpl callSession) {
        Log.d(TAG, "onSessionAdded callSession = " + callSession);
        if (this.isCarrierOneSupported && this.mIsLowBattery && ImsCallUtils.isIncomingCall(callSession)) {
            List<ImsCallSessionImpl> sessionList = this.mServiceSub.getCallSessionByState(DriverCallIms.State.DIALING);
            if (!sessionList.isEmpty()) {
                sessionList.get(0).terminate(501);
            }
        }
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionRemoved(ImsCallSessionImpl callSession) {
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionAdded(ImsUssdSessionImpl ussdSession) {
    }

    @Override // org.codeaurora.ims.ICallListListener
    public void onSessionRemoved(ImsUssdSessionImpl ussdSession) {
    }

    public boolean isLowBattery() {
        if (this.isCarrierOneSupported || !QtiImsExtUtils.allowVideoCallsInLowBattery(this.mPhoneId, this.mContext)) {
            return this.mIsLowBattery;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLowBatteryStatus() {
        this.mServiceSub.updateLowBatteryStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnectVideoCalls() {
        Log.d(TAG, "disconnectVideoCalls ");
        disconnectVideoCalls(DriverCallIms.State.ACTIVE);
        disconnectVideoCalls(DriverCallIms.State.HOLDING);
    }

    private void disconnectVideoCalls(DriverCallIms.State state) {
        for (ImsCallSessionImpl session : this.mServiceSub.getCallSessionByState(state)) {
            Log.d(TAG, "disconnectVideoCalls session : " + session);
            if (ImsCallUtils.isVideoCall(session.getInternalCallType())) {
                session.terminate(505);
            }
        }
    }
}
