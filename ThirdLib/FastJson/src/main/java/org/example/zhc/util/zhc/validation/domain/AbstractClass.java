package org.example.zhc.util.zhc.validation.domain;

import org.junit.Assert;

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

    public void assertEqual(AbstractClass abstractClass){
        Assert.assertEquals(privateField,abstractClass.privateField);
        Assert.assertEquals(packageField,abstractClass.packageField);
        Assert.assertEquals(protectedField,abstractClass.protectedField);
        Assert.assertEquals(publicField,abstractClass.publicField);
    }
}
