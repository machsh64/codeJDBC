package DAO_PoolConnection;

import JDBC.JDBCUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-15 14:37
 * @description:
 **/
public class C3P0Test {
    //方式一：
    @Test
    public void testGetConnection() throws Exception {
        //获取C3P0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.properties.Driver"); //loads the jdbc.properties driver
        cpds.setJdbcUrl("jdbc.properties:mysql://localhost:3306/test");
        cpds.setUser("root");
        cpds.setPassword("omgd45945.+");

        //通过设置相关的参数，对数据库连接池进行管理
        //设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);

        //销毁C3P0数据库连接池 通常情况下不会使用
        //DataSources.destroy(cpds);

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

    //方式二：使用配置文件  使用此种方式即可
    @Test
    public void testGetConnection2() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0pool");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }

    @Test
    public void testConnection() {
        try {
            Connection conn = JDBCUtils.getConnection1();

            String sql = "SELECT * " +
                    "FROM user";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                System.out.println(id + " " + name + " " + password + " " + address + " " + phone);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



/*    @Test
    public void test() {
        String sql = "SELECT * " +
                "FROM user";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getResultSet();
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            String address = resultSet.getString("address");
            String phone = resultSet.getString("phone");
            System.out.println(id + " " + name + " " + password + " " + address + " " + phone);
        }
    }*/
}
