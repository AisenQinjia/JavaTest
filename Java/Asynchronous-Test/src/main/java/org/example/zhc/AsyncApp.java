package org.example.zhc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.Log;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
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

    @SneakyThrows
    @Test
    public void futureTask(){
        ExecutorService threadPool = Executors.newCachedThreadPool();
        String[] strs = new String[]{"a","b","c"};
        List<Future<String>> tasks = new ArrayList<>();
        for(String str: strs){
            Future<String> futureTask = threadPool.submit(()-> str);
            tasks.add(futureTask);
        }
        for(val task: tasks){
            log.error("task: {}", task.get());
        }
    }

    @SneakyThrows
    @Test
    public void completableFuture(){
        CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync(()->Math.pow(2,13));
        log.error("res: {}",completableFuture.get());
    }
}