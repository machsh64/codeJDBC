package DAO;

import DAO.Customer;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 /**
 * @program: codeJDBC
 * @author: Ren
 * @Date: 2022/10/14 12:03
 * @description:
 *
 *   此接口用于规范对于customers表的常用操作
*/
@SuppressWarnings("unused")
public interface CustomerDAO {

    //将cust对象添加到数据库中
    int insert(Connection conn, Customer cust);

    //针对于指定的ID，删除表中的一条记录
    int deleteByID(Connection conn,int id);

    //针对于内存中的Customer对象，修改数据表中指定的记录
    int update(Connection conn, Customer cust);

    //针对于指定的ID查询得到对应的Customer对象
    Customer getCustomerByID(Connection conn, int id);

    //查询表中所有的记录构成的集合
    List<Customer> getAll(Connection conn);

    //返回数据表中数据的条目数
    long getCount(Connection conn);

    //返回数据表中最大的生日
    Date getMaxBirth(Connection conn);
}
