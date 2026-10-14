package org.codeaurora.ims;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.qualcomm.ims.utils.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.codeaurora.ims.internal.IImsMultiIdentityListener;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsMultiIdentityImpl extends ImsMultiIdentityControllerBase {
    private ImsSenderRxr mCi;
    private Context mContext;
    private Handler mHandler;
    private HashMap<String, MultiIdentityLineInfo> mLinesInfoMap;
    private volatile IImsMultiIdentityListener mMultiIdentityListener;
    private int mPhoneId;
    private HashSet<String> mRegisteredSet;
    private final IBinder.DeathRecipient recipient = new IBinder.DeathRecipient() { // from class: org.codeaurora.ims.ImsMultiIdentityImpl.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            ImsMultiIdentityImpl.this.onClientDeath();
        }
    };
    private final String MODIFY_PHONE_STATE = "android.permission.MODIFY_PHONE_STATE";
    private final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    private final int EVENT_MULTI_IDENTITY_UPDATE_REGISTRATION_STATE_RESPONSE = 1;
    private final int EVENT_MULTI_IDENTITY_REGISTRATION_STATUS_CHANGE = 2;
    private final int EVENT_MULTI_IDENTITY_INFO_PENDING_INDICATION = 3;
    private final int EVENT_MULTI_IDENTITY_INFO_PENDING_RESPONSE = 4;
    private final int EVENT_MULTI_IDENTITY_VIRTUAL_LINE_INFO_RESPONSE = 5;

    public ImsMultiIdentityImpl(int phoneId, ImsSenderRxr senderRxr, Context context, Looper looper) {
        this.mPhoneId = phoneId;
        this.mCi = senderRxr;
        this.mContext = context;
        ImsMultiIdentityHandler imsMultiIdentityHandler = new ImsMultiIdentityHandler(looper);
        this.mHandler = imsMultiIdentityHandler;
        this.mCi.registerForMultiIdentityRegistrationStatusChange(imsMultiIdentityHandler, 2, null);
        this.mCi.registerForMultiIdentityInfoPendingIndication(this.mHandler, 3, null);
        this.mLinesInfoMap = new HashMap<>();
        this.mRegisteredSet = new HashSet<>();
    }

    private class ImsMultiIdentityHandler extends Handler {
        ImsMultiIdentityHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(this, "Message received: what = " + msg.what);
            AsyncResult ar = (AsyncResult) msg.obj;
            switch (msg.what) {
                case 1:
                    ImsMultiIdentityImpl.this.onRegisterMultiIdentityLinesResponse(ar);
                    break;
                case 2:
                    ImsMultiIdentityImpl.this.onMultiIdentityRegistrationStatusChange(ar);
                    break;
                case 3:
                    ImsMultiIdentityImpl.this.onMultiIdentityInfoPendingIndication();
                    break;
                case 4:
                    ImsMultiIdentityImpl.this.onMultiIdentityInfoPendingResponse(ar);
                    break;
                case 5:
                    ImsMultiIdentityImpl.this.onMultiIdentityVirtualLineInfoResponse(ar);
                    break;
                default:
                    Log.i(this, "Unknown message = " + msg.what);
                    break;
            }
        }
    }

    public void setMultiIdentityListener(IImsMultiIdentityListener listener) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "setMultiIdentityListener");
        IImsMultiIdentityListener intf = this.mMultiIdentityListener;
        if (intf != null) {
            IBinder binder = intf.asBinder();
            binder.unlinkToDeath(this.recipient, 0);
        }
        this.mMultiIdentityListener = null;
        if (listener != null) {
            IBinder binder2 = listener.asBinder();
            binder2.linkToDeath(this.recipient, 0);
        } else {
            Log.w(this, "Trying to set a NULL listener.");
        }
        this.mMultiIdentityListener = listener;
    }

    private IImsMultiIdentityListener getMultiIdentityListener() throws RemoteException {
        IImsMultiIdentityListener intf = this.mMultiIdentityListener;
        if (intf == null) {
            Log.e(this, "getMultiIdentityListener :: IImsMultiIdentityListener is null.");
            throw new RemoteException("getMultiIdentityListener :: IImsMultiIdentityListener is null.");
        }
        return intf;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onClientDeath() {
        this.mMultiIdentityListener = null;
    }

    public void queryVirtualLineInfo(String msisdn) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", "queryVirtualLineInfo");
        if (msisdn == null) {
            Log.e(this, "queryVirtualLineInfo :: msisdn is null.");
            throw new RemoteException("queryVirtualLineInfo :: msisdn is null.");
        }
        this.mCi.queryVirtualLineInfo(msisdn, this.mHandler.obtainMessage(5));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiIdentityVirtualLineInfoResponse(AsyncResult ar) {
        VirtualLineInfo virtualInfo = (VirtualLineInfo) ar.result;
        if (virtualInfo == null) {
            Log.e(this, "multiIdentityVirtualLineInfoResponse :: virtualInfo is null.");
            return;
        }
        String msisdn = virtualInfo.mMsisdn;
        ArrayList<String> virtualLines = virtualInfo.mVirtualInfo;
        try {
            getMultiIdentityListener().onQueryVirtualLineInfoResponse(this.mPhoneId, msisdn, virtualLines);
        } catch (Throwable t) {
            Log.e(this, "multiIdentityVirtualLineInfoResponse exception : " + t);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiIdentityInfoPendingResponse(AsyncResult ar) {
        Log.v(this, "multiIdentityInfoPendingResponse");
        if (ar == null || ar.exception != null) {
            Log.e(this, "multiIdentity Info Pending action was unsuccessfull");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiIdentityInfoPendingIndication() {
        Collection<MultiIdentityLineInfo> lines;
        synchronized (this.mLinesInfoMap) {
            lines = this.mLinesInfoMap.values();
        }
        this.mCi.requestMultiIdentityLinesRegistration(lines, this.mHandler.obtainMessage(4));
    }

    public void updateRegistrationStatus(List<MultiIdentityLineInfo> linesInfo) {
        Collection<MultiIdentityLineInfo> lines;
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "updateRegistrationStatus");
        synchronized (this.mLinesInfoMap) {
            for (MultiIdentityLineInfo line : linesInfo) {
                String key = line.getMsisdn();
                if (line.getLineStatus() == 1) {
                    this.mLinesInfoMap.remove(key);
                } else {
                    this.mLinesInfoMap.put(key, new MultiIdentityLineInfo(line));
                }
            }
            lines = this.mLinesInfoMap.values();
        }
        this.mCi.requestMultiIdentityLinesRegistration(lines, this.mHandler.obtainMessage(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRegisterMultiIdentityLinesResponse(AsyncResult ar) {
        if (ar == null) {
            Log.e(this, "registerMultiIdentityLinesResponse :: NULL response received. Returning");
            return;
        }
        int ret = ar.exception == null ? 1 : 0;
        try {
            getMultiIdentityListener().onUpdateRegistrationInfoResponse(this.mPhoneId, ret);
        } catch (Throwable t) {
            Log.e(this, "registerMultiIdentityLinesResponse exception : " + t);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiIdentityRegistrationStatusChange(AsyncResult ar) {
        if (ar == null || ar.result == null) {
            Log.e(this, "onMultiIdentityRegistrationStatusChange :: AsyncResult is null.");
            return;
        }
        if (ar.exception != null) {
            Log.e(this, "onMultiIdentityRegistrationStatusChange :: AsyncResult exception : " + ar.exception);
            return;
        }
        ArrayList<MultiIdentityLineInfo> lines = (ArrayList) ar.result;
        synchronized (this.mRegisteredSet) {
            this.mRegisteredSet.clear();
            for (MultiIdentityLineInfo line : lines) {
                String key = line.getMsisdn();
                if (line.getLineStatus() == 2) {
                    this.mRegisteredSet.add(key);
                }
            }
        }
        try {
            getMultiIdentityListener().onRegistrationStatusChange(this.mPhoneId, lines);
        } catch (Throwable t) {
            Log.e(this, "onMultiIdentityRegistrationStatusChange exception : " + t);
        }
    }

    public Handler getHandler() {
        return this.mHandler;
    }
}
