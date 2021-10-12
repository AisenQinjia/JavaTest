package org.example.zhc;

import com.codahale.metrics.*;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MetricsApp {
    static final MetricRegistry metrics = new MetricRegistry();
    static final Timer response = metrics.timer("timer1");
    static final CountDownLatch latch = new CountDownLatch(1);
    @Test
    public void report() throws InterruptedException {

        Meter requests = metrics.meter("requests1");
        metrics.register("gauges1", new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return new Random().nextInt();
            }
        });
        Counter counter = metrics.counter("count1");
        Histogram histogram = metrics.histogram("histogram1");
        requests.mark();
        requests.mark();
        counter.inc();
        counter.inc();
        histogram.update(1);
        histogram.update(3);
        timerTest();
        CustomReporter customReporter = new CustomReporter(metrics,"custome reporter",MetricFilter.ALL,TimeUnit.SECONDS,TimeUnit.MILLISECONDS,null,true, Collections.emptySet());
        customReporter.b=customReporter.a;

        customReporter.start(1,TimeUnit.SECONDS);
    }

    private void timerTest() throws InterruptedException {
        try(final Timer.Context context = response.time();) {
            Thread.sleep(100);
        }
    }
    @After
    public void hold() throws InterruptedException {
        latch.await();
    }
}
