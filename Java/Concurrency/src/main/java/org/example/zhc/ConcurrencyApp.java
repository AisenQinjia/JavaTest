package org.example.zhc;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author aisen
 */
public class ConcurrencyApp {
    public static void main(String[] args){

    }
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static volatile boolean shouldStop;
    @Test
    public void hoistingTest() throws InterruptedException {
        Thread thread = new Thread(()->{
            int i = 0;
            while (!shouldStop){
                i++;
            }
            System.out.println("end");
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        shouldStop = true;
        countDownLatch.await();
    }
}
