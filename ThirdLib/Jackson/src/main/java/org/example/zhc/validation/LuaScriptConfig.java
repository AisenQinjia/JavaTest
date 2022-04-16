package org.example.zhc.validation;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
/**
 * LuaScriptConfig.java
 *
 * @author rockhu
 * @date 11:54 2020/11/25
 **/

/**
 * @author yaren
 */
@ToString
@Getter
@Component
@Slf4j
public class LuaScriptConfig {

    private String asynIncrScript;

    private String lockScript;

    private String unlockScript;

    private String dirtyKeyScript;

    private String queryAllKeyScript;

    private String queryKeysScript;

    private String updateKeysScript;

    private String updateKeysExpireScript;

    private String removeKeysScript;

    private String hotAllKeyScript;

    private String incrRedisScript;

    private String hotKeysScript;

    private String queryDelKeyScript;

    private String updateRanksScript;

    private String getRankInfoScript;

    private String getAroundRankInfoScript;

    private String getOwnerRankInfoScript;

    private String clearRankScript;

    private String delRankScript;

    private String statefulGetLink;
    private String statefulComputeLinkIfAbsent;
    private String statefulGetServicePod;
    private String statefulSetLink;
    private String statefulRemoveLink;
    private String statefulSetState;
    private String statefulGetLinkService;


    @PostConstruct
    public void init(){
        try {
            asynIncrScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/asynIncr.lua").getInputStream(), StandardCharsets.UTF_8);
            lockScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/lock.lua").getInputStream(), StandardCharsets.UTF_8);
            unlockScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/unlock.lua").getInputStream(), StandardCharsets.UTF_8);
            dirtyKeyScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/getDirtyKey.lua").getInputStream(), StandardCharsets.UTF_8);
            queryAllKeyScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/queryAllKey.lua").getInputStream(), StandardCharsets.UTF_8);
            queryKeysScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/queryKeys.lua").getInputStream(), StandardCharsets.UTF_8);
            updateKeysScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/updateKeys.lua").getInputStream(), StandardCharsets.UTF_8);
            updateKeysExpireScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/updateKeysExpire.lua").getInputStream(), StandardCharsets.UTF_8);
            removeKeysScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/removeKeys.lua").getInputStream(), StandardCharsets.UTF_8);
            hotAllKeyScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/hotAllKey.lua").getInputStream(), StandardCharsets.UTF_8);
            incrRedisScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/incrRedis.lua").getInputStream(), StandardCharsets.UTF_8);
            hotKeysScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/hotKeys.lua").getInputStream(), StandardCharsets.UTF_8);
            queryDelKeyScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/queryDelKey.lua").getInputStream(), StandardCharsets.UTF_8);
            updateRanksScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/updateRanks.lua").getInputStream(), StandardCharsets.UTF_8);
            getRankInfoScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/getRankInfo.lua").getInputStream(), StandardCharsets.UTF_8);
            getAroundRankInfoScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/getAroundRankInfo.lua").getInputStream(), StandardCharsets.UTF_8);
            getOwnerRankInfoScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/getOwnerRankInfo.lua").getInputStream(), StandardCharsets.UTF_8);
            clearRankScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/clearRank.lua").getInputStream(), StandardCharsets.UTF_8);
            delRankScript = StreamUtils.copyToString(new ClassPathResource("redis_lua/delRank.lua").getInputStream(), StandardCharsets.UTF_8);

            //stateful
            statefulGetLink = StreamUtils.copyToString(new ClassPathResource("redis_lua/statefulGetLink.lua").getInputStream(), StandardCharsets.UTF_8);
            statefulComputeLinkIfAbsent = StreamUtils.copyToString(new ClassPathResource("redis_lua/statefulComputeLinkIfAbsent.lua").getInputStream(), StandardCharsets.UTF_8);
            statefulGetServicePod = StreamUtils.copyToString(new ClassPathResource("redis_lua/statefulGetServicePod.lua").getInputStream(), StandardCharsets.UTF_8);
            statefulSetLink = StreamUtils.copyToString(new ClassPathResource("redis_lua/statefulSetLink.lua").getInputStream(), StandardCharsets.UTF_8);
            statefulRemoveLink = StreamUtils.copyToString(new ClassPathResource("redis_lua/statefulRemoveLink.lua").getInputStream(), StandardCharsets.UTF_8);
            statefulSetState = StreamUtils.copyToString(new ClassPathResource("redis_lua/statefulSetState.lua").getInputStream(), StandardCharsets.UTF_8);
            statefulGetLinkService = StreamUtils.copyToString(new ClassPathResource("redis_lua/statefulGetLinkService.lua").getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e){
            log.error("init load redis lua exception: ", e);
        }
    }
}
