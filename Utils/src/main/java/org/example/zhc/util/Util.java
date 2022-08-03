package org.example.zhc.util;

public class Util {
    private Util(){}
    public static long runTime(Runnable runnable){
        long currentTS = System.nanoTime();
        runnable.run();
        return (System.nanoTime() - currentTS)/1000000;
    }
}
