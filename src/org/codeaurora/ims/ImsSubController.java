package org.codeaurora.ims;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.qualcomm.ims.utils.Log;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.codeaurora.telephony.utils.AsyncResult;
import org.codeaurora.telephony.utils.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class ImsSubController implements ImsSenderRxr.ImsRadioServiceListener {
    private static final String ACTION_DDS_SWITCH_DONE = "org.codeaurora.intent.action.ACTION_DDS_SWITCH_DONE";
    private static final String ACTION_GET_RADIO_ACCESS_FAMILY_CHANGED = "org.codeaurora.intent.action.ACTION_RADIO_CAPABILITY_UPDATED";
    private static final int DEFAULT_PHONE_ID = 0;
    private static final int EVENT_GET_SUB_CONFIG = 2;
    private static final int EVENT_IMS_SERVICE_DOWN = 4;
    private static final int EVENT_IMS_SERVICE_UP = 3;
    private static final int EVENT_RADIO_AVAILABLE = 5;
    private static final int EVENT_RADIO_NOT_AVAILABLE = 6;
    private static final int EVENT_SUB_CONFIG_CHANGED = 1;
    private static final int INVALID_PHONE_ID = -1;
    private static int mSimultStackCount = 0;
    private static List<Boolean> mStackStatus;
    private final int MAX_VALID_STACK_STATUS_COUNT;
    private boolean[] mActiveStacks;
    private BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    private Handler mHandler;
    private boolean mIsDsdv;
    private boolean mIsReceiverRegistered;
    private List<ImsStateListener> mListeners;
    private int mNumMultiModeStacks;
    private ImsSenderRxr[] mSenderRxrs;
    private List<ImsStackConfigListener> mStackConfigListeners;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTm;

    public interface ImsStackConfigListener {
        void onStackConfigChanged(boolean[] zArr, int i);
    }

    public interface ImsStateListener {
        void onActivateIms(int i);

        void onDeactivateIms(int i);
    }

    public void registerListener(ImsStackConfigListener stackConfigListener, int phoneId) {
        if (isDisposed()) {
            Log.d(this, "returning as ImsSubController is disposed");
            return;
        }
        if (stackConfigListener == null) {
            throw new IllegalArgumentException("stackConfigListener is null!");
        }
        if (!this.mStackConfigListeners.contains(stackConfigListener)) {
            this.mStackConfigListeners.add(stackConfigListener);
        } else {
            Log.w(this, "registerListener :: duplicate stackConfigListener!");
        }
        synchronized (getClass()) {
            notifyStackConfigChanged(this.mActiveStacks, phoneId);
        }
    }

    public boolean unregisterListener(ImsStackConfigListener stackConfigListener) {
        if (isDisposed()) {
            Log.d(this, "returning as ImsSubController is disposed");
            return false;
        }
        if (stackConfigListener == null) {
            throw new IllegalArgumentException("stackConfigListener is null");
        }
        return this.mStackConfigListeners.remove(stackConfigListener);
    }

    public ImsSubController(Context context, ImsSenderRxr[] senderRxrs) {
        this(context, senderRxrs, Looper.getMainLooper());
    }

    public ImsSubController(Context context, ImsSenderRxr[] senderRxrs, Looper looper) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mStackConfigListeners = new CopyOnWriteArrayList();
        this.mNumMultiModeStacks = 0;
        this.mIsReceiverRegistered = false;
        this.MAX_VALID_STACK_STATUS_COUNT = 6;
        this.mActiveStacks = new boolean[6];
        this.mSubscriptionManager = null;
        this.mIsDsdv = true;
        this.mTm = null;
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: org.codeaurora.ims.ImsSubController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (ImsSubController.this.isDisposed()) {
                    Log.d(this, "mBroadcastReceiver onReceive, returning as is disposed");
                    return;
                }
                String action = intent.getAction();
                Log.i(this, "mBroadcastReceiver - " + action);
                if (action.equals(ImsSubController.ACTION_DDS_SWITCH_DONE)) {
                    int ddsSubId = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                    Log.i(this, "got ACTION_DDS_SWITCH_DONE, new DDS = " + ddsSubId);
                    ImsSubController.this.updateActiveImsStackForSubId(ddsSubId);
                } else if (action.equals(ImsSubController.ACTION_GET_RADIO_ACCESS_FAMILY_CHANGED)) {
                    ImsSubController.this.handleRafInfoChange();
                }
            }
        };
        this.mContext = context;
        this.mTm = (TelephonyManager) context.getSystemService("phone");
        this.mSenderRxrs = senderRxrs;
        this.mHandler = new ImsSubControllerHandler(looper);
        for (int i = 0; i < senderRxrs.length; i++) {
            senderRxrs[i].registerForAvailable(this.mHandler, 5, Integer.valueOf(i));
            senderRxrs[i].registerForNotAvailable(this.mHandler, 6, Integer.valueOf(i));
            senderRxrs[i].registerListener(this);
        }
        this.mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
    }

    public void setIsDsdv(boolean isDsdv) {
        this.mIsDsdv = isDsdv;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    @Override // org.codeaurora.ims.ImsSenderRxr.ImsRadioServiceListener
    public void onServiceUp(int phoneId) {
        Log.i(this, "onServiceUp :: phoneId=" + phoneId);
        if (isDisposed()) {
            Log.d(this, "onServiceUp, returning as isDisposed");
            return;
        }
        Message msg = this.mHandler.obtainMessage(3);
        msg.arg1 = phoneId;
        msg.sendToTarget();
    }

    @Override // org.codeaurora.ims.ImsSenderRxr.ImsRadioServiceListener
    public void onServiceDown(int phoneId) {
        Log.i(this, "onServiceDown :: phoneId=" + phoneId);
        if (isDisposed()) {
            Log.d(this, "onServiceDown, returning as isDisposed");
            return;
        }
        Message msg = this.mHandler.obtainMessage(4);
        msg.arg1 = phoneId;
        msg.sendToTarget();
    }

    public void registerListener(ImsStateListener listener) {
        if (isDisposed()) {
            Log.d(this, "registerListener, returning as isDisposed");
        } else {
            if (listener == null) {
                throw new IllegalArgumentException("listener is null");
            }
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
            } else {
                Log.w(this, "Duplicate listener " + listener);
            }
        }
    }

    public boolean unregisterListener(ImsStateListener listener) {
        if (isDisposed()) {
            Log.d(this, "unregisterListener, returning as isDisposed");
            return false;
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        return this.mListeners.remove(listener);
    }

    public static int getDefaultPhoneId() {
        return 0;
    }

    public boolean isDsdv() {
        return this.mIsDsdv;
    }

    private void notifyStackConfigChanged(boolean[] activeStacks, int phoneId) {
        Log.v(this, "notifyStackConfigChanged: activeStacks:" + Arrays.toString(activeStacks) + " phoneId: " + phoneId);
        for (ImsStackConfigListener listener : this.mStackConfigListeners) {
            listener.onStackConfigChanged(activeStacks, phoneId);
        }
    }

    private void handleSubConfigException(Throwable exception) {
        Preconditions.checkArgument(exception != null);
        int errorCode = ((ImsRilException) exception).getErrorCode();
        Log.i(this, "handleSubConfigException error : " + errorCode);
        if (errorCode == 6) {
            this.mIsDsdv = false;
            initSubscriptionStatus();
        } else {
            Log.w(this, "Unhandled error code : " + errorCode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSubConfigChanged(AsyncResult ar) {
        if (ar.exception != null) {
            handleSubConfigException(ar.exception);
            return;
        }
        if (ar.result != null) {
            ImsSubConfigDetails config = (ImsSubConfigDetails) ar.result;
            mSimultStackCount = config.getSimultStackCount();
            mStackStatus = config.getImsStackEnabledList();
            boolean[] activeStacks = new boolean[6];
            for (int i = 0; i < mSimultStackCount; i++) {
                activeStacks[i] = mStackStatus.get(i).booleanValue();
            }
            if (ar.userObj == null) {
                Log.e(this, "handleSubConfigChanged ar.userObj is null");
                return;
            } else {
                notifyStackConfigChanged(activeStacks, ((Integer) ar.userObj).intValue());
                return;
            }
        }
        Log.e(this, "ar.result and ar.exception are null");
    }

    private class ImsSubControllerHandler extends Handler {
        public ImsSubControllerHandler() {
        }

        public ImsSubControllerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(this, "Message received: what = " + msg.what);
            if (ImsSubController.this.isDisposed()) {
                Log.d(this, "handleMessage, returning as isDisposed");
            }
            try {
                switch (msg.what) {
                    case 1:
                    case 2:
                        AsyncResult ar = (AsyncResult) msg.obj;
                        Log.i(this, "Received SubConfig Event phoneId = " + ((Integer) ar.userObj).intValue());
                        ImsSubController.this.handleSubConfigChanged(ar);
                        break;
                    case 3:
                        int phoneId = msg.arg1;
                        Log.i(this, "Received EVENT_IMS_SERVICE_UP phoneId = " + phoneId);
                        if (ImsSubController.this.mTm != null && ImsSubController.this.mTm.getActiveModemCount() > 1) {
                            ImsSubController.this.registerSubConfigChanged(phoneId);
                        } else {
                            Log.i(this, "Single SIM mode");
                            ImsSubController.this.initSubscriptionStatus();
                        }
                        break;
                    case 4:
                        int phoneId2 = msg.arg1;
                        Log.i(this, "Received EVENT_IMS_SERVICE_DOWN phoneId = " + phoneId2);
                        if (ImsSubController.this.mSenderRxrs[phoneId2] != null) {
                            ImsSubController.this.mSenderRxrs[phoneId2].deregisterForImsSubConfigChanged(ImsSubController.this.mHandler);
                        }
                        ImsSubController.this.updateStackConfig(phoneId2, false);
                        break;
                    case 5:
                        int phoneId3 = ((Integer) ((AsyncResult) msg.obj).userObj).intValue();
                        Log.i(this, "Received EVENT_RADIO_AVAILABLE phoneId = " + phoneId3);
                        if (ImsSubController.this.mSenderRxrs[phoneId3] != null) {
                            ImsSubController.this.mSenderRxrs[phoneId3].getImsSubConfig(ImsSubController.this.mHandler.obtainMessage(2, Integer.valueOf(phoneId3)));
                        }
                        break;
                    case 6:
                        int phoneId4 = ((Integer) ((AsyncResult) msg.obj).userObj).intValue();
                        Log.i(this, "Received EVENT_RADIO_NOT_AVAILABLE phoneId = " + phoneId4);
                        ImsSubController.this.updateStackConfig(phoneId4, false);
                        break;
                    default:
                        Log.w(this, "Unknown message = " + msg.what);
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(this, "handleMessage :: Invalid phoneId -1");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSubConfigChanged(int phoneId) {
        this.mSenderRxrs[phoneId].registerForImsSubConfigChanged(this.mHandler, 1, Integer.valueOf(phoneId));
        if (this.mSenderRxrs[phoneId].getRadioState().isAvailable()) {
            this.mSenderRxrs[phoneId].getImsSubConfig(this.mHandler.obtainMessage(2, Integer.valueOf(phoneId)));
        } else {
            Log.w(this, "radio not available");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStackConfig(int phoneId, boolean isEnabled) {
        boolean[] activeStacks;
        Log.v(this, "updateStackConfig phoneId: " + phoneId + " isEnabled: " + isEnabled + " mIsDsdv : " + this.mIsDsdv);
        if (this.mIsDsdv) {
            List<Boolean> list = mStackStatus;
            if (list == null) {
                Log.w(this, "updateStackConfig Stacks are not yet initialized");
                return;
            }
            if (list.get(phoneId).booleanValue() == isEnabled) {
                Log.w(this, "updateStackConfig nothing to update");
                return;
            }
            activeStacks = new boolean[6];
            mStackStatus.set(phoneId, Boolean.valueOf(isEnabled));
            for (int i = 0; i < mSimultStackCount; i++) {
                activeStacks[i] = mStackStatus.get(i).booleanValue();
            }
        } else {
            if (this.mIsReceiverRegistered) {
                Log.v(this, "updateStackConfig: unregistering BroadcastReceiver");
                this.mContext.unregisterReceiver(this.mBroadcastReceiver);
                this.mIsReceiverRegistered = false;
            }
            boolean[] zArr = this.mActiveStacks;
            if (zArr[phoneId] == isEnabled) {
                Log.w(this, "updateStackConfig nothing to update");
                return;
            }
            if (!isEnabled) {
                this.mNumMultiModeStacks = 0;
            }
            zArr[phoneId] = isEnabled;
            activeStacks = this.mActiveStacks;
        }
        notifyStackConfigChanged(activeStacks, phoneId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSubscriptionStatus() {
        mSimultStackCount = 0;
        mStackStatus = null;
        if (this.mTm.getActiveModemCount() > 1) {
            Log.i(this, "initSubscriptionStatus: [Multi-sim] Using RAF and DDS to decide IMS Sub");
            handleRafInfoChange();
            if (this.mNumMultiModeStacks == 0) {
                Log.i(this, "initSubscriptionStatus: registered for RAF info");
                this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter(ACTION_GET_RADIO_ACCESS_FAMILY_CHANGED));
                this.mIsReceiverRegistered = true;
                return;
            }
            return;
        }
        Log.i(this, "initSubscriptionStatus: Not multi-sim.");
        updateActiveImsStackForPhoneId(0);
    }

    public void updateActiveImsStackForPhoneId(int phoneId) {
        if (isDisposed()) {
            Log.d(this, "updateActiveImsStackForPhoneId return as ImsSubController is disposed");
            return;
        }
        if (phoneId == -1) {
            Log.e(this, "switchImsPhone: Invalid phoneId: " + phoneId);
            return;
        }
        // 由 JADX 反編譯修正：JADX 還原成無窮迴圈；依 smali 還原為 for 迴圈 + 通知
        synchronized (getClass()) {
            int i = 0;
            while (true) {
                boolean[] zArr = this.mActiveStacks;
                if (i >= zArr.length) {
                    notifyStackConfigChanged(zArr, phoneId);
                    return;
                }
                if (i == phoneId) {
                    zArr[i] = true;
                } else {
                    zArr[i] = false;
                }
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveImsStackForSubId(int ddsSubId) {
        int phoneId = 0;
        if (SubscriptionManager.isValidSubscriptionId(ddsSubId)) {
            phoneId = SubscriptionManager.getSlotIndex(ddsSubId);
        } else {
            ddsSubId = SubscriptionManager.getDefaultDataSubscriptionId();
            if (SubscriptionManager.isValidSubscriptionId(ddsSubId)) {
                phoneId = SubscriptionManager.getSlotIndex(ddsSubId);
            }
        }
        Log.i(this, "updateActiveImsStackForSubId: new DDS = " + ddsSubId);
        updateActiveImsStackForPhoneId(phoneId);
    }

    private boolean isMultiModeSupported(long nRatMask) {
        return (4096 & nRatMask) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRafInfoChange() {
        if (this.mNumMultiModeStacks > 0) {
            Log.i(this, "handleRafInfoChange: " + this.mNumMultiModeStacks + "Multimode stacks. Handled by DDS switch. EXIT!!!");
            return;
        }
        int numPhones = this.mTm.getActiveModemCount();
        int tempPhoneId = 0;
        for (int i = 0; i < numPhones; i++) {
            SubscriptionInfo subInfo = this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(i);
            if (subInfo != null) {
                int subId = subInfo.getSubscriptionId();
                long rafInfo = this.mTm.createForSubscriptionId(subId).getSupportedRadioAccessFamily();
                Log.i(this, "handleRafInfoChange: Phone: " + i + " Info:" + rafInfo);
                if (isMultiModeSupported(rafInfo)) {
                    this.mNumMultiModeStacks++;
                    tempPhoneId = i;
                }
            } else {
                Log.e(this, "handleRafInfoChange: subIds not valid");
            }
        }
        Log.i(this, "handleRafInfoChange: NumPhones: " + numPhones + "Multiple Multimode stacks: " + this.mNumMultiModeStacks);
        if (this.mNumMultiModeStacks > 1) {
            if (this.mIsReceiverRegistered) {
                this.mContext.unregisterReceiver(this.mBroadcastReceiver);
                this.mIsReceiverRegistered = false;
            }
            updateActiveImsStackForSubId(-1);
            this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter(ACTION_DDS_SWITCH_DONE));
            this.mIsReceiverRegistered = true;
            Log.i(this, "handleRafInfoChange: registered for DDS switch...");
            return;
        }
        this.mNumMultiModeStacks = 0;
        updateActiveImsStackForPhoneId(tempPhoneId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDisposed() {
        return this.mHandler == null;
    }

    public void dispose() {
        if (isDisposed()) {
            Log.d(this, "dispose: returning as already disposed");
            return;
        }
        Log.d(this, "dispose ImsSubController, unregistering handler and listeners");
        int i = 0;
        while (true) {
            ImsSenderRxr[] imsSenderRxrArr = this.mSenderRxrs;
            if (i >= imsSenderRxrArr.length) {
                break;
            }
            imsSenderRxrArr[i].unregisterForAvailable(this.mHandler);
            this.mSenderRxrs[i].unregisterForNotAvailable(this.mHandler);
            this.mSenderRxrs[i].unregisterListener(this);
            this.mSenderRxrs[i].deregisterForImsSubConfigChanged(this.mHandler);
            this.mSenderRxrs[i] = null;
            i++;
        }
        if (this.mIsReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        }
        this.mTm = null;
        this.mHandler = null;
        this.mSenderRxrs = null;
        this.mSubscriptionManager = null;
        this.mStackConfigListeners.clear();
        this.mStackConfigListeners = null;
        this.mBroadcastReceiver = null;
        this.mListeners.clear();
        this.mListeners = null;
    }
}
