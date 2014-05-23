package by.bsu.belt

import by.bsu.belt.provider.Bee2SecurityProvider
import by.bsu.belt.xml.Decrypter
import by.bsu.belt.xml.Encrypter
import org.apache.xml.security.algorithms.JCEMapper

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

        String algorithmURI = "urn:oid:1.2.112.0.2.0.34.101.45.12-bign-wrap";
        String encryptionAlgorithmURI = "urn:oid:1.2.112.0.2.0.34.101.45.12-belt-enc";

        JCEMapper.register(algorithmURI, new JCEMapper.Algorithm("", "Bign", "KeyTransport"));
        JCEMapper.register(encryptionAlgorithmURI, new JCEMapper.Algorithm("", "Belt", "BlockEncryption"));



        Encrypter.encrypt(keys.getPublic())
        Decrypter.decrypt(keys.getPrivate())
    }

}
