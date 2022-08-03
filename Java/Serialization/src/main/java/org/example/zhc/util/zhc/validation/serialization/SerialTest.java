package org.example.zhc.util.zhc.validation.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.zhc.util.Log;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SerialTest {
    public static void main(String[] args){
        serialTest();
    }
    public static void serialTest(){
        Serial1 s1 =new Serial1(1,2,3,4,5,6);
        Log.info(JSON.toJSONString(s1));
        String jsonS1 = "{\"privateInt\":5,\"privateInteger\":\"{}\",\"protectedInt\":3,\"protectedInteger\":4,\"publicInt\":1,\"publicInteger\":2}";
        Serial1 s2 = JSON.parseObject(jsonS1,
                new TypeReference<Serial1>(){});
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
}
