package org.example.zhc.util.zhc.validation.domain;


import java.util.UUID;

public class MapKey  {
    public String key;
    public String m1;
    public void ctor(){
        key = UUID.randomUUID().toString();
        m1= UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return key;
    }

    public static void main(String[] args) {
        String substring = UUID.randomUUID().toString().substring(0, 8);
        System.out.println(substring);
    }
}
