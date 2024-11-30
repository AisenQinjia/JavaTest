package org.example.test.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanC {
    @Autowired
    BeanA beanA;
}
