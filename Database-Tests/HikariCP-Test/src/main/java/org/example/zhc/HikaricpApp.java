package org.example.zhc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author aisen
 */
public class HikaricpApp {
    public static String companyConnect = "jdbc:mysql://10.100.1.41:30006/tpf_storage";

    public static void main(String[] args) throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(companyConnect);
        config.setUsername("root");
        config.setPassword("password");
        HikariDataSource ds = new HikariDataSource(config);
        Connection con =  ds.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs=stmt.executeQuery("select * from storage_1000004_1");
        while(rs.next()){
            System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
        }
    }
}
