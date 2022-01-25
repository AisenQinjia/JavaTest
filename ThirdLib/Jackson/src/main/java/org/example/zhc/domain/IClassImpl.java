package org.example.zhc.domain;

import org.junit.Assert;

public class IClassImpl implements IClass {
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
        Assert.assertEquals(interfaceImpl,((IClassImpl)iClass).interfaceImpl);
    }

}
