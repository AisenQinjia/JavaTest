package org.example.zhc.validation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private static final Set<String> lock = new HashSet<>();
    private void lockObj(){

        synchronized (lock){
            System.out.println("lock success!");
        }
    }

    @Test
    public void testLock(){
        lock.add("1");
        lock.add("2");
        for(String str: lock){
            lock.remove("1");
        }
    }
}
