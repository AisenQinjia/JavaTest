package org.example.zhc.validation.singleton;

public class FieldSingleton implements SSingleton {
    public final static FieldSingleton FIELD_SINGLETON = new FieldSingleton();
    private FieldSingleton(){

    }
    @Override
    public String getMyName() {
        return FieldSingleton.class.getSimpleName();
    }
}
