package org.example.zhc.validation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.var;
import org.example.msg.proto.Msg;
import org.example.zhc.validation.domain.IClass;
import org.example.zhc.validation.domain.SerialClass;
import org.example.zhc.validation.domain.TestKv;
import org.example.zhc.validation.domain.getter.GetterClass;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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
        SerialClass<IClass> serialClass = new SerialClass<>();
        serialClass.ctor();
        String jsonStr = JSON.toJSONString(serialClass);
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
}
