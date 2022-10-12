package jdbc_Test.CRUD;

import JDBC.JDBCUtil;
import jdbc_Test.CRUD.Instances.Customer;
import jdbc_Test.CRUD.Instances.Order;
import org.junit.Test;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-29 19:03
 * @description: 使用PreparedStatement来替换Statement，实现对数据库的增删改查操作
 * <p>
 * 增删改；查
 **/
public class PreparedStatementUpdateTest {
    @Test
    public void testUpdate1() {
        /*String sql = "INSERT INTO tb_user(id,name,age,gender) VALUES(?,?,?,?)";
        update(sql,21);*/

       /* Connection conn = JDBCUtil.getConnection("itcast");
        String sql1= "UPDATE tb_user SET id = ? WHERE age = ?";
        JDBCUtil.update(conn,sql1,"1006",21);*/

       /* String sql = "SELECT id,name,email,birth FROM customers WHERE id = ? ORDER BY id";
        Customer cust = querytest(sql,13);
        System.out.println(cust);*/

     /* Connection conn = JDBCUtil.getConnection("test");
        String sql = "UPDATE `order` SET order_id = ? WHERE order_name = ?";
        JDBCUtil.update(conn,sql,3,"GG");*/
        String sql = "SELECT order_id orderId,order_name orderName,order_date orderDate FROM `order` WHERE order_id = ?";
        /**针对于表的字段名与类的属性名不相同的情况：
         *   1，必须声明sql时，使用类的属性名来命名字段的别名
         *   2，使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName()获取列的别名
         *  说明：如果sql中没有给字段起别名，getColumnLabel()获取的就是列名
         */
        Order order = query(sql, 3);
        System.out.println(order);
    }

    //通用的查询操作
    public static Order query(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;

        try {
            //连接数据库
            conn = JDBCUtil.getConnection("test");
            preStatement = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preStatement.setObject(i + 1, args[i]);
            }
            //获取结果集
            resultSet = preStatement.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = resultSet.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    //读取每个列的列值，通过ResultSet
                    Object columnValue = resultSet.getObject(i + 1);
                    //通过ResultMetaData
                    //获取列的列名 rsmd.getColumnName();    不推荐使用
                    //获取列的别名 rsmd.getColumnLabel();
                    /* String columnName = rsmd.getColumnName(i + 1);*/   //获取到的是表中的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);   //获取到的是sql语句中，所属表中的别名
                    //通过反射，将对象指定名赋值为指定的值
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(conn, preStatement, resultSet);
        }
        return null;
    }

    //查询操作
    public static Customer querytest(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement preStatement = null;
        try {
            conn = JDBCUtil.getConnection("test");
            preStatement = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preStatement.setObject(i + 1, args[i]);
            }
            ResultSet resultSet = preStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                Customer cust = Customer.getInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);
                    //给cust指定的column Name属性，赋值为columnValue，通过反射
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust, columnValue);
                }
                return cust;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(conn, preStatement, null);
        }
        return null;
    }

    //通用的增删改操作
    public static void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement preStatement = null;
        try {
            //获取数据库的连接
            conn = JDBCUtil.getConnection("test");

            //预编译sql语句，返回preStatement的实例
            preStatement = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preStatement.setObject(i + 1, args[i]);
            }

            //执行
            preStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(conn, preStatement, null);
        }
    }

    //向customers表中添加一条记录
    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement prestatement = null;
        try {
            //1，读取配置文件中的4个基本信息
            Properties pro = new Properties();
            pro.load(new FileInputStream("JDBC/jdbc"));

            String driverClass = pro.getProperty("driverClass");
            String url = pro.getProperty("url3");
            String username = pro.getProperty("username");
            String password = pro.getProperty("password");

            //2，加载驱动
            Class.forName(driverClass);

            //3，获取数据库的连接
            conn = DriverManager.getConnection(url, username, password);

            //4，预编译sql语句，返回PreparedStatement的实例
            String sql = "INSERT INTO customers(name,email,birth) VALUES(?,?,?)";  // ? : 占位符
            prestatement = conn.prepareStatement(sql);

            //5，填充占位符
            prestatement.setString(1, "李金章");
            prestatement.setString(2, "com.lijz@meiyouJJ");
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sfd.parse("2022-09-28");
            prestatement.setDate(3, new java.sql.Date(date.getTime()));

            //6，执行sql操作
            prestatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7，资源的关闭
            JDBCUtil.release(conn, prestatement, null);
        }
    }

    //修改customers表的一条记录
    @Test
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement prestatement = null;

        try {
            //获取数据库的连接
            conn = JDBCUtil.getConnection("test");

            //预编译sql语句
            String sql = "UPDATE customers SET name = ? WHERE id = ?";
            prestatement = conn.prepareStatement(sql);

            //填充占位符
            prestatement.setString(1, "泥头车");
            prestatement.setString(2, "21");

            //执行
            prestatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源的关闭
            JDBCUtil.release(conn, prestatement, null);
        }
    }

    /**
     *
     *     //查询表中数据
     *         String sql1 = "SELECT * FROM customer OBEY BY id";
     *         prestatement = conn.prepareStatement(sql1);
     *         ResultSet resultSet = prestatement.getResultSet(sql1);
     *         while(resultSet.next()){
     *             int id = resultSet.getInt("id");
     *             String name= resultSet.getString("name");
     *             String email = resultSet.getString("email");
     *             Date birth = resultSet.getDate("bitrh");
     *             System.out.println(id + " " + name + " " + email + " " + birth);
     *         }
     *
     *
     */
}
