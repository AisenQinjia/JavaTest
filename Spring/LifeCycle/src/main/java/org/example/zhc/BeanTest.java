package org.example.zhc;

import org.example.Log;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeanTest implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean {
    String name;
    ApplicationContext context;
    public BeanTest(){
        Log.info("````````BeanTestBeforeCtor ctor`````````````");
    }
    @Override
    public void setBeanName(String name) {
        Log.info("set bean name: %s", name);
        this.name = name;
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Log.info("set bean factory");
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Log.info("set app context");
        this.context =applicationContext;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        Log.info("after properties set");
    }
    @PostConstruct
    public void init(){
        Log.info("bean init");
    }





}
