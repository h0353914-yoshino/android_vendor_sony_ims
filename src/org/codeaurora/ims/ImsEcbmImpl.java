package org.codeaurora.ims;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ims.stub.ImsEcbmImplBase;
import com.qualcomm.ims.utils.Log;

/* JADX INFO: loaded from: classes.dex */
public class ImsEcbmImpl extends ImsEcbmImplBase {
    private final int EVENT_ENTER_EMERGENCY_CALLBACK_MODE = 1;
    private final int EVENT_EXIT_EMERGENCY_CALLBACK_MODE = 2;
    private ImsSenderRxr mCi;
    private Handler mHandler;
    private ImsServiceSub mServiceSub;

    public ImsEcbmImpl(ImsServiceSub serviceSub, ImsSenderRxr ci) {
        ImsEcbmImplHandler imsEcbmImplHandler = new ImsEcbmImplHandler(this);
        this.mHandler = imsEcbmImplHandler;
        this.mCi = ci;
        ci.setEmergencyCallbackMode(imsEcbmImplHandler, 1, null);
        this.mCi.registerForExitEmergencyCallbackMode(this.mHandler, 2, null);
    }

    public void exitEmergencyCallbackMode() {
        this.mCi.exitEmergencyCallbackMode(Message.obtain(this.mHandler, 2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createEcbmCallBackThread(final boolean isEntered) {
        Runnable r = new Runnable() { // from class: org.codeaurora.ims.ImsEcbmImpl.1
            @Override // java.lang.Runnable
            public void run() {
                Log.i(this, "createEcbmCallBackThread : isEntered = " + isEntered);
                try {
                    if (isEntered) {
                        ImsEcbmImpl.this.enteredEcbm();
                    } else {
                        ImsEcbmImpl.this.exitedEcbm();
                    }
                } catch (Exception e) {
                    Log.i(this, "RemoteException @createEcbmCallBackThread " + e);
                }
            }
        };
        Thread t = new Thread(r, this + "ImsEcbmImpl:EcbmCallBackThread");
        t.start();
    }

    private class ImsEcbmImplHandler extends Handler {
        ImsEcbmImplHandler(ImsEcbmImpl imsEcbmImpl) {
            this(Looper.getMainLooper());
        }

        ImsEcbmImplHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(this, "Message received: what = " + msg.what);
            switch (msg.what) {
                case 1:
                    Log.i(this, "EVENT_ENTER_EMERGENCY_CALLBACK_MODE");
                    ImsEcbmImpl.this.createEcbmCallBackThread(true);
                    break;
                case 2:
                    Log.i(this, "EVENT_EXIT_EMERGENCY_CALLBACK_MODE");
                    ImsEcbmImpl.this.createEcbmCallBackThread(false);
                    break;
                default:
                    Log.i(this, "Unknown message = " + msg.what);
                    break;
            }
        }
    }

    public Handler getHandler() {
        return this.mHandler;
    }
}
