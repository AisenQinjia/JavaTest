package org.example.zhc.util.zhc.validation.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Aspect
@Slf4j
@Component
public class NeedProxyAspect {
    @After(value = "execution(public void org.example.zhc.util.zhc.validation.aop.NeedProxy.a())")
    public void aAfter(){
        log.info("aAfter");
    }

    @Around("@annotation(org.example.zhc.util.zhc.validation.aop.NeedProxyAnn)")
    public void  logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("@@@@@@@");

//        joinPoint.proceed();
    }

    @PostConstruct
    public void init(){
        log.info("NeedProxyAspect init");
    }
}
