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
    public void ff(){
        Collection<String> strs = new HashSet<>();
        strs.add("l");
        strs.add("e");
       String[] a =   strs.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.equals("l");
            }
        }).toArray(String[]::new);

    }
}
