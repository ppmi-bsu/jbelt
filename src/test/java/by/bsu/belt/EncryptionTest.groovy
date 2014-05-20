package by.bsu.belt

import by.bsu.belt.provider.Bee2SecurityProvider
import by.bsu.belt.xml.Decrypter
import by.bsu.belt.xml.Encrypter

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security

/**
 * Created by mihas
 * Date: 4/21/14
 * Time: 8:19 AM
 */
class EncryptionTest extends GroovyTestCase{

    public void test() {
        Security.addProvider(new Bee2SecurityProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Bign");
        keyPairGenerator.initialize(128);
        KeyPair keys = keyPairGenerator.generateKeyPair();

        Encrypter.encrypt(keys.getPublic())
        Decrypter.main()
    }

}
