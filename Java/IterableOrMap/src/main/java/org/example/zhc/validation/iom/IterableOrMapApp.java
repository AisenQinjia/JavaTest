package org.example.zhc.validation.iom;

import org.junit.Test;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class IterableOrMapApp {
    public static PrintStream out = System.out;

    public static void main(String[] args){

    }
    @Test
    public void removeAll(){
        Set<String> a = new HashSet<>();
        Set<String> b = new HashSet<>();
        String key1 = "sdf";
        String key2 = "dsfs";
        a.add(key1);
        a.add(key2);
        b.add(key1);
        a.removeAll(b);
        System.out.println(a.size());
        System.out.println(b.size());
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

    @Test
    public void mapContains(){
        Map<String,String> ssMap = new ConcurrentHashMap<>();
        ssMap.computeIfAbsent("ss",key->null);
        out.println("contains key : " + ssMap.containsKey("ss"));
    }
}
