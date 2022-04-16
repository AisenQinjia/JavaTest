package org.example.zhc.validation;

import org.springframework.beans.factory.FactoryBean;

public class FactoryBeanTest<T> implements FactoryBean<T> {
    @Override
    public T getObject() throws Exception {
        return (T)new BeanByFactoryBean("1");
    }

    @Override
    public Class<?> getObjectType() {
        return BeanByFactoryBean.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
