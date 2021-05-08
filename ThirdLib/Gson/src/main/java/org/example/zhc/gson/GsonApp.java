package org.example.zhc.gson;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class GsonApp {
    static String jStr ="{\"default\":{\"limits-cpu\":\"1\",\"limits-memory\":\"2Gi\",\"requests-cpu\":\"0\",\"requests-memory\":\"0\",\"is.local.debug\":\"false\"}}";
    static String jStr1 = "{\"default\":\"sdfs\"}";
    public static void main(String[] strs){
        Gson gson = new Gson();
        Map<String, Map<String,String>> m = gson.fromJson(jStr, Map.class);
        Object mm = m.get("default");
    }

    private static void ee(){
        if(true){

        }else{
            System.out.println("sdf");
        }
    }
}
