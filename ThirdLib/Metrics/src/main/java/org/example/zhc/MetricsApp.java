package org.example.zhc;

import com.codahale.metrics.*;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MetricsApp {
    static final MetricRegistry registry = new MetricRegistry();
    static  Timer timer1;
    static final CountDownLatch latch = new CountDownLatch(1);
    ConsoleReporter reporter;
    @Before
    public void init() {
//        metrics.register("gauges1", new Gauge<Integer>() {
//            @Override
//            public Integer getValue() {
//                return new Random().nextInt();
//            }
//        });
//        Counter counter = metrics.counter("count1");


//        counter.inc();
//        counter.inc();

//        CustomReporter customReporter = new CustomReporter(metrics,"custome reporter",MetricFilter.ALL,TimeUnit.SECONDS,TimeUnit.MILLISECONDS,null,true, Collections.emptySet());
//        customReporter.b=customReporter.a;
//
//        customReporter.start(1,TimeUnit.SECONDS);
    }

    @Test
    public void meter(){
        Meter requests = registry.meter("requests1");
        requests.mark();
        requests.mark();
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true){
//                    System.out.println("mark");
                    requests.mark();
                    Thread.sleep(1000);
                }
            }
        });
        thread.start();
    }

    @Test
    public void histogram(){
        Histogram histogram = registry.histogram("histogram1");
        Thread thread = new Thread(new Runnable() {
            int i = 0;
            @SneakyThrows
            @Override
            public void run() {
                while (true){
                    if(i<6){
                        histogram.update(1);
                    }else{
                        histogram.update(10);
                    }
                    i++;
                    Thread.sleep(1000);
                }
            }
        });
        thread.start();
    }

    @Test
    public void EDRHistogram(){

    }

    /**
     *
     */
    @Test
    public void STWAReservoir(){
        Histogram histogram = registry.histogram("sdf", () -> new Histogram(new SlidingTimeWindowArrayReservoir(5,TimeUnit.SECONDS)));
        Thread thread = new Thread(new Runnable() {
            int i = 0;
            @SneakyThrows
            @Override
            public void run() {
                while (true){
                    if(i<6){
                        histogram.update(1);
                    }else{
                        histogram.update(10);
                    }
                    i++;
                    Thread.sleep(1000);
                }
            }
        });
        thread.start();
    }
    @Test
    public void timer() throws InterruptedException {
        timer1 = registry.timer("process-time");
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true){
                    timerTest();
                    Thread.sleep(1000);
                }
            }
        });
        thread.start();
    }
//    public static int ti = 0;
    private void timerTest(){
        Timer.Context context = timer1.time();
        int i = 0;
        while (i< 1000){
            i++;
        }
        context.close();
    }

    private void slidingTime(){
//        SlidingTimeWindowReservoir
    }
    @After
    public void hold() throws InterruptedException {
        reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1,TimeUnit.SECONDS);
        latch.await();
    }
}
