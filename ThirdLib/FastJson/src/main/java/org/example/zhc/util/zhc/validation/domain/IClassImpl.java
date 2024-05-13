package org.example.zhc.util.zhc.validation.domain;

import org.junit.Assert;

public class IClassImpl implements IClass {
    public int interfaceImpl;

    public FiledClass filedClass;
    public String getMyProperty() {
        return "getMethodReturn";
    }

    @Override
    public void ctor(){
        interfaceImpl = 1;
        filedClass = new FiledClass();
        filedClass.dd();
    }

    @Override
    public void assertEqual(IClass iClass) {
        Assert.assertEquals(interfaceImpl,((IClassImpl)iClass).interfaceImpl);
    }

    public Boolean isOpen(){
        return false;
    }

    String getAbc(){
        return "sdf";
    }
}
