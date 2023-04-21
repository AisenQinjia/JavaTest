package org.example.zhc.docker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Base64;

public class CheckRemoteImage {
    public static void main(String[] args) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        //内网
        String account = "aaa";
        String password = "bbb";
        String registryDomain = "ccc";
        String dir = "/syyx-tpf/";
        String imageName = "tpf-managercenter-client";
        String tag = "1.0.0.11";
        String authDomain = registryDomain;

        boolean inRemoteRepository = isInRemoteRepository(registryDomain, dir,imageName, tag, authDomain, account, password);
        System.out.println(inRemoteRepository);
    }

    /**
     *
     * @param registryDomain
     * @param dir
     * @param imageName
     * @param tag
     * @param authDomain
     * @param account
     * @param password
     * @return
     * @throws IOException
     */
    public static boolean isInRemoteRepository(String registryDomain,String dir, String imageName, String tag,String authDomain,String account,String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String protocol = authDomain.startsWith("dockerauth") ?  "https://" : "http://";
        String registryUrl =protocol + registryDomain + "/v2/";
        String repository = dir + imageName;
        if(repository.startsWith("/")){
            repository = repository.substring(1);
        }
        String authUrl = protocol + authDomain + "/registry/auth?service=" + registryDomain + "&scope=repository:" + repository + ":pull";
        // Get the access token
        HttpGet httpGetToken = new HttpGet(authUrl);
        httpGetToken.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((account + ":" + password).getBytes()));
        String token = null;
        int tokenStatusCode = httpClient.execute(httpGetToken).getStatusLine().getStatusCode();
        if (tokenStatusCode == HttpStatus.SC_OK) {
            String tokenResponse = EntityUtils.toString(httpClient.execute(httpGetToken).getEntity());
            JSONObject tokenJson = JSON.parseObject(tokenResponse);
            token = tokenJson.getString("token");
        } else {
            throw new RuntimeException("Failed to get access token! authUrl: "+authUrl + " code: " + tokenStatusCode);
        }

        // Check if the image exists using the access token
        String imageUrl = registryUrl + repository + "/manifests/" + tag;
        HttpGet httpGet = new HttpGet(imageUrl);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        int statusCode = httpClient.execute(httpGet).getStatusLine().getStatusCode();
        httpClient.close();
        if (statusCode == HttpStatus.SC_OK) {
            System.out.println("Image exists in remote repository: " + imageUrl);
            return true;
        } else {
            System.out.println("Image does not exist in remote repository: " + imageUrl + " code: " + statusCode);
            return false;
        }
    }
}

