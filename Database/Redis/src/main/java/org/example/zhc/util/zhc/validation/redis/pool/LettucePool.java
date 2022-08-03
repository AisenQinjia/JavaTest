package org.example.zhc.util.zhc.validation.redis.pool;

import io.lettuce.core.RedisClient;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.api.sync.RedisScriptingCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * redis 连接 lettuce 实现
 * @author aisen
 */
public class LettucePool {
    RedisClient redisClient;

    StatefulRedisConnection<String, String> connection;
    StatefulRedisConnection<byte[], byte[]> byteConnection;
    StatefulRedisConnection<String, byte[]> stringByteConnection;

    public LettucePool(RedisClient redisClient){
        this.redisClient = redisClient;
        this.connection = redisClient.connect();
        this.byteConnection = redisClient.connect(ByteArrayCodec.INSTANCE);

        this.stringByteConnection = redisClient.connect(new RedisCodec<String, byte[]>() {
            @Override
            public String decodeKey(ByteBuffer byteBuffer) {
                return StringCodec.UTF8.decodeKey(byteBuffer);
            }

            @Override
            public byte[] decodeValue(ByteBuffer byteBuffer) {
                return ByteArrayCodec.INSTANCE.decodeValue(byteBuffer);
            }

            @Override
            public ByteBuffer encodeKey(String s) {
                return StringCodec.UTF8.encodeKey(s);
            }

            @Override
            public ByteBuffer encodeValue(byte[] bytes) {
                return ByteArrayCodec.INSTANCE.encodeValue(bytes);
            }
        });
    }

    public Flux<Object> evalAsyncReactive(String script, String[] keys, byte[]... args){
        RedisReactiveCommands<String, byte[]> commands = stringByteConnection.reactive();
        return commands.eval(script, ScriptOutputType.MULTI, keys, args);
    }

    public Flux<Object> evalAsyncReactive(String script, String[] keys, String... args){
        RedisReactiveCommands<String, String> commands = connection.reactive();
        return commands.eval(script, ScriptOutputType.MULTI, keys, args);
    }

}
