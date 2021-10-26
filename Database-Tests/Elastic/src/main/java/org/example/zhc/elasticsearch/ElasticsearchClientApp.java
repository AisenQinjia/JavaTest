package org.example.zhc.elasticsearch;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ElasticsearchClientApp {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    static RestClient restClient;
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
    @Before
    public void init(){
        RestClientBuilder builder = RestClient.builder(new HttpHost("10.100.3.2",9200));
        builder.setFailureListener(new RestClient.FailureListener(){
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
                System.out.println(node.getHost() + "failed!");
            }
        });
        restClient = RestClient.builder(new HttpHost("10.100.3.2",9200)).build();
    }
    @Test
    public void getTest(){
        req = new Request("GET","/customer/_doc/1");
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
        req = new Request("PUT","/my-metrics-stream/_bulk");
        req.setJsonEntity(bulkStream);
    }

    @After
    public void end() throws InterruptedException {
        restClient.performRequestAsync(req, new ResponseListener() {
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
