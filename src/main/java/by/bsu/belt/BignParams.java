package by.bsu.belt;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Created with IntelliJ IDEA.
 * User: mihas
 * Date: 4/12/14
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class BignParams extends Structure implements Structure.ByReference {
    public int l;		/*!< уровень стойкости (128, 192 или 256) */
    public byte[] p = new byte[64];	/*!< модуль p */
    public byte[] a = new byte[64];	/*!< коэффициент a */
    public byte[] b = new byte[64];	/*!< коэффициент b */
    public byte[] q = new byte[64];	/*!< порядок q */
    public byte[] yG = new byte[64];	/*!< y-координата точки G */
    public byte[] seed = new byte[8];  /*!< параметр seed */

    public BignParams(int level) {
        String curve_name = null;
        curve_name = getCurveName(level);

        int res = Bee2Library.INSTANCE.bignStdParams(this, curve_name);
        if (res!=0)
            throw new RuntimeException("Params were not loaded, code is " + res);

        assert is_valid(this);

    }

    public static String getCurveName(int level) {
        String curve_name;
        switch (level) {
            case 128:
                curve_name = "1.2.112.0.2.0.34.101.45.3.1"; break;
            case 192:
                curve_name = "1.2.112.0.2.0.34.101.45.3.2"; break;
            case 256:
                curve_name = "1.2.112.0.2.0.34.101.45.3.3"; break;
            default:
                throw new IllegalArgumentException("Level " + level + "is invalid");
        }
        return curve_name;
    }

    public static boolean is_valid(BignParams bignParams) {
        return Bee2Library.INSTANCE.bignValParams(bignParams) == 0;

    }

}