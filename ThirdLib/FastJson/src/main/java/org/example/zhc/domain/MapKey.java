package org.example.zhc.domain;


import java.util.UUID;

public class MapKey  {
    private String key;
    public void ctor(){
        key = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return key;
    }

}
