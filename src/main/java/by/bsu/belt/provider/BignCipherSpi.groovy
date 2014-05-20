package by.bsu.belt.provider

import by.bsu.belt.Bee2Library

import javax.crypto.BadPaddingException
import javax.crypto.CipherSpi
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.ShortBufferException
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
        return 0  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int engineGetOutputSize(int i) {
        return 0  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected byte[] engineGetIV() {
        return new byte[0]  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineInit(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected byte[] engineUpdate(byte[] bytes, int i, int i1) {
        return new byte[0]  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException {
        return 0  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected byte[] engineDoFinal(byte[] bytes, int offset, int len) throws IllegalBlockSizeException, BadPaddingException {
        def bee2 = Bee2Library.INSTANCE;
        def result = byte[bytes.length]
        bee2.bi
        return
    }

    @Override
    protected int engineDoFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return 0  //To change body of implemented methods use File | Settings | File Templates.
    }
}
