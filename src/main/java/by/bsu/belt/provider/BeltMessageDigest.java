package by.bsu.belt.provider;

import by.bsu.belt.Bee2Library;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: mihas
 * Date: 4/12/14
 * Time: 6:53 PM
 */
public class BeltMessageDigest extends MessageDigest implements Cloneable {

    ArrayList<Byte> data = new ArrayList<Byte>();

    public BeltMessageDigest() {
        super("Belt");
    }

    @Override
    protected void engineUpdate(byte input) {
        data.add(input);
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        for (byte b: input)
            data.add(b);
    }

    @Override
    protected byte[] engineDigest() {

        byte[] bytes = Util.bytes(data);

        Bee2Library bee2 = Bee2Library.INSTANCE;
        byte[] hash = new byte[32];
        bee2.beltHash(hash, new String(bytes), bytes.length);
        return hash;
    }

    @Override
    protected void engineReset() {
        data.clear();
    }

    @Override
    protected int engineGetDigestLength() {
        return 32;
    }
}
