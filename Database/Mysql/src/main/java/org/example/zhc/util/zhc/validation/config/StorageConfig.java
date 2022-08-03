package org.example.zhc.util.zhc.validation.config;

import lombok.Getter;

import java.util.List;

@Getter
public class StorageConfig {
    String mysqlIp;
    Integer mysqlPort;
    String mysqlUserName;
    String mysqlPassword;
    String mysqlDataBase;
    String redisIp;
    Integer redisPort;
    Integer redisDatabase;
    String redisAuth;

    List<ServiceConfig> svcConfigs;

    public void checkParam(){
        //参数检查
        svcConfigs.forEach(ServiceConfig::checkParam);
    }
}
