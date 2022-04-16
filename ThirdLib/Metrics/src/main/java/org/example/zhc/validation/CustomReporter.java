package org.example.zhc.validation;

import com.codahale.metrics.*;

import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomReporter extends ScheduledReporter {
    private int c;
    int a;
    protected int b;
    public CustomReporter(MetricRegistry registry,
                          String name,
                          MetricFilter filter,
                          TimeUnit rateUnit,
                          TimeUnit durationUnit,
                          ScheduledExecutorService executor,
                          boolean shutdownExecutorOnStop,
                          Set<MetricAttribute> disabledMetricAttributes){
        super(registry,name,filter,rateUnit,durationUnit,executor,shutdownExecutorOnStop,disabledMetricAttributes);
    }
    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        System.out.println("fds");

    }
}
