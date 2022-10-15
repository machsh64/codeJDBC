package dbutils;

import DAO.Customer;
import JDBC.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-15 20:32
 * @description:
 *
 *  commons-dbutils 是Apache 组织提供的一个开源JDBC工具类库，封装了针对于数据库的增删改查操作
 **/
public class QueryRunnerTest {
    QueryRunner runner = null;
    Connection conn = null;

    //测试插入
    @Test
    public void testInsert() {
        try{
            runner = new QueryRunner();
            conn = JDBCUtils.getConnection2();

            String sql = "INSERT INTO customers(name,email,birth) " +
                    "VALUES(?,?,?)";
            int insertCount = runner.update(conn,sql,"牛马李","NiuMa@Mun.con","1983-10-21");

            System.out.println("添加了 " + insertCount + "条记录");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.release(conn,null);
        }
    }

    //测试查询
    //BeanHandler   &   BeanListHandler
    @Test
    public void testQuery1() {
        try{
            runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();

            //一条记录
            //BeanHandler：是ResultSetHandler接口的实现类，用于封装表中的一条记录
           /* String sql = "SELECT id, name, email, birth " +
                    "FROM customers " +
                    "WHERE id = ?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer cust = runner.query(conn, sql, handler, 23);*/

            //一组记录
            //BeanListHandler：是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合
            String sql = "SELECT id, name, email, birth " +
                    "FROM customers " +
                    "WHERE id < ?";
            BeanListHandler<Customer> handlers = new BeanListHandler<>(Customer.class);
            List<Customer> custs = runner.query(conn,sql,handlers,23);
            custs.forEach(System.out::println);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.release(conn,null);
        }
    }

    //测试查询
    //MapHandler
    @Test
    public void testQuery2() {
        try{
            runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();

            //一个记录
            //MapHandler：是ResultSetHandler接口的实现类，用于封装表中的一条记录构成
            String sql = "SELECT id, name, email, birth " +
                    "FROM customers " +
                    "WHERE id = ?";
            MapHandler handler = new MapHandler();
            Map<String,Object> cust = runner.query(conn,sql,handler,23);
            System.out.println(cust);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.release(conn,null);
        }
    }

    //测试查询
    //MapListHandler
    @Test
    public void testQuery3() {
        try{
            runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();

            //一个记录
            //MapHandler：是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合
            String sql = "SELECT id, name, email, birth " +
                    "FROM customers " +
                    "WHERE id < ?";
            MapListHandler handlers = new MapListHandler();
            List<Map<String,Object>> custs = runner.query(conn,sql,handlers,23);
            custs.forEach(System.out::println);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.release(conn,null);
        }
    }

    //针对于表中特殊值的查询
    //ScalarHandler
    @Test
    public void testQuery4() {
        try{
            runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();

            //一个记录
            //MapHandler：是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合
            String sql = "SELECT count(*) " +
                    "FROM customers ";
            ScalarHandler handler = new ScalarHandler();
            long count = (Long) runner.query(conn, sql, handler);
            System.out.println(count);   //17

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.release(conn,null);
        }
    }

    @Test
    public void testQuery5() {
        try{
            runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();

            //一个记录
            //MapHandler：是ResultSetHandler接口的实现类，用于封装表中的多条记录构成的集合
            String sql = "SELECT MIN(birth) " +
                    "FROM customers ";
            ScalarHandler handler = new ScalarHandler();
            Date birth = (Date) runner.query(conn, sql, handler);
            System.out.println(birth);   //1955-07-14

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.release(conn,null);
        }
    }

}
