package org.codeaurora.ims.parser;

import android.os.Bundle;
import android.os.Parcel;
import android.telephony.ims.ImsConferenceState;
import com.qualcomm.ims.utils.Log;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ConfInfo {
    public static final int INDEX_DISPLAY_TEXT = 1;
    public static final int INDEX_ENDPOINT = 2;
    public static final int INDEX_STATUS = 3;
    public static final int INDEX_USER = 0;
    public static final int MAX_CONF_PARTICIPANT_INFO = 4;
    private static SAXXMLHandler sHandler;
    public Element mNewElement;
    private final int VERSION_INVALID = 0;
    private final int VERSION_EQUAL = 1;
    private final int VERSION_GREATER = 2;
    private final int VERSION_lESSER = 3;
    private final int STATE_FULL = 4;
    private final int STATE_PARTIAL = 5;
    private final int STATE_DELETE = 6;
    public String LOGTAG = "ConfInfo";
    public ArrayList<String> allowedPartial = new ArrayList<>();
    private boolean debug = false;
    public Element mCachedElement = new Element();

    public ConfInfo() {
        setMapAttributeWithDefaultValue();
        this.mNewElement = new Element();
        preparePartialList();
    }

    private void setMapAttributeWithDefaultValue() {
        this.mCachedElement.setMapAttribute();
        this.mCachedElement.getMapAttribute().put("version", "NotApplicable");
    }

    public static void setSAXHandler(SAXXMLHandler handler) {
        sHandler = handler;
    }

    public void updateConfXmlBytes(byte[] confxml) {
        InputStream is = new ByteArrayInputStream(confxml);
        SAXXMLParser.getSAXXMLParser();
        this.mNewElement = SAXXMLParser.parse(is);
        debugLog("*************New Notification*****************");
        updateNotification(null, this.mCachedElement, this.mNewElement, -1);
    }

    protected void debugLog(String str) {
        if (this.debug) {
            Log.d(this.LOGTAG, str);
        }
    }

    protected void log(String str) {
        Log.d(this.LOGTAG, str);
    }

    private void dumpstate() {
        List<Element> list = this.mCachedElement.getSubElementList();
        if (list != null) {
            int length = list.size();
            debugLog(this.mCachedElement.getTagName());
            debugLog("sublist length: " + length);
            for (int index = 0; index < length; index++) {
                debugLog("SubElement list Element at Index" + index + "::" + list.get(index).getTagName());
                if (list.get(index).getSubElementList() != null) {
                    int length2 = list.get(index).getSubElementList().size();
                    debugLog("SubElement List Length:" + length2);
                    for (int index2 = 0; index2 < length2; index2++) {
                        debugLog("SubElement List Length:" + list.get(index).getSubElementList().get(index2).getTagName());
                    }
                } else {
                    debugLog("List two is null");
                }
            }
        } else {
            debugLog("List one is null");
        }
        getUserUriList();
    }

    public String[] getUserUriList() {
        String[] userUri = null;
        if (this.mCachedElement != null) {
            log("mCachedElement reference " + this.mCachedElement);
            String version = this.mCachedElement.getAttributeValue("version");
            debugLog("version string is " + version);
            if (version != null && !version.equalsIgnoreCase("NotApplicable")) {
                Element.clearMatchedElementsList();
                ArrayList<Element> usersList = Element.getMatchedElements(Element.CONF_USER, Element.CONF_USERS, this.mCachedElement);
                int length = usersList.size();
                log("userlist length in getuserUri = " + length);
                userUri = new String[length];
                for (int index = 0; index < length; index++) {
                    userUri[index] = usersList.get(index).getAttributeValue("entity");
                    log("Inside getUser URI list" + userUri[index].toString());
                }
            }
        } else {
            log("conf_version not valid");
        }
        return userUri;
    }

    public ImsConferenceState getConfUriList() {
        if (this.mCachedElement != null) {
            log("mCachedElement reference " + this.mCachedElement);
            String version = this.mCachedElement.getAttributeValue("version");
            debugLog("version string is " + version);
            if (version != null && !version.equalsIgnoreCase("NotApplicable")) {
                Element.clearMatchedElementsList();
                ArrayList<Element> usersList = Element.getMatchedElements(Element.CONF_USER, Element.CONF_USERS, this.mCachedElement);
                int length = usersList.size();
                log("userlist length in getuserUri = " + length);
                Parcel p = Parcel.obtain();
                p.writeInt(length);
                for (int index = 0; index < length; index++) {
                    String[] participantInfo = getParticipantInfoFromElement(usersList.get(index));
                    log("getConfUriList[" + index + "] -> userEntity=" + Log.pii(participantInfo[0]) + ", Display Text=" + Log.pii(participantInfo[1]) + ", endPoint=" + Log.pii(participantInfo[2]) + ", status=" + participantInfo[3]);
                    p.writeString(participantInfo[0]);
                    Bundle b = new Bundle();
                    b.putString(Element.CONF_USER, participantInfo[0]);
                    b.putString(Element.CONF_ENDPOINT, participantInfo[2]);
                    b.putString(Element.CONF_STATUS, participantInfo[3]);
                    p.writeParcelable(b, 0);
                }
                p.setDataPosition(0);
                return (ImsConferenceState) ImsConferenceState.CREATOR.createFromParcel(p);
            }
            return null;
        }
        log("conf_version not valid");
        return null;
    }

    private String[] getParticipantInfoFromElement(Element e) {
        String[] participantInfo = new String[4];
        participantInfo[0] = e.getAttributeValue("entity");
        participantInfo[1] = e.getAttributeValue(Element.CONF_DISPLAY_TEXT);
        List<Element> userSubElements = e.getSubElementList();
        for (int i = 0; i < userSubElements.size(); i++) {
            log("subElement[" + i + "]:: " + userSubElements.get(i).getTagName());
            if (userSubElements.get(i).getTagName().equals(Element.CONF_ENDPOINT)) {
                participantInfo[2] = userSubElements.get(i).getAttributeValue("entity");
                participantInfo[3] = userSubElements.get(i).getAttributeValue(Element.CONF_STATUS);
            }
        }
        return participantInfo;
    }

    private int getElementIndexOnKeyMatch(List<Element> mList, Element source, String key) {
        int length = mList.size();
        String value = source.getAttributeValue(key);
        if (value == null) {
            return -1;
        }
        for (int i = 0; i < length; i++) {
            if (value.equalsIgnoreCase(mList.get(i).getAttributeValue(key))) {
                int ret = i;
                return ret;
            }
        }
        return -1;
    }

    private void updateAttributeList(Element cachedElement, Element newElement) {
        Set<String> newElementTags = new HashSet<>(newElement.getMapAttribute().keySet());
        Object[] mappedKeys = newElementTags.toArray();
        for (Object obj : mappedKeys) {
            String key = obj.toString();
            cachedElement.setAttributValue(key, newElement.getAttributeValue(key));
        }
    }

    private String getKey(Element element) {
        String tag = element.getTagName();
        if (tag.equalsIgnoreCase(Element.CONF_USER) || tag.equalsIgnoreCase(Element.CONF_ENDPOINT) || tag.equalsIgnoreCase(Element.CONF_ENTRY)) {
            return "entity";
        }
        if (tag.equalsIgnoreCase(Element.CONF_MEDIA)) {
            return "id";
        }
        if (tag.equalsIgnoreCase(Element.CONF_SIDEBAR_REF)) {
            return "uri";
        }
        log(" :Is not supported");
        return null;
    }

    private void preparePartialList() {
        this.allowedPartial.add(Element.CONF_INFO);
        this.allowedPartial.add(Element.CONF_USERS);
        this.allowedPartial.add(Element.CONF_USER);
        this.allowedPartial.add(Element.CONF_ENDPOINT);
        this.allowedPartial.add(Element.CONF_SIDEBAR_VALUE);
        this.allowedPartial.add(Element.CONF_SIDEBAR_REF);
    }

    private boolean isPartialAllowed(String tagname) {
        return this.allowedPartial.contains(tagname);
    }

    protected void updateNotification(List<Element> oldList, Element oldElement, Element newElement, int index) {
        log("UpdateNotification args OldList: " + oldList + ", OldElement: " + oldElement + ", NewElement: " + newElement + ", index = " + index);
        debugLog("mCachedElement element: " + this.mCachedElement);
        try {
            String latestState = checkElementState(newElement);
            String newTagName = newElement.getTagName();
            String oldTagName = null;
            if (oldElement != null) {
                oldTagName = oldElement.getTagName();
            }
            debugLog("Old Element Tag name: " + oldTagName);
            debugLog("New Element Tag name: " + newTagName);
            if (latestState.equalsIgnoreCase(Element.STATE_FULL)) {
                if (oldList == null) {
                    log("Root Element is replced with Full state");
                    this.mCachedElement = newElement;
                } else if (oldList != null) {
                    if (index < 0) {
                        debugLog("adding new Element with Full state");
                        oldList.add(newElement);
                    } else {
                        debugLog("replacing Element with Full state with parentTag" + newElement.getParentTag() + " Index :" + index);
                        oldList.set(index, newElement);
                    }
                }
            } else if (latestState.equalsIgnoreCase(Element.STATE_PARTIAL)) {
                log("updateNotification: partial state");
                try {
                    if (isPartialAllowed(newTagName)) {
                        debugLog("Partial Notification state is supported to this element" + newTagName);
                        if (oldElement != null || index >= 0) {
                            List<Element> mNewSubElementList = newElement.getSubElementList();
                            if (oldList != null) {
                                oldElement = oldList.get(index);
                            }
                            List<Element> oldSubElementList = oldElement.getSubElementList();
                            debugLog("Old Element Tag name: " + oldTagName);
                            updateAttributeList(oldElement, newElement);
                            if (oldSubElementList == null) {
                                oldSubElementList = reIndexOldElementList(oldSubElementList, newElement);
                            }
                            handleNotificationOnSubElements(mNewSubElementList, oldSubElementList);
                        } else {
                            debugLog("Partial Notification state, but no old element.");
                            oldList.add(newElement);
                        }
                    }
                } catch (Exception ex) {
                    log("Exception in handlePatialNotification ");
                    ex.printStackTrace();
                }
            } else if (latestState.equalsIgnoreCase(Element.STATE_DELETE)) {
                log("updateNotification Deleting Element");
                if (!newTagName.equals(Element.CONF_INFO)) {
                    oldList.remove(index);
                } else {
                    this.mCachedElement.ClearAll();
                }
            }
        } catch (IndexOutOfBoundsException ex2) {
            log("Indexout of bound exception in UpdateNotification");
            ex2.printStackTrace();
        } catch (NullPointerException ex3) {
            log("Null Pointer Exception in UpdateNotification");
            ex3.printStackTrace();
        }
        log("updateNotification : comming out");
    }

    private void handleNotificationOnSubElements(List<Element> mNewSubElementList, List<Element> mOldSubElementList) {
        int newListLength = mNewSubElementList.size();
        log("updateNotification : HandlePartialNotification");
        for (int elementIndex = 0; elementIndex < newListLength; elementIndex++) {
            Element newSubElement = mNewSubElementList.get(elementIndex);
            debugLog("New List :" + newListLength + "at index : " + elementIndex + "tag name:" + newSubElement.getTagName());
            log("Old Element List :" + mOldSubElementList);
            if (isPartialAllowed(newSubElement.getTagName())) {
                String key = getKey(newSubElement);
                if (key != null) {
                    log("Key is not null");
                    int iIndex = getElementIndexOnKeyMatch(mOldSubElementList, newSubElement, key);
                    debugLog("updateNotification : HandlePartialNotification elementIndex : " + elementIndex);
                    debugLog("Old element index Index value: " + iIndex);
                    updateNotification(mOldSubElementList, null, newSubElement, iIndex);
                } else {
                    log("key is null");
                    int iIndex2 = getElementIndexOnTagMatch(mOldSubElementList, newSubElement.getTagName().toString());
                    if (iIndex2 < 0) {
                        log("element is not found after doing Index on key match");
                        updateNotification(mOldSubElementList, null, newSubElement, iIndex2);
                    } else {
                        Element tempelement = mOldSubElementList.get(iIndex2);
                        List<Element> mOldList = tempelement.getSubElementList();
                        List<Element> mNewList = newSubElement.getSubElementList();
                        if (mNewList != null && mOldList != null) {
                            handleNotificationOnSubElements(mNewList, mOldList);
                        }
                    }
                }
            } else {
                log("Tag not valid for Partial Notification");
                int iIndex3 = getElementIndexOnTagMatch(mOldSubElementList, newSubElement.getTagName().toString());
                debugLog("newSubElement Tag Name: " + newSubElement.getTagName().toString());
                if (iIndex3 >= 0) {
                    Element tempelement2 = mOldSubElementList.get(iIndex3);
                    updateNotification(mOldSubElementList, tempelement2, newSubElement, iIndex3);
                } else {
                    log("May be tags are not at same level checking it by reindexing one level up");
                    if (mOldSubElementList != null) {
                        reIndexOldElementList(mOldSubElementList, newSubElement);
                        Element tempelement3 = mOldSubElementList.get(iIndex3);
                        updateNotification(mOldSubElementList, tempelement3, newSubElement, iIndex3);
                    }
                }
            }
        }
    }

    private List<Element> reIndexOldElementList(List<Element> mOldSubElementList, Element newSubElement) {
        if (mOldSubElementList != null) {
            for (int index = 0; index < mOldSubElementList.size(); index++) {
                Element temp = mOldSubElementList.get(index);
                if (temp.getTagName().equalsIgnoreCase(newSubElement.getTagName())) {
                    debugLog("Old Element List item at Index[" + index + "]" + temp.getTagName());
                    mOldSubElementList = temp.getSubElementList();
                }
            }
        } else {
            debugLog("old element list is null");
        }
        return mOldSubElementList;
    }

    private int getElementIndexOnTagMatch(List<Element> aList, String tagName) {
        if (aList == null) {
            return -1;
        }
        int length = aList.size();
        for (int i = 0; i < length; i++) {
            if (aList.get(i).getTagName().equalsIgnoreCase(tagName)) {
                int iIndex = i;
                return iIndex;
            }
        }
        return -1;
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

    public int validateConfInfoVersion(Element newmessage) {
        int oldversion = Integer.parseInt(this.mCachedElement.getAttributeValue("version"));
        if (newmessage.getMapAttribute().containsKey("version")) {
            int newversion = Integer.parseInt(newmessage.getAttributeValue("version"));
            if (oldversion == newversion) {
                Objects.requireNonNull(this);
                return 1;
            }
            if (oldversion > newversion) {
                Objects.requireNonNull(this);
                return 2;
            }
            if (oldversion < newversion) {
                Objects.requireNonNull(this);
                return 3;
            }
        }
        Objects.requireNonNull(this);
        return 0;
    }

    public void UpdateLocalVersionNumber(Map<String, String> parent, String value) {
        if (parent.containsKey("version") && !parent.get("version").equalsIgnoreCase(value)) {
            parent.remove("version");
        }
        parent.put("version", value);
    }

    public String getState(Map<String, String> parent) {
        if (parent.containsKey("state")) {
            return parent.get("state");
        }
        return null;
    }

    public void UpdateConfState(Map<String, String> parent, String value) {
        parent.put("state", value);
    }

    public void UpdateConfState(String key, String value) {
        this.mCachedElement.setAttributValue(key, value);
    }

    public void UpdateConfInfoAllElement(Element element) {
        this.mCachedElement = element;
    }

    private void UpdateConfInfoElement(String key, String value) {
        sHandler.getConferenceInfoLatestMessage().setAttributValue(key, value);
    }

    private void UpdateConfDesElement(String key, String value) {
        sHandler.getConferenceDescElement().setAttributValue(key, value);
    }

    private boolean compareElements(Element lhs) {
        if (lhs.getTagName().equalsIgnoreCase(this.mCachedElement.getTagName()) && this.mCachedElement.getParentTag().equalsIgnoreCase(this.mCachedElement.getParentTag()) && lhs.getMapAttribute() != null && this.mCachedElement.getMapAttribute() != null && lhs.getMapAttribute().equals(this.mCachedElement.getMapAttribute())) {
            return true;
        }
        return false;
    }

    public void dispose() {
        Element element = this.mCachedElement;
        if (element != null) {
            element.ClearAll();
        }
        Element.clearMatchedElementsList();
        Element element2 = this.mNewElement;
        if (element2 != null) {
            element2.ClearAll();
        }
    }

    public void clearAndSetDefault() {
        dispose();
        setMapAttributeWithDefaultValue();
    }

    private int getConfStateValue() {
        return 1;
    }
}
