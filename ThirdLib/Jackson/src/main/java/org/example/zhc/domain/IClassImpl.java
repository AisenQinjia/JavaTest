package org.example.zhc.domain;

public class IClassImpl implements IClass {
    private int interfaceImpl;
    public String getMyProperty() {
        return "getMethodReturn";
    }

    @Override
    public void ctor(){
        interfaceImpl = 1;
    }
}
