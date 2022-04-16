package org.example.zhc.validation.redis;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.batch.CommandBatching;


public interface CustomCommand extends Commands {
    void set(String key, String value);

    RedisFuture<String> set(String key,String value, CommandBatching batching);
    RedisFuture<String> hget(String key,String field, CommandBatching batching);

    RedisFuture<String> get(String key);
    RedisFuture<String> get(String key, CommandBatching batching);
}
