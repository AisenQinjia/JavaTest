package org.example.zhc.serial;

import org.junit.jupiter.api.Test;
import org.nustaq.serialization.FSTConfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FstMain {
    static final FSTConfiguration fst=FSTConfiguration.createDefaultConfiguration();

    public static void main(String[] args) {
        BroadcastMessage<Serializable> m1 = new BroadcastMessage<>();
        m1.setEvent("event");
        String strMsg = "hi";
        m1.setMsg(strMsg);
        BroadcastMessage<Serializable> o = (BroadcastMessage<Serializable>)fst.asObject(fst.asByteArray(m1));

    }

    @Test
    public void ObjTest(){
        Obj1 obj1 = new Obj1();
        obj1.setAge(1);
        obj1.setId(2);
        obj1.setName("zhc");
        Map<String,String> map = new HashMap<>();
        map.put("key1","value1");
        List<String> stringList = new ArrayList<>();
        stringList.add("list1");
        obj1.setMap(map);
        obj1.setListStr(stringList);
        BroadcastMessage<Serializable> m1 = new BroadcastMessage<>();
        m1.setEvent("event");
        m1.setMsg(obj1);
        Obj1 o = (Obj1)fst.asObject(fst.asByteArray(obj1));
    }
}
