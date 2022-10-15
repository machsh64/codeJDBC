package DAO2;

import JDBC.JDBCUtil;
import JDBC.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomerDAOImplTest {
    Connection conn;
    private CustomerDAOImpl dao = new CustomerDAOImpl();

    {
        try {
            conn = JDBCUtils.getConnection3();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void insert() throws ParseException {
        System.out.println(dao.insert(conn, new Customer(30, "岸边露伴", "too@jojora.com", new SimpleDateFormat("yyyy-MM-dd").parse("2002-10-21"))) > 0 ? "创建成功" : "创建失败");
    }

    @Test
    public void deleteByID() {
        System.out.println(dao.deleteByID(conn, 30) > 0 ? "删除成功" : "删除失败");
    }

    @Test
    public void update() throws ParseException {
        System.out.println(dao.update(conn, new Customer(27, "迪亚波罗", "diorpolo@jojora.com", new SimpleDateFormat("yyyy-MM-dd").parse("1983-10-21"))) > 0 ? "修改成功" : "修改失败");
    }

    @Test
    public void getCustomerByID() {
        System.out.println(dao.getCustomerByID(conn, 27));
    }

    @Test
    public void getAll() {
        List<Customer> list = dao.getAll(conn);
        list.forEach(System.out::println);
    }

    @Test
    public void getCount() {
        System.out.println(dao.getCount(conn));
    }

    @Test
    public void getMaxBirth() {
        System.out.println(dao.getMaxBirth(conn));
    }
}