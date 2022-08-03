package org.example.zhc.util.zhc.validation;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * @author aisen
 */
public class MybatisApp {
    public static void main(String[] args){
        PooledDataSourceFactory factory = new PooledDataSourceFactory();
        Properties pp = new Properties();
        pp.setProperty("driver","com.mysql.cj.jdbc.Driver");
        pp.setProperty("url","jdbc:mysql://10.100.1.41:30006/tpf_storage");
        pp.setProperty("username","root");
        pp.setProperty("password","password");
        factory.setProperties(pp);
        DataSource dataSource = factory.getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        try (SqlSession session = sqlSessionFactory.openSession()) {
//            User user =  session.selectOne("org.example.zhc.BlogMapper.selectBlog", "0:1000004_32:data");
            BlogMapper blogMapper = session.getMapper(BlogMapper.class);
            User user = blogMapper.selectBlog("0:1000004_32:data");
            factory.getDataSource();
        }
    }
}
