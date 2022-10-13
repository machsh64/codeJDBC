package jdbc_Test.blob;

import JDBC.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-12 18:56
 * @description: 向数据库表中批量插入数据
 **/
public class GoodsTest {

    //建表
    @Test
    public void create() {
        Connection conn = null;

        conn = JDBCUtil.getConnection("test");
        String sql = "CREATE TABLE goods(" +
                "`id` int AUTO_INCREMENT COMMENT 'ID号码', " +
                "`name` varchar(20) NOT NULL DEFAULT '姓名' COMMENT '姓名', " +
                "PRIMARY KEY(id)) " +
                "COMMENT '人员姓名表'";
        System.out.println(JDBCUtil.update(conn, sql) > 0 ? "创建成功" : "创建失败");
        JDBCUtil.release(conn, null, null);
    }

    //批量插入数据
    //方式一：Statement 略

    //方式二：PrepareStatement
    @Test
    public void insert2() {
        Connection conn = null;
        PreparedStatement preStatement = null;

        try {
            conn = JDBCUtil.getConnection("test");
            String sql = "INSERT INTO goods(name) VALUES(?)";
            preStatement = conn.prepareStatement(sql);

            long start = System.currentTimeMillis();
            for(int i = 0; i < 20000; i++){
               preStatement.setObject(1,"name_"+(i+1));
               preStatement.execute();
            }
            long end = System.currentTimeMillis();

            System.out.println("总共花费的时间为：" + (end - start));
        }catch (SQLException e){
            e.getErrorCode();
        }finally {
            JDBCUtil.release(conn,preStatement,null);
        }
    }

    //批量插入的方式三：
    //1,addBatch() , executeBatch() , clearBatch()
    //2,mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
        //  ?rewriteBatchedStatements=true 写在配置文件的url后面
    //3,mysql配置文件需要为5.1.37及以上
    @Test
    public void insert3() {
        Connection conn = null;
        PreparedStatement preStatement = null;

        try {
            conn = JDBCUtil.getConnection("test");
            String sql = "INSERT INTO goods(name) VALUES(?)";
            preStatement = conn.prepareStatement(sql);

            long start = System.currentTimeMillis();
            for(int i = 1; i <= 1000000; i++){
                preStatement.setObject(1,"name_" + i);
                //1，“攒”sql
                preStatement.addBatch();
                if (i % 500 == 0){
                    //2，执行batch
                    preStatement.executeBatch();
                    //3，清空batch
                    preStatement.clearBatch();
                }
            }
            long end = System.currentTimeMillis();

            System.out.println("总共花费的时间为：" + (end - start));  //总共花费的时间为：6645
        }catch (SQLException e){
            e.getErrorCode();
        }finally {
            JDBCUtil.release(conn,preStatement,null);
        }
    }

    //批量插入的方式四：
    @Test
    public void insert4() {
        Connection conn = null;
        PreparedStatement preStatement = null;

        try {
            conn = JDBCUtil.getConnection("test");
            String sql = "INSERT INTO goods(name) VALUES(?)";
            preStatement = conn.prepareStatement(sql);

            long start = System.currentTimeMillis();
            //设置不允许自动提交数据
            conn.setAutoCommit(false);
            for(int i = 1; i <= 1000000; i++){
                preStatement.setObject(1,"name_" + i);
                //1，“攒”sql
                preStatement.addBatch();
                if (i % 500 == 0){
                    //2，执行batch
                    preStatement.executeBatch();
                    //3，清空batch
                    preStatement.clearBatch();
                }
            }
            //提交数据
            conn.commit();
            long end = System.currentTimeMillis();

            System.out.println("总共花费的时间为：" + (end - start));  //总共花费的时间为：4968
        }catch (SQLException e){
            e.getErrorCode();
        }finally {
            JDBCUtil.release(conn,preStatement,null);
        }
    }
}
