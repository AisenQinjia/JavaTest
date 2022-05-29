package org.example.zhc.validation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.zhc.validation.domain.getter.GetterClass;
import org.example.zhc.validation.domain.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JacksonApp {
    static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    static { mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE));}

    public static ObjectMapper mapper2 = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
    static { mapper2.setVisibility(mapper2.getSerializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
            .withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
            .withIsGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
            .withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
            .withCreatorVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY));}
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
    public void serialTest() throws JsonProcessingException {
        SerialClass<IClass> serialClass = new SerialClass<>();

        serialClass.ctor();
        String jsonStr = mapper.writeValueAsString(serialClass);
        SerialClass<IClass> ss = mapper.readValue(jsonStr, new TypeReference<SerialClass<IClass>>() {});
        serialClass.assertEqual(ss);
        System.out.println(mapper.writeValueAsString(ss));
    }

    @Test
    public void serialBytesTest() throws IOException {
        SerialClass<IClass> serialClass = new SerialClass<>();
        serialClass.ctor();
        byte[] serialBytes = mapper.writeValueAsBytes(serialClass);
        SerialClass<IClass> ss = mapper.readValue(serialBytes, new TypeReference<SerialClass<IClass>>() {});
        serialClass.assertEqual(ss);
        System.out.println(mapper.writeValueAsString(ss));
    }

    @Test
    public void test2() throws JsonProcessingException {
        GetterClass getterClass = new GetterClass();
        getterClass.setEnabled(true);
        String jsonStr = mapper2.writeValueAsString(getterClass);
        GetterClass ss = mapper2.readValue(jsonStr,GetterClass.class );
    }
    public static void main(String[] args) throws IOException {
        ClassA a = new ClassA();
        ClassA a2 = new ClassA();
        ClassB b = new ClassB();
        a.classB =b;
        b.classA = a;
        b.classA2 = a2;
        String jsonStr = mapper.writeValueAsString(a);
        ClassA ss = mapper.readValue(jsonStr,ClassA.class );
    }
}
