package org.example.zhc.util.zhc.validation.redis;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.dynamic.RedisCommandFactory;
import io.lettuce.core.dynamic.batch.CommandBatching;
import org.example.zhc.util.Log;
import org.example.zhc.util.zhc.validation.redis.pool.LettucePool;
import org.example.zhc.util.zhc.validation.redis.util.LuaScriptConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 不同库下redis实现
 * @author zhanghaochen
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RedisApp {
    public static CountDownLatch countDownLatch =new CountDownLatch(1);
    public static final String URL ="redis";
    public static final int PORT =30379;
    public static final int DATABASE =0;

    public RedisClient redisClient;
    private static final String REDIS_UID_KEY_TMPL = "stateful:{%s:%s:%d}linking:%s";
    private static final String REDIS_SERVICE_STATE_KEY_TMPL = "stateful:{%s:%s:%d}state:%s";
    private static final String appId = "1";
    private static final String regionId = "1";
    private static final Integer logicType = 1;
    private static final String uid = "player_1";
    private static final String serviceName = "servicetype3:30002";

    public static void main(String[] args){}
    public void createLettuceClient(){
        RedisURI uri = RedisURI.builder()
                .withHost(URL)
                .withPort(PORT)
                .withDatabase(DATABASE)
                .build();
        redisClient = RedisClient.create(uri);
    }

    @BeforeAll
    public void poolClient(){
        createLettuceClient();
    }
    @AfterAll
    public void hold(){
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lettucePool(){
        try {
            LettucePool lettucePool =  new LettucePool(redisClient);
            String uidKey = String.format(REDIS_UID_KEY_TMPL,appId,regionId,logicType,uid);
            String serviceKey = String.format(REDIS_SERVICE_STATE_KEY_TMPL,appId,regionId,logicType,serviceName);
            String[] keys = {uidKey,serviceKey};
            String[] args = new String[2];
            args[0] = serviceName;
            args[1] = "1621480477661";
            String getLinkScript = LuaScriptConfig.statefulGet();
            Flux<Object> publisher = lettucePool.evalAsyncReactive(getLinkScript,keys,args);

            publisher.subscribe(ret->{
                try {
                    List<String> podList = (List<String>)ret;
                    String podNumStr = podList.get(0);
                    int podNum = Integer.parseInt(podNumStr);
                    if(podNum == -1){
                        Log.info("can not find a pod for uid: %s sevice:%s",uid,serviceName);
                    }else{
                        Log.info("getLinked Pod success: uid:%s service:%s,podId:%s",uid,serviceName,podNum);
                    }
                }catch (Exception e){
                    Log.info("getLinkedPod subscribe catch exception", e);
                }
            });
        }catch (Exception e){
            Log.info(e.getMessage());
        }

    }

    @Test
    public void pipeliningTest(){
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        //using pipelining
//        computePipTime(connection,commands,randomStr());
//        computePipTime(connection,commands,randomStr());
//        computePipTime(connection,commands,randomStr());
//        computePipTime(connection,commands,randomStr());
//        computePipTime(connection,commands,randomStr());
//        computePipTime(connection,commands,randomStr());
//        //disabling pipelining
//        computeNonPipTime(connection,commands,randomStr());
//        computeNonPipTime(connection,commands,randomStr());
//        computeNonPipTime(connection,commands,randomStr());
//        computeNonPipTime(connection,commands,randomStr());
//        computeNonPipTime(connection,commands,randomStr());

        computeReactiveTime(connection,randomStr());
    }

    private String randomStr(){
        return UUID.randomUUID().toString();
    }
    void computePipTime(StatefulRedisConnection<String, String> connection,RedisAsyncCommands<String, String> commands,String prefix){
        connection.setAutoFlushCommands(false);
        long t11 = System.currentTimeMillis();
        List<RedisFuture<?>> commandFutures23 = createCommand(commands,"pip" + prefix);
        commands.flushCommands();
        boolean result0 = LettuceFutures.awaitAll(5, TimeUnit.SECONDS,
                commandFutures23.toArray(new RedisFuture[0]));
        long t22 = System.currentTimeMillis();
        System.out.println("pipelining time: " + (t22-t11) + result0);
    }


    void computeNonPipTime(StatefulRedisConnection<String, String> connection,RedisAsyncCommands<String, String> commands,String prefix){
        connection.setAutoFlushCommands(true);
        long t33 = System.currentTimeMillis();
        List<RedisFuture<?>> commandFutures22 = createCommand(commands,"nop" + prefix);
        boolean result22 = LettuceFutures.awaitAll(5, TimeUnit.SECONDS,
                commandFutures22.toArray(new RedisFuture[0]));
        long t44 = System.currentTimeMillis();
        System.out.println("non-pipelining time: " + (t44-t33) + result22);
    }

    void computeReactiveTime(StatefulRedisConnection<String, String> connection,String prefix){
        connection.setAutoFlushCommands(true);
        long t11 = System.currentTimeMillis();
        RedisReactiveCommands<String, String> reactiveCommands = connection.reactive();
        List<Mono<?>> monoList = new ArrayList<>();
        for (int i =0 ; i< 1000;i++){
            monoList.add(reactiveCommands.set(prefix + "key-"+i,"val-"+i));
            monoList.add(reactiveCommands.expire("key-"+i,600));
        }
        List<Mono<?>> monoList1 = Flux.fromIterable(monoList).collectList().doOnNext(monos -> {
        }).blockOptional(Duration.ofSeconds(10)).get();
        long t22 = System.currentTimeMillis();
        System.out.println("pipelining time: " + (t22-t11) + "  ");
    }

    List<RedisFuture<?>> createCommand(RedisAsyncCommands<String, String> commands,String keyPrifix){
        List<RedisFuture<?>> futures = new ArrayList<>();
        for (int i =0 ; i< 1000;i++){
            futures.add(commands.set(keyPrifix + "key-"+i,"val-"+i));
            futures.add(commands.expire("key-"+i,600));
        }
        return futures;
    }
    @Test
    public void computePipTime2(){
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommandFactory redisCommandFactory = new RedisCommandFactory(connection, Arrays.asList(StringCodec.UTF8, StringCodec.UTF8));
        CustomCommand customCommand = redisCommandFactory.getCommands(CustomCommand.class);
        customCommand.set("sowhat","h",CommandBatching.queue());
        customCommand.set("sowhat2","h",CommandBatching.queue());
        customCommand.set("sowhat3","h",CommandBatching.flush());
    }
}
