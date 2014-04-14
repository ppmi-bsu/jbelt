package by.bsu.belt.provider;

import java.util.ArrayList;

/**
 * Created by mihas
 * Date: 4/12/14
 * Time: 11:30 PM
 */
public class Util {

    static public byte[] bytes(ArrayList<Byte> data){
        byte[] bytes = new byte[data.size()];
        for(int i=0;i<data.size();i++)
            bytes[i] = data.get(i);

        return bytes;
    }
}
