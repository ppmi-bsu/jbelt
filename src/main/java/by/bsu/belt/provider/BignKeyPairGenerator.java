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

    BignParams bignParams;

	public BignKeyPairGenerator() {
		super("Bign");
        initialize(128);
	}

	public void initialize(int level, SecureRandom sr) {
		random = sr;
        bignParams = new BignParams(level);
    }

	public KeyPair generateKeyPair() {
        assert BignParams.is_valid(bignParams);

        byte[] privKey = new byte[bignParams.l/4];
        byte[] pubKey = new byte[bignParams.l/2];

        Bee2Library.INSTANCE.bignGenKeypair(privKey, pubKey, bignParams, rng, null);



        BignPublicKey pub = new BignPublicKey();
        BignPrivateKey priv = new BignPrivateKey() ;
		pub.setBytes(pubKey);
		priv.setBytes(privKey);

        assert isValid(pub);

		return new KeyPair(pub, priv);
	}

    public static boolean isValid(BignPublicKey pubKey) {
        return Bee2Library.INSTANCE.bignValPubkey(pubKey.bignParams, pubKey.bytes) == 0;
    }

    public static KeyPair calcKeyPair(byte[] privateKeyBytes) {

        BignPrivateKey priv = new BignPrivateKey(privateKeyBytes);
        return calcKeyPair(priv);

    }

    public static KeyPair calcKeyPair(BignPrivateKey privateKey) {
        byte[] calcPubKey = new byte[privateKey.bignParams.l / 2];

        Bee2Library.INSTANCE.bignCalcPubkey(calcPubKey, privateKey.bignParams, privateKey.bytes);

        BignPublicKey publicKey = new BignPublicKey(calcPubKey);
        assert isValid(publicKey);
        return new KeyPair(publicKey, privateKey);
    }
}
