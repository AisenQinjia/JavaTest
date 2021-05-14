package org.example.zhc.serialization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Serial1 implements Serializable {
    public int publicInt;
    public Integer publicInteger;
    protected int protectedInt;
    protected Integer protectedInteger;
    private int privateInt;
    private Integer privateInteger;

    public static void methodStaticOverride(){
        System.out.println("1111");
    }

    protected void overrideTest(){
        System.out.println("1111");
    }

    private void privateOverride(){
        System.out.println("privateOverride 1111");
    }

    private final void privateFinalOvr(){

    }
}
