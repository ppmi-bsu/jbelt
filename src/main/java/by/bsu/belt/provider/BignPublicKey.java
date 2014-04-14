package by.bsu.belt.provider;

import by.bsu.belt.BignParams;

import java.security.PublicKey;

/**
 * Created by mihas
 * Date: 4/13/14
 * Time: 2:13 AM
 */
public class BignPublicKey extends BignKey implements PublicKey{

    @Override
    public void setBytes(byte[] bytes) {
        super.setBytes(bytes);
        bignParams = new BignParams(bytes.length * 2);
    }

    public BignPublicKey() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public BignPublicKey(byte[] bytes) {
        super(bytes);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
