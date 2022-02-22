package org.example.zhc.domain;

public class TestKv<T> {
    public T genericField;
    public void ctor(T f){

        genericField = f;
    }
}
