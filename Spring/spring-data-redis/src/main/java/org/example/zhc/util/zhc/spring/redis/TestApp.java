package org.example.zhc.util.zhc.spring.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class TestApp {
    static CountDownLatch downLatch=new CountDownLatch(1);
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
