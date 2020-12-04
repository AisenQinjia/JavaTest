package org.example.zhc.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class NeedProxy {
    public void a(){
        log.info("a");
    }

    @NeedProxyAnn
    public void b(){
        log.info("b");
    }
}
