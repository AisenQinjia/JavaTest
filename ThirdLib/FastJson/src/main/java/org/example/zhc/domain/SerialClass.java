package org.example.zhc.domain;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class SerialClass<T> {
    /**
     *
     */
    public String publicField;
    @Setter
    private String privateField;
    @Getter
    String packageField;

    @Getter
    @Setter
    protected String protectedField;

    public SubClass subClass;

    private List<SubClass> subClasses;

    private Map<String,IClassImpl> stringIClassImplMap;

    private T genericField;

    public StaticClass staticClass;

//    private InnerClass innerClass;

    public AbstractClass abstractClass;

    private IClass iClass;

    public Map<MapKey,IClass> mapKeyIClassMap;

    public SerialClass(){

    }

    public SerialClass(String publicField){

    }
    public void assertEqual(SerialClass<T> serialClass){
        Assert.assertEquals(privateField, serialClass.privateField);
        Assert.assertEquals(packageField, serialClass.packageField);
        Assert.assertEquals(protectedField, serialClass.protectedField);
        Assert.assertEquals(publicField, serialClass.publicField);
        subClass.assertEqual(serialClass.subClass);
        Assert.assertArrayEquals(subClasses.toArray(new SubClass[0]),serialClass.subClasses.toArray(new SubClass[0]));
        staticClass.assertEqual(serialClass.staticClass);
//        innerClass.assertEqual(serialClass.innerClass);
        abstractClass.assertEqual(serialClass.abstractClass);
        iClass.assertEqual(serialClass.iClass);
        stringIClassImplMap.forEach((key,subClass)->{
            subClass.assertEqual(serialClass.stringIClassImplMap.get(key));
        });
    }
    public void ctor(){
        privateField = "priF";
        packageField = "pac";
        protectedField = "protF";
        publicField = "pubF";
        abstractClass = new SubClass();
        ((SubClass)abstractClass).ctor(this);
        subClasses = new ArrayList<>();
        subClasses.add((SubClass) abstractClass);
        subClass = (SubClass) abstractClass;
        iClass = new IClassImpl();
        iClass.ctor();
        IClassImpl iClassImpl = new IClassImpl();
        iClassImpl.ctor();
        staticClass = new StaticClass();
        staticClass.ctor();
//        innerClass = new InnerClass();
//        innerClass.ctor();
        stringIClassImplMap = new HashMap<>();
        stringIClassImplMap.put("IClassImplKey",iClassImpl);
        mapKeyIClassMap = new HashMap<>();
        MapKey key = new MapKey();
        key.ctor();
        MapKey key1 = new MapKey();
        key1.ctor();
        mapKeyIClassMap.put(key,new IClassImpl());
        mapKeyIClassMap.put(key1,new IClassImpl());
        genericField = (T)iClass;
    }

    private static class StaticClass{
        @Getter
        @Setter
        private int staticInt;
        public void ctor(){
            staticInt = 12;
        }
        public void assertEqual(StaticClass staticClass){
            Assert.assertEquals(staticInt,staticClass.staticInt);
        }
    }

//    private class InnerClass{
//        @Getter
//        private int innerInt;
//        public InnerClass(){}
//        public void ctor(){
//            innerInt = 13;
//        }
//        public void assertEqual(InnerClass innerClass){
//            Assert.assertEquals(innerInt,innerClass.innerInt);
//        }
//    }

}

