package org.example.zhc.util.zhc.validation.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;
import org.example.zhc.util.zhc.domain.Style;
import org.example.zhc.util.zhc.validation.CustomDeserializer;
import org.junit.Assert;
import org.springframework.core.serializer.Deserializer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SerialClass<T> {
    @JSONField(serialzeFeatures = SerializerFeature.WriteClassName)
    public Map<Object,String> primitiveKeyMap;
    /**
     *
     */
    public String publicField;
    @Setter
    private String privateField;
    @Getter
    String packageField;
    private boolean isDestoryed = false;
    public FiledClass[] filedClasses;
    public TestKv<String> kv = new TestKv<>();

    public StaticEnum staticEnum = StaticEnum.DARK;

    public Style style;

    protected String protectedField;

    public Queue<Integer> queue = new ConcurrentLinkedQueue<>();
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

    @JSONField(deserializeUsing = CustomDeserializer.class)
    public Object[] objectArray;
//    @JSONField(serialzeFeatures = SerializerFeature.WriteClassName)
    public ArrayList<Object> variables;
    private T genericField;

    public StaticClass staticClass;

//    private InnerClass innerClass;

//    @JSONField(serialzeFeatures = SerializerFeature.WriteClassName)
    public AbstractClass abstractClass;

    private IClass iClass;

    public Map<MapKey,IClass> mapKeyIClassMap;

    public SerialClass(){

    }
    String getPackageField(){
        return "errrror";
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
        primitiveKeyMap = new HashMap<>();
        primitiveKeyMap.put(1,"xxx");
        primitiveKeyMap.put("str","xxx");
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
        FiledClass fc = new FiledClass();
        fc.dd();
        FiledClass fc2 = new FiledClass();
        fc2.dd();
        filedClasses = new FiledClass[]{fc,fc2};
        queue.add(1);
        queue.add(2);
        queue.add(4);
        queue.add(7);
        objectArray = new Object[1];
        List<String>  tmpStrs = Arrays.asList("have","fun");
        objectArray[0] = tmpStrs;
        variables = new ArrayList<>();
        variables.add(Arrays.asList("have","fun"));
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

    public static  <S extends AbstractClass> S getS(Class<S> sClass){
        return null;
    }

    public List<String> ss(){
        return null;
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

