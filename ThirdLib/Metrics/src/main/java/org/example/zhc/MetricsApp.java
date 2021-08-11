package org.example.zhc;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.sun.javafx.font.Metrics;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MetricsApp {
    static final MetricRegistry metrics = new MetricRegistry();
    static final CountDownLatch latch = new CountDownLatch(1);
    @Test
    public void report(){
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);
        Meter requests = metrics.meter("requests");
        Counter counter = metrics.counter("count");
        requests.mark();
        requests.mark();
        counter.inc();
        counter.inc();

    }
    @After
    public void hold() throws InterruptedException {
        latch.await();
    }
}
