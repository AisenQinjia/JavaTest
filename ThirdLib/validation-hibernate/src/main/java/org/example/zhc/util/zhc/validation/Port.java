package org.example.zhc.util.zhc.validation;

public class Port {
    Integer outPort;
    Integer inPort;

    public void init(){
        assert  outPort != null;
        if(inPort == null){
            inPort = outPort;
        }
    }


}
