package org.example.zhc.command.garage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GarageDoor {
    public void open(){
        log.info("garage door open!");
    }

    public void close(){
        log.info("garage door close!");
    }
}
