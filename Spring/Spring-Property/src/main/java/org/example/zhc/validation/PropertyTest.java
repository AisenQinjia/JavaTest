package org.example.zhc.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Slf4j
@Component
public class PropertyTest {
    @Value("#{'${key1}'.split(',')}")
    private Set<String> value;

    @PostConstruct
    public void init(){
        log.info("value: {}",value);
    }
}
