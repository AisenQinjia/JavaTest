package org.example.zhc.util.zhc.validation.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SerialClassTwo {
    public Map<Integer, Set<IClassImpl>> ysetMap = new HashMap<>();
    public Map<IClassImpl,Integer> testMap= new HashMap<>();
    public int a;
    public int b;
    public void set(int a){
        log.info("set a");
    }

    public void set(int a,int b){
        log.info("set a, b");
    }

//    public void setA(int a){
//        log.info("setA");
//    }

}
