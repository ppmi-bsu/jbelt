package by.bsu.belt;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testWrap() {

        Bee2Library bee2 = Bee2Library.INSTANCE;
        Bee2Library.RngFunc rng = new Bee2Library.RngFunc();
        int[] levels = { 128, 192, 256};

        for(int level: levels ) {

            BignParams bignParams = new BignParams(level);

            byte[] symkey = "12345678123456781234567812345678".getBytes();
            byte[] privKey = new byte[level/4];
            byte[] pubKey = new byte[level/2];

            assertEquals(bee2.bignGenKeypair(privKey, pubKey, bignParams, rng, null), 0);

            int token_len = 32 + 16 + level / 4;
            byte[] wrapped = new byte[token_len];

            byte[] unwrapped = new byte[32];
            assertEquals(0, bee2.bignKeyWrap(wrapped, bignParams, symkey, 32, "1234567890123456".getBytes(), pubKey, rng, null));
            assertEquals(0, bee2.bignKeyUnwrap(unwrapped, bignParams, wrapped, token_len, "1234567890123456".getBytes(), privKey));
            assertEquals(new String(symkey), new String(unwrapped));
        }
    }

    /**
     * Rigourous Test :-)
     */
    public void testBeltEnc() {
        Bee2Library bee2 = Bee2Library.INSTANCE;


        int message_lenght = new Random().nextInt(50);
        byte[] encrypted = new byte[message_lenght];
        byte[] decrypted = new byte[message_lenght];
        byte[] key = "12345678123456781234567812345678".getBytes();
        assertEquals(key.length, 32);
        assertEquals(0, bee2.beltECBEncr(encrypted, "1234567890121234567890121234567890121234567890121".getBytes(), message_lenght, key, 32));
        System.out.println(Arrays.toString(encrypted));
        assertEquals(0, bee2.beltECBDecr(decrypted, encrypted, message_lenght, key, 32));
        System.out.println(Arrays.toString(decrypted));
    }

    public void testKeysBee2() {
        Bee2Library.RngFunc rng = new Bee2Library.RngFunc();
        Bee2Library bee2 = Bee2Library.INSTANCE;


        int level = 128;
        BignParams bignParams = new BignParams(level);
        assertEquals(bignParams.l, level);
        assertTrue(BignParams.is_valid(bignParams));

        byte[] privKey = new byte[level/4];
        byte[] pubKey = new byte[level/2];

        assertEquals(bee2.bignGenKeypair(privKey, pubKey, bignParams, rng, null), 0);
        assertEquals(bee2.bignValPubkey(bignParams, pubKey), 0);

        byte[] calcPubKey = new byte[level/2];

        bee2.bignCalcPubkey(calcPubKey, bignParams, privKey);
        System.out.println(Arrays.toString(pubKey));
        System.out.println(Arrays.toString(calcPubKey));


        assertEquals(new BigInteger(calcPubKey), new BigInteger(pubKey));



    }

    public void testBee2()
    {
        Bee2Library.RngFunc rng = new Bee2Library.RngFunc();
        Bee2Library bee2 = Bee2Library.INSTANCE;
        assertNotNull(bee2);
        int[] levels = { 128, 192, 256};
        for(int level: levels ) {
            BignParams bignParams = new BignParams(level);
            assertEquals(bignParams.l, level);
            assertTrue(BignParams.is_valid(bignParams));

            byte[] privKey = new byte[level/4];
            byte[] pubKey = new byte[level/2];

            assertEquals(bee2.bignGenKeypair(privKey, pubKey, bignParams, rng, null), 0);
            assertEquals(bee2.bignValPubkey(bignParams, pubKey), 0);

            byte[] hash = new byte[level/4];
            assertEquals(bee2.beltHash(hash, "1234567890", 10), 0);
            System.out.println(Arrays.toString(hash));


            byte[] sig = new byte[level/2 + level/4];
            assertEquals(
                    bee2.bignSign(sig, bignParams, "1.2.112.0.2.0.34.101.31.81", hash, privKey, rng, null),
                    0
            );
            System.out.println(Arrays.toString(sig));

            assertEquals(
                    bee2.bignVerify(bignParams, "1.2.112.0.2.0.34.101.31.81", hash, sig, pubKey),
                    0
            );
        }


    }

}
