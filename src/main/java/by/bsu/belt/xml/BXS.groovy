package by.bsu.belt.xml

import by.bsu.belt.provider.BignKeyPairGenerator

/**
 * Created by mihas
 * Date: 4/14/14
 * Time: 9:36 PM
 */
class BXS {

    def sign(def xml, def keys) {
        CreateBeeSignature.convertToString(
                CreateBeeSignature.sign(xml, 'last.xml', keys))
    }

    def sign(def xml) {
        sign(xml, new BignKeyPairGenerator().generateKeyPair())


    }

    def verify(def xml) {
        VerifySignature.validate(xml)
    }
}
