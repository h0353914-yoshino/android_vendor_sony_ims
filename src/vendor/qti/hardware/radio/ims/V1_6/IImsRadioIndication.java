package vendor.qti.hardware.radio.ims.V1_6;

import android.hidl.base.V1_0.DebugInfo;
import android.hidl.base.V1_0.IBase;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import vendor.qti.hardware.radio.ims.V1_0.BlockStatus;
import vendor.qti.hardware.radio.ims.V1_0.CallModifyInfo;
import vendor.qti.hardware.radio.ims.V1_0.ConferenceInfo;
import vendor.qti.hardware.radio.ims.V1_0.ImsSubConfigInfo;
import vendor.qti.hardware.radio.ims.V1_0.MessageWaitingIndication;
import vendor.qti.hardware.radio.ims.V1_0.ParticipantStatusInfo;
import vendor.qti.hardware.radio.ims.V1_0.SipErrorInfo;
import vendor.qti.hardware.radio.ims.V1_0.StkCcUnsolSsResult;
import vendor.qti.hardware.radio.ims.V1_0.SuppServiceNotification;
import vendor.qti.hardware.radio.ims.V1_0.TtyInfo;
import vendor.qti.hardware.radio.ims.V1_0.ViceInfo;
import vendor.qti.hardware.radio.ims.V1_2.ImsSmsSendStatusReport;
import vendor.qti.hardware.radio.ims.V1_2.IncomingImsSms;
import vendor.qti.hardware.radio.ims.V1_4.MultiIdentityLineInfoHal;

/* JADX INFO: loaded from: classes.dex */
public interface IImsRadioIndication extends vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication {
    public static final String kInterfaceName = "vendor.qti.hardware.radio.ims@1.6::IImsRadioIndication";

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    IHwBinder asBinder();

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    void onCallComposerInfoAvailable(CallComposerInfo callComposerInfo) throws RemoteException;

    void onCallStateChanged_1_6(ArrayList<CallInfo> arrayList) throws RemoteException;

    void onHandover_1_6(HandoverInfo handoverInfo) throws RemoteException;

    void onIncomingCallAutoRejected_1_6(AutoCallRejectionInfo autoCallRejectionInfo) throws RemoteException;

    void onIncomingCallComposerCallAutoRejected(AutoCallRejectionInfo autoCallRejectionInfo, CallComposerInfo callComposerInfo) throws RemoteException;

    void onRegistrationChanged_1_6(RegistrationInfo registrationInfo) throws RemoteException;

    void onRetrievingGeoLocationDataStatus(int i) throws RemoteException;

    void onServiceStatusChanged_1_6(ArrayList<ServiceStatusInfo> arrayList) throws RemoteException;

    void onUssdReceived(int i, String str, SipErrorInfo sipErrorInfo) throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IImsRadioIndication asInterface(IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof IImsRadioIndication)) {
            return (IImsRadioIndication) iface;
        }
        IImsRadioIndication proxy = new Proxy(binder);
        try {
            for (String descriptor : proxy.interfaceChain()) {
                if (descriptor.equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    static IImsRadioIndication castFrom(IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static IImsRadioIndication getService(String serviceName, boolean retry) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static IImsRadioIndication getService(boolean retry) throws RemoteException {
        return getService("default", retry);
    }

    @Deprecated
    static IImsRadioIndication getService(String serviceName) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName));
    }

    @Deprecated
    static IImsRadioIndication getService() throws RemoteException {
        return getService("default");
    }

    public static final class Proxy implements IImsRadioIndication {
        private IHwBinder mRemote;

        public Proxy(IHwBinder remote) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(remote);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException e) {
                return "[class or subclass of vendor.qti.hardware.radio.ims@1.6::IImsRadioIndication]@Proxy";
            }
        }

        public final boolean equals(Object other) {
            return HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onCallStateChanged(ArrayList<vendor.qti.hardware.radio.ims.V1_0.CallInfo> callList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            vendor.qti.hardware.radio.ims.V1_0.CallInfo.writeVectorToParcel(_hidl_request, callList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRing() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRingbackTone(int tone) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(tone);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRegistrationChanged(vendor.qti.hardware.radio.ims.V1_0.RegistrationInfo registration) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            registration.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onHandover(vendor.qti.hardware.radio.ims.V1_0.HandoverInfo handover) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            handover.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onServiceStatusChanged(ArrayList<vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo> srvStatusList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo.writeVectorToParcel(_hidl_request, srvStatusList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRadioStateChanged(int radioState) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(radioState);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onEnterEmergencyCallBackMode() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onExitEmergencyCallBackMode() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onTtyNotification(TtyInfo ttyInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            ttyInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRefreshConferenceInfo(ConferenceInfo conferenceInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            conferenceInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRefreshViceInfo(ViceInfo viceInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            viceInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onModifyCall(CallModifyInfo callModifyInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            callModifyInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onSuppServiceNotification(SuppServiceNotification suppServiceNotification) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            suppServiceNotification.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(14, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onMessageWaiting(MessageWaitingIndication messageWaitingIndication) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            messageWaitingIndication.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(15, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onGeolocationInfoRequested(double lat, double lon) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeDouble(lat);
            _hidl_request.writeDouble(lon);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(16, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onImsSubConfigChanged(ImsSubConfigInfo config) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            config.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(17, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onParticipantStatusInfo(ParticipantStatusInfo participantStatusInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            participantStatusInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(18, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRegistrationBlockStatus(boolean hasBlockStatusOnWwan, BlockStatus blockStatusOnWwan, boolean hasBlockStatusOnWlan, BlockStatus blockStatusOnWlan) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeBool(hasBlockStatusOnWwan);
            blockStatusOnWwan.writeToParcel(_hidl_request);
            _hidl_request.writeBool(hasBlockStatusOnWlan);
            blockStatusOnWlan.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(19, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onRttMessageReceived(String message) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeString(message);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(20, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onVoWiFiCallQuality(int voWiFiCallQualityInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(voWiFiCallQualityInfo);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(21, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication
        public void onSupplementaryServiceIndication(StkCcUnsolSsResult ss) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
            ss.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(22, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication
        public void onCallStateChanged_1_1(ArrayList<vendor.qti.hardware.radio.ims.V1_1.CallInfo> callList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication.kInterfaceName);
            vendor.qti.hardware.radio.ims.V1_1.CallInfo.writeVectorToParcel(_hidl_request, callList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(23, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication
        public void onCallStateChanged_1_2(ArrayList<vendor.qti.hardware.radio.ims.V1_2.CallInfo> callList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName);
            vendor.qti.hardware.radio.ims.V1_2.CallInfo.writeVectorToParcel(_hidl_request, callList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(24, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication
        public void onImsSmsStatusReport(ImsSmsSendStatusReport smsStatusReport) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName);
            smsStatusReport.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(25, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication
        public void onIncomingImsSms(IncomingImsSms imsSms) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName);
            imsSms.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(26, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication
        public void onVopsChanged(boolean isVopsEnabled) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeBool(isVopsEnabled);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(27, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication
        public void onCallStateChanged_1_3(ArrayList<vendor.qti.hardware.radio.ims.V1_3.CallInfo> callList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication.kInterfaceName);
            vendor.qti.hardware.radio.ims.V1_3.CallInfo.writeVectorToParcel(_hidl_request, callList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(28, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication
        public void onIncomingCallAutoRejected(vendor.qti.hardware.radio.ims.V1_3.AutoCallRejectionInfo autoCallRejectionInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication.kInterfaceName);
            autoCallRejectionInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(29, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication
        public void onVoiceInfoChanged(int voiceInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(voiceInfo);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(30, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication
        public void onMultiIdentityRegistrationStatusChange(ArrayList<MultiIdentityLineInfoHal> info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication.kInterfaceName);
            MultiIdentityLineInfoHal.writeVectorToParcel(_hidl_request, info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(31, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication
        public void onMultiIdentityInfoPending() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(32, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication
        public void onCallStateChanged_1_4(ArrayList<vendor.qti.hardware.radio.ims.V1_4.CallInfo> callList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication.kInterfaceName);
            vendor.qti.hardware.radio.ims.V1_4.CallInfo.writeVectorToParcel(_hidl_request, callList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(33, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication
        public void onCallStateChanged_1_5(ArrayList<vendor.qti.hardware.radio.ims.V1_5.CallInfo> callList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName);
            vendor.qti.hardware.radio.ims.V1_5.CallInfo.writeVectorToParcel(_hidl_request, callList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(34, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication
        public void onIncomingCallAutoRejected_1_5(vendor.qti.hardware.radio.ims.V1_5.AutoCallRejectionInfo autoCallRejectionInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName);
            autoCallRejectionInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(35, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication
        public void onModemSupportsWfcRoamingModeConfiguration(boolean wfcRoamingConfigurationSupport) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeBool(wfcRoamingConfigurationSupport);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(36, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication
        public void onUssdMessageFailed(int type, SipErrorInfo errorDetails) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            errorDetails.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(37, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onCallStateChanged_1_6(ArrayList<CallInfo> callList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            CallInfo.writeVectorToParcel(_hidl_request, callList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(38, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onServiceStatusChanged_1_6(ArrayList<ServiceStatusInfo> srvStatusList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            ServiceStatusInfo.writeVectorToParcel(_hidl_request, srvStatusList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(39, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onRegistrationChanged_1_6(RegistrationInfo regInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            regInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(40, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onHandover_1_6(HandoverInfo handover) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            handover.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(41, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onUssdReceived(int type, String msg, SipErrorInfo errorDetails) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(msg);
            errorDetails.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(42, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onCallComposerInfoAvailable(CallComposerInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(43, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onIncomingCallComposerCallAutoRejected(AutoCallRejectionInfo autoCallRejectionInfo, CallComposerInfo callComposerInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            autoCallRejectionInfo.writeToParcel(_hidl_request);
            callComposerInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(44, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onIncomingCallAutoRejected_1_6(AutoCallRejectionInfo autoCallRejectionInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            autoCallRejectionInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(45, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication
        public void onRetrievingGeoLocationDataStatus(int geoLocationDataStatus) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IImsRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(geoLocationDataStatus);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(46, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256067662, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<String> _hidl_out_descriptors = _hidl_reply.readStringVector();
                return _hidl_out_descriptors;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle fd, ArrayList<String> options) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            _hidl_request.writeNativeHandle(fd);
            _hidl_request.writeStringVector(options);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256131655, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public String interfaceDescriptor() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256136003, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                String _hidl_out_descriptor = _hidl_reply.readString();
                return _hidl_out_descriptor;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256398152, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<byte[]> _hidl_out_hashchain = new ArrayList<>();
                HwBlob _hidl_blob = _hidl_reply.readBuffer(16L);
                int _hidl_vec_size = _hidl_blob.getInt32(8L);
                HwBlob childBlob = _hidl_reply.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
                _hidl_out_hashchain.clear();
                for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                    byte[] _hidl_vec_element = new byte[32];
                    long _hidl_array_offset_1 = _hidl_index_0 * 32;
                    childBlob.copyToInt8Array(_hidl_array_offset_1, _hidl_vec_element, 32);
                    _hidl_out_hashchain.add(_hidl_vec_element);
                }
                _hidl_reply.release();
                return _hidl_out_hashchain;
            } catch (Throwable th) {
                _hidl_reply.release();
                throw th;
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public void setHALInstrumentation() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256462420, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) throws RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public void ping() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256921159, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public DebugInfo getDebugInfo() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257049926, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                DebugInfo _hidl_out_info = new DebugInfo();
                _hidl_out_info.readFromParcel(_hidl_reply);
                return _hidl_out_info;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public void notifySyspropsChanged() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257120595, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends HwBinder implements IImsRadioIndication {
        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IImsRadioIndication.kInterfaceName, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication.kInterfaceName, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication.kInterfaceName, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication.kInterfaceName, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle fd, ArrayList<String> options) {
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return IImsRadioIndication.kInterfaceName;
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{44, 95, 8, -119, 76, 61, -111, 124, 125, 97, -5, 85, -19, -120, -100, -7, -60, -114, -95, 118, 17, -112, -3, -34, 105, 120, -76, -77, -39, -105, 115, 116}, new byte[]{-110, -32, -112, -105, -18, -19, 24, 55, -35, -15, 84, 104, -90, -21, 58, -44, 21, -69, 126, -100, 107, -47, 0, -113, 73, 52, -114, 37, 36, -38, -9, 14}, new byte[]{32, 30, 87, -82, 104, 22, -61, -51, 109, -65, 110, -35, 121, 7, 68, 101, -63, 113, -128, -4, -10, -52, 2, -55, -106, 91, 35, 38, -36, 2, -79, 29}, new byte[]{-108, -29, 30, -96, -69, 11, -28, 43, 18, 26, -24, 84, -108, -93, -64, -104, 114, 34, 41, 125, -60, 91, -95, -67, 39, -14, 56, 125, -55, -124, 32, -22}, new byte[]{80, -11, 88, -62, 115, -17, 127, 120, -18, -21, -59, -17, -97, -28, -40, -95, 24, 116, 101, 5, -79, -1, 93, 116, 124, -44, -75, -46, -28, -125, 119, 32}, new byte[]{-5, 50, -11, 0, 55, -35, 13, -84, 21, -41, 56, -67, 91, 46, 68, 55, 96, -56, -96, -32, 65, -85, -82, -116, 106, -125, -26, -4, -11, 10, 35, -124}, new byte[]{43, 35, 57, -20, -31, -12, -78, 107, -3, 75, -29, 69, -88, 0, 78, -31, -31, -86, -79, -1, -116, -27, 1, -92, -76, 30, 122, -90, -6, -104, 24, -83}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo info = new DebugInfo();
            info.pid = HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        @Override // vendor.qti.hardware.radio.ims.V1_6.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication, vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public IHwInterface queryLocalInterface(String descriptor) {
            if (IImsRadioIndication.kInterfaceName.equals(descriptor)) {
                return this;
            }
            return null;
        }

        public void registerAsService(String serviceName) throws RemoteException {
            registerService(serviceName);
        }

        public String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        public void onTransact(int _hidl_code, HwParcel _hidl_request, HwParcel _hidl_reply, int _hidl_flags) throws RemoteException {
            switch (_hidl_code) {
                case 1:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    ArrayList<vendor.qti.hardware.radio.ims.V1_0.CallInfo> callList = vendor.qti.hardware.radio.ims.V1_0.CallInfo.readVectorFromParcel(_hidl_request);
                    onCallStateChanged(callList);
                    return;
                case 2:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    onRing();
                    return;
                case 3:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    int tone = _hidl_request.readInt32();
                    onRingbackTone(tone);
                    return;
                case 4:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    vendor.qti.hardware.radio.ims.V1_0.RegistrationInfo registration = new vendor.qti.hardware.radio.ims.V1_0.RegistrationInfo();
                    registration.readFromParcel(_hidl_request);
                    onRegistrationChanged(registration);
                    return;
                case 5:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    vendor.qti.hardware.radio.ims.V1_0.HandoverInfo handover = new vendor.qti.hardware.radio.ims.V1_0.HandoverInfo();
                    handover.readFromParcel(_hidl_request);
                    onHandover(handover);
                    return;
                case 6:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    ArrayList<vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo> srvStatusList = vendor.qti.hardware.radio.ims.V1_0.ServiceStatusInfo.readVectorFromParcel(_hidl_request);
                    onServiceStatusChanged(srvStatusList);
                    return;
                case 7:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    int radioState = _hidl_request.readInt32();
                    onRadioStateChanged(radioState);
                    return;
                case 8:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    onEnterEmergencyCallBackMode();
                    return;
                case 9:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    onExitEmergencyCallBackMode();
                    return;
                case 10:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    TtyInfo ttyInfo = new TtyInfo();
                    ttyInfo.readFromParcel(_hidl_request);
                    onTtyNotification(ttyInfo);
                    return;
                case 11:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    ConferenceInfo conferenceInfo = new ConferenceInfo();
                    conferenceInfo.readFromParcel(_hidl_request);
                    onRefreshConferenceInfo(conferenceInfo);
                    return;
                case 12:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    ViceInfo viceInfo = new ViceInfo();
                    viceInfo.readFromParcel(_hidl_request);
                    onRefreshViceInfo(viceInfo);
                    return;
                case 13:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    CallModifyInfo callModifyInfo = new CallModifyInfo();
                    callModifyInfo.readFromParcel(_hidl_request);
                    onModifyCall(callModifyInfo);
                    return;
                case 14:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    SuppServiceNotification suppServiceNotification = new SuppServiceNotification();
                    suppServiceNotification.readFromParcel(_hidl_request);
                    onSuppServiceNotification(suppServiceNotification);
                    return;
                case 15:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    MessageWaitingIndication messageWaitingIndication = new MessageWaitingIndication();
                    messageWaitingIndication.readFromParcel(_hidl_request);
                    onMessageWaiting(messageWaitingIndication);
                    return;
                case 16:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    double lat = _hidl_request.readDouble();
                    double lon = _hidl_request.readDouble();
                    onGeolocationInfoRequested(lat, lon);
                    return;
                case 17:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    ImsSubConfigInfo config = new ImsSubConfigInfo();
                    config.readFromParcel(_hidl_request);
                    onImsSubConfigChanged(config);
                    return;
                case 18:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    ParticipantStatusInfo participantStatusInfo = new ParticipantStatusInfo();
                    participantStatusInfo.readFromParcel(_hidl_request);
                    onParticipantStatusInfo(participantStatusInfo);
                    return;
                case 19:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    boolean hasBlockStatusOnWwan = _hidl_request.readBool();
                    BlockStatus blockStatusOnWwan = new BlockStatus();
                    blockStatusOnWwan.readFromParcel(_hidl_request);
                    boolean hasBlockStatusOnWlan = _hidl_request.readBool();
                    BlockStatus blockStatusOnWlan = new BlockStatus();
                    blockStatusOnWlan.readFromParcel(_hidl_request);
                    onRegistrationBlockStatus(hasBlockStatusOnWwan, blockStatusOnWwan, hasBlockStatusOnWlan, blockStatusOnWlan);
                    return;
                case 20:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    String message = _hidl_request.readString();
                    onRttMessageReceived(message);
                    return;
                case 21:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    int voWiFiCallQualityInfo = _hidl_request.readInt32();
                    onVoWiFiCallQuality(voWiFiCallQualityInfo);
                    return;
                case 22:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_0.IImsRadioIndication.kInterfaceName);
                    StkCcUnsolSsResult ss = new StkCcUnsolSsResult();
                    ss.readFromParcel(_hidl_request);
                    onSupplementaryServiceIndication(ss);
                    return;
                case 23:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_1.IImsRadioIndication.kInterfaceName);
                    ArrayList<vendor.qti.hardware.radio.ims.V1_1.CallInfo> callList2 = vendor.qti.hardware.radio.ims.V1_1.CallInfo.readVectorFromParcel(_hidl_request);
                    onCallStateChanged_1_1(callList2);
                    return;
                case 24:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName);
                    ArrayList<vendor.qti.hardware.radio.ims.V1_2.CallInfo> callList3 = vendor.qti.hardware.radio.ims.V1_2.CallInfo.readVectorFromParcel(_hidl_request);
                    onCallStateChanged_1_2(callList3);
                    return;
                case 25:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName);
                    ImsSmsSendStatusReport smsStatusReport = new ImsSmsSendStatusReport();
                    smsStatusReport.readFromParcel(_hidl_request);
                    onImsSmsStatusReport(smsStatusReport);
                    return;
                case 26:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName);
                    IncomingImsSms imsSms = new IncomingImsSms();
                    imsSms.readFromParcel(_hidl_request);
                    onIncomingImsSms(imsSms);
                    return;
                case 27:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_2.IImsRadioIndication.kInterfaceName);
                    boolean isVopsEnabled = _hidl_request.readBool();
                    onVopsChanged(isVopsEnabled);
                    return;
                case 28:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication.kInterfaceName);
                    ArrayList<vendor.qti.hardware.radio.ims.V1_3.CallInfo> callList4 = vendor.qti.hardware.radio.ims.V1_3.CallInfo.readVectorFromParcel(_hidl_request);
                    onCallStateChanged_1_3(callList4);
                    return;
                case 29:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication.kInterfaceName);
                    vendor.qti.hardware.radio.ims.V1_3.AutoCallRejectionInfo autoCallRejectionInfo = new vendor.qti.hardware.radio.ims.V1_3.AutoCallRejectionInfo();
                    autoCallRejectionInfo.readFromParcel(_hidl_request);
                    onIncomingCallAutoRejected(autoCallRejectionInfo);
                    return;
                case 30:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_3.IImsRadioIndication.kInterfaceName);
                    int voiceInfo = _hidl_request.readInt32();
                    onVoiceInfoChanged(voiceInfo);
                    return;
                case 31:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication.kInterfaceName);
                    onMultiIdentityRegistrationStatusChange(MultiIdentityLineInfoHal.readVectorFromParcel(_hidl_request));
                    return;
                case 32:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication.kInterfaceName);
                    onMultiIdentityInfoPending();
                    return;
                case 33:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_4.IImsRadioIndication.kInterfaceName);
                    ArrayList<vendor.qti.hardware.radio.ims.V1_4.CallInfo> callList5 = vendor.qti.hardware.radio.ims.V1_4.CallInfo.readVectorFromParcel(_hidl_request);
                    onCallStateChanged_1_4(callList5);
                    return;
                case 34:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName);
                    ArrayList<vendor.qti.hardware.radio.ims.V1_5.CallInfo> callList6 = vendor.qti.hardware.radio.ims.V1_5.CallInfo.readVectorFromParcel(_hidl_request);
                    onCallStateChanged_1_5(callList6);
                    return;
                case 35:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName);
                    vendor.qti.hardware.radio.ims.V1_5.AutoCallRejectionInfo autoCallRejectionInfo2 = new vendor.qti.hardware.radio.ims.V1_5.AutoCallRejectionInfo();
                    autoCallRejectionInfo2.readFromParcel(_hidl_request);
                    onIncomingCallAutoRejected_1_5(autoCallRejectionInfo2);
                    return;
                case 36:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName);
                    boolean wfcRoamingConfigurationSupport = _hidl_request.readBool();
                    onModemSupportsWfcRoamingModeConfiguration(wfcRoamingConfigurationSupport);
                    return;
                case 37:
                    _hidl_request.enforceInterface(vendor.qti.hardware.radio.ims.V1_5.IImsRadioIndication.kInterfaceName);
                    int type = _hidl_request.readInt32();
                    SipErrorInfo errorDetails = new SipErrorInfo();
                    errorDetails.readFromParcel(_hidl_request);
                    onUssdMessageFailed(type, errorDetails);
                    return;
                case 38:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    ArrayList<CallInfo> callList7 = CallInfo.readVectorFromParcel(_hidl_request);
                    onCallStateChanged_1_6(callList7);
                    return;
                case 39:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    ArrayList<ServiceStatusInfo> srvStatusList2 = ServiceStatusInfo.readVectorFromParcel(_hidl_request);
                    onServiceStatusChanged_1_6(srvStatusList2);
                    return;
                case 40:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    RegistrationInfo regInfo = new RegistrationInfo();
                    regInfo.readFromParcel(_hidl_request);
                    onRegistrationChanged_1_6(regInfo);
                    return;
                case 41:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    HandoverInfo handover2 = new HandoverInfo();
                    handover2.readFromParcel(_hidl_request);
                    onHandover_1_6(handover2);
                    return;
                case 42:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    int type2 = _hidl_request.readInt32();
                    String msg = _hidl_request.readString();
                    SipErrorInfo errorDetails2 = new SipErrorInfo();
                    errorDetails2.readFromParcel(_hidl_request);
                    onUssdReceived(type2, msg, errorDetails2);
                    return;
                case 43:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    CallComposerInfo info = new CallComposerInfo();
                    info.readFromParcel(_hidl_request);
                    onCallComposerInfoAvailable(info);
                    return;
                case 44:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    AutoCallRejectionInfo autoCallRejectionInfo3 = new AutoCallRejectionInfo();
                    autoCallRejectionInfo3.readFromParcel(_hidl_request);
                    CallComposerInfo callComposerInfo = new CallComposerInfo();
                    callComposerInfo.readFromParcel(_hidl_request);
                    onIncomingCallComposerCallAutoRejected(autoCallRejectionInfo3, callComposerInfo);
                    return;
                case 45:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    AutoCallRejectionInfo autoCallRejectionInfo4 = new AutoCallRejectionInfo();
                    autoCallRejectionInfo4.readFromParcel(_hidl_request);
                    onIncomingCallAutoRejected_1_6(autoCallRejectionInfo4);
                    return;
                case 46:
                    _hidl_request.enforceInterface(IImsRadioIndication.kInterfaceName);
                    int geoLocationDataStatus = _hidl_request.readInt32();
                    onRetrievingGeoLocationDataStatus(geoLocationDataStatus);
                    return;
                case 256067662:
                    _hidl_request.enforceInterface(IBase.kInterfaceName);
                    ArrayList<String> _hidl_out_descriptors = interfaceChain();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeStringVector(_hidl_out_descriptors);
                    _hidl_reply.send();
                    return;
                case 256131655:
                    _hidl_request.enforceInterface(IBase.kInterfaceName);
                    NativeHandle fd = _hidl_request.readNativeHandle();
                    ArrayList<String> options = _hidl_request.readStringVector();
                    debug(fd, options);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 256136003:
                    _hidl_request.enforceInterface(IBase.kInterfaceName);
                    String _hidl_out_descriptor = interfaceDescriptor();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeString(_hidl_out_descriptor);
                    _hidl_reply.send();
                    return;
                case 256398152:
                    _hidl_request.enforceInterface(IBase.kInterfaceName);
                    ArrayList<byte[]> _hidl_out_hashchain = getHashChain();
                    _hidl_reply.writeStatus(0);
                    HwBlob _hidl_blob = new HwBlob(16);
                    int _hidl_vec_size = _hidl_out_hashchain.size();
                    _hidl_blob.putInt32(8L, _hidl_vec_size);
                    _hidl_blob.putBool(12L, false);
                    HwBlob childBlob = new HwBlob(_hidl_vec_size * 32);
                    for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                        long _hidl_array_offset_1 = _hidl_index_0 * 32;
                        byte[] _hidl_array_item_1 = _hidl_out_hashchain.get(_hidl_index_0);
                        if (_hidl_array_item_1 == null || _hidl_array_item_1.length != 32) {
                            throw new IllegalArgumentException("Array element is not of the expected length");
                        }
                        childBlob.putInt8Array(_hidl_array_offset_1, _hidl_array_item_1);
                    }
                    _hidl_blob.putBlob(0L, childBlob);
                    _hidl_reply.writeBuffer(_hidl_blob);
                    _hidl_reply.send();
                    return;
                case 256462420:
                    _hidl_request.enforceInterface(IBase.kInterfaceName);
                    setHALInstrumentation();
                    return;
                case 256660548:
                default:
                    return;
                case 256921159:
                    _hidl_request.enforceInterface(IBase.kInterfaceName);
                    ping();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 257049926:
                    _hidl_request.enforceInterface(IBase.kInterfaceName);
                    DebugInfo _hidl_out_info = getDebugInfo();
                    _hidl_reply.writeStatus(0);
                    _hidl_out_info.writeToParcel(_hidl_reply);
                    _hidl_reply.send();
                    return;
                case 257120595:
                    _hidl_request.enforceInterface(IBase.kInterfaceName);
                    notifySyspropsChanged();
                    return;
            }
        }
    }
}
