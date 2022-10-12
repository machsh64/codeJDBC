package jdbc_Test;

import JDBC.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-27 11:48
 * @description:
 **/
public class sqlTest {
    public static void main(String[] args) throws SQLException {
        String sql = "Create Table md ( id INT NOT NULL COMMENT '编号' ," +
                " name VARCHAR(50) COMMENT '姓名'," +
                "gender CHAR(1) COMMENT '性别') COMMENT '人员表'";

        System.out.println("连接数据库中");
        Connection conn = JDBCUtil.getConnection("itcast");
        System.out.println("执行sql语句中");
        Statement statement = conn.createStatement();
        //创建了一个名为 md 的表
        int resultSet = statement.executeUpdate(sql);
        System.out.println("操作执行成功");

        String sql1 = "INSERT INTO md(id,name,gender) VALUES(1001,'loser','男')";
        System.out.println("执行sql语句中");
        //将sql1 中的 字段 添加进了 md 表
        int resultSet1 = statement.executeUpdate(sql1);
        System.out.println("操作执行成功");

        String sql2 = "SELECT * FROM md";
        System.out.println("执行sql语句中");
        //打印 md 表的操作
        ResultSet resultSet2 = statement.executeQuery(sql2);
        while (resultSet2.next()) {
            int id = resultSet2.getInt("id");
            String name = resultSet2.getString("name");
            String gender = resultSet2.getString("gender");
            System.out.println(id + " " + name + " " + gender);
        }
        System.out.println("操作执行成功");

        JDBCUtil.release(conn,statement,null);
    }

    @Test
    public void test1() throws SQLException {
        //删除之前创建的表
        String sql = "DROP TABLE md";
        System.out.println("连接数据库中");
        Connection conn = JDBCUtil.getConnection("itcast");
        System.out.println("执行sql语句中");
        Statement statement = conn.createStatement();
        //删除了一个名为 md 的表
        int resultSet = statement.executeUpdate(sql);
        System.out.println("操作执行成功");

        JDBCUtil.release(conn,statement,null);
    }
}
