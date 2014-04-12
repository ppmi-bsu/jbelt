package by.bsu.belt;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;

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

    /**
     * Rigourous Test :-)
     */
    public void testBee2()
    {
        Bee2Library.RngFunc rng = new Bee2Library.RngFunc();
        Bee2Library bee2 = Bee2Library.INSTANCE;
        assertNotNull(bee2);
        BignParams bignParams = new BignParams();
        assertEquals(bee2.bignStdParams(bignParams, "1.2.112.0.2.0.34.101.45.3.1"), 0);
        assertEquals(bignParams.l, 128);
        assertEquals(bee2.bignValParams(bignParams), 0);

        byte[] privKey = new byte[32];
        byte[] pubKey = new byte[64];

        assertEquals(bee2.bignGenKeypair(privKey, pubKey, bignParams, rng, null), 0);
        assertEquals(bee2.bignValPubkey(bignParams, pubKey), 0);

        byte[] hash = new byte[32];
        System.out.println(Arrays.toString(hash));
        assertEquals(bee2.beltHash(hash, "1234567890", 10), 0);
        System.out.println(Arrays.toString(hash));


        byte[] sig = new byte[64 + 32];
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
