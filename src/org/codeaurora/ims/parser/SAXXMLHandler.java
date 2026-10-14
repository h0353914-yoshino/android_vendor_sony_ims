package org.codeaurora.ims.parser;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes.dex */
public class SAXXMLHandler extends DefaultHandler {
    private StringBuilder builder;
    private List<Element> mConfDescSubElementList;
    private Element mConfDescriptionElement;
    private List<Element> mConfUriEntryList;
    private List<Element> mConfUriSubElementList;
    private Element mConfuri;
    private Element mMessageElement;
    private List<Element> mMessageSubList;
    private Element mUsersElement;
    private List<Element> mUsersList;
    private List<Element> muserEndPointList;
    private List<Element> muserSubElementlist;
    private List<Element> usersubElementList;
    private boolean mInConfDescription = false;
    private boolean mInUser = false;
    private boolean mIsEndPoint = false;
    private boolean mIsEnpointInfo = false;
    private boolean mIsreferredInfo = false;
    private boolean mIsJoiningInfo = false;
    private boolean mIsDisconnectInfo = false;
    private boolean mIsUserCallInfo = false;
    private boolean mIsconuri = false;
    private boolean mIsassociated = false;
    private boolean mIsserviceuri = false;
    private boolean mIsmedia = false;
    private boolean mIsentry = false;
    private boolean mIscallinfo = false;
    private boolean mIsroles = false;
    private final String LOGTAG = "SAXXMLHandler";

    public SAXXMLHandler() {
        Log.d("SAXXMLHandler", "New List obj created");
        this.mMessageElement = new Element();
    }

    public Element getConferenceInfoLatestMessage() {
        return this.mMessageElement;
    }

    public Element getConferenceDescElement() {
        return this.mConfDescriptionElement;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        super.startDocument();
        this.mConfUriEntryList = new ArrayList();
        this.muserEndPointList = new ArrayList();
        this.builder = new StringBuilder();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase(Element.CONF_INFO)) {
            Element element = new Element();
            this.mMessageElement = element;
            element.setTagName(Element.CONF_INFO);
            this.mMessageElement.setMapAttribute();
            this.mMessageElement.setParentTag(null);
            this.mMessageElement.setAttributValue("state", attributes.getValue("state"));
            this.mMessageElement.setAttributValue("version", attributes.getValue("version"));
            this.mMessageElement.setAttributValue("entity", attributes.getValue("entity"));
            ArrayList arrayList = new ArrayList();
            this.mMessageSubList = arrayList;
            this.mMessageElement.setSubElementList(arrayList);
            Log.d("SAXXMLHandler", "New ConfreInf obj created");
        }
        if (qName.equalsIgnoreCase(Element.CONF_DESC) || qName.equalsIgnoreCase(Element.CONF_MAX_COUNT)) {
            Element element2 = new Element();
            this.mConfDescriptionElement = element2;
            element2.setTagName(Element.CONF_DESC);
            this.mConfDescriptionElement.setParentTag(Element.CONF_INFO);
            this.mConfDescriptionElement.setMapAttribute();
            ArrayList arrayList2 = new ArrayList();
            this.mConfDescSubElementList = arrayList2;
            this.mConfDescriptionElement.setSubElementList(arrayList2);
            this.mMessageSubList.add(this.mConfDescriptionElement);
            this.mInConfDescription = true;
        } else if (qName.equalsIgnoreCase(Element.CONF_USERS)) {
            Element element3 = new Element();
            this.mUsersElement = element3;
            element3.setTagName(Element.CONF_USERS);
            this.mUsersElement.setParentTag(Element.CONF_INFO);
            ArrayList arrayList3 = new ArrayList();
            this.muserSubElementlist = arrayList3;
            this.mUsersElement.setSubElementList(arrayList3);
            this.mMessageSubList.add(this.mUsersElement);
        } else if (qName.equalsIgnoreCase(Element.CONF_USER)) {
            Log.d("SAXXMLHandler", "User need to added start");
            Element UserElement = new Element();
            UserElement.setTagName(Element.CONF_USER);
            UserElement.setParentTag(Element.CONF_USERS);
            UserElement.setMapAttribute();
            UserElement.setAttributValue("state", attributes.getValue("state"));
            UserElement.setAttributValue("entity", attributes.getValue("entity"));
            this.muserSubElementlist.add(UserElement);
            this.mInUser = true;
        } else if (qName.equalsIgnoreCase(Element.CONF_DISPLAY_TEXT) && this.mInUser && !this.mIsassociated) {
            this.mIscallinfo = false;
        } else if (qName.equalsIgnoreCase(Element.CONF_ASSOCIATED_AORS) && this.mInUser) {
            this.mIsassociated = true;
        } else if (qName.equalsIgnoreCase(Element.CONF_DISPLAY_TEXT) && this.mIsassociated) {
            this.mIsentry = true;
        } else if (qName.equalsIgnoreCase("uri") && this.mIsassociated) {
            this.mIsentry = true;
        } else if (qName.equalsIgnoreCase(Element.CONF_ROLES)) {
            this.mIsroles = true;
            this.mIsentry = true;
        } else if (qName.equalsIgnoreCase(Element.CONF_ENDPOINT) && this.mInUser) {
            Element endpoint = new Element();
            endpoint.setTagName(Element.CONF_ENDPOINT);
            endpoint.setParentTag(Element.CONF_USER);
            endpoint.setMapAttribute();
            endpoint.setAttributValue("entity", attributes.getValue("entity"));
            this.muserEndPointList.add(endpoint);
            this.mIsEndPoint = true;
        } else if (qName.equalsIgnoreCase(Element.CONF_URIS)) {
            this.mIsconuri = true;
            Element element4 = new Element();
            this.mConfuri = element4;
            element4.setTagName(Element.CONF_URIS);
            this.mConfuri.setParentTag(Element.CONF_DESC);
            ArrayList arrayList4 = new ArrayList();
            this.mConfUriSubElementList = arrayList4;
            this.mConfuri.setSubElementList(arrayList4);
            this.mConfDescSubElementList.add(this.mConfuri);
        } else {
            qName.equalsIgnoreCase(Element.CONF_ENTRY);
        }
        if (this.mIsEndPoint) {
            this.mIsEnpointInfo = true;
            if (qName.equalsIgnoreCase(Element.CONF_REFERRED)) {
                this.mIsreferredInfo = true;
                return;
            }
            if (qName.equalsIgnoreCase(Element.CONF_JOINING_INFO)) {
                this.mIsJoiningInfo = true;
            } else if (qName.equalsIgnoreCase(Element.CONF_DISC_INFO)) {
                this.mIsDisconnectInfo = true;
            } else if (qName.equalsIgnoreCase(Element.CONF_CALL_INFO)) {
                this.mIsUserCallInfo = true;
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        this.builder.append(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        boolean z;
        super.endElement(uri, localName, qName);
        if (this.mMessageElement != null) {
            Log.d("SAXXMLHandler", "endelment:inside");
            if (this.mInConfDescription) {
                if (qName.equalsIgnoreCase(Element.CONF_DISPLAY_TEXT)) {
                    this.mConfDescriptionElement.setAttributValue(Element.CONF_DISPLAY_TEXT, this.builder.toString().trim());
                } else if (qName.equalsIgnoreCase(Element.CONF_MAX_COUNT)) {
                    this.mConfDescriptionElement.setAttributValue(Element.CONF_MAX_COUNT, this.builder.toString().trim());
                }
                this.mInConfDescription = false;
            } else if (qName.equalsIgnoreCase(Element.CONF_SUBJECT)) {
                this.mConfDescriptionElement.setAttributValue(Element.CONF_SUBJECT, this.builder.toString().trim());
            } else if (qName.equalsIgnoreCase(Element.CONF_FREE_TEXT)) {
                this.mConfDescriptionElement.setAttributValue(Element.CONF_FREE_TEXT, this.builder.toString().trim());
            } else if (qName.equalsIgnoreCase(Element.CONF_KEYWORDS)) {
                this.mConfDescriptionElement.setAttributValue(Element.CONF_KEYWORDS, this.builder.toString().trim());
            }
            if (this.mIsconuri) {
                Element mEntry = new Element();
                mEntry.setTagName(Element.CONF_ENTRY);
                mEntry.setParentTag(Element.CONF_URIS);
                if (qName.equalsIgnoreCase(Element.CONF_DISPLAY_TEXT)) {
                    mEntry.setAttributValue(Element.CONF_DISPLAY_TEXT, this.builder.toString().trim());
                } else if (qName.equalsIgnoreCase("uri")) {
                    mEntry.setAttributValue("uri", this.builder.toString().trim());
                } else if (qName.equalsIgnoreCase(Element.CONF_PURPOSE)) {
                    mEntry.setAttributValue(Element.CONF_PURPOSE, this.builder.toString().trim());
                } else if (qName.equalsIgnoreCase(Element.CONF_ENTRY)) {
                    this.mConfUriSubElementList.add(mEntry);
                } else if (qName.equalsIgnoreCase(Element.CONF_URIS)) {
                    this.mConfuri.setSubElementList(this.mConfUriSubElementList);
                    this.mIsconuri = false;
                }
            }
            if (this.mInUser) {
                List<Element> list = this.muserSubElementlist;
                Element UserElement = list.get(list.size() - 1);
                UserElement.setTagName(Element.CONF_USER);
                UserElement.setParentTag(Element.CONF_USERS);
                ArrayList<Element> usersubElementList = new ArrayList<>();
                UserElement.setSubElementList(usersubElementList);
                if (qName.equalsIgnoreCase(Element.CONF_DISPLAY_TEXT) && !this.mIscallinfo && !this.mIsassociated) {
                    UserElement.setAttributValue(Element.CONF_DISPLAY_TEXT, this.builder.toString().trim());
                }
                if (this.mIsentry) {
                    Element Associated = new Element();
                    Associated.setMapAttribute();
                    if (qName.equalsIgnoreCase(Element.CONF_DISPLAY_TEXT) && this.mIsassociated) {
                        Associated.setTagName(Element.CONF_ASSOCIATED_AORS);
                        Associated.setParentTag(Element.CONF_USER);
                        Associated.setAttributValue(Element.CONF_DISPLAY_TEXT, this.builder.toString().trim());
                    } else if (qName.equalsIgnoreCase("uri") && this.mIsassociated) {
                        Associated.setAttributValue("uri", this.builder.toString().trim());
                    } else if (qName.equalsIgnoreCase(Element.CONF_ENTRY) && this.mIsassociated) {
                        usersubElementList.add(Associated);
                        this.mIsentry = false;
                    } else if (qName.equalsIgnoreCase(Element.CONF_ENTRY) && this.mIsroles) {
                        Element Role = new Element();
                        Role.setMapAttribute();
                        Role.setTagName(Element.CONF_ROLES);
                        Role.setParentTag(Element.CONF_ASSOCIATED_AORS);
                        Role.setAttributValue(Element.CONF_ENTRY, this.builder.toString().trim());
                        usersubElementList.add(Role);
                    } else if (qName.equalsIgnoreCase(Element.CONF_LANGUAGE)) {
                        UserElement.setAttributValue(Element.CONF_LANGUAGE, this.builder.toString().trim());
                    }
                }
                if (this.mIsEndPoint) {
                    List<Element> list2 = this.muserEndPointList;
                    Element endpoint = list2.get(list2.size() - 1);
                    endpoint.setTagName(Element.CONF_ENDPOINT);
                    endpoint.setParentTag(Element.CONF_USER);
                    ArrayList<Element> EndpointInfoList = new ArrayList<>();
                    if (qName.equalsIgnoreCase(Element.CONF_DISPLAY_TEXT)) {
                        endpoint.setAttributValue(Element.CONF_DISPLAY_TEXT, this.builder.toString().trim());
                    } else if (qName.equalsIgnoreCase(Element.CONF_STATUS)) {
                        endpoint.setAttributValue(Element.CONF_STATUS, this.builder.toString().trim());
                    } else if (qName.equalsIgnoreCase(Element.CONF_JOIN_METHOD)) {
                        endpoint.setAttributValue(Element.CONF_JOIN_METHOD, this.builder.toString().trim());
                    } else if (qName.equalsIgnoreCase(Element.CONF_DISC_METHOD)) {
                        endpoint.setAttributValue(Element.CONF_DISC_METHOD, this.builder.toString().trim());
                    }
                    if (this.mIsEnpointInfo) {
                        if (!this.mIsreferredInfo) {
                            if (!this.mIsJoiningInfo) {
                                if (this.mIsDisconnectInfo) {
                                    Element DisconnectInfo = new Element();
                                    DisconnectInfo.setMapAttribute();
                                    DisconnectInfo.setTagName(Element.CONF_DISC_INFO);
                                    DisconnectInfo.setParentTag(Element.CONF_ENDPOINT);
                                    EndpointInfoList.add(DisconnectInfo);
                                    if (qName.equalsIgnoreCase(Element.CONF_WHEN)) {
                                        DisconnectInfo.setAttributValue(Element.CONF_WHEN, this.builder.toString().trim());
                                    } else if (qName.equalsIgnoreCase(Element.CONF_REASON)) {
                                        DisconnectInfo.setAttributValue(Element.CONF_REASON, this.builder.toString().trim());
                                    } else if (qName.equalsIgnoreCase(Element.CONF_BY)) {
                                        DisconnectInfo.setAttributValue(Element.CONF_BY, this.builder.toString().trim());
                                    }
                                } else if (this.mIsUserCallInfo) {
                                    Element UserCallInfo = new Element();
                                    UserCallInfo.setMapAttribute();
                                    UserCallInfo.setTagName(Element.CONF_CALL_INFO);
                                    UserCallInfo.setParentTag(Element.CONF_ENDPOINT);
                                    EndpointInfoList.add(UserCallInfo);
                                    if (qName.equalsIgnoreCase(Element.CONF_DISPLAY_TEXT)) {
                                        UserCallInfo.setAttributValue(Element.CONF_DISPLAY_TEXT, this.builder.toString().trim());
                                    } else if (qName.equalsIgnoreCase("call-id")) {
                                        UserCallInfo.setAttributValue("call-id", this.builder.toString().trim());
                                    } else if (qName.equalsIgnoreCase(Element.CONF_FROM_TAG)) {
                                        UserCallInfo.setAttributValue(Element.CONF_FROM_TAG, this.builder.toString().trim());
                                    } else if (qName.equalsIgnoreCase(Element.CONF_TO_TAG)) {
                                        UserCallInfo.setAttributValue(Element.CONF_TO_TAG, this.builder.toString().trim());
                                    }
                                }
                            } else {
                                Element JoiningInfo = new Element();
                                JoiningInfo.setTagName(Element.CONF_JOINING_INFO);
                                JoiningInfo.setParentTag(Element.CONF_ENDPOINT);
                                JoiningInfo.setMapAttribute();
                                EndpointInfoList.add(JoiningInfo);
                                if (qName.equalsIgnoreCase(Element.CONF_WHEN)) {
                                    JoiningInfo.setAttributValue(Element.CONF_WHEN, this.builder.toString().trim());
                                } else if (qName.equalsIgnoreCase(Element.CONF_REASON)) {
                                    JoiningInfo.setAttributValue(Element.CONF_REASON, this.builder.toString().trim());
                                } else if (qName.equalsIgnoreCase(Element.CONF_BY)) {
                                    JoiningInfo.setAttributValue(Element.CONF_BY, this.builder.toString().trim());
                                }
                            }
                        } else {
                            Element refferedinfo = new Element();
                            refferedinfo.setTagName(Element.CONF_REFERRED);
                            refferedinfo.setParentTag(Element.CONF_ENDPOINT);
                            refferedinfo.setMapAttribute();
                            EndpointInfoList.add(refferedinfo);
                            if (qName.equalsIgnoreCase(Element.CONF_WHEN)) {
                                refferedinfo.setAttributValue(Element.CONF_WHEN, this.builder.toString().trim());
                            } else if (qName.equalsIgnoreCase(Element.CONF_REASON)) {
                                refferedinfo.setAttributValue(Element.CONF_REASON, this.builder.toString().trim());
                            } else if (qName.equalsIgnoreCase(Element.CONF_BY)) {
                                refferedinfo.setAttributValue(Element.CONF_BY, this.builder.toString().trim());
                            }
                        }
                    }
                    endpoint.setSubElementList(EndpointInfoList);
                    usersubElementList.add(endpoint);
                }
                if (!qName.equalsIgnoreCase(Element.CONF_ASSOCIATED_AORS)) {
                    z = false;
                } else {
                    z = false;
                    this.mIsassociated = false;
                }
                if (qName.equalsIgnoreCase(Element.CONF_USER)) {
                    Log.d("SAXXMLHandler", "User need to added end");
                    this.mInUser = z;
                    this.mIsEndPoint = z;
                    this.mIsEnpointInfo = z;
                    this.mIsUserCallInfo = z;
                }
                if (qName.equalsIgnoreCase(Element.CONF_USERS)) {
                    this.mUsersList.add(UserElement);
                }
            }
            qName.equalsIgnoreCase(Element.CONF_INFO);
            this.builder.setLength(0);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() {
        try {
            super.endDocument();
            Log.d("SAXXMLHandler", "Root Tag Name:" + this.mMessageElement.getTagName());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
