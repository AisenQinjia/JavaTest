package org.example.zhc.util.zhc.validation.serialization;

public class Serial2 extends Serial1 {
    public int propertyA = 5;
    public Serial2(){
        super(1,1,1,1,1,1,"sr".getBytes());
    }

    public static void methodStaticOverride(){
        System.out.println("222222");
    }

    protected void overrideTest(){
        System.out.println("222222");
    }

    private void privateOverride(){
        System.out.println("privateOverride 222");
    }

    private final void  privateFinalOvr(){

    }

    public String getPropertyA(){
        return "sdf";
    }
}
