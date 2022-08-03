package org.example.zhc.util.zhc.validation;

import org.junit.jupiter.api.Test;

import java.util.*;

public class Favorites {
    private final Map<Class<?>,Object> instMap = new HashMap<>();
    public <T> void put(Class<T> tClass,T inst){
        instMap.put(tClass,inst);
    }
    //
    public <T> T dangerousGet(Class<T> tClass){
        return tClass.cast(instMap.get(tClass));
    }

    @Test
    public void getTest(){
        List<String> a = new ArrayList<String>();
        a.add("sdf");
        put(List.class,a);
        dangerousGet(List.class);
        List<Integer> list = dangerousGet(List.class);
        Integer integer = list.get(0);
        System.out.println("dangerousGet: " + dangerousGet(Integer.class));
//        System.out.println("dangerousGet: " + );
    }
}
