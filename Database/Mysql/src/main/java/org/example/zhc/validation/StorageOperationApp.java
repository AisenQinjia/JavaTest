package org.example.zhc.validation;

import com.google.gson.Gson;
import org.example.zhc.validation.config.ServiceConfig;
import org.example.zhc.validation.config.StorageConfig;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库脚本
 */
public class StorageOperationApp {
    //配置文件前缀
    public static final String  FILE_PREFIX = "StorageConfig_";
    static Gson gson = new Gson();
    static PrintStream log = System.out;

    public static void main(String[] args){
        try {
            File[] files = Utils.getFilesByPrefix("./",FILE_PREFIX);
            if (files.length == 0){
                Utils.existWithMessage("未发现任何配置文件");
            }
            for (File f : files){
                log.println("操作配置表... " + f.getName());
                StorageConfig config =  gson.fromJson(new FileReader(f), StorageConfig.class);
                processConfig(config);
                log.println("操作配置表: " + f.getName()  + "完成!");
            }
            log.println("全部操作成功!");
        }catch (Exception e){
            log.println("UNKNOWN EXCEPTION: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void processConfig(StorageConfig config) throws SQLException {
        config.checkParam();
        //建立连接
        Connection mysqlCon = Utils.buildMysqlCon(config.getMysqlIp(),config.getMysqlPort(),config.getMysqlDataBase(),config.getMysqlUserName(),config.getMysqlPassword());
        Jedis jedis = Utils.buildRedisCon(config.getRedisIp(),config.getRedisPort(),config.getRedisAuth(),config.getRedisDatabase());
        //每个服务处理storage操作
        for (ServiceConfig serviceConfig : config.getSvcConfigs()) {
            serviceConfig.opProcess(mysqlCon, jedis);
        }

    }
}
