package DAO_PoolConnection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-15 18:22
 * @description:
 **/
public class DruidTest {

    @Test
    public void getConnection() throws Exception {
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("JDBC\\druid.properties");
        pros.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
