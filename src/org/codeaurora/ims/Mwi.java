package org.codeaurora.ims;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Mwi {
    public static final int MWI_MSG_FAX = 2;
    public static final int MWI_MSG_MULTIMEDIA = 4;
    public static final int MWI_MSG_NONE = -1;
    public static final int MWI_MSG_PAGER = 3;
    public static final int MWI_MSG_PRIORITY_LOW = 0;
    public static final int MWI_MSG_PRIORITY_NORMAL = 1;
    public static final int MWI_MSG_PRIORITY_UNKNOWN = -1;
    public static final int MWI_MSG_PRIORITY_URGENT = 2;
    public static final int MWI_MSG_TEXT = 5;
    public static final int MWI_MSG_VIDEO = 1;
    public static final int MWI_MSG_VOICE = 0;
    public String mUeAddress;
    public List<MwiMessageSummary> mwiMsgSummary = new ArrayList();
    public List<MwiMessageDetails> mwiMsgDetails = new ArrayList();

    public static class MwiMessageDetails {
        public String mDate;
        public String mFromAddress;
        public String mMessageId;
        public int mMessageType;
        public int mPriority;
        public String mSubject;
        public String mToAddress;
    }

    public static class MwiMessageSummary {
        public int mMessageType;
        public int mNewMessage;
        public int mNewUrgent;
        public int mOldMessage;
        public int mOldUrgent;
    }

    public String summaryToString(MwiMessageSummary msgSummary) {
        StringBuilder sb = new StringBuilder("{");
        sb.append(" Msg Type = " + msgSummary.mMessageType + " , New Msg = " + msgSummary.mNewMessage + " , Old Msg = " + msgSummary.mOldMessage + " , New Urgent = " + msgSummary.mNewUrgent + " , Old Urgent = " + msgSummary.mOldUrgent);
        sb.append("}");
        return sb.toString();
    }

    public String detailsToString(MwiMessageDetails msgDetails) {
        StringBuilder sb = new StringBuilder("{");
        if (msgDetails.mToAddress != null) {
            sb.append("To Address = " + msgDetails.mToAddress);
        }
        if (msgDetails.mFromAddress != null) {
            sb.append(", From Address = " + msgDetails.mFromAddress);
        }
        if (msgDetails.mSubject != null) {
            sb.append(", Subject = " + msgDetails.mSubject);
        }
        if (msgDetails.mDate != null) {
            sb.append(", Date = " + msgDetails.mDate);
        }
        sb.append(", Priority = " + msgDetails.mPriority);
        if (msgDetails.mMessageId != null) {
            sb.append(", Message Id = " + msgDetails.mMessageId);
        }
        sb.append(", Message Type = " + msgDetails.mMessageType);
        sb.append("}");
        return sb.toString();
    }
}
