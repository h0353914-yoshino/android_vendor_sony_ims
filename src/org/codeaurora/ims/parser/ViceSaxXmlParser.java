package org.codeaurora.ims.parser;

import android.util.Log;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* JADX INFO: loaded from: classes.dex */
public class ViceSaxXmlParser {
    private static ViceSaxXmlHandler mSaxHandler;
    private static ViceSaxXmlParser mXmlparser = null;

    private ViceSaxXmlParser() {
    }

    public static synchronized Element parse(InputStream is) {
        try {
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            ViceSaxXmlHandler viceSaxXmlHandler = new ViceSaxXmlHandler();
            mSaxHandler = viceSaxXmlHandler;
            xmlReader.setContentHandler(viceSaxXmlHandler);
            InputSource inputSource = new InputSource(is);
            inputSource.setEncoding("utf-8");
            xmlReader.parse(inputSource);
        } catch (Exception ex) {
            if (ex instanceof InvalidConfVersionException) {
                Log.d("ViceSaxxParser", "Exception caught at SAXParser");
            } else if (ex instanceof ParseException) {
                Log.d("ViceSaxxParser", "InValid Format , Exception in Parser");
                ex.printStackTrace();
            }
        }
        return mSaxHandler.getConferenceInfoLatestMessage();
    }

    public static ViceSaxXmlParser getInstance() {
        if (mXmlparser == null) {
            mXmlparser = new ViceSaxXmlParser();
        }
        return mXmlparser;
    }

    public List<ImsViceParser.DialogInfo> getDialogInfo() {
        ViceSaxXmlHandler viceSaxXmlHandler = mSaxHandler;
        if (viceSaxXmlHandler != null) {
            return viceSaxXmlHandler.dumpDialogInfo();
        }
        return null;
    }
}
