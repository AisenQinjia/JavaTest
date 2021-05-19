package org.example.zhc.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.resource.DefaultEventLoopGroupProvider;
import io.lettuce.core.resource.Delay;
import io.netty.channel.nio.NioEventLoopGroup;
import org.example.Log;
import org.example.zhc.redis.pool.LettucePool;
import org.example.zhc.redis.util.LuaScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 不同库下redis实现
 * @author zhanghaochen
 */
public class RedisApp {
    public static CountDownLatch countDownLatch =new CountDownLatch(1);
    public static final String URL ="redis";
    public static final int PORT =30379;
    public static final int DATABASE =0;

    public RedisClient redisClient;


    public static void main(String[] args){}
    public void createLettuceClient(){
        RedisURI uri = RedisURI.builder()
                .withHost(URL)
                .withPort(PORT)
                .withDatabase(DATABASE)
                .build();
        redisClient = RedisClient.create(uri);
    }

    @Before
    public void poolCilent(){
        createLettuceClient();
    }
    @After
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
            LettucePool lettucePool = new LettucePool(redisClient);
            String[] keys = {"testLink","zzzz"};
            byte[][] args = new byte[2][];
            args[0] = "stateful-word".getBytes();
            args[1] = "1621394822001".getBytes();
            Flux<Object> result =  lettucePool.evalAsyncReactive(LuaScript.statefulGet(),keys,args);
            result.subscribe(ret->{
                List<byte[]> a = (List)ret;
                for(byte[] bytes: a){

                    Log.info(new String(bytes, StandardCharsets.UTF_8));
                }
                ret = null;
            });
        }catch (Exception e){
            Log.info(e.getMessage());
        }

    }

}
