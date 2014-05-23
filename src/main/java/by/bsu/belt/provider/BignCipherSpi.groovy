package by.bsu.belt.provider

import by.bsu.belt.Bee2Library
import by.bsu.belt.BignParams
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.CipherSpi
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.ShortBufferException
import javax.crypto.spec.SecretKeySpec
import java.security.AlgorithmParameters
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec

/**
 * Created by mihas
 * Date: 5/19/14
 * Time: 1:40 PM
 */
class BignCipherSpi extends CipherSpi{

    def data = [];

    Key key = null;
    int mode;

    @Override
    protected byte[] engineWrap(Key symkey) throws IllegalBlockSizeException, InvalidKeyException {
        assert key instanceof BignPublicKey
        assert symkey.getEncoded().length == 32
        def bee2 = Bee2Library.INSTANCE;
        byte[] wrapped = new byte[32 + 16 + (((BignKey)key).bignParams.l)/ 4]
        bee2.bignKeyWrap(wrapped, ((BignPublicKey)key).bignParams, symkey.getEncoded(), 32, "1234567890123456".getBytes(), key.bytes, new Bee2Library.RngFunc(), null)
        return wrapped;
    }

    @Override
    protected Key engineUnwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType) throws InvalidKeyException, NoSuchAlgorithmException {
        assert key instanceof BignPrivateKey
        assert wrappedKey.length == 32 + 16 + ((BignKey)key).bignParams.l/ 4;

        def bee2 = Bee2Library.INSTANCE;
        byte[] unwrapped = new byte[32]
        bee2.bignKeyUnwrap(unwrapped, ((BignPrivateKey)key).bignParams, wrappedKey, wrappedKey.length, "1234567890123456".getBytes(), key.bytes);

        key = new SecretKeySpec(unwrapped, "Belt");
        return key;

    }

    @Override
    protected void engineSetMode(String s) throws NoSuchAlgorithmException {
        throw new NotImplementedException()
    }

    @Override
    protected void engineSetPadding(String s) throws NoSuchPaddingException {
        throw new NotImplementedException()
    }

    @Override
    protected int engineGetBlockSize() {
        throw new NotImplementedException()
    }

    @Override
    protected int engineGetOutputSize(int i) {
        throw new NotImplementedException()
    }

    @Override
    protected byte[] engineGetIV() {
        throw new NotImplementedException()
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineInit(int mode, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        assert key.getAlgorithm().equals("Bign");
        this.key = key;
        this.mode = mode;
    }


    @Override
    protected void engineInit(int mode, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        assert key.getAlgorithm().equals("Bign");
        this.key = key;
        this.mode = mode;
    }

    @Override
    protected void engineInit(int mode, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        assert key.getAlgorithm().equals("Bign");
        this.key = key;
        this.mode = mode;
    }

    @Override
    protected byte[] engineUpdate(byte[] bytes, int i, int i1) {
        throw new NotImplementedException()
    }

    @Override
    protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException {
        throw new NotImplementedException()
    }

    @Override
    protected byte[] engineDoFinal(byte[] bytes, int offset, int len) throws IllegalBlockSizeException, BadPaddingException {
        if (mode==Cipher.WRAP_MODE) {
            assert key instanceof BignPublicKey
            assert bytes.length == 32
            def bee2 = Bee2Library.INSTANCE;
            byte[] wrapped = new byte[80]
            bee2.bignKeyWrap(wrapped, ((BignPublicKey)key).bignParams, bytes, 32, "1234567890123456".getBytes(), key, new Bee2Library.RngFunc(), null)
            return wrapped;
        } else if (mode==Cipher.UNWRAP_MODE) {
            assert key instanceof BignPrivateKey
            assert bytes.length == 80
            def bee2 = Bee2Library.INSTANCE;
            byte[] unwrapped = new byte[32]
            bee2.bignKeyUnwrap(unwrapped, ((BignPrivateKey)key).bignParams, bytes, 80, "1234567890123456".getBytes(), key);
            return unwrapped;

        }
        throw new NotImplementedException()
    }

    @Override
    protected int engineDoFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        throw new NotImplementedException()
    }
}
