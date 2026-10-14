package org.codeaurora.ims.parser;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes.dex */
public class ViceSaxXmlHandler extends DefaultHandler {
    private StringBuilder builder;
    private ImsViceParser.DialogInfo mDialogInfo;
    private ImsViceParser.MediaAttribute mMediaAttribute;
    private Element mViceDialogElement;
    private Element mViceDialogHopElement;
    private Element mViceDialogInfoElement;
    private List<Element> mViceDialogInfoSubList;
    private Element mViceDialogLocalElement;
    private Element mViceDialogLocalIdentityElement;
    private List<Element> mViceDialogLocalSubList;
    private Element mViceDialogLocalTargetElement;
    private List<Element> mViceDialogLocalTargetSubList;
    private Element mViceDialogMediaAttriElement;
    private List<Element> mViceDialogMediaAttriSubList;
    private Element mViceDialogMediaDirElement;
    private Element mViceDialogMediaPortElement;
    private Element mViceDialogMediaTypeElement;
    private Element mViceDialogReferredElement;
    private Element mViceDialogRemoteElement;
    private Element mViceDialogRemoteIdentityElement;
    private List<Element> mViceDialogRemoteSubList;
    private Element mViceDialogRemoteTargetElement;
    private Element mViceDialogRouteElement;
    private List<Element> mViceDialogRouteSubList;
    private List<Element> mViceDialogSubList;
    private boolean mDoesDialogHaveSubElement = false;
    private boolean mDoesDialogLocalHaveSubElement = false;
    private boolean mDoesRouteSetHaveSubElement = false;
    private boolean mDoesRemoteDialogHaveSubElement = false;
    private List<ImsViceParser.DialogInfo> mDialogInfoList = new ArrayList();
    private final String LOGTAG = "ViceSaxXmlHandler";

    public ViceSaxXmlHandler() {
        Log.d("ViceSaxXmlHandler", "ViceSaxxHandler: constructor");
        this.mViceDialogInfoElement = new Element();
    }

    public Element getConferenceInfoLatestMessage() {
        return this.mViceDialogInfoElement;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        super.startDocument();
        this.builder = new StringBuilder();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        Log.d("ViceSaxXmlHandler", "uri = " + uri + ", localname = " + localName + ", qName = " + qName + ", attri = " + attributes);
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG_INFO)) {
            Element element = new Element();
            this.mViceDialogInfoElement = element;
            element.setTagName(Element.VICE_DIALOG_INFO);
            this.mViceDialogInfoElement.setMapAttribute();
            this.mViceDialogInfoElement.setParentTag(null);
            this.mViceDialogInfoElement.setAttributValue("version", attributes.getValue("version"));
            this.mViceDialogInfoElement.setAttributValue("state", attributes.getValue("state"));
            this.mViceDialogInfoElement.setAttributValue("entity", attributes.getValue("entity"));
            ArrayList arrayList = new ArrayList();
            this.mViceDialogInfoSubList = arrayList;
            this.mViceDialogInfoElement.setSubElementList(arrayList);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogInfoElement.getTagName());
            Log.d("ViceSaxXmlHandler", "attribute vals = " + this.mViceDialogInfoElement.getAttributeValue("version") + ", " + this.mViceDialogInfoElement.getAttributeValue("state") + ", " + this.mViceDialogInfoElement.getAttributeValue("entity"));
        }
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG)) {
            this.mDialogInfo = new ImsViceParser.DialogInfo();
            Element element2 = new Element();
            this.mViceDialogElement = element2;
            element2.setTagName(Element.VICE_DIALOG);
            this.mViceDialogElement.setParentTag(Element.VICE_DIALOG_INFO);
            this.mViceDialogElement.setMapAttribute();
            this.mViceDialogInfoSubList.add(this.mViceDialogElement);
            ArrayList arrayList2 = new ArrayList();
            this.mViceDialogSubList = arrayList2;
            this.mViceDialogElement.setSubElementList(arrayList2);
            this.mViceDialogElement.setAttributValue("id", attributes.getValue("id"));
            this.mViceDialogElement.setAttributValue("call-id", attributes.getValue("call-id"));
            this.mViceDialogElement.setAttributValue("local-tag", attributes.getValue("local-tag"));
            this.mViceDialogElement.setAttributValue("remote-tag", attributes.getValue("remote-tag"));
            this.mViceDialogElement.setAttributValue(Element.VICE_DIALOG_DIRECTION, attributes.getValue(Element.VICE_DIALOG_DIRECTION));
            this.mDoesDialogHaveSubElement = true;
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogElement.getTagName());
            Log.d("ViceSaxXmlHandler", "attribute vals = " + this.mViceDialogElement.getAttributeValue("id") + ", " + this.mViceDialogElement.getAttributeValue("call-id") + ", " + this.mViceDialogElement.getAttributeValue("local-tag") + ", " + this.mViceDialogElement.getAttributeValue("remote-tag") + ", " + this.mViceDialogElement.getAttributeValue(Element.VICE_DIALOG_DIRECTION));
            ImsViceParser.DialogInfo dialogInfo = new ImsViceParser.DialogInfo();
            this.mDialogInfo = dialogInfo;
            dialogInfo.dialogId = this.mViceDialogElement.getAttributeValue("id");
            this.mDialogInfo.direction = this.mViceDialogElement.getAttributeValue(Element.VICE_DIALOG_DIRECTION);
        }
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG_SA) || qName.equalsIgnoreCase("state") || qName.equalsIgnoreCase(Element.VICE_DIALOG_DURATION)) {
            this.mDoesDialogHaveSubElement = true;
        }
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG_REPLACES)) {
            Element viceDialogReplacesElement = new Element();
            viceDialogReplacesElement.setTagName(Element.VICE_DIALOG_REPLACES);
            viceDialogReplacesElement.setParentTag(Element.VICE_DIALOG);
            viceDialogReplacesElement.setMapAttribute();
            this.mViceDialogSubList.add(viceDialogReplacesElement);
            viceDialogReplacesElement.setAttributValue("call-id", attributes.getValue("call-id"));
            viceDialogReplacesElement.setAttributValue("local-tag", attributes.getValue("local-tag"));
            viceDialogReplacesElement.setAttributValue("remote-tag", attributes.getValue("remote-tag"));
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + viceDialogReplacesElement.getTagName());
            Log.d("ViceSaxXmlHandler", "attribute vals = " + attributes.getValue("call-id") + ", " + attributes.getValue("local-tag") + ", " + attributes.getValue("remote-tag"));
        }
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG_REFERRED_BY)) {
            Element element3 = new Element();
            this.mViceDialogReferredElement = element3;
            element3.setTagName(Element.VICE_DIALOG_REFERRED_BY);
            this.mViceDialogReferredElement.setParentTag(Element.VICE_DIALOG);
            this.mViceDialogReferredElement.setMapAttribute();
            this.mViceDialogSubList.add(this.mViceDialogReferredElement);
            this.mDoesDialogHaveSubElement = true;
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogReferredElement.getTagName());
        }
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG_ROUTE_SET)) {
            Element element4 = new Element();
            this.mViceDialogRouteElement = element4;
            element4.setTagName(Element.VICE_DIALOG_ROUTE_SET);
            this.mViceDialogRouteElement.setParentTag(Element.VICE_DIALOG);
            this.mViceDialogRouteElement.setMapAttribute();
            this.mViceDialogSubList.add(this.mViceDialogRouteElement);
            ArrayList arrayList3 = new ArrayList();
            this.mViceDialogRouteSubList = arrayList3;
            this.mViceDialogRouteElement.setSubElementList(arrayList3);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogRouteElement.getTagName());
        }
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG_ROUTE_SET_HOP)) {
            Element element5 = new Element();
            this.mViceDialogHopElement = element5;
            element5.setTagName(Element.VICE_DIALOG_ROUTE_SET_HOP);
            this.mViceDialogHopElement.setParentTag(Element.VICE_DIALOG_ROUTE_SET);
            this.mViceDialogHopElement.setMapAttribute();
            this.mViceDialogRouteSubList.add(this.mViceDialogHopElement);
            this.mDoesRouteSetHaveSubElement = true;
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogHopElement.getTagName());
        }
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG_LOCAL)) {
            Element element6 = new Element();
            this.mViceDialogLocalElement = element6;
            element6.setTagName(Element.VICE_DIALOG_LOCAL);
            this.mViceDialogLocalElement.setParentTag(Element.VICE_DIALOG);
            this.mViceDialogLocalElement.setMapAttribute();
            this.mViceDialogSubList.add(this.mViceDialogLocalElement);
            ArrayList arrayList4 = new ArrayList();
            this.mViceDialogLocalSubList = arrayList4;
            this.mViceDialogLocalElement.setSubElementList(arrayList4);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogLocalElement.getTagName());
            this.mDoesDialogLocalHaveSubElement = true;
        }
        if (qName.equalsIgnoreCase("identity") && this.mDoesDialogLocalHaveSubElement) {
            Element element7 = new Element();
            this.mViceDialogLocalIdentityElement = element7;
            element7.setTagName("identity");
            this.mViceDialogLocalIdentityElement.setParentTag(Element.VICE_DIALOG_LOCAL);
            this.mViceDialogLocalIdentityElement.setMapAttribute();
            this.mViceDialogLocalSubList.add(this.mViceDialogLocalIdentityElement);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogLocalIdentityElement.getTagName());
            this.mDoesDialogLocalHaveSubElement = true;
        }
        if (qName.equalsIgnoreCase("target") && !this.mDoesRemoteDialogHaveSubElement) {
            Element element8 = new Element();
            this.mViceDialogLocalTargetElement = element8;
            element8.setTagName("target");
            this.mViceDialogLocalTargetElement.setParentTag(Element.VICE_DIALOG_LOCAL);
            this.mViceDialogLocalTargetElement.setMapAttribute();
            this.mViceDialogLocalSubList.add(this.mViceDialogLocalTargetElement);
            ArrayList arrayList5 = new ArrayList();
            this.mViceDialogLocalTargetSubList = arrayList5;
            this.mViceDialogLocalTargetElement.setSubElementList(arrayList5);
            this.mDoesDialogLocalHaveSubElement = true;
            this.mViceDialogLocalTargetElement.setAttributValue("uri", attributes.getValue("uri"));
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogLocalTargetElement.getTagName());
            Log.d("ViceSaxXmlHandler", "attribute vals = " + this.mViceDialogLocalTargetElement.getAttributeValue("uri"));
        }
        if (qName.equalsIgnoreCase(Element.VICE_LOCAL_TARGET_PARAM)) {
            Element viceDialogLocalParamElement = new Element();
            viceDialogLocalParamElement.setTagName(Element.VICE_LOCAL_TARGET_PARAM);
            viceDialogLocalParamElement.setParentTag("target");
            viceDialogLocalParamElement.setMapAttribute();
            this.mViceDialogLocalTargetSubList.add(viceDialogLocalParamElement);
            this.mDoesDialogLocalHaveSubElement = true;
            viceDialogLocalParamElement.setAttributValue(Element.VICE_LOCAL_TARGET_PARAM_NAME, attributes.getValue(Element.VICE_LOCAL_TARGET_PARAM_NAME));
            viceDialogLocalParamElement.setAttributValue(Element.VICE_LOCAL_TARGET_PARAM_VAL, attributes.getValue(Element.VICE_LOCAL_TARGET_PARAM_VAL));
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + viceDialogLocalParamElement.getTagName());
            Log.d("ViceSaxXmlHandler", "attribute vals = " + attributes.getValue(Element.VICE_LOCAL_TARGET_PARAM_NAME) + ", " + attributes.getValue(Element.VICE_LOCAL_TARGET_PARAM_VAL));
            this.mDialogInfo.paramName = viceDialogLocalParamElement.getAttributeValue(Element.VICE_LOCAL_TARGET_PARAM_NAME);
            this.mDialogInfo.paramVal = viceDialogLocalParamElement.getAttributeValue(Element.VICE_LOCAL_TARGET_PARAM_VAL);
        }
        if (qName.equalsIgnoreCase(Element.VICE_MEDIA_ATTRI)) {
            Element element9 = new Element();
            this.mViceDialogMediaAttriElement = element9;
            element9.setTagName(Element.VICE_MEDIA_ATTRI);
            this.mViceDialogMediaAttriElement.setParentTag(Element.VICE_DIALOG_LOCAL);
            this.mViceDialogMediaAttriElement.setMapAttribute();
            this.mViceDialogLocalSubList.add(this.mViceDialogMediaAttriElement);
            ArrayList arrayList6 = new ArrayList();
            this.mViceDialogMediaAttriSubList = arrayList6;
            this.mViceDialogMediaAttriElement.setSubElementList(arrayList6);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogMediaAttriElement.getTagName());
            this.mDoesDialogLocalHaveSubElement = true;
            this.mMediaAttribute = new ImsViceParser.MediaAttribute();
        }
        if (qName.equalsIgnoreCase(Element.VICE_MEDIA_TYPE)) {
            Element element10 = new Element();
            this.mViceDialogMediaTypeElement = element10;
            element10.setTagName(Element.VICE_MEDIA_TYPE);
            this.mViceDialogMediaTypeElement.setParentTag(Element.VICE_MEDIA_ATTRI);
            this.mViceDialogMediaTypeElement.setMapAttribute();
            this.mViceDialogMediaAttriSubList.add(this.mViceDialogMediaTypeElement);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogMediaTypeElement.getTagName());
            this.mDoesDialogLocalHaveSubElement = true;
        }
        if (qName.equalsIgnoreCase(Element.VICE_MEDIA_DIRECTION)) {
            Element element11 = new Element();
            this.mViceDialogMediaDirElement = element11;
            element11.setTagName(Element.VICE_MEDIA_DIRECTION);
            this.mViceDialogMediaDirElement.setParentTag(Element.VICE_MEDIA_ATTRI);
            this.mViceDialogMediaDirElement.setMapAttribute();
            this.mViceDialogMediaAttriSubList.add(this.mViceDialogMediaDirElement);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogMediaDirElement.getTagName());
            this.mDoesDialogLocalHaveSubElement = true;
        }
        if (qName.equalsIgnoreCase(Element.VICE_MEDIA_PORT)) {
            Element element12 = new Element();
            this.mViceDialogMediaPortElement = element12;
            element12.setTagName(Element.VICE_MEDIA_PORT);
            this.mViceDialogMediaPortElement.setParentTag(Element.VICE_MEDIA_ATTRI);
            this.mViceDialogMediaPortElement.setMapAttribute();
            this.mViceDialogMediaAttriSubList.add(this.mViceDialogMediaPortElement);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogMediaPortElement.getTagName());
            this.mDoesDialogLocalHaveSubElement = true;
        }
        if (qName.equalsIgnoreCase(Element.VICE_DIALOG_REMOTE)) {
            Element element13 = new Element();
            this.mViceDialogRemoteElement = element13;
            element13.setTagName(Element.VICE_DIALOG_REMOTE);
            this.mViceDialogRemoteElement.setParentTag(Element.VICE_DIALOG);
            this.mViceDialogRemoteElement.setMapAttribute();
            this.mViceDialogSubList.add(this.mViceDialogRemoteElement);
            ArrayList arrayList7 = new ArrayList();
            this.mViceDialogRemoteSubList = arrayList7;
            this.mViceDialogRemoteElement.setSubElementList(arrayList7);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogRemoteElement.getTagName());
            this.mDoesDialogHaveSubElement = true;
            this.mDoesRemoteDialogHaveSubElement = true;
        }
        if (qName.equalsIgnoreCase("identity") && !this.mDoesDialogLocalHaveSubElement) {
            Element element14 = new Element();
            this.mViceDialogRemoteIdentityElement = element14;
            element14.setTagName("identity");
            this.mViceDialogRemoteIdentityElement.setParentTag(Element.VICE_DIALOG_REMOTE);
            this.mViceDialogRemoteIdentityElement.setMapAttribute();
            this.mViceDialogRemoteSubList.add(this.mViceDialogRemoteIdentityElement);
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogRemoteIdentityElement.getTagName());
            this.mDoesDialogHaveSubElement = true;
        }
        if (qName.equalsIgnoreCase("target") && this.mDoesRemoteDialogHaveSubElement) {
            Element element15 = new Element();
            this.mViceDialogRemoteTargetElement = element15;
            element15.setTagName("target");
            this.mViceDialogRemoteTargetElement.setParentTag(Element.VICE_DIALOG_REMOTE);
            this.mViceDialogRemoteTargetElement.setMapAttribute();
            this.mViceDialogRemoteSubList.add(this.mViceDialogRemoteTargetElement);
            this.mViceDialogRemoteTargetElement.setAttributValue("uri", attributes.getValue("uri"));
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogRemoteTargetElement.getTagName());
            Log.d("ViceSaxXmlHandler", "attribute vals = " + this.mViceDialogRemoteTargetElement.getAttributeValue("uri"));
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        this.builder.append(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (this.mViceDialogInfoElement != null) {
            Log.d("ViceSaxXmlHandler", "endelment:inside");
            if (this.mDoesDialogHaveSubElement) {
                if (qName.equalsIgnoreCase(Element.VICE_DIALOG_SA)) {
                    this.mViceDialogElement.setAttributValue(Element.VICE_DIALOG_SA, this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_DIALOG_SA attribute vals = " + this.mViceDialogElement.getAttributeValue(Element.VICE_DIALOG_SA));
                    this.mDialogInfo.exclusive = this.mViceDialogElement.getAttributeValue(Element.VICE_DIALOG_SA);
                } else if (qName.equalsIgnoreCase("state")) {
                    this.mViceDialogElement.setAttributValue("state", this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_DIALOG_STATE attribute vals = " + this.mViceDialogElement.getAttributeValue("state"));
                    this.mDialogInfo.state = this.mViceDialogElement.getAttributeValue("state");
                } else if (qName.equalsIgnoreCase(Element.VICE_DIALOG_DURATION)) {
                    this.mViceDialogElement.setAttributValue(Element.VICE_DIALOG_DURATION, this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_DIALOG_DURATION attribute vals = " + this.mViceDialogElement.getAttributeValue(Element.VICE_DIALOG_DURATION));
                } else if (qName.equalsIgnoreCase("identity")) {
                    this.mViceDialogRemoteIdentityElement.setAttributValue("identity", this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_DIALOG_REMOTE_IDENTITY attribute vals = " + com.qualcomm.ims.utils.Log.pii(this.mViceDialogRemoteIdentityElement.getAttributeValue("identity")));
                    this.mDialogInfo.remoteIdentity = this.mViceDialogRemoteIdentityElement.getAttributeValue("identity");
                } else if (qName.equalsIgnoreCase(Element.VICE_DIALOG_REFERRED_BY)) {
                    this.mViceDialogReferredElement.setAttributValue(Element.VICE_DIALOG_REFERRED_BY, this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_DIALOG_REFERRED_BY attribute vals = " + this.mViceDialogReferredElement.getAttributeValue(Element.VICE_DIALOG_REFERRED_BY));
                }
                this.mDoesDialogHaveSubElement = false;
            }
            if (this.mDoesRouteSetHaveSubElement) {
                if (qName.equalsIgnoreCase(Element.VICE_DIALOG_ROUTE_SET_HOP)) {
                    this.mViceDialogHopElement.setAttributValue(Element.VICE_DIALOG_ROUTE_SET_HOP, this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_DIALOG_ROUTE_SET_HOP attribute vals = " + this.mViceDialogHopElement.getAttributeValue(Element.VICE_DIALOG_ROUTE_SET_HOP));
                }
                this.mDoesRouteSetHaveSubElement = false;
            }
            if (this.mDoesDialogLocalHaveSubElement) {
                if (qName.equalsIgnoreCase("identity")) {
                    this.mViceDialogLocalIdentityElement.setAttributValue("identity", this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_DIALOG_LOCAL_IDENTITY attribute vals = " + com.qualcomm.ims.utils.Log.pii(this.mViceDialogLocalIdentityElement.getAttributeValue("identity")));
                    this.mDialogInfo.localIdentity = this.mViceDialogLocalIdentityElement.getAttributeValue("identity");
                } else if (qName.equalsIgnoreCase(Element.VICE_MEDIA_TYPE)) {
                    this.mViceDialogMediaTypeElement.setAttributValue(Element.VICE_MEDIA_TYPE, this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_MEDIA_TYPE attribute vals = " + this.mViceDialogMediaTypeElement.getAttributeValue(Element.VICE_MEDIA_TYPE));
                    this.mMediaAttribute.mediaType = this.mViceDialogMediaTypeElement.getAttributeValue(Element.VICE_MEDIA_TYPE);
                } else if (qName.equalsIgnoreCase(Element.VICE_MEDIA_DIRECTION)) {
                    this.mViceDialogMediaDirElement.setAttributValue(Element.VICE_MEDIA_DIRECTION, this.builder.toString().trim());
                    Log.d("ViceSaxXmlHandler", "VICE_MEDIA_DIRECTION attribute vals = " + this.mViceDialogMediaDirElement.getAttributeValue(Element.VICE_MEDIA_DIRECTION));
                    this.mMediaAttribute.mediaDirection = this.mViceDialogMediaDirElement.getAttributeValue(Element.VICE_MEDIA_DIRECTION);
                } else if (qName.equalsIgnoreCase(Element.VICE_MEDIA_PORT)) {
                    this.mViceDialogMediaPortElement.setAttributValue(Element.VICE_MEDIA_PORT, String.valueOf(true));
                    Log.d("ViceSaxXmlHandler", "VICE_MEDIA_PORT attribute vals = " + this.mViceDialogMediaPortElement.getAttributeValue(Element.VICE_MEDIA_PORT));
                    this.mMediaAttribute.mediaPort = this.mViceDialogMediaPortElement.getAttributeValue(Element.VICE_MEDIA_PORT);
                }
                this.mDoesDialogLocalHaveSubElement = false;
            }
            if (this.mDoesRemoteDialogHaveSubElement && qName.equalsIgnoreCase(Element.VICE_DIALOG_REMOTE)) {
                this.mDoesRemoteDialogHaveSubElement = false;
            }
            if (qName.equalsIgnoreCase(Element.VICE_MEDIA_ATTRI)) {
                this.mDialogInfo.mediaAttributes.add(this.mMediaAttribute);
            }
            if (qName.equalsIgnoreCase(Element.VICE_DIALOG)) {
                this.mDialogInfoList.add(this.mDialogInfo);
            }
            this.builder.setLength(0);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() {
        try {
            super.endDocument();
            Log.d("ViceSaxXmlHandler", "Root Tag Name:" + this.mViceDialogInfoElement.getTagName());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<ImsViceParser.DialogInfo> dumpDialogInfo() {
        List<ImsViceParser.DialogInfo> list = this.mDialogInfoList;
        if (list != null && !list.isEmpty()) {
            int size = this.mDialogInfoList.size();
            for (int i = 0; i < size; i++) {
                ImsViceParser.DialogInfo info = this.mDialogInfoList.get(i);
                Log.d("ViceSaxXmlHandler", "dumpDialogInfo[" + i + "] -> index , dialog ID = " + info.dialogId + ", exclusive = " + info.exclusive + ", direction = " + info.direction + ", state = " + info.state + ", param name = " + info.paramName + ", param val = " + info.paramVal + ", remote identity = " + com.qualcomm.ims.utils.Log.pii(info.remoteIdentity) + ", local identity = " + com.qualcomm.ims.utils.Log.pii(info.localIdentity));
                if (info.mediaAttributes != null && !info.mediaAttributes.isEmpty()) {
                    int mediaattri = info.mediaAttributes.size();
                    for (int j = 0; j < mediaattri; j++) {
                        Log.d("ViceSaxXmlHandler", "dumpDialogInfo[" + i + "] -> index , dialog media type = " + info.mediaAttributes.get(j).mediaType + ", dialog media direction = " + info.mediaAttributes.get(j).mediaDirection + ", dialog media port = " + info.mediaAttributes.get(j).mediaPort);
                    }
                }
            }
            return this.mDialogInfoList;
        }
        return null;
    }
}
