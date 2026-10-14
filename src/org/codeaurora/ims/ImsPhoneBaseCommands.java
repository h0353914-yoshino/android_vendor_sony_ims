package org.codeaurora.ims;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.telephony.utils.AsyncResult;
import org.codeaurora.telephony.utils.Registrant;
import org.codeaurora.telephony.utils.RegistrantList;

/* JADX INFO: loaded from: classes.dex */
public abstract class ImsPhoneBaseCommands implements ImsPhoneCommandsInterface {
    protected RegistrantList mAvailRegistrants;
    protected RegistrantList mCallComposerInfoRegistrants;
    protected RegistrantList mCallStateRegistrants;
    protected Context mContext;
    protected Registrant mEmergencyCallbackModeRegistrant;
    protected RegistrantList mExitEmergencyCallbackModeRegistrants;
    protected RegistrantList mImsNetworkStateChangedRegistrants;
    protected RegistrantList mNotAvailRegistrants;
    protected PowerManager.WakeLock mNotifyWakeLock;
    protected RegistrantList mOffOrNotAvailRegistrants;
    protected RegistrantList mOnRegistrants;
    protected int mPhoneType;
    protected RegistrantList mRadioStateChangedRegistrants;
    protected RegistrantList mRefreshViceInfoRegistrants;
    protected Registrant mRingRegistrant;
    protected RegistrantList mRingbackToneRegistrants;
    protected Registrant mSsnRegistrant;
    protected ImsPhoneCommandsInterface.RadioState mState = ImsPhoneCommandsInterface.RadioState.RADIO_UNAVAILABLE;
    protected Object mStateMonitor = new Object();
    protected RegistrantList mVoicePrivacyOffRegistrants;
    protected RegistrantList mVoicePrivacyOnRegistrants;

    public ImsPhoneBaseCommands(Context context) {
        this.mContext = context;
        if (context != null) {
            PowerManager pm = (PowerManager) context.getSystemService("power");
            if (pm != null) {
                PowerManager.WakeLock wakeLockNewWakeLock = pm.newWakeLock(1, WakeLockRegistrant.WAKELOCK_NAME);
                this.mNotifyWakeLock = wakeLockNewWakeLock;
                if (wakeLockNewWakeLock != null) {
                    Log.v(this, "Constructor: wakelock initialised");
                    this.mNotifyWakeLock.setReferenceCounted(false);
                }
            }
        } else {
            Log.e(this, "Constructor: Context is null");
        }
        this.mRadioStateChangedRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mOnRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mAvailRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mOffOrNotAvailRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mNotAvailRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mCallStateRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mImsNetworkStateChangedRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mVoicePrivacyOnRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mVoicePrivacyOffRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mRingbackToneRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mExitEmergencyCallbackModeRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mRefreshViceInfoRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mCallComposerInfoRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public ImsPhoneCommandsInterface.RadioState getRadioState() {
        return this.mState;
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForRadioStateChanged(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        synchronized (this.mStateMonitor) {
            this.mRadioStateChangedRegistrants.add(r);
            r.notifyRegistrant();
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForRadioStateChanged(Handler h) {
        synchronized (this.mStateMonitor) {
            this.mRadioStateChangedRegistrants.remove(h);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForImsNetworkStateChanged(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mImsNetworkStateChangedRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForImsNetworkStateChanged(Handler h) {
        this.mImsNetworkStateChangedRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForOn(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        synchronized (this.mStateMonitor) {
            this.mOnRegistrants.add(r);
            if (this.mState.isOn()) {
                r.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForOn(Handler h) {
        synchronized (this.mStateMonitor) {
            this.mOnRegistrants.remove(h);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForAvailable(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        synchronized (this.mStateMonitor) {
            this.mAvailRegistrants.add(r);
            if (this.mState.isAvailable()) {
                r.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForAvailable(Handler h) {
        synchronized (this.mStateMonitor) {
            this.mAvailRegistrants.remove(h);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForNotAvailable(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        synchronized (this.mStateMonitor) {
            this.mNotAvailRegistrants.add(r);
            if (!this.mState.isAvailable()) {
                r.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForNotAvailable(Handler h) {
        synchronized (this.mStateMonitor) {
            this.mNotAvailRegistrants.remove(h);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForOffOrNotAvailable(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        synchronized (this.mStateMonitor) {
            this.mOffOrNotAvailRegistrants.add(r);
            if (this.mState == ImsPhoneCommandsInterface.RadioState.RADIO_OFF || !this.mState.isAvailable()) {
                r.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForOffOrNotAvailable(Handler h) {
        synchronized (this.mStateMonitor) {
            this.mOffOrNotAvailRegistrants.remove(h);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForCallStateChanged(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mCallStateRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForCallStateChanged(Handler h) {
        this.mCallStateRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setOnSuppServiceNotification(Handler h, int what, Object obj) {
        this.mSsnRegistrant = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unSetOnSuppServiceNotification(Handler h) {
        this.mSsnRegistrant.clear();
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setEmergencyCallbackMode(Handler h, int what, Object obj) {
        this.mEmergencyCallbackModeRegistrant = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setOnCallRing(Handler h, int what, Object obj) {
        this.mRingRegistrant = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unSetOnCallRing(Handler h) {
        this.mRingRegistrant.clear();
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForInCallVoicePrivacyOn(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mVoicePrivacyOnRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForInCallVoicePrivacyOn(Handler h) {
        this.mVoicePrivacyOnRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForInCallVoicePrivacyOff(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mVoicePrivacyOffRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForInCallVoicePrivacyOff(Handler h) {
        this.mVoicePrivacyOffRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForRingbackTone(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mRingbackToneRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForRingbackTone(Handler h) {
        this.mRingbackToneRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForExitEmergencyCallbackMode(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mExitEmergencyCallbackModeRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForExitEmergencyCallbackMode(Handler h) {
        this.mExitEmergencyCallbackModeRegistrants.remove(h);
    }

    protected void setRadioState(ImsPhoneCommandsInterface.RadioState newState) {
        synchronized (this.mStateMonitor) {
            ImsPhoneCommandsInterface.RadioState oldState = this.mState;
            this.mState = newState;
            if (oldState == newState) {
                return;
            }
            this.mRadioStateChangedRegistrants.notifyRegistrants();
            if (this.mState.isAvailable() && !oldState.isAvailable()) {
                this.mAvailRegistrants.notifyRegistrants();
                onRadioAvailable();
            }
            if (!this.mState.isAvailable() && oldState.isAvailable()) {
                this.mNotAvailRegistrants.notifyRegistrants();
            }
            if (this.mState.isOn() && !oldState.isOn()) {
                this.mOnRegistrants.notifyRegistrants();
            }
            if ((!this.mState.isOn() || !this.mState.isAvailable()) && oldState.isOn() && oldState.isAvailable()) {
                this.mOffOrNotAvailRegistrants.notifyRegistrants();
            }
        }
    }

    protected void onRadioAvailable() {
    }

    public void setUiccSubscription(int slotId, int appIndex, int subId, int subStatus, Message response) {
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForViceRefreshInfo(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mRefreshViceInfoRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForCallComposerInfo(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mCallComposerInfoRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void deregisterForCallComposerInfo(Handler h) {
        this.mCallComposerInfoRegistrants.remove(h);
    }
}
