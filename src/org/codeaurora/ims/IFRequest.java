package org.codeaurora.ims;

import android.os.Message;
import com.qualcomm.ims.utils.Log;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: compiled from: ImsSenderRxr.java */
/* JADX INFO: loaded from: classes.dex */
class IFRequest {
    private static final int MAX_POOL_SIZE = 4;
    byte[] mData;
    IFRequest mNext;
    int mRequest;
    Message mResult;
    int mSerial;
    static int sNextSerial = 0;
    static Object sSerialMonitor = new Object();
    private static Object sPoolSync = new Object();
    private static IFRequest sPool = null;
    private static int sPoolSize = 0;

    static IFRequest obtain(int request, Message result) {
        IFRequest rr;
        IFRequest rr2 = null;
        synchronized (sPoolSync) {
            IFRequest iFRequest = sPool;
            if (iFRequest != null) {
                rr2 = iFRequest;
                sPool = rr2.mNext;
                rr2.mNext = null;
                sPoolSize--;
            }
        }
        if (rr2 != null) {
            rr = rr2;
        } else {
            IFRequest rr3 = new IFRequest();
            rr = rr3;
        }
        synchronized (sSerialMonitor) {
            int i = sNextSerial;
            sNextSerial = i + 1;
            rr.mSerial = i;
        }
        rr.mRequest = request;
        rr.mResult = result;
        if (result != null && result.getTarget() == null) {
            throw new NullPointerException("Message target must not be null");
        }
        return rr;
    }

    void setResult(Message newMsg) {
        this.mResult = newMsg;
    }

    void release() {
        synchronized (sPoolSync) {
            int i = sPoolSize;
            if (i < 4) {
                this.mNext = sPool;
                sPool = this;
                sPoolSize = i + 1;
                this.mResult = null;
            }
        }
    }

    private IFRequest() {
    }

    static void resetSerial() {
        synchronized (sSerialMonitor) {
            sNextSerial = 0;
        }
    }

    String serialString() {
        StringBuilder sb = new StringBuilder(8);
        String sn = Integer.toString(this.mSerial);
        sb.append('[');
        int s = sn.length();
        for (int i = 0; i < 4 - s; i++) {
            sb.append('0');
        }
        sb.append(sn);
        sb.append(']');
        return sb.toString();
    }

    void onError(int error, Object ret) {
        RuntimeException ex;
        if (error == 0) {
            ex = null;
        } else {
            String errorMsg = ImsSenderRxr.errorIdToString(error);
            ex = new ImsRilException(error, errorMsg);
        }
        Log.i(this, serialString() + "< " + ImsSenderRxr.msgIdToString(this.mRequest) + " error: " + error);
        Message message = this.mResult;
        if (message != null) {
            AsyncResult.forMessage(message, ret, ex);
            this.mResult.sendToTarget();
        }
    }
}
