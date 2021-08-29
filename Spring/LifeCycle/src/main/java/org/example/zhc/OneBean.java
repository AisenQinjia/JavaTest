package org.example.zhc;

import org.example.Log;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class OneBean implements BeanPostProcessor {
    static {
        Log.info("static filed init");
        String HELLO_WORLD = "hello world";
    }
    public OneBean(){
        Log.info("ctor");
    }
    @PostConstruct
    public void post(){
        Log.info("OneComponent post");
    }
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName){
        Log.info("Post Before: %s",beanName);
        return bean;
    }
    @PreDestroy
    public void destroy(){
        Log.info("destroy");
    }

}
