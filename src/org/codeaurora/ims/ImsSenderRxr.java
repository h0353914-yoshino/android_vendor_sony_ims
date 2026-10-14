package org.codeaurora.ims;

import android.content.Context;
import android.content.Intent;
import android.hidl.manager.V1_0.IServiceManager;
import android.hidl.manager.V1_0.IServiceNotification;
import android.location.Address;
import android.os.Handler;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.sysprop.TelephonyProperties;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;
import com.qualcomm.ims.utils.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.codeaurora.ims.sms.IncomingSms;
import org.codeaurora.ims.sms.SmsResponse;
import org.codeaurora.ims.sms.StatusReport;
import org.codeaurora.ims.utils.QtiImsExtUtils;
import org.codeaurora.telephony.utils.AsyncResult;
import org.codeaurora.telephony.utils.Registrant;
import org.codeaurora.telephony.utils.RegistrantList;
import org.codeaurora.telephony.utils.SomeArgs;
import vendor.qti.hardware.radio.ims.V1_0.BlockStatus;
import vendor.qti.hardware.radio.ims.V1_0.CallForwardInfo;
import vendor.qti.hardware.radio.ims.V1_0.CallModifyInfo;
import vendor.qti.hardware.radio.ims.V1_0.CbNumListInfo;
import vendor.qti.hardware.radio.ims.V1_0.CfData;
import vendor.qti.hardware.radio.ims.V1_0.ClipProvisionStatus;
import vendor.qti.hardware.radio.ims.V1_0.ClirInfo;
import vendor.qti.hardware.radio.ims.V1_0.ConferenceInfo;
import vendor.qti.hardware.radio.ims.V1_0.ConfigInfo;
import vendor.qti.hardware.radio.ims.V1_0.DeflectRequestInfo;
import vendor.qti.hardware.radio.ims.V1_0.DtmfInfo;
import vendor.qti.hardware.radio.ims.V1_0.ExplicitCallTransferInfo;
import vendor.qti.hardware.radio.ims.V1_0.HandoverInfo;
import vendor.qti.hardware.radio.ims.V1_0.IImsRadio;
import vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication;
import vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse;
import vendor.qti.hardware.radio.ims.V1_0.ImsSubConfigInfo;
import vendor.qti.hardware.radio.ims.V1_0.MessageWaitingIndication;
import vendor.qti.hardware.radio.ims.V1_0.ParticipantStatusInfo;
import vendor.qti.hardware.radio.ims.V1_0.RegistrationInfo;
import vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo;
import vendor.qti.hardware.radio.ims.V1_0.SipErrorInfo;
import vendor.qti.hardware.radio.ims.V1_0.SsInfoData;
import vendor.qti.hardware.radio.ims.V1_0.StkCcUnsolSsResult;
import vendor.qti.hardware.radio.ims.V1_0.SuppServiceNotification;
import vendor.qti.hardware.radio.ims.V1_0.TtyInfo;
import vendor.qti.hardware.radio.ims.V1_0.ViceInfo;
import vendor.qti.hardware.radio.ims.V1_2.AddressInfo;
import vendor.qti.hardware.radio.ims.V1_2.ImsSmsMessage;
import vendor.qti.hardware.radio.ims.V1_2.ImsSmsSendStatusReport;
import vendor.qti.hardware.radio.ims.V1_2.IncomingImsSms;
import vendor.qti.hardware.radio.ims.V1_3.ColrInfo;
import vendor.qti.hardware.radio.ims.V1_3.HangupRequestInfo;
import vendor.qti.hardware.radio.ims.V1_3.SuppServiceStatus;
import vendor.qti.hardware.radio.ims.V1_4.DialRequest;
import vendor.qti.hardware.radio.ims.V1_4.MultiIdentityLineInfoHal;
import vendor.qti.hardware.radio.ims.V1_6.AutoCallRejectionInfo;
import vendor.qti.hardware.radio.ims.V1_7.CallInfo;

/* JADX INFO: loaded from: classes.dex */
public class ImsSenderRxr extends ImsPhoneBaseCommands {
    private static final int DEFAULT_WAKE_LOCK_TIMEOUT = 60000;
    static final int EVENT_WAKE_LOCK_TIMEOUT = 1;
    static final String[] IIMS_RADIO_SERVICE_NAME = {"imsradio0", "imsradio1", "imsradio2"};
    static final String LOG_TAG = "ImsSenderRxr";
    private static final int MSG_TAG_LENGTH = 1;
    private static final int PDU_LENGTH_OFFSET = 4;
    private static final int STATUS_INTERROGATE = 2;
    static final int ZERO_SECONDS = 0;
    private RegistrantList mAutoRejectRegistrants;
    final ImsRadioDeathRecipient mDeathRecipient;
    private RegistrantList mGeoLocationDataStatusRegistrants;
    private RegistrantList mGeolocationRegistrants;
    private final Object mHalSync;
    private RegistrantList mHandoverStatusRegistrants;
    private volatile IImsRadio mImsRadio;
    final ImsRadioHandler mImsRadioHandler;
    private IImsRadioIndication mImsRadioIndication;
    private IImsRadioResponse mImsRadioResponse;
    private volatile vendor.qti.hardware.radio.ims.V1_2.IImsRadio mImsRadioV12;
    private volatile vendor.qti.hardware.radio.ims.V1_3.IImsRadio mImsRadioV13;
    private volatile vendor.qti.hardware.radio.ims.V1_4.IImsRadio mImsRadioV14;
    private volatile vendor.qti.hardware.radio.ims.V1_5.IImsRadio mImsRadioV15;
    private volatile vendor.qti.hardware.radio.ims.V1_6.IImsRadio mImsRadioV16;
    private volatile vendor.qti.hardware.radio.ims.V1_7.IImsRadio mImsRadioV17;
    private RegistrantList mImsSubConfigChangeRegistrants;
    private Registrant mIncomingSmsRegistrant;
    private boolean mIsUnsolCallListPresent;
    private List<ImsRadioServiceListener> mListeners;
    protected RegistrantList mModifyCallRegistrants;
    private RegistrantList mMultiIdentityInfoPendingRegistrants;
    private RegistrantList mMultiIdentityStatusChangeRegistrants;
    protected RegistrantList mMwiRegistrants;
    private RegistrantList mParticipantStatusRegistrants;
    private Integer mPhoneId;
    private RegistrantList mRadioStateRegistrations;
    private RegistrantList mRefreshConfInfoRegistrations;
    private RegistrantList mRegistrationBlockStatusRegistrants;
    int mRequestMessagesWaiting;
    ArrayList<IFRequest> mRequestsList;
    private RegistrantList mRttMessageRegistrants;
    private RegistrantList mRttModifyRegistrants;
    private Registrant mSendSmsStatusReportRegistrant;
    private final ImsRadioServiceNotification mServiceNotification;
    private RegistrantList mSipDtmfInfoRegistrants;
    private RegistrantList mSrvStatusRegistrations;
    protected Registrant mSsIndicationRegistrant;
    private RegistrantList mSsaccRegistrants;
    protected Registrant mSsnRegistrant;
    AtomicBoolean mTestingEmergencyCall;
    private RegistrantList mTtyStatusRegistrants;
    private RegistrantList mUssdInfoRegistrants;
    private RegistrantList mVoWiFiCallQualityRegistrants;
    private RegistrantList mVoiceInfoStatusRegistrants;
    private RegistrantList mVopsRegistrants;
    PowerManager.WakeLock mWakeLock;
    int mWakeLockTimeout;
    private boolean mWfcRoamingConfigurationSupport;
    private RegistrantList mWfcRoamingModeConfigRegistrants;

    public interface ImsRadioServiceListener {
        void onServiceDown(int i);

        void onServiceUp(int i);
    }

    public void registerListener(ImsRadioServiceListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
                if (imsRadioV10() != null) {
                    listener.onServiceUp(this.mPhoneId.intValue());
                } else {
                    listener.onServiceDown(this.mPhoneId.intValue());
                }
            } else {
                Log.e(this, "Duplicate listener " + listener);
            }
        }
    }

    public void unregisterListener(ImsRadioServiceListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        synchronized (this.mListeners) {
            if (this.mListeners.contains(listener)) {
                this.mListeners.remove(listener);
            } else {
                Log.e(this, "Listener not found " + listener);
            }
        }
    }

    private void notifyServiceUp(String instanceName) {
        Log.i(this, "onServiceUp : " + instanceName);
        synchronized (this.mListeners) {
            for (ImsRadioServiceListener l : this.mListeners) {
                l.onServiceUp(this.mPhoneId.intValue());
            }
        }
    }

    private void notifyServiceDown(String instanceName) {
        Log.i(this, "onServiceDown : " + instanceName);
        synchronized (this.mListeners) {
            for (ImsRadioServiceListener l : this.mListeners) {
                l.onServiceDown(this.mPhoneId.intValue());
            }
        }
    }

    final class ImsRadioDeathRecipient implements IHwBinder.DeathRecipient {
        ImsRadioDeathRecipient() {
        }

        public void serviceDied(long cookie) {
            Log.e(this, " IImsRadio Died");
            ImsSenderRxr.this.resetServiceAndRequestList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resetServiceAndRequestList() {
        notifyServiceDown(IIMS_RADIO_SERVICE_NAME[this.mPhoneId.intValue()]);
        clearRequestsList(1, false);
        resetHalInterfaces();
    }

    private void resetHalInterfaces() {
        Log.d(this, "resetHalInterfaces: Resetting HAL interfaces.");
        synchronized (this.mHalSync) {
            this.mImsRadio = null;
            this.mImsRadioV12 = null;
            this.mImsRadioV13 = null;
            this.mImsRadioV14 = null;
            this.mImsRadioV15 = null;
            this.mImsRadioV16 = null;
            this.mImsRadioV17 = null;
            this.mImsRadioResponse = null;
            this.mImsRadioIndication = null;
        }
    }

    final class ImsRadioServiceNotification extends IServiceNotification.Stub {
        ImsRadioServiceNotification() {
        }

        public void onRegistration(String fqName, String name, boolean preexisting) {
            Log.v(this, "Ims Radio interface service started " + fqName + " " + name + " preexisting =" + preexisting);
            ImsSenderRxr.this.initImsRadio();
        }
    }

    private void registerForImsRadioServiceNotification() {
        try {
            boolean ret = IServiceManager.getService().registerForNotifications(IImsRadio.kInterfaceName, IIMS_RADIO_SERVICE_NAME[this.mPhoneId.intValue()], this.mServiceNotification);
            if (!ret) {
                Log.e(this, "Failed to register for service start notifications");
            }
        } catch (RemoteException ex) {
            Log.e(this, "Failed to register for service start notifications. Exception " + ex);
        }
    }

    private IImsRadio imsRadioV10() {
        IImsRadio imsRadio;
        synchronized (this.mHalSync) {
            imsRadio = this.mImsRadio;
        }
        return imsRadio;
    }

    private vendor.qti.hardware.radio.ims.V1_2.IImsRadio imsRadioV12() {
        vendor.qti.hardware.radio.ims.V1_2.IImsRadio imsRadio;
        synchronized (this.mHalSync) {
            imsRadio = this.mImsRadioV12;
        }
        return imsRadio;
    }

    private vendor.qti.hardware.radio.ims.V1_3.IImsRadio imsRadioV13() {
        vendor.qti.hardware.radio.ims.V1_3.IImsRadio imsRadio;
        synchronized (this.mHalSync) {
            imsRadio = this.mImsRadioV13;
        }
        return imsRadio;
    }

    private vendor.qti.hardware.radio.ims.V1_4.IImsRadio imsRadioV14() {
        vendor.qti.hardware.radio.ims.V1_4.IImsRadio imsRadio;
        synchronized (this.mHalSync) {
            imsRadio = this.mImsRadioV14;
        }
        return imsRadio;
    }

    private vendor.qti.hardware.radio.ims.V1_5.IImsRadio imsRadioV15() {
        vendor.qti.hardware.radio.ims.V1_5.IImsRadio imsRadio;
        synchronized (this.mHalSync) {
            imsRadio = this.mImsRadioV15;
        }
        return imsRadio;
    }

    private vendor.qti.hardware.radio.ims.V1_6.IImsRadio imsRadioV16() {
        vendor.qti.hardware.radio.ims.V1_6.IImsRadio imsRadio;
        synchronized (this.mHalSync) {
            imsRadio = this.mImsRadioV16;
        }
        return imsRadio;
    }

    private vendor.qti.hardware.radio.ims.V1_7.IImsRadio imsRadioV17() {
        vendor.qti.hardware.radio.ims.V1_7.IImsRadio imsRadio;
        synchronized (this.mHalSync) {
            imsRadio = this.mImsRadioV17;
        }
        return imsRadio;
    }

    private static String toAvailability(boolean v) {
        return v ? "available" : "unavailable";
    }

    public boolean isSmsSupported() {
        return this.mImsRadioV12 != null;
    }

    public boolean isCrsSupported() {
        return this.mImsRadioV17 != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initImsRadio() {
        try {
            String[] strArr = IIMS_RADIO_SERVICE_NAME;
            IImsRadio imsRadio = IImsRadio.getService(strArr[this.mPhoneId.intValue()]);
            if (imsRadio == null) {
                resetHalInterfaces();
                Log.e(this, "initImsRadio: imsRadio is null.");
                return;
            }
            boolean z = true;
            Log.i(this, "initImsRadio: imsRadioV10 availability: " + toAvailability(imsRadio != null));
            vendor.qti.hardware.radio.ims.V1_1.IImsRadio imsRadioV11 = vendor.qti.hardware.radio.ims.V1_1.IImsRadio.castFrom((IHwInterface) imsRadio);
            Log.i(this, "initImsRadio: imsRadioV11 availability: " + toAvailability(imsRadioV11 != null));
            vendor.qti.hardware.radio.ims.V1_2.IImsRadio imsRadioV12 = vendor.qti.hardware.radio.ims.V1_2.IImsRadio.castFrom((IHwInterface) imsRadio);
            Log.i(this, "initImsRadio: imsRadioV12 availability: " + toAvailability(imsRadioV12 != null));
            vendor.qti.hardware.radio.ims.V1_3.IImsRadio imsRadioV13 = vendor.qti.hardware.radio.ims.V1_3.IImsRadio.castFrom((IHwInterface) imsRadio);
            Log.i(this, "initImsRadio: imsRadioV13 availability: " + toAvailability(imsRadioV13 != null));
            vendor.qti.hardware.radio.ims.V1_4.IImsRadio imsRadioV14 = vendor.qti.hardware.radio.ims.V1_4.IImsRadio.castFrom((IHwInterface) imsRadio);
            Log.i(this, "initImsRadio: imsRadioV14 availability: " + toAvailability(imsRadioV14 != null));
            vendor.qti.hardware.radio.ims.V1_5.IImsRadio imsRadioV15 = vendor.qti.hardware.radio.ims.V1_5.IImsRadio.castFrom((IHwInterface) imsRadio);
            Log.i(this, "initImsRadio: imsRadioV15 availability: " + toAvailability(imsRadioV15 != null));
            vendor.qti.hardware.radio.ims.V1_6.IImsRadio imsRadioV16 = vendor.qti.hardware.radio.ims.V1_6.IImsRadio.castFrom((IHwInterface) imsRadio);
            Log.i(this, "initImsRadio: imsRadioV16 availability: " + toAvailability(imsRadioV16 != null));
            vendor.qti.hardware.radio.ims.V1_7.IImsRadio imsRadioV17 = vendor.qti.hardware.radio.ims.V1_7.IImsRadio.castFrom((IHwInterface) imsRadio);
            StringBuilder sbAppend = new StringBuilder().append("initImsRadio: imsRadioV17 availability: ");
            if (imsRadioV17 == null) {
                z = false;
            }
            Log.i(this, sbAppend.append(toAvailability(z)).toString());
            ImsRadioResponse imsRadioResponse = new ImsRadioResponse();
            ImsRadioIndication imsRadioIndication = new ImsRadioIndication();
            synchronized (this.mHalSync) {
                this.mImsRadioResponse = imsRadioResponse;
                this.mImsRadioIndication = imsRadioIndication;
                imsRadio.setCallback(imsRadioResponse, imsRadioIndication);
                imsRadio.linkToDeath(this.mDeathRecipient, 0L);
                this.mImsRadio = imsRadio;
                this.mImsRadioV12 = imsRadioV12;
                this.mImsRadioV13 = imsRadioV13;
                this.mImsRadioV14 = imsRadioV14;
                this.mImsRadioV15 = imsRadioV15;
                this.mImsRadioV16 = imsRadioV16;
                this.mImsRadioV17 = imsRadioV17;
            }
            notifyServiceUp(strArr[this.mPhoneId.intValue()]);
        } catch (Exception ex) {
            Log.e(this, "initImsRadio: Exception: " + ex);
            resetServiceAndRequestList();
        }
    }

    public void registerForImsSubConfigChanged(Handler h, int what, Object obj) {
        this.mImsSubConfigChangeRegistrants.addUnique(h, what, obj);
    }

    public void deregisterForImsSubConfigChanged(Handler h) {
        this.mImsSubConfigChangeRegistrants.remove(h);
    }

    public void registerForSsacStatusChanged(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mSsaccRegistrants.add(r);
    }

    public void unregisterForSsacStatusChanged(Handler h) {
        this.mSsaccRegistrants.remove(h);
    }

    public void registerForVopsStatusChanged(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mVopsRegistrants.add(r);
    }

    public void unregisterForVopsStatusChanged(Handler h) {
        this.mVopsRegistrants.remove(h);
    }

    public void registerForWfcRoamingModeFeatureSupport(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mWfcRoamingModeConfigRegistrants.add(r);
        this.mWfcRoamingModeConfigRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.valueOf(this.mWfcRoamingConfigurationSupport), (Throwable) null));
    }

    public void unRegisterForWfcRoamingModeFeatureSupport(Handler h) {
        this.mWfcRoamingModeConfigRegistrants.remove(h);
    }

    public void registerForHandoverStatusChanged(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mHandoverStatusRegistrants.add(r);
    }

    public void unregisterForHandoverStatusChanged(Handler h) {
        this.mHandoverStatusRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForRefreshConfInfo(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mRefreshConfInfoRegistrations.add(r);
    }

    public void unregisterForRefreshConfInfo(Handler h) {
        this.mRefreshConfInfoRegistrations.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForSrvStatusUpdate(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mSrvStatusRegistrations.add(r);
    }

    public void unregisterForSrvStatusUpdate(Handler h) {
        this.mSrvStatusRegistrations.remove(h);
    }

    public void registerForTtyStatusChanged(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mTtyStatusRegistrants.add(r);
    }

    public void unregisterForTtyStatusChanged(Handler h) {
        this.mTtyStatusRegistrants.remove(h);
    }

    public void registerForGeolocationRequest(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mGeolocationRegistrants.add(r);
    }

    public void unregisterForGeolocationRequest(Handler h) {
        this.mGeolocationRegistrants.remove(h);
    }

    public void registerForVoWiFiCallQualityUpdate(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mVoWiFiCallQualityRegistrants.add(r);
    }

    public void unregisterForVoWiFiCallQualityUpdate(Handler h) {
        this.mVoWiFiCallQualityRegistrants.remove(h);
    }

    public void registerForParticipantStatusInfo(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mParticipantStatusRegistrants.add(r);
    }

    public void unregisterForParticipantStatusInfo(Handler h) {
        this.mParticipantStatusRegistrants.remove(h);
    }

    public void registerForRegistrationBlockStatus(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mRegistrationBlockStatusRegistrants.add(r);
    }

    public void unregisterForRegistrationBlockStatus(Handler h) {
        this.mRegistrationBlockStatusRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneBaseCommands, org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setOnSuppServiceNotification(Handler h, int what, Object obj) {
        this.mSsnRegistrant = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
    }

    @Override // org.codeaurora.ims.ImsPhoneBaseCommands, org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unSetOnSuppServiceNotification(Handler h) {
        this.mSsnRegistrant.clear();
    }

    public void registerForSuppServiceIndication(Handler h, int what, Object obj) {
        this.mSsIndicationRegistrant = new Registrant(h, what, obj);
    }

    public void unregisterForSuppServiceIndication(Handler h) {
        this.mSsIndicationRegistrant.clear();
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForUssdInfo(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mUssdInfoRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForUssdInfo(Handler h) {
        this.mUssdInfoRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForSipDtmfInfo(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mSipDtmfInfoRegistrants.add(r);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void unregisterForSipDtmfInfo(Handler h) {
        this.mSipDtmfInfoRegistrants.remove(h);
    }

    public void setOnIncomingImsSms(Handler h, int what, Object obj) {
        this.mIncomingSmsRegistrant = new Registrant(h, what, obj);
    }

    public void unSetIncomingImsSms(Handler h) {
        Registrant registrant = this.mIncomingSmsRegistrant;
        if (registrant != null) {
            registrant.clear();
            this.mIncomingSmsRegistrant = null;
        }
    }

    public void setOnImsSmsStatusReport(Handler h, int what, Object obj) {
        this.mSendSmsStatusReportRegistrant = new Registrant(h, what, obj);
    }

    public void unSetImsSmsStatusReport(Handler h) {
        Registrant registrant = this.mSendSmsStatusReportRegistrant;
        if (registrant != null) {
            registrant.clear();
            this.mSendSmsStatusReportRegistrant = null;
        }
    }

    public void registerForCallAutoRejection(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mAutoRejectRegistrants.add(r);
    }

    public void unregisterForCallAutoRejection(Handler h) {
        this.mAutoRejectRegistrants.remove(h);
    }

    public void registerForMultiIdentityRegistrationStatusChange(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mMultiIdentityStatusChangeRegistrants.add(r);
    }

    public void registerForMultiIdentityInfoPendingIndication(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mMultiIdentityInfoPendingRegistrants.add(r);
    }

    public void unregisterForMultiIdentityRegistrationStatusChange(Handler h) {
        this.mMultiIdentityStatusChangeRegistrants.remove(h);
    }

    public void unregisterForMultiIdentityPendingInfoRequest(Handler h) {
        this.mMultiIdentityInfoPendingRegistrants.remove(h);
    }

    public void registerForGeoLocationDataStatus(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mGeoLocationDataStatusRegistrants.add(r);
    }

    public void unregisterForGeoLocationDataStatus(Handler h) {
        this.mGeoLocationDataStatusRegistrants.remove(h);
    }

    public Registrant getIncomingSmsRegistrant() {
        return this.mIncomingSmsRegistrant;
    }

    public Registrant getSendSmsStatusReportRegistrant() {
        return this.mSendSmsStatusReportRegistrant;
    }

    class ImsRadioHandler extends Handler {
        ImsRadioHandler(ImsSenderRxr this$0) {
            this(Looper.getMainLooper());
        }

        ImsRadioHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            IFRequest rr = (IFRequest) msg.obj; // 由 JADX 反編譯修正：依 smali 補回 check-cast
            switch (msg.what) {
                case 1:
                    synchronized (ImsSenderRxr.this.mWakeLock) {
                        if (ImsSenderRxr.this.mWakeLock.isHeld()) {
                            if (ImsSenderRxr.this.mRequestMessagesWaiting != 0) {
                                Log.i(this, "Number of messages still waiting for response " + ImsSenderRxr.this.mRequestMessagesWaiting + " at TIMEOUT. Reset to 0");
                                ImsSenderRxr.this.mRequestMessagesWaiting = 0;
                                synchronized (ImsSenderRxr.this.mRequestsList) {
                                    int count = ImsSenderRxr.this.mRequestsList.size();
                                    Log.i(this, "WAKE_LOCK_TIMEOUT  mRequestList=" + count);
                                    for (int i = 0; i < count; i++) {
                                        rr = ImsSenderRxr.this.mRequestsList.get(i);
                                        Log.i(this, i + ": [" + rr.mSerial + "] " + ImsSenderRxr.msgIdToString(rr.mRequest));
                                    }
                                }
                            }
                            ImsSenderRxr.this.mWakeLock.release();
                        }
                        break;
                    }
                default:
                    return;
            }
        }
    }

    class ImsRadioResponse extends vendor.qti.hardware.radio.ims.V1_7.IImsRadioResponse.Stub {
        ImsRadioResponse() {
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void dialResponse(int token, int errorCode) {
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioResponse
        public void sendImsSmsResponse_1_5(int token, int messageRef, int smsStatusResult, int reason) {
            Log.i(this, "Ims sms response received");
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            SmsResponse response = ImsRadioUtils.imsSmsResponsefromHidl(messageRef, smsStatusResult, reason);
            ImsSenderRxr.this.sendResponse(rr, 0, response);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioResponse
        public void sendImsSmsResponse(int token, int messageRef, int smsStatusResult, int reason) {
            sendImsSmsResponse_1_5(token, messageRef, smsStatusResult, reason);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void setConfigResponse(int token, int errorCode, ConfigInfo config) {
            setConfigResponse_1_6(token, errorCode, ImsRadioUtilsV16.migrateConfigInfoFrom(config));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioResponse
        public void setConfigResponse_1_6(int token, int errorCode, vendor.qti.hardware.radio.ims.V1_6.ConfigInfo config) {
            Log.i(this, "Set config response received.");
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            Object ret = ImsSenderRxr.this.processConfigResponse(config);
            ImsSenderRxr.this.sendResponse(rr, errorCode, ret);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getConfigResponse(int token, int errorCode, ConfigInfo config) {
            Log.i(this, "Get config response received.");
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            Object ret = ImsSenderRxr.this.processConfigResponse(ImsRadioUtilsV16.migrateConfigInfoFrom(config));
            ImsSenderRxr.this.sendResponse(rr, errorCode, ret);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void queryServiceStatusResponse(int token, int errorCode, ArrayList<ServiceStatusInfo> serviceStatusInfoList) {
            Log.i(this, "queryServiceStatusResponse()");
            queryServiceStatusResponse_1_6(token, errorCode, ImsRadioUtilsV16.migrateServiceStatusInfo(serviceStatusInfoList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioResponse
        public void queryServiceStatusResponse_1_6(int token, int errorCode, ArrayList<vendor.qti.hardware.radio.ims.V1_6.ServiceStatusInfo> infoList) {
            Log.i(this, "queryServiceStatusResponse_1_6()");
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            Object ret = null;
            if (infoList != null) {
                ret = ImsRadioUtils.handleSrvStatus(infoList);
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, ret);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void setServiceStatusResponse(int token, int errorCode) {
            Log.i(this, "SetServiceStatus response received.");
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                Log.e(this, "SetServiceStatusResponsse rr is NULL");
                return;
            }
            try {
                SomeArgs setCapArgs = (SomeArgs) rr.mResult.obj;
                Message orgMsg = (Message) setCapArgs.arg1;
                rr.setResult(orgMsg);
                ImsSenderRxr.this.sendResponse(rr, errorCode, (ArrayList) setCapArgs.arg2);
            } catch (ClassCastException ex) {
                Log.e(this, "setServiceStatusResponse exception = " + ex);
            }
        }

        private void sendSipErrorInfo(int token, int errorCode, SipErrorInfo errorInfo) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            ImsReasonInfo sipErrorInfo = ImsRadioUtils.sipErrorFromHal(errorInfo);
            Log.i(this, ImsSenderRxr.msgIdToString(rr.mRequest) + " response - sip error code: " + errorInfo.errorCode + " sip error string: " + errorInfo.errorString);
            ImsSenderRxr.this.sendResponse(rr, errorCode, sipErrorInfo);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void resumeResponse(int token, int errorCode, SipErrorInfo errorInfo) {
            sendSipErrorInfo(token, errorCode, errorInfo);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void holdResponse(int token, int errorCode, SipErrorInfo errorInfo) {
            sendSipErrorInfo(token, errorCode, errorInfo);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void hangupResponse(int token, int errorCode) {
            Log.i(this, "Got hangup response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void answerResponse(int token, int errorCode) {
            Log.i(this, "Got answer response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void requestRegistrationChangeResponse(int token, int errorCode) {
            Log.i(this, "Got registration change response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getRegistrationResponse(int token, int errorCode, RegistrationInfo registration) {
            Log.i(this, "getRegistrationResponse()");
            getRegistrationResponse_1_6(token, errorCode, ImsRadioUtilsV16.migrateRegistrationInfo(registration));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioResponse
        public void getRegistrationResponse_1_6(int token, int errorCode, vendor.qti.hardware.radio.ims.V1_6.RegistrationInfo registration) {
            Log.i(this, "getRegistrationResponse_1_6()");
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            ImsRegistrationInfo regMessage = null;
            if (registration != null) {
                regMessage = ImsRadioUtils.registrationFromHal(registration);
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, regMessage);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_3.IImsRadioResponse
        public void suppServiceStatusResponse_1_3(int token, int errorCode, SuppServiceStatus suppServiceStatus) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            Log.i(this, "Response to " + ImsSenderRxr.msgIdToString(rr.mRequest) + " status:" + suppServiceStatus.status + " provisionStatus:" + suppServiceStatus.provisionStatus + " facilityType:" + suppServiceStatus.facilityType + " failureCause:" + suppServiceStatus.failureCause + " errorCode:" + suppServiceStatus.errorDetails.errorCode);
            SuppSvcResponse suppSvcResponse = ImsRadioUtils.suppSvcStatusResponseFromHal(suppServiceStatus);
            if (rr.mRequest == 23) {
                CallForwardStatusInfo cfStatusInfo = new CallForwardStatusInfo(suppSvcResponse.getErrorDetails(), null);
                ImsSenderRxr.this.sendResponse(rr, errorCode, cfStatusInfo);
            } else {
                ImsSenderRxr.this.sendResponse(rr, errorCode, suppSvcResponse);
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void suppServiceStatusResponse(int token, int errorCode, vendor.qti.hardware.radio.ims.V1_0.SuppServiceStatus suppServiceStatus) {
            SuppServiceStatus status = ImsRadioUtilsV13.migrateSuppServiceStatusFromV10(suppServiceStatus);
            suppServiceStatusResponse_1_3(token, errorCode, status);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void conferenceResponse(int token, int errorCode, SipErrorInfo errorInfo) {
            sendSipErrorInfo(token, errorCode, errorInfo);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getClipResponse(int token, int errorCode, ClipProvisionStatus clipProvisionStatus) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            SuppService clipProvStatus = new SuppService();
            int clipStatus = clipProvisionStatus.clipStatus;
            if (clipStatus != 3) {
                clipProvStatus.setStatus(ImsRadioUtils.clipStatusFromHal(clipProvisionStatus.clipStatus));
                Log.i(this, "getClipResponse from ImsRadio. Clipstatus " + clipProvisionStatus);
            }
            if (clipProvisionStatus.hasErrorDetails) {
                clipProvStatus.setErrorDetails(ImsRadioUtils.sipErrorFromHal(clipProvisionStatus.errorDetails));
                Log.i(this, "getClipResponse from ImsRadio. Errorcode " + clipProvisionStatus.errorDetails.errorCode + "String " + clipProvisionStatus.errorDetails.errorString);
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, clipProvStatus);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getClirResponse(int token, int errorCode, ClirInfo clirInfo, boolean hasClirInfo) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            int[] response = null;
            if (hasClirInfo) {
                response = new int[2];
                if (clirInfo.paramN != Integer.MAX_VALUE) {
                    response[0] = clirInfo.paramN;
                }
                if (clirInfo.paramM != Integer.MAX_VALUE) {
                    response[1] = clirInfo.paramM;
                }
                Log.i(this, "getClirResponse from ImsRadio. param_m - " + clirInfo.paramM + "param_n - " + clirInfo.paramN);
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, response);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void queryCallForwardStatusResponse(int token, int errorCode, ArrayList<CallForwardInfo> callForwardInfoList, SipErrorInfo errorDetails) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            Object ret = ImsRadioUtils.buildCFStatusResponseFromHal(callForwardInfoList);
            ImsSenderRxr.this.sendResponse(rr, errorCode, ret);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getCallWaitingResponse(int token, int errorCode, int inServiceStatus, int serviceClass, SipErrorInfo errorDetails) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            int[] response = null;
            if (inServiceStatus != 2) {
                int outServiceStatus = ImsRadioUtils.serviceClassStatusFromHal(inServiceStatus);
                if (outServiceStatus == 0) {
                    response = new int[]{0};
                } else {
                    response = new int[]{1, serviceClass};
                }
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, response);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void setClirResponse(int token, int errorCode) {
            Log.i(this, "Got setClirResponse from ImsRadio. error " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_3.IImsRadioResponse
        public void getColrResponse_1_3(int token, int errorCode, ColrInfo colrInfo) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            SuppService colrValue = new SuppService();
            colrValue.setServiceClassStatus(ImsRadioUtils.serviceClassStatusFromHal(colrInfo.status));
            colrValue.setProvisionStatus(ImsRadioUtils.serviceClassProvisionStatusFromHal(colrInfo.provisionStatus));
            colrValue.setStatus(ImsRadioUtils.serviceClassStatusFromHal(colrInfo.status));
            int presentation = ImsRadioUtils.ipPresentationFromHal(colrInfo.presentation);
            if (presentation != 3) {
                colrValue.setPresentation(presentation);
                Log.i(this, "getColrResponse from ImsRadio. presentation " + presentation);
            }
            if (colrInfo.hasErrorDetails) {
                colrValue.setErrorDetails(ImsRadioUtils.sipErrorFromHal(colrInfo.errorDetails));
                Log.i(this, "getColrResponse from ImsRadio. errorcode " + colrInfo.errorDetails.errorCode + "string " + colrInfo.errorDetails.errorString);
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, colrValue);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getColrResponse(int token, int errorCode, vendor.qti.hardware.radio.ims.V1_0.ColrInfo colrInfo) {
            ColrInfo ci = ImsRadioUtilsV13.migrateColrInfoFromV10(colrInfo);
            getColrResponse_1_3(token, errorCode, ci);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void exitEmergencyCallbackModeResponse(int token, int errorCode) {
            Log.i(this, "Got exitEmergencyCallbackModeResponse from ImsRadio error " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void sendDtmfResponse(int token, int errorCode) {
            Log.i(this, "Got sendDtmfResponse from ImsRadio error " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void startDtmfResponse(int token, int errorCode) {
            Log.i(this, "Got startDtmfResponse from ImsRadio error " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void stopDtmfResponse(int token, int errorCode) {
            Log.i(this, "Got stopDtmfResponse from ImsRadio error " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void setUiTTYModeResponse(int token, int errorCode) {
            Log.i(this, "Got setUiTTYModeResponse from ImsRadio error " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void modifyCallInitiateResponse(int token, int errorCode) {
            Log.i(this, "Got modify call initiate response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void cancelModifyCallResponse(int token, int errorCode) {
            Log.i(this, "Got cancel modify call response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void modifyCallConfirmResponse(int token, int errorCode) {
            Log.i(this, "Got modify call confirm response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void explicitCallTransferResponse(int token, int errorCode) {
            Log.i(this, "Got explicit call transfer response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void setSuppServiceNotificationResponse(int token, int errorCode, int serviceStatusClass) {
            Log.i(this, "Got set supp service notification response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getRtpStatisticsResponse(int token, int errorCode, long packetCount) {
            Log.i(this, "Got getRtpStatisticsResponse from ImsRadio packetCount = " + packetCount + " error " + errorCode);
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, Long.valueOf(packetCount));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getRtpErrorStatisticsResponse(int token, int errorCode, long packetErrorCount) {
            Log.i(this, "Got getRtpErrorStatisticsResponse from ImsRadio packetErrorCount = " + packetErrorCount + " error " + errorCode);
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, Long.valueOf(packetErrorCount));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void addParticipantResponse(int token, int errorCode) {
            Log.i(this, "Add Participant response received. errorCode: " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void deflectCallResponse(int token, int errorCode) {
            Log.i(this, "Got deflect call response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void sendGeolocationInfoResponse(int token, int errorCode) {
            Log.i(this, "Received GeoLocationInfo response from ImsRadio.");
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void getImsSubConfigResponse(int token, int errorCode, ImsSubConfigInfo subConfigInfo) {
            Log.i(this, "Received Subconfig response from ImsRadio.");
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            Object ret = ImsRadioUtils.imsSubconfigFromHal(subConfigInfo);
            ImsSenderRxr.this.sendResponse(rr, errorCode, ret);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioResponse
        public void sendUssdResponse(int token, int errorCode, SipErrorInfo errorDetails) {
            sendSipErrorInfo(token, errorCode, errorDetails);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_7.IImsRadioResponse
        public void sendSipDtmfResponse(int token, int errorCode) {
            Log.i(this, "Received Send SIP DTMF response from ImsRadio.");
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            ImsSenderRxr.this.sendResponse(rr, errorCode, null);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioResponse
        public void cancelPendingUssdResponse(int token, int errorCode, SipErrorInfo errorDetails) {
            sendSipErrorInfo(token, errorCode, errorDetails);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioResponse
        public void sendRttMessageResponse(int token, int errorCode) {
            Log.i(this, "Send Rtt Message response received. errorCode: " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_4.IImsRadioResponse
        public void registerMultiIdentityLinesResponse(int token, int errorCode) {
            Log.i(this, "registerMultiIdentityLines Response received. errorCode : " + errorCode);
            ImsSenderRxr.this.removeFromQueueAndSendResponse(token, errorCode);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_4.IImsRadioResponse
        public void queryVirtualLineInfoResponse(int token, String msisdn, ArrayList<String> virtualLineInfo) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            VirtualLineInfo virtualInfo = new VirtualLineInfo(msisdn, virtualLineInfo);
            Log.i(this, "queryVirtualLineInfoResponse :: " + virtualInfo);
            ImsSenderRxr.this.sendResponse(rr, 0, virtualInfo);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioResponse
        public void setCallForwardStatusResponse(int token, int errorCode, vendor.qti.hardware.radio.ims.V1_5.CallForwardStatusInfo callForwardStatusInfo) {
            IFRequest rr = ImsSenderRxr.this.findAndRemoveRequestFromList(token);
            if (rr == null) {
                return;
            }
            CallForwardStatusInfo cfStatusInfo = ImsRadioUtilsV15.callForwardStatusInfoFromHal(callForwardStatusInfo);
            Log.i(this, "setCallForwardStatusResponse :: " + cfStatusInfo);
            ImsSenderRxr.this.sendResponse(rr, errorCode, cfStatusInfo);
        }
    }

    class ImsRadioIndication extends vendor.qti.hardware.radio.ims.V1_7.IImsRadioIndication.Stub {
        ImsRadioIndication() {
        }

        @Override // vendor.qti.hardware.radio.ims.V1_7.IImsRadioIndication
        public void onCallStateChanged_1_7(ArrayList<CallInfo> callList) {
            Log.i(this, "onCallStateChanged_1_7()");
            if (callList == null) {
                Log.e(this, "Call list is null.");
                return;
            }
            ArrayList<DriverCallIms> response = new ArrayList<>();
            int numOfCalls = callList.size();
            ImsSenderRxr.this.mIsUnsolCallListPresent = true;
            for (int i = 0; i < numOfCalls; i++) {
                DriverCallIms dc = ImsRadioUtils.buildDriverCallImsFromHal(callList.get(i));
                if (dc.callFailCause.getExtraMessage() != null && dc.callFailCause.getExtraMessage().equalsIgnoreCase("Forbidden. Not Authorized for Service")) {
                    int subId = QtiImsExtUtils.getSubscriptionIdFromPhoneId(ImsSenderRxr.this.mContext, ImsSenderRxr.this.mPhoneId.intValue());
                    ImsSenderRxr.this.log("Throwing ACTION_FORBIDDEN_NO_SERVICE_AUTHORIZATION intent for subId " + subId);
                    Intent intent = new Intent("com.android.internal.intent.action.ACTION_FORBIDDEN_NO_SERVICE_AUTHORIZATION");
                    intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId);
                    intent.addFlags(536870912);
                    ImsSenderRxr.this.mContext.sendBroadcast(intent);
                }
                if (dc.isVoicePrivacy) {
                    ImsSenderRxr.this.mVoicePrivacyOnRegistrants.notifyRegistrants();
                    ImsSenderRxr.this.log("InCall VoicePrivacy is enabled");
                } else {
                    ImsSenderRxr.this.mVoicePrivacyOffRegistrants.notifyRegistrants();
                    ImsSenderRxr.this.log("InCall VoicePrivacy is disabled");
                }
                response.add(dc);
            }
            Collections.sort(response);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_RESPONSE_CALL_STATE_CHANGED, response);
            ImsSenderRxr.this.mCallStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onCallStateChanged_1_6(ArrayList<vendor.qti.hardware.radio.ims.V1_6.CallInfo> callList) {
            Log.i(this, "onCallStateChanged_1_6()");
            onCallStateChanged_1_7(ImsRadioUtilsV17.migrateCallListFrom(callList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication
        public void onCallStateChanged_1_5(ArrayList<vendor.qti.hardware.radio.ims.V1_5.CallInfo> callList) {
            Log.i(this, "onCallStateChanged_1_5()");
            onCallStateChanged_1_6(ImsRadioUtilsV16.migrateCallListFrom(callList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication
        public void onCallStateChanged_1_4(ArrayList<vendor.qti.hardware.radio.ims.V1_4.CallInfo> callList) {
            Log.i(this, "onCallStateChanged_1_4()");
            onCallStateChanged_1_5(ImsRadioUtilsV15.migrateCallListFrom(callList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication
        public void onCallStateChanged_1_3(ArrayList<vendor.qti.hardware.radio.ims.V1_3.CallInfo> callList) {
            Log.i(this, "onCallStateChanged_1_3()");
            onCallStateChanged_1_4(ImsRadioUtilsV14.migrateCallListFromV13(callList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication
        public void onCallStateChanged_1_2(ArrayList<vendor.qti.hardware.radio.ims.V1_2.CallInfo> callList) {
            Log.i(this, "onCallStateChanged_1_2()");
            onCallStateChanged_1_3(ImsRadioUtilsV13.migrateCallListFromV12(callList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication
        public void onCallStateChanged_1_1(ArrayList<vendor.qti.hardware.radio.ims.V1_1.CallInfo> callList) {
            Log.i(this, "onCallStateChanged_1_1()");
            onCallStateChanged_1_2(ImsRadioUtilsV12.migrateCallListFromV11(callList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onCallStateChanged(ArrayList<vendor.qti.hardware.radio.ims.V1_0.CallInfo> callList) {
            Log.i(this, "onCallStateChanged()");
            onCallStateChanged_1_1(ImsRadioUtils.migrateCallListToV11(callList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication
        public void onImsSmsStatusReport(ImsSmsSendStatusReport smsStatusReport) {
            ImsSenderRxr.this.unsljLog(MessageId.UNSOL_IMS_SMS_STATUS_REPORT);
            if (smsStatusReport.pdu != null) {
                StatusReport smsReport = ImsRadioUtils.statusReportfromHidl(smsStatusReport);
                if (ImsSenderRxr.this.mSendSmsStatusReportRegistrant != null) {
                    ImsSenderRxr.this.mSendSmsStatusReportRegistrant.notifyRegistrant(new AsyncResult((Object) null, smsReport, (Throwable) null));
                }
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication
        public void onIncomingImsSms(IncomingImsSms imsSms) {
            ImsSenderRxr.this.unsljLog(MessageId.UNSOL_INCOMING_IMS_SMS);
            if (imsSms.pdu != null && imsSms.format != null) {
                IncomingSms newSms = ImsRadioUtils.incomingSmsfromHidl(imsSms);
                if (ImsSenderRxr.this.mIncomingSmsRegistrant != null) {
                    ImsSenderRxr.this.mIncomingSmsRegistrant.notifyRegistrant(new AsyncResult((Object) null, newSms, (Throwable) null));
                }
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRing() {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_CALL_RING, null);
            if (ImsSenderRxr.this.mRingRegistrant != null) {
                ImsSenderRxr.this.mRingRegistrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRingbackTone(int tone) {
            ImsSenderRxr.this.log(" responseCallRingBack ");
            int[] response = {ImsRadioUtils.ringbackToneFromHal(tone)};
            ImsSenderRxr.this.log("responseCallRingBack " + response[0]);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_RINGBACK_TONE, response);
            boolean playtone = response[0] == 1;
            if (ImsSenderRxr.this.mRingbackToneRegistrants != null) {
                ImsSenderRxr.this.mRingbackToneRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.valueOf(playtone), (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRegistrationChanged(RegistrationInfo registration) {
            Log.i(this, "onRegistrationChanged()");
            onRegistrationChanged_1_6(ImsRadioUtilsV16.migrateRegistrationInfo(registration));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onRegistrationChanged_1_6(vendor.qti.hardware.radio.ims.V1_6.RegistrationInfo registration) {
            Log.i(this, "onRegistrationChanged_1_6()");
            ImsSenderRxr.this.unsljLog(MessageId.UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED);
            ImsRegistrationInfo regMessage = ImsRadioUtils.registrationFromHal(registration);
            ImsSenderRxr.this.mImsNetworkStateChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, regMessage, (Throwable) null));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onHandover(HandoverInfo inHandover) {
            Log.i(this, "onHandover()");
            onHandover_1_6(ImsRadioUtilsV16.migrateHandoverInfo(inHandover));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onHandover_1_6(vendor.qti.hardware.radio.ims.V1_6.HandoverInfo inHandover) {
            Log.i(this, "onHandover_1_6()");
            HoInfo outHandover = ImsRadioUtils.handoverFromHal(inHandover);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_RESPONSE_HANDOVER, outHandover);
            if (outHandover != null) {
                ImsSenderRxr.this.mHandoverStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, outHandover, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onServiceStatusChanged(ArrayList<ServiceStatusInfo> srvStatusList) {
            Log.i(this, "onServiceStatusChanged()");
            onServiceStatusChanged_1_6(ImsRadioUtilsV16.migrateServiceStatusInfo(srvStatusList));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onServiceStatusChanged_1_6(ArrayList<vendor.qti.hardware.radio.ims.V1_6.ServiceStatusInfo> srvStatusList) {
            Log.i(this, "onServiceStatusChanged_1_6()");
            Object ret = ImsRadioUtils.handleSrvStatus(srvStatusList);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_SRV_STATUS_UPDATE, ret);
            if (ret != null) {
                ImsSenderRxr.this.mSrvStatusRegistrations.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRadioStateChanged(int radioState) {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_RADIO_STATE_CHANGED, Integer.valueOf(radioState));
            switch (radioState) {
                case 0:
                    ImsSenderRxr.this.setRadioState(ImsPhoneCommandsInterface.RadioState.RADIO_OFF);
                    break;
                case 1:
                    ImsSenderRxr.this.setRadioState(ImsPhoneCommandsInterface.RadioState.RADIO_UNAVAILABLE);
                    break;
                case 2:
                    ImsSenderRxr.this.setRadioState(ImsPhoneCommandsInterface.RadioState.RADIO_ON);
                    break;
                default:
                    Log.e(this, "onRadioStateChanged: Invalid Radio State Change");
                    break;
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onEnterEmergencyCallBackMode() {
            ImsSenderRxr.this.unsljLog(MessageId.UNSOL_ENTER_EMERGENCY_CALLBACK_MODE);
            if (ImsSenderRxr.this.mEmergencyCallbackModeRegistrant != null) {
                ImsSenderRxr.this.mEmergencyCallbackModeRegistrant.notifyRegistrant();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onExitEmergencyCallBackMode() {
            ImsSenderRxr.this.unsljLog(MessageId.UNSOL_EXIT_EMERGENCY_CALLBACK_MODE);
            if (ImsSenderRxr.this.mExitEmergencyCallbackModeRegistrants != null) {
                ImsSenderRxr.this.mExitEmergencyCallbackModeRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onTtyNotification(TtyInfo ttyInfo) {
            int[] mode = null;
            if (ttyInfo != null) {
                mode = new int[]{ImsRadioUtils.ttyModeFromHal(ttyInfo.mode)};
            }
            if (ttyInfo.userData != null && !ttyInfo.userData.isEmpty()) {
                ImsRadioUtils.getByteArray(ttyInfo.userData);
            }
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_TTY_NOTIFICATION, mode);
            if (mode != null && ImsSenderRxr.this.mTtyStatusRegistrants != null) {
                ImsSenderRxr.this.mTtyStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, mode, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRefreshConferenceInfo(ConferenceInfo conferenceInfo) {
            if (conferenceInfo != null) {
                ConfInfo info = new ConfInfo();
                if (conferenceInfo.confInfoUri != null && !conferenceInfo.confInfoUri.isEmpty()) {
                    info.setConfInfoUri(conferenceInfo.confInfoUri);
                    Log.v(this, "onRefreshConferenceInfo: confUri = " + conferenceInfo.confInfoUri);
                }
                if (conferenceInfo.conferenceCallState != 3) {
                    info.setConfCallState(ImsRadioUtils.conferenceCallStateFromHal(conferenceInfo.conferenceCallState));
                }
                Log.v(this, "onRefreshConferenceInfo: conferenceCallState = " + conferenceInfo.conferenceCallState);
                ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_REFRESH_CONF_INFO, info);
                ImsSenderRxr.this.mRefreshConfInfoRegistrations.notifyRegistrants(new AsyncResult((Object) null, info, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRefreshViceInfo(ViceInfo viceInfo) {
            if (viceInfo != null && viceInfo.viceInfoUri != null) {
                ViceUriInfo info = new ViceUriInfo(viceInfo.viceInfoUri);
                Log.v(this, "onRefreshViceInfo: viceUri = " + viceInfo.viceInfoUri);
                ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_REFRESH_VICE_INFO, info);
                ImsSenderRxr.this.mRefreshViceInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, info, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onModifyCall(CallModifyInfo callModifyInfo) {
            if (callModifyInfo == null) {
                ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_MODIFY_CALL, null);
                Log.e(this, "onModifyCall callModifyInfo is null");
            } else {
                CallModify ret = ImsRadioUtils.callModifyFromHal(callModifyInfo);
                ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_MODIFY_CALL, ret);
                ImsSenderRxr.this.mModifyCallRegistrants.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onSuppServiceNotification(SuppServiceNotification suppServiceNotification) {
            Object ret = ImsRadioUtils.suppServiceNotificationFromHal(suppServiceNotification);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_SUPP_SVC_NOTIFICATION, ret);
            if (ImsSenderRxr.this.mSsnRegistrant != null) {
                ImsSenderRxr.this.mSsnRegistrant.notifyRegistrant(new AsyncResult((Object) null, ret, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onMessageWaiting(MessageWaitingIndication messageWaitingIndication) {
            Mwi ret = ImsRadioUtils.messageWaitingIndicationFromHal(messageWaitingIndication);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_MWI, ret);
            if (ret != null) {
                ImsSenderRxr.this.mMwiRegistrants.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
            } else {
                Log.e(this, "onMessageWaiting: Data is null");
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onGeolocationInfoRequested(double lat, double lon) {
            GeoLocationInfo ret = ImsRadioUtils.geolocationIndicationFromHal(lat, lon);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_REQUEST_GEOLOCATION, ret);
            if (ret != null) {
                ImsSenderRxr.this.mGeolocationRegistrants.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
            } else {
                Log.e(this, "onGeolocationInfoRequested: Null location data!");
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onIncomingCallComposerCallAutoRejected(AutoCallRejectionInfo rejectInfo, vendor.qti.hardware.radio.ims.V1_6.CallComposerInfo callComposerInfo) {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_AUTO_CALL_COMPOSER_CALL_REJECTION_IND, callComposerInfo);
            if (callComposerInfo == null) {
                Log.e(this, "onIncomingCallComposerCallAutoRejected: callComposerInfo is null");
            } else if (rejectInfo == null) {
                Log.e(this, "onIncomingCallComposerCallAutoRejected: rejectInfo is null ");
                return;
            }
            DriverCallIms dc = ImsRadioUtilsV16.toDriverCallIms(rejectInfo, callComposerInfo);
            Log.v(this, "onIncomingCallComposerCallAutoRejected :: Call auto rejected from : " + rejectInfo.number);
            ImsSenderRxr.this.mAutoRejectRegistrants.notifyRegistrants(new AsyncResult((Object) null, dc, (Throwable) null));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onIncomingCallAutoRejected_1_6(AutoCallRejectionInfo rejectInfo) {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_AUTO_CALL_REJECTION_IND, rejectInfo);
            if (rejectInfo == null) {
                Log.e(this, "onIncomingCallAutoRejected: rejectInfo is null. Returning");
                return;
            }
            DriverCallIms dc = ImsRadioUtilsV16.toDriverCallIms(rejectInfo, null);
            Log.v(this, "onIncomingCallAutoRejected :: Call auto rejected from : " + rejectInfo.number);
            ImsSenderRxr.this.mAutoRejectRegistrants.notifyRegistrants(new AsyncResult((Object) null, dc, (Throwable) null));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication
        public void onIncomingCallAutoRejected_1_5(vendor.qti.hardware.radio.ims.V1_5.AutoCallRejectionInfo rejectInfo) {
            onIncomingCallAutoRejected_1_6(ImsRadioUtilsV16.migrateAutoCallRejectionInfoFrom(rejectInfo));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication
        public void onIncomingCallAutoRejected(vendor.qti.hardware.radio.ims.V1_3.AutoCallRejectionInfo rejectInfo) {
            onIncomingCallAutoRejected_1_5(ImsRadioUtilsV15.migrateAutoCallRejectionInfoFrom(rejectInfo));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onImsSubConfigChanged(ImsSubConfigInfo config) {
            ImsSubConfigDetails ret = ImsRadioUtils.imsSubconfigFromHal(config);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_IMS_SUB_CONFIG_CHANGED, ret);
            if (ret != null) {
                ImsSenderRxr.this.mImsSubConfigChangeRegistrants.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onParticipantStatusInfo(ParticipantStatusInfo participantStatusInfo) {
            ParticipantStatusDetails ret = ImsRadioUtils.participantStatusFromHal(participantStatusInfo);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_PARTICIPANT_STATUS_INFO, ret);
            if (ret != null) {
                ImsSenderRxr.this.mParticipantStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRegistrationBlockStatus(boolean hasBlockStatusOnWwan, BlockStatus blockStatusOnWwan, boolean hasBlockStatusOnWlan, BlockStatus blockStatusOnWlan) {
            RegistrationBlockStatusInfo ret = ImsRadioUtils.registrationBlockStatusFromHal(hasBlockStatusOnWwan, blockStatusOnWwan, hasBlockStatusOnWlan, blockStatusOnWlan);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_RESPONSE_REGISTRATION_BLOCK_STATUS, ret);
            if (ret != null) {
                ImsSenderRxr.this.mRegistrationBlockStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
            } else {
                Log.e(this, "onRegistrationBlockStatus: Data is null.");
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRttMessageReceived(String msg) {
            if (msg != null) {
                Log.v(this, "onRttMessageReceived: msg = " + msg);
                ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_RESPONSE_RTT_MSG_RECEIVED, msg);
                ImsSenderRxr.this.mRttMessageRegistrants.notifyRegistrants(new AsyncResult((Object) null, msg, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication
        public void onVoiceInfoChanged(int voiceInfo) {
            Log.v(this, "onVoiceInfoChanged: VoiceInfo = " + voiceInfo);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_VOICE_INFO, Integer.valueOf(voiceInfo));
            ImsSenderRxr.this.mVoiceInfoStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(voiceInfo), (Throwable) null));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onVoWiFiCallQuality(int voWiFiCallQuality) {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_VOWIFI_CALL_QUALITY, Integer.valueOf(voWiFiCallQuality));
            Object ret = ImsRadioUtils.voWiFiCallQualityFromHal(voWiFiCallQuality);
            if (ImsSenderRxr.this.mVoWiFiCallQualityRegistrants != null) {
                ImsSenderRxr.this.mVoWiFiCallQualityRegistrants.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onSupplementaryServiceIndication(StkCcUnsolSsResult ss) {
            ImsSsData.Builder ssDataBuilder = new ImsSsData.Builder(ImsRadioUtils.serviceTypeFromRILServiceType(ss.serviceType), ImsRadioUtils.requestTypeFromRILRequestType(ss.requestType), ImsRadioUtils.teleserviceTypeFromRILTeleserviceType(ss.teleserviceType), ss.serviceClass, ss.result);
            ImsSsData ssData = ssDataBuilder.build();
            if (ssData.isTypeCf() && ssData.isTypeInterrogation()) {
                List<ImsCallForwardInfo> cfInfo = new ArrayList<>();
                if (ss.cfData == null) {
                    Log.d(this, "cfData is null, which is unexpected for: " + ssData.getServiceType());
                } else {
                    CfData cfData = ss.cfData.get(0);
                    Object ret = ImsRadioUtils.buildCFStatusResponseFromHal(cfData.cfInfo);
                    if (ret instanceof ImsCallForwardTimerInfo[]) {
                        Log.i(this, "onSupplementaryServiceIndication : Not handling CFUT SS");
                        ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_ON_SS, null);
                        return;
                    }
                    int i = 0;
                    for (int num = cfData.cfInfo.size(); i < num; num = num) {
                        CallForwardInfo rCfInfo = cfData.cfInfo.get(i);
                        cfInfo.add(new ImsCallForwardInfo(ImsRadioUtils.getUTInterfaceCFReasonFromCommandsInterfaceCFReason(rCfInfo.reason), rCfInfo.status, rCfInfo.toa, rCfInfo.serviceClass, rCfInfo.number, rCfInfo.timeSeconds));
                        Log.i(this, "[SS Data] CF Info " + i + Log.TAG_DELIMETER + cfInfo.get(i));
                        i++;
                        cfData = cfData;
                        ret = ret;
                    }
                }
                ssDataBuilder.setCallForwardingInfo(cfInfo);
            } else if (ssData.isTypeIcb() && ssData.isTypeInterrogation()) {
                List<ImsSsInfo> imsSsInfo = new ArrayList<>();
                if (ss.cbNumInfo == null) {
                    Log.d(this, "cbNumInfo is null, which is unexpected for: " + ssData.getServiceType());
                } else {
                    int num2 = ss.cbNumInfo.size();
                    for (int i2 = 0; i2 < num2; i2++) {
                        ImsSsInfo.Builder imsSsInfoBuilder = new ImsSsInfo.Builder(ss.cbNumInfo.get(i2).status);
                        imsSsInfoBuilder.setIncomingCommunicationBarringNumber(ss.cbNumInfo.get(i2).number);
                        imsSsInfo.add(imsSsInfoBuilder.build());
                        Log.i(this, "[SS Data] ICB Info " + i2 + Log.TAG_DELIMETER + imsSsInfo.get(i2));
                    }
                }
                ssDataBuilder.setSuppServiceInfo(imsSsInfo);
            } else {
                List<ImsSsInfo> imsSsInfo2 = new ArrayList<>();
                if (ss.ssInfoData == null) {
                    Log.d(this, "imsSsInfo is null, which is unexpected for: " + ssData.getServiceType());
                } else {
                    SsInfoData ssInfoData = ss.ssInfoData.get(0);
                    int num3 = ssInfoData.ssInfo.size();
                    if (num3 > 0) {
                        ImsSsInfo.Builder imsSsInfoBuilder2 = new ImsSsInfo.Builder(ssInfoData.ssInfo.get(0).intValue());
                        if (ssData.isTypeClir() && ssData.isTypeInterrogation()) {
                            imsSsInfoBuilder2.setClirOutgoingState(ssInfoData.ssInfo.get(0).intValue());
                            if (num3 > 1) {
                                imsSsInfoBuilder2.setClirInterrogationStatus(ssInfoData.ssInfo.get(1).intValue());
                            }
                        } else if (num3 > 1) {
                            imsSsInfoBuilder2.setProvisionStatus(ssInfoData.ssInfo.get(1).intValue());
                        }
                        imsSsInfo2.add(imsSsInfoBuilder2.build());
                    }
                }
                ssDataBuilder.setSuppServiceInfo(imsSsInfo2);
            }
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_ON_SS, ssData);
            if (ImsSenderRxr.this.mSsIndicationRegistrant != null) {
                ImsSenderRxr.this.mSsIndicationRegistrant.notifyRegistrant(new AsyncResult((Object) null, ssData, (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication
        public void onVopsChanged(boolean isVopsEnabled) {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_VOPS_CHANGED, Boolean.valueOf(isVopsEnabled));
            if (ImsSenderRxr.this.mVopsRegistrants != null) {
                ImsSenderRxr.this.mVopsRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.valueOf(isVopsEnabled), (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication
        public void onMultiIdentityRegistrationStatusChange(ArrayList<MultiIdentityLineInfoHal> lines) {
            ArrayList<MultiIdentityLineInfo> linesInfo = new ArrayList<>();
            for (MultiIdentityLineInfoHal line : lines) {
                MultiIdentityLineInfo lineInfo = ImsRadioUtilsV14.fromMultiIdentityLineInfoHal(line);
                linesInfo.add(lineInfo);
            }
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_MULTI_IDENTITY_REGISTRATION_STATUS_CHANGE, linesInfo);
            ImsSenderRxr.this.mMultiIdentityStatusChangeRegistrants.notifyRegistrants(new AsyncResult((Object) null, linesInfo, (Throwable) null));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication
        public void onMultiIdentityInfoPending() {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_MULTI_IDENTITY_INFO_PENDING, null);
            ImsSenderRxr.this.mMultiIdentityInfoPendingRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication
        public void onModemSupportsWfcRoamingModeConfiguration(boolean wfcRoamingConfigurationSupport) {
            Log.i(this, "wfcRoamingConfigurationSupport = " + wfcRoamingConfigurationSupport);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_MODEM_SUPPORTS_WFC_ROAMING_MODE, Boolean.valueOf(wfcRoamingConfigurationSupport));
            ImsSenderRxr.this.mWfcRoamingConfigurationSupport = wfcRoamingConfigurationSupport;
            if (ImsSenderRxr.this.mWfcRoamingModeConfigRegistrants != null) {
                ImsSenderRxr.this.mWfcRoamingModeConfigRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.valueOf(wfcRoamingConfigurationSupport), (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication
        public void onUssdMessageFailed(int type, SipErrorInfo errorDetails) {
            UssdInfo ussdInfo = ImsRadioUtils.ussdInfoFromHal(type, "", errorDetails);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_USSD_FAILED, ussdInfo);
            ImsRilException ex = new ImsRilException(ussdInfo.getErrorCode(), ussdInfo.getErrorMessage());
            ImsSenderRxr.this.notifyUssdInfo(ussdInfo, ex);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onUssdReceived(int type, String msg, SipErrorInfo errorDetails) {
            UssdInfo ussdInfo = ImsRadioUtils.ussdInfoFromHal(type, msg, errorDetails);
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_USSD_RECEIVED, ussdInfo);
            ImsSenderRxr.this.notifyUssdInfo(ussdInfo, null);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onCallComposerInfoAvailable(vendor.qti.hardware.radio.ims.V1_6.CallComposerInfo callComposerInfo) {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_CALL_COMPOSER_INFO_AVAILABLE_IND, callComposerInfo);
            CallComposerInfo info = ImsRadioUtilsV16.buildCallComposerInfoFromHal(callComposerInfo);
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = info;
            args.argi1 = callComposerInfo.callId;
            ImsSenderRxr.this.mCallComposerInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, args, (Throwable) null));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onRetrievingGeoLocationDataStatus(int geoLocationDataStatus) {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_RETRIEVE_GEO_LOCATION_DATA_STATUS, Integer.valueOf(geoLocationDataStatus));
            if (ImsSenderRxr.this.mGeoLocationDataStatusRegistrants != null) {
                ImsSenderRxr.this.mGeoLocationDataStatusRegistrants.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(ImsRadioUtilsV16.geoLocationDataStatusFromHal(geoLocationDataStatus)), (Throwable) null));
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_7.IImsRadioIndication
        public void onSipDtmfReceived(String configCode) {
            ImsSenderRxr.this.unsljLogRet(MessageId.UNSOL_SIP_DTMF_RECEIVED, configCode);
            ImsSenderRxr.this.notifySipDtmfInfo(configCode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyUssdInfo(UssdInfo info, ImsRilException ex) {
        RegistrantList registrantList = this.mUssdInfoRegistrants;
        if (registrantList != null) {
            registrantList.notifyRegistrants(new AsyncResult((Object) null, info, ex));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySipDtmfInfo(String configCode) {
        RegistrantList registrantList = this.mSipDtmfInfoRegistrants;
        if (registrantList != null) {
            registrantList.notifyRegistrants(new AsyncResult((Object) null, configCode, (Throwable) null));
        }
    }

    private void removeFromQueueAndSendResponse(int token) {
        removeFromQueueAndSendResponse(token, 2);
    }

    private void removeFromQueueAndSendResponse(int token, Object ret) {
        removeFromQueueAndSendResponse(token, 2, ret);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromQueueAndSendResponse(int token, int errorCode) {
        removeFromQueueAndSendResponse(token, errorCode, null);
    }

    private void removeFromQueueAndSendResponse(int token, int errorCode, Object ret) {
        IFRequest rr = findAndRemoveRequestFromList(token);
        if (rr == null) {
            Log.w(this, "Unexpected solicited response or Invalid token id! token: " + token + " error: " + errorCode);
        } else {
            sendResponse(rr, errorCode, ret);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResponse(IFRequest rr, int error, Object ret) {
        if (error != 0) {
            rr.onError(error, ret);
            rr.release();
            releaseWakeLockIfDone();
        } else {
            log(rr.serialString() + "< " + msgIdToString(rr.mRequest) + " " + retToString(rr.mRequest, ret));
            if (rr.mResult != null) {
                AsyncResult.forMessage(rr.mResult, ret, (Throwable) null);
                rr.mResult.sendToTarget();
            }
            rr.release();
            releaseWakeLockIfDone();
        }
    }

    public ImsSenderRxr(Context context, int phoneId) {
        super(context);
        this.mIsUnsolCallListPresent = false;
        this.mWfcRoamingConfigurationSupport = false;
        this.mRequestsList = new ArrayList<>();
        this.mTestingEmergencyCall = new AtomicBoolean(false);
        this.mServiceNotification = new ImsRadioServiceNotification();
        this.mListeners = new CopyOnWriteArrayList();
        this.mHalSync = new Object();
        this.mPhoneId = Integer.valueOf(phoneId);
        initNotifyRegistrants();
        PowerManager pm = (PowerManager) context.getSystemService("power");
        PowerManager.WakeLock wakeLockNewWakeLock = pm.newWakeLock(1, LOG_TAG);
        this.mWakeLock = wakeLockNewWakeLock;
        wakeLockNewWakeLock.setReferenceCounted(false);
        this.mWakeLockTimeout = ((Integer) TelephonyProperties.wake_lock_timeout().orElse(Integer.valueOf(DEFAULT_WAKE_LOCK_TIMEOUT))).intValue();
        this.mImsRadioHandler = new ImsRadioHandler(this);
        this.mRequestMessagesWaiting = 0;
        this.mDeathRecipient = new ImsRadioDeathRecipient();
        registerForImsRadioServiceNotification();
    }

    private void initNotifyRegistrants() {
        this.mHandoverStatusRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mRefreshConfInfoRegistrations = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mSrvStatusRegistrations = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mTtyStatusRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mRadioStateRegistrations = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mGeolocationRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mVoWiFiCallQualityRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mSsaccRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mVopsRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mParticipantStatusRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mImsSubConfigChangeRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mRegistrationBlockStatusRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mModifyCallRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mMwiRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mRttMessageRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mRttModifyRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mAutoRejectRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mVoiceInfoStatusRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mMultiIdentityStatusChangeRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mMultiIdentityInfoPendingRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mWfcRoamingModeConfigRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mUssdInfoRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mGeoLocationDataStatusRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
        this.mSipDtmfInfoRegistrants = new WakeLockRegistrantList(this.mNotifyWakeLock);
    }

    private void acquireWakeLock() {
        synchronized (this.mWakeLock) {
            this.mWakeLock.acquire();
            this.mImsRadioHandler.removeMessages(1);
            Message msg = this.mImsRadioHandler.obtainMessage(1);
            this.mImsRadioHandler.sendMessageDelayed(msg, this.mWakeLockTimeout);
        }
    }

    private void releaseWakeLockIfDone() {
        synchronized (this.mWakeLock) {
            if (this.mWakeLock.isHeld() && this.mRequestMessagesWaiting == 0) {
                this.mWakeLock.release();
            }
        }
    }

    private void queueRequest(IFRequest rr) {
        acquireWakeLock();
        synchronized (this.mRequestsList) {
            this.mRequestsList.add(rr);
            this.mRequestMessagesWaiting++;
        }
    }

    private void clearRequestsList(int error, boolean loggable) {
        synchronized (this.mRequestsList) {
            int count = this.mRequestsList.size();
            if (loggable) {
                Log.i(this, "clearRequestsList: mRequestList=" + count);
            }
            for (int i = 0; i < count; i++) {
                IFRequest rr = this.mRequestsList.get(i);
                if (loggable) {
                    Log.i(this, i + ": [" + rr.mSerial + "] " + msgIdToString(rr.mRequest));
                }
                rr.onError(error, null);
                rr.release();
            }
            this.mRequestsList.clear();
            this.mRequestMessagesWaiting = 0;
            releaseWakeLockIfDone();
        }
        if (this.mIsUnsolCallListPresent) {
            this.mIsUnsolCallListPresent = false;
            this.mCallStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, new RuntimeException(errorIdToString(error))));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IFRequest findAndRemoveRequestFromList(int serial) {
        synchronized (this.mRequestsList) {
            int s = this.mRequestsList.size();
            for (int i = 0; i < s; i++) {
                IFRequest rr = this.mRequestsList.get(i);
                if (rr.mSerial == serial) {
                    this.mRequestsList.remove(i);
                    int i2 = this.mRequestMessagesWaiting;
                    if (i2 > 0) {
                        this.mRequestMessagesWaiting = i2 - 1;
                    }
                    return rr;
                }
            }
            return null;
        }
    }

    private String retToString(int req, Object ret) {
        if (ret == null) {
            return "";
        }
        if (ret instanceof int[]) {
            int[] intArray = (int[]) ret;
            int length = intArray.length;
            StringBuilder sb = new StringBuilder("{");
            if (length > 0) {
                int i = intArray[0];
                sb.append(i);
                for (int i2 = 0 + 1; i2 < length; i2++) {
                    sb.append(", ").append(intArray[i2]);
                }
            }
            sb.append("}");
            String s = sb.toString();
            return s;
        }
        if (!(ret instanceof String[])) {
            if (req == 201 || req == 6) {
                ArrayList<DriverCallIms> calls = (ArrayList) ret;
                StringBuilder sb2 = new StringBuilder(" ");
                for (DriverCallIms dc : calls) {
                    sb2.append("[").append(dc).append("] ");
                }
                String s2 = sb2.toString();
                return s2;
            }
            String s3 = ret.toString();
            return s3;
        }
        String[] strings = (String[]) ret;
        int length2 = strings.length;
        StringBuilder sb3 = new StringBuilder("{");
        if (length2 > 0) {
            sb3.append(strings[0]);
            for (int i3 = 0 + 1; i3 < length2; i3++) {
                sb3.append(", ").append(strings[i3]);
            }
        }
        sb3.append("}");
        String s4 = sb3.toString();
        return s4;
    }

    public void registerForModifyCall(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mModifyCallRegistrants.add(r);
    }

    public void unregisterForModifyCall(Handler h) {
        this.mModifyCallRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForMwi(Handler h, int what, Object obj) {
        Registrant r = new WakeLockRegistrant(h, what, obj, this.mNotifyWakeLock);
        this.mMwiRegistrants.add(r);
    }

    public void unregisterForMwi(Handler h) {
        this.mMwiRegistrants.remove(h);
    }

    static String errorIdToString(int request) {
        switch (request) {
            case 0:
                return "SUCCESS";
            case 1:
                return "E_RADIO_NOT_AVAILABLE";
            case 2:
                return "E_GENERIC_FAILURE";
            case 3:
                return "E_PASSWORD_INCORRECT";
            case 6:
                return "E_REQUEST_NOT_SUPPORTED";
            case 7:
                return "E_CANCELLED";
            case 16:
                return "E_UNUSED";
            case 27:
                return "E_INVALID_PARAMETER";
            case 28:
                return "E_REJECTED_BY_REMOTE";
            case 30:
                return "E_NETWORK_NOT_SUPPORTED";
            case 34:
                return "E_FDN_CHECK_FAILURE";
            case 35:
                return "E_SS_MODIFIED_TO_DIAL";
            case 36:
                return "E_SS_MODIFIED_TO_USSD";
            case 37:
                return "E_SS_MODIFIED_TO_SS";
            case 38:
                return "E_SS_MODIFIED_TO_DIAL_VIDEO";
            case 47:
                return "E_USSD_CS_FALLBACK";
            case 48:
                return "E_SEND_SIP_DTMF_FAILED";
            default:
                return "E_UNKNOWN";
        }
    }

    static String msgIdToString(int request) {
        switch (request) {
            case 1:
                return "REQUEST_IMS_REGISTRATION_STATE";
            case 2:
                return "DIAL";
            case 3:
                return "REQUEST_ANSWER";
            case 4:
                return "HANGUP";
            case 5:
                return "LAST_CALL_FAIL_CAUSE";
            case 6:
                return "GET_CURRENT_CALLS";
            case 7:
                return "HANGUP_WAITING_OR_BACKGROUND";
            case 8:
                return "HANGUP_FOREGROUND_RESUME_BACKGROUND";
            case 9:
                return "MessageId.REQUEST_SWITCH_WAITING_OR_HOLDING_AND_ACTIVE";
            case 10:
                return "CONFERENCE";
            case 11:
                return "REQUEST_EXIT_EMERGENCY_CALLBACK_MODE";
            case 12:
                return "REQUEST_EXPLICIT_CALL_TRANSFER";
            case 13:
                return "DTMF";
            case 14:
                return "DTMF_START";
            case 15:
                return "DTMF_STOP";
            case 16:
                return "UDUB";
            case 17:
                return "MODIFY_CALL_INITIATE";
            case 18:
                return "MODIFY_CALL_CONFIRM";
            case 19:
                return "REQUEST_QUERY_CLIP";
            case 20:
                return "REQUEST_GET_CLIR";
            case 21:
                return "REQUEST_SET_CLIR";
            case 22:
                return "REQUEST_QUERY_CALL_FORWARD_STATUS";
            case 23:
                return "REQUEST_SET_CALL_FORWARD_STATUS";
            case 24:
                return "REQUEST_QUERY_CALL_WAITING";
            case 25:
                return "REQUEST_SET_CALL_WAITING";
            case 26:
                return "REQUEST_IMS_REG_STATE_CHANGE";
            case 27:
                return "REQUEST_SET_SUPP_SVC_NOTIFICATION";
            case 28:
                return "REQUEST_ADD_PARTICIPANT";
            case 29:
                return "REQUEST_QUERY_SERVICE_STATUS";
            case 30:
                return "REQUEST_SET_SERVICE_STATUS";
            case 31:
                return "REQUEST_SUPP_SVC_STATUS";
            case 32:
                return "REQUEST_DEFLECT_CALL";
            case 33:
                return "REQUEST_GET_COLR";
            case 34:
                return "REQUEST_SET_COLR";
            case 37:
                return "REQUEST_HOLD";
            case 38:
                return "REQUEST_RESUME";
            case 39:
                return "REQUEST_SEND_UI_TTY_MODE";
            case 40:
                return "REQUEST_GET_RTP_STATISTICS";
            case 41:
                return "REQUEST_GET_RTP_ERROR_STATISTICS";
            case 42:
                return "REQUEST_GET_WIFI_CALLING_STATUS";
            case 43:
                return "REQUEST_SET_WIFI_CALLING_STATUS";
            case 44:
                return "REQUEST_SET_IMS_CONFIG";
            case 45:
                return "REQUEST_GET_IMS_CONFIG";
            case 46:
                return "REQUEST_SEND_GEOLOCATION_INFO";
            case 48:
                return "REQUEST_GET_VOPS_INFO";
            case 49:
                return "REQUEST_GET_SSAC_INFO";
            case 50:
                return "REQUEST_SET_VOLTE_PREF";
            case 51:
                return "REQUEST_GET_VOLTE_PREF";
            case 52:
                return "REQUEST_GET_HANDOVER_CONFIG";
            case 53:
                return "REQUEST_SET_HANDOVER_CONFIG";
            case 54:
                return "REQUEST_GET_IMS_SUB_CONFIG";
            case 55:
                return "REQUEST_SEND_RTT_MSG";
            case 56:
                return "REQUEST_CANCEL_MODIFY_CALL";
            case 57:
                return "REQUEST_SEND_IMS_SMS";
            case 58:
                return "REQUEST_ACK_IMS_SMS";
            case 59:
                return "REQUEST_ACK_IMS_SMS_STATUS_REPORT";
            case 60:
                return "REQUEST_REGISTER_MULTI_IDENTITY_LINES";
            case 61:
                return "REQUEST_QUERY_VIRTUAL_LINE_INFO";
            case 62:
                return "REQUEST_EMERGENCY_DIAL";
            case 63:
                return "CALL_COMPOSER_DIAL";
            case 64:
                return "REQUEST_USSD";
            case 65:
                return "REQUEST_CANCEL_USSD";
            case 66:
                return "REQUEST_SIP_DTMF";
            case MessageId.UNSOL_RESPONSE_CALL_STATE_CHANGED /* 201 */:
                return "UNSOL_RESPONSE_CALL_STATE_CHANGED";
            case MessageId.UNSOL_CALL_RING /* 202 */:
                return "UNSOL_CALL_RING";
            case MessageId.UNSOL_RINGBACK_TONE /* 203 */:
                return "UNSOL_RINGBACK_TONE";
            case MessageId.UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED /* 204 */:
                return "UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED";
            case MessageId.UNSOL_ENTER_EMERGENCY_CALLBACK_MODE /* 205 */:
                return "UNSOL_ENTER_EMERGENCY_CALLBACK_MODE";
            case MessageId.UNSOL_EXIT_EMERGENCY_CALLBACK_MODE /* 206 */:
                return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
            case MessageId.UNSOL_MODIFY_CALL /* 207 */:
                return "UNSOL_MODIFY_CALL";
            case MessageId.UNSOL_RESPONSE_HANDOVER /* 208 */:
                return "UNSOL_RESPONSE_HANDOVER";
            case MessageId.UNSOL_REFRESH_CONF_INFO /* 209 */:
                return "UNSOL_REFRESH_CONF_INFO";
            case MessageId.UNSOL_SRV_STATUS_UPDATE /* 210 */:
                return "UNSOL_SRV_STATUS_UPDATE";
            case MessageId.UNSOL_SUPP_SVC_NOTIFICATION /* 211 */:
                return "UNSOL_SUPP_SVC_NOTIFICATION";
            case MessageId.UNSOL_TTY_NOTIFICATION /* 212 */:
                return "UNSOL_TTY_NOTIFICATION";
            case MessageId.UNSOL_RADIO_STATE_CHANGED /* 213 */:
                return "UNSOL_RADIO_STATE_CHANGED";
            case MessageId.UNSOL_MWI /* 214 */:
                return "UNSOL_MWI";
            case MessageId.UNSOL_REFRESH_VICE_INFO /* 215 */:
                return "UNSOL_REFRESH_VICE_INFO";
            case MessageId.UNSOL_REQUEST_GEOLOCATION /* 216 */:
                return "UNSOL_REQUEST_GEOLOCATION";
            case MessageId.UNSOL_VOWIFI_CALL_QUALITY /* 217 */:
                return "UNSOL_VOWIFI_CALL_QUALITY";
            case MessageId.UNSOL_VOPS_CHANGED /* 218 */:
                return "UNSOL_VOPS_CHANGED";
            case MessageId.UNSOL_SSAC_CHANGED /* 219 */:
                return "UNSOL_SSAC_CHANGED";
            case MessageId.UNSOL_PARTICIPANT_STATUS_INFO /* 220 */:
                return "UNSOL_PARTICIPANT_STATUS_INFO";
            case MessageId.UNSOL_IMS_SUB_CONFIG_CHANGED /* 221 */:
                return "UNSOL_IMS_SUB_CONFIG_CHANGED";
            case MessageId.UNSOL_RESPONSE_REGISTRATION_BLOCK_STATUS /* 222 */:
                return "UNSOL_RESPONSE_REGISTRATION_BLOCK_STATUS";
            case MessageId.UNSOL_RESPONSE_RTT_MSG_RECEIVED /* 223 */:
                return "UNSOL_RESPONSE_RTT_MSG_RECEIVED";
            case MessageId.UNSOL_ON_SS /* 224 */:
                return "UNSOL_ON_SS";
            case MessageId.UNSOL_IMS_SMS_STATUS_REPORT /* 225 */:
                return "UNSOL_IMS_SMS_STATUS_REPORT";
            case MessageId.UNSOL_INCOMING_IMS_SMS /* 226 */:
                return "UNSOL_INCOMING_IMS_SMS";
            case MessageId.UNSOL_AUTO_CALL_REJECTION_IND /* 227 */:
                return "UNSOL_AUTO_CALL_REJECTION_IND";
            case MessageId.UNSOL_VOICE_INFO /* 228 */:
                return "UNSOL_VOICE_INFO";
            case MessageId.UNSOL_MULTI_IDENTITY_REGISTRATION_STATUS_CHANGE /* 229 */:
                return "UNSOL_MULTI_IDENTITY_REGISTRATION_STATUS_CHANGE";
            case MessageId.UNSOL_MULTI_IDENTITY_INFO_PENDING /* 230 */:
                return "UNSOL_MULTI_IDENTITY_INFO_PENDING";
            case MessageId.UNSOL_MODEM_SUPPORTS_WFC_ROAMING_MODE /* 231 */:
                return "UNSOL_MODEM_SUPPORTS_WFC_ROAMING_MODE";
            case MessageId.UNSOL_USSD_FAILED /* 232 */:
                return "UNSOL_USSD_FAILED";
            case MessageId.UNSOL_AUTO_CALL_COMPOSER_CALL_REJECTION_IND /* 233 */:
                return "UNSOL_AUTO_CALL_COMPOSER_CALL_REJECTION_IND";
            case MessageId.UNSOL_CALL_COMPOSER_INFO_AVAILABLE_IND /* 234 */:
                return "UNSOL_CALL_COMPOSER_INFO_AVAILABLE_IND";
            case MessageId.UNSOL_RETRIEVE_GEO_LOCATION_DATA_STATUS /* 235 */:
                return "UNSOL_RETRIEVE_GEO_LOCATION_DATA_STATUS";
            case MessageId.UNSOL_USSD_RECEIVED /* 236 */:
                return "UNSOL_USSD_RECEIVED";
            case MessageId.UNSOL_SIP_DTMF_RECEIVED /* 237 */:
                return "UNSOL_SIP_DTMF_RECEIVED";
            default:
                return "<unknown message>";
        }
    }

    public void log(String msg) {
        Log.i(this, msg + "[SUB" + this.mPhoneId + "]");
    }

    public void logv(String msg) {
        Log.v(this, msg + "[SUB" + this.mPhoneId + "]");
    }

    public void logUnimplemented() {
        try {
            Exception e = new Exception();
            throw e;
        } catch (Exception e2) {
            Log.i(this, "Unimplemented method. Stack trace: ");
            e2.printStackTrace();
        }
    }

    public void unsljLog(int response) {
        log("[UNSL]< " + msgIdToString(response));
    }

    public void unsljLogMore(int response, String more) {
        log("[UNSL]< " + msgIdToString(response) + " " + more);
    }

    public void unsljLogRet(int response, Object ret) {
        log("[UNSL]< " + msgIdToString(response) + " " + retToString(response, ret));
    }

    public void unsljLogvRet(int response, Object ret) {
        logv("[UNSL]< " + msgIdToString(response) + " " + retToString(response, ret));
    }

    public void logSolicitedRequest(IFRequest rr) {
        log(rr.serialString() + "> " + msgIdToString(rr.mRequest) + " ");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setPhoneType(int phoneType) {
        log("setPhoneType=" + phoneType + " old value=" + this.mPhoneType);
        this.mPhoneType = phoneType;
    }

    public void addParticipant(String address, int clirMode, CallDetails callDetails, Message result) {
        log("addParticipant address = " + Log.pii(address) + " clirMode = " + clirMode + " callDetails = " + callDetails);
        String msgIdString = msgIdToString(28);
        IFRequest rr = IFRequest.obtain(28, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        DialRequest dialRequest = ImsRadioUtils.buildDialRequest(address, clirMode, callDetails, false);
        try {
            logSolicitedRequest(rr);
            vendor.qti.hardware.radio.ims.V1_4.IImsRadio imsRadio = imsRadioV14();
            if (imsRadio != null) {
                imsRadio.addParticipant_1_4(rr.mSerial, dialRequest);
            } else {
                Log.w(this, "mImsRadio V1.4 is null. invoking mImsRadio.dial()");
                imsRadioV10().addParticipant(rr.mSerial, ImsRadioUtils.migrateFromDialRequestV14(dialRequest));
            }
            Log.v(this, msgIdString + " request to IImsRadio - token = " + rr.mSerial + " address = " + dialRequest.address + " callType = " + dialRequest.callDetails.callType + " callDomain = " + dialRequest.callDetails.callDomain + " isConferenceUri = " + dialRequest.isConferenceUri + "isCallPull =" + dialRequest.isCallPull + " isEncrypted = " + dialRequest.isEncrypted);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + " to IImsRadio: Exception: " + ex);
        }
    }

    private boolean sendErrorOnImsRadioDown(IFRequest rr, String msgIdString) {
        return sendErrorOnImsRadioDown(rr, msgIdString, null);
    }

    private boolean sendErrorOnImsRadioDown(IFRequest rr, String msgIdString, Object ret) {
        if (imsRadioV10() == null) {
            Log.e(this, "ImsRadio HAL is not available. Can't send " + msgIdString + " to QCRIL");
            rr.onError(1, ret);
            rr.release();
            return true;
        }
        return false;
    }

    public void dial(String address, EmergencyCallInfo eInfo, int clirMode, CallDetails callDetails, boolean isEncrypted, Message result) {
        dial(address, eInfo, clirMode, callDetails, isEncrypted, null, null, result);
    }

    // 由 JADX 反編譯修正：JADX 漏掉共用的 catch 收尾，依 smali 還原成單一 try / catch(Exception)
    public void dial(String address, EmergencyCallInfo eInfo, int clirMode, CallDetails callDetails, boolean isEncrypted, CallComposerInfo ccInfo, RedialInfo redialInfo, Message result) {
        int msgId;
        log("Dial Request - address= " + Log.pii(address) + "clirMode= " + clirMode + " callDetails= " + callDetails + " isEncrypted= " + isEncrypted + " redialInfo " + redialInfo);
        if (eInfo != null && imsRadioV15() != null) {
            msgId = 62;
        } else if (ccInfo != null && imsRadioV16() != null) {
            msgId = 63;
        } else {
            msgId = 2;
        }
        String msgIdString = msgIdToString(msgId);
        IFRequest rr = IFRequest.obtain(msgId, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        DialRequest dialRequest = ImsRadioUtils.buildDialRequest(address, clirMode, callDetails, isEncrypted, redialInfo);
        try {
            logSolicitedRequest(rr);
            if (imsRadioV16() != null) {
                vendor.qti.hardware.radio.ims.V1_6.DialRequest dialRequestV16 = ImsRadioUtilsV16.buildDialRequest(address, clirMode, callDetails, isEncrypted, redialInfo);
                if (ccInfo != null) {
                    log("Dialing call composer call v1.6");
                    vendor.qti.hardware.radio.ims.V1_6.CallComposerInfo composerInfo = ImsRadioUtilsV16.buildCallComposerInfoHal(ccInfo);
                    imsRadioV16().callComposerDial(rr.mSerial, dialRequestV16, composerInfo);
                } else if (eInfo != null) {
                    int categories = ImsRadioUtilsV15.mapEmergencyServiceCategoryToHal(eInfo.getEmergencyServiceCategories());
                    int route = ImsRadioUtilsV15.mapEmergencyCallRoutingToHal(eInfo.getEmergencyCallRouting());
                    ArrayList<String> urns = new ArrayList<>(eInfo.getEmergencyUrns());
                    log("emergencyDial v1.6");
                    imsRadioV16().emergencyDial_1_6(rr.mSerial, dialRequestV16, categories, urns, route, eInfo.hasKnownUserIntentEmergency(), eInfo.isEmergencyCallTesting());
                } else {
                    log("dial v1.6");
                    imsRadioV16().dial_1_6(rr.mSerial, dialRequestV16);
                }
            } else if (eInfo != null && imsRadioV15() != null) {
                int categories2 = ImsRadioUtilsV15.mapEmergencyServiceCategoryToHal(eInfo.getEmergencyServiceCategories());
                int route2 = ImsRadioUtilsV15.mapEmergencyCallRoutingToHal(eInfo.getEmergencyCallRouting());
                ArrayList<String> urns2 = new ArrayList<>(eInfo.getEmergencyUrns());
                log("emergencyDial v1.5");
                imsRadioV15().emergencyDial(rr.mSerial, dialRequest, categories2, urns2, route2, eInfo.hasKnownUserIntentEmergency(), eInfo.isEmergencyCallTesting());
            } else {
                vendor.qti.hardware.radio.ims.V1_4.IImsRadio imsRadio = imsRadioV14();
                if (imsRadio != null) {
                    log("dial v1.4");
                    imsRadio.dial_1_4(rr.mSerial, dialRequest);
                } else {
                    log("dial v1.0");
                    imsRadioV10().dial(rr.mSerial, ImsRadioUtils.migrateFromDialRequestV14(dialRequest));
                }
            }
            Log.v(this, msgIdString + " request to IImsRadio - token = " + rr.mSerial + "address = " + dialRequest.address + "callType =" + dialRequest.callDetails.callType + "callDomain =" + dialRequest.callDetails.callDomain + "isConferenceUri =" + dialRequest.isConferenceUri + "isCallPull =" + dialRequest.isCallPull + "isEncrypted =" + dialRequest.isEncrypted + "rttMode =" + dialRequest.callDetails.rttMode);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "to IImsRadio: Exception: " + ex);
        }
    }

    public void sendUssd(String address, Message result) {
        log("USSD Request - address- " + Log.pii(address));
        String msgIdString = msgIdToString(64);
        IFRequest rr = IFRequest.obtain(64, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        if (imsRadioV16() == null) {
            sendResponse(rr, 47, new ImsReasonInfo());
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV16().sendUssd(rr.mSerial, address);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + " to IImsRadio: Exception: " + ex);
        }
    }

    public void sendSipDtmf(String requestCode, Message result) {
        log("Send Sip Dtmf Request - requestCode- " + Log.pii(requestCode));
        String msgIdString = msgIdToString(66);
        IFRequest rr = IFRequest.obtain(66, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        if (imsRadioV17() == null) {
            sendResponse(rr, 48, null);
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV17().sendSipDtmf(rr.mSerial, requestCode);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + " to IImsRadio: Exception: " + ex);
        }
    }

    public void cancelPendingUssd(Message result) {
        log("Cancel pending USSD");
        String msgIdString = msgIdToString(65);
        IFRequest rr = IFRequest.obtain(65, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV16().cancelPendingUssd(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + " to IImsRadio: Exception: " + ex);
        }
    }

    public void acceptCall(Message result, int callType) {
        acceptCall(result, callType, 0);
    }

    public void acceptCall(Message result, int callType, int rttMode) {
        acceptCall(result, callType, 2, rttMode);
    }

    public void acceptCall(Message result, int callType, int ipPresentation, int rttMode) {
        String msgIdString = msgIdToString(3);
        IFRequest rr = IFRequest.obtain(3, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().answer(rr.mSerial, ImsRadioUtils.callTypeToHal(callType), ipPresentation, rttMode);
            Log.i(this, "rtt mode : " + rttMode + " and ipPresentation: " + ipPresentation + " to HAL");
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void deflectCall(int index, String number, Message result) {
        logv("deflect call to: " + number + "connid:" + index);
        String msgIdString = msgIdToString(32);
        IFRequest rr = IFRequest.obtain(32, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        DeflectRequestInfo deflectRequestInfo = new DeflectRequestInfo();
        deflectRequestInfo.connIndex = index;
        deflectRequestInfo.number = number;
        try {
            logSolicitedRequest(rr);
            imsRadioV10().deflectCall(rr.mSerial, deflectRequestInfo);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void sendSms(int messageRef, String format, String smsc, boolean isRetry, byte[] pdu, Message result) {
        Log.i(this, "sendSms over IImsRadio with format:" + format);
        String msgIdString = msgIdToString(57);
        IFRequest rr = IFRequest.obtain(57, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        ImsSmsMessage imsSms = ImsRadioUtils.buildImsSms(messageRef, format, smsc, isRetry, pdu);
        try {
            logSolicitedRequest(rr);
            imsRadioV12().sendImsSms(rr.mSerial, imsSms);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(57) + "to ImsRadioV12: Exception: " + ex.getMessage());
        }
    }

    public void acknowledgeSms(int messageRef, int result, Message target) {
        Log.i(this, "acknowledgeSms: messageRef: " + messageRef + " result: " + result);
        String msgIdString = msgIdToString(58);
        IFRequest rr = IFRequest.obtain(58, target);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        int deliverStatus = ImsRadioUtils.imsSmsDeliverStatusToHidl(result);
        try {
            logSolicitedRequest(rr);
            vendor.qti.hardware.radio.ims.V1_5.IImsRadio imsRadioV15 = imsRadioV15();
            if (imsRadioV15 != null) {
                imsRadioV15().acknowledgeSms_1_5(rr.mSerial, messageRef, deliverStatus);
            } else {
                Log.w(this, "ImsRadioV15 is null. Invoking ImsRadioV12.acknowledgeSms()");
                imsRadioV12().acknowledgeSms(rr.mSerial, messageRef, deliverStatus);
            }
        } catch (Exception ex) {
            Log.e(this, msgIdToString(58) + "request to ImsRadio: Exception: " + ex);
        }
        rr.release();
    }

    public void acknowledgeSmsReport(int messageRef, int result, Message target) {
        Log.i(this, "acknowledgeSmsReport: messageRef: " + messageRef + " result: " + result);
        String msgIdString = msgIdToString(59);
        IFRequest rr = IFRequest.obtain(59, target);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        int statusReportStatus = ImsRadioUtils.imsSmsStatusReportStatusToHidl(result);
        try {
            logSolicitedRequest(rr);
            imsRadioV12().acknowledgeSmsReport(rr.mSerial, messageRef, statusReportStatus);
        } catch (Exception ex) {
            Log.e(this, msgIdToString(59) + "to ImsRadioV12: Exception: " + ex);
        }
        rr.release();
    }

    public String getSmsFormat() {
        try {
            return imsRadioV12().getSmsFormat();
        } catch (Exception ex) {
            Log.e(this, "Failed to getSmsFormat. Exception " + ex);
            return null;
        }
    }

    // 由 JADX 反編譯修正：JADX 漏掉共用的 catch 收尾，依 smali 還原
    public synchronized void sendGeolocationInfo(double lat, double lon, Address address, Message result) {
        Log.i(this, "sendGeolocationInfo: lat: " + lat + " lon: " + lon + " address: " + address);
        String msgIdString = msgIdToString(46);
        IFRequest rr = IFRequest.obtain(46, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        AddressInfo addressInfo = ImsRadioUtils.getHidlAddressInfo(lat, lon, address);
        try {
            logSolicitedRequest(rr);
            vendor.qti.hardware.radio.ims.V1_2.IImsRadio imsRadioV12 = imsRadioV12();
            if (imsRadioV12 != null) {
                imsRadioV12.sendGeolocationInfo_1_2(rr.mSerial, lat, lon, addressInfo);
            } else {
                imsRadioV10().sendGeolocationInfo(rr.mSerial, lat, lon, ImsRadioUtils.migrateAddressToV10(addressInfo));
            }
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void hangupConnection(int index, Message result) {
        hangupWithReason(index, null, null, false, RadioTech.RADIO_TECH_INVALID, null, result);
    }

    public void hangupWithReason(int connectionId, String userUri, String confUri, boolean mpty, int failCause, String errorInfo, Message result) {
        String msgIdString = msgIdToString(4);
        IFRequest rr = IFRequest.obtain(4, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        HangupRequestInfo hangup = ImsRadioUtils.buildHangupRequest(connectionId, userUri, confUri, mpty, failCause, errorInfo);
        try {
            logSolicitedRequest(rr);
            vendor.qti.hardware.radio.ims.V1_3.IImsRadio imsRadioV13 = imsRadioV13();
            if (imsRadioV13 != null) {
                imsRadioV13.hangup_1_3(rr.mSerial, hangup);
            } else {
                Log.w(this, "ImsRadioV13 is null. Invoking ImsRadioV10.hangup()");
                imsRadioV10().hangup(rr.mSerial, ImsRadioUtils.migrateHangupRequestInfoFromV13(hangup));
            }
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void queryServiceStatus(Message result) {
        String msgIdString = msgIdToString(29);
        IFRequest rr = IFRequest.obtain(29, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().queryServiceStatus(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, "Serivce status query request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setServiceStatus(Message result, ArrayList<CapabilityStatus> capabilityStatusList, int restrictCause) {
        int i = restrictCause;
        int msgId = 30;
        int i2 = 30;
        String msgIdString = msgIdToString(30);
        vendor.qti.hardware.radio.ims.V1_6.IImsRadio imsRadio = imsRadioV16();
        if (imsRadio != null) {
            IFRequest rr = IFRequest.obtain(30, result);
            if (sendErrorOnImsRadioDown(rr, msgIdString, capabilityStatusList)) {
                return;
            }
            SomeArgs setCapArgs = SomeArgs.obtain();
            setCapArgs.arg1 = Message.obtain(result);
            setCapArgs.arg2 = capabilityStatusList;
            Message newMsg = Message.obtain();
            newMsg.obj = setCapArgs;
            rr.setResult(newMsg);
            queueRequest(rr);
            ArrayList<vendor.qti.hardware.radio.ims.V1_6.ServiceStatusInfo> serviceStatusInfoList = ImsRadioUtils.buildServiceStatusInfoList(capabilityStatusList, restrictCause);
            logSolicitedRequest(rr);
            Log.i(this, msgIdString + " to ImsRadio: token -" + rr.mSerial + " RestrictCause:" + i);
            try {
                imsRadio.setServiceStatus_1_6(rr.mSerial, serviceStatusInfoList);
            } catch (Exception ex) {
                rr.setResult(result);
                removeFromQueueAndSendResponse(rr.mSerial, capabilityStatusList);
                Log.e(this, "SetServiceStatus request to IImsRadio: Exception: " + ex);
            }
            return;
        }
        for (CapabilityStatus capabilityStatus : capabilityStatusList) {
            IFRequest rr2 = IFRequest.obtain(i2, result);
            if (sendErrorOnImsRadioDown(rr2, msgIdString, capabilityStatusList)) {
                return;
            }
            SomeArgs setCapArgs2 = SomeArgs.obtain();
            setCapArgs2.arg1 = Message.obtain(result);
            ArrayList<CapabilityStatus> newCapabilityStatusList = new ArrayList<>();
            newCapabilityStatusList.add(capabilityStatus);
            setCapArgs2.arg2 = newCapabilityStatusList;
            Message newMsg2 = Message.obtain();
            newMsg2.obj = setCapArgs2;
            rr2.setResult(newMsg2);
            queueRequest(rr2);
            int msgId2 = msgId;
            Log.v(this, "SetServiceStatus capabilityStatus: " + capabilityStatus);
            vendor.qti.hardware.radio.ims.V1_6.IImsRadio imsRadio2 = imsRadio;
            vendor.qti.hardware.radio.ims.V1_6.ServiceStatusInfo serviceStatusInfo = ImsRadioUtils.buildServiceStatusInfo(ImsRadioUtils.mapCapabilityToSrvType(capabilityStatus.getCapability()), ImsRadioUtils.mapRadioTechToHidlRadioTech(capabilityStatus.getRadioTech()), ImsRadioUtils.mapValueToServiceStatus(capabilityStatus.getStatus()), i);
            logSolicitedRequest(rr2);
            Log.i(this, msgIdString + " to ImsRadio: token -" + rr2.mSerial);
            try {
                Log.w(this, "mImsRadio V1.6 is null. invoking mImsRadio.setServiceStatus()");
                imsRadioV10().setServiceStatus(rr2.mSerial, ImsRadioUtils.migrateServiceStatusInfoFromV16(serviceStatusInfo));
                i = restrictCause;
                msgId = msgId2;
                imsRadio = imsRadio2;
                i2 = 30;
            } catch (Exception ex2) {
                rr2.setResult(result);
                removeFromQueueAndSendResponse(rr2.mSerial, capabilityStatusList);
                Log.e(this, "SetServiceStatus request to IImsRadio: Exception: " + ex2);
                return;
            }
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void getImsRegistrationState(Message result) {
        String msgIdString = msgIdToString(1);
        IFRequest rr = IFRequest.obtain(1, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().getImsRegistrationState(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void sendImsRegistrationState(int imsRegState, Message result) {
        String msgIdString = msgIdToString(26);
        IFRequest rr = IFRequest.obtain(26, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            Log.i(this, msgIdString + " request to ImsRadio - token:" + rr.mSerial + " RegState" + imsRegState);
            imsRadioV10().requestRegistrationChange(rr.mSerial, ImsRadioUtils.regStateToHal(imsRegState));
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void modifyCallInitiate(Message result, CallModify callModify) {
        logv("modifyCallInitiate callModify= " + callModify);
        String msgIdString = msgIdToString(17);
        IFRequest rr = IFRequest.obtain(17, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().modifyCallInitiate(rr.mSerial, ImsRadioUtils.buildCallModifyInfo(callModify));
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void cancelModifyCall(Message result, int callId) {
        logv("cancelModifyCall");
        String msgIdString = msgIdToString(56);
        IFRequest rr = IFRequest.obtain(56, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            Log.i(this, msgIdString + " request to ImsRadio - " + rr.mSerial + " callId:" + callId);
            imsRadioV10().cancelModifyCall(rr.mSerial, callId);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void modifyCallConfirm(Message result, CallModify callModify) {
        logv("modifyCallConfirm callModify= " + callModify);
        String msgIdString = msgIdToString(18);
        IFRequest rr = IFRequest.obtain(18, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().modifyCallConfirm(rr.mSerial, ImsRadioUtils.buildCallModifyInfo(callModify));
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void hold(Message result, int callId) {
        String msgIdString = msgIdToString(37);
        IFRequest rr = IFRequest.obtain(37, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            Log.i(this, msgIdString + " request to ImsRadio - " + rr.mSerial + " callId:" + callId);
            imsRadioV10().hold(rr.mSerial, callId);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void resume(Message result, int callId) {
        String msgIdString = msgIdToString(38);
        IFRequest rr = IFRequest.obtain(38, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            Log.i(this, msgIdString + " request to ImsRadio - " + rr.mSerial + " callId:" + callId);
            imsRadioV10().resume(rr.mSerial, callId);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void conference(Message result) {
        String msgIdString = msgIdToString(10);
        IFRequest rr = IFRequest.obtain(10, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().conference(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void explicitCallTransfer(int srcCallId, int type, String number, int destCallId, Message result) {
        logv("explicitCallTransfer srcCallId= " + srcCallId + " type= " + type + " number= " + number + "destCallId = " + destCallId);
        String msgIdString = msgIdToString(12);
        IFRequest rr = IFRequest.obtain(12, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        ExplicitCallTransferInfo ectInfo = ImsRadioUtils.buildExplicitCallTransferInfo(srcCallId, type, number, destCallId);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().explicitCallTransfer(rr.mSerial, ectInfo);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void sendConfigRequest(int requestType, int item, boolean boolValue, int intValue, String strValue, int errorCause, Message result) {
        String msgIdString = msgIdToString(requestType);
        IFRequest rr = IFRequest.obtain(requestType, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            if (requestType == 45) {
                ConfigInfo configInfo = ImsRadioUtils.buildConfigInfo(item, boolValue, intValue, strValue, errorCause);
                Log.i(this, msgIdString + " request to ImsRadio: token " + rr.mSerial + " item:" + configInfo.item + " boolValue:" + boolValue + " intValue:" + intValue + " strValue:" + strValue + " errorCause:" + errorCause);
                imsRadioV10().getConfig(rr.mSerial, configInfo);
            } else {
                vendor.qti.hardware.radio.ims.V1_6.IImsRadio imsRadioV16 = imsRadioV16();
                vendor.qti.hardware.radio.ims.V1_6.ConfigInfo configInfo2 = ImsRadioUtilsV16.buildConfigInfo(item, boolValue, intValue, strValue, errorCause);
                Log.i(this, msgIdString + " request to ImsRadio: token " + rr.mSerial + " item:" + configInfo2.item + " boolValue:" + boolValue + " intValue:" + intValue + " strValue:" + strValue + " errorCause:" + errorCause);
                if (imsRadioV16 != null) {
                    imsRadioV16.setConfig_1_6(rr.mSerial, configInfo2);
                } else {
                    vendor.qti.hardware.radio.ims.V1_5.ConfigInfo configInfoV15 = ImsRadioUtilsV15.migrateConfigInfoFromV16(configInfo2);
                    if (imsRadioV15() != null) {
                        imsRadioV15().setConfig_1_5(rr.mSerial, configInfoV15);
                    } else {
                        imsRadioV10().setConfig(rr.mSerial, ImsRadioUtils.migrateConfigInfoFromV15(configInfoV15));
                    }
                }
            }
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + " request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void sendDtmf(char c, Message result) {
        String msgIdString = msgIdToString(13);
        IFRequest rr = IFRequest.obtain(13, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        DtmfInfo dtmfValue = new DtmfInfo();
        dtmfValue.dtmf = Character.toString(c);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().sendDtmf(rr.mSerial, dtmfValue);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void startDtmf(char c, Message result) {
        String msgIdString = msgIdToString(14);
        IFRequest rr = IFRequest.obtain(14, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        DtmfInfo dtmfValue = new DtmfInfo();
        dtmfValue.dtmf = Character.toString(c);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().startDtmf(rr.mSerial, dtmfValue);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(14) + "to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void stopDtmf(Message result) {
        String msgIdString = msgIdToString(15);
        IFRequest rr = IFRequest.obtain(15, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().stopDtmf(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(15) + "to IImsRadio: Exception: " + ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object processConfigResponse(vendor.qti.hardware.radio.ims.V1_6.ConfigInfo configInfo) {
        if (configInfo.hasBoolValue) {
            return Boolean.valueOf(configInfo.boolValue);
        }
        if (configInfo.intValue != Integer.MAX_VALUE) {
            return Integer.valueOf(configInfo.intValue);
        }
        if (configInfo.stringValue != null) {
            return configInfo.stringValue;
        }
        if (configInfo.errorCause != 6) {
            return Integer.valueOf(ImsRadioUtils.configFailureCauseFromHal(configInfo.errorCause));
        }
        return ImsRadioUtilsV16.configInfoFromHal(configInfo);
    }

    private void disableSrvStatus() {
        Log.v(this, "disableSrvStatus");
        if (this.mSrvStatusRegistrations != null) {
            ImsRilException ex = new ImsRilException(1, null);
            this.mSrvStatusRegistrations.notifyRegistrants(new AsyncResult((Object) null, (Object) null, ex));
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setSuppServiceNotifications(boolean enable, Message result) {
        logv("setSuppServiceNotifications enable = " + enable);
        String msgIdString = msgIdToString(27);
        IFRequest rr = IFRequest.obtain(27, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        int status = enable ? 1 : 0;
        try {
            logSolicitedRequest(rr);
            imsRadioV10().setSuppServiceNotification(rr.mSerial, status);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void getCLIR(Message result) {
        logv("getCLIR");
        String msgIdString = msgIdToString(20);
        IFRequest rr = IFRequest.obtain(20, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().getClir(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, "GetClir request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setCLIR(int clirMode, Message result) {
        logv("setCLIR clirmode = " + clirMode);
        String msgIdString = msgIdToString(21);
        IFRequest rr = IFRequest.obtain(21, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        ClirInfo clirValue = new ClirInfo();
        clirValue.paramN = clirMode;
        try {
            logSolicitedRequest(rr);
            imsRadioV10().setClir(rr.mSerial, clirValue);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(21) + "to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void queryCallWaiting(int serviceClass, Message response) {
        logv("queryCallWaiting serviceClass = " + serviceClass);
        String msgIdString = msgIdToString(24);
        IFRequest rr = IFRequest.obtain(24, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().getCallWaiting(rr.mSerial, serviceClass);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setCallWaiting(boolean enable, int serviceClass, Message response) {
        logv("setCallWaiting enable = " + enable + "serviceClass = " + serviceClass);
        String msgIdString = msgIdToString(25);
        IFRequest rr = IFRequest.obtain(25, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        int serviceStatus = enable ? 1 : 0;
        try {
            logSolicitedRequest(rr);
            imsRadioV10().setCallWaiting(rr.mSerial, serviceStatus, serviceClass);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void queryIncomingCallBarring(String facility, int serviceClass, Message response) {
        suppSvcStatus(3, facilityStringToInt(facility), null, serviceClass, response);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setIncomingCallBarring(int operation, String facility, String[] icbNum, int serviceClass, Message response) {
        suppSvcStatus(operation, facilityStringToInt(facility), icbNum, serviceClass, response);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setCallForward(int action, int cfReason, int serviceClass, String number, int timeSeconds, Message response) {
        logv("setCallForward cfReason= " + cfReason + " serviceClass = " + serviceClass + "number = " + number + "timeSeconds = " + timeSeconds);
        setCallForwardInternal(RadioTech.RADIO_TECH_INVALID, RadioTech.RADIO_TECH_INVALID, RadioTech.RADIO_TECH_INVALID, RadioTech.RADIO_TECH_INVALID, action, cfReason, serviceClass, number, timeSeconds, response);
    }

    public void setCallForwardUncondTimer(int startHour, int startMinute, int endHour, int endMinute, int action, int cfReason, int serviceClass, String number, Message response) {
        setCallForwardInternal(startHour, startMinute, endHour, endMinute, action, cfReason, serviceClass, number, 0, response);
    }

    private void setCallForwardInternal(int startHour, int startMinute, int endHour, int endMinute, int action, int cfReason, int serviceClass, String number, int timeSeconds, Message response) {
        logv("setCallForwardInternal cfReason= " + cfReason + " serviceClass = " + serviceClass + "number = " + number + "startHour = " + startHour + "startMinute = " + startMinute + "endHour = " + endHour + "endMin = " + endMinute);
        String msgIdString = msgIdToString(23);
        IFRequest rr = IFRequest.obtain(23, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        CallForwardInfo cfInfo = ImsRadioUtils.buildCallForwardInfo(cfReason, serviceClass, number, action, timeSeconds);
        if (startHour != Integer.MAX_VALUE && startMinute != Integer.MAX_VALUE) {
            cfInfo.hasCallFwdTimerStart = true;
            ImsRadioUtils.buildCallFwdTimerInfo(cfInfo.callFwdTimerStart, startHour, startMinute);
        }
        if (endHour != Integer.MAX_VALUE && endMinute != Integer.MAX_VALUE) {
            cfInfo.hasCallFwdTimerEnd = true;
            ImsRadioUtils.buildCallFwdTimerInfo(cfInfo.callFwdTimerEnd, endHour, endMinute);
        }
        try {
            logSolicitedRequest(rr);
            imsRadioV10().setCallForwardStatus(rr.mSerial, cfInfo);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void queryCallForwardStatus(int cfReason, int serviceClass, String number, Message response) {
        logv("queryCallForwardStatus cfReason= " + cfReason + " serviceClass = " + serviceClass + "number = " + number);
        String msgIdString = msgIdToString(22);
        IFRequest rr = IFRequest.obtain(22, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        CallForwardInfo cfInfo = ImsRadioUtils.buildCallForwardInfo(cfReason, serviceClass, number, 2, 0);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().queryCallForwardStatus(rr.mSerial, cfInfo);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void queryCLIP(Message response) {
        logv("queryClip");
        String msgIdString = msgIdToString(19);
        IFRequest rr = IFRequest.obtain(19, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().getClip(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, "GetClip request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setUiTTYMode(int uiTtyMode, Message response) {
        logv("setUiTTYMode uittyMode=" + uiTtyMode);
        String msgIdString = msgIdToString(39);
        IFRequest rr = IFRequest.obtain(39, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        TtyInfo info = new TtyInfo();
        info.mode = ImsRadioUtils.ttyModeToHal(uiTtyMode);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().setUiTtyMode(rr.mSerial, info);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(39) + "to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void exitEmergencyCallbackMode(Message response) {
        logv("exitEmergencyCallbackMode");
        String msgIdString = msgIdToString(11);
        IFRequest rr = IFRequest.obtain(11, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().exitEmergencyCallbackMode(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void queryFacilityLock(String facility, String password, int serviceClass, Message response) {
        suppSvcStatus(3, facilityStringToInt(facility), response);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setFacilityLock(String facility, boolean lockState, String password, int serviceClass, Message response) {
        int operation = lockState ? 1 : 2;
        suppSvcStatus(operation, facilityStringToInt(facility), response);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void getSuppSvc(String facility, Message response) {
        suppSvcStatus(3, facilityStringToInt(facility), response);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setSuppSvc(String facility, boolean lockState, Message response) {
        int operation = lockState ? 1 : 2;
        suppSvcStatus(operation, facilityStringToInt(facility), response);
    }

    public void suppSvcStatus(int operationType, int facility, String[] icbNum, int serviceClassValue, Message response) {
        logv("suppSvcStatus operationType = " + operationType + " facility = " + facility + "serviceClassValue = " + serviceClassValue);
        suppSvcStatusInternal(operationType, facility, icbNum, null, serviceClassValue, response);
    }

    public void suppSvcStatus(int operationType, int facility, Message response) {
        logv("suppSvcStatus operationType = " + operationType + " facility = " + facility);
        suppSvcStatusInternal(operationType, facility, null, null, RadioTech.RADIO_TECH_INVALID, response);
    }

    public void suppSvcStatus(int operationType, int facility, String[] icbNum, String password, int serviceClassValue, Message response) {
        logv("suppSvcStatus operationType = " + operationType + " facility = " + facility + "serviceClassValue = " + serviceClassValue);
        suppSvcStatusInternal(operationType, facility, icbNum, password, serviceClassValue, response);
    }

    private void suppSvcStatusInternal(int operationType, int facility, String[] inCbNumList, String password, int serviceClass, Message response) {
        String msgIdString = msgIdToString(31);
        IFRequest rr = IFRequest.obtain(31, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        CbNumListInfo cbNumListInfo = ImsRadioUtils.buildCbNumListInfo(inCbNumList, serviceClass);
        // 由 JADX 反編譯修正：依 smali，整段在同一個 try / catch(Exception) 內
        try {
            logSolicitedRequest(rr);
            vendor.qti.hardware.radio.ims.V1_6.IImsRadio imsRadio = imsRadioV16();
            if (imsRadio != null) {
                imsRadio.suppServiceStatus_1_6(rr.mSerial, operationType, ImsRadioUtils.facilityTypeToHal(facility), cbNumListInfo, password != null ? password : "");
            } else {
                imsRadioV10().suppServiceStatus(rr.mSerial, operationType, ImsRadioUtils.facilityTypeToHal(facility), cbNumListInfo);
            }
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void getCOLR(Message result) {
        logv("getCOLR");
        String msgIdString = msgIdToString(33);
        IFRequest rr = IFRequest.obtain(33, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().getColr(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, "GetColr request to IImsRadio: Exception: " + ex);
        }
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void setCOLR(int presentationValue, Message result) {
        logv("setCOLR presentationValue = " + presentationValue);
        String msgIdString = msgIdToString(34);
        IFRequest rr = IFRequest.obtain(34, result);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        vendor.qti.hardware.radio.ims.V1_0.ColrInfo colrValue = new vendor.qti.hardware.radio.ims.V1_0.ColrInfo();
        colrValue.presentation = ImsRadioUtils.ipPresentationToHal(presentationValue);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().setColr(rr.mSerial, colrValue);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(34) + "to IImsRadio: Exception: " + ex);
        }
    }

    static int facilityStringToInt(String sc) {
        if (sc == null) {
            throw new RuntimeException("invalid supplementary service");
        }
        if (sc.equals("CLIP")) {
            return 1;
        }
        if (sc.equals("COLP")) {
            return 2;
        }
        return 0;
    }

    public void getPacketCount(Message response) {
        logv("getPacketCount");
        String msgIdString = msgIdToString(40);
        IFRequest rr = IFRequest.obtain(40, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().getRtpStatistics(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void getPacketErrorCount(Message response) {
        logv("getPacketErrorCount");
        String msgIdString = msgIdToString(41);
        IFRequest rr = IFRequest.obtain(41, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().getRtpErrorStatistics(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void getImsSubConfig(Message response) {
        logv("getImsSubConfig");
        String msgIdString = msgIdToString(54);
        IFRequest rr = IFRequest.obtain(54, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().getImsSubConfig(rr.mSerial);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdString + "request to IImsRadio: Exception: " + ex);
        }
    }

    public void getWifiCallingPreference(Message response) {
        Log.i(this, "getWifiCallingPreference : Not supported");
    }

    public void setWifiCallingPreference(int wifiCallingStatus, int wifiCallingPreference, Message response) {
        Log.i(this, "setWifiCallingPreference : Not supported");
    }

    public void getHandoverConfig(Message response) {
        Log.i(LOG_TAG, "getHandoverConfig : Not supported");
    }

    public void setHandoverConfig(int hoConfig, Message response) {
        Log.i(LOG_TAG, "setHandoverConfig : Not supported");
    }

    public void queryVopsStatus(Message response) {
        Log.i(this, "queryVopsStatus : Not supported");
    }

    public void querySsacStatus(Message response) {
        Log.i(this, "querySsacStatus : Not supported");
    }

    public void updateVoltePref(int preference, Message response) {
        Log.i(this, "updateVoltePref : Not supported");
    }

    public void queryVoltePref(Message response) {
        Log.i(this, "queryVoltePref : Not supported");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void rejectCall(Message result) {
        Log.i(this, "rejectCall : Not supported");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void getLastCallFailCause(Message result) {
        Log.i(this, "getLastCallFailCause : Not supported");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void hangupWaitingOrBackground(Message result) {
        Log.i(this, "hangupWaitingOrBackground : Not supported");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void getCurrentCalls(Message result) {
        Log.i(this, "getCurrentCalls : Not supported");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void switchWaitingOrHoldingAndActive(Message result) {
        Log.i(this, "switchWaitingOrHoldingAndActive : Not supported");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void explicitCallTransfer(Message result) {
        Log.i(this, "explicitCallTransfer : Not supported");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void hangupForegroundResumeBackground(Message result) {
        Log.i(this, "hangupForegroundResumeBackground : Not supported");
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForRttMessage(Handler h, int what, Object obj) {
        this.mRttMessageRegistrants.addUnique(h, what, obj);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void deregisterForRttMessage(Handler h) {
        this.mRttMessageRegistrants.remove(h);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void registerForVoiceInfo(Handler h, int what, Object obj) {
        this.mVoiceInfoStatusRegistrants.addUnique(h, what, obj);
    }

    @Override // org.codeaurora.ims.ImsPhoneCommandsInterface
    public void deregisterForVoiceInfo(Handler h) {
        this.mVoiceInfoStatusRegistrants.remove(h);
    }

    public void sendRttMessage(String message, Message response) {
        Log.i(this, "RTT: sendRttMessage msg = " + message);
        String msgIdString = msgIdToString(55);
        IFRequest rr = IFRequest.obtain(55, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV10().sendRttMessage(rr.mSerial, message);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(55) + "to IImsRadio: Exception: " + ex);
        }
    }

    public void queryVirtualLineInfo(String msisdn, Message response) {
        Log.i(this, "queryVirtualLineInfo = " + msisdn);
        String msgIdString = msgIdToString(61);
        IFRequest rr = IFRequest.obtain(61, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        try {
            logSolicitedRequest(rr);
            imsRadioV14().queryVirtualLineInfo(rr.mSerial, msisdn);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(61) + "to IImsRadio V1.4: Exception: " + ex);
        }
    }

    public void requestMultiIdentityLinesRegistration(Collection<MultiIdentityLineInfo> linesInfo, Message response) {
        Log.i(this, "registerMultiIdentityLines = " + linesInfo);
        String msgIdString = msgIdToString(60);
        IFRequest rr = IFRequest.obtain(60, response);
        if (sendErrorOnImsRadioDown(rr, msgIdString)) {
            return;
        }
        queueRequest(rr);
        ArrayList<MultiIdentityLineInfoHal> halLinesInfo = new ArrayList<>();
        for (MultiIdentityLineInfo line : linesInfo) {
            MultiIdentityLineInfoHal lineInfo = ImsRadioUtilsV14.toMultiIdentityLineInfoHal(line);
            halLinesInfo.add(lineInfo);
        }
        try {
            logSolicitedRequest(rr);
            imsRadioV14().registerMultiIdentityLines(rr.mSerial, halLinesInfo);
        } catch (Exception ex) {
            removeFromQueueAndSendResponse(rr.mSerial);
            Log.e(this, msgIdToString(60) + "to IImsRadio: Exception: " + ex);
        }
    }
}
