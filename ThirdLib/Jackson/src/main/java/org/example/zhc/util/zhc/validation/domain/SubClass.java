package org.example.zhc.util.zhc.validation.domain;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;

@Setter
@Getter

public class SubClass extends AbstractClass {
    private String subPrivateStr;
    private String name;
    public void ctor(){
        super.ctor();
        subPrivateStr = "SubPriStr";
        name = "subClass";
    }

    @Override
    public void assertEqual(AbstractClass abstractClass) {
        super.assertEqual(abstractClass);
        Assert.assertEquals(subPrivateStr,((SubClass)abstractClass).subPrivateStr);
    }

    public void assertEqual(SubClass subClass){
        Assert.assertEquals(subPrivateStr, subClass.subPrivateStr);
    }

    @Override
    public boolean equals(Object obj) {
        return subPrivateStr.equals(((SubClass)obj).subPrivateStr);
    }
}
