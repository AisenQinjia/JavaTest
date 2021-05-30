package org.example.zhc.iom;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class IterableOrMapApp {
    public static void main(String[] args){

    }
    @Test
    public void simpleArray(){
        List<String> simpleList = new SimpleList<>();
        simpleList.add("love");
        simpleList.add("and");
        simpleList.add("peace");
        String[] ss = simpleList.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return !s.equals("and");
            }
        }).toArray(String[]::new);
        for(String str: ss){
            System.out.println(str);
        }
    }

    private class Ref1{
        int a;
    }

    /**
     * toArray返回的是相同的对象
     */
    @Test
    public void listToArray(){
        List<Ref1> refs = new ArrayList<>();
        refs.add(new Ref1());
        Object[] refArray = refs.toArray();
        System.out.println(String.format("a1: %s",((Ref1)refArray[0]).a));
        refs.get(0).a = 2;
        System.out.println(String.format("a2: %s",((Ref1)refArray[0]).a));
    }

    @Test
    public void simpleMap(){
        Map<String,String> simpleMap = new SimpleMap<>();
        simpleMap.put("love","peace");
        System.out.println(simpleMap.get("love"));
        simpleMap.put("love2","peace2");
        System.out.println(simpleMap.get("love2"));
        simpleMap.remove("lov");
        System.out.println(simpleMap.size());
    }
}
