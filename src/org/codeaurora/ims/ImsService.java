package org.codeaurora.ims;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.ims.feature.MmTelFeature;
import android.telephony.ims.stub.ImsConfigImplBase;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import android.telephony.ims.stub.ImsRegistrationImplBase;
import com.qualcomm.ims.utils.Log;
import com.qualcomm.ims.vt.ImsVideoGlobals;
import org.codeaurora.ims.utils.QtiCarrierConfigHelper;

/* JADX INFO: loaded from: classes.dex */
public class ImsService extends android.telephony.ims.ImsService {
    private static final int INVALID_SLOT_ID = -1;
    private static final String LOG_TAG = "ImsService";
    private static final int UNINITIALIZED_VALUE = -1;
    private static ImsServiceSub[] mServiceSubs;
    private int mNumPhonesCache = -1;
    private ImsSenderRxr[] mSenderRxrs;
    private ImsSubController mSubController;

    private int getNumSlots() {
        if (this.mNumPhonesCache == -1) {
            this.mNumPhonesCache = ((TelephonyManager) getSystemService("phone")).getPhoneCount();
        }
        return this.mNumPhonesCache;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setup() {
        getQtiCarrierConfigHelper().setup(this);
        migrateDb();
        int numSlots = getNumSlots();
        mServiceSubs = new ImsServiceSub[numSlots];
        this.mSenderRxrs = new ImsSenderRxr[numSlots];
        for (int i = 0; i < numSlots; i++) {
            this.mSenderRxrs[i] = new ImsSenderRxr(this, i);
        }
        this.mSubController = new ImsSubController(this, this.mSenderRxrs);
        for (int i2 = 0; i2 < numSlots; i2++) {
            mServiceSubs[i2] = new ImsServiceSub(this, i2, this.mSenderRxrs[i2], this.mSubController);
        }
        ImsVideoGlobals.init(mServiceSubs, this);
    }

    public static ImsServiceSub[] getServiceSubs() {
        return mServiceSubs;
    }

    private void migrateDb() {
        int rttMode = Settings.Global.getInt(getContentResolver(), "rtt_mode", -1);
        if (rttMode != -1) {
            Log.v(this, "upgradeDb: migrate to new db key for rtt. mode=" + rttMode);
            Settings.Secure.putInt(getContentResolver(), "rtt_calling_mode", rttMode);
            Settings.Global.putInt(getContentResolver(), "rtt_mode", -1);
        }
    }

    public void onCreate() {
        super.onCreate();
        Log.i(this, "ImsService created!");
        setup();
    }

    public ImsFeatureConfiguration querySupportedImsFeatures() {
        ImsFeatureConfiguration.Builder features = new ImsFeatureConfiguration.Builder();
        for (int i = 0; i < getNumSlots(); i++) {
            features.addFeature(i, 1).addFeature(i, 0);
        }
        return features.build();
    }

    public void readyForFeatureCreation() {
        Log.i(this, "readyForFeatureCreation :: No-op");
    }

    public void enableIms(int slotId) {
        Log.i(this, "enableIms :: slotId=" + slotId);
        if (slotId > -1 && slotId < getNumSlots()) {
            mServiceSubs[slotId].turnOnIms();
        } else {
            Log.e(this, "enableIms :: Invalid slotId " + slotId);
        }
    }

    public void disableIms(int slotId) {
        Log.i(this, "disableIms :: slotId=" + slotId);
        if (slotId > -1 && slotId < getNumSlots()) {
            mServiceSubs[slotId].turnOffIms();
        } else {
            Log.e(this, "disableIms :: Invalid slotId " + slotId);
        }
    }

    public MmTelFeature createMmTelFeature(int slotId) {
        Log.d(this, "createMmTelFeature :: slotId=" + slotId);
        if (slotId > -1 && slotId < getNumSlots()) {
            return mServiceSubs[slotId];
        }
        Log.e(this, "createMmTelFeature :: Invalid slotId " + slotId);
        return null;
    }

    public ImsConfigImplBase getConfig(int slotId) {
        Log.d(this, "getConfig :: slotId=" + slotId);
        if (slotId > -1 && slotId < getNumSlots()) {
            return mServiceSubs[slotId].getConfigInterface();
        }
        Log.e(this, "getConfig :: invalid slotId=" + slotId);
        return null;
    }

    public ImsRegistrationImplBase getRegistration(int slotId) {
        Log.d(this, "getRegistration :: slotId=" + slotId);
        if (slotId > -1 && slotId < getNumSlots()) {
            return mServiceSubs[slotId].getImsRegistrationInterface();
        }
        Log.e(this, "getRegistration :: invalid slotId=" + slotId);
        return null;
    }

    public void onDestroy() {
        Log.i(this, "Ims Service Destroyed Successfully...");
        ImsVideoGlobals.getInstance().dispose();
        ImsSubController imsSubController = this.mSubController;
        if (imsSubController != null) {
            imsSubController.dispose();
        }
        for (int i = 0; i < getNumSlots(); i++) {
            mServiceSubs[i].dispose();
            mServiceSubs[i] = null;
            this.mSenderRxrs[i] = null;
        }
        mServiceSubs = null;
        this.mSenderRxrs = null;
        this.mSubController = null;
        getQtiCarrierConfigHelper().teardown();
        super.onDestroy();
    }

    public QtiCarrierConfigHelper getQtiCarrierConfigHelper() {
        return QtiCarrierConfigHelper.getInstance();
    }
}
