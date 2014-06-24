package by.bsu.belt.xml

import org.w3c.dom.Document

/**
 * Created by mihas
 * Date: 6/24/14
 * Time: 8:27 PM
 */
class Util {

    static def toDoc(xml_str) {
        javax.xml.parsers.DocumentBuilderFactory dbf =
            javax.xml.parsers.DocumentBuilderFactory.newInstance();

        dbf.setNamespaceAware(true);
        dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);

        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();

        InputStream stream = new ByteArrayInputStream(xml_str.getBytes("UTF-8"));
        Document doc = db.parse(stream);

    }

    static def toStr(doc) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        org.apache.xml.security.utils.XMLUtils.outputDOMc14nWithComments(doc, stream);

        return stream.toString();
    }
}
