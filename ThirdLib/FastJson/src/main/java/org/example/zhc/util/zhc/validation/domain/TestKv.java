package org.example.zhc.util.zhc.validation.domain;

public class TestKv<T> {
    public T genericField;
    public void ctor(T f){

        genericField = f;
    }
}
