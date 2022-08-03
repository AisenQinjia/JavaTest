package org.example.zhc.util.zhc.validation;

import org.example.zhc.util.Log;

public class BeanByFactoryBean {

    public void greeting(){
        Log.info("hello this is a bean created by a factory bean");
    }

    public BeanByFactoryBean(String name){

    }
}
