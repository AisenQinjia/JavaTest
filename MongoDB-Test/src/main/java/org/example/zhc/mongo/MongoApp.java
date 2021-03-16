package org.example.zhc.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.val;
import org.bson.Document;
import org.example.Log;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class MongoApp implements CommandLineRunner {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args){
        Log.info("start mongo main");
        SpringApplication.run(MongoApp.class);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addDoc(){
        Log.info("async add document");
        ConnectionString connString = new ConnectionString(
          "mongodb://root:BEL2ow54Ny@10.100.1.166:27017/?authSource=admin"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("tpf_storage");
        val testCollection = database.getCollection("test-collection");
        Log.info("111");
        testCollection.find().subscribe(new Subscriber<Document>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.info("onSubscribe");
                s.request(1);
            }

            @Override
            public void onNext(Document document) {
                Log.info("onNext");
            }

            @Override
            public void onError(Throwable t) {
                Log.info("onError");

            }

            @Override
            public void onComplete() {
                Log.info("onComplete");

            }
        });
        Log.info("222");

    }

    @Override
    public void run(String... args) throws Exception {
        Log.info("Application run!");
        addDoc();
    }
}
