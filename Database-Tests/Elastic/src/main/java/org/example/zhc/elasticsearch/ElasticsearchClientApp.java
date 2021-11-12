package org.example.zhc.elasticsearch;

import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.PasswordAuthentication;
import java.util.concurrent.CountDownLatch;

public class ElasticsearchClientApp {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    static RestClientBuilder restClientBuilder;
    static Request req;
    static String jsonData =  "{\"timestamp\":\"1100000\",\"value\":3}";
    //remember the last line must add a newline as well :(
    static String bulkData = "{ \"index\" : { \"_index\" : \"customer\"} }\n" +
            "{ \"field1\" : \"value1\" }\n" +
            "{ \"index\" : { \"_index\" : \"customer\"} }\n" +
            "{ \"field1\" : \"value1\" }\n";

    static String bulkStream = "{ \"create\":{ } }\n" +
            "{\"@timestamp\":1634562370973,\"regionId\":\"region2\", \"value\":10,\"name\":\"gauge\"}\n" +
            "{ \"create\":{ } }\n" +
            "{\"@timestamp\":1634562470974,\"regionId\":\"region2\", \"value\":10,\"name\":\"gauge\"}\n" +
            "{ \"create\":{ } }\n" +
            "{\"@timestamp\":1634562570976,\"regionId\":\"region2\", \"value\":10,\"name\":\"gauge\"}\n";

    static String HOST1 = "";
    static String HOST2 = "10.100.3.2";

    @Before
    public void init(){
        restClientBuilder = RestClient.builder(new HttpHost(HOST2,9200));
    }
    @Test
    public void getTest(){
        req = new Request("GET","/customer/_doc/1");
    }

    @Test
    public void auth(){
        restClientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                BasicCredentialsProvider provider = new BasicCredentialsProvider();
                provider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("name","pwd"));
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(provider);
            }
        });
        put();
    }
    @Test
    public void put(){
        req = new Request("PUT","/customer/_doc/1");
        req.setJsonEntity(jsonData);
    }
    @Test
    public void post(){
        req = new Request("POST", "/customer/_doc/");
        req.setJsonEntity(jsonData);
    }

    @Test
    public void bulkPost(){
        req = new Request("POST", "/_bulk");

        req.setJsonEntity(bulkData);
    }

    @Test
    public void bulkStream(){
        req = new Request("PUT","/tpf-metrics-stream/_bulk");
        req.setJsonEntity(bulkStream);
    }

    @After
    public void end() throws InterruptedException {
        restClientBuilder.build().performRequestAsync(req, new ResponseListener() {
            @SneakyThrows
            @Override
            public void onSuccess(Response response) {
                System.out.println("success");
                System.out.println(EntityUtils.toString(response.getEntity()));
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("exp");
                System.out.println(e.getMessage());
            }
        });
     countDownLatch.await();
    }
}
