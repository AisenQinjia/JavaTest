package org.example.zhc.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SubClass.class, name = "subClass"),
})
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
