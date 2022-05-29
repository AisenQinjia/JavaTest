package org.example.zhc.validation;

import org.junit.jupiter.api.Test;
public class waitNofiyApp {
    public static final Object lock = new Object();
    public static void main(String[] args){

    }

    @Test
    public void waitNotify() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                t1Wait();
            } catch (InterruptedException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(this::t2Notify);
        t1.start();
        Thread.sleep(3000);
        t2.start();
        Thread.sleep(5000);
    }

    public String ret = "";
    public void t1Wait() throws InterruptedException {
        System.out.println("t1 wait");
        synchronized (lock){
            lock.wait();
        }
        System.out.println("t1 wake: "+ret);
    }

    public void t2Notify(){
        ret = "t2Notify";
        synchronized (lock){
            System.out.println("t2Nofity");
            lock.notify();
        }
    }
}
