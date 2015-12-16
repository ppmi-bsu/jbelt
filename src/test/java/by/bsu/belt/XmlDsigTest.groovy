package by.bsu.belt

import by.bsu.belt.provider.BignKeyPairGenerator
import by.bsu.belt.xml.BXS
import by.bsu.belt.xml.CreateBeeSignature
import by.bsu.belt.xml.Util
import by.bsu.belt.xml.VerifySignature
import junit.framework.TestCase

/**
 * Created by mihas
 * Date: 4/13/14
 * Time: 7:04 PM
 */




class XmlDsigTest extends TestCase{
    def test_xml = '''<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>jbelt</groupId>
    <artifactId>jbelt</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.sun.jna</groupId>
            <artifactId>jna</artifactId>
            <version>3.0.9</version>
        </dependency>
        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcprov-jdk16</artifactId>
            <version>1.46</version>
        </dependency>
        <dependency>
            <groupId>org.python</groupId>
            <artifactId>jython</artifactId>
            <version>2.5.4-rc1</version>
        </dependency>
         <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.1.3</version>
        </dependency>
         <dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy-all</artifactId>
            <version>2.2.2</version>
        </dependency>
        <dependency>
            <groupId>xalan</groupId>
            <artifactId>xalan</artifactId>
            <version>2.7.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.santuario</groupId>
            <artifactId>xmlsec</artifactId>
            <version>1.5.6</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.5</source>
                    <target>1.5</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
'''

    def xml_string = '''<?xml version="1.0" encoding="UTF-8"?>
        <PurchaseOrder>
        <Item number="130046593231">
        <Description>Video Game</Description>
          <Price>10.29</Price>
        </Item>
         <Buyer id="8492340">
          <Name>My Name</Name>
        <Address>
        <Street> Nezalezhnasti </Street>
           <City>Minsk</City>
           <Country>Belarus</Country>
        <PostalCode>01803</PostalCode>
          </Address>
        </Buyer>
        </PurchaseOrder>
    '''

    public void test() {
        def doc = CreateBeeSignature.sign(xml_string, 'apache_signed_test.xml')
        assertTrue(VerifySignature.validate(doc))

        System.out.println(Util.toStr(doc))
    }

    public void test_strings() {
        for (str in [test_xml, xml_string]) {
            def xml = Util.toStr(CreateBeeSignature.sign(test_xml, 'apache_signed_test.xml'))
            System.out.println(xml)
            assertTrue(VerifySignature.validate(xml))
        }

    }

    public void test_BXS() {
        def bxs = new BXS()
        assertTrue(bxs.verify(bxs.sign(xml_string)))
        assertTrue(bxs.verify(bxs.sign(test_xml)))
    }

    public void test_with_keys() {
        def bxs = new BXS()
        assertTrue(bxs.verify(bxs.sign(xml_string, new BignKeyPairGenerator().generateKeyPair())))
    }


}
