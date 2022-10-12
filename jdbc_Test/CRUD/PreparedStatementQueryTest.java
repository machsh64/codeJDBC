package jdbc_Test.CRUD;

import JDBC.JDBCUtil;
import jdbc_Test.CRUD.Instances.Order;
import jdbc_Test.CRUD.Instances.Products;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-30 15:13
 * @description:
 *
 * 使用PreparedStatement针对与不同表的查询操作
 **/
public class PreparedStatementQueryTest {

    public static void main(String[] args) {
        Connection conn = JDBCUtil.getConnection("test");
        String sql = "SELECT order_id orderId,order_name orderName,order_date orderDate FROM `order` WHERE order_id < ?";
        List<Order> list = getInstances(Order.class,conn,sql,4);
        assert list != null;
        list.forEach(System.out::println);

        System.out.println();

        conn = JDBCUtil.getConnection("loadsql");
        sql = "SELECT prod_id id,vend_id vid,prod_name name,prod_price price,prod_desc `desc` FROM products ORDER BY prod_price";
        List<Products> list1 = getInstances(Products.class,conn,sql);
        assert list1 != null;
        list1.forEach(System.out::println);
    }

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
            JDBCUtil.release(conn, preStatement, resultSet);
        }
        return null;
    }

    //针对于不同表的通用的查询操作，返回一条结果
    public static <T> T  getInstance(Class<T> clazz,Connection conn,String sql,Object ...args) {
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
            JDBCUtil.release(conn, preStatement, resultSet);
        }
        return null;
    }
}
