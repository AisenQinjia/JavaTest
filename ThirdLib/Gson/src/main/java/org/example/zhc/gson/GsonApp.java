package org.example.zhc.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.example.FileUtil;
import org.example.zhc.gson.serialize.RegionDefine;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GsonApp {
    static String jStr ="{\"default\":{\"limits-cpu\":\"1\",\"limits-memory\":\"2Gi\",\"requests-cpu\":\"0\",\"requests-memory\":\"0\",\"is.local.debug\":\"false\"}}";
    static String jStr1 = "{\"default\":\"sdfs\"}";
    static String jArr = "[\n" +
            "    {\n" +
            "        \"grayTest\":\"true\",                                           \n" +
            "        \"192.168.10.51\":{                                            \n" +
            "            \"dev\":{                                                  \n" +
            "                \"/shitou/tpf-game-server\":{                          \n" +
            "                    \"tag\":                                         \n" +
            "                    \t[\"tpf-game-proxy\",\"tpf-game-login\"],          \n" +
            "                    \n" +
            "                    \"idc\":\"common\"                                   \n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "]";
    public static void main(String[] strs){
        Gson gson = new Gson();
        JsonArray m = gson.fromJson(jArr, JsonArray.class);

        Map<String,Object> o = (Map<String,Object>)m.get(0);
        Map<String, Object> oo = ((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) o.get("192.168.10.51")).get("dev")).get("/shitou/tpf-game-server"));
        List<String> svcs = (List<String>)oo.get("tag");
        System.out.println(svcs.get(0));

         String jsonStr =  gson.toJson(o);
    }
    static Gson gson;
    @Before
    public void init(){
        gson = new Gson();
    }
    @Test
    public void regionTest() throws IOException {
        String regionStr = FileUtil.readCharacterFileToStr("3RegionDefine.json",false);
        Type regionType = new TypeToken<Map<String, RegionDefine>>() {}.getType();
        Map<String, RegionDefine> regionDefine = gson.fromJson(regionStr, regionType);
        RegionDefine a = new RegionDefine();


        System.out.println("regionDefine: " + regionDefine.keySet().iterator().next());
    }

}
