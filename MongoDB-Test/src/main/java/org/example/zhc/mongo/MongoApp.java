package org.example.zhc.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.*;
import com.mongodb.internal.operation.FindAndUpdateOperation;
import com.mongodb.internal.operation.UpdateOperation;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.Log;
import org.example.zhc.mongo.helper.SubscriberHelpers;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class MongoApp implements CommandLineRunner {
    static CountDownLatch countDownLatch = new CountDownLatch(1);

    public MongoCollection testCollection;
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
        List<String> tds = Arrays.asList("a","b");
        Map<String,String> tt = new HashMap<>();
        tt.keySet();
        Log.info("async add document");

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
    public void bulkWrite(){
        Log.info("bulkWrite");

        val a = new UpdateOneModel<Document>(Filters.eq("hello", "wrq"),
                Updates.set("hello", "wrq2"),
                new UpdateOptions().upsert(true));
//        val b = new ReplaceOneModel<>(Filters.eq("updateAdd","sdfsdf"), new Document("add", "helper2"),new ReplaceOptions().upsert(true));
        val publisher = testCollection.bulkWrite(Arrays.asList(a),new BulkWriteOptions().ordered(false));
        SubscriberHelpers.BulkUpdateSubscribe sub = new SubscriberHelpers.BulkUpdateSubscribe();
        publisher.subscribe(sub);
    }

    public void update(){
        val publisher = testCollection.updateOne(Filters.eq("main_id","sdfsdfsdf"),
                Updates.combine(Updates.set("updateAdd","sdfsdf")),
                new UpdateOptions().upsert(true));
        subscribe(publisher);
    }

    public void find(){
        Bson[] filters = new Bson[2];
        filters[0] = Filters.eq("idDel",false);
        filters[1] = Filters.eq("key","key");
        val publisher = testCollection.find(Filters.and(filters));
        subscribe(publisher);

    }

    public void incr(){
        val f = Filters.eq("hello", "wrq");
        val u = Updates.inc("in1c",1);
        val pub = testCollection.findOneAndUpdate(Filters.eq("hello", "wrq1"),Updates.inc("inc",1),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));

        subscribe(pub);
    }

    public void createIndex(){
        val pub = testCollection.createIndex(Indexes.hashed("notExist"));
        subscribe(pub);
    }

    public void createIndexAndOther(){
        createIndex();
        update();
    }

    private <T> void subscribe(Publisher<T> publisher){
        val sub = new Subscriber<T>(){
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T t) {
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
        };
        publisher.subscribe(sub);

    }
    @Override
    public void run(String... args) throws Exception {
        Log.info("Application run!");
        ConnectionString connString = new ConnectionString(
                "mongodb://root:BEL2ow54Ny@10.100.1.166:27017/?authSource=admin"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("tpf_storage");
        testCollection = database.getCollection("test-collection2");
        createIndexAndOther();
    }
}
