package org.example.zhc.util.zhc.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootApplication
public class WebApp implements CommandLineRunner{
    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args){
        SpringApplication.run(WebApp.class);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("web app running!");
    }
}
