package org.example.zhc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SerialClass {
    private String privateField;
    String packageField;
    protected String protectedField;
    public String publicField;

    @JsonDeserialize(as=SubClass.class)
    private AbstractClass abstractClass;

    private List<SubClass> subClasses;

    @JsonDeserialize(as=IClassImpl.class)
    private IClass iClass;

    public void ctor(){
        privateField = "priF";
        packageField = "pac";
        protectedField = "protF";
        publicField = "pubF";
        abstractClass = new SubClass();
        abstractClass.ctor();
        subClasses = new ArrayList<>();
        subClasses.add((SubClass) abstractClass);
        iClass = new IClassImpl();
        iClass.ctor();
    }
}

