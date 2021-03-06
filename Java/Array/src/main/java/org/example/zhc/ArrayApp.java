package org.example.zhc;

import org.junit.Test;

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
}
