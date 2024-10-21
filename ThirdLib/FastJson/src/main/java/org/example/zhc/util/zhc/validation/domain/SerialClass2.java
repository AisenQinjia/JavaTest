package org.example.zhc.util.zhc.validation.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class SerialClass2 {
    public Map<String,String> alpha;
    public Map<String,String> getAlphax(){
        return alpha;
    }
}
