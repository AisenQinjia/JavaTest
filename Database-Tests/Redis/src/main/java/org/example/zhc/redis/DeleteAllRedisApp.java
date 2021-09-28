//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.example.zhc.redis;

import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DeleteAllRedisApp {
    public DeleteAllRedisApp() {
    }

    public static void main(String[] args) {
        Jedis jedis = null;

        try {
            jedis = new Jedis(args[0], Integer.parseInt(args[1]));
            if (!"123".equals(args[2])) {
                jedis.auth(args[2]);
            }

            jedis.select(Integer.parseInt(args[3]));
            Set<String> removeKeys = new HashSet();
            removeKeys.addAll(jedis.keys("sg:{1000004:100:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:100:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:100:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:100:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:101:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:101:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:101:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:101:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:102:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:102:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:102:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:102:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:11:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:11:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:11:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:11:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:12:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:12:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:12:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:12:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:13:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:13:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:13:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:13:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:14:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:14:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:14:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:14:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:16:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:16:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:16:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:16:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:2:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:2:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:2:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:2:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:23:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:23:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:23:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:23:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:30:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:30:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:30:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:30:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:26:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:26:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:26:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:26:700175:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:IncrRegionId:6:wuhui-release:*:}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:wuhui-release:6:wuhui-release:*:}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:wuhui-release:7:*}"));
            System.out.println(">>>>>>>>>>>>>>开始删除缓存>>>>>>>>>>>>>>>>>");
            Iterator var3 = removeKeys.iterator();

            while(var3.hasNext()) {
                String keyStr = (String)var3.next();
                System.out.println(keyStr);
                jedis.del(keyStr);
            }

            System.out.println(">>>>>>>>>>>>>>缓存已删除>>>>>>>>>>>>>>>>>");
        } catch (Exception var8) {
            System.out.println(var8);
        } finally {
            jedis.close();
        }

    }
}
