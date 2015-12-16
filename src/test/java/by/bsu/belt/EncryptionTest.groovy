package by.bsu.belt

import by.bsu.belt.provider.Bee2SecurityProvider
import by.bsu.belt.xml.BXS
import org.apache.xml.security.algorithms.JCEMapper

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security

/**
 * Created by mihas
 * Date: 4/21/14
 * Time: 8:19 AM
 *
 */

class EncryptionTest extends GroovyTestCase{

    def xml_string = '''<?xml version="1.0" encoding="UTF-8"?>
        <PurchaseOrder>
        <Item number="130046593231">
        <Description>Video Game</Description>
          <Price>10.29</Price>
        </Item>
         <Buyer id="8492340">
          <Name>My Name</Name>
        <Address>
        <Street>One Network Drive</Street>
           <Town>Burlington</Town>
        <State>MA</State>
           <Country>United States</Country>
        <PostalCode>01803</PostalCode>
          </Address>
        </Buyer>
        </PurchaseOrder>
'''


    public void testBXS() {
        Security.addProvider(new Bee2SecurityProvider());

        def bxs = new BXS()

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Bign");
        keyPairGenerator.initialize(128);
        KeyPair keys = keyPairGenerator.generateKeyPair();

        def encrypted = bxs.enc(xml_string, keys.getPublic().bytes)
        def decrypted = bxs.dec(encrypted, keys.private.bytes)
        //assertEquals(xml_string, decrypted)
    }

}
