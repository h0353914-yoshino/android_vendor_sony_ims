package org.codeaurora.ims;

import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsCallSessionListener;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import com.qualcomm.ims.utils.Log;

/* JADX INFO: loaded from: classes.dex */
public class ImsCallSessionCallbackHandler {
    private Handler mCallbackHandler;
    private HandlerThread mHandlerThread;
    public ImsCallSessionListener mListener;

    public ImsCallSessionCallbackHandler() {
        Log.i(this, "Constructor: start handler thread for callbacks.");
        HandlerThread handlerThread = new HandlerThread(ImsCallSessionCallbackHandler.class.getSimpleName());
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mCallbackHandler = new Handler(this.mHandlerThread.getLooper());
    }

    public ImsCallSessionCallbackHandler(ImsCallSessionCallbackHandler copyFrom) {
        Log.i(this, "Copy Constructor: Pass the thread and callback handler refs.");
        this.mListener = copyFrom.mListener;
        HandlerThread handlerThread = new HandlerThread(ImsCallSessionCallbackHandler.class.getSimpleName());
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mCallbackHandler = new Handler(this.mHandlerThread.getLooper());
    }

    public void callSessionInitiated(final ImsCallProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m41lambda$callSessionInitiated$0$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionInitiated$0$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m41lambda$callSessionInitiated$0$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionInitiated :: profile=" + profile);
            try {
                this.mListener.callSessionInitiated(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionInitiated :: RuntimeException " + r);
            }
        }
    }

    public void callSessionInitiatedFailed(final ImsReasonInfo reason) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m42lambda$callSessionInitiatedFailed$1$orgcodeauroraimsImsCallSessionCallbackHandler(reason);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionInitiatedFailed$1$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m42lambda$callSessionInitiatedFailed$1$orgcodeauroraimsImsCallSessionCallbackHandler(ImsReasonInfo reason) {
        if (this.mListener != null) {
            Log.i(this, "callSessionInitiatedFailed :: reason=" + reason);
            try {
                this.mListener.callSessionInitiatedFailed(reason);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionInitiatedFailed :: RuntimeException " + r);
            }
        }
    }

    public void callSessionProgressing(final ImsStreamMediaProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m50lambda$callSessionProgressing$2$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionProgressing$2$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m50lambda$callSessionProgressing$2$orgcodeauroraimsImsCallSessionCallbackHandler(ImsStreamMediaProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionProgressing :: profile=" + profile);
            try {
                this.mListener.callSessionProgressing(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionProgressing :: RuntimeException " + r);
            }
        }
    }

    public void callSessionTerminated(final ImsReasonInfo reason) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m59lambda$callSessionTerminated$3$orgcodeauroraimsImsCallSessionCallbackHandler(reason);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionTerminated$3$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m59lambda$callSessionTerminated$3$orgcodeauroraimsImsCallSessionCallbackHandler(ImsReasonInfo reason) {
        if (this.mListener != null) {
            Log.i(this, "callSessionTerminated :: reason=" + reason);
            try {
                this.mListener.callSessionTerminated(reason);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionTerminated :: RuntimeException " + r);
            }
        }
    }

    public void callSessionHandover(final int srcAccessTech, final int targetAccessTech, final ImsReasonInfo reasonInfo) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m36lambda$callSessionHandover$4$orgcodeauroraimsImsCallSessionCallbackHandler(srcAccessTech, targetAccessTech, reasonInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionHandover$4$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m36lambda$callSessionHandover$4$orgcodeauroraimsImsCallSessionCallbackHandler(int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) {
        if (this.mListener != null) {
            Log.i(this, "callSessionHandover :: srcAccessTech=" + srcAccessTech + " targetAccessTech=" + targetAccessTech + " reasonInfo=" + reasonInfo);
            try {
                this.mListener.callSessionHandover(srcAccessTech, targetAccessTech, reasonInfo);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionHandover :: RuntimeException " + r);
            }
        }
    }

    public void callSessionHandoverFailed(final int srcAccessTech, final int targetAccessTech, final ImsReasonInfo reasonInfo) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m37lambda$callSessionHandoverFailed$5$orgcodeauroraimsImsCallSessionCallbackHandler(srcAccessTech, targetAccessTech, reasonInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionHandoverFailed$5$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m37lambda$callSessionHandoverFailed$5$orgcodeauroraimsImsCallSessionCallbackHandler(int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) {
        if (this.mListener != null) {
            Log.i(this, "callSessionHandoverFailed :: srcAccessTech=" + srcAccessTech + " targetAccessTech=" + targetAccessTech + " reasonInfo=" + reasonInfo);
            try {
                this.mListener.callSessionHandoverFailed(srcAccessTech, targetAccessTech, reasonInfo);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionHandoverFailed :: RuntimeException " + r);
            }
        }
    }

    public void callSessionMayHandover(final int srcAccessTech, final int targetAccessTech) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m45lambda$callSessionMayHandover$6$orgcodeauroraimsImsCallSessionCallbackHandler(srcAccessTech, targetAccessTech);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionMayHandover$6$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m45lambda$callSessionMayHandover$6$orgcodeauroraimsImsCallSessionCallbackHandler(int srcAccessTech, int targetAccessTech) {
        if (this.mListener != null) {
            Log.i(this, "callSessionMayHandover :: srcAccessTech=" + srcAccessTech + " targetAccessTech=" + targetAccessTech);
            try {
                this.mListener.callSessionMayHandover(srcAccessTech, targetAccessTech);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionMayHandover :: RuntimeException " + r);
            }
        }
    }

    public void callSessionTtyModeReceived(final int ttyMode) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m60lambda$callSessionTtyModeReceived$7$orgcodeauroraimsImsCallSessionCallbackHandler(ttyMode);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionTtyModeReceived$7$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m60lambda$callSessionTtyModeReceived$7$orgcodeauroraimsImsCallSessionCallbackHandler(int ttyMode) {
        if (this.mListener != null) {
            Log.i(this, "callSessionTtyModeReceived :: ttyMode=" + ttyMode);
            try {
                this.mListener.callSessionTtyModeReceived(ttyMode);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionTtyModeReceived :: RuntimeException " + r);
            }
        }
    }

    public void callSessionHeld(final ImsCallProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m38lambda$callSessionHeld$8$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionHeld$8$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m38lambda$callSessionHeld$8$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionHeld :: profile=" + profile);
            try {
                this.mListener.callSessionHeld(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionHeld :: RuntimeException " + r);
            }
        }
    }

    public void callSessionHoldFailed(final ImsReasonInfo reasonInfo) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m39lambda$callSessionHoldFailed$9$orgcodeauroraimsImsCallSessionCallbackHandler(reasonInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionHoldFailed$9$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m39lambda$callSessionHoldFailed$9$orgcodeauroraimsImsCallSessionCallbackHandler(ImsReasonInfo reasonInfo) {
        if (this.mListener != null) {
            Log.i(this, "callSessionHoldFailed :: reasonInfo=" + reasonInfo);
            try {
                this.mListener.callSessionHoldFailed(reasonInfo);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionHoldFailed :: RuntimeException " + r);
            }
        }
    }

    public void callSessionHoldReceived(final ImsCallProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m40lambda$callSessionHoldReceived$10$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionHoldReceived$10$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m40lambda$callSessionHoldReceived$10$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionHoldReceived :: profile=" + profile);
            try {
                this.mListener.callSessionHoldReceived(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionHoldReceived :: RuntimeException " + r);
            }
        }
    }

    public void callSessionResumed(final ImsCallProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m53lambda$callSessionResumed$11$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionResumed$11$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m53lambda$callSessionResumed$11$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionResumed :: profile=" + profile);
            try {
                this.mListener.callSessionResumed(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionResumed :: RuntimeException " + r);
            }
        }
    }

    public void callSessionResumeFailed(final ImsReasonInfo reasonInfo) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m51lambda$callSessionResumeFailed$12$orgcodeauroraimsImsCallSessionCallbackHandler(reasonInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionResumeFailed$12$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m51lambda$callSessionResumeFailed$12$orgcodeauroraimsImsCallSessionCallbackHandler(ImsReasonInfo reasonInfo) {
        if (this.mListener != null) {
            Log.i(this, "callSessionResumeFailed :: reasonInfo=" + reasonInfo);
            try {
                this.mListener.callSessionResumeFailed(reasonInfo);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionResumeFailed :: RuntimeException " + r);
            }
        }
    }

    public void callSessionResumeReceived(final ImsCallProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m52lambda$callSessionResumeReceived$13$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionResumeReceived$13$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m52lambda$callSessionResumeReceived$13$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionResumeReceived :: profile=" + profile);
            try {
                this.mListener.callSessionResumeReceived(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionResumeReceived :: RuntimeException " + r);
            }
        }
    }

    public void callSessionMergeStarted(final ImsCallSessionImpl newSession, final ImsCallProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m48lambda$callSessionMergeStarted$14$orgcodeauroraimsImsCallSessionCallbackHandler(newSession, profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionMergeStarted$14$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m48lambda$callSessionMergeStarted$14$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallSessionImpl newSession, ImsCallProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionMergeStarted :: newSession=" + newSession + " profile=" + profile);
            try {
                this.mListener.callSessionMergeStarted(newSession, profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionMergeStarted :: RuntimeException " + r);
            }
        }
    }

    public void callSessionMergeComplete(final ImsCallSessionImpl activeCallSession) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m46lambda$callSessionMergeComplete$15$orgcodeauroraimsImsCallSessionCallbackHandler(activeCallSession);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionMergeComplete$15$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m46lambda$callSessionMergeComplete$15$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallSessionImpl activeCallSession) {
        if (this.mListener != null) {
            Log.i(this, "callSessionMergeComplete :: activeCallSession =" + activeCallSession);
            try {
                this.mListener.callSessionMergeComplete(activeCallSession);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionMergeComplete :: RuntimeException " + r);
            }
        }
    }

    public void callSessionMergeFailed(final ImsReasonInfo reasonInfo) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m47lambda$callSessionMergeFailed$16$orgcodeauroraimsImsCallSessionCallbackHandler(reasonInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionMergeFailed$16$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m47lambda$callSessionMergeFailed$16$orgcodeauroraimsImsCallSessionCallbackHandler(ImsReasonInfo reasonInfo) {
        if (this.mListener != null) {
            Log.i(this, "callSessionMergeFailed :: reasonInfo=" + reasonInfo);
            try {
                this.mListener.callSessionMergeFailed(reasonInfo);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionMergeFailed :: RuntimeException " + r);
            }
        }
    }

    public void callSessionUpdated(final ImsCallProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m61lambda$callSessionUpdated$17$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionUpdated$17$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m61lambda$callSessionUpdated$17$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionUpdated :: profile=" + profile);
            try {
                this.mListener.callSessionUpdated(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionUpdated :: RuntimeException " + r);
            }
        }
    }

    public void callSessionSuppServiceReceived(final ImsSuppServiceNotification suppServiceInfo) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m58lambda$callSessionSuppServiceReceived$18$orgcodeauroraimsImsCallSessionCallbackHandler(suppServiceInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionSuppServiceReceived$18$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m58lambda$callSessionSuppServiceReceived$18$orgcodeauroraimsImsCallSessionCallbackHandler(ImsSuppServiceNotification suppServiceInfo) {
        if (this.mListener != null) {
            Log.i(this, "callSessionSuppServiceReceived :: suppServiceInfo=" + suppServiceInfo);
            try {
                this.mListener.callSessionSuppServiceReceived(suppServiceInfo);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionSuppServiceReceived :: RuntimeException " + r);
            }
        }
    }

    public void callSessionInviteParticipantsRequestDelivered() {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m43lambda$callSessionInviteParticipantsRequestDelivered$19$orgcodeauroraimsImsCallSessionCallbackHandler();
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionInviteParticipantsRequestDelivered$19$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m43lambda$callSessionInviteParticipantsRequestDelivered$19$orgcodeauroraimsImsCallSessionCallbackHandler() {
        if (this.mListener != null) {
            Log.i(this, "callSessionInviteParticipantsRequestDelivered");
            try {
                this.mListener.callSessionInviteParticipantsRequestDelivered();
            } catch (RuntimeException r) {
                Log.e(this, "callSessionInviteParticipantsRequestDelivered :: RuntimeException " + r);
            }
        }
    }

    public void callSessionInviteParticipantsRequestFailed(final ImsReasonInfo reasonInfo) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m44lambda$callSessionInviteParticipantsRequestFailed$20$orgcodeauroraimsImsCallSessionCallbackHandler(reasonInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionInviteParticipantsRequestFailed$20$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m44lambda$callSessionInviteParticipantsRequestFailed$20$orgcodeauroraimsImsCallSessionCallbackHandler(ImsReasonInfo reasonInfo) {
        if (this.mListener != null) {
            Log.i(this, "callSessionInviteParticipantsRequestFailed reasonInfo=" + reasonInfo);
            try {
                this.mListener.callSessionInviteParticipantsRequestFailed(reasonInfo);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionInviteParticipantsRequestFailed :: RuntimeException " + r);
            }
        }
    }

    public void callSessionConferenceStateUpdated(final ImsConferenceState state) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m35lambda$callSessionConferenceStateUpdated$21$orgcodeauroraimsImsCallSessionCallbackHandler(state);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionConferenceStateUpdated$21$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m35lambda$callSessionConferenceStateUpdated$21$orgcodeauroraimsImsCallSessionCallbackHandler(ImsConferenceState state) {
        if (this.mListener != null) {
            Log.i(this, "callSessionConferenceStateUpdated :: state=" + state);
            try {
                this.mListener.callSessionConferenceStateUpdated(state);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionConferenceStateUpdated :: RuntimeException " + r);
            }
        }
    }

    public void callSessionMultipartyStateChanged(final boolean isMultiParty) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m49lambda$callSessionMultipartyStateChanged$22$orgcodeauroraimsImsCallSessionCallbackHandler(isMultiParty);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionMultipartyStateChanged$22$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m49lambda$callSessionMultipartyStateChanged$22$orgcodeauroraimsImsCallSessionCallbackHandler(boolean isMultiParty) {
        if (this.mListener != null) {
            Log.i(this, "callSessionMultipartyStateChanged :: isMultiParty=" + isMultiParty);
            try {
                this.mListener.callSessionMultipartyStateChanged(isMultiParty);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionMultipartyStateChanged :: RuntimeException " + r);
            }
        }
    }

    public void callSessionRttModifyResponseReceived(final int status) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m57lambda$callSessionRttModifyResponseReceived$23$orgcodeauroraimsImsCallSessionCallbackHandler(status);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionRttModifyResponseReceived$23$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m57lambda$callSessionRttModifyResponseReceived$23$orgcodeauroraimsImsCallSessionCallbackHandler(int status) {
        if (this.mListener != null) {
            Log.i(this, "callSessionRttModifyResponseReceived :: status=" + status);
            try {
                this.mListener.callSessionRttModifyResponseReceived(status);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionRttModifyResponseReceived :: RuntimeException " + r);
            }
        }
    }

    public void callSessionRttMessageReceived(final String rttMessage) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m55lambda$callSessionRttMessageReceived$24$orgcodeauroraimsImsCallSessionCallbackHandler(rttMessage);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionRttMessageReceived$24$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m55lambda$callSessionRttMessageReceived$24$orgcodeauroraimsImsCallSessionCallbackHandler(String rttMessage) {
        if (this.mListener != null) {
            Log.i(this, "callSessionRttMessageReceived :: rttMessage=" + rttMessage);
            try {
                this.mListener.callSessionRttMessageReceived(rttMessage);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionRttMessageReceived :: RuntimeException " + r);
            }
        }
    }

    public void callSessionRttModifyRequestReceived(final ImsCallProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m56lambda$callSessionRttModifyRequestReceived$25$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionRttModifyRequestReceived$25$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m56lambda$callSessionRttModifyRequestReceived$25$orgcodeauroraimsImsCallSessionCallbackHandler(ImsCallProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionRttModifyRequestReceived :: profile=" + profile);
            try {
                this.mListener.callSessionRttModifyRequestReceived(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionRttModifyRequestReceived :: RuntimeException " + r);
            }
        }
    }

    public void callSessionRttAudioIndicatorChanged(final ImsStreamMediaProfile profile) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m54lambda$callSessionRttAudioIndicatorChanged$26$orgcodeauroraimsImsCallSessionCallbackHandler(profile);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionRttAudioIndicatorChanged$26$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m54lambda$callSessionRttAudioIndicatorChanged$26$orgcodeauroraimsImsCallSessionCallbackHandler(ImsStreamMediaProfile profile) {
        if (this.mListener != null) {
            Log.i(this, "callSessionRttAudioIndicatorChanged :: profile=" + profile);
            try {
                this.mListener.callSessionRttAudioIndicatorChanged(profile);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionRttAudioIndicatorChanged :: RuntimeException " + r);
            }
        }
    }

    public void callSessionUssdMessageReceived(final int mode, final String ussdMessage) {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m62lambda$callSessionUssdMessageReceived$27$orgcodeauroraimsImsCallSessionCallbackHandler(mode, ussdMessage);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$callSessionUssdMessageReceived$27$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m62lambda$callSessionUssdMessageReceived$27$orgcodeauroraimsImsCallSessionCallbackHandler(int mode, String ussdMessage) {
        if (this.mListener != null) {
            Log.i(this, "callSessionUssdMessageReceived :: mode=" + mode);
            try {
                this.mListener.callSessionUssdMessageReceived(mode, ussdMessage);
            } catch (RuntimeException r) {
                Log.e(this, "callSessionUssdMessageReceived :: RuntimeException " + r);
            }
        }
    }

    public void dispose() {
        postRunnable(new Runnable() { // from class: org.codeaurora.ims.ImsCallSessionCallbackHandler$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ImsCallSessionCallbackHandler.this.m63lambda$dispose$28$orgcodeauroraimsImsCallSessionCallbackHandler();
            }
        });
    }

    /* JADX INFO: renamed from: lambda$dispose$28$org-codeaurora-ims-ImsCallSessionCallbackHandler, reason: not valid java name */
    /* synthetic */ void m63lambda$dispose$28$orgcodeauroraimsImsCallSessionCallbackHandler() {
        Log.i(this, "dispose");
        cleanup();
    }

    private void cleanup() {
        Log.i(this, "cleanup");
        HandlerThread handlerThread = this.mHandlerThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            this.mHandlerThread = null;
            this.mCallbackHandler = null;
        }
    }

    private void postRunnable(Runnable r) {
        Log.i(this, "posting to handler");
        Handler handler = this.mCallbackHandler;
        if (handler != null) {
            handler.post(r);
        } else {
            Log.e(this, "Handler is null. Can't post runnable!");
        }
    }
}
