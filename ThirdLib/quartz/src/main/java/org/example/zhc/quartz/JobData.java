package org.example.zhc.quartz;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class JobData {
    private Map<String,Integer> key;
    public Map<String,Integer> f(){
        return key;
    }
}
