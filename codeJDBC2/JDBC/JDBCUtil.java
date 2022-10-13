package JDBC;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-27 09:37
 * @description:
 **/
@SuppressWarnings("unused")
public final class JDBCUtil {

    static String driverClass;
    static String url1;  //loadsql  from the sql book
    static String url2;  //itcast   made with myself
    static String url3;  //test     from the atguigu :Teacher Son
    static String username;
    static String password;

    //配置文件，驱动注册封装
    static {
        try {
            Properties pro = new Properties();
            //加载配置文件
            pro.load(new FileInputStream("JDBC/jdbc"));
            //获取配置文件对应信息
            driverClass = pro.getProperty("driverClass");
            url1 = pro.getProperty("url1");
            url2 = pro.getProperty("url2");
            url3 = pro.getProperty("url3");
            username = pro.getProperty("username");
            password = pro.getProperty("password");
            //注册驱动
            Class.forName(driverClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取数据库的连接
    //! 注  数据库的连接考虑到连接出现异常后，程序应该终止进行，此处将异常抛出，如果使用catch，则有可能出现连接出现异常的情况下程序仍在进行的情况
    public static Connection getConnection(String url) throws SQLException {
        Connection conn = null;

            //获取链接
            if (url.equals("loadsql")) {
                url = url1;
            } else if (url.equals("itcast")) {
                url = url2;
            } else if (url.equals("test")) {
                url = url3;
            } else {
                throw new RuntimeException("配置文件中没有加载名为 " + url + " 的数据库");
            }
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功...\n");

        return conn;
    }

    //释放资源
    //！注  所有操作方法中仅释放了方法内涉及到的资源，所有调用操作最后应该使用该方法进行统一资源释放
    public static void release(Connection conn, Statement statement, ResultSet resultSet) {
        try {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            System.out.println("\n资源释放完毕...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通用的增删改操作
    public static int update(Connection conn, String sql, Object... args) {
        PreparedStatement preStatement = null;
        try {
            //预编译sql语句，返回preStatement的实例
            preStatement = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preStatement.setObject(i + 1, args[i]);
            }

            //执行
           /* return preStatement.execute();*/
            //如果执行的是查询操作，有返回结果，则返回true
            //如果执行的是增删改操作，没有返回结果，则此方法始终返回false
            return preStatement.executeUpdate();   //此方法返回的是一个int型数据，返回的是执行命令的次数，若返回0，则没有任务成功执行
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //为了不影响后续对数据库的操作，仅释放该操作中实例的资源
            try{
                if(preStatement != null)
                preStatement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    //通用的表字段查询操作，返回一条数据 return instance
    public static <T> T getInstance(Class<T> clazz, Connection conn, String sql, Object... args) {
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;

        try {
            //连接数据库
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
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //读取每个列的列值，通过ResultSet
                    Object columnValue = resultSet.getObject(i + 1);
                    //通过ResultMetaData
                    //获取列的列名 rsmd.getColumnName();    不推荐使用
                    //获取列的别名 rsmd.getColumnLabel();
                    /* String columnName = rsmd.getColumnName(i + 1);*/   //获取到的是表中的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);   //获取到的是sql语句中，所属表中的别名
                    //通过反射，将对象指定名赋值为指定的值
                    Field field = t.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //为了不影响后续对数据库的操作，仅释放该操作中实例的资源
            try{
                if(preStatement != null)
                    preStatement.close();
                if(resultSet != null)
                    resultSet.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //通用的表查询操作，返回多条数据 return List<T> list
    //针对于不同表的通用的查询操作，返回多条结果
    public static <T> List<T> getInstances(Class<T> clazz, Connection conn, String sql, Object ...args) {
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;

        try {
            //连接数据库
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
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()) {
                T t = clazz.newInstance();
                //处理集中每一行数据中的每一个列：给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    //读取每个列的列值，通过ResultSet
                    Object columnValue = resultSet.getObject(i + 1);
                    //通过ResultMetaData
                    //获取列的列名 rsmd.getColumnName();    不推荐使用
                    //获取列的别名 rsmd.getColumnLabel();
                    /* String columnName = rsmd.getColumnName(i + 1);*/   //获取到的是表中的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);   //获取到的是sql语句中，所属表中的别名
                    //通过反射，将对象指定名赋值为指定的值
                    Field field = t.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //为了不影响后续对数据库的操作，仅释放该操作中实例的资源
            try{
                if(preStatement != null)
                    preStatement.close();
                if(resultSet != null)
                    resultSet.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //创建该类时的快捷测试，可删除，但不建议，后续增加功能可在此处测试
    public static void main(String[] args) throws Exception {
        String sql = "SELECT * FROM tb_user ORDER BY id";
        //1，获取连接
        Connection conn = JDBCUtil.getConnection("itcast");
        //2，创建执行语句对象
        PreparedStatement statement = conn.prepareStatement(sql);
        //3，执行语句
        ResultSet resultSet = statement.executeQuery();
        //4，处理结果
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            System.out.println(id + " " + name + " " + age + " " + gender);
        }
        //5，关流
        JDBCUtil.release(conn, statement, resultSet);
    }

}
