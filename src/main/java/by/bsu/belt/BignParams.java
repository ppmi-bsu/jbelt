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

    public BignParams() {
    }
}