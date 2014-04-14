package by.bsu.belt.xml;

import org.apache.xml.security.algorithms.SignatureAlgorithmSpi;
import org.apache.xml.security.signature.XMLSignatureException;

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by mihas
 * Date: 4/12/14
 * Time: 7:41 PM
 */
public class BignSignatureAlgorithmSpi extends SignatureAlgorithmSpi {


    private Signature signatureAlgorithm = null;

    public BignSignatureAlgorithmSpi() throws Exception{
        signatureAlgorithm = Signature.getInstance("BignSign");
    }

    @Override
    protected String engineGetURI() {
        return "uri:BignSignature";
    }

    @Override
    protected String engineGetJCEAlgorithmString() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String engineGetJCEProviderName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineUpdate(byte[] input) throws XMLSignatureException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineUpdate(byte input) throws XMLSignatureException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineUpdate(byte[] buf, int offset, int len) throws XMLSignatureException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineInitSign(Key signingKey) throws XMLSignatureException {
        try {
            signatureAlgorithm.initSign((PrivateKey) signingKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    protected void engineInitSign(Key signingKey, SecureRandom secureRandom) throws XMLSignatureException {
        this.engineInitSign(signingKey);
    }

    @Override
    protected void engineInitSign(Key signingKey, AlgorithmParameterSpec algorithmParameterSpec) throws XMLSignatureException {
        this.engineInitSign(signingKey);
    }

    @Override
    protected byte[] engineSign() throws XMLSignatureException {
        try {
            return signatureAlgorithm.sign();
        } catch (SignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }

    }

    @Override
    protected void engineInitVerify(Key verificationKey) throws XMLSignatureException {
        try {
            signatureAlgorithm.initVerify((PublicKey) verificationKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    protected boolean engineVerify(byte[] signature) throws XMLSignatureException {
        try {
            return signatureAlgorithm.verify(signature);
        } catch (SignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
    }

    @Override
    protected void engineSetParameter(AlgorithmParameterSpec params) throws XMLSignatureException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineSetHMACOutputLength(int HMACOutputLength) throws XMLSignatureException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
