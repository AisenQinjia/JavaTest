package org.example.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication(scanBasePackages = "org.example")
public class SpringApp {
    static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(SpringApp.class);
        countDownLatch.await();
    }
}
