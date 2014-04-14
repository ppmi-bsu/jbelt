package by.bsu.belt.xml

import by.bsu.belt.provider.BignKeyPairGenerator
import org.w3c.dom.Document
import org.xml.sax.InputSource

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by mihas
 * Date: 4/14/14
 * Time: 9:36 PM
 */
class BXS {

    static def toDoc(def xml) {

        def dbf = DocumentBuilderFactory.newInstance()

        dbf.setNamespaceAware(true);

        def db = dbf.newDocumentBuilder();
        def is = new InputSource();
        is.setCharacterStream(new StringReader(xml));

        db.parse(is);
    }

    def sign(def xml, def keys) {
        CreateBeeSignature.convertToString(
                CreateBeeSignature.sign(toDoc(xml), 'last.xml', keys))
    }

    def sign(def xml) {
        sign(xml, new BignKeyPairGenerator().generateKeyPair())


    }

    def verify(def xml) {
        VerifySignature.validate(xml)
    }
}
