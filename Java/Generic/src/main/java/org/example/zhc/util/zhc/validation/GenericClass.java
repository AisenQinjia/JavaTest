package org.example.zhc.util.zhc.validation;

public class GenericClass<T> {

    private final Class<T> type;

    public GenericClass(Class<T> type) {
        this.type = type;
    }
    public Class<T> getMyType() {
        return this.type;
    }
}
