package org.example.zhc.util.zhc.validation;

import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeletRedisMysqlApp {
    public static final int REDIS_PORT = 30379;
    public static final int MYSQL_PORT = 30006;
    public static final String MYSQL_USERNAME = "root";
    public static final String MYSQL_PASSWORD = "password";



    /**
     * @param args: mysql_ip mysql_database redis_ip role_id regionId channelId
     */
    public static void main(String[] args) throws SQLException {
        if(args.length < 6){
            System.out.println("参数不足");
            return;
        }

        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);
        System.out.println(args[3]);
        System.out.println(args[4]);
        System.out.println(args[5]);

        String mysqlIp       = args[0];
        String mysqlDataBase = args[1];

        String redisIp = args[2];
        String roleId  = args[3];
        String regionId  = args[4];
        String channelId  = args[5];

        String mysqlTable = "storage_1000004_" + regionId ;
        String ownerId = String.format("%s:%s:%s:",regionId,channelId,roleId);

        //mysql del
        String url = "jdbc:mysql://"+ mysqlIp +":"+ MYSQL_PORT +"/" + mysqlDataBase;
        System.out.println("mysql url is " + url);
        Connection connection = DriverManager.getConnection(url, MYSQL_USERNAME, MYSQL_PASSWORD);
        String sql1 = String.format("UPDATE `%s` SET is_del=1 WHERE logic_type=6 AND owner_id='%s' AND `key`!='srcInfoList';",
                mysqlTable,ownerId);
        String sql2 = String.format("UPDATE `storage_1000004_IncrRegionId` SET is_del=1 WHERE logic_type=6 AND owner_id='%s' AND `key`='incKey';", ownerId);
        System.out.println("sql1: "+sql1);
        System.out.println("sql2: "+sql1);
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
        System.out.println(">>>>>>>>>>>>>>开始删除数据>>>>>>>>>>>>>>>>>");
        preparedStatement1.execute();
        preparedStatement2.execute();
        System.out.println(">>>>>>>>>>>>>>数据删除成功>>>>>>>>>>>>>>>>>");

        //redis del
        Jedis jedis = new Jedis(redisIp, REDIS_PORT);
        jedis.select(0);
        String incrKey = String.format("sg:{1000004:IncrRegionId:6:%s}", ownerId);
        String dataKey = String.format("sg:{1000004:%s:6:%s}", regionId,ownerId);
        System.out.println("redis key: "+ incrKey);
        System.out.println("redis key: "+ dataKey);
        System.out.println(">>>>>>>>>>>>>>开始删除缓存>>>>>>>>>>>>>>>>>");

        jedis.del(incrKey);
        jedis.del(dataKey);
        System.out.println(">>>>>>>>>>>>>>缓存已删除>>>>>>>>>>>>>>>>>");

    }
}
