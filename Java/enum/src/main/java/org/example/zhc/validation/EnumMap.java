package org.example.zhc.validation;


import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnumMap {
    enum  DataType{
        INT,
        STR,
        MAP
    }
    @Test
    public void enumMap(){
        Map<DataType, Set<Object>> m = new java.util.EnumMap<>(DataType.class);
        m.computeIfAbsent(DataType.INT,t-> new HashSet<>()).add(1);
        m.computeIfAbsent(DataType.MAP,t-> new HashSet<>()).add(new HashMap<>());
        m.computeIfAbsent(DataType.STR,t-> new HashSet<>()).add("str");
        for(Map.Entry<DataType,Set<Object>> entry : m.entrySet()){
            System.out.println(String.format("type:%s, value:%s",entry.getKey(),entry.getValue()));
        }
    }
}
