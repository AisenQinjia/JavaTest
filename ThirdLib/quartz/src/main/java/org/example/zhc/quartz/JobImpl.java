package org.example.zhc.quartz;

import org.quartz.*;

import java.util.Date;
import java.util.Map;

//@PersistJobDataAfterExecution
public class JobImpl implements Job {
    static int count = 0;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 获取当前时间
        Date now = new Date();

        // 获取触发时间
        Date fireTime = context.getFireTime();

        // 获取开始时间
        Date startTime = new Date(1000000l); // 获取开始时间的逻辑

        if (fireTime.compareTo(startTime) < 0) {
            // 如果触发时间早于开始时间，则说明是由于过去的开始时间而触发
            System.out.println("作业触发是由于过去的开始时间而触发");
        } else {
            // 否则，说明是由于调度器启动而触发
            System.out.println("作业触发是由于调度器启动而触发");
        }
        System.out.println("jobImpl");
//        JobDataMap mergedJobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
//        Integer runCount = (Integer)mergedJobDataMap.get("key");
////        System.out.println(runCount);
//        mergedJobDataMap.put("key",++runCount);
//        JobData jobData = (JobData)mergedJobDataMap.get("jobData");
////        System.out.println(jobData);
//        jobData.f().put("key",++count);
//        System.out.println(jobData.f().get("key"));
    }
}
