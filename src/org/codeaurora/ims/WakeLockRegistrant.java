package org.codeaurora.ims;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.telephony.utils.AsyncResult;
import org.codeaurora.telephony.utils.Registrant;

/* JADX INFO: loaded from: classes.dex */
public class WakeLockRegistrant extends Registrant {
    public static final long UNSOL_WAKELOCK_TIMEOUT_MS = 200;
    public static final String WAKELOCK_NAME = "NotifyRegistrantLock";
    private PowerManager.WakeLock mNotifyWakeLock;
    private long mWakeLockTimeOut;

    public WakeLockRegistrant(Handler h, int what, Object obj, PowerManager.WakeLock wakeLock, long timeout) {
        super(h, what, obj);
        this.mNotifyWakeLock = wakeLock;
        this.mWakeLockTimeOut = timeout;
    }

    public WakeLockRegistrant(Handler h, int what, Object obj, PowerManager.WakeLock wakeLock) {
        this(h, what, obj, wakeLock, 200L);
    }

    public WakeLockRegistrant(Handler h, int what, Object obj, Context context, long timeout) {
        super(h, what, obj);
        PowerManager pm;
        if (context != null && (pm = (PowerManager) context.getSystemService("power")) != null) {
            PowerManager.WakeLock wakeLockNewWakeLock = pm.newWakeLock(1, WAKELOCK_NAME);
            this.mNotifyWakeLock = wakeLockNewWakeLock;
            if (wakeLockNewWakeLock != null) {
                wakeLockNewWakeLock.setReferenceCounted(false);
            }
        }
        this.mWakeLockTimeOut = timeout;
    }

    public WakeLockRegistrant(Handler h, int what, Object obj, Context context) {
        this(h, what, obj, context, 200L);
    }

    public void notifyRegistrant() {
        acquireNotifyWakeLock();
        super.notifyRegistrant();
    }

    public void notifyResult(Object result) {
        acquireNotifyWakeLock();
        super.notifyResult(result);
    }

    public void notifyException(Throwable exception) {
        acquireNotifyWakeLock();
        super.notifyException(exception);
    }

    public void notifyRegistrant(AsyncResult ar) {
        acquireNotifyWakeLock();
        super.notifyRegistrant(ar);
    }

    private void acquireNotifyWakeLock() {
        if (this.mNotifyWakeLock != null) {
            Log.v(this, "acquireNotifyWakeLock");
            this.mNotifyWakeLock.acquire(this.mWakeLockTimeOut);
        }
    }
}
