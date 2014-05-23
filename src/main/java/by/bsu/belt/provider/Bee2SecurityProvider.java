package by.bsu.belt.provider;

import java.security.Provider;

/**
 * Created by mihas
 * Date: 4/12/14
 * Time: 6:49 PM
 */
public class Bee2SecurityProvider extends Provider {

    public Bee2SecurityProvider(){
        super("Bee2", 1.0, "Bee2 Security Provider v1.0");

        put("MessageDigest.Belt", "by.bsu.belt.provider.BeltMessageDigest");
        put("Signature.BignSign", "by.bsu.belt.provider.BignSignature");
        put("KeyPairGenerator.Bign", "by.bsu.belt.provider.BignKeyPairGenerator");
        put("Cipher.Belt", "by.bsu.belt.provider.BeltCipherSpi");
        put("Cipher.Bign", "by.bsu.belt.provider.BignCipherSpi");
        put("KeyGenerator.Belt", "by.bsu.belt.provider.BeltKeyGenerator");


    }
}
