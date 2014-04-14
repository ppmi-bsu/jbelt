package by.bsu.belt.provider;

import by.bsu.belt.Bee2Library;

import java.security.*;
import java.util.ArrayList;

/**
 * Created by mihas
 * Date: 4/12/14
 * Time: 7:27 PM
 */
public class BignSignature extends Signature {

    Bee2Library bee2 = Bee2Library.INSTANCE;
    Bee2Library.RngFunc rng = new Bee2Library.RngFunc();

    public BignSignature() {
        super("BignSign");
    }

    BignKey privKey = null;
    BignKey pubKey = null;

    ArrayList<Byte> data = new ArrayList<Byte>();

    @Override
    public void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        this.state = VERIFY;
        this.pubKey = (BignKey)publicKey;
    }

    @Override
    public void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        this.state = SIGN;
        this.privKey = (BignKey) privateKey;
    }

    @Override
    public void engineUpdate(byte b) throws SignatureException {
        data.add(b);
    }

    @Override
    public void engineUpdate(byte[] b, int off, int len) throws SignatureException {
        for (int i=off; i<len; i++){
            data.add(b[i]);
        }
    }

    @Override
    public byte[] engineSign() throws SignatureException {

        byte[] sig = new byte[(privKey.bignParams.l * 3)/4];

        bee2.bignSign(sig, privKey.bignParams, "1.2.112.0.2.0.34.101.31.81", Util.bytes(data), privKey.bytes, rng, null);

        return sig;
    }

    @Override
    public boolean engineVerify(byte[] sigBytes) throws SignatureException {

        assert this.pubKey != null;

        int res = bee2.bignVerify(pubKey.bignParams, "1.2.112.0.2.0.34.101.31.81", Util.bytes(data), sigBytes, pubKey.bytes);

        return res==0;

    }

    @Override
    public void engineSetParameter(String param, Object value) throws InvalidParameterException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object engineGetParameter(String param) throws InvalidParameterException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
