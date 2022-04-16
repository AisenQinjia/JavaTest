package org.example.zhc.validation;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
@Setter
@Getter
public class ParamTest {
    @NotNull(message = "名字不能为空!")
    public String name;

}
