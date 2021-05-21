package org.example.zhc.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.example.Log;
import org.example.zhc.redis.pool.LettucePool;
import org.example.zhc.redis.util.LuaScriptConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

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

}
