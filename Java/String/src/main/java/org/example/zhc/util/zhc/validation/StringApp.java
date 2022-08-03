package org.example.zhc.util.zhc.validation;

import org.junit.jupiter.api.Test;

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

    @Test
    public void divisionOpt(){
        long a,b;
        long  t1 = System.currentTimeMillis();
        for (long val = 0; val < 10000000; val += 5){
            a = val * 8;
            b = val/2;
        }
        long t2 = System.currentTimeMillis();
        System.out.println("before opt(/mills): "+ (t2 - t1)/1);
        for (long val = 0; val < 10000000; val += 5){
            a = val << 8;
            b = val >> 2;
        }
        long t3 = System.currentTimeMillis();
        System.out.println("after opt(/mills): "+(t3 - t2)/1);
        System.out.println("diff: "+(2*t2 - t1 - t3)/1);
    }
}
