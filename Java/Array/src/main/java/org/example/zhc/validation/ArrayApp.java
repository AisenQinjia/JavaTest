package org.example.zhc.validation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * array 测试
 * @author aisen
 */
public class ArrayApp {
    @Test
    public void arrayType(){
        {
            String[] a = new String[2];
            Object[] b = a;
            a[0] = "hi";
            //报错,试图把一个Integer赋给String
            b[1] = Integer.valueOf(42);
        }

        {
            Parent[] ps = new Parent[2];
            Object[] os = ps;
            os[0] = new Parent();
            //不报错,把Child赋给Parent
            os[1] = new Child();
        }
    }

    class Parent{

    }
    class Child extends Parent{

    }

    @Test
    public void zeroArray(){
        String[] zs = null ;
//        System.out.println("zs length: " + zs.length);
        for(String str: zs){
            System.out.println("str: " + str);
        }
    }

    @Test
    public void sort(){
        List<Long> l = new ArrayList<>();
        l.add(2L);
        l.add(1L);
//        l.sort(null);
        l.forEach(System.out::println);
    }

    @Test
    public void varragsTest(){
        byte[] b1 = new byte[1];
        varargs(b1);
    }

    private void varargs(byte[]... bytes){
        System.out.println("varargs type: " + bytes.getClass());
        System.out.println("varargs length: " + bytes.length);
    }
}
