package org.example;

public class Log {
    public static void info(String format, Object... objects){
        System.out.println(String.format(format,objects));
    }
}
