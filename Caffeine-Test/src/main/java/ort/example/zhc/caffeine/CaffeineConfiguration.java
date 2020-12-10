package ort.example.zhc.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Cacheable
@Configuration
public class CaffeineConfiguration {
    @Bean
    public Caffeine caffeineConfig(){
        return  Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES);
    }
    @Bean
    public CacheManager cacheManager(Caffeine caffeine){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
