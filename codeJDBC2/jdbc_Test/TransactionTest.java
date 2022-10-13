package jdbc_Test;

import JDBC.JDBCUtil;
import jdbc_Test.Instance.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-13 14:49
 * @description: 1，什么叫数据库事务？
 *       事务：一组逻辑操作单元，使数据从一种状态变换到另一种状态
 *            > 一组逻辑操作单元：一个或多个DML操作
 *
 *       2，事务处理的原则
 *       保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 *       当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存下来；
 *       要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *
 *       3，数据一旦提交，就不可回滚
 *
 *       4，哪些操作会导致数据的自动提交？
 *          > DDL操作一旦执行，都会自动提交
 *          > DML默认情况下，一旦执行，就会自动提交
 *             > 我们可以通过set autocommit = false的方式取消DML操作的自动提交
 *          > 默认在关闭连接时，会自动的提交数据
 **/
public class TransactionTest {

    //考虑数据库事务后的转账操作
    @Test
    public void testUpdate() {
        Connection conn = null;

        try {
            conn = JDBCUtil.getConnection("test");
            //1，取消数据的自动提交
            conn.setAutoCommit(false);

            String sql1 = "UPDATE user_table " +
                    "SET balance = balance - 100 " +
                    "WHERE user = ?";
            update(conn, sql1, "AA");

            //模拟提交数据产生的异常
            /*System.out.println(100/0 == 1);*/

            String sql2 = "UPDATE user_table " +
                    "SET balance = balance + 100 " +
                    "WHERE user = ?";
            update(conn, sql2, "BB");

            System.out.println("转账成功!");

            //2，提交数据
            conn.commit();

        } catch (SQLException e) {
            e.getErrorCode();

            //3，回滚数据
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            //设置为自动提交数据
            //主要针对于数据库连接池的使用
            try {
                if(conn != null)
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtil.release(conn, null, null);
        }
    }

    //通用的增删改操作  //考虑数据库事务后的转账操作
    public void update(Connection conn, String sql, Object... args) {
        PreparedStatement preStatement = null;
        try {
            //预编译sql语句，返回preStatement的实例
            preStatement = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preStatement.setObject(i + 1, args[i]);
            }

            //执行sql语句
            preStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //为了不影响后续对数据库的操作，仅释放该操作中实例的资源
            try {
                if (preStatement != null)
                    preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*****************************************************/
    @Test
    public void testTransactionSelect() {
        Connection conn = null;

        try{
            conn = JDBCUtil.getConnection("test");

            //获取当前连接的隔离级别
            System.out.println(conn.getTransactionIsolation());
            //设置数据库的隔离级别
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            //取消自动提交数据
            conn.setAutoCommit(false);

            String sql = "SELECT user,password,balance " +
                    "FROM user_table " +
                    "WHERE user = ?";
            User user = getInstance(User.class, conn, sql,"CC");

            System.out.println(user);   //update修改之后，未进行提交操作，此处数据库的隔离级别为2，解决了脏读问题，
                                        //所以未提交的数据不会被读取到  查询到的数据仍然为修改之前的数据
        } catch (SQLException e){
            e.getErrorCode();
        } finally {
            JDBCUtil.release(conn,null,null);
        }
    }

    @Test
    public void testTransactionUpdate() {
        Connection conn = null;

        try{
            conn = JDBCUtil.getConnection("test");

            //取消自动提交数据
            conn.setAutoCommit(false);
            String sql = "UPDATE user_table " +
                    "SET balance = ? " +
                    "WHERE user = ?";
            update(conn,sql,5000,"CC");

            Thread.sleep(1500);
            System.out.println("修改结束...");

        } catch (SQLException e){
            e.getErrorCode();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(conn,null,null);
        }
    }

    //通用的表字段查询操作，返回一条数据 return instance
    public <T> T getInstance(Class<T> clazz, Connection conn, String sql, Object... args) {
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
}
