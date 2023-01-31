package org.example.zhc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class RandomTest {
    @Test
    public void randomT1(){
        Random random = new Random();
        long t1 = System.currentTimeMillis();
        int count = 1000000;
        int modeSize = 100;
        for (int i =0;i<count;i++){
            random.nextInt(modeSize);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("random " + (t2-t1));
        int i1;
        Map<String,Integer> modeMap = new ConcurrentHashMap<>();
        long t3 = System.currentTimeMillis();
        for (int i =0;i<count;i++){
            Integer currentCount = modeMap.getOrDefault("m", 0);
            modeMap.put("m",currentCount+1);
            currentCount = currentCount < 0 ? 0 : currentCount;
            i1 = currentCount % modeSize;
        }
        long t4 = System.currentTimeMillis();
        System.out.println("mode " + (t4-t3));
        long t5 = System.currentTimeMillis();
        for (int i =0;i<count;i++){
            ThreadLocalRandom.current().nextInt(modeSize);
        }
        long t6 = System.currentTimeMillis();
        System.out.println("threadLocalRandom " + (t6-t5));

    }

    @Test
    public void randomT2(){
        Random random = new Random();
        int count = 10;
        int modeSize = 100;
        for (int i =0;i<count;i++){
            System.out.println(random.nextInt());
        }
    }

}
