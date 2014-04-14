/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package by.bsu.belt.xml;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.keyresolver.KeyResolver;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * @author $Author: coheigea $
 */
public class VerifySignature {
    
    static {
        org.apache.xml.security.Init.init();
        KeyResolver.register(new BignKeyValue.BignKeyValueResolver(), true);

    }

    public static void validate(String signatureFileName) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, XMLSecurityException {

        boolean schemaValidate = false;
        final String signatureSchemaFile = "xmldsig-core-schema.xsd";

        if (schemaValidate) {
            System.out.println("We do schema-validation");
        }

        javax.xml.parsers.DocumentBuilderFactory dbf =
            javax.xml.parsers.DocumentBuilderFactory.newInstance();

        if (schemaValidate) {
            dbf.setAttribute("http://apache.org/xml/features/validation/schema",
                             Boolean.TRUE);
            dbf.setAttribute("http://apache.org/xml/features/dom/defer-node-expansion",
                             Boolean.TRUE);
            dbf.setValidating(true);
            dbf.setAttribute("http://xml.org/sax/features/validation",
                             Boolean.TRUE);
        }

        dbf.setNamespaceAware(true);
        dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);

        if (schemaValidate) {
            dbf.setAttribute("http://apache.org/xml/properties/schema/external-schemaLocation",
                             Constants.SignatureSpecNS + " " + signatureSchemaFile);
        }

        File f = new File(signatureFileName);
        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream(f));

        System.out.println("Try to verify " + f.toURI().toURL().toString());



        db.setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());

        if (schemaValidate) {
            db.setEntityResolver(new org.xml.sax.EntityResolver() {

                public org.xml.sax.InputSource resolveEntity(
                    String publicId, String systemId
                ) throws SAXException {

                    if (systemId.endsWith("xmldsig-core-schema.xsd")) {
                        try {
                            return new org.xml.sax.InputSource(
                                new FileInputStream(signatureSchemaFile));
                        } catch (FileNotFoundException ex) {
                            throw new SAXException(ex);
                        }
                    } else {
                        return null;
                    }
                }
            });
        }

        validate(doc);
    }
    public static boolean validate(Document doc) throws XPathExpressionException, XMLSecurityException {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        xpath.setNamespaceContext(new DSNamespaceContext());

        String expression = "//ds:Signature[1]";
        Element sigElement =
            (Element) xpath.evaluate(expression, doc, XPathConstants.NODE);
        XMLSignature signature =
            new XMLSignature(sigElement, "");

        signature.addResourceResolver(new OfflineResolver());

        KeyInfo ki = signature.getKeyInfo();

        if (ki != null) {
            if (ki.containsX509Data()) {
                System.out.println("Could find a X509Data element in the KeyInfo");
            }

            X509Certificate cert = signature.getKeyInfo().getX509Certificate();

            if (cert != null) {
                System.out.println("The XML signature is "
                                   + (signature.checkSignatureValue(cert)
                                       ? "valid (good)"  : "invalid !!!!! (bad)"));
            } else {
                System.out.println("Did not find a Certificate");


                PublicKey pk = signature.getKeyInfo().getPublicKey();

                if (pk != null) {
                    boolean is_valid = signature.checkSignatureValue(pk);
                    System.out.println("The XML signature in file is "
                                       + (is_valid
                                           ? "valid (good)" : "invalid !!!!! (bad)"));
                    return is_valid;
                } else {
                    System.out.println(
                        "Did not find a public key, so I can't check the signature");
                }
            }
        } else {
            System.out.println("Did not find a KeyInfo");
        }

        return false;
    }

}
