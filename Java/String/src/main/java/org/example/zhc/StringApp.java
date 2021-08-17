package org.example.zhc;

import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.HashMap;
import java.util.Map;

public class StringApp {
    @Test
    public void regexSplit(){
        String str = " one two  three  ";
        String[] strs = str.trim().split("\\s+");
        for (String s: strs){
            System.out.println(s);
        }
    }

    @Test
    public void formatNull(){
        Map<String,Object> o = null;
        Map<String,Object> o1 = new HashMap<>();
        o1.put("key", "value");
        System.out.printf(String.format("%s  || %s", o, o1==null));
    }
}
