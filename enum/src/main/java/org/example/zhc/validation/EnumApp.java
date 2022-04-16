package org.example.zhc.validation;

import org.junit.Test;

public class EnumApp {
    @Test
    public void enumTest(){
        assert false;
        LogicType[] values = LogicType.values();
        for(LogicType type: values){
            System.out.println("type.name: "+type.name());
            System.out.println("type.ordinal: "+type.ordinal());
            System.out.println("type.type: "+type.type());
        }
    }
}
