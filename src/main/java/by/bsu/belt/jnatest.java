package by.bsu.belt;

// This exaple is for linux platform only
// Save the code in a file named jnatest.java
// Keep the jna.jar in the same directory
// Then compile it as
// $javac -cp jna.jar jnatest.java
//
// Then run it as below:
// $java -cp .:jna.jar jnatest

import com.sun.jna.Library;
import com.sun.jna.Native;


interface CLibrary extends Library {
    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);
    int getpid();
    int getppid();
    long time(long buf[]);

}

public class jnatest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println(CLibrary.INSTANCE.getpid());
            System.out.println(CLibrary.INSTANCE.getppid());
            long[] timenul = new long[1];
            System.out.println(CLibrary.INSTANCE.time(timenul));
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Exception" + e);
        }
    }
}

