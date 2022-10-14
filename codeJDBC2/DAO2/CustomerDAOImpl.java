package DAO2;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-14 12:14
 * @description:
 *
 *
 **/
public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO {

    //将cust对象添加到数据库中
    @Override
    public int insert(Connection conn, Customer cust) {
        try {
            if (cust.getPhoto() != null) {
                String sql = "INSERT INTO customers(name,email,birth,photo) " +
                        "VALUES(?,?,?,?)";
                return update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getPhoto());
            } else {
                String sql = "INSERT INTO customers(name,email,birth) " +
                        "VALUES(?,?,?)";
                return update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
            }
        } finally {
            if (cust.getPhoto() != null)
                try {
                    cust.getPhoto().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    //针对于指定的ID，删除表中的一条记录
    @Override
    public int deleteByID(Connection conn, int id) {
        String sql = "DELETE FROM customers " +
                "WHERE id = ?";
        return update(conn, sql, id);
    }

    //针对于内存中的Customer对象，修改数据表中指定的记录
    @Override
    public int update(Connection conn, Customer cust) {
        try {
            if (cust.getPhoto() != null) {
                String sql = "UPDATE customers " +
                        "SET name = ?,email = ?,birth = ?,photo = ? " +
                        "WHERE id = ?";
                return update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getPhoto(), cust.getId());
            } else {
                String sql = "UPDATE customers " +
                        "SET name = ?,email = ?,birth = ? " +
                        "WHERE id = ?";
                return update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
            }
        } finally {
            if (cust.getPhoto() != null)
                try {
                    cust.getPhoto().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    //针对于指定的ID查询得到对应的Customer对象
    @Override
    public Customer getCustomerByID(Connection conn, int id) {
        String sql = "SELECT id, name, email, birth " +
                "FROM customers " +
                "WHERE id = ?";
        return getInstance(conn, sql, id);
    }

    //查询表中所有的记录构成的集合
    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "SELECT id, name, email, birth " +
                "FROM customers";
        return getInstances(conn, sql);
    }

    //返回数据表中数据的条目数
    @Override
    public long getCount(Connection conn) {
        String sql = "SELECT count(id) " +
                "FROM customers";
        return (long) getValue(conn, sql);
    }

    //返回数据表中最大的生日
    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "SELECT MIN(birth) " +
                "FROM customers";
        return (Date) getValue(conn, sql);
    }
}
