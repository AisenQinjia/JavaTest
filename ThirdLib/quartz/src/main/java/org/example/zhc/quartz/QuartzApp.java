package org.example.zhc.quartz;

import javafx.scene.effect.Light;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.example.zhc.quartz.TimerConfig.TIMER_TYPE_EVERY_DAY;
import static org.example.zhc.quartz.TimerConfig.TIMER_TYPE_EVERY_WEEK;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzApp {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    static Scheduler scheduler;

    static {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
//            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        timerTest3();
//        timerTest1();

        long epochMilli = LocalDateTime.of(2023, 7, 7, 21, 42, 0).atZone(ZoneId.of("+1")).toInstant().toEpochMilli();
        System.out.println(epochMilli);
        System.out.println(System.currentTimeMillis());

        // and start it off

        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("key",0);
        Map<String,Integer> a = new HashMap<>();
        a.put("jobData",0);
        jobDataMap.put("jobData",new JobData(a));
        JobDetail job = newJob(JobImpl.class)
                .withIdentity("job1")
                .setJobData(jobDataMap)
                .build();
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(new Date(System.currentTimeMillis() - 1000000))
//                .endAt(new Date(System.currentTimeMillis() + 10000000))
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1))

//                .withSchedule(CronScheduleBuilder.cronSchedule("0 0,6 0,21 * * ? *"))
                .build();

        scheduler.scheduleJob(job, trigger);
//        scheduler.deleteJob("job1"));
        countDownLatch.await();

    }
    private static void timerTest3() throws SchedulerException, InterruptedException {
        JobDetail job = newJob(JobImpl.class)
                .withIdentity("job1")
                .build();

//timerConfig=TimerConfig(timerType=5, timerStartTime=1689004800000, timerEndTime=1690560000000, year=null, month=5, monthDay=4, week=null, hour=0, minute=0, second=2, oneTimeStamp=null)
        scheduler.scheduleJob(job, createTrigger("job1", new TimerConfig(TIMER_TYPE_EVERY_WEEK, 1000000l, null, null, null, null, 2, 14, 55,0,System.currentTimeMillis()+10*1000)));
        scheduler.start();
        countDownLatch.await();
    }
    private static void timerTest1() throws SchedulerException, InterruptedException {
        JobDetail job = newJob(JobImpl.class)
                .withIdentity("job1")
                .build();

//timerConfig=TimerConfig(timerType=5, timerStartTime=1689004800000, timerEndTime=1690560000000, year=null, month=5, monthDay=4, week=null, hour=0, minute=0, second=2, oneTimeStamp=null)
        scheduler.scheduleJob(job, createTrigger("job1", new TimerConfig(5, 0l, 1690560000000l, null, 5, 4, null, 0, 0,0,null)));
//        scheduler.deleteJob("job1"));
        countDownLatch.await();
    }

    private static void timerTest2() throws SchedulerException, InterruptedException {
        JobDetail job = newJob(JobImpl.class)
                .withIdentity("job1")
                .build();

//timerConfig=TimerConfig(timerType=5, timerStartTime=1689004800000, timerEndTime=1690560000000, year=null, month=5, monthDay=4, week=null, hour=0, minute=0, second=2, oneTimeStamp=null)
        scheduler.scheduleJob(job, createTrigger("job1", new TimerConfig(5, 0l, null, null, 5, 4, null, 0, 0,0,null)));
//        scheduler.deleteJob("job1"));
        countDownLatch.await();
    }

    private static Trigger createTrigger(String jobName, TimerConfig timerConfig) {
        long currentTimeMillis = System.currentTimeMillis();
        TriggerBuilder<Trigger> builder = newTrigger().withIdentity(jobName);
        Long timerEndTime = timerConfig.getTimerEndTime();
        Long timerStartTime = timerConfig.getTimerStartTime();
        //只有大于当前时间的开始发送时间才有意义,否则会立即发送
        if(timerStartTime != null ){
            builder.startAt(new Date(timerStartTime));
        }
        if(timerEndTime != null){
            builder.endAt(new Date(timerEndTime));
        }
        builder.startNow();
        Integer timerType = timerConfig.getTimerType();
        if(TimerConfig.TIMER_TYPE_ONE_TIME.equals(timerType)){
            long typeOnceTime = timerConfig.fetchTypeOnceTime();
            if(currentTimeMillis - typeOnceTime > 60*1000){
                throw new RuntimeException("定时时间:"+typeOnceTime+"不能早于当前时间:"+currentTimeMillis);
            }
            return builder.startAt(new Date(typeOnceTime)).build();
        }
        //cron表达式 seconds minutes hours dayOfMonth month dayOfWeek year
        String cronExpression = "";
        //seconds
        cronExpression = cronExpression + timerConfig.getSecond() + " ";
        //minutes
        cronExpression = cronExpression + timerConfig.getMinute() + " ";
        //hours
        cronExpression = cronExpression + timerConfig.getHour() + " ";
        //dayOfMonth
        if (TIMER_TYPE_EVERY_DAY.equals(timerType)) {
            cronExpression = cronExpression + "* * ? *";
        } else if(TimerConfig.TIMER_TYPE_EVERY_WEEK.equals(timerType)){
            cronExpression = cronExpression + "? * "+timerConfig.getWeek()+" *";
        }else if (TimerConfig.TIMER_TYPE_EVERY_MONTH.equals(timerType)) {
            cronExpression = cronExpression + timerConfig.getMonthDay() + " * ? *";
        } else if (TimerConfig.TIMER_TYPE_EVERY_YEAR.equals(timerType)) {
            cronExpression = cronExpression + timerConfig.getMonthDay() + " " + timerConfig.getMonth() + " ? *";
        }
        System.out.println("cronExpression: "+ cronExpression);
        return builder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
    }


}
