package org.example.zhc.validation;

import org.example.Log;

public class BeanByFactoryBean {

    public void greeting(){
        Log.info("hello this is a bean created by a factory bean");
    }

    public BeanByFactoryBean(String name){

    }
}
