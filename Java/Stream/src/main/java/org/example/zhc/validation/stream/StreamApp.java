package org.example.zhc.validation.stream;

import lombok.Data;
import org.example.Util;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;


/**
 *
 */
public class StreamApp {
    public static void main(String[] args){

    }
    @Data
    class TestClass{
        int index;
        String name;
        public TestClass(int i, String s){
            index = i;
            name = s;
        }
    }
    @Test
    public void array2String(){
//        String[] strs = new String[4];
//        strs[0] = "l";
//        strs[1] = "o";
//        strs[2] = "v";
//        strs[3] = "e";
        Collection<String> strs = new HashSet<>();
        strs.add("1");
        strs.add("2");
        strs.add("3");
       String[] a =   strs.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.equals("l");
            }
        }).toArray(String[]::new);
        Object[] b = strs.stream().map(s -> 1).toArray();
    }

    @Test
    public void sort(){
        Set<TestClass> testClassSet = new HashSet<>();
        testClassSet.add(new TestClass(1,"b"));
        testClassSet.add(new TestClass(2,"a"));
        TestClass testClass = testClassSet.stream().min(Comparator.comparing(TestClass::getName)).get();

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
