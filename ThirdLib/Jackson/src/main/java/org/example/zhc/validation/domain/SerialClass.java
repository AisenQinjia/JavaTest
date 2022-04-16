package org.example.zhc.validation.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Assert;

import java.util.*;

public class SerialClass<T> {
    private String privateField;
    String packageField;
    protected String protectedField;
    public String publicField;
    private SubClass subClass;
    private List<SubClass> subClasses;
    private Map<String,IClassImpl> stringIClassImplMap;
    @JsonDeserialize(as=IClassImpl.class)
    private T genericField;
    //静态类等同于普通字段
    private StaticClass staticClass;
    //内网类需要public的构造函数(和说明不符)
    private InnerClass innerClass;
    //抽象类可以在注解中声明具体的类
//    @JsonDeserialize(as=SubClass.class)
    private AbstractClass abstractClass;
    //接口类可以在注解中声明具体的类
    @JsonDeserialize(as=IClassImpl.class)
    private IClass iClass;
    /**
     * serialize: 调用key类toString(),即使同名会序列化两个同名key
     * deserialize: 需要有自定义的deserializer
     */
    @JsonDeserialize(keyUsing = MapKey.class,contentAs = IClassImpl.class)
    private Map<MapKey,IClass> mapKeyIClassMap;


    public void assertEqual(SerialClass<T> serialClass){
        Assert.assertEquals(privateField, serialClass.privateField);
        Assert.assertEquals(packageField, serialClass.packageField);
        Assert.assertEquals(protectedField, serialClass.protectedField);
        Assert.assertEquals(publicField, serialClass.publicField);
        subClass.assertEqual(serialClass.subClass);
        Assert.assertArrayEquals(subClasses.toArray(new SubClass[0]),serialClass.subClasses.toArray(new SubClass[0]));
        staticClass.assertEqual(serialClass.staticClass);
        innerClass.assertEqual(serialClass.innerClass);
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
        abstractClass.ctor();
        subClasses = new ArrayList<>();
        subClasses.add((SubClass) abstractClass);
        subClass = (SubClass) abstractClass;
        iClass = new IClassImpl();
        iClass.ctor();
        IClassImpl iClassImpl = new IClassImpl();
        iClassImpl.ctor();
        staticClass = new StaticClass();
        staticClass.ctor();
        innerClass = new InnerClass();
        innerClass.ctor();
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
        private int staticInt;
        public void ctor(){
            staticInt = 12;
        }
        public void assertEqual(StaticClass staticClass){
            Assert.assertEquals(staticInt,staticClass.staticInt);
        }
    }

    private class InnerClass{
        private int innerInt;
        public InnerClass(){}
        public void ctor(){
            innerInt = 13;
        }
        public void assertEqual(InnerClass innerClass){
            Assert.assertEquals(innerInt,innerClass.innerInt);
        }
    }

}

