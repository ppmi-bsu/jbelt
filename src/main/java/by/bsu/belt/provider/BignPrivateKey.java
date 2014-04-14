package by.bsu.belt.provider;

import by.bsu.belt.BignParams;

import java.security.PrivateKey;

/**
 * Created by mihas
 * Date: 4/13/14
 * Time: 2:13 AM
 */
public class BignPrivateKey extends BignKey implements PrivateKey {

    public BignPrivateKey() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public BignPrivateKey(byte[] bytes) {
        super(bytes);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void setBytes(byte[] bytes) {
        super.setBytes(bytes);
        bignParams = new BignParams(bytes.length * 4);
    }

}
