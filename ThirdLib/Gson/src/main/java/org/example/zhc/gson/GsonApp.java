package org.example.zhc.gson;

import com.google.gson.Gson;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.HashMap;
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
        List<Object> m = gson.fromJson(jArr, List.class);
        Map<String,Object> o = (Map<String,Object>)m.get(0);
        Map<String, Object> oo = ((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) o.get("192.168.10.51")).get("dev")).get("/shitou/tpf-game-server"));
        List<String> svcs = (List<String>)oo.get("tag");
        System.out.println(svcs.get(0));
    }

    private static void ee(){
        if(true){

        }else{
            System.out.println("sdf");
        }
    }
}
