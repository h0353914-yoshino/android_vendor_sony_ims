package org.codeaurora.ims.parser;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Element {
    public static final String CONF_ASSOCIATED_AORS = "associated-aors";
    public static final String CONF_AVIL_MEDIA = "available-media";
    public static final String CONF_BY = "by";
    public static final String CONF_CALL_ID = "call-id";
    public static final String CONF_CALL_INFO = "call-info";
    public static final String CONF_DESC = "conference-description";
    public static final String CONF_DISC_INFO = "disconnection-info";
    public static final String CONF_DISC_METHOD = "disconnection-method";
    public static final String CONF_DISPLAY_TEXT = "display-text";
    public static final String CONF_ENDPOINT = "endpoint";
    public static final String CONF_ENTITY = "entity";
    public static final String CONF_ENTRY = "entry";
    public static final String CONF_FREE_TEXT = "free-text";
    public static final String CONF_FROM_TAG = "from-tag";
    public static final String CONF_ID = "id";
    public static final String CONF_INFO = "conference-info";
    public static final String CONF_JOINING_INFO = "joining-info";
    public static final String CONF_JOIN_METHOD = "joining-method";
    public static final String CONF_KEYWORDS = "keywords";
    public static final String CONF_LANGUAGE = "languages";
    public static final String CONF_MAX_COUNT = "maximum-user-count";
    public static final String CONF_MEDIA = "media";
    public static final String CONF_NA = "NotApplicable";
    public static final String CONF_PURPOSE = "purpose";
    public static final String CONF_REASON = "reason";
    public static final String CONF_REFERRED = "refrred";
    public static final String CONF_ROLES = "roles";
    public static final String CONF_SIDEBAR_REF = "sidebars-by-ref";
    public static final String CONF_SIDEBAR_VALUE = "sidebars-by-val";
    public static final String CONF_STATE = "state";
    public static final String CONF_STATUS = "status";
    public static final String CONF_SUBJECT = "subject";
    public static final String CONF_TO_TAG = "to-tag";
    public static final String CONF_URI = "uri";
    public static final String CONF_URIS = "conf_uris";
    public static final String CONF_USER = "user";
    public static final String CONF_USERS = "users";
    public static final String CONF_VERSION = "version";
    public static final String CONF_WHEN = "when";
    public static final String HOST_INFO = "host-info";
    public static final String STATE_DELETE = "deleted";
    public static final String STATE_FULL = "full";
    public static final String STATE_INVALID = "invalid";
    public static final String STATE_PARTIAL = "partial";
    public static final String VICE_DIALOG = "dialog";
    public static final String VICE_DIALOG_CALLID = "call-id";
    public static final String VICE_DIALOG_DIRECTION = "direction";
    public static final String VICE_DIALOG_DURATION = "duration";
    public static final String VICE_DIALOG_ID = "id";
    public static final String VICE_DIALOG_INFO = "dialog-info";
    public static final String VICE_DIALOG_INFO_STATE = "state";
    public static final String VICE_DIALOG_LOCAL = "local";
    public static final String VICE_DIALOG_LOCAL_IDENTITY = "identity";
    public static final String VICE_DIALOG_LOCAL_TAG = "local-tag";
    public static final String VICE_DIALOG_REFERRED_BY = "referred-by";
    public static final String VICE_DIALOG_REMOTE = "remote";
    public static final String VICE_DIALOG_REMOTE_IDENTITY = "identity";
    public static final String VICE_DIALOG_REMOTE_TAG = "remote-tag";
    public static final String VICE_DIALOG_REPLACES = "replaces";
    public static final String VICE_DIALOG_REPLACES_CALLID = "call-id";
    public static final String VICE_DIALOG_REPLACES_LOCALTAG = "local-tag";
    public static final String VICE_DIALOG_REPLACES_REMOTETAG = "remote-tag";
    public static final String VICE_DIALOG_ROUTE_SET = "route-set";
    public static final String VICE_DIALOG_ROUTE_SET_HOP = "hop";
    public static final String VICE_DIALOG_SA = "sa:exclusive";
    public static final String VICE_DIALOG_STATE = "state";
    public static final String VICE_DISPLAY = "display";
    public static final String VICE_ENTITY = "entity";
    public static final String VICE_LOCAL_TARGET = "target";
    public static final String VICE_LOCAL_TARGET_PARAM = "param";
    public static final String VICE_LOCAL_TARGET_PARAM_NAME = "pname";
    public static final String VICE_LOCAL_TARGET_PARAM_VAL = "pval";
    public static final String VICE_LOCAL_TARGET_URI = "uri";
    public static final String VICE_MEDIA_ATTRI = "mediaAttributes";
    public static final String VICE_MEDIA_DIRECTION = "mediaDirection";
    public static final String VICE_MEDIA_PORT = "port0";
    public static final String VICE_MEDIA_TYPE = "mediaType";
    public static final String VICE_NA = "NotApplicable";
    public static final String VICE_REMOTE_TARGET = "target";
    public static final String VICE_REMOTE_TARGET_URI = "uri";
    public static final String VICE_VERSION = "version";
    public static final String VICE_XML = "xml";
    public static final String VICE_XML_ENCODING = "encoding";
    public static final String VICE_XML_VERSION = "version";
    private Map<String, String> mAttribute;
    public boolean mIsUpdateRequire = false;
    private String mParentTag;
    private List<Element> mSubElement;
    private String mTagName;
    private static ArrayList<Element> mMatchedElementList = new ArrayList<>();
    private static String LOGTAG = "Element";

    public void Element() {
        this.mTagName = null;
        this.mParentTag = null;
        this.mAttribute = null;
        this.mSubElement = null;
    }

    public void setTagName(String tag) {
        this.mTagName = tag;
    }

    public void setMapAttribute() {
        this.mAttribute = new HashMap();
    }

    public Map<String, String> getMapAttribute() {
        return this.mAttribute;
    }

    public void setMapAttribute(Map<String, String> marg) {
        this.mAttribute = marg;
    }

    public String getTagName() {
        return this.mTagName;
    }

    public String getParentTag() {
        return this.mParentTag;
    }

    public void setParentTag(String parenttag) {
        this.mParentTag = parenttag;
    }

    public String getAttributeValue(String attname) {
        Map<String, String> map;
        if (attname == null || (map = this.mAttribute) == null || !map.containsKey(attname)) {
            return null;
        }
        String ret = this.mAttribute.get(attname);
        return ret;
    }

    public void setAttributValue(String key, String value) {
        this.mAttribute.put(key, value);
    }

    public List<Element> getSubElementList() {
        return this.mSubElement;
    }

    public void addSubElement(Element child) {
        this.mSubElement.add(child);
    }

    public void updateAttributeValue(String attname, String value) {
        this.mAttribute.put(attname, value);
    }

    public void setSubElementList(List<Element> list) {
        this.mSubElement = list;
    }

    private boolean CheckForParentTag(Element element, String Parenttag) {
        if (element.mParentTag.equals(Parenttag)) {
            Log.d(LOGTAG, "Parent Tag" + element.mParentTag);
            Log.d(LOGTAG, "Search Parent Tag" + Parenttag);
            return true;
        }
        return false;
    }

    public static void clearMatchedElementsList() {
        mMatchedElementList.clear();
    }

    public static ArrayList<Element> getMatchedElements(String tagname, String parentTag, Element root) {
        if (root != null) {
            if (root.getSubElementList() == null) {
                String str = root.mParentTag;
                if (str != null && str.equals(parentTag) && root.mTagName.equals(tagname)) {
                    mMatchedElementList.add(root);
                    Log.d(LOGTAG, "Single node element added to mMatchedElementList");
                }
            } else {
                Log.d(LOGTAG, "Sublist not null");
                for (int index = 0; index < root.mSubElement.size(); index++) {
                    Element currentElement = root.getSubElementList().get(index);
                    if (currentElement.mTagName.equals(tagname)) {
                        mMatchedElementList.add(currentElement);
                        Log.d(LOGTAG, "Sub Element " + index + "added to mMatchedElementList");
                    } else if (currentElement.getMapAttribute() != null && currentElement.getMapAttribute().containsKey(tagname)) {
                        if (currentElement.mParentTag.equals(parentTag)) {
                            mMatchedElementList.add(currentElement);
                        } else {
                            Log.d(LOGTAG, "Ignoring sub Element " + index + "with no match as Parent tag ");
                        }
                    } else {
                        Log.d(LOGTAG, "Recurssive call on index " + index);
                        getMatchedElements(tagname, parentTag, currentElement);
                    }
                }
            }
        } else {
            Log.d(LOGTAG, "Root element is null");
        }
        return mMatchedElementList;
    }

    public ArrayList<Element> getElementWithKey(String tagname, String parenttag, String key) {
        List<Element> SubElement = getMatchedElements(tagname, parenttag, this);
        ArrayList<Element> KeyMatchElementList = new ArrayList<>();
        for (int index = 0; index < SubElement.size(); index++) {
            if (SubElement.get(index).mAttribute.containsKey(key)) {
                KeyMatchElementList.add(SubElement.get(index));
            }
        }
        return KeyMatchElementList;
    }

    public String getDocVersion() {
        return this.mAttribute.get("version");
    }

    public String getElementState(Element element) {
        return element.mAttribute.get("state");
    }

    public void DeleteSubElementsFromTree(Element element) {
        if (element.mAttribute.get("state").equals(STATE_DELETE)) {
            ClearAll();
        }
    }

    public void ClearAll() {
        this.mTagName = null;
        Map<String, String> map = this.mAttribute;
        if (map != null) {
            map.clear();
        }
        this.mAttribute = null;
        List<Element> list = this.mSubElement;
        if (list != null) {
            list.clear();
        }
        this.mSubElement = null;
    }

    public boolean compareElements(Element lhs) {
        Map<String, String> map;
        if (!lhs.mTagName.equals(this.mTagName) || !lhs.mParentTag.equals(this.mParentTag)) {
            return false;
        }
        List<Element> list = this.mSubElement;
        if (list != null && lhs.mSubElement != null) {
            int index = list.size();
            int lindex = lhs.mSubElement.size();
            if (index == lindex) {
                while (index != 0) {
                    compareElements(lhs.mSubElement.get(index));
                    index--;
                }
            }
            return false;
        }
        Map<String, String> map2 = this.mAttribute;
        if (map2 == null || (map = lhs.mAttribute) == null) {
            return true;
        }
        map.equals(map2);
        return true;
    }

    public boolean IsUpdateRequire() {
        return this.mIsUpdateRequire;
    }
}
