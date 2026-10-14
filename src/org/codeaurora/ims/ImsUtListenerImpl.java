package org.codeaurora.ims;

import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;
import android.telephony.ims.ImsUtListener;
import com.qualcomm.ims.utils.Log;

/* JADX INFO: loaded from: classes.dex */
public class ImsUtListenerImpl {
    public ImsUtListener mListener;

    public void utConfigurationUpdated(final int id) {
        startThread(new Runnable() { // from class: org.codeaurora.ims.ImsUtListenerImpl$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ImsUtListenerImpl.this.m206lambda$utConfigurationUpdated$0$orgcodeauroraimsImsUtListenerImpl(id);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$utConfigurationUpdated$0$org-codeaurora-ims-ImsUtListenerImpl, reason: not valid java name */
    /* synthetic */ void m206lambda$utConfigurationUpdated$0$orgcodeauroraimsImsUtListenerImpl(int id) {
        if (this.mListener != null) {
            Log.i(this, "utConfigurationUpdated :: id = " + id);
            this.mListener.onUtConfigurationUpdated(id);
        }
    }

    public void utConfigurationUpdateFailed(final int id, final ImsReasonInfo error) {
        startThread(new Runnable() { // from class: org.codeaurora.ims.ImsUtListenerImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ImsUtListenerImpl.this.m205lambda$utConfigurationUpdateFailed$1$orgcodeauroraimsImsUtListenerImpl(id, error);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$utConfigurationUpdateFailed$1$org-codeaurora-ims-ImsUtListenerImpl, reason: not valid java name */
    /* synthetic */ void m205lambda$utConfigurationUpdateFailed$1$orgcodeauroraimsImsUtListenerImpl(int id, ImsReasonInfo error) {
        if (this.mListener != null) {
            Log.i(this, "utConfigurationUpdateFailed :: id = " + id + " error = " + error);
            this.mListener.onUtConfigurationUpdateFailed(id, error);
        }
    }

    public void lineIdentificationSupplementaryServiceResponse(final int id, final ImsSsInfo ssInfo) {
        startThread(new Runnable() { // from class: org.codeaurora.ims.ImsUtListenerImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ImsUtListenerImpl.this.m199lambda$lineIdentificationSupplementaryServiceResponse$2$orgcodeauroraimsImsUtListenerImpl(id, ssInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$lineIdentificationSupplementaryServiceResponse$2$org-codeaurora-ims-ImsUtListenerImpl, reason: not valid java name */
    /* synthetic */ void m199lambda$lineIdentificationSupplementaryServiceResponse$2$orgcodeauroraimsImsUtListenerImpl(int id, ImsSsInfo ssInfo) {
        if (this.mListener != null) {
            Log.i(this, "lineIdentificationSupplementaryServiceResponse:: id = " + id + " ssInfo = " + ssInfo);
            this.mListener.onLineIdentificationSupplementaryServiceResponse(id, ssInfo);
        }
    }

    public void utConfigurationQueryFailed(final int id, final ImsReasonInfo error) {
        startThread(new Runnable() { // from class: org.codeaurora.ims.ImsUtListenerImpl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ImsUtListenerImpl.this.m204lambda$utConfigurationQueryFailed$3$orgcodeauroraimsImsUtListenerImpl(id, error);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$utConfigurationQueryFailed$3$org-codeaurora-ims-ImsUtListenerImpl, reason: not valid java name */
    /* synthetic */ void m204lambda$utConfigurationQueryFailed$3$orgcodeauroraimsImsUtListenerImpl(int id, ImsReasonInfo error) {
        if (this.mListener != null) {
            Log.i(this, "utConfigurationQueryFailed :: id = " + id + " error = " + error);
            this.mListener.onUtConfigurationQueryFailed(id, error);
        }
    }

    public void utConfigurationCallBarringQueried(final int id, final ImsSsInfo[] cbInfo) {
        startThread(new Runnable() { // from class: org.codeaurora.ims.ImsUtListenerImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ImsUtListenerImpl.this.m201lambda$utConfigurationCallBarringQueried$4$orgcodeauroraimsImsUtListenerImpl(id, cbInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$utConfigurationCallBarringQueried$4$org-codeaurora-ims-ImsUtListenerImpl, reason: not valid java name */
    /* synthetic */ void m201lambda$utConfigurationCallBarringQueried$4$orgcodeauroraimsImsUtListenerImpl(int id, ImsSsInfo[] cbInfo) {
        if (this.mListener != null) {
            Log.i(this, "utConfigurationCallBarringQueried :: id = " + id + " cbInfo = " + cbInfo);
            this.mListener.onUtConfigurationCallBarringQueried(id, cbInfo);
        }
    }

    public void utConfigurationCallForwardQueried(final int id, final ImsCallForwardInfo[] cfInfo) {
        startThread(new Runnable() { // from class: org.codeaurora.ims.ImsUtListenerImpl$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ImsUtListenerImpl.this.m202lambda$utConfigurationCallForwardQueried$5$orgcodeauroraimsImsUtListenerImpl(id, cfInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$utConfigurationCallForwardQueried$5$org-codeaurora-ims-ImsUtListenerImpl, reason: not valid java name */
    /* synthetic */ void m202lambda$utConfigurationCallForwardQueried$5$orgcodeauroraimsImsUtListenerImpl(int id, ImsCallForwardInfo[] cfInfo) {
        if (this.mListener != null) {
            Log.i(this, "onUtConfigurationCallForwardQueried :: id = " + id + " cfInfo = " + cfInfo);
            this.mListener.onUtConfigurationCallForwardQueried(id, cfInfo);
        }
    }

    public void utConfigurationCallWaitingQueried(final int id, final ImsSsInfo[] cwInfo) {
        startThread(new Runnable() { // from class: org.codeaurora.ims.ImsUtListenerImpl$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ImsUtListenerImpl.this.m203lambda$utConfigurationCallWaitingQueried$6$orgcodeauroraimsImsUtListenerImpl(id, cwInfo);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$utConfigurationCallWaitingQueried$6$org-codeaurora-ims-ImsUtListenerImpl, reason: not valid java name */
    /* synthetic */ void m203lambda$utConfigurationCallWaitingQueried$6$orgcodeauroraimsImsUtListenerImpl(int id, ImsSsInfo[] cwInfo) {
        if (this.mListener != null) {
            Log.i(this, "utConfigurationCallWaitingQueried :: id = " + id + " cwInfo = " + cwInfo);
            this.mListener.onUtConfigurationCallWaitingQueried(id, cwInfo);
        }
    }

    public void onSupplementaryServiceIndication(final ImsSsData ssData) {
        startThread(new Runnable() { // from class: org.codeaurora.ims.ImsUtListenerImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ImsUtListenerImpl.this.m200lambda$onSupplementaryServiceIndication$7$orgcodeauroraimsImsUtListenerImpl(ssData);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$onSupplementaryServiceIndication$7$org-codeaurora-ims-ImsUtListenerImpl, reason: not valid java name */
    /* synthetic */ void m200lambda$onSupplementaryServiceIndication$7$orgcodeauroraimsImsUtListenerImpl(ImsSsData ssData) {
        if (this.mListener != null) {
            Log.i(this, "onSupplementaryServiceIndication :: ssData = " + ssData);
            this.mListener.onSupplementaryServiceIndication(ssData);
        }
    }

    void startThread(Runnable r) {
        Thread t = new Thread(r, "ImsUtListenerImplThread");
        t.start();
    }
}
