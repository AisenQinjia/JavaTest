package org.example.zhc.validation;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

public class EnumApp {
    @Test
    public void enumTest(){
//        assert false;
//        LogicType[] values = LogicType.values();
//        for(LogicType type: values){
//            System.out.println("type.name: "+type.name());
//            System.out.println("type.ordinal: "+type.ordinal());
//            System.out.println("type.type: "+type.type());
//        }
        try {
            ttt();
        }catch (Exception e){
            System.out.println("fff");
        }
    }

    public void ttt(){
        try {
            throw new RuntimeException("ff");
        }finally {
            System.out.println("rrr");
        }
    }
}
