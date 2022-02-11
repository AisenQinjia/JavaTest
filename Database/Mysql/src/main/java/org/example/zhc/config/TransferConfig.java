package org.example.zhc.config;

import lombok.Getter;
import org.example.zhc.StorageTransferApp;

import java.util.List;

@Getter
public class TransferConfig {
    String mysqlIp;
    Integer mysqlPort;
    String mysqlUserName;
    String mysqlPassword;
    String mysqlDataBase;
    String redisIp;
    Integer redisPort;
    Integer redisDatabase;
    String redisAuth;
    String appId;
    String serverName;
    String regionId;
    /**
     * 此服务使用的旧表结构
     */
    List<OldServerStorageConfig> oldStorageConfigs;

    @Getter
    public static class OldServerStorageConfig{
        /**
         * 使用过的appId
         */
        String appId;
        /**
         * 使用过的regionId
         */
        String regionId;
        /**
         * 使用过的logicType
         * 可为空
         */
        Integer logicType;
        /**
         * 使用过的ownerId
         * 可为空
         */
        String ownerId;

        public void checkParamValid(){
            if(checkIsNullOrEmpty(appId,regionId)){
                System.out.print("OldServerStorageConfig config not valid!,param not enough!");
                System.exit(1);
            }
        }
        public String getTableName(){
            return StorageTransferApp.STORAGE_PREFIX + appId + "_" + regionId;
        }

        public String toInfo(){
            return String.format("appId:%s,regionId:%s,logicType:%d,ownerId:%s",appId,regionId,logicType,ownerId);
        }
    }

    public void checkParam(){
        if(checkIsNullOrEmpty(appId,serverName,regionId) || oldStorageConfigs == null ){
            System.out.print("config param not enough appId,serverName,regionId,oldStorageConfigs has empty field");
            System.exit(1);
        }
        oldStorageConfigs.forEach(OldServerStorageConfig::checkParamValid);
    }

    public static boolean checkIsNullOrEmpty(String... strs){
        for(String str:strs){
            if(str==null || str.isEmpty()){
                return true;
            }
        }
        return false;
    }

    public String getOneTableName(){
        OldServerStorageConfig oldServerStorageConfig = oldStorageConfigs.get(0);
        return StorageTransferApp.STORAGE_PREFIX + oldServerStorageConfig.appId + "_" + oldServerStorageConfig.regionId;
    }
}
