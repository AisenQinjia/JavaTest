package org.example.zhc.util.zhc.spring.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class RedisTestComponent implements CommandLineRunner {
    @Autowired
    ReactiveStringRedisTemplate reactiveRedisTemplate;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    StreamListenerExample listener;
    @Autowired
    RedisConnectionFactory connectionFactory;
    @Override
    public void run(String... args){
        int batchSize = 50;
        List<Object> keys = new ArrayList<>();
        for (int i = 0;i<batchSize;i++){
            String key = "key"+ i;
            keys.add(key);
//            redisTemplate.opsForHash().put(key,key,key);
//            redisTemplate.opsForHash().put("fixedKey1",key,key);
        }
//        reactiveRedisTemplate.opsForHash().multiGet("fixedKey1",keys)
//                .subscribe(ret->{
//                    log.info("singleGet ");
//                });
//        execute(keys,1,0,10);
//        flatMap(keys,1,0,10);
        mget(keys,1,0,10);
    }

    private void mget(List<Object> keys,int currentCount,long totalTime,int runCount){
        if(currentCount > runCount){
            log.info("averageTime:{}",(float)totalTime/(runCount - 1));
            return;
        }
        long begin = System.currentTimeMillis();
        reactiveRedisTemplate.opsForHash()
                .multiGet("fixedKey1",keys)
                .subscribe(ret->{
                    long end = System.currentTimeMillis();
                    long diff = end-begin;
                    log.info("mget diffTime:{} size:{}",diff,keys.size());
                    mget(keys,currentCount + 1,totalTime +(currentCount==1?0:diff),runCount);
                });
    }

    private void execute(List<String> keys,int currentCount,long totalTime,int runCount){
        if(currentCount > runCount){
            log.info("averageTime:{}",(float)totalTime/(runCount - 1));
            return;
        }
        long begin = System.currentTimeMillis();
        reactiveRedisTemplate.execute(connection -> Flux.fromIterable(keys).flatMap(key ->
                connection.hashCommands()
                        .hGet(ByteBuffer.wrap(key.getBytes()), ByteBuffer.wrap(key.getBytes()))))
                .collectList()
                .subscribe(ret->{
                    long end = System.currentTimeMillis();
                    long diff = end-begin;
                    log.info("execute diffTime:{} size:{}",diff,keys.size());
                    execute(keys,currentCount + 1,totalTime +(currentCount==1?0:diff),runCount);
                });
    }

    private void flatMap(List<String> keys,int currentCount,long totalTime,int runCount){
        if(currentCount > runCount){
            log.info("averageTime:{}",(float)totalTime/(runCount - 1));
            return;
        }
        long begin = System.currentTimeMillis();
        Flux.fromIterable(keys)
                .flatMap(key-> reactiveRedisTemplate.opsForHash().get(key,key)).collectList()
                .subscribe(ret->{
                    long end = System.currentTimeMillis();
                    long diff = end-begin;
                    log.info("flatMap diffTime:{} size:{}",end-begin,keys.size());
                    flatMap(keys,currentCount + 1,totalTime +(currentCount==1?0:diff),runCount);
                });
    }

    private void streamTest(){
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, byte[]>> containerOptions =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .keySerializer(StringRedisSerializer.UTF_8)
                        .<String, byte[]>hashKeySerializer(StringRedisSerializer.UTF_8)
                        .<String, byte[]>hashValueSerializer(RedisSerializer.byteArray())
                        .pollTimeout(Duration.ofMillis(1000))
                        .build();
        StreamMessageListenerContainer<String, MapRecord<String, String, byte[]>> container = StreamMessageListenerContainer.create(connectionFactory,
                containerOptions);
        long l = System.currentTimeMillis();
        String entryId = l + "-0";
        container.receive(StreamOffset.create("my-stream2",ReadOffset.lastConsumed()), listener);
        container.start();
    }
}
