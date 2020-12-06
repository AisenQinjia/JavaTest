package org.example.zhc.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
public class NeedProxy {
    @Value(value = "ssss")
    private String logInfo;

    public void a(){
        log.info(logInfo);
        String[] arrayA = new String[3];
        List<String> listA =  Arrays.asList(arrayA);
        List<String> sList = new ArrayList<>();
        String[] sArray =  sList.toArray(new String[0]);
    }

    @NeedProxyAnn
    public void b(){
        log.info("b");
    }
}
