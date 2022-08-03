package org.example.zhc.util.zhc.validation.singleton;

public enum  EnumSingleton {
    INSTANCE;
    public String getMyName(){
        return getClass().getSimpleName();
    }

}
