package org.codeaurora.ims.parser;

import android.content.Context;
import android.net.Uri;
import android.telephony.ims.ImsExternalCallState;
import com.qualcomm.ims.utils.Log;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ImsViceParser extends ConfInfo {
    private static final String CALL_TYPE_VIDEO_HELD = "vtheld";
    private static final String CALL_TYPE_VIDEO_RX = "vtrx";
    private static final String CALL_TYPE_VIDEO_TX = "vttx";
    private static final String CALL_TYPE_VIDEO_TX_RX = "vttxrx";
    private static final String CALL_TYPE_VOICE_ACTIVE = "volteactive";
    private static final String CALL_TYPE_VOICE_HELD = "volteheld";
    private static final String LOCAL_TARGET_PARAM_NAME = "+sip.rendering";
    private static final String LOCAL_TARGET_PARAM_PVAL_NO = "no";
    private static String LOGTAG = "ImsViceParser";
    private static final String MEDIA_DIRECTION_INACTIVE = "inactive";
    private static final String MEDIA_DIRECTION_RECVONLY = "recvonly";
    private static final String MEDIA_DIRECTION_SENDONLY = "sendonly";
    private static final String MEDIA_DIRECTION_SENDRECV = "sendrecv";
    private static final String MEDIA_TYPE_AUDIO = "audio";
    private static final String MEDIA_TYPE_VIDEO = "video";
    private static final String STATE_CONFIRMED = "confirmed";
    private static final String STATE_TERMINATED = "terminated";
    private static ArrayList<Element> mDialogList;
    private static ViceSaxXmlHandler sHandler;
    private Context mContext;
    private List<DialogInfo> mDialogIds;
    private String mViceDialogStr = null;
    private ViceSaxXmlParser mViceparser;

    public static class DialogInfo {
        public String callId;
        public String dialogId;
        public String direction;
        public String exclusive;
        public String localIdentity;
        public List<MediaAttribute> mediaAttributes = new ArrayList();
        public String paramName;
        public String paramVal;
        public String remoteIdentity;
        public String state;
    }

    public static class MediaAttribute {
        public String mediaDirection;
        public String mediaPort;
        public String mediaType;
    }

    public ImsViceParser(Context context) {
        this.mContext = context;
        preparePartialList();
    }

    public static void setSAXHandler(ViceSaxXmlHandler handler) {
        sHandler = handler;
    }

    public void updateViceXmlBytes(byte[] vicexml) {
        InputStream is = new ByteArrayInputStream(vicexml);
        this.mDialogIds = new ArrayList();
        try {
            this.mViceDialogStr = new String(vicexml, "UTF-8");
            Log.v(this, "VICE XML in string :- \n" + this.mViceDialogStr);
            this.mViceparser = ViceSaxXmlParser.getInstance();
            this.mNewElement = ViceSaxXmlParser.parse(is);
            debugLog("*************New Vice Notification*****************");
            updateNotification(null, this.mCachedElement, this.mNewElement, -1);
            this.mDialogIds = getDialogInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String checkElementState(Element element) {
        String resultantState = Element.STATE_FULL;
        String elementState = element.getAttributeValue("state");
        if (elementState != null) {
            resultantState = elementState;
        }
        debugLog("Notification state is" + resultantState + " element state " + elementState);
        return resultantState;
    }

    private void preparePartialList() {
        this.allowedPartial.add("state");
    }

    public List<DialogInfo> getDialogInfo() {
        return this.mViceparser.getDialogInfo();
    }

    public String getViceDialogInfoAsString() {
        return this.mViceDialogStr;
    }

    private static int getCallState(DialogInfo dialog) {
        if (dialog != null) {
            if (dialog.state.equalsIgnoreCase(STATE_CONFIRMED)) {
                return 1;
            }
            dialog.state.equalsIgnoreCase(STATE_TERMINATED);
            return 2;
        }
        android.util.Log.d(LOGTAG, "getCallState Dialog null!");
        return 2;
    }

    private static String getCallType(DialogInfo dialog) {
        String audioState = null;
        String videoState = null;
        boolean portPresent = false;
        if (dialog != null && dialog.state.equalsIgnoreCase(STATE_CONFIRMED)) {
            int size = dialog.mediaAttributes.size();
            for (int i = 0; i < size; i++) {
                MediaAttribute media = dialog.mediaAttributes.get(i);
                if (media != null && media.mediaType != null && media.mediaDirection != null) {
                    if (media.mediaType.equalsIgnoreCase(MEDIA_TYPE_AUDIO)) {
                        if (media.mediaDirection.equalsIgnoreCase(MEDIA_DIRECTION_SENDRECV)) {
                            audioState = CALL_TYPE_VOICE_ACTIVE;
                        } else if (media.mediaDirection.equalsIgnoreCase(MEDIA_DIRECTION_SENDONLY) || media.mediaDirection.equalsIgnoreCase(MEDIA_DIRECTION_RECVONLY) || media.mediaDirection.equalsIgnoreCase(MEDIA_DIRECTION_INACTIVE)) {
                            audioState = CALL_TYPE_VOICE_HELD;
                        }
                    } else if (media.mediaType.equalsIgnoreCase(MEDIA_TYPE_VIDEO)) {
                        if (media.mediaDirection.equalsIgnoreCase(MEDIA_DIRECTION_SENDRECV)) {
                            videoState = CALL_TYPE_VIDEO_TX_RX;
                        } else if (media.mediaDirection.equalsIgnoreCase(MEDIA_DIRECTION_SENDONLY)) {
                            videoState = CALL_TYPE_VIDEO_TX;
                        } else if (media.mediaDirection.equalsIgnoreCase(MEDIA_DIRECTION_RECVONLY)) {
                            videoState = CALL_TYPE_VIDEO_RX;
                        } else if (media.mediaDirection.equalsIgnoreCase(MEDIA_DIRECTION_INACTIVE)) {
                            videoState = CALL_TYPE_VIDEO_HELD;
                        }
                        if (media.mediaType.equalsIgnoreCase(MEDIA_TYPE_VIDEO) && media.mediaPort != null && !media.mediaPort.isEmpty()) {
                            portPresent = true;
                        }
                    }
                } else {
                    android.util.Log.e(LOGTAG, "Media Parameter incorrect!!");
                }
                android.util.Log.d(LOGTAG, "audioState = " + audioState + ", videoState = " + videoState + ", portPresent = " + portPresent);
            }
            if (audioState != null) {
                if (audioState.equalsIgnoreCase(CALL_TYPE_VOICE_ACTIVE)) {
                    if (videoState == null || (videoState != null && portPresent)) {
                        return CALL_TYPE_VOICE_ACTIVE;
                    }
                    if (videoState.equalsIgnoreCase(CALL_TYPE_VIDEO_TX_RX)) {
                        return CALL_TYPE_VIDEO_TX_RX;
                    }
                    if (videoState.equalsIgnoreCase(CALL_TYPE_VIDEO_TX)) {
                        return CALL_TYPE_VIDEO_TX;
                    }
                    if (videoState.equalsIgnoreCase(CALL_TYPE_VIDEO_RX)) {
                        return CALL_TYPE_VIDEO_RX;
                    }
                    if (videoState.equalsIgnoreCase(CALL_TYPE_VIDEO_HELD)) {
                        return CALL_TYPE_VIDEO_HELD;
                    }
                    return null;
                }
                if (audioState.equalsIgnoreCase(CALL_TYPE_VOICE_HELD)) {
                    if (videoState == null || (videoState != null && portPresent)) {
                        return CALL_TYPE_VOICE_HELD;
                    }
                    if (videoState == null || portPresent) {
                        return null;
                    }
                    return CALL_TYPE_VIDEO_HELD;
                }
                return null;
            }
            android.util.Log.e(LOGTAG, "audioState null!!");
            return null;
        }
        return null;
    }

    private static int mapViceCallTypeToImsCallProfileCallType(String callType) {
        if (callType == null) {
            android.util.Log.e(LOGTAG, "mapViceCallToImsCallProfileCallType callType null");
            return 2;
        }
        switch (callType) {
            case "volteactive":
            case "volteheld":
                return 2;
            case "vttxrx":
            case "vtheld":
                return 4;
            case "vttx":
                return 5;
            case "vtrx":
                return 6;
            default:
                android.util.Log.e(LOGTAG, "mapViceCallToImsCallProfileCallType unknown callType = " + callType);
                return 2;
        }
    }

    private static boolean isCallHeld(String callType) {
        if (callType == null) {
            android.util.Log.e(LOGTAG, "mapViceCallToImsCallProfileCallType callType null");
            return false;
        }
        switch (callType) {
            case "volteheld":
            case "vtheld":
                return true;
            default:
                return false;
        }
    }

    private static boolean isCallPullable(DialogInfo dialog, boolean isVideoSupported, boolean isVolteSupported) {
        String callType;
        if (dialog == null || (callType = getCallType(dialog)) == null) {
            return false;
        }
        boolean canVtBePulled = callType.equalsIgnoreCase(CALL_TYPE_VIDEO_TX_RX) || callType.equalsIgnoreCase(CALL_TYPE_VIDEO_TX) || callType.equalsIgnoreCase(CALL_TYPE_VIDEO_RX);
        if (!dialog.exclusive.equalsIgnoreCase("false")) {
            return false;
        }
        if ((!getCallType(dialog).equalsIgnoreCase(CALL_TYPE_VOICE_ACTIVE) || !isVolteSupported) && (!canVtBePulled || !isVideoSupported)) {
            return false;
        }
        if (dialog.paramName == null || !dialog.paramName.equalsIgnoreCase(LOCAL_TARGET_PARAM_NAME) || dialog.paramVal == null || !dialog.paramVal.equalsIgnoreCase(LOCAL_TARGET_PARAM_PVAL_NO)) {
            return true;
        }
        return false;
    }

    public List<ImsExternalCallState> getCallPullInfo(boolean isVideoSupported, boolean isVolteSupported) {
        if (isDialogIdsEmpty()) {
            android.util.Log.d(LOGTAG, "getCallPullInfo mDialogIds null");
            return null;
        }
        List<ImsExternalCallState> extCallStateList = new ArrayList<>();
        for (int i = 0; i < this.mDialogIds.size(); i++) {
            DialogInfo info = this.mDialogIds.get(i);
            String callId = info.dialogId;
            Uri remoteAddress = Uri.parse(info.remoteIdentity);
            Uri localAddress = Uri.parse(info.localIdentity);
            boolean isPullable = isCallPullable(info, isVideoSupported, isVolteSupported);
            int callState = getCallState(info);
            int callType = mapViceCallTypeToImsCallProfileCallType(getCallType(info));
            boolean isHeld = isCallHeld(getCallType(info));
            ImsExternalCallState extCallState = new ImsExternalCallState(callId, remoteAddress, localAddress, isPullable, callState, callType, isHeld);
            extCallStateList.add(extCallState);
        }
        return extCallStateList;
    }

    private boolean isDialogIdsEmpty() {
        List<DialogInfo> list = this.mDialogIds;
        return list == null || list.isEmpty();
    }
}
