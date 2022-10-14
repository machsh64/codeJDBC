package DAO2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-14 11:21
 * @description:
 *
 *   DAO: data(base) access object 数据库访问对象
 *   封装了针对于数据表的通用的操作
 **/
public abstract class BaseDAO<T> {
    PreparedStatement preStatement = null;
    ResultSet resultSet = null;

    private Class<T> clazz = null;
    //后期谁继承了该类，则会在谁中运行此代码块对clazz进行赋值
    {
        //获取当前BaseDAO的子类继承父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;

        Type[] typeArguments = paramType.getActualTypeArguments();   //获取了父类的泛型类型数组
        clazz = (Class<T>) typeArguments[0];  //泛型的第一个参数
    }

    //通用的增删改操作
    public int update(Connection conn, String sql, Object... args) {
        try {
            //预编译sql语句，返回preStatement的实例
            preStatement = conn.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preStatement.setObject(i + 1, args[i]);
            }

            //此方法返回的是一个int型数据，返回的是执行命令的次数，若返回0，则没有任务成功执行
            return preStatement.executeUpdate();
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
    public T getInstance(Connection conn, String sql, Object... args) {
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
                    String columnLabel = rsmd.getColumnLabel(i + 1);  //获取到的是sql语句中，所属表中的别名
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
    public List<T> getInstances(Connection conn, String sql, Object ...args) {
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

    //用于查询特殊值的通用的方法
    public Object getValue(Connection conn, String sql, Object... args){
        try{
            preStatement = conn.prepareStatement(sql);

            //填充占位符
            for(int i = 0; i < args.length; i++){
               preStatement.setObject(i + 1,args[i]);
            }

            resultSet = preStatement.executeQuery();
            if(resultSet.next())
                return resultSet.getObject(1);
        }catch (SQLException e){
            e.getErrorCode();
        }finally {
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
