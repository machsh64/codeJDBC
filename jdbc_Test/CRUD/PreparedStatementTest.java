package jdbc_Test.CRUD;

import JDBC.JDBCUtil;
import jdbc_Test.CRUD.Instances.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-11 19:06
 * @description: 演示解决PreparedStatement的sql注入问题
 *
 *    PreparedStatement除了解决了Statement的拼串，sql注入问题之外，PreparedStatement还有那些好处？
 *    1，PreparedStatement操作Blob的数据，而Statement做不到
 *    2，PreparedStatement可以实现更高效的批量操作
 **/
public class PreparedStatementTest {

    @Test
    public void testLogin() {
        Connection conn = null;
        User user = null;
        conn = JDBCUtil.getConnection("test");
        Scanner scan = new Scanner(System.in);

        while (true) {
            String sql = "SELECT user user,password password,balance balance FROM user_table WHERE user = ? AND password = ?";

            System.out.print("请输入账号名：");
            String username = scan.next();
            System.out.print("请输入密码：");
            String password = scan.next();

            user = getInstance(User.class, conn, sql, username,password);
            if (user != null) {
                System.out.println("登陆成功");
                System.out.println("用户 " + username + " 的账户余额为：" + user.getBalance());
                break;
            } else {
                System.out.println("用户名或用户密码输入错误，请重新输入");
            }
        }
        JDBCUtil.release(conn,null,null);
    }

    //针对于不同表的通用的查询操作，返回一条结果
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
        }finally {
            JDBCUtil.release(null,preStatement,resultSet);
        }
        return null;
    }
}
