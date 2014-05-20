package by.bsu.belt.provider;

import javax.crypto.KeyGenerator;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by mihas
 * Date: 4/21/14
 * Time: 10:22 AM
 */
public class BeltKeyGenerator extends KeyGeneratorSpi {

    @Override
    protected void engineInit(SecureRandom secureRandom) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void engineInit(int i, SecureRandom secureRandom) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected SecretKey engineGenerateKey() {
        return new SecretKeySpec(new SecureRandom().generateSeed(32), "Belt");
    }
}
