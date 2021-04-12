package org.example.zhc;

import org.junit.Test;

import java.sql.*;

public class MysqlApp {
    public static void main(String[] args){

    }
    @Test
    public void mysql(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zhc","root","");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from emp");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

