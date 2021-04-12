package org.example.zhc.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.operation.FindAndUpdateOperation;
import com.mongodb.internal.operation.UpdateOperation;
import com.mongodb.reactivestreams.client.*;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.Log;
import org.example.zhc.mongo.helper.Key;
import org.example.zhc.mongo.helper.SubscriberHelpers;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@SpringBootApplication(exclude = {MongoReactiveAutoConfiguration.class})
public class MongoApp implements CommandLineRunner {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    MongoClient mongoClient;
    MongoDatabase database;
    public MongoCollection<Document> testCollection;
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
        Mono.from(testCollection.updateOne(Filters.eq("main_id","22"),
                Updates.combine(Updates.set("updateAdd","pram"),Updates.set("isDel", true)),
                new UpdateOptions().upsert(true))
        ).subscribe(updateResult -> {Log.info("updateResult: %s",updateResult);},throwable -> Log.info("throwable: %s",throwable));
    }

    public void session(){
        ClientSession[] clientSessions = new ClientSession[1];
        Mono.from(mongoClient.startSession())
                .flatMap(clientSession->{
                    clientSessions[0] = clientSession;
                    clientSession.startTransaction();
                    Log.info("11111");
                    return Mono.from(testCollection.updateOne(clientSession,Filters.eq("ite", "fsdf"),
                            Updates.set("session","1 update"),
                            new UpdateOptions().upsert(true)));

                }).flatMap(rel->{
                    Log.info("22222222");
                    ClientSession clientSession = clientSessions[0];
                    val ss = clientSession.commitTransaction();
                    return Mono.from(ss);
                 }).subscribe(rel->{ Log.info("rellll");},throwable -> {Log.info("throwable: %s", throwable);});
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
    /**
     * 获取需要创建的索引
     * @return 需要创建的索引
     */
    public List<IndexModel> getDefaultIndexes(){
        List<IndexModel> defaultIndexes = new ArrayList<>();
        //主键index
        Bson primaryIndexBson = Indexes.compoundIndex(Indexes.hashed(Key.PRIMARY_KEY),Indexes.ascending((Key.DELETE_KEY)));
        IndexModel primaryIndex = new IndexModel(primaryIndexBson);
        //查询键
        Bson queryIndexBson = Indexes.compoundIndex(Indexes.hashed(Key.QUERY_ALL_KEY),Indexes.ascending((Key.DELETE_KEY)));
        IndexModel queryIndex = new IndexModel(queryIndexBson);
        defaultIndexes.add(primaryIndex);
        defaultIndexes.add(queryIndex);
        return defaultIndexes;
    }


    public void checkCreateIndex(String collectionName){
        Mono.from(database.getCollection(collectionName).createIndexes(getDefaultIndexes()))
                .subscribe(s -> Log.info("create indexes: %s",s),throwable -> Log.info("create indexes error: %s", throwable));

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

//        "mongodb://root:lHmKKJc3r9@8.136.208.139:27017/?authSource=admin"
        ConnectionString connString = new ConnectionString(
                "mongodb://root:H8oM55SbN4@10.100.2.90:27017/?authSource=admin"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("tpf_storage");

        testCollection = database.getCollection("test-collection2");

//        session();
//        incr();
//        update();
//        checkCreateIndex("test-index-one");
    }


}
