package org.example.zhc.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @author aisen
 */
@Slf4j
@SpringBootApplication
public class PropertyApp implements CommandLineRunner {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args){
        SpringApplication.run(PropertyApp.class);
        try {
            System.exit(1);
            countDownLatch.await();
        }catch (Exception e){

        }
    }
    @Override
    public void run(String... args) throws Exception {
        log.info("property app run!");
    }
}
