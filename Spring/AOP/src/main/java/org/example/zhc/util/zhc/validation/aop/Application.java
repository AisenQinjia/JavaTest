package org.example.zhc.util.zhc.validation.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    NeedProxy needProxy;
    @Autowired
    ParamTest paramTest;
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) {
        SpringApplication.run(Application.class);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application run!");
        needProxy.a();
        needProxy.b();
        paramTest.check(null);
    }
}
