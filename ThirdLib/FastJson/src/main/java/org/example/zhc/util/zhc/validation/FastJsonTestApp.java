package org.example.zhc.util.zhc.validation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.var;
import org.example.zhc.util.msg.proto.Msg;
import org.example.zhc.util.zhc.validation.domain.*;
import org.example.zhc.util.zhc.validation.domain.getter.GetterClass;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class FastJsonTestApp {
    public static void main(String[] args){

    }

    @Test
    public void parseGson(){
        Msg msg = Msg.newBuilder()
                .setMsgId("main")
                .build();

        String str = JsonFormat.printToString(msg);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgId", "main");
        Msg pbMsg = jsonObject.toJavaObject(Msg.class);
        System.out.println(pbMsg.getMsgId());
    }

    @Test
    public void serialTest(){
        ClassA1 a1 = new ClassA1();
        a1.setAnInt(5);
        String jsonString = JSON.toJSONString(a1);
        ClassA2 classA2 = JSON.parseObject(jsonString, ClassA2.class);
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        SerialClass<IClass> serialClass = new SerialClass<>();
        serialClass.ctor();
        String jsonStr = JSON.toJSONString(serialClass);
        Map map = JSON.parseObject(jsonStr, Map.class);
        String prefix = "ooo";
//        // 创建自定义的 JSONParser
//        DefaultJSONParser parser = new DefaultJSONParser(jsonStr);
//
//        // 设置自定义的解析器处理器
//        parser.getConfig().putDeserializer(Object.class, new ObjectDeserializer() {
//
//            @Override
//            public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
//                return parser.parse(fieldName);
//            }
//
//            @Override
//            public int getFastMatchToken() {
//                return 0;
//            }
//        });
        JSONObject jsonObject = (JSONObject)JSONObject.parse(jsonStr);
        JSONObject o = (JSONObject)jsonObject.get("mapKeyIClassMap");

        Set strings = o.keySet();
        for (Object string : strings) {
            JSONObject jsonObject1 = (JSONObject)string;
            jsonObject1.isEmpty();
        }

        SerialClass<IClass>  ss = JSON.parseObject(jsonStr,new TypeReference<SerialClass<IClass>>(){});

        SerialClass<IClass> ss2 = JSON.parseObject(jsonStr,(Type)SerialClass.class);
        //type inference by Target types
        SerialClass<IClass> ss22= JSON.parseObject(jsonStr,(Type)SerialClass.class);
        FastJsonTestApp     ss3 = JSON.parseObject(jsonStr,(Type)SerialClass.class);
        //type inference to Object
        var                 ss4 = JSON.parseObject(jsonStr,(Type)SerialClass.class);
        //type witness
        var                 ss5 = JSON.<SerialClass<IClass>>parseObject(jsonStr,(Type)SerialClass.class);
//        serialClass.assertEqual(ss);
//        System.out.println(jsonStr);
    }

    @Test
    public void serialTest2(){
        SerialClassTwo two = new SerialClassTwo();
        IClassImpl iClass = new IClassImpl();
        String string = JSON.toJSONString(iClass);
        JSON.parseObject(string,IClassImpl.class);
        IClassImpl iClass2 = new IClassImpl();
        IClassImpl iClass3 = new IClassImpl();
        iClass.interfaceImpl = 1;
        iClass2.interfaceImpl =2;
        iClass3.interfaceImpl =3;
        two.testMap.put(iClass,1);
        two.testMap.put(iClass2,2);
        two.testMap.put(iClass3,2);
        HashSet<IClassImpl> objectHashSet = new HashSet<>();
        objectHashSet.add(iClass);
        two.ysetMap.put(1,objectHashSet);
        HashSet<IClassImpl> objectHashSet2 = new HashSet<>();
        objectHashSet2.add(iClass2);
        objectHashSet2.add(iClass3);
        two.ysetMap.put(2,objectHashSet2);
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.setAsmEnable(false);
        String jsonStr = JSON.toJSONString(two,serializeConfig);
        SerialClassTwo ss2 = JSON.parseObject(jsonStr,SerialClassTwo.class);
    }

    @Test
    public void t(){
        AClass aClass = new AClass();
        AClass ta = new AClass();
        aClass.a.add("asdf");
        aClass.transientInt = 10;
        ta.transientAClass = ta;
        String s = JSON.toJSONString(aClass);
        AClass aClass1 = JSON.parseObject(s, AClass.class);

    }

    @Test
    public void test2(){
        GetterClass getterClass = new GetterClass(1);
        String jsonStr = JSON.toJSONString(getterClass);
        GetterClass ss = JSON.parseObject(jsonStr,GetterClass.class);
    }

    @Test
    public void customTypeToken(){
//        TestKv2 kv2 = new TestKv2();
//        kv2.ctor();
//        String kv2Str = JSON.toJSONString(kv2);
//        TestKv2 deKv2 = JSON.parseObject(kv2Str,TestKv2.class);

        TestKv<List<Long>> ts = new TestKv<>();
        List<Long> ls = new ArrayList<>();
        ls.add(1L);
        ts.ctor(ls);
        String tsStr = JSON.toJSONString(ts);
        TestKv<List<Long>> deTs3 = JSON.parseObject(tsStr,new TypeReference<TestKv<List<Long>>>(){});
    }

    @Test
    public void refTest(){
        SerialClass2 s2 = new SerialClass2();
        Map<String, String> m = new HashMap<>();
        m.put("key","vale");
        s2.setAlpha(m);
        String jsonString = JSON.toJSONString(s2);
        SerialClass2 serialClass2 = JSON.parseObject(jsonString, SerialClass2.class);

    }
}
