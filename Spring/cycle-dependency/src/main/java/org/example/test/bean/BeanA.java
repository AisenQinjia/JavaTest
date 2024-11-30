package org.example.test.bean;

import org.springframework.stereotype.Component;

/**
 * change className to BeanD, the cycular dependency is solved.
 */
@Component
public class BeanA {

    private BeanB beanB;
    public BeanA(BeanB beanB){
        this.beanB = beanB;
    }
}
