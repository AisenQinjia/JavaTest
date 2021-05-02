package org.example.zhc.reflection;

import org.example.Log;

public class Class2Reflected implements Interface2Reflected{
    public int pubInt;
    protected int proInt;
    private int priInt;

    @Override
    public String interfaceMethod1() {
        return "impl 1";
    }

    public void reflectionMethod1(){
        Log.info("reflectionMethod1");
    }

    private void reflectionMethod2(String info){
        Log.info("reflectionMethod2 %s", info);
    }
}
