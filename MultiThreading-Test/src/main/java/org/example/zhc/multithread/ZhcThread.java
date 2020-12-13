package org.example.zhc.multithread;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZhcThread  {
    public static SynchronizedClass sharedClass = new SynchronizedClass();
    public static Map<String,String> sharedMap = new HashMap<>();
    public static void main(String[] args){
        SpringApplication.run(ZhcThread.class);
        log.info("current thread: {}, state: {}", Thread.currentThread().getName(), Thread.currentThread().getState());
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
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                 for(int i = 0; i < 9999; i++){
                     log.info("thread1 <<<<<<<<<");
                     ZhcThread.sharedMap.put("thread","1");
                     log.info("thread1 >>>>>>>>>");
                     synchronized (this){

                     }
                 }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for(int i = 0; i <9999; i++){
                    log.info("thread2 <<<<<<<<");
                    ZhcThread.sharedMap.put("thread","2");
                    log.info("thread2 >>>>>>>>");
                }
            }
        });
        thread1.start();
        thread2.start();

    }

}
@Getter
@Slf4j
class SynchronizedClass{
    public String b;
    @SneakyThrows
    public synchronized void wirteB(String b){
        log.info("{} is writing", b);
        Thread.sleep(1000);
        this.b = b;
        log.info("{} writeB finish", b);
    }
}
