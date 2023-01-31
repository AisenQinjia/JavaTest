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
