package org.example.zhc;

import org.example.Log;
import org.junit.Test;

import java.util.ArrayList;

interface Factory<T>{
    T create();
}

class F1 implements Factory<String>{

    @Override
    public String create() {
        return "str";
    }
}
class Widget{
    public static class F2 implements Factory<Integer>{
        @Override
        public Integer create() {
            return new Integer(1);
        }
    }
}

class Foo<T>{
    T item;
    public <F extends Factory<T>> Foo(F factory){
        this.item = factory.create();
    }
}
public class GenericApp {
    @Test
    public void erasure(){
        Class a = new ArrayList<Integer>().getClass();
        Class b = new ArrayList<String>().getClass();
        Log.info("a==b: %s",a==b);
    }
}
