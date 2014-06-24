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
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.security.Key;
import java.security.PublicKey;

/**
 * Created by mihas
 * Date: 4/13/14
 * Time: 11:43 AM
 */

public class BignKeyValue extends KeyValue {

    public final static String NAMESPACE_PREFIX = "bign";

    public final static String BIGN_KEY_VALUE_TAG = "BignKeyValue";
    public final static String DOMAIN_PARAMS_TAG = "DomainParameters";
    public final static String NAMED_CURVE_TAG = "NamedCurve";

    public BignKeyValue(Document doc, Element unknownKeyValue) {
        super(doc, unknownKeyValue);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public BignKeyValue(Document doc, PublicKey pk) {
        super(doc, pk);

        if (pk instanceof BignKey) {
            XMLUtils.setDsPrefix(NAMESPACE_PREFIX);
            try {
                BignKeyValueContent bign = new BignKeyValueContent(this.doc, pk);
                this.constructionElement.appendChild(bign.getElement());
                XMLUtils.addReturnToElement(this.constructionElement);
            }finally {
                XMLUtils.setDsPrefix("ds");
            }

        }

    }

    public BignKeyValue(Element element, String BaseURI) throws XMLSecurityException {
        super(element, BaseURI);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static class BignKeyValueContent extends SignatureElementProxy implements KeyValueContent {

        @Override
        public String getBaseNamespace() {
            return Identifiers.BIGN_NAMESPACE_URI;
        }

        @Override
        public String getBaseLocalName() {

            return  BignKeyValue.BIGN_KEY_VALUE_TAG;
        }

        public BignKeyValueContent(Element element, String BaseURI) throws XMLSecurityException {
            super(element, BaseURI);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public PublicKey getPublicKey() throws XMLSecurityException {
            BignPublicKey key = new BignPublicKey();
            key.setBytes(this.getBytesFromChildElement("PublicKey", Identifiers.BIGN_NAMESPACE_URI));
            return key;
        }

        public BignKeyValueContent(Document doc, Key key) throws IllegalArgumentException {



            super(doc);

            if (key instanceof BignPublicKey) {
                XMLUtils.addReturnToElement(this.constructionElement);


                this.addBase64Element(((BignPublicKey) key).bytes, "PublicKey");
                Element domainParams = this.doc.createElementNS(Identifiers.BIGN_NAMESPACE_URI, DOMAIN_PARAMS_TAG);
                this.constructionElement.appendChild(domainParams);
                Element namedCurve = this.doc.createElementNS(Identifiers.BIGN_NAMESPACE_URI, NAMED_CURVE_TAG);
                domainParams.appendChild(namedCurve);

                Text t = this.doc.createTextNode(getCurveName(((BignPublicKey) key).bignParams));
                namedCurve.appendChild(t);
                XMLUtils.addReturnToElement(this.constructionElement);

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
                        XMLUtils.selectNode(element.getFirstChild(), Identifiers.BIGN_NAMESPACE_URI, BIGN_KEY_VALUE_TAG, 0);
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

    static String getCurveName(BignParams bignParams) {
        switch (bignParams.l) {
            case 128:
                return Identifiers.NAMED_CURVE_256;
            case 192:
                return Identifiers.NAMED_CURVE_384;
            case 256:
                return Identifiers.NAMED_CURVE_512;
            default:
                throw new IllegalArgumentException("Wrong bign params, level is " + bignParams.l);
        }
    }

}