package org.codeaurora.ims;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.qualcomm.ims.utils.Log;
import java.util.HashMap;
import java.util.List;
import org.codeaurora.ims.internal.ICrsCrbtListener;

/* JADX INFO: loaded from: classes.dex */
public class CrsCrbtControllerImpl extends CrsCrbtControllerBase {
    private Context mContext;
    private ImsServiceSub mServiceSub;
    private final HashMap<IBinder, CrsCrbtDeathRecipient> mClients = new HashMap<>();
    private final String MODIFY_PHONE_STATE = "android.permission.MODIFY_PHONE_STATE";

    private final class CrsCrbtDeathRecipient implements IBinder.DeathRecipient {
        ICrsCrbtListener mListener;
        ImsCallSessionImpl mSession;

        public CrsCrbtDeathRecipient(ICrsCrbtListener listener, ImsCallSessionImpl session) {
            this.mListener = listener;
            this.mSession = session;
        }

        public void linkToDeath() throws RemoteException {
            this.mListener.asBinder().linkToDeath(this, 0);
        }

        public void close() {
            this.mListener.asBinder().unlinkToDeath(this, 0);
            synchronized (CrsCrbtControllerImpl.this.mClients) {
                CrsCrbtControllerImpl.this.mClients.remove(this.mListener.asBinder());
            }
            ImsCallSessionImpl imsCallSessionImpl = this.mSession;
            if (imsCallSessionImpl != null) {
                imsCallSessionImpl.setCrsCrbtListener(null);
            }
            this.mListener = null;
            this.mSession = null;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d(this, "Client died: " + this);
            close();
        }

        public String toString() {
            return new StringBuilder(128).append("Client{").append(",ICrsCrbtListener:").append(this.mListener).append(",ImsCallSessionImpl:").append(this.mSession).append("}").toString();
        }
    }

    public CrsCrbtControllerImpl(ImsServiceSub serviceSub, Context context) {
        this.mServiceSub = serviceSub;
        this.mContext = context;
    }

    public void onSetCrsCrbtListener(ICrsCrbtListener listener) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setCrsCrbtListener");
        Log.d(this, "onSetCrsCrbtListener");
        if (listener == null) {
            Log.w(this, "onSetCrsCrbtListener : listener is null");
            return;
        }
        ImsCallSessionImpl session = getIncomingOrOutgoingCallSession();
        if (session == null) {
            Log.d(this, "onSetCrsCrbtListener : no incoming/outgoing call is available");
            return;
        }
        synchronized (this.mClients) {
            CrsCrbtDeathRecipient client = this.mClients.get(listener.asBinder());
            if (client != null) {
                client.close();
                this.mClients.remove(client);
            }
            CrsCrbtDeathRecipient client2 = new CrsCrbtDeathRecipient(listener, session);
            client2.linkToDeath();
            session.setCrsCrbtListener(listener);
            this.mClients.put(listener.asBinder(), client2);
        }
    }

    public void onRemoveCrsCrbtListener(ICrsCrbtListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "removeCrsCrbtListener");
        if (listener == null) {
            Log.w(this, "onRemoveCrsCrbtListener : listener is null");
            return;
        }
        synchronized (this.mClients) {
            CrsCrbtDeathRecipient client = this.mClients.get(listener.asBinder());
            if (client != null) {
                client.close();
                this.mClients.remove(client);
            }
        }
    }

    public void onSendSipDtmf(String requestCode) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "onSendSipDtmf");
        ImsCallSessionImpl session = getIncomingOrOutgoingCallSession();
        if (session == null) {
            Log.d(this, "onSendSipDtmf : no incoming/outgoing call is available");
        } else {
            session.sendSipDtmf(requestCode);
        }
    }

    public boolean onIsPreparatorySession(String callId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "onIsPreparatorySession");
        ImsCallSessionImpl session = getIncomingOrOutgoingCallSession();
        if (session == null) {
            return false;
        }
        return session.isPreparatorySession(callId);
    }

    private ImsCallSessionImpl getIncomingOrOutgoingCallSession() {
        List<ImsCallSessionImpl> sessionList = this.mServiceSub.getCallSessionByState(DriverCallIms.State.INCOMING);
        if (sessionList.isEmpty()) {
            sessionList = this.mServiceSub.getCallSessionByState(DriverCallIms.State.DIALING);
        }
        if (sessionList.isEmpty()) {
            sessionList = this.mServiceSub.getCallSessionByState(DriverCallIms.State.ALERTING);
        }
        if (sessionList.isEmpty()) {
            return null;
        }
        return sessionList.get(0);
    }
}
