package org.example.zhc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.protobuf.format.JsonFormat;
import org.example.msg.proto.Msg;
import org.junit.Test;


public class FastJsonTestApp {
    public static void main(String[] args){

    }
    @Test
    void parseGson(){
        Msg msg = Msg.newBuilder()
                .setMsgId("main")
                .build();

        String str = JsonFormat.printToString(msg);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgId_", "main");
        Msg pbMsg = jsonObject.toJavaObject(Msg.class);
        System.out.println(pbMsg.getMsgId());
    }


}
