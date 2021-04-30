package org.example.zhc;

import org.junit.Test;

import java.sql.*;

public class MysqlApp {
    public static void main(String[] args){

    }
    public static String homeConnect = "jdbc:mysql://localhost:3306/zhc";
    public static String homeConnect_root = "root";
    public static String homeConnect_password = "";

    public static String companyConnect = "jdbc:mysql://10.100.1.41:30006/tpf_storage";
    public static String companyConnect_root = "root";
    public static String companyConnect_password = "password";

    public static String connect = companyConnect;
    public static String root = companyConnect_root;
    public static String password = companyConnect_password;


    @Test
    public void mysql(){
        try {
            Connection con = DriverManager.getConnection(connect,root,password);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from storage_50_0");
            while(rs.next()){
                System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

