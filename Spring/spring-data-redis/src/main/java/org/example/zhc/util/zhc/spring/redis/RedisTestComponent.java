package org.example.zhc.util.zhc.spring.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

@Configuration
public class RedisTestComponent implements CommandLineRunner {
    @Autowired
    ReactiveStringRedisTemplate redisTemplate;
    @Autowired
    StreamListenerExample listener;
    @Autowired
    RedisConnectionFactory connectionFactory;
    @Override
    public void run(String... args){
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
