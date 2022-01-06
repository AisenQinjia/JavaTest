package org.example.zhc.domain;

public abstract class AbstractClass {
    private String privateField;
    String packageField;
    protected String protectedField;
    public String publicField;
    public void ctor(){
        privateField = "priF";
        packageField = "pac";
        protectedField = "protF";
        publicField = "pubF";
    }
}
