package jdbc_Test;

import org.junit.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-29 17:14
 * @description:
 **/
public class ConnectionTest {
    //方式一
    @Test
    public void testConnection1() throws SQLException {
        Driver driver = new com.mysql.cj.jdbc.Driver();
        
        String url = "jdbc:mysql://localhost:3306/test";
        //用户名与密码封装在Properties里
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "omgd45945.+");

        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //方式二：对方式一的迭代
    @Test
    public void testConnection2() throws Exception{
        //1，获取Driver实现类对象，的反射
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2，提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //3，提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "omgd45945.+");

        //4，获取连接
        Connection conn = driver.connect(url,info);
        System.out.println(conn);
    }

    //方式三：使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception{
        //1，获取Driver实现类的对象
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2，提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "omgd45945.+";

        //3，注册驱动
        DriverManager.registerDriver(driver);

        //4，获取连接
        Connection conn = DriverManager.getConnection(url,username,password);
        System.out.println(conn);
    }

    //方式四：方式三的优化  可以只是加载驱动，不用显示的注册驱动了
    @Test
    public void testConnection4() throws Exception{
        //1，提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "omgd45945.+";

        //2，加载Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
  /*    Driver driver = (Driver) clazz.newInstance();

        //3，注册驱动
        DriverManager.registerDriver(driver);
*/
        //4，获取连接
        Connection conn = DriverManager.getConnection(url,username,password);
        System.out.println(conn);
    }

    //方式五：
    @Test
    public void testConnection5() throws Exception{
        //读取配置文件中的4个基本信息
        Properties pro = new Properties();
        FileInputStream files = new FileInputStream("JDBC/jdbc");
        pro.load(files);

        String url = pro.getProperty("url3");
        String username = pro.getProperty("username");
        String password = pro.getProperty("password");
        String driverClass = pro.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        Connection coll = DriverManager.getConnection(url,username,password);
        System.out.println(coll);
    }
}
