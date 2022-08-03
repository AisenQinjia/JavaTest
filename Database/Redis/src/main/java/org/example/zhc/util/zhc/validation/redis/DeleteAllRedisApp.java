//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.example.zhc.util.zhc.validation.redis;

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
            Set<String> removeKeys = new HashSet<>();
            removeKeys.addAll(jedis.keys("sg:{1000004:2:700200:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:2:700130:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:2:700150:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:2:700175:*}"));
            //游戏邮件
            removeKeys.addAll(jedis.keys("sg:{1000004:IncrRegionId:3:IncrOwnerId}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:mailRegion:3:mailOwner}"));
            //追踪邮件
            removeKeys.addAll(jedis.keys("sg:{1000004:mailTrackRegion:8:TrackMailOwner}"));
            //uid邮件
            removeKeys.addAll(jedis.keys("sg:{1000004:uidRegion:9:uidRegion}"));
            //源
            removeKeys.addAll(jedis.keys("sg:{1000004:IncrRegionId:5:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:mailRegion:5:*}"));
            //玩家邮箱
            removeKeys.addAll(jedis.keys("sg:{1000004:IncrRegionId:6:wuhui-1222:*}"));
            removeKeys.addAll(jedis.keys("sg:{1000004:wuhui-1222:6:wuhui-1222:*}"));
            //问卷
            removeKeys.addAll(jedis.keys("sg:{1000004:questionnaire_region:4:*}"));

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
