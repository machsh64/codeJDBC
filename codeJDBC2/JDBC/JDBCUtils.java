package JDBC;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-15 16:43
 * @description:
 **/
@SuppressWarnings("unused")
public class JDBCUtils {

    //使用c3p0的数据库连接池技术
    //数据库连接池只需提供一个即可
    private static final ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0pool");
    public static Connection getConnection1() throws SQLException {
        Connection conn = cpds.getConnection();
        System.out.println("数据库连接成功...\n");
        return conn;
    }

    //使用DBCP数据库连接池技术
    //数据库连接池只需提供一个即可
    private static DataSource source;
    static {
        Properties pros = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(new File("JDBC/dbcp.properties"));
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if (is != null)
                    is.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static Connection getConnection2() throws Exception {

        Connection conn = source.getConnection();
        System.out.println("数据库连接成功...");
        return conn;
    }

    //使用Druid数据库连接池技术
    private static DataSource source1 = null;
    static {
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("JDBC\\druid.properties");
        try {
            pros.load(is);
            source1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception {

        Connection conn = source1.getConnection();
        System.out.println("数据库连接成功...\n");
        return conn;
    }

    //释放资源
    //！注  所有操作方法中仅释放了方法内涉及到的资源，所有调用操作最后应该使用该方法进行统一资源释放
    public static void release(Connection conn, Statement statement) {
        try {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            System.out.println("\n资源释放完毕...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //使用dbutils.jar 中提供的DBUtils工具类，实现资源的关闭
    public static void release1(Connection conn, Statement statement, ResultSet resultSet) {
        /*try {
            DbUtils.close(conn);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            DbUtils.close(statement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            DbUtils.close(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/

        DbUtils.closeQuietly(conn, statement, resultSet);
    }
}
