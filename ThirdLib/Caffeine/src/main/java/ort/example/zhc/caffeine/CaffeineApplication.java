package ort.example.zhc.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@SpringBootApplication
public class CaffeineApplication implements CommandLineRunner {

    static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) {
        SpringApplication.run(CaffeineApplication.class);

        log.debug("============================================");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Cache<String, String > caffeine = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();
        caffeine.put("A", "a");
        String aValue =  caffeine.get("a", new Function<String, String>() {
            @Override
            public String apply(String s) {
                log.info("Apppy A");
                return "aa";
            }
        });
        log.info("getA: {}", aValue);
    }
}
