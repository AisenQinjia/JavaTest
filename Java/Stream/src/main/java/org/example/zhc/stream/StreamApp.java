package org.example.zhc.stream;

import org.example.Util;
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

        Collection<String> strs = new HashSet<>();
        strs.add("l");
        strs.add("o");
        strs.add("v");
        strs.add("e");
        String[] at = strs.stream().map(s -> s + " t").toArray(String[]::new);
    }

    @Test
    public void parallelStream(){
        Collection<Integer> iSet = new HashSet<>();
        int i = 0;
        while (i< 100000000){
            iSet.add(i);
            i++;
        }
        final Integer[][] array1 = new Integer[2][1];
        System.out.println("stream time: " + Util.runTime(()-> array1[0] = iSet.stream().map(integer -> integer + 1).toArray(Integer[]::new)));
        //OOM?
        System.out.println("parallel stream time: " + Util.runTime(()->array1[1] = iSet.parallelStream().map(integer -> integer + 1).toArray(Integer[]::new)));
    }
}
