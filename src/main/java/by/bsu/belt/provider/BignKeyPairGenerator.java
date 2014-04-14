package by.bsu.belt.provider;

import by.bsu.belt.Bee2Library;
import by.bsu.belt.BignParams;

import java.security.*;

/**
 * Created by mihas
 * Date: 4/12/14
 * Time: 8:10 PM
 */
public class BignKeyPairGenerator  extends KeyPairGenerator {
	SecureRandom random;

    Bee2Library.RngFunc rng = new Bee2Library.RngFunc();
    Bee2Library bee2 = Bee2Library.INSTANCE;

    BignParams bignParams = new BignParams(bee2, 128);

	public BignKeyPairGenerator() {
		super("Bign");
	}

	public void initialize(int strength, SecureRandom sr) {
		random = sr;
        bee2.bignStdParams(bignParams, "1.2.112.0.2.0.34.101.45.3.1");
        assert bee2.bignValParams(bignParams) == 0;

    }

	public KeyPair generateKeyPair() {
        assert bee2.bignValParams(bignParams) == 0;

        byte[] privKey = new byte[32];
        byte[] pubKey = new byte[64];

        bee2.bignGenKeypair(privKey, pubKey, bignParams, rng, null);

        assert bee2.bignValPubkey(bignParams, pubKey) == 0;

        BignPublicKey pub = new BignPublicKey()   ;
        BignPrivateKey priv = new BignPrivateKey() ;
		pub.bytes = pubKey;
		priv.bytes = privKey;
		return new KeyPair(pub, priv);
	}
}
