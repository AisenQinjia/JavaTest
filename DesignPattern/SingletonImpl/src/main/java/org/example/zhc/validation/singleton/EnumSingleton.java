package org.example.zhc.validation.singleton;

public enum  EnumSingleton {
    INSTANCE;
    public String getMyName(){
        return getClass().getSimpleName();
    }

}
