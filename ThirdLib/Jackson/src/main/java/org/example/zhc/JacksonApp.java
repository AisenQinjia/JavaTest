package org.example.zhc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.zhc.domain.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JacksonApp {
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    { mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE));}
    @Test
    public void POJOs() throws JsonProcessingException {
        AClass aClass =  mapper.readValue(jsonStr, AClass.class);
        String jsonStr = mapper.writeValueAsString(aClass);
    }

    String jsonStr = "{\"name\":\"Bob\", \"age\":13}";
    @Test
    public void ctor_paramName() throws JsonProcessingException {
        SerialClass bClass = mapper.readValue(jsonStr, SerialClass.class);
        String jsonStr = mapper.writeValueAsString(bClass);
        byte[] bytes= mapper.writeValueAsBytes(bClass);
    }

    final String filedStr = "{\"privateField\":\"prif\",\"packageField\":\"packf\",\"protectedField\":\"prof\",\"publicField\":\"pubf\"}";
    @Test
    public void fileTest() throws JsonProcessingException {
        FiledClass filedClass = mapper.readValue(filedStr,FiledClass.class);
        filedClass.dd();
        String jsonStr = mapper.writeValueAsString(filedClass);
    }
    @Test
    public void methodTest()throws JsonProcessingException{
        MethodClass methodClass = mapper.readValue(filedStr, MethodClass.class);methodClass.dd();
        String jsonStr = mapper.writeValueAsString(methodClass);
    }

    final String absSubStr = "{\"privateField\":\"prif\",\"packageField\":\"packf\",\"protectedField\":\"prof\",\"publicField\":\"pubf\",\"subPrivateStr\":\"subPstr\"}";
    @Test
    public void AbstractClass() throws JsonProcessingException {
        SubClass subClass = new SubClass();
        subClass.ctor();
        SerialClass bClass = new SerialClass();
        bClass.setAbstractClass(subClass);
        List<SubClass> subClassList = new ArrayList<>();
        subClassList.add(subClass);
        subClassList.add(subClass);
        bClass.setSubClasses(subClassList);
        String jsonStr = mapper.writeValueAsString(bClass);
        SerialClass b = mapper.readValue(jsonStr, SerialClass.class);
    }

    @Test
    public void serialTest() throws JsonProcessingException {
        SerialClass serialClass = new SerialClass();
        serialClass.ctor();
        String jsonStr = mapper.writeValueAsString(serialClass);
        SerialClass ss = mapper.readValue(jsonStr,SerialClass.class);
    }
}
