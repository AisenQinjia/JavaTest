package org.example.zhc;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UMI {
    Long id;
    Map<String,Object> mailTag;

    public static final String FIRST_PULL_TIME = "FIRST_PULL_TIMESTAMP";

    public Map<String,Object> computeMailTagIfAbsent(){
        if(mailTag == null){
            mailTag = new HashMap<>();
        }
        return mailTag;
    }
}
