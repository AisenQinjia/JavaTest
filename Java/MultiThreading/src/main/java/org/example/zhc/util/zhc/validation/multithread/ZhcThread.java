package org.example.zhc.util.zhc.validation.multithread;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZhcThread  {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static List<Integer> getList(){
        return null;
    }
    private  class InnerClass{

        int get(){
            return a;
        }
    }
    public static SynchronizedClass sharedClass = new SynchronizedClass();
    public static Map<String,String> sharedMap = new HashMap<>();
    public int a;
    public static void main(String[] args){
//        SpringApplication.run(ZhcThread.class);
//        log.info("current thread: {}, state: {}", Thread.currentThread().getName(), Thread.currentThread().getState());
//
//        List<Integer> integerList = getList();
//        for(val in:integerList){
//            log.info("sdf1");
//        }
//        String[] strs = new String[3];
//
//        integerList.removeIf(integer -> integer.equals(1));
//        for(Iterator<Integer> it = integerList.iterator(); it.hasNext();){
//
//        }

//        Thread thread1 = new Thread(new Runnable() {
//            @SneakyThrows
//            @Override
//            public void run() {
//                log.info("thread1 is sleeping 0.1s");
//                Thread.sleep(100);
//                log.info("thread1 write 11111111");
//                ZhcThread.sharedClass.wirteB("thread1");
//                log.info("thread1 read: {}", ZhcThread.sharedClass.getB());
//            }
//        });
//        Thread thread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                log.info("thread2 write 11111111");
//                ZhcThread.sharedClass.wirteB("thread2");
//                log.info("thread2 read: {}", ZhcThread.sharedClass.getB());
//            }
//        });
//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                 for(int i = 0; i < 9999; i++){
//                     log.info("thread1 <<<<<<<<<");
//                     ZhcThread.sharedMap.put("thread","1");
//                     log.info("thread1 >>>>>>>>>");
//                     synchronized (this){
//
//                     }
//                 }
//            }
//        });
//        Thread thread2 = new Thread(new Runnable() {
//            @SneakyThrows
//            @Override
//            public void run() {
//                for(int i = 0; i <9999; i++){
//                    log.info("thread2 <<<<<<<<");
//                    ZhcThread.sharedMap.put("thread","2");
//                    log.info("thread2 >>>>>>>>");
//                }
//            }
//        });
//        thread1.start();
//        thread2.start();
    }
    @After
    public void after() throws InterruptedException {
        countDownLatch.await();
    }
    public void init(){
        InnerClass a = new InnerClass();
        a.get();
    }
    public ThreadLocal<String> localThing = new ThreadLocal<>();

    @Test
    public void threadLocal(){
        for(int i =0;i<3;i++){
            final int  ii = i;
            Thread thread = new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+ " this thread num:" + localThing.get());
                localThing.set(String.valueOf(ii));
                System.out.println(Thread.currentThread().getName()+" this thread num:" + localThing.get());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" this thread num:" + localThing.get());
            });
            thread.start();
        }

    }
    
    @Test
    public void timerTask() throws InterruptedException {
        short a = Short.MAX_VALUE;
        a += 1;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int count;
            @Override
            public void run() {
                log.info("task-1 run count:{}",++count);
                if(count == 2){
                    throw new RuntimeException("exception!");
                }
            }
        },500,1000);
        timer.scheduleAtFixedRate(new TimerTask() {
            int count;
            @Override
            public void run() {
                log.info("task-2 run count:{}",++count);
            }
        },600,1000);
        Thread.sleep(3000);
        timer.scheduleAtFixedRate(new TimerTask() {
            int count;
            @Override
            public void run() {
                log.info("task-2 run count:{}",++count);
            }
        },600,1000);
    }

    @Test
    public void timerCancel() throws InterruptedException {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("run task!");
            }
        };
        timer.schedule(timerTask,0);
        Thread.sleep(1000);
        boolean cancel = timerTask.cancel();
        System.out.println("cancel timer ret:" + cancel);

    }
    @Test
    public void t(){
        SynchronizedClass synchronizedClass = new SynchronizedClass();
        Thread thread = new Thread(() -> {
            try {
                synchronizedClass.writeA("fsdf");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                synchronizedClass.writeA2("fsdf");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread2.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
@Getter
@Slf4j
class SynchronizedClass{
    public String b;

    public synchronized void writeA(String a) throws InterruptedException {
        System.out.println("1111111111111");
        Thread.sleep(1000);
        System.out.println("22222222222222");

    }
    public synchronized void writeA2(String a) throws InterruptedException {
        System.out.println("3333333333333");
        Thread.sleep(1000);
        System.out.println("444444444444444");

    }
    @SneakyThrows
    public synchronized void wirteB(String b){
        log.info("{} is writing", b);
        Thread.sleep(1000);
        this.b = b;
        log.info("{} writeB finish", b);
    }
}
