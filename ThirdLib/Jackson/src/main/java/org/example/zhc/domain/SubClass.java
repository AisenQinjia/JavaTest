package org.example.zhc.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubClass extends AbstractClass {
    private String subPrivateStr;

    public void ctor(){
        super.ctor();
        subPrivateStr = "SubPriStr";
    }
}
