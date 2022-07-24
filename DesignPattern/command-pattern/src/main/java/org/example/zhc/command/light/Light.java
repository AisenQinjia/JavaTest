package org.example.zhc.command.light;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Light {
    public void on(){
        log.info("light on!");
    }
    public void off(){
        log.info("light off!");
    }
}
