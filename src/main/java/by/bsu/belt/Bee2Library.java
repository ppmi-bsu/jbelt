package by.bsu.belt;

import com.sun.jna.*;
import com.sun.jna.ptr.PointerByReference;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: mihas
 * Date: 4/12/14
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Bee2Library extends Library {
    Bee2Library INSTANCE = (Bee2Library) Native.loadLibrary("bee2", Bee2Library.class);

    public interface IRngFunction extends Callback {
        void invoke(PointerByReference buf, int count, PointerByReference stack);
    }

    public class RngFunc implements IRngFunction{

        public void invoke(PointerByReference buf, int count, PointerByReference stack) {


            byte[] random = new byte[count];
            new Random(System.currentTimeMillis()).nextBytes(random);
            buf.getPointer().write(0, random, 0, count);

        }
    }

    int bignStdParams(BignParams bignParams, String name);
    int bignValParams(BignParams bignParams);
    int bignValPubkey(BignParams bignParams, byte[] pubKey);
    int bignGenKeypair(byte[] privKey, byte[] pubKey, BignParams bignParams,
                       IRngFunction rng, Pointer rng_state);

    int beltHash(byte[] hash, String src, int count);

    int bignSign(
            byte[] sig,					/*!< [out] подпись */
            BignParams params,	/*!< [in] долговременные параметры */
            String oid,			/*!< [in] идентификатор хэш-алгоритма */
            byte[] hash,			/*!< [in] хэш-значение */
            byte[] privkey,		/*!< [in] личный ключ */
            IRngFunction rng,					/*!< [in] генератор случайных чисел */
            Pointer rng_state);				/*!< [in/out] состояние генератора */

    int bignVerify(
            BignParams params,	/*!< [in] долговременные параметры */
            String oid,			/*!< [in] идентификатор хэш-алгоритма */
            byte[] hash,			/*!< [in] хэш-значение */
            byte[] sig,			/*!< [in] подпись */
            byte[] pubkey			/*!< [in] открытый ключ */
    );

    int bignCalcPubkey(
            byte[] pubkey,				/*!< [out] открытый ключ */
            BignParams params,	/*!< [in] долговременные параметры */
            byte[] privkey		/*!< [in] личный ключ */
    );

    int beltECBEncr(byte[] dest, byte[] src, int count,
                byte[] theta, int len);

    int beltECBDecr(byte[] dest, byte[] src, int count,
                    byte[] theta, int len);

    int bignKeyWrap(
            byte[] token,					/*!< [out] токен ключа */
            BignParams params,		/*!< [in] долговременные параметры */
            byte[] key,				/*!< [in] транспортируемый ключ */
            int len,						/*!< [in] длина ключа в октетах */
            byte[] header,			/*!< [in] заголовок ключа [16]*/
            byte[] pubkey,			/*!< [in] открытый ключ получателя */
            IRngFunction rng,						/*!< [in] генератор случайных чисел */
            Pointer rng_state					/*!< [in/out] состояние генератора */
    );

    int bignKeyUnwrap(
            byte[] key,						/*!< [out] ключ */
            BignParams params,		/*!< [in] долговременные параметры */
            byte[] token,				/*!< [in] токен ключа */
            int len,						/*!< [in] длина токена в октетах */
            byte[] header,			/*!< [in] заголовок ключа [16]*/
            byte[] privkey);			/*!< [in] личный ключ получателя */
}
