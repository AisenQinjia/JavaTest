package org.example.zhc.stream;

import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
    }
}
