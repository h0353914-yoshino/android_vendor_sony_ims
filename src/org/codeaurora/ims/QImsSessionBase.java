package org.codeaurora.ims;

import android.content.Context;
import android.telephony.ims.ImsCallSessionListener;
import android.telephony.ims.stub.ImsCallSessionImplBase;
import com.qualcomm.ims.utils.Log;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class QImsSessionBase extends ImsCallSessionImplBase {
    public ImsCallSessionCallbackHandler mCallbackHandler;
    protected ImsSenderRxr mCi;
    protected Context mContext;
    protected List<Listener> mListeners;
    protected int mPhoneId;
    protected int mState;
    protected ImsServiceClassTracker mTracker;

    public interface Listener {
        void onCallModifyInitiated(QImsSessionBase qImsSessionBase, CallModify callModify);

        void onCallTypeChanging(QImsSessionBase qImsSessionBase, int i);

        void onClosed(QImsSessionBase qImsSessionBase);

        void onDisconnected(QImsSessionBase qImsSessionBase);

        void onUnsolCallModify(QImsSessionBase qImsSessionBase, CallModify callModify);
    }

    public static class ListenerBase implements Listener {
        @Override // org.codeaurora.ims.QImsSessionBase.Listener
        public void onDisconnected(QImsSessionBase session) {
        }

        @Override // org.codeaurora.ims.QImsSessionBase.Listener
        public void onClosed(QImsSessionBase session) {
        }

        @Override // org.codeaurora.ims.QImsSessionBase.Listener
        public void onUnsolCallModify(QImsSessionBase session, CallModify callModify) {
        }

        @Override // org.codeaurora.ims.QImsSessionBase.Listener
        public void onCallModifyInitiated(QImsSessionBase session, CallModify callModify) {
        }

        @Override // org.codeaurora.ims.QImsSessionBase.Listener
        public void onCallTypeChanging(QImsSessionBase session, int newCallType) {
        }
    }

    protected QImsSessionBase(Context context, ImsSenderRxr senderRxr, int phoneId, ImsServiceClassTracker tracker) {
        this(context, senderRxr, phoneId, tracker, new ImsCallSessionCallbackHandler());
    }

    protected QImsSessionBase(Context context, ImsSenderRxr senderRxr, int phoneId, ImsServiceClassTracker tracker, ImsCallSessionCallbackHandler handler) {
        this.mTracker = null;
        this.mPhoneId = -1;
        this.mState = 0;
        this.mListeners = new CopyOnWriteArrayList();
        this.mCi = senderRxr;
        this.mContext = context;
        this.mTracker = tracker;
        this.mPhoneId = phoneId;
        this.mCallbackHandler = handler;
    }

    public void updateFeatureCapabilities(boolean isVideo, boolean isVoice) {
    }

    public void setListener(ImsCallSessionListener listener) {
        if (isSessionValid()) {
            this.mCallbackHandler.mListener = listener;
        }
    }

    public void addListener(Listener listener) {
        if (isSessionValid()) {
            if (listener == null) {
                throw new IllegalArgumentException("listener is null.");
            }
            synchronized (this.mListeners) {
                if (!this.mListeners.contains(listener)) {
                    this.mListeners.add(listener);
                } else {
                    Log.e(this, "Duplicate listener, " + listener);
                }
            }
        }
    }

    public void removeListener(Listener listener) {
        if (isSessionValid()) {
            if (listener == null) {
                throw new IllegalArgumentException("listener is null.");
            }
            synchronized (this.mListeners) {
                if (this.mListeners.contains(listener)) {
                    this.mListeners.remove(listener);
                } else {
                    Log.e(this, "Listener not found, " + listener);
                }
            }
        }
    }

    protected void notifySessionDisconnected() {
        synchronized (this.mListeners) {
            for (Listener l : this.mListeners) {
                l.onDisconnected(this);
            }
        }
    }

    protected void notifySessionClosed() {
        synchronized (this.mListeners) {
            for (Listener l : this.mListeners) {
                l.onClosed(this);
            }
        }
    }

    public void notifyUnsolCallModify(CallModify callModify) {
        if (isSessionValid()) {
            synchronized (this.mListeners) {
                for (Listener l : this.mListeners) {
                    l.onUnsolCallModify(this, callModify);
                }
            }
        }
    }

    public void notifyCallModifyInitiate(CallModify callModify) {
        if (isSessionValid()) {
            synchronized (this.mListeners) {
                for (Listener l : this.mListeners) {
                    l.onCallModifyInitiated(this, callModify);
                }
            }
        }
    }

    protected void notifyCallTypeChanging(int callType) {
        if (isSessionValid()) {
            synchronized (this.mListeners) {
                for (Listener l : this.mListeners) {
                    l.onCallTypeChanging(this, callType);
                }
            }
        }
    }

    public boolean isSessionValid() {
        boolean isValid = this.mState != -1;
        if (!isValid) {
            Log.e(this, "Session is closed");
        }
        return isValid;
    }
}
