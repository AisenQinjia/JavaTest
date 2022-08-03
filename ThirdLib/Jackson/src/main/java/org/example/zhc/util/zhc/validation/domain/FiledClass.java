package org.example.zhc.util.zhc.validation.domain;

import java.io.Serializable;

public class FiledClass implements Serializable {
    private String privateField;
    String packageField;
    protected String protectedField;
    public String publicField;

    public void dd(){
        privateField = "priF";
        packageField = "pac";
        protectedField = "protF";
        publicField = "pubF";
    }
}
