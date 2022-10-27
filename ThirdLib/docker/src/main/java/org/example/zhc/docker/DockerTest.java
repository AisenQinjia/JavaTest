package org.example.zhc.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.example.zhc.util.FileUtil;

import java.io.IOException;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isBlank;

public class DockerTest {

    public static void main(String[] args) throws IOException {
        DockerClient dockerClient = getDockerClient();

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
