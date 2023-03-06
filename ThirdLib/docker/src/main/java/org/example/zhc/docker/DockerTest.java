package org.example.zhc.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.example.zhc.util.FileUtil;

import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isBlank;

public class DockerTest {
    static final String repo = "724563954217.dkr.ecr.us-west-1.amazonaws.com";
    static final String user = "aws";
    static final String password =  "eyJwYXlsb2FkIjoiRndPZFMzYXdtVWpSTlg5N1hMZVlMVFNjb2k1NlNxK2trcGswV3pqL2thQnh3L3hEbXhNbDl1c1FiRGt6Z2ZUMUJLUEhxdlFJem4zeEJadWlWa0VXblZsODB3NXpuY3F0OXg2ZEtSMXhQK3ZscGs2UU01a0VqSU5mQlVSM2E5RXVEc3krNTZTWW1rNW9MbFBqeXdaM0k3VWxzaEVOM20yeDdKb1pRcnRLL3hBRDF3Sk5jQzRrQnl5YnNVeURTOUhZS1l4VWFvUlVTU2xGSTBkNXg3ZVc3b2FYYXJFSTFPazlzWnhCVHZMV2xGVmpPd2NOQ1FSd0hSZVZDRWY5Rm42ckJYSzVlajhCTWF0TGREbGVyKzBtb0ZBYm1xa0VKUnFwSmNsUEFZVUdWcXpFYWVNVUd6aDd2TllhUXVmY3cxQ2NEb0RvZlBJeFVEMGxZY2prUWY5cFVScDZVOFBJMlJiZFJHNVRzdTdBOURQYjBTcFltTFZRcUNhTEE4ay9adjFpTWRkTWJrM0NiRExta3lWYzg1L21RU3lzUE9iYnR6M2VyL2ZQbE5rb3RlVkR4WWFFWHI1dDFMUFNwRVRhdGJEajdVSzNtcURGeVdpL3I2UVQvWHVZbm1HVzFLZE1IdFdldloxZDcvbFdleERCMFprZGlhTFlyR09HUmdpVm5LSWdvWXNBRk5iZXl0K25jNDJQcDM4V0tqQWZ3VXVoRkVQR3EyN3R1NlQ3dkFtSFpvV3Urckl3bkJEVmQ3b0NvL2dPSnZERUhLZ0FtRkgzNVpLNmcySDR2TFlWUkVqbE9ZRmhaZXhGOGdsM2Y0RU5pUUlpVXluRHdCMTFaUXZacERmN1FRYjlVU0FvUjljZ2Z2RjNoRG16VVFVNEh2YU9TZjdhYmpSSU5ER1Z0Z3NDK0d6YitrbnY1UlBEcGl5NnJSRlBZWmFIYWtmbmhSZUUwSzYxeVFZS3dnMHRzdTQvUU8vRWlQdytxQUZhOEZRQUxMNFZOOFN1WUtCZ3pZMjNERUFOMW5YVXNtVDZGaE8zTzBiYUxoRFNaVG9mWnQ5bE9OQ3JVVlJKcnB1UVFET0RiYjNDWlVtdGRuK2o3RWhURGNRSVU3L2E1cU0rendlNVhUd3VyVVJWQnNjWEFvVHM4N3BrVVFPcHhMdUErZUpVTm53QW00bnFhVGd5MGs1NUk2MjIzSEh0ZjFFKzBVVzk4SjhuYnphWHNQM3RvZnlBQVFPMlpFS2xFaDVWSXpwY3Q5WWpCSkFCZ2JybjdmK2YyTTFENEswdkRJMG11YTZndnlrY3hoMm9MWDFYbFREVU5tK3RSRzZVa3piemVScUEwaWllUWt5Q1pRS3hlWmxDUTJTWXpMbEcxclMyaG1qcmtYMmJoZz09IiwiZGF0YWtleSI6IkFRRUJBSGlqRUZYR3dGMWNpcFZPYWNHOHFSbUpvVkJQYXk4TFVVdlU4UkNWVjBYb0h3QUFBSDR3ZkFZSktvWklodmNOQVFjR29HOHdiUUlCQURCb0Jna3Foa2lHOXcwQkJ3RXdIZ1lKWUlaSUFXVURCQUV1TUJFRURDWWM2NHhiU0U3azB1QUcxUUlCRUlBN3Z4QU1VaDFkWXhnVTZkdHF4ZENpMzREMjhIVXB1bzdWV0lVdlYvMGtIOU95ZENnSkRWclBuQjJBYkpYbW9iWWozMnB0RUdUT1YzVkg3WW89IiwidmVyc2lvbiI6IjIiLCJ0eXBlIjoiREFUQV9LRVkiLCJleHBpcmF0aW9uIjoxNjc3MTc0NjI4fQ==";
    public static void main(String[] args) throws IOException, InterruptedException {
        DockerClient dockerClient = getDockerClient();
        String nameWithRepository = "default.registry.tke-syyx.com/syyx-tpf/tpf-managercenter-client";
        String tag = "1.0.1.2";
        dockerClient.pullImageCmd(nameWithRepository)
                .withTag(tag)
                .start()
                .awaitCompletion();
    }

    public static DockerClient getDockerClient() throws IOException {
        String host = isOSWindows() ? "tcp://localhost:2375" : "unix:///var/run/docker.sock";
        Properties properties = FileUtil.readFileAsProperties("config.properties.ignore", false);
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(host)
                .withRegistryUsername(properties.getProperty("registry-username"))
                .withRegistryPassword(properties.getProperty("registry-password"))
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
//        DockerHttpClient.Request request = DockerHttpClient.Request.builder()
//                .method(DockerHttpClient.Request.Method.GET)
//                .path("/containers/json")
//                .build();
//        String string = Base64.getEncoder().encodeToString("tkestack:eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE5ODA3NDcwMzUsImp0aSI6IjdhYTciLCJpYXQiOjE2NjUzODcwMzUsInVzciI6ImppYW50dSIsInRlZCI6ImRlZmF1bHQifQ.anEJJzQS1XYisFfMc1uWNMTo-ZKu7SDQh9X49sTnggvc3rUb-tusibjxBa2oittAOsRbgeL8d_7UD85bxiNIBaaKgrthMplZaU3ZuaRdziDE34mTkOZ8dH54gtpenpRAr7q45QtXAQ6ClE9PQaIs1Y21wb88mk5gzVVqOklXA9X0ZeHtMwZf-cZz-8y3bZX_bEnT6pqLO6XIaMA1FahYhyE8Qs2SXiHrayyYaBae1WMVBPwI2SdF8_fkpqAKnVe0OYUHvyq7IGAOMevusGuzYzvUNVafexwCC8jaBsU21bIPMhJVlYVLnX1BFwdsnR-ToGNuIX9u19azK6dEPLVIkA".getBytes());
        String string = Base64.getEncoder().encodeToString("account:password".getBytes());
//
//        try (DockerHttpClient.Response response = httpClient.execute(request)) {
//            System.out.println(response.getBody());
//        }
        return DockerClientImpl.getInstance(config, httpClient);
    }

    public static boolean isOSWindows() {
        String osName = System.getProperty("os.name");
        if (isBlank(osName)) {
            return false;
        }
        return osName.startsWith("Windows");
    }
}
