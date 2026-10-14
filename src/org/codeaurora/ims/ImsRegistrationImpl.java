package org.codeaurora.ims;

import android.net.Uri;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.stub.ImsRegistrationImplBase;
import com.qualcomm.ims.utils.Log;

/* JADX INFO: loaded from: classes.dex */
public class ImsRegistrationImpl extends ImsRegistrationImplBase {
    public void registeredWithRadioTech(int imsRadioTech) {
        Log.d(this, "registeredWithRadioTech :: imsRadioTech=" + imsRadioTech);
        onRegistered(getImsRegTechFromRadioTech(imsRadioTech));
    }

    public void registeringWithRadioTech(int imsRadioTech) {
        Log.d(this, "registeringWithRadioTech :: imsRadioTech=" + imsRadioTech);
        onRegistering(getImsRegTechFromRadioTech(imsRadioTech));
    }

    public void registrationDisconnected(ImsReasonInfo info) {
        Log.d(this, "registrationDisconnected :: info=" + info);
        onDeregistered(info);
    }

    public void registrationChangeFailed(int imsRadioTech, ImsReasonInfo info) {
        Log.d(this, "registrationChangeFailed :: imsRadioTech=" + imsRadioTech + " info=" + info);
        onTechnologyChangeFailed(getImsRegTechFromRadioTech(imsRadioTech), info);
    }

    public void registrationAssociatedUriChanged(Uri[] uris) {
        Log.d(this, "registrationAssociatedUriChanged :: uris.length=" + uris.length);
        onSubscriberAssociatedUriChanged(uris);
    }

    private int getImsRegTechFromRadioTech(int imsRadioTech) {
        switch (imsRadioTech) {
            case 13:
                return 0;
            case 18:
                return 1;
            default:
                return -1;
        }
    }
}
