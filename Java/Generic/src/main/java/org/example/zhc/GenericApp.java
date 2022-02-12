package org.example.zhc;

import lombok.var;
import org.example.Log;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void getMyType(){
        String a = "1";

        GenericClass<String> genericClass = new GenericClass<>(String.class);

//        GenericClass<String> genericClass2 = new GenericClass<String>(a.getClass());
    }

    @Test
    public void setCompare(){
        Set<String> stringSet = new HashSet<>();
        stringSet.add("1");
        ((Set)stringSet).add(1);
        ((Set)stringSet).add("1");
        stringSet.forEach(s -> {});
        String a = getT();
        Integer b = getT();
        var c = this.<GenericApp>getT();
    }

    @Test
    public void typeToken(){
//        TypeToken
    }
    @SuppressWarnings("unchecked")
    public <T> T getT(){
        return (T)"a";
    }
}
