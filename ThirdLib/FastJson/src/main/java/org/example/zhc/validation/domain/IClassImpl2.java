package org.example.zhc.validation.domain;

import org.junit.Assert;

public class IClassImpl2 implements IClass {
    private int interfaceImpl;
    public String getMyProperty() {
        return "getMethodReturn";
    }

    @Override
    public void ctor(){
        interfaceImpl = 1;
    }

    @Override
    public void assertEqual(IClass iClass) {
        Assert.assertEquals(interfaceImpl,((IClassImpl2)iClass).interfaceImpl);
    }

}
