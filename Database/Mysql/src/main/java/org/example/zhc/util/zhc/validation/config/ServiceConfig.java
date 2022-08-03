package org.example.zhc.util.zhc.validation.config;

import org.example.zhc.util.zhc.validation.Utils;
import redis.clients.jedis.Jedis;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceConfig {
    String appId;
    String regionId;
    String serverName;
    List<ServiceOperation> operations;
    static PrintStream log = System.out;

    public static class ServiceOperation {
        Op op;
        String appId;
        String regionId;
        Integer logicType;
        String ownerId;
        public static final String REF = "$ref";
        public void checkParam(String apolloAppId,String apolloRegionId){
            if(op == null){
                op = Op.DELETE;
            }
            if(!Utils.checkIsNullOrEmpty(appId) && appId.contains(REF)){
                appId = apolloAppId;
            }
            if(!Utils.checkIsNullOrEmpty(regionId) && regionId.contains(REF)){
                regionId = apolloRegionId;
            }
        }
    }

    public enum Op{
        DELETE
    }

    public void checkParam(){
        if(Utils.checkIsNullOrEmpty(appId,regionId,serverName)){
            System.out.print("ServiceConfig param not enough!");
            System.exit(1);
        }
        for (ServiceOperation operation : operations) {
            operation.checkParam(appId,regionId);
        }
    }

    public void opProcess(Connection mysqlCon, Jedis jedis) throws SQLException {
        // 表名
        String serviceTableName = Utils.tableName(appId,serverName,regionId);
        String appIdServerType = Utils.appIdServerType(appId,serverName);
        String formattedRegionId = Utils.formattedRegionId(regionId);
        ResultSet res = mysqlCon.getMetaData().getTables(null,null,serviceTableName,null);
        if(res.next()){
            log.println("操作服务... " + serverName);
        }else {
            Utils.existWithMessage("未找到该表: " + serviceTableName);
        }
        for(ServiceOperation serviceOperation: operations){
            //删除操作
            if(serviceOperation.op == Op.DELETE){
                //构造redis key
                Set<String> redisKey = new HashSet<>();
                String selectStr = Utils.select(serviceTableName,serviceOperation.appId,serviceOperation.regionId,serviceOperation.logicType,serviceOperation.ownerId);
                ResultSet resultSet = Utils.sqlExecuteQuery(mysqlCon, selectStr);
                int size = 0;
                while (resultSet.next()){
                    size++;
                    int logicType = resultSet.getInt(Utils.STORAGE_LOGIC_TYPE);
                    String owner_id = resultSet.getString(Utils.STORAGE_OWNER_ID);
                    redisKey.add(Utils.getRedisKey(appIdServerType,formattedRegionId,logicType,owner_id));
                }
                log.println("affected rows: " + size);
                log.println("delete redis key size: " + redisKey.size());
                //删除redis key
                Utils.redisDelete(jedis,redisKey);
                //mysql 删除记录
                Utils.deleteMysql(mysqlCon,serviceTableName,serviceOperation.appId,serviceOperation.regionId,serviceOperation.logicType,serviceOperation.ownerId);
            }
        }
        log.println("操作服务:  " + serverName + "完成!");
    }

}
