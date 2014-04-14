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
        int[] levels = { 128};
        for(int l: levels ) {
            BignParams bignParams = new BignParams(bee2, l);
            assertEquals(bignParams.l, l);
            assertEquals(bee2.bignValParams(bignParams), 0);

            byte[] privKey = new byte[l/4];
            byte[] pubKey = new byte[l/2];

            assertEquals(bee2.bignGenKeypair(privKey, pubKey, bignParams, rng, null), 0);
            assertEquals(bee2.bignValPubkey(bignParams, pubKey), 0);

            byte[] hash = new byte[32];
            assertEquals(bee2.beltHash(hash, "1234567890", 10), 0);
            System.out.println(Arrays.toString(hash));


            byte[] sig = new byte[48];
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
