package by.bsu.belt

import by.bsu.belt.xml.CreateBeeSignature
import by.bsu.belt.xml.VerifySignature
import junit.framework.TestCase

/**
 * Created by mihas
 * Date: 4/13/14
 * Time: 7:04 PM
 */
class XmlDsigTest extends TestCase{


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

    public void test() {
        def doc = CreateBeeSignature.sign(xml_string, 'apache_signed_test.xml')
        assertTrue(VerifySignature.validate(doc))

        System.out.println(CreateBeeSignature.convertToString(doc))
    }


}
