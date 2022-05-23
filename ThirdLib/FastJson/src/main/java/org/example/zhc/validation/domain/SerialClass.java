package org.example.zhc.validation.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;
import org.example.zhc.domain.Style;
import org.junit.Assert;

import java.util.*;

public class SerialClass<T> {
    /**
     *
     */
    public String publicField;
    @Setter
    private String privateField;
    @Getter
    String packageField;
    private boolean isDestoryed = false;

    public TestKv<String> kv = new TestKv<>();

    public StaticEnum staticEnum = StaticEnum.DARK;

    public Style style;

    protected String protectedField;

    @JSONField(serialize = false)
    public String getNotExistProperty1(){
        System.out.println("getNotExistProperty1");
        return "not exist str1";
    }

    @JSONField(deserialize = false)
    public void setNotExistProperty1(String str){
        System.out.println("setNotExistProperty1 with str param");
    }

    public String getNotExistProperty2(String str){
        System.out.println("getNotExistProperty2");
        return "not exist str2";
    }

    /**
     * protected 方法对fastjson不可见
     */
    protected String getProtectedField(){
        return "protected field";
    }

    public void setNotExistProperty1(){
        System.out.println("setNotExistProperty1");
    }



    public String setNotExistProperty1(String str,String str1){
        System.out.println("setNotExistProperty1 with str param and str1");
        return "not exist str3";
    }

    public SubClass subClass;

    private List<SubClass> subClasses;

    @JSONField(serialzeFeatures = SerializerFeature.WriteClassName)
    public Map<String,IClass> stringIClassMap;

    private T genericField;

    public StaticClass staticClass;

//    private InnerClass innerClass;

    @JSONField(serialzeFeatures = SerializerFeature.WriteClassName)
    public AbstractClass abstractClass;

    private IClass iClass;

    private Map<MapKey,IClass> mapKeyIClassMap;

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
        stringIClassMap.forEach((key, subClass)->{
            subClass.assertEqual(serialClass.stringIClassMap.get(key));
        });
        Assert.assertEquals(style,serialClass.style);
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
        stringIClassMap = new HashMap<>();
        stringIClassMap.put("IClassImplKey",iClassImpl);
        stringIClassMap.put("IClassImpl2Key",new IClassImpl2());
        mapKeyIClassMap = new HashMap<>();
        MapKey key = new MapKey();
        key.ctor();
        MapKey key1 = new MapKey();
        key1.ctor();
        mapKeyIClassMap.put(key,new IClassImpl());
        mapKeyIClassMap.put(key1,new IClassImpl());
        genericField = (T)iClass;
        kv.ctor("genericStr");
        style = Style.BOLD;
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

    private static enum StaticEnum{
        LIGHT,
        DARK
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

