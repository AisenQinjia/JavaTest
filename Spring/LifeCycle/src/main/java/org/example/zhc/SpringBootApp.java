package org.example.zhc;

import org.example.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;


@SpringBootApplication(scanBasePackages = "org.example")
public class SpringBootApp implements CommandLineRunner {
    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] arg){
        try {
            Log.info("SpringApplication.run Begin");
            SpringApplication.run(SpringBootApp.class);
            Log.info("SpringAppli cation.run End");
            countDownLatch.await();
        }catch (Exception e){
            System.out.println("exception");
        }

    }
    @Override
    public void run(String... args) throws Exception {
        Log.info("CommandLineRunner run");
    }
}
