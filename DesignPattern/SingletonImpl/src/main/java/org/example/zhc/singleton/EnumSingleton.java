package org.example.zhc.singleton;

public enum  EnumSingleton {
    INSTANCE;
    public String getMyName(){
        return getClass().getSimpleName();
    }

}
