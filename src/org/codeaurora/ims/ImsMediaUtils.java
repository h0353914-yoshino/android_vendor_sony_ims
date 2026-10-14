package org.codeaurora.ims;

import android.telephony.ims.ImsStreamMediaProfile;

/* JADX INFO: loaded from: classes.dex */
public class ImsMediaUtils {
    private ImsMediaUtils() {
    }

    public static ImsStreamMediaProfile newImsStreamMediaProfile() {
        return newImsStreamMediaProfile(3, 0);
    }

    public static ImsStreamMediaProfile newImsStreamMediaProfile(int audioDirection, int rttMode) {
        return newImsStreamMediaProfile(0, audioDirection, rttMode);
    }

    public static ImsStreamMediaProfile newImsStreamMediaProfile(int audioQuality, int audioDirection, int rttMode) {
        return newImsStreamMediaProfile(audioQuality, audioDirection, 0, -1, rttMode);
    }

    public static ImsStreamMediaProfile newImsStreamMediaProfile(int audioQuality, int videoQuality, int videoDirection, int rttMode) {
        return newImsStreamMediaProfile(audioQuality, 3, videoQuality, videoDirection, rttMode);
    }

    public static ImsStreamMediaProfile newImsStreamMediaProfile(int audioQuality, int audioDirection, int videoQuality, int videoDirection, int rttMode) {
        return new ImsStreamMediaProfile(audioQuality, audioDirection, videoQuality, videoDirection, rttMode);
    }
}
