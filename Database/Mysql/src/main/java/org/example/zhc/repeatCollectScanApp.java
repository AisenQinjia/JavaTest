package org.example.zhc;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.zhc.util.CsvUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@Slf4j
public class repeatCollectScanApp {
    public static final String readPath = "scan.csv";
    public static final String writePath = "bug.csv";
    public static final String preWritePath = "preEqualsCurrent.csv";
    public static final String allWritePath = "allPossible.csv";

    public static final String DB_IOS = "tpf_storage_wuhui_release_common_ios";
    public static final String TABLE_IOS = "storage_1000004_mail_wuhui_release_common_ios";
    public static final String REGION_IOS = "wuhui-release-ios";
    public static final String DB_ANDROID = "tpf_storage_wuhui_release_common";
    public static final String TABLE_ANDROID = "storage_1000004_mail_wuhui_release_common";
    public static final String REGION_ANDROID = "wuhui-release-game";

    public static final String STORAGE_PRIMARY_KEY = "primary_key";
    public static final String STORAGE_VAL = "value";
    public static final String STORAGE_KEY = "key";
    static Connection iOSCon;
    static Connection androidCon;

//    public static final String TABLE_IOS = "storage_1000004_mail_tpfdemo_test_zhiqian";
//    public static final String REGION_IOS = "tpfdemo-test-zhiqian";
//    public static final String TABLE_ANDROID = "storage_1000004_mail_tpfdemo_test_zhiqian";
//    public static final String REGION_ANDROID = "tpfdemo-test-zhiqian";
    public static void main(String[] args) throws IOException {
        //读表
        //建立连接
        iOSCon = buildCon("rm-bp1vq8v2042z82450.mysql.rds.aliyuncs.com",3306,DB_IOS,"whwebquery","E2B52D612085a");
        androidCon = buildCon("rm-bp1vq8v2042z82450.mysql.rds.aliyuncs.com",3306,DB_ANDROID,"whwebquery","E2B52D612085a");
//        androidCon = buildCon("10.100.1.168",30006,"tpf_storage_test_zhiqian","root","password");
//        iOSCon = buildCon("10.100.1.168",30006,"tpf_storage_test_zhiqian111","root","password");
        //读表
        CSVParser csvParser = CsvUtil.readCsvFileWithHeader(readPath);
        List<CSVRecord> errRecords = new ArrayList<>();
        List<CSVRecord> preEqualsCurrentRecords = new ArrayList<>();
        List<CSVRecord> allPossiableRecords = new ArrayList<>();
        try {
            for (CSVRecord record : csvParser){
                String userId = getUserId(record);
                String channelId = getChannel(record);
                log.info("begin: user:{}", userId);
                if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(channelId)){
                    log.error("empty userId || channelId detected!");
                    return;
                }
                boolean ios = isIos(channelId);
                Connection con = ios? iOSCon:androidCon;
                String tableName = ios? TABLE_IOS : TABLE_ANDROID;
                String region = ios? REGION_IOS: REGION_ANDROID;
                //获取offset->umi 记录
                Map<Long,String> mailIdMap = new HashMap<>();
                Map<String, UMI> offsetUmi = getOffsetUmi(tableName, region, channelId, userId, con);
                for(Map.Entry<String,UMI> entry :offsetUmi.entrySet()){
                    String offset = entry.getKey();
                    UMI umi = entry.getValue();
                    String preOffset = mailIdMap.get(umi.getId());
                    if (preOffset==null){
                        mailIdMap.put(umi.getId(),offset);
                        continue;
                    }
                    //重复数据
                    Long repeatedId = umi.getId();
                    //获取记录的offset
                    int refOffsetInt = Integer.parseInt(getRefOffset(con,tableName,region,channelId,userId,repeatedId));
                    int preOffsetInt = Integer.parseInt(preOffset);
                    int currentOffsetInt = Integer.parseInt(offset);
                    log.warn("repeat! offset {} {}, mailId:{} ref:{}",preOffsetInt,currentOffsetInt,repeatedId,refOffsetInt);
                    if(refOffsetInt == Math.min(preOffsetInt,currentOffsetInt)){
                        log.error("bug detect! {}",userId);
                        errRecords.add(record);
                    }
                    if(refOffsetInt == preOffsetInt){
                        log.info("ref == pre");
                        preEqualsCurrentRecords.add(record);
                    }
                    allPossiableRecords.add(record);
                }
            }
        }catch (Exception e){
            log.error("catch error",e);
        }finally {
            //保存数据
            CsvUtil.writesToCsvFileWithHeader(writePath, CSVFormat.DEFAULT.withHeader(csvParser.getHeaderMap().keySet().toArray(new String[0]))
                    ,errRecords);
            CsvUtil.writesToCsvFileWithHeader(preWritePath, CSVFormat.DEFAULT.withHeader(csvParser.getHeaderMap().keySet().toArray(new String[0]))
                    ,preEqualsCurrentRecords);
            CsvUtil.writesToCsvFileWithHeader(allWritePath, CSVFormat.DEFAULT.withHeader(csvParser.getHeaderMap().keySet().toArray(new String[0]))
                    ,allPossiableRecords);
        }

    }

    private static Map<String,UMI> getOffsetUmi(String tableName, String region, String channel, String userId,Connection connection) throws SQLException {
        //获取offset
        Map<String,UMI> umiMap = new HashMap<>();
        String incrSql = getIncrValue(tableName, region, channel, userId);
        ResultSet incrSet = sqlExecuteQuery(incrSql,connection);
        if(!incrSet.next()){
            log.error("用户{}没有offset!",userId);
            return umiMap;
        }
        int maxOffset = Integer.parseInt(new String(incrSet.getBytes(STORAGE_VAL)));
        if(maxOffset<=0 || maxOffset>300){
            log.error("user {} offset too big ", userId);
            return umiMap;
        }
        String offsetSql = offsetSql(tableName,region,channel,userId,maxOffset);
        ResultSet resultSet = sqlExecuteQuery(offsetSql,connection);
        while (resultSet.next()){
            String key = resultSet.getString(STORAGE_KEY);
            UMI umi = JSON.parseObject(resultSet.getBytes(STORAGE_VAL), UMI.class);
            umiMap.put(key,umi);
        }
        return umiMap;
    }

    private static String getRefOffset(Connection connection, String tableName,String region,String channel,String userId,Long mailId) throws SQLException {
        String refSql = refSql(tableName,region,channel,userId,mailId);
        ResultSet resultSet = sqlExecuteQuery(refSql,connection);
        if(resultSet.next()){
            return new String(resultSet.getBytes(STORAGE_VAL));
        }else {
            throw new RuntimeException("getRefOffset error " +  refSql);
        }
    }

    private static String offsetSql(String tableName, String region, String channel, String userId,int offset){
        return "SELECT * FROM "+table(tableName)+" WHERE " + STORAGE_PRIMARY_KEY + " IN (" + inSql(region,channel,userId,offset)
                + ");";
    }

    private static String inSql(String region,String channel,String userId,int offset){
        int i = 1;
        String[] strs = new String[offset];
        while (i<=offset){
            strs[i-1] = inSql(region,channel,userId,String.valueOf(i));
            i++;
        }
        return String.join(",",strs);
    }

    private static String inSql(String region,String channel,String userId,String key){
        return  "\"6:1000004_"+region+"_" + region + ":" + channel + ":" + userId + ":"
                +":"+key+"\"";
    }
    private static String refSql(String tableName,String region,String channel,String userId,Long mailId){
        return "SELECT * FROM "+table(tableName)+" WHERE " + STORAGE_PRIMARY_KEY + "=\"" +
                "6:1000004_"+region+"_" + region + ":" + channel + ":" + userId + ":"
                +":userMailId:"+ mailId +":\";";
    }
    private static String getIncrValue(String tableName,String region,String channel,String userId){
        return "SELECT * FROM "+table(tableName)+" WHERE " + STORAGE_PRIMARY_KEY + "=\"" +
                "6:1000004_IncrRegionId_" + region + ":" + channel + ":" + userId + ":"
                +":incKey\";";
    }


    private static String getUserId(CSVRecord record){
        return record.get(0);
    }

    private static String getChannel(CSVRecord record){
        return record.get(3);
    }

    public static Connection buildCon(String ip,int port, String db,String userName,String password){
        String url = "jdbc:mysql://"+ ip +":"+ port +"/" + db;
        Connection mysqlCon = null;
        try {
            mysqlCon = DriverManager.getConnection(url, userName, password);
            if(mysqlCon==null){
                throw new RuntimeException("msql connection is null");
            }
        }catch (Exception e){
            log.error("build connection error!",e);
            System.exit(1);
        }
        return mysqlCon;
    }

    public static boolean isIos(String channel){
        return Objects.equals("3",channel.trim());
    }

    private static ResultSet sqlExecuteQuery(String sql,Connection mysqlCon) throws SQLException {
        Statement createState =  mysqlCon.createStatement();
        return createState.executeQuery(sql);
    }

    private static String table(String tableName){
        return "`" + tableName + "`";
    }
}
