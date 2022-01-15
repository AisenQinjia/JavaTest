package org.example.zhc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;
import java.util.function.Consumer;

public class SerialClass {
    private String privateField;
    String packageField;
    protected String protectedField;
    public String publicField;


    @JsonDeserialize(as=SubClass.class)
    private AbstractClass abstractClass;

    private SubClass subClass;

    private List<SubClass> subClasses;

    @JsonDeserialize(as=IClassImpl.class)
    private IClass iClass;

    private StaticClass staticClass;

    private InnerClass innerClass;

    private Set<SubClass> subClassSet;

    private Map<String,IClass> stringIClassMap;

    public void ctor(){
        privateField = "priF";
        packageField = "pac";
        protectedField = "protF";
        publicField = "pubF";
        abstractClass = new SubClass();
        abstractClass.ctor();
        subClasses = new ArrayList<>();
        subClasses.add((SubClass) abstractClass);
        subClass = (SubClass) abstractClass;
        iClass = new IClassImpl();
        iClass.ctor();
        staticClass = new StaticClass();
        staticClass.ctor();
        innerClass = new InnerClass();
        innerClass.ctor();
        subClassSet = new LinkedHashSet<>();
        subClassSet.add((SubClass) abstractClass);
        stringIClassMap = new HashMap<>();
        stringIClassMap.put("IClassImplKey",iClass);
    }

    private static class StaticClass{
        private int staticInt;
        public void ctor(){
            staticInt = 12;
        }
    }

    private class InnerClass{
        private int innerInt;
        public InnerClass(){}
        public void ctor(){
            innerInt = 13;
        }
    }

}

