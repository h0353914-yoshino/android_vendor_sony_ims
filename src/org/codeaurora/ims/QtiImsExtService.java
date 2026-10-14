package org.codeaurora.ims;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.qualcomm.ims.utils.Log;

/* JADX INFO: loaded from: classes.dex */
public class QtiImsExtService extends Service {
    private QtiImsExt mQtiImsExt;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.i(this, "QtiImsExtService created!");
        createQtiImsExt();
    }

    private void createQtiImsExt() {
        ImsServiceSub[] imsServiceSubs = ImsService.getServiceSubs();
        if (imsServiceSubs != null) {
            this.mQtiImsExt = new QtiImsExt(this, imsServiceSubs);
        } else {
            Log.e(this, "QtiImsExtService, ImsService is not yet started retry.");
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.d(this, "onBind " + this.mQtiImsExt);
        if (this.mQtiImsExt == null) {
            createQtiImsExt();
        }
        QtiImsExt qtiImsExt = this.mQtiImsExt;
        if (qtiImsExt == null) {
            Log.w(this, "onBind returned null");
            return null;
        }
        return qtiImsExt.getBinder();
    }
}
