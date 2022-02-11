package org.example.zhc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.googlecode.protobuf.format.JsonFormat;
import org.example.msg.proto.Msg;
import org.example.zhc.domain.IClass;
import org.example.zhc.domain.SerialClass;
import org.example.zhc.domain.getter.GetterClass;
import org.junit.Test;


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
        String jsonStr = JSON.toJSONString(serialClass, SerializerFeature.WriteClassName);
        SerialClass<IClass> ss = JSON.parseObject(jsonStr,new TypeReference<SerialClass<IClass>>(){});
        serialClass.assertEqual(ss);
        System.out.println(jsonStr);
    }
    @Test
    public void test2(){
        GetterClass getterClass = new GetterClass(1);
        String jsonStr = JSON.toJSONString(getterClass);
        GetterClass ss = JSON.parseObject(jsonStr,GetterClass.class);
    }
}
