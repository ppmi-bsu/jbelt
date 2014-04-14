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

import by.bsu.belt.provider.Bee2SecurityProvider;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.algorithms.SignatureAlgorithm;
import org.apache.xml.security.exceptions.AlgorithmAlreadyRegisteredException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;


/**
 * @author $Author: coheigea $
 */
public class CreateBeeSignature {

    /** {@link org.apache.commons.logging} logging facility */
    static org.apache.commons.logging.Log log = 
        org.apache.commons.logging.LogFactory.getLog(CreateBeeSignature.class.getName());

    static {
        org.apache.xml.security.Init.init();
    }
    static {
        Security.addProvider(new Bee2SecurityProvider());


        JCEMapper.register(
                "uri:Belt",
                new JCEMapper.Algorithm("", "Belt", "MessageDigest")
        );
        JCEMapper.register(
                "uri:BignSignature",
                new JCEMapper.Algorithm("", "BignSign", "Signature")
        );

        try {
            SignatureAlgorithm.register("uri:BignSignature", BignSignatureAlgorithmSpi.class);
        } catch (AlgorithmAlreadyRegisteredException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public static Document sign(String xml, String output) throws Exception{

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));

        Document doc = db.parse(is);

        return sign(doc, output);


    }
    public static Document sign(Document doc, String output) throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Bign");
        keyPairGenerator.initialize(1024);
        KeyPair keys = keyPairGenerator.generateKeyPair();
        return sign(doc, output);
    }

    public static Document sign(Document doc, String output, KeyPair keys) throws Exception{

        File signatureFile = new File(output);

        //The BaseURI is the URI that's used to prepend to relative URIs
        String BaseURI = signatureFile.toURI().toURL().toString();
        //Create an XML Signature object from the document, BaseURI and
        //signature algorithm (in this case DSA)
        XMLSignature sig =
                new XMLSignature(doc, BaseURI, "uri:BignSignature");
        sig.addResourceResolver(new OfflineResolver());



        //Append the signature element to the root element before signing because
        //this is going to be an enveloped signature.
        //This means the signature is going to be enveloped by the document.
        //Two other possible forms are enveloping where the document is inside the
        //signature and detached where they are seperate.
        //Note that they can be mixed in 1 signature with seperate references as
        //shown below.
        doc.getDocumentElement().appendChild(sig.getElement());
        doc.appendChild(doc.createComment(" Comment after "));
        //sig.getSignedInfo().addResourceResolver(
        //    new org.apache.xml.security.samples.utils.resolver.OfflineResolver()
        //);

        {
            //create the transforms object for the Document/Reference
            Transforms transforms = new Transforms(doc);

            //First we have to strip away the signature element (it's not part of the
            //signature calculations). The enveloped transform can be used for this.
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            //Part of the signature element needs to be canonicalized. It is a kind
            //of normalizing algorithm for XML. For more information please take a
            //look at the W3C XML Digital Signature webpage.
            transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
            //Add the above Document/Reference


            sig.addDocument("", transforms, "uri:Belt");
        }

        {
            //Add in 2 external URIs. This is a detached Reference.
            //
            // When sign() is called, two network connections are made. -- well,
            // not really, as we use the OfflineResolver which acts as a proxy for
            // these two resouces ;-))
            //
            sig.addDocument("http://www.w3.org/TR/xml-stylesheet");
            sig.addDocument("http://www.nue.et-inf.uni-siegen.de/index.html");
        }

        {


            //Add in the KeyInfo for the certificate that we used the private key of
//            X509Certificate cert = getCertificate(KPair);

  //          sig.addKeyInfo(cert);
            //sig.addKeyInfo(KPair.getPublic());

            sig.getKeyInfo().add(new BignKeyValue(sig.getKeyInfo().getDocument(), keys.getPublic()));
            System.out.println("Start signing");
            sig.sign(keys.getPrivate());
            System.out.println("Finished signing");
        }

        FileOutputStream f = new FileOutputStream(signatureFile);

        XMLUtils.outputDOMc14nWithComments(doc, f);

        f.close();
        System.out.println("Wrote signature to " + BaseURI);

        return doc;

    }

  public static Document create_test_doc() throws ParserConfigurationException {
        DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();

        //XML Signature needs to be namespace aware
        dbf.setNamespaceAware(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        //Build a sample document. It will look something like:
        //<!-- Comment before -->
        //<apache:RootElement xmlns:apache="http://www.apache.org/ns/#app1">Some simple text
        //</apache:RootElement>
        //<!-- Comment after -->
        doc.appendChild(doc.createComment(" Comment before "));

        Element root =
                doc.createElementNS("http://www.apache.org/ns/#app1", "apache:RootElement");

        root.setAttributeNS(null, "attr1", "test1");
        root.setAttributeNS(null, "attr2", "test2");
        root.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:foo", "http://example.org/#foo");
        root.setAttributeNS("http://example.org/#foo", "foo:attr1", "foo's test");



        root.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:apache", "http://www.apache.org/ns/#app1");
        doc.appendChild(root);
        root.appendChild(doc.createTextNode("Some simple text\n"));
        return doc;



    }
}