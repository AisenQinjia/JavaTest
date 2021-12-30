package org.example.zhc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.zhc.domain.AClass;
import org.junit.Test;

public class JacksonApp {
    @Test
    public void POJOs() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AClass aClass =  mapper.readValue("{\"name\":\"Bob\", \"age\":13}", AClass.class);

    }
}
