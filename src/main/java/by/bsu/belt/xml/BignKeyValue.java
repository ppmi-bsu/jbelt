package by.bsu.belt.xml;

import by.bsu.belt.BignParams;
import by.bsu.belt.provider.BignKey;
import by.bsu.belt.provider.BignPublicKey;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.KeyValue;
import org.apache.xml.security.keys.content.keyvalues.KeyValueContent;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.security.Key;
import java.security.PublicKey;

/**
 * Created by mihas
 * Date: 4/13/14
 * Time: 11:43 AM
 */

public class BignKeyValue extends KeyValue {

    public final static String BIGN_KEY_VALUE_TAG = "BignKeyValue";

    public BignKeyValue(Document doc, Element unknownKeyValue) {
        super(doc, unknownKeyValue);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public BignKeyValue(Document doc, PublicKey pk) {
        super(doc, pk);

        if (pk instanceof BignKey) {
            BignKeyValueContent bign = new BignKeyValueContent(this.doc, pk);
            this.constructionElement.appendChild(bign.getElement());
            XMLUtils.addReturnToElement(this.constructionElement);
        }

    }

    public BignKeyValue(Element element, String BaseURI) throws XMLSecurityException {
        super(element, BaseURI);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static class BignKeyValueContent extends SignatureElementProxy implements KeyValueContent {
        @Override
        public String getBaseLocalName() {

            return BignKeyValue.BIGN_KEY_VALUE_TAG;
        }

        public BignKeyValueContent(Element element, String BaseURI) throws XMLSecurityException {
            super(element, BaseURI);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public PublicKey getPublicKey() throws XMLSecurityException {
            BignPublicKey key = new BignPublicKey();
            key.setBytes(this.getBytesFromChildElement("PublicKey", "http://www.w3.org/2000/09/xmldsig#"));
            return key;
        }

        public BignKeyValueContent(Document doc, Key key) throws IllegalArgumentException {

            super(doc);

            if (key instanceof BignPublicKey) {
                XMLUtils.addReturnToElement(this.constructionElement);
                this.addBase64Element(((BignPublicKey) key).bytes, "PublicKey");
                this.addTextElement(BignParams.getCurveName(((BignPublicKey) key).bignParams.l),"NamedCurve");
            }else {
                throw new IllegalArgumentException();
            }

        }
    }

    public static class BignKeyValueResolver extends KeyResolverSpi {
        @Override
        public boolean engineCanResolve(Element element, String baseURI, StorageResolver storage) {
            return false;  // can resolve X509?
        }

        @Override
        public PublicKey engineLookupAndResolvePublicKey(Element element, String baseURI, StorageResolver storage) throws KeyResolverException {
            if (element == null) {
                return null;
            }
            Element bignKeyElement = null;
            boolean isKeyValue =
                    XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYVALUE);
            if (isKeyValue) {
                bignKeyElement =
                        XMLUtils.selectDsNode(element.getFirstChild(), BIGN_KEY_VALUE_TAG, 0);
            } else if (XMLUtils.elementIsInSignatureSpace(element, BIGN_KEY_VALUE_TAG)) {
                // this trick is needed to allow the RetrievalMethodResolver to eat a
                // ds:DSAKeyValue directly (without KeyValue)
                bignKeyElement = element;
            }

            if (bignKeyElement == null) {
                return null;
            }

            try {
                BignKeyValueContent bignKeyValueContent = new BignKeyValueContent(bignKeyElement, baseURI);
                PublicKey pk = bignKeyValueContent.getPublicKey();

                return pk;
            } catch (XMLSecurityException ex) {
                if (log.isDebugEnabled()) {
                    log.debug(ex);
                }
                //do nothing
            }

            return null;
        }
    }

}