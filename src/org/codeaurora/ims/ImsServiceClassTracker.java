package org.codeaurora.ims;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSuppServiceNotification;
import android.telephony.ims.stub.ImsCallSessionImplBase;
import android.util.SparseIntArray;
import com.qualcomm.ims.utils.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.codeaurora.ims.utils.QtiImsExtUtils;
import org.codeaurora.telephony.utils.AsyncResult;

/* JADX INFO: loaded from: classes.dex */
public class ImsServiceClassTracker {
    public static final String CONF_URI_DC_NUMBER = "Conference Call";
    private static final int EVENT_CONFERENCE = 1;
    private static final int INVALID_CALL_ID = -1;
    private static int sToken = -1;
    private ImsSenderRxr mCi;
    private ConferenceResult mConferenceResult;
    private Context mContext;
    private Handler mHandler;
    private ImsCallSessionCallbackHandler mMergeHostListener;
    private ImsServiceSub mServiceSub;
    private boolean mIsVideoSupported = false;
    private boolean mIsVoiceSupported = false;
    private QImsSessionBase.ListenerBase mCallListener = new QImsSessionBase.ListenerBase() { // from class: org.codeaurora.ims.ImsServiceClassTracker.1
        @Override // org.codeaurora.ims.QImsSessionBase.ListenerBase, org.codeaurora.ims.QImsSessionBase.Listener
        public void onClosed(QImsSessionBase s) {
            ImsServiceClassTracker.this.onCallClosed(s);
        }
    };
    private QImsSessionBase.ListenerBase mUssdListener = new QImsSessionBase.ListenerBase() { // from class: org.codeaurora.ims.ImsServiceClassTracker.2
        @Override // org.codeaurora.ims.QImsSessionBase.ListenerBase, org.codeaurora.ims.QImsSessionBase.Listener
        public void onClosed(QImsSessionBase s) {
            ImsServiceClassTracker.this.onUssdClosed(s);
        }
    };
    private final Map<String, ImsCallSessionImpl> mCallList = new HashMap();
    private final ArrayList<ImsCallSessionImpl> mPendingSessionList = new ArrayList<>();
    private ArrayList<ImsUssdSessionImpl> mUssdList = new ArrayList<>();
    private List<ICallListListener> mCallListListeners = new CopyOnWriteArrayList();
    private SparseIntArray mCallComposerTokenList = new SparseIntArray();

    public ImsServiceClassTracker(ImsSenderRxr ci, Context context, ImsServiceSub serviceSub) {
        this.mCi = null;
        this.mCi = ci;
        this.mContext = context;
        this.mServiceSub = serviceSub;
    }

    public int getCallCount() {
        return this.mCallList.size();
    }

    private Bundle createIncomingCallBundle(boolean isUssd, boolean isUnknown) {
        Bundle extras = new Bundle();
        extras.putBoolean("android.telephony.ims.feature.extra.IS_USSD", isUssd);
        extras.putBoolean("android.telephony.ims.feature.extra.IS_UNKNOWN_CALL", isUnknown);
        return extras;
    }

    public void updateFeatureCapabilities(boolean isVideoSupported, boolean isVoiceSupported) {
        Log.i(this, "updateFeatureCapabilities video " + isVideoSupported + " voice " + isVoiceSupported);
        this.mIsVideoSupported = isVideoSupported;
        this.mIsVoiceSupported = isVoiceSupported;
        synchronized (this.mCallList) {
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                ImsCallSessionImpl session = e.getValue();
                session.updateFeatureCapabilities(isVideoSupported, isVoiceSupported);
            }
        }
    }

    public void updateLowBatteryStatus() {
        synchronized (this.mCallList) {
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                ImsCallSessionImpl session = e.getValue();
                session.updateLowBatteryStatus();
            }
        }
    }

    private class ConferenceResult {
        ImsCallSessionImpl activeCall;
        ImsCallSessionImpl confHostCall;
        ImsCallSessionImpl heldCall;
        boolean shouldHaveTransientSession = true;

        ConferenceResult() {
        }
    }

    private void cleanupConferenceAttempt() {
        this.mMergeHostListener = null;
        this.mConferenceResult = null;
    }

    public void handleConferenceResult(AsyncResult ar) {
        if (ar != null) {
            if (ar.exception != null) {
                ImsReasonInfo reason = ImsCallUtils.getImsReasonInfo(ar);
                this.mMergeHostListener.callSessionMergeFailed(reason);
                cleanupConferenceAttempt();
                for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                    ImsCallSessionImpl cs = e.getValue();
                    cs.unMuteStateReporting();
                    cs.updateMergeStatus(3);
                    cs.mIsMergeHostSession = false;
                }
                return;
            }
            processConferenceResult();
        }
    }

    public void processConferenceResult() {
        Log.i(this, "Conference response received. Processing final result.");
        boolean foundHeldCall = false;
        for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
            ImsCallSessionImpl cs = e.getValue();
            if (cs.getDcState() == DriverCallIms.State.ACTIVE) {
                if (this.mConferenceResult.shouldHaveTransientSession && this.mConferenceResult.activeCall != null) {
                    cs.setConfInfo(this.mConferenceResult.activeCall.getConfInfo());
                }
                this.mConferenceResult.activeCall = cs;
                if (this.mConferenceResult.shouldHaveTransientSession) {
                    cs.mIsConferenceHostSession = true;
                    Log.v(this, "Setting mIsConferenceHostSession to true");
                }
            } else if (cs.getDcState() == DriverCallIms.State.HOLDING) {
                foundHeldCall = true;
            }
        }
        if (!foundHeldCall) {
            this.mConferenceResult.heldCall = null;
        }
        if (!this.mConferenceResult.shouldHaveTransientSession) {
            this.mMergeHostListener.callSessionMergeComplete(null);
        } else {
            this.mMergeHostListener.callSessionMergeComplete(this.mConferenceResult.activeCall);
            if (this.mConferenceResult.confHostCall != null && !this.mConferenceResult.confHostCall.isImsCallSessionAlive()) {
                Log.i(this, "processConferenceResult: close conf host session");
                this.mConferenceResult.confHostCall.close();
            }
        }
        for (Map.Entry<String, ImsCallSessionImpl> e2 : this.mCallList.entrySet()) {
            ImsCallSessionImpl cs2 = e2.getValue();
            cs2.unMuteStateReporting();
            cs2.updateMergeStatus(2);
            cs2.mIsMergeHostSession = false;
        }
        cleanupConferenceAttempt();
    }

    public void sendConferenceRequest(ImsCallSessionImpl hostSession) {
        Log.i(this, "Conference request being requested by session = " + hostSession);
        ConferenceResult conferenceResult = new ConferenceResult();
        this.mConferenceResult = conferenceResult;
        conferenceResult.confHostCall = hostSession;
        hostSession.mIsMergeHostSession = true;
        this.mMergeHostListener = hostSession.mCallbackHandler;
        for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
            ImsCallSessionImpl cs = e.getValue();
            cs.muteStateReporting();
            if (cs.getDcState() == DriverCallIms.State.ACTIVE) {
                this.mConferenceResult.activeCall = cs;
            } else if (cs.getDcState() == DriverCallIms.State.HOLDING) {
                this.mConferenceResult.heldCall = cs;
            }
            if (cs.mIsConferenceHostSession) {
                Log.v(this, "Setting should have transient session to false ");
                this.mConferenceResult.shouldHaveTransientSession = false;
            }
            cs.updateMergeStatus(1);
        }
        this.mServiceSub.sendConferenceRequest(this);
    }

    public Object getCallsListToClear() {
        if (this.mCallList.size() <= 0) {
            return null;
        }
        ArrayList<DriverCallIms> response = new ArrayList<>();
        synchronized (this.mCallList) {
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                ImsCallSessionImpl callSession = e.getValue();
                DriverCallIms dc = new DriverCallIms();
                dc.state = DriverCallIms.State.END;
                dc.index = Integer.parseInt(callSession.getCallId());
                dc.callDetails = new CallDetails();
                dc.callDetails.call_type = callSession.getInternalCallType();
                dc.callDetails.call_domain = callSession.getCallDomain();
                dc.callFailCause = new ImsReasonInfo(131, 0, null);
                response.add(dc);
            }
        }
        Collections.sort(response);
        return response;
    }

    // 由 JADX 反編譯修正：JADX 無法還原此方法，依 smali（ImsServiceClassTracker.smali handleCalls）手寫還原
    public void handleCalls(ArrayList<DriverCallIms> dcList) {
        ImsCallSessionImpl callSession;
        int token;
        Map<String, DriverCallIms> dcMap = new HashMap<>();
        boolean disableVideo = shallDisableVideo(dcList);
        for (int i = 0; dcList != null && i < dcList.size(); i++) {
            DriverCallIms dc = maybeDisableVideo(dcList.get(i), disableVideo);
            ArrayList<ImsCallSessionImpl> arrayList = this.mPendingSessionList;
            if (arrayList != null) {
                synchronized (arrayList) {
                    Iterator<ImsCallSessionImpl> it = this.mPendingSessionList.iterator();
                    while (it.hasNext()) {
                        ImsCallSessionImpl s = it.next();
                        if (dc.state == DriverCallIms.State.DIALING && this.mCallList.get(Integer.toString(dc.index)) == null) {
                            Log.i(this, "Found match call session in temp list, s = " + s);
                            Log.i(this, "Index in call list is " + dc.index);
                            addCall(Integer.valueOf(dc.index), s, false);
                            it.remove();
                        }
                    }
                }
            }
            synchronized (this.mCallList) {
                callSession = this.mCallList.get(Integer.toString(dc.index));
            }
            if (callSession != null) {
                callSession.updateCall(dc);
            } else {
                boolean isUnknown = false;
                if (dc.state == DriverCallIms.State.END) {
                    continue;
                }
                ImsCallSessionImpl callSession2 = new ImsCallSessionImpl(dc, this.mCi, this.mContext, this, this.mIsVideoSupported, this.mServiceSub.getPhoneId());
                callSession2.addListener(this.mCallListener);
                callSession2.updateFeatureCapabilities(this.mIsVideoSupported, this.mIsVoiceSupported);
                this.mServiceSub.onNewCall();
                if (dc.isMT) {
                    Log.i(this, "MT Call creating a new call session");
                    reportIncomingCall(callSession2, dc.index, false);
                } else if (dc.isMpty) {
                    Log.i(this, "Conference Call creating a new call session");
                    isUnknown = true;
                    synchronized (this.mCallList) {
                        Iterator<Map.Entry<String, ImsCallSessionImpl>> it2 = this.mCallList.entrySet().iterator();
                        while (it2.hasNext()) {
                            ImsCallSessionImpl oldSession = it2.next().getValue();
                            if (oldSession.mIsMergeHostSession) {
                                Log.i(this, "Set New Session as Id " + callSession2.getCallId());
                                callSession2.setConfInfo(oldSession.getConfInfo());
                                oldSession.setNewSession(callSession2);
                                oldSession.reportNewConferenceCallSession(callSession2);
                                isUnknown = false;
                                break;
                            }
                            if (oldSession.getMergeStatus() == 1) {
                                isUnknown = false;
                            }
                        }
                    }
                    if (isUnknown) {
                        Log.i(this, "Phantom conference call Scenario");
                    } else {
                        addCall(Integer.valueOf(dc.index), callSession2, true);
                    }
                    callSession2.updateConfSession(dc);
                } else if (dc.state != DriverCallIms.State.END) {
                    Log.i(this, "MO phantom Call Scenario. State = " + dc.state);
                    isUnknown = true;
                }
                if (isUnknown) {
                    reportIncomingCall(callSession2, dc.index, true);
                }
            }
            if (dc.state != DriverCallIms.State.END) {
                dcMap.put(Integer.toString(dc.index), dc);
            }
            if (dc.state == DriverCallIms.State.END && (token = this.mCallComposerTokenList.get(dc.index, -1)) != -1) {
                broadcastCallComposerIntent(token);
                this.mCallComposerTokenList.delete(dc.index);
            }
        }
        synchronized (this.mCallList) {
            Iterator<Map.Entry<String, ImsCallSessionImpl>> it3 = this.mCallList.entrySet().iterator();
            while (it3.hasNext()) {
                Map.Entry<String, ImsCallSessionImpl> e = it3.next();
                if (dcMap.get(e.getKey()) == null) {
                    Log.i(this, "handleCalls: removing dropped/ended call id:" + e.getKey());
                    it3.remove();
                    notifyCallRemoved(e.getValue());
                }
            }
        }
    }

    public void onUssdMessageReceived(UssdInfo info) {
        boolean isErrorCase = info.getMessage().isEmpty();
        if (!this.mUssdList.isEmpty() && !isErrorCase) {
            Log.d(this, "onUssdMessageReceived: ongoing USSD session");
        } else if (isErrorCase) {
            Log.d(this, "onUssdMessageReceived: received empty message");
        } else {
            ImsCallProfile profile = new ImsCallProfile(1, 2);
            this.mServiceSub.notifyIncomingCall(createCallSession(profile, info), createIncomingCallBundle(true, false));
        }
    }

    private boolean shallDisableVideo(ArrayList<DriverCallIms> dcList) {
        if (QtiImsExtUtils.canHoldVideoCall(getPhoneId(), this.mContext)) {
            return false;
        }
        boolean hasActiveVoiceCall = false;
        boolean hasHeldVoiceCall = false;
        boolean isMergeStarted = false;
        Iterator<DriverCallIms> it = dcList.iterator();
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                break;
            }
            DriverCallIms dc = it.next();
            boolean hasActiveVoiceCall2 = (dc.state == DriverCallIms.State.ACTIVE && ImsCallUtils.isVoiceCall(dc.callDetails.call_type)) | hasActiveVoiceCall;
            boolean hasHeldVoiceCall2 = hasHeldVoiceCall | (dc.state == DriverCallIms.State.HOLDING && ImsCallUtils.isVoiceCall(dc.callDetails.call_type));
            synchronized (this.mCallList) {
                ImsCallSessionImpl callSession = this.mCallList.get(Integer.toString(dc.index));
                if (callSession == null || callSession.getMergeStatus() != 1) {
                    z = false;
                }
                isMergeStarted |= z;
            }
            hasActiveVoiceCall = hasActiveVoiceCall2;
            hasHeldVoiceCall = hasHeldVoiceCall2;
        }
        return (hasActiveVoiceCall && hasHeldVoiceCall) || isMergeStarted;
    }

    private DriverCallIms maybeDisableVideo(DriverCallIms dc, boolean disableVideo) {
        if (!disableVideo || dc == null || dc.callDetails == null || dc.callDetails.localAbility == null) {
            return dc;
        }
        boolean isVideoDisabled = !ImsCallSessionImpl.isServiceAllowed(3, dc.callDetails.localAbility);
        if (isVideoDisabled == disableVideo) {
            return dc;
        }
        for (ServiceStatus srv : dc.callDetails.localAbility) {
            if (srv.type == 3) {
                srv.status = 0;
            }
        }
        return dc;
    }

    public void onCallClosed(QImsSessionBase session) {
        ImsCallSessionImpl callSession = (ImsCallSessionImpl) session;
        if (callSession.getState() != 8) {
            Log.i(this, "onCallClosed: call session not in terminated state. Ignore.");
            return;
        }
        synchronized (this.mPendingSessionList) {
            if (this.mPendingSessionList.remove(callSession)) {
                Log.i(this, "Removing pending session on close " + callSession);
                notifyCallRemoved(callSession);
            }
        }
    }

    private void removeAndNotifyCallSessions(Collection<ImsCallSessionImpl> list, String listName) {
        if (list == null) {
            return;
        }
        Iterator<ImsCallSessionImpl> it = list.iterator();
        while (it.hasNext()) {
            ImsCallSessionImpl session = it.next();
            it.remove();
            Log.i(this, "Removing session: " + session + " from " + listName);
            notifyCallRemoved(session);
        }
    }

    private void removeCallSessionsAfterSrvcc() {
        Log.i(this, "removeCallSessionsAfterSrvcc");
        synchronized (this.mCallList) {
            removeAndNotifyCallSessions(this.mCallList.values(), "call list");
        }
        synchronized (this.mPendingSessionList) {
            removeAndNotifyCallSessions(this.mPendingSessionList, "pending list");
        }
    }

    public void onUssdClosed(QImsSessionBase session) {
        Log.i(this, "onUssdClosed for session " + session);
        ArrayList<ImsUssdSessionImpl> arrayList = this.mUssdList;
        if (arrayList != null) {
            synchronized (arrayList) {
                if (this.mUssdList.remove((ImsUssdSessionImpl) session)) {
                    Log.i(this, "Removing session on close " + session);
                    notifyUssdRemoved((ImsUssdSessionImpl) session);
                }
            }
        }
    }

    public void handleModifyCallRequest(CallModify cm) {
        ImsCallSessionImpl callSession;
        if (cm != null) {
            synchronized (this.mCallList) {
                callSession = this.mCallList.get(Integer.toString(cm.call_index));
            }
            if (callSession != null) {
                callSession.onReceivedModifyCall(cm);
                return;
            } else {
                Log.e(this, "handleModifyCallRequest Error: callSession is null");
                return;
            }
        }
        Log.e(this, "handleModifyCallRequest Error: Null Call Modify request ");
    }

    public ImsCallSessionImplBase createCallSession(ImsCallProfile profile, UssdInfo info) {
        if (profile.getCallExtraInt("dialstring") == 2 || info != null) {
            QImsSessionBase session = createUssdSession(profile, info);
            return session;
        }
        QImsSessionBase session2 = createCallSession(profile);
        return session2;
    }

    private QImsSessionBase createCallSession(ImsCallProfile profile) {
        ImsCallSessionImpl session = new ImsCallSessionImpl(profile, this.mCi, this.mContext, this, this.mIsVideoSupported, this.mServiceSub.getPhoneId());
        session.addListener(this.mCallListener);
        session.updateFeatureCapabilities(this.mIsVideoSupported, this.mIsVoiceSupported);
        this.mServiceSub.onNewCall();
        synchronized (this.mPendingSessionList) {
            this.mPendingSessionList.add(session);
            notifyCallAdded(session);
        }
        return session;
    }

    private QImsSessionBase createUssdSession(ImsCallProfile profile, UssdInfo info) {
        ImsUssdSessionImpl session = new ImsUssdSessionImpl(profile, this.mContext, this.mCi, this.mServiceSub.getPhoneId(), this, info);
        session.addListener(this.mUssdListener);
        session.updateFeatureCapabilities(this.mIsVideoSupported, this.mIsVoiceSupported);
        this.mServiceSub.onNewCall();
        synchronized (this.mUssdList) {
            this.mUssdList.add(session);
            notifyUssdAdded(session);
        }
        return session;
    }

    public ImsCallSessionImpl getCallSession(String callId) {
        ImsCallSessionImpl session;
        synchronized (this.mCallList) {
            session = this.mCallList.get(callId);
        }
        return session;
    }

    public void handleSuppSvcUnsol(SuppNotifyInfo info) {
        String[] callHistory;
        Log.i(this, "handleSuppSvcUnsol connId= " + info.getConnId());
        synchronized (this.mCallList) {
            ImsCallSessionImpl callSession = this.mCallList.get(Integer.toString(info.getConnId()));
            if (callSession != null) {
                boolean startOnHoldLocalTone = false;
                String forwardedCallHistory = info.getHistoryInfo();
                if (forwardedCallHistory != null && !forwardedCallHistory.isEmpty()) {
                    String[] callHistory2 = forwardedCallHistory.split("\r\n");
                    callHistory = callHistory2;
                } else {
                    callHistory = null;
                }
                ImsSuppServiceNotification suppServiceInfo = new ImsSuppServiceNotification(info.getNotificationType(), info.getCode(), info.getIndex(), info.getNotificationType(), info.getNumber(), callHistory);
                if (info.hasHoldTone()) {
                    startOnHoldLocalTone = info.getHoldTone();
                }
                Log.i(this, "handleSuppSvcUnsol suppNotification= " + suppServiceInfo + " startOnHoldLocalTone = " + startOnHoldLocalTone);
                callSession.updateSuppServiceInfo(suppServiceInfo, startOnHoldLocalTone);
            } else {
                Log.e(this, "No call session found for number: ");
            }
        }
    }

    public void handleRefreshConfInfo(ConfInfo confRefreshInfo) {
        byte[] confInfoBytes = null;
        int state = -1;
        ImsCallSessionImpl callSession = null;
        if (confRefreshInfo != null && (confInfoBytes = confRefreshInfo.getConfInfoUri()) != null) {
            if (confInfoBytes.length >= 1) {
                int confCallState = confRefreshInfo.getConfCallState();
                state = confCallState != -1 ? confCallState : 1;
                callSession = getCallSessionWithMptyBitSet(state);
            }
        }
        Log.i(this, "handleRefreshConfInfo: confCallState = " + state + ", callSession = " + callSession);
        if (confInfoBytes != null && callSession != null) {
            Log.i(this, "Update UI for Conference");
            callSession.notifyConfInfo(confInfoBytes);
        } else {
            Log.e(this, "No CallSession with Multiparty bit set is found. Some Error!!!");
        }
    }

    public void handleTtyModeChangeUnsol(int mode) {
        ImsCallSessionImpl callSession = null;
        synchronized (this.mCallList) {
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                if (e.getValue().getState() == 4) {
                    callSession = e.getValue();
                    callSession.notifyTtyModeChange(mode);
                    break;
                }
            }
        }
        if (callSession == null) {
            Log.e(this, "No Active call session found for TTY mode change");
        }
    }

    private ImsCallSessionImpl getCallSessionWithMptyBitSet(int state) {
        synchronized (this.mCallList) {
            ConferenceResult conferenceResult = this.mConferenceResult;
            if (conferenceResult != null && conferenceResult.shouldHaveTransientSession) {
                Log.i(this, "getCallSessionWithMptyBitSet session = " + this.mConferenceResult.activeCall);
                return this.mConferenceResult.activeCall;
            }
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                ImsCallSessionImpl session = e.getValue();
                DriverCallIms.State dcState = session.getDriverCallState();
                Log.i(this, "getCallSessionWithMptyBitSet:: ImsCallSession state = " + session.getState() + ", isMultiparty = " + session.isMultiparty());
                if (session.isMultiparty()) {
                    Log.i(this, "ImsCallSession found with Multiparty bit set");
                    if ((dcState == DriverCallIms.State.DIALING || dcState == DriverCallIms.State.ALERTING || dcState == DriverCallIms.State.ACTIVE) && state == 1) {
                        Log.i(this, "Foreground Conference callSession found");
                        return session;
                    }
                    if (dcState == DriverCallIms.State.HOLDING && state == 2) {
                        Log.i(this, "Background Conference callSession found");
                        return session;
                    }
                    if ((dcState == DriverCallIms.State.INCOMING || dcState == DriverCallIms.State.WAITING) && state == 0) {
                        Log.i(this, "Ringing Conference callSession found");
                        return session;
                    }
                }
            }
            for (Map.Entry<String, ImsCallSessionImpl> e2 : this.mCallList.entrySet()) {
                ImsCallSessionImpl session2 = e2.getValue();
                if (session2.isConfInProgress()) {
                    if (state == 1) {
                        Log.i(this, "Foreground ImsCallSession found during Conference setup");
                        return session2;
                    }
                } else if (state == 2) {
                    Log.i(this, "Background ImsCallSession found");
                }
            }
            return null;
        }
    }

    public List<ImsCallSessionImpl> getCallSessionByState(DriverCallIms.State state) {
        List<ImsCallSessionImpl> sessionList = new ArrayList<>();
        if (state == null) {
            return sessionList;
        }
        synchronized (this.mPendingSessionList) {
            for (ImsCallSessionImpl session : this.mPendingSessionList) {
                if (session.getInternalState() == state) {
                    sessionList.add(session);
                }
            }
        }
        synchronized (this.mCallList) {
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                ImsCallSessionImpl session2 = e.getValue();
                if (session2.getInternalState() == state) {
                    sessionList.add(session2);
                }
            }
        }
        return sessionList;
    }

    public ImsCallSessionImpl findSessionByMediaId(int mediaId) {
        synchronized (this.mCallList) {
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                ImsCallSessionImpl session = e.getValue();
                if (session.getMediaId() == mediaId) {
                    return session;
                }
            }
            return null;
        }
    }

    private static boolean isSrvcc(HoInfo hoInfo) {
        return hoInfo.getSrcTech() == Integer.MAX_VALUE && hoInfo.getTargetTech() == Integer.MAX_VALUE;
    }

    public void handleHandover(HoInfo handover) {
        Log.i(this, "in handleHandover");
        if (isSrvcc(handover)) {
            if (handover.getType() == 1) {
                removeCallSessionsAfterSrvcc();
                return;
            }
            return;
        }
        Log.i(this, "hoState: " + handover.getType() + " srcTech: " + handover.getSrcTech() + " tarTech: " + handover.getTargetTech() + " extraType: " + handover.getExtraType() + " extraInfo: " + handover.getExtraInfo() + " ErrorCode: " + handover.getErrorCode() + " errorMessage: " + handover.getErrorMessage());
        boolean showHandoverToast = false;
        synchronized (this.mCallList) {
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                ImsCallSessionImpl callSession = e.getValue();
                if (callSession == null) {
                    Log.i(this, "handleHandover: null callsession. Key = " + e.getKey());
                } else {
                    callSession.handleHandover(handover.getType(), handover.getSrcTech(), handover.getTargetTech(), handover.getExtraType(), handover.getExtraInfo(), handover.getErrorCode(), handover.getErrorMessage());
                    if (!showHandoverToast && callSession.isInCall()) {
                        showHandoverToast = true;
                    }
                }
            }
        }
        if (showHandoverToast && handover.getType() == 1) {
            if (handover.getSrcTech() == 18 || handover.getSrcTech() == 19) {
                if (handover.getTargetTech() == 14 || handover.getTargetTech() == 20) {
                    Log.i(this, "Switching to LTE network for better quality");
                }
            }
        }
    }

    void addListener(ICallListListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("addListener error: listener is null.");
        }
        if (!this.mCallListListeners.contains(listener)) {
            this.mCallListListeners.add(listener);
        } else {
            Log.i(this, "addListener: Listener already added, " + listener);
        }
    }

    void removeListener(ICallListListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("removeListener error: listener is null.");
        }
        if (this.mCallListListeners.contains(listener)) {
            this.mCallListListeners.remove(listener);
        } else {
            Log.e(this, "removeListener error: Listener not found, " + listener);
        }
    }

    private void notifyCallAdded(ImsCallSessionImpl session) {
        for (ICallListListener listener : this.mCallListListeners) {
            listener.onSessionAdded(session);
        }
    }

    private void addCall(Integer id, ImsCallSessionImpl session, boolean notify) {
        synchronized (this.mCallList) {
            this.mCallList.put(id.toString(), session);
        }
        if (notify) {
            notifyCallAdded(session);
        }
    }

    private void notifyCallRemoved(ImsCallSessionImpl session) {
        for (ICallListListener listener : this.mCallListListeners) {
            listener.onSessionRemoved(session);
        }
    }

    private void notifyUssdAdded(final ImsUssdSessionImpl session) {
        this.mCallListListeners.forEach(new Consumer() { // from class: org.codeaurora.ims.ImsServiceClassTracker$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ICallListListener) obj).onSessionAdded(session);
            }
        });
    }

    private void notifyUssdRemoved(final ImsUssdSessionImpl session) {
        this.mCallListListeners.forEach(new Consumer() { // from class: org.codeaurora.ims.ImsServiceClassTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ICallListListener) obj).onSessionRemoved(session);
            }
        });
    }

    public void reportIncomingCall(ImsCallSessionImpl session, int index, boolean isUnknown) {
        Log.d(this, "reportIncomingCall :: session=" + session + " index=" + index + " isUnknown=" + isUnknown);
        this.mServiceSub.notifyIncomingCall(session, createIncomingCallBundle(false, isUnknown));
        addCall(Integer.valueOf(index), session, true);
    }

    public void updateVoWiFiCallQuality(int quality) {
        synchronized (this.mCallList) {
            for (Map.Entry<String, ImsCallSessionImpl> e : this.mCallList.entrySet()) {
                ImsCallSessionImpl callSession = e.getValue();
                callSession.updateVoWiFiCallQuality(quality);
            }
        }
    }

    public boolean isVoiceSupportedOverWifi() {
        return this.mServiceSub.isVoiceSupportedOverWifi();
    }

    public boolean isVideoSupportedOverWifi() {
        return this.mServiceSub.isVideoSupportedOverWifi();
    }

    public boolean isUssdSupported() {
        return this.mServiceSub.isUssdSupported();
    }

    public int getPhoneId() {
        return this.mServiceSub.getPhoneId();
    }

    public SparseIntArray getCallComposerTokenList() {
        return this.mCallComposerTokenList;
    }

    private static int getToken() {
        int i = sToken;
        int i2 = i == Integer.MAX_VALUE ? 0 : i + 1;
        sToken = i2;
        return i2;
    }

    public void onCallComposerDataAvailable(int callId, Bundle ccExtra) {
        int token = getToken();
        ccExtra.putInt(QtiCallConstants.EXTRA_CALL_COMPOSER_TOKEN, token);
        this.mCallComposerTokenList.put(callId, token);
        broadcastCallComposerIntent(ccExtra, token);
    }

    private void broadcastCallComposerIntent(int token) {
        broadcastCallComposerIntent(null, token);
    }

    private void broadcastCallComposerIntent(Bundle ccExtra, int token) {
        Intent intent = new Intent("org.codeaurora.intent.action.CALL_COMPOSER_INFO");
        intent.putExtra(QtiCallConstants.EXTRA_CALL_COMPOSER_TOKEN, token);
        if (ccExtra != null) {
            intent.putExtra(QtiCallConstants.EXTRA_CALL_COMPOSER_INFO, ccExtra);
        } else {
            Log.d(this, "broadcastCallComposerIntent for end state");
            intent.putExtra(QtiCallConstants.EXTRA_CALL_COMPOSER_CALL_ENDED, true);
        }
        this.mContext.sendBroadcast(intent, Manifest.permission.RECEIVE_CALLCOMPOSER_INFO);
    }
}
