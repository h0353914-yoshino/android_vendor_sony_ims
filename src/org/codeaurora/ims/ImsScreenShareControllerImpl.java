package org.codeaurora.ims;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.qualcomm.ims.utils.Log;
import java.util.List;
import org.codeaurora.ims.internal.IImsScreenShareListener;

/* JADX INFO: loaded from: classes.dex */
public class ImsScreenShareControllerImpl extends ImsScreenShareControllerBase {
    private Context mContext;
    private volatile IImsScreenShareListener mScreenShareListener;
    private ImsServiceSub mServiceSub;
    private final IBinder.DeathRecipient recipient = new IBinder.DeathRecipient() { // from class: org.codeaurora.ims.ImsScreenShareControllerImpl.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            ImsScreenShareControllerImpl.this.onClientDeath();
        }
    };
    private final String MODIFY_PHONE_STATE = "android.permission.MODIFY_PHONE_STATE";

    public ImsScreenShareControllerImpl(ImsServiceSub serviceSub, Context context) {
        this.mServiceSub = serviceSub;
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onClientDeath() {
        this.mScreenShareListener = null;
    }

    public void onSetScreenShareListener(IImsScreenShareListener listener) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setScreenShareListener");
        List<ImsCallSessionImpl> sessionList = this.mServiceSub.getCallSessionByState(DriverCallIms.State.ACTIVE);
        if (sessionList.isEmpty()) {
            Log.d(this, "onSetScreenShareListener: no call is available");
            return;
        }
        IImsScreenShareListener intf = this.mScreenShareListener;
        if (intf != null) {
            IBinder binder = intf.asBinder();
            binder.unlinkToDeath(this.recipient, 0);
        }
        if (listener != null) {
            IBinder binder2 = listener.asBinder();
            binder2.linkToDeath(this.recipient, 0);
        } else {
            Log.w(this, "Trying to set a NULL listener.");
        }
        this.mScreenShareListener = listener;
        sessionList.get(0).setScreenShareListener(listener);
    }

    public void onStartScreenShare(int width, int height) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "onStartScreenShare");
        List<ImsCallSessionImpl> sessionList = this.mServiceSub.getCallSessionByState(DriverCallIms.State.ACTIVE);
        if (sessionList.isEmpty()) {
            Log.d(this, "onStartScreenShare: no call is available");
        } else {
            sessionList.get(0).startScreenShare(width, height);
        }
    }

    public void onStopScreenShare() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "onStopScreenShare");
        List<ImsCallSessionImpl> sessionList = this.mServiceSub.getCallSessionByState(DriverCallIms.State.ACTIVE);
        if (sessionList.isEmpty()) {
            Log.d(this, "onStopScreenShare: no call is available");
        } else {
            sessionList.get(0).stopScreenShare();
        }
    }
}
