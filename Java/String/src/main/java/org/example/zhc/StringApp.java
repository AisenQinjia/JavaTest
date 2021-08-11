package org.example.zhc;

import org.junit.Test;

public class StringApp {
    @Test
    public void regexSplit(){
        String str = " one two  three  ";
        String[] strs = str.trim().split("\\s+");
        for (String s: strs){
            System.out.println(s);
        }
    }
}
