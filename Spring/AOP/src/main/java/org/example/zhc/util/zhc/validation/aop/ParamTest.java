package org.example.zhc.util.zhc.validation.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
@Slf4j
@Component
public class ParamTest {
    @NotNull(message = "名字不能为空!")
    public String name;

    public void check(@Valid ParamTest paramTest){
        log.info("paramTest {}",paramTest);
    }
}
