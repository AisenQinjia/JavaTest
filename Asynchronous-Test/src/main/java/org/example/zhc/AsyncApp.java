package org.example.zhc;

import org.example.Log;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class AsyncApp {
    public static void main(String[] args){

    }
    public static int count = 1;
    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    @Test
    public void manyThreadsTest() throws InterruptedException {
        int threadNum = 500;
        Thread[] threads = new Thread[threadNum];
        for(int i =0 ; i<threadNum;i++){
            threads[i] = new Thread(()->{
                UUID uuid = UUID.randomUUID();
                Log.info("begin thread! %s",count++);
                Log.info("begin thread! %s %s",count,Thread.currentThread().getState());
                ;
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.info("end thread %s", count);
            });
        }
        for (Thread thread: threads){
            thread.start();
        }
        countDownLatch.await();
    }
}
