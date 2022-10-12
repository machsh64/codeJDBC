package jdbc_Test;

import JDBC.JDBCUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-28 13:37
 * @description:
 **/
public class PersonTest {
    public static void main(String[] args){
        String sql = "SELECT * FROM tb_user ORDER BY id";
        Connection connection = JDBCUtil.getConnection("itcast");
 /*       Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            System.out.println(id + " " +name+ "\t\t" + age + "\t" + gender);
        }*/

        /*String sql1 = "INSERT INTO tb_user(id,name,age,gender) VALUES(?,?,?,?)";
        JDBCUtil.update(connection,sql1,1007,"konjor",43,"男");*/
        String sql1 = "UPDATE tb_user SET name = ? , age = ? , gender = ? WHERE id = ?";
        JDBCUtil.update(connection,sql1,"lilise","25","女",1007);

        //释放资源
        /*JDBCUtil.release(connection,statement,resultSet);*/
    }
}
