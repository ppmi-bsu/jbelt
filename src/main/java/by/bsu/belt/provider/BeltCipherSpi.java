package by.bsu.belt.provider;

import by.bsu.belt.Bee2Library;

import javax.crypto.*;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;

/**
 * Created by mihas
 * Date: 5/17/14
 * Time: 5:13 PM
 */
public class BeltCipherSpi extends CipherSpi{

    ArrayList<Byte> data = new ArrayList<Byte>();
    Key key = null;
    int mode;

    @Override
    protected void engineSetMode(String s) throws NoSuchAlgorithmException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineSetPadding(String s) throws NoSuchPaddingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int engineGetBlockSize() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int engineGetOutputSize(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected byte[] engineGetIV() {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineInit(int mode, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        assert key.getAlgorithm().equals("Belt");
        this.key = key;
        this.mode = mode;
    }


    @Override
    protected void engineInit(int mode, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        assert key.getAlgorithm().equals("Belt");
        this.key = key;
        this.mode = mode;
    }

    @Override
    protected void engineInit(int mode, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        assert key.getAlgorithm().equals("Belt");
        this.key = key;
        this.mode = mode;
    }

    @Override
    protected byte[] engineUpdate(byte[] bytes, int offset, int len) {

        for (int i=offset;  i<offset+len;i++)
            data.add(bytes[i]);
        return null;
    }

    @Override
    protected int engineUpdate(byte[] input, int offset, int len, byte[] output, int out_offset) throws ShortBufferException {
        throw new RuntimeException("AAAAAAA");
    }

    @Override
    protected byte[] engineDoFinal(byte[] bytes, int offset, int len) throws IllegalBlockSizeException, BadPaddingException {
        byte[] result = new byte[len];
        Bee2Library bee2 = Bee2Library.INSTANCE;
        if(mode == Cipher.ENCRYPT_MODE || mode == Cipher.WRAP_MODE)
            bee2.beltECBEncr(result, bytes, len, key.getEncoded(), 32);
        else if(mode==Cipher.DECRYPT_MODE || mode == Cipher.UNWRAP_MODE)
            bee2.beltECBDecr(result, bytes, len, key.getEncoded(), 32);
        else
            throw new RuntimeException("Unsupported mode");
        return result;
    }

    @Override
    protected int engineDoFinal(
            byte[]  input,
            int     inputOffset,
            int     inputLen,
            byte[]  output,
            int     outputOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        byte[] result = engineDoFinal(input, inputOffset, inputLen);
        System.arraycopy(result, 0, output, outputOffset, inputLen);
        return 0;
    }
}
