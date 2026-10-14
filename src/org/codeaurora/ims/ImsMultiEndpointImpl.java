package org.codeaurora.ims;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ims.ImsExternalCallState;
import android.telephony.ims.stub.ImsMultiEndpointImplBase;
import android.util.Log;
import android.util.Pair;
import java.util.List;
import org.codeaurora.ims.parser.ImsViceParser;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsMultiEndpointImpl extends ImsMultiEndpointImplBase {
    private static final String TAG = "ImsMultiEndpointImpl";
    private ImsSenderRxr mCi;
    private Context mContext;
    private ImsViceParser mImsViceParser;
    private ImsServiceSub mServiceSub;
    private final int EVENT_REFRESH_VICE_INFO = 1;
    private final int EVENT_CAPABILITIES_CHANGED = 2;
    private Handler mHandler = new ImsMultiEndpointImplHandler(this);
    private boolean mIsVideoSupported = false;
    private boolean mIsVoiceSupported = false;

    public ImsMultiEndpointImpl(ImsSenderRxr ci, Context context, ImsServiceSub serviceSub) {
        this.mContext = context;
        this.mCi = ci;
        this.mServiceSub = serviceSub;
        this.mImsViceParser = new ImsViceParser(this.mContext);
        this.mCi.registerForViceRefreshInfo(this.mHandler, 1, null);
        this.mServiceSub.registerForCapabilitiesChanged(this.mHandler, 2, null);
    }

    private class ImsMultiEndpointImplHandler extends Handler {
        ImsMultiEndpointImplHandler(ImsMultiEndpointImpl imsMultiEndpointImpl) {
            this(Looper.getMainLooper());
        }

        ImsMultiEndpointImplHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.d(ImsMultiEndpointImpl.TAG, "Message received: what = " + msg.what);
            AsyncResult ar = (AsyncResult) msg.obj;
            switch (msg.what) {
                case 1:
                    Log.d(ImsMultiEndpointImpl.TAG, "EVENT_REFRESH_VICE_INFO");
                    ImsMultiEndpointImpl.this.handleRefreshViceInfo(ar);
                    break;
                case 2:
                    Log.d(ImsMultiEndpointImpl.TAG, "EVENT_CAPABILITIES_CHANGED");
                    ImsMultiEndpointImpl.this.updateCapabilities(ar);
                    break;
                default:
                    Log.d(ImsMultiEndpointImpl.TAG, "Unknown message = " + msg.what);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRefreshViceInfo(AsyncResult ar) {
        if (ar == null || ar.exception != null || ar.result == null) {
            Log.d(TAG, "handleRefreshViceInfo exception");
            return;
        }
        ViceUriInfo viceInfo = (ViceUriInfo) ar.result;
        byte[] refreshViceInfoUri = viceInfo.getViceInfoUri();
        if (refreshViceInfoUri == null || refreshViceInfoUri.length < 0) {
            Log.d(TAG, "handleRefreshViceInfo: refreshViceInfoUri null");
        } else {
            processViceInfo(refreshViceInfoUri);
        }
    }

    private void processViceInfo(byte[] viceInfoBytes) {
        this.mImsViceParser.updateViceXmlBytes(viceInfoBytes);
        notifyViceInfo();
    }

    private void notifyViceInfo() {
        List<ImsExternalCallState> dialogIds = this.mImsViceParser.getCallPullInfo(this.mIsVideoSupported, this.mIsVoiceSupported);
        try {
            Log.i(TAG, "Calling onImsExternalCallStateUpdate");
            onImsExternalCallStateUpdate(dialogIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCapabilities(AsyncResult ar) {
        if (ar == null || ar.exception != null || ar.result == null) {
            Log.d(TAG, "updateCapabilities exception");
            return;
        }
        Pair<Boolean, Boolean> capabilites = (Pair) ar.result;
        this.mIsVideoSupported = ((Boolean) capabilites.first).booleanValue();
        this.mIsVoiceSupported = ((Boolean) capabilites.second).booleanValue();
        Log.d(TAG, "updateCapabilities:: Video = " + this.mIsVideoSupported + ", Voice = " + this.mIsVoiceSupported);
        notifyViceInfo();
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean getIsVoiceSupported() {
        return this.mIsVoiceSupported;
    }

    public boolean getIsVideoSupported() {
        return this.mIsVideoSupported;
    }
}
