package org.example.zhc.util.zhc.validation.domain;

public class MethodClass {
    private String privateField;
    String packageField;
    protected String protectedField;
    public String publicField;

//    public void setPackageField(String p){
//        packageField = p;
//    }

    public String getPackageField(){
        return packageField;
    }


    public void dd(){
        privateField = "priF";
        packageField = "pac";
        protectedField = "protF";
        publicField = "pubF";
    }
}
