package org.example.zhc.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class OkHttpApp {
    public static OkHttpClient httpClient;
    public static String url = "http://10.100.3.3:8080/job/deploy/job/deploy_hvyt_1.0.0.15/1/consoleText";
    @Before
    public void init(){
        httpClient = new OkHttpClient();
    }
    @Test
    public void getMethod(){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body()!=null){
                String consoleInfo =  response.body().string();
                System.out.println(consoleInfo);
            }
        } catch (IOException e) {
            System.out.println("updateBuildConsole exception" + e.getMessage());
        }
    }
}
