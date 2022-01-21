package org.example.zhc;

import com.google.gson.Gson;
import org.example.zhc.config.TransferConfig;
import redis.clients.jedis.Jedis;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class StorageTransferApp {
    static final String file = "./transferConfig.json";
    public static final String STORAGE_PREFIX = "storage_";
    public static final String STORAGE_OWNER_ID = "owner_id";
    //logicType+":"+ownerId"+":"+key
    public static final String STORAGE_PRIMARY_KEY = "primary_key";
    public static final String STORAGE_LOGIC_TYPE = "logic_type";
    public static final String STORAGE_KEY = "`key`";
    //logicType+":"+ownerId
    public static final String STORAGE_QUERY_ALL_KEY = "query_all_key";

    private static final String REDIS_EXIST_KEY_TMPL = "sg:{%s:%s:%d:%s}exist";

    static PrintStream log = System.out;
    static Gson gson = new Gson();
    static TransferConfig config;
    static Connection mysqlCon;
    static Jedis jedis;
    static String appIdServerType;
    static String formattedRegionId;
    public static void main(String[] args) throws FileNotFoundException, SQLException {
        try {
            config = gson.fromJson(new FileReader(file), TransferConfig.class);
            config.checkParam();
            appIdServerType = config.getAppId() + "_" + config.getServerName().split(":")[0].replace("-","_");
            formattedRegionId = config.getRegionId().replace("-","_");
            log.println(String.format("appIdServerType:%s,formattedRegionId:%s",appIdServerType,formattedRegionId));

            //建立连接
            buildCon();
            //transfer
            transfer();
            log.println("全部转移成功!");
        }catch (Exception e){
            log.println("UNKNOWN EXCEPTION: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void buildCon(){
        String url = "jdbc:mysql://"+ config.getMysqlIp() +":"+ config.getMysqlPort() +"/" + config.getMysqlDataBase();
        try {
            mysqlCon = DriverManager.getConnection(url, config.getMysqlUserName(), config.getMysqlPassword());
            if(mysqlCon==null){
                throw new RuntimeException("msql connection is null");
            }
        }catch (Exception e){
            log.print(String.format("mysql建立连接失败!addr:%s, port:%d,user:%s,password:%s,error:%s",
                    config.getMysqlIp(),config.getMysqlPort(),config.getMysqlUserName(),config.getMysqlPassword(),e.getMessage()));
            e.printStackTrace();
            System.exit(1);
        }
        try {
            jedis = new Jedis(config.getRedisIp(), config.getRedisPort());
            if(!TransferConfig.checkIsNullOrEmpty(config.getRedisAuth())){
                jedis.auth(config.getRedisAuth());
            }
            jedis.select(config.getRedisDatabase());
        }catch (Exception e){
            log.println(String.format("redis建立连接失败!addr:%s, port:%d,auth:%s,error:%s",
                    config.getRedisIp(),config.getRedisPort(),config.getRedisAuth(),e.getMessage()));
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 转移mysql数据
     * @return 返回redisExist表
     */
    public static void transfer() throws SQLException {
        String newTableName = STORAGE_PREFIX + appIdServerType + "_" + formattedRegionId;
        log.println("operate table: "+ newTableName + " ......");
        //建新表
        ResultSet res = mysqlCon.getMetaData().getTables(null,null,newTableName,null);
        if(res.next()){
            log.println("已存在新表");
        }else {
            log.println("建表");
            sqlExecute(sqlCreateTable(newTableName,config.getOneTableName()));
        }

        config.getOldStorageConfigs().forEach(oldServerStorageConfig -> {
            try {
                Set<String> redisKey = new HashSet<>();
                String oldTableName = oldServerStorageConfig.getTableName();
                String tmpTable = oldTableName +"_tpf_tmp";
                log.println("operate old table: "+ tmpTable + " ......");
                sqlExecute(sqlCreateTable(tmpTable,oldTableName));
                try{
                    //转移目标数据到tmp表中
                    sqlExecute(sqlInsertTable(tmpTable,oldServerStorageConfig.getTableName(),oldServerStorageConfig.getLogicType(),oldServerStorageConfig.getOwnerId()));
                    //更新旧表
                    //ownerId
                    sqlExecute(sqlConcatTableFiled(tmpTable,STORAGE_OWNER_ID,
                            getQuotaStr(oldServerStorageConfig.getAppId()),getQuotaStr("_"),getQuotaStr(oldServerStorageConfig.getRegionId()),getQuotaStr("_"),STORAGE_OWNER_ID));
                    //primary key
                    sqlExecute(sqlConcatTableFiled(tmpTable,STORAGE_PRIMARY_KEY,
                            STORAGE_LOGIC_TYPE,getQuotaStr(":"),STORAGE_OWNER_ID,getQuotaStr(":"),STORAGE_KEY));
                    //query all key
                    sqlExecute(sqlConcatTableFiled(tmpTable,STORAGE_QUERY_ALL_KEY,
                            STORAGE_LOGIC_TYPE,getQuotaStr(":"),STORAGE_OWNER_ID));
                    //复制到新表
                    sqlExecute(sqlInsertTable(newTableName,tmpTable,null,null));
                    //获取redis key
                    ResultSet resultSet =  sqlExecuteQuery(sqlSelect(tmpTable,STORAGE_LOGIC_TYPE,STORAGE_OWNER_ID));
                    while (resultSet.next()){
                        int logicType = resultSet.getInt(STORAGE_LOGIC_TYPE);
                        String owner_id = resultSet.getString(STORAGE_OWNER_ID);
                        redisKey.add(getRedisExistKey(appIdServerType,formattedRegionId,logicType,owner_id));
                    }
                }catch (Exception e){
                    log.println(String.format("operate mysql oldTable: appId:%s,regionId:%s catch error:%s",oldServerStorageConfig.getAppId(),oldServerStorageConfig.getRegionId(),e.getMessage()));
                    e.printStackTrace();
                    //删除tmp表
                    sqlExecute(sqlDropTable(tmpTable));
                    System.exit(1);
                }
                //删除tmp表
                sqlExecute(sqlDropTable(tmpTable));
                redisTransfer(redisKey);
            } catch (SQLException throwable) {
                log.println("copy msql error! " + throwable.getLocalizedMessage());
                throwable.printStackTrace();
            }
        });
    }

    public static void redisTransfer(Set<String> existKeys){
        existKeys.forEach(key->jedis.set(key,"1"));
    }

    private static String getRedisExistKey(String appId,String regionId,int logicType,String ownerId){
        return String.format(REDIS_EXIST_KEY_TMPL,appId,regionId,logicType,ownerId);
    }
    private static String getQuotaStr(String str){
        return "'"+str+"'";
    }

    private static void sqlExecute(String sql) throws SQLException {
        Statement createState =  mysqlCon.createStatement();
        createState.execute(sql);
    }

    private static ResultSet sqlExecuteQuery(String sql) throws SQLException {
        Statement createState =  mysqlCon.createStatement();
        return createState.executeQuery(sql);
    }

    private static String sqlCreateTable(String newTable, String oldTable){
        return "CREATE TABLE "+newTable+" LIKE " + oldTable;
    }
    private static String sqlCleanTable(String tableName){
        return "DELETE FROM " + tableName;
    }
    private static String sqlDropTable(String tableName){
        return "DROP TABLE "+tableName;
    }
    private static String sqlInsertTable(String newTable,String oldTable,Integer logicType,String ownerId){
        String base = "INSERT INTO "+newTable+" SELECT * FROM "+ oldTable;
        if(logicType!=null || !TransferConfig.checkIsNullOrEmpty(ownerId)){
            base = base + " WHERE ";
            if(logicType!=null){
                base = base + "logic_type=" + logicType;
            }
            String and = (logicType!=null && !TransferConfig.checkIsNullOrEmpty(ownerId))? " AND " : "";
            if(TransferConfig.checkIsNullOrEmpty(ownerId)){
                base = base + and + "owner_id="+"'"+ownerId+"'";
            }
        }
        return base + ";";
    }
    private  static String sqlConcatTableFiled(String tableName,String fileName,String... concatStrs){
        String base = "update "+tableName+" set "+fileName+"=CONCAT(";
        for(int i =0;i<concatStrs.length;i++){
            if(i>0){
                base  = base + ",";
            }
            base = base + concatStrs[i];
        }
        return base + ")";

    }
    private static String sqlSelect(String tableName,String... fields){
        return  "SELECT " + String.join(",",fields) + " FROM " + "`"+tableName+"`";
    }
    public static String concatOwnerId(String... subOwnerId){
        return String.join("_",subOwnerId);
    }
}
