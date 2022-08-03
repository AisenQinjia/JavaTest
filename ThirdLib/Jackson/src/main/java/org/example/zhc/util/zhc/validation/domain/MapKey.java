package org.example.zhc.util.zhc.validation.domain;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;
import java.util.UUID;

public class MapKey extends KeyDeserializer {
    private String key;
    public void ctor(){
        key = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        MapKey key1 = new MapKey();
        key1.key = key;
        return key1;
    }
}
