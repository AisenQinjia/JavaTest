package org.example.zhc.util;

public class Log {
    public static void info(String format, Object... objects){
        System.out.println(String.format(format,objects));
    }
}
