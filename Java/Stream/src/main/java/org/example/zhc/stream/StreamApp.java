package org.example.zhc.stream;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 */
public class StreamApp {
    public static void main(String[] args){

    }
    @Test
    public void array2String(){
        String[] strs1 = new String[4];
//        strs[0] = "l";
//        strs[1] = "o";
//        strs[2] = "v";
//        strs[3] = "e";
        Collection<String> strs = new HashSet<>();
       String[] a =   strs.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.equals("l");
            }
        }).toArray(String[]::new);

    }
}
