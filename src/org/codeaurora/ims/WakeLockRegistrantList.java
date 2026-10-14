package org.codeaurora.ims;

import android.content.Context;
import android.os.PowerManager;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.telephony.utils.AsyncResult;
import org.codeaurora.telephony.utils.RegistrantList;

/* JADX INFO: loaded from: classes.dex */
public class WakeLockRegistrantList extends RegistrantList {
    private PowerManager.WakeLock mNotifyWakeLock;
    private long mWakeLockTimeOut;

    public WakeLockRegistrantList(PowerManager.WakeLock wakeLock, long timeout) {
        this.mNotifyWakeLock = wakeLock;
        this.mWakeLockTimeOut = timeout;
    }

    public WakeLockRegistrantList(PowerManager.WakeLock wakeLock) {
        this(wakeLock, 200L);
    }

    public WakeLockRegistrantList(Context context, long timeout) {
        PowerManager pm;
        if (context != null && (pm = (PowerManager) context.getSystemService("power")) != null) {
            PowerManager.WakeLock wakeLockNewWakeLock = pm.newWakeLock(1, WakeLockRegistrant.WAKELOCK_NAME);
            this.mNotifyWakeLock = wakeLockNewWakeLock;
            if (wakeLockNewWakeLock != null) {
                wakeLockNewWakeLock.setReferenceCounted(false);
            }
        }
        this.mWakeLockTimeOut = timeout;
    }

    public WakeLockRegistrantList(Context context) {
        this(context, 200L);
    }

    public void notifyRegistrants() {
        acquireNotifyWakeLock();
        super.notifyRegistrants();
    }

    public void notifyException(Throwable exception) {
        acquireNotifyWakeLock();
        super.notifyException(exception);
    }

    public void notifyResult(Object result) {
        acquireNotifyWakeLock();
        super.notifyResult(result);
    }

    public void notifyRegistrants(AsyncResult ar) {
        acquireNotifyWakeLock();
        super.notifyRegistrants(ar);
    }

    private void acquireNotifyWakeLock() {
        if (this.mNotifyWakeLock != null) {
            Log.v(this, "acquireNotifyWakeLock");
            this.mNotifyWakeLock.acquire(this.mWakeLockTimeOut);
        }
    }
}
