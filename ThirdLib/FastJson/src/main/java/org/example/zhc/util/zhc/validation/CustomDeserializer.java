package org.example.zhc.util.zhc.validation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

public class CustomDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if(fieldName.equals("objectArray")){
            JSONArray vars = (JSONArray)parser.parse(fieldName);
            Object[] rel = new Object[vars.size()];
            for (int i = 0; i < vars.size(); i ++){
                rel[i] = vars.get(i);
                if (rel[i] instanceof JSONArray){
                   rel[i] = ((JSONArray)rel[i]).toJavaList(Object.class);
                }
            }
            return (T)rel;
        }
        return (T)parser.parse(fieldName);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
