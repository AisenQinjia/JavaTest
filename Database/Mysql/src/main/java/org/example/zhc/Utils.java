package org.example.zhc;

import org.example.zhc.config.TransferConfig;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.PrintStream;
import java.sql.*;
import java.util.Set;


public class Utils {
    static PrintStream log = System.out;
    public static final String REDIS_EXIST_KEY_TMPL = "sg:{%s:%s:%d:%s}exist";
    public static final String REDIS_KEY_TMPL = "sg:{%s:%s:%d:%s}";
    public static final String STORAGE_LOGIC_TYPE = "logic_type";
    public static final String STORAGE_PREFIX = "storage_";
    public static final String STORAGE_OWNER_ID = "owner_id";
    public static final String STORAGE_IS_DEL = "is_del";
    //返回当前目录下所有带有前缀的文件
    public static File[] getFilesByPrefix(String path,String prefix){
        File f = new File(path);
        return f.listFiles((dir, name) -> prefix == null || name.startsWith(prefix));
    }

    public static boolean checkIsNullOrEmpty(String... strs){
        for(String str:strs){
            if(str==null || str.isEmpty()){
                return true;
            }
        }
        return false;
    }
    public static String appIdServerType(String appId,String serverName){
        return appId + "_" + serverName.split(":")[0].replace("-","_");
    }
    public static String formattedRegionId(String regionId){
        return regionId.replace("-","_");
    }

    public static Connection buildMysqlCon(String ip, Integer port,String database, String username,String password){
        String url = "jdbc:mysql://"+ ip +":"+ port +"/" + database;
        Connection mysqlCon = null;
        try {
            mysqlCon = DriverManager.getConnection(url, username, password);
            if(mysqlCon==null){
                throw new RuntimeException("msql connection is null");
            }

        }catch (Exception e){
            log.print(String.format("mysql建立连接失败!addr:%s, port:%d,user:%s,passwordLength:%d,error:%s",
                    ip,port,username,password.length(),e.getMessage()));
            e.printStackTrace();
            System.exit(1);
        }
        return mysqlCon;
    }

    public static Jedis buildRedisCon(String ip, Integer port,String auth,Integer database){
        Jedis jedis= null;
        try {
            jedis = new Jedis(ip, port);
            if(!TransferConfig.checkIsNullOrEmpty(auth)){
                jedis.auth(auth);
            }
            jedis.select(database);
        }catch (Exception e){
            log.println(String.format("redis建立连接失败!addr:%s, port:%d,authLength:%d,error:%s",
                    ip,port,auth.length(),e.getMessage()));
            e.printStackTrace();
            System.exit(1);
        }
        return jedis;
    }

    public static String tableName(String appId,String serverName,String regionId){
        return STORAGE_PREFIX + appIdServerType(appId,serverName) + "_" + formattedRegionId(regionId);
    }

    public static void existWithMessage(String message){
        log.println(message);
        System.exit(1);
    }

    //根据接口参数形成mysql select
    public static String select(String tableName, String appId, String regionId, Integer logicType, String ownerId){
        String select = "SELECT * FROM " + mysqlTable(tableName);
        select = select + where(appId,regionId,logicType,ownerId) + ";";
        log.println("select: " +select);
        return select;
    }

    private static String where(String appId, String regionId, Integer logicType, String ownerId){
        String where = " WHERE " + STORAGE_OWNER_ID + " LIKE ";
        //ownerId
        String mysqlOwnerId = "'";
        if(!checkIsNullOrEmpty(appId)){
            mysqlOwnerId = mysqlOwnerId + appId;
        }else{
            mysqlOwnerId = mysqlOwnerId + "%";
        }
        mysqlOwnerId = mysqlOwnerId  + "_";
        if(!checkIsNullOrEmpty(regionId)){
            mysqlOwnerId = mysqlOwnerId + regionId;
        }else{
            mysqlOwnerId = mysqlOwnerId + "%";
        }
        mysqlOwnerId = mysqlOwnerId  + "_";
        if(!checkIsNullOrEmpty(ownerId)){
            mysqlOwnerId = mysqlOwnerId + ownerId;
        }else{
            mysqlOwnerId = mysqlOwnerId + "%";
        }
        mysqlOwnerId = mysqlOwnerId  + "'";
        where = where + mysqlOwnerId;
        //logicType
        if(logicType != null){
            where = where + " AND " + STORAGE_LOGIC_TYPE + "=" + logicType;
        }
        return where;
    }
    public static void deleteMysql(Connection mysqlCon, String tableName, String appId, String regionId, Integer logicType, String ownerId) throws SQLException {
        String del ="UPDATE "+mysqlTable(tableName)+" SET "+STORAGE_IS_DEL+"=1";
        del = del + where(appId,regionId,logicType,ownerId);
        log.println("del: " +del);
        sqlExecute(mysqlCon,del);
    }

    public static void sqlExecute(Connection mysqlCon, String sql) throws SQLException {
        Statement createState =  mysqlCon.createStatement();
        createState.execute(sql);
    }

    public static ResultSet sqlExecuteQuery(Connection mysqlCon, String sql) throws SQLException {
        Statement createState =  mysqlCon.createStatement();
        return createState.executeQuery(sql);
    }

    public static String getRedisKey(String appId,String regionId,int logicType,String ownerId){
        return String.format(REDIS_KEY_TMPL,appId,regionId,logicType,ownerId);
    }

    public static void redisDelete(Jedis jedis, Set<String> existKeys){
        existKeys.forEach(key->{
            //删除数据
            jedis.del(key);
            //删除存在key
            jedis.del(key + "exist");
        });
    }

    private static String mysqlTable(String tableName){
        return "`" + tableName + "`";
    }
}
