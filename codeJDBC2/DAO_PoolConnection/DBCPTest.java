package DAO_PoolConnection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-15 16:58
 * @description:
 *   DBCP的数据库连接技术
 **/
public class DBCPTest {

    //测试DBCP的数据库连接池技术
    //方式一：不推荐
    @Test
    public void testGetConnection() throws SQLException {
        //创建了DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();

        //设置基本信息
        source.setDriverClassName("com.mysql.cj.jdbc.properties.Driver");
        source.setUrl("jdbc.properties:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("omgd45945.+");

        //设置数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);
        //...

        Connection conn = source.getConnection();
        System.out.println(conn);
    }

    //方式二：推荐: 使用配置文件
    @Test
    public void testGetConnection2() throws Exception {
        Properties pros = new Properties();
        //方式1：
       // InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("JDBC\\dbcp.properties.properties");
        //方式2：
        FileInputStream is = new FileInputStream(new File("JDBC\\dbcp.properties.properties"));
        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();

        System.out.println(conn);

        String sql = "SELECT * " +
                "FROM user";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getResultSet();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            String address = resultSet.getString("address");
            String phone = resultSet.getString("phone");
            System.out.println(id + " " + name + " " + password + " " + address + " " + phone);

        }
    }

}
