package org.example.zhc.util.zhc.validation.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.zhc.util.Log;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SerialTest {
    public static void main(String[] args){
        serialTest();
    }
    public static void serialTest(){
        Serial1 s1 =new Serial1(1,2,3,4,5,6);
        List<String> stringList = new ArrayList<>();
        stringList.add("sdfs");
        Log.info(JSON.toJSONString(stringList));
//        String jsonS1 = "{\"privateInt\":5,\"privateInteger\":\"{}\",\"protectedInt\":3,\"protectedInteger\":4,\"publicInt\":1,\"publicInteger\":2}";
//        Serial1 s2 = JSON.parseObject(jsonS1,
//                new TypeReference<Serial1>(){});
    }

    public static void overrideTest(){
        Serial1[] ss = new Serial1[2];
        ss[0] = new Serial1(1,1,1,1,1,1);
        ss[1] = new Serial2();
        for(val s:ss){

        }
    }

    @Test
    public void iterableSer(){
        Map<String, Map<String,String>> m = new HashMap<>();
        Map<String,String> mm = new HashMap<>();
        mm.put("sdf","so what");
        mm.put("but","what else");
        m.put("1", mm);
        JSONArray result = new JSONArray();
        result.addAll(m.get("1").entrySet());
        System.out.println(result.toJSONString());
    }
    @Test
    public void getterTest(){
        Serial2 serial2 = new Serial2();
        String s = JSON.toJSONString(serial2);
    }

    @Test
    public void primetiveTest(){
        int a = 1;
        Integer b = 1;
        int count = 1000;
        IntClass intClass = new IntClass();
        IntegerClass integerClass = new IntegerClass();
        intClass.a = 1;
        integerClass.a = b;
        System.out.println("序列化次数 :"+count);
        //预热
        for(int i=0;i<count;i++){
            JSON.toJSONBytes(intClass);
            JSON.toJSONBytes(integerClass);
        }


        byte[] intBytes=null;
        long start = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            intBytes = JSON.toJSONBytes(intClass);
        }
        long end = System.currentTimeMillis();
        System.out.println("序列化int cost:"+(end-start));

        byte[] integerBytes=null;
        long start2 = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            integerBytes = JSON.toJSONBytes(integerClass);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("序列化Integer cost:"+(end2-start2));

        //预热
        for(int i=0;i<count;i++){
            JSON.parseObject(intBytes,IntClass.class);
            JSON.parseObject(integerBytes,IntegerClass.class);
        }
        //反序列化


        long start3 = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            JSON.parseObject(intBytes,IntClass.class);
        }
        long end3 = System.currentTimeMillis();
        System.out.println("反序列化 int cost:"+(end3-start3));

        long start4 = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            JSON.parseObject(integerBytes,IntegerClass.class);
        }
        long end4 = System.currentTimeMillis();
        System.out.println("反序列化 Integer cost:"+(end4-start4));

    }
}
