package org.example.zhc.singleton;

import java.security.PublicKey;

public class FieldSingleton implements SSingleton {
    public final static FieldSingleton FIELD_SINGLETON = new FieldSingleton();
    private FieldSingleton(){

    }
    @Override
    public String getMyName() {
        return FieldSingleton.class.getSimpleName();
    }
}
