package org.codeaurora.ims.parser;

import android.util.Log;
import java.io.InputStream;
import java.text.ParseException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* JADX INFO: loaded from: classes.dex */
public class SAXXMLParser {
    private static SAXXMLHandler sSaxHandler;
    private static SAXXMLParser sXmlparser = null;

    public static synchronized Element parse(InputStream is) {
        try {
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SAXXMLHandler sAXXMLHandler = new SAXXMLHandler();
            sSaxHandler = sAXXMLHandler;
            xmlReader.setContentHandler(sAXXMLHandler);
            InputSource inputSource = new InputSource(is);
            inputSource.setEncoding("utf-8");
            xmlReader.parse(inputSource);
        } catch (Exception ex) {
            if (ex instanceof InvalidConfVersionException) {
                Log.d("SAXXMLHandler", "Exception caught at SAXParser");
            } else if (ex instanceof ParseException) {
                Log.d("SAXXMLHandler", "InValid Format , Exception in Parser");
                ex.printStackTrace();
            }
        }
        return sSaxHandler.getConferenceInfoLatestMessage();
    }

    public static SAXXMLParser getSAXXMLParser() {
        if (sXmlparser == null) {
            sXmlparser = new SAXXMLParser();
        }
        return sXmlparser;
    }

    public Element getConfInfoHandle() {
        return sSaxHandler.getConferenceInfoLatestMessage();
    }
}
