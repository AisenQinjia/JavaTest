package org.example.zhc.util.zhc.validation.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class AClass {
    public final List<String> a = new ArrayList<>();
    public final String b = "final";
    private String name;
    private int age;
    @Getter
    @Setter
    public transient int transientInt;

    @Getter
    @Setter
    public transient AClass transientAClass;
    int pacc;
    public String getAbc(){
        System.out.println("getAbc");
        return null;
    }

    public void setAbc(){
        System.out.println("setAbc");
    }
}

