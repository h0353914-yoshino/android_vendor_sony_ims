package org.codeaurora.ims;

import android.os.Bundle;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsStreamMediaProfile;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class QImsCallProfile {
    private ImsCallProfile mCallProfile;

    public QImsCallProfile() {
        this.mCallProfile = new ImsCallProfile();
    }

    public QImsCallProfile(int serviceType, int callType) {
        this.mCallProfile = new ImsCallProfile(serviceType, callType);
    }

    public QImsCallProfile(int serviceType, int callType, Bundle callExtras, ImsStreamMediaProfile mediaProfile) {
        this.mCallProfile = new ImsCallProfile(serviceType, callType, callExtras, mediaProfile);
    }

    public ImsCallProfile newImsCallProfile() {
        return newImsCallProfile(this.mCallProfile);
    }

    public ImsCallProfile newImsCallProfile(ImsCallProfile profile) {
        ImsCallProfile newImsCallProfile = new ImsCallProfile(profile.getServiceType(), profile.getCallType(), profile.getCallExtras().deepCopy(), newImsStreamMediaProfile(profile.getMediaProfile()));
        newImsCallProfile.setCallerNumberVerificationStatus(profile.getCallerNumberVerificationStatus());
        newImsCallProfile.setCallRestrictCause(profile.getRestrictCause());
        return newImsCallProfile;
    }

    public ImsStreamMediaProfile newImsStreamMediaProfile(ImsStreamMediaProfile profile) {
        return new ImsStreamMediaProfile(profile.getAudioQuality(), profile.getAudioDirection(), profile.getVideoQuality(), profile.getVideoDirection(), profile.getRttMode());
    }

    public void updateProfile(ImsCallProfile profile) {
        synchronized (this.mCallProfile) {
            if (profile != null) {
                this.mCallProfile = newImsCallProfile(profile);
            }
        }
    }

    public void updateMediaProfile(ImsCallProfile profile) {
        synchronized (this.mCallProfile) {
            if (profile != null) {
                this.mCallProfile.mMediaProfile = newImsStreamMediaProfile(profile.getMediaProfile());
            }
        }
    }

    public void updateMediaProfile(ImsStreamMediaProfile profile) {
        synchronized (this.mCallProfile) {
            if (profile != null) {
                this.mCallProfile.mMediaProfile = newImsStreamMediaProfile(profile);
            }
        }
    }

    public void updateCallType(ImsCallProfile profile) {
        synchronized (this.mCallProfile) {
            if (profile != null) {
                this.mCallProfile.updateCallType(profile);
            }
        }
    }

    public void setCallExtra(String name, String value) {
        synchronized (this.mCallProfile) {
            this.mCallProfile.setCallExtra(name, value);
        }
    }

    public void setCallExtraBoolean(String name, boolean value) {
        synchronized (this.mCallProfile) {
            this.mCallProfile.setCallExtraBoolean(name, value);
        }
    }

    public void setCallExtraInt(String name, int value) {
        synchronized (this.mCallProfile) {
            this.mCallProfile.setCallExtraInt(name, value);
        }
    }

    public void setCallRestrictCause(int cause) {
        synchronized (this.mCallProfile) {
            this.mCallProfile.setCallRestrictCause(cause);
        }
    }

    public void setCallerNumberVerificationStatus(int status) {
        synchronized (this.mCallProfile) {
            this.mCallProfile.setCallerNumberVerificationStatus(status);
        }
    }

    public int getCallType() {
        int callType;
        synchronized (this.mCallProfile) {
            callType = this.mCallProfile.getCallType();
        }
        return callType;
    }

    public int getServiceType() {
        int serviceType;
        synchronized (this.mCallProfile) {
            serviceType = this.mCallProfile.getServiceType();
        }
        return serviceType;
    }

    public ImsStreamMediaProfile getMediaProfile() {
        ImsStreamMediaProfile imsStreamMediaProfileNewImsStreamMediaProfile;
        synchronized (this.mCallProfile) {
            imsStreamMediaProfileNewImsStreamMediaProfile = newImsStreamMediaProfile(this.mCallProfile.getMediaProfile());
        }
        return imsStreamMediaProfileNewImsStreamMediaProfile;
    }

    public Bundle getCallExtras() {
        Bundle callExtras;
        synchronized (this.mCallProfile) {
            callExtras = this.mCallProfile.getCallExtras();
        }
        return callExtras;
    }

    public String getCallExtra(String name) {
        String callExtra;
        synchronized (this.mCallProfile) {
            callExtra = this.mCallProfile.getCallExtra(name);
        }
        return callExtra;
    }

    public boolean getCallExtraBoolean(String name) {
        boolean callExtraBoolean;
        synchronized (this.mCallProfile) {
            callExtraBoolean = this.mCallProfile.getCallExtraBoolean(name);
        }
        return callExtraBoolean;
    }

    public void removeExtras(List<String> extras) {
        synchronized (this.mCallProfile) {
            Bundle callExtras = getCallExtras();
            if (extras == null) {
                return;
            }
            for (String extra : extras) {
                callExtras.remove(extra);
            }
        }
    }

    public String toString() {
        String string;
        synchronized (this.mCallProfile) {
            string = this.mCallProfile.toString();
        }
        return string;
    }
}
