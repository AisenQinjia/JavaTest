package org.example.zhc.util.zhc.validation;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.Error;
import com.cdancy.jenkins.rest.domain.common.IntegerResponse;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.domain.job.BuildInfo;
import com.cdancy.jenkins.rest.domain.job.Job;
import com.cdancy.jenkins.rest.domain.job.JobInfo;
import com.cdancy.jenkins.rest.domain.job.JobList;
import com.cdancy.jenkins.rest.domain.queue.QueueItem;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import com.cdancy.jenkins.rest.features.JobsApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jenkins 测试模块
 * @author aisen
 */
@Slf4j
public class JenkinsApp {
    public static JenkinsClient client;
    JobsApi jobsApi;
    public static final String ENDPOINT="http://10.100.3.3:8080";
    public static final String USER = "hvyt";
    @Before
    public void createClient(){
        client = JenkinsClient.builder()
                .endPoint(ENDPOINT)
                .credentials("aisen:123456")
                .build();
        jobsApi = client.api().jobsApi();

    }
    @Test
    public void connectTest(){
        SystemInfo systemInfo = client.api().systemApi().systemInfo();
        log.info("systemInfo: {}",systemInfo);
    }
    public static final String JOB_ROOT_FOLDER = "deploy";
    public static final String JOB_NAME_PREFIX = "deploy_";
    @Test
    public void findJob(){
        JobList jobList = jobsApi.jobList("deploy");
        log.info("jobInfo: {}",jobList);
    }
    @Test
    public void remoteDeploy() throws InterruptedException, IOException {
        JobList jobList = jobsApi.jobList(JOB_ROOT_FOLDER);
        String jobName = JOB_NAME_PREFIX + USER;
        //查看是否已经创建此job
        boolean hasJob = false;
        for(Job job:jobList.jobs()){
            if(job.name().equals(jobName)){
                hasJob = true;
                break;
            }
        }

        if(!hasJob){
            //创建job
            InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("job.xml");
            byte[] bytes =new byte[configStream.available()];
            configStream.read(bytes);
            String config =new String(bytes, StandardCharsets.UTF_8);
            RequestStatus status =  jobsApi.create(JOB_ROOT_FOLDER,jobName,config);
            dealWithErrors("Unable to create job", status.errors());
            log.info("Job {} successfully created", jobName);
        }else{
            log.info("job {} found",jobName);
        }

        Map<String,List<String>> buildParams = new HashMap<>();
        buildParams.put("paramStr", Collections.singletonList("tttttttt"));
        IntegerResponse queueId = jobsApi.buildWithParameters(JOB_ROOT_FOLDER,jobName,buildParams);
        dealWithErrors("Unable to submit build", queueId.errors());
        log.info("Build successfuly submitted with queue id: " + queueId.value());
        QueueItem queueItem = client.api().queueApi().queueItem(queueId.value());
        while (true) {
            if (queueItem.cancelled()) {
                throw new RuntimeException("Queue item cancelled");
            }

            if (queueItem.executable()!=null) {
                log.info ("Build is executing with build number: " + queueItem.executable().number());
                break;
            }

            Thread.sleep(10000);
            queueItem = client.api().queueApi().queueItem(queueId.value());
        }

        BuildInfo buildInfo = jobsApi.buildInfo(JOB_ROOT_FOLDER, jobName, queueItem.executable().number());
        while (buildInfo.result() == null) {
            Thread.sleep(10000);
            buildInfo = client.api().jobsApi().buildInfo(JOB_ROOT_FOLDER, jobName, queueItem.executable().number());
        }
        log.info("Build status: {}", buildInfo.result());
    }
    @Test
    public void deleteJobAndWorkspace(){
        //貌似只能删除master上的
        boolean b1 = false;
        boolean b2 = false;

        System.out.println(String.format("%s",b1==b2) );
    }
    void dealWithErrors(String msg, List<Error> errors) {
        if (errors.size() > 0) {
            for (Error error : errors) {
                log.error("Exception: {}", error.exceptionName());
            }
            throw new RuntimeException(msg);
        }
    }

}
