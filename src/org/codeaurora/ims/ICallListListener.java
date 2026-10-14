package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public interface ICallListListener {
    void onSessionAdded(ImsCallSessionImpl imsCallSessionImpl);

    void onSessionAdded(ImsUssdSessionImpl imsUssdSessionImpl);

    void onSessionRemoved(ImsCallSessionImpl imsCallSessionImpl);

    void onSessionRemoved(ImsUssdSessionImpl imsUssdSessionImpl);
}
