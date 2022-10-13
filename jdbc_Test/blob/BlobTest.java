package jdbc_Test.blob;

import JDBC.JDBCUtil;
import jdbc_Test.CRUD.Instances.Customer;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Scanner;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-12 13:18
 * @description:  测试使用PrepareStatement操作Blob类型的数据
 **/
public class BlobTest {

    //像数据表customers中插入Blob类型的字段
    @Test
    public void testInsert() {
        Connection conn = null;
        PreparedStatement preStatement = null;
        FileInputStream photo = null;
        Scanner scan = new Scanner(System.in);

        try{
            conn = JDBCUtil.getConnection("test");

            String sql = "INSERT INTO customers(name,email,birth,photo) " +
                         "VALUES(?,?,?,?)";

            preStatement = conn.prepareStatement(sql);

            System.out.println("请输入姓名：");
            String name = "色块";
            System.out.println("请输入邮箱：");
            String email = "12342@num.com";
            System.out.println("请输入生日：");
            String date = "1980-02-24";
            System.out.println("请输入需要上传的文件的地址：");
            photo = new FileInputStream("jdbc_Test\\exer\\IOFile\\Photo.jpg");

            preStatement.setObject(1,name);
            preStatement.setObject(2,email);
            preStatement.setObject(3,date);
            preStatement.setBlob(4,photo);

            System.out.println(preStatement.executeUpdate() > 0 ? "上传成功" : "上传失败");
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.getErrorCode();
        } finally {
            JDBCUtil.release(conn,null,null);
            try{
                if(photo != null)
                    photo.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //查询数据表中的Blob类型的字段
    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;
        ResultSetMetaData rsmd = null;
        InputStream is = null;
        FileOutputStream fos = null;
        Scanner scan = new Scanner(System.in);

        try{
            conn = JDBCUtil.getConnection("test");
            String sql = "SELECT `id` id, `name` name, `email` email, `birth` birth, `photo` photo " +
                         "FROM customers " +
                         "WHERE id = ?";
            preStatement = conn.prepareStatement(sql);
            System.out.println("请输入要查询数据的id: ");
            int id1 = scan.nextInt();
            preStatement.setObject(1,id1);

            resultSet = preStatement.executeQuery();
            rsmd = resultSet.getMetaData();
            Customer cust = null;
            //
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date date = resultSet.getDate("birth");

                cust = Customer.getInstance(id,name,email,date);
                System.out.println(cust);

                //将Blob类型的字段下载下来，以文件的方式保存在本地
                Blob photos = resultSet.getBlob("photo");
                is = photos.getBinaryStream();
                fos = new FileOutputStream("jdbc_Test\\blob\\picture.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }
            }
            //普通的方式查询操作
            /*while(resultSet.next()){
                cust = Customer.getInstance();
                //获取列数
                int columnCount = rsmd.getColumnCount();
                for(int i = 0; i < columnCount; i++){
                    //获取表中列名的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //获取表中数据值
                    Object obj = resultSet.getObject(i + 1);

                    //利用反射对Customer中的属性进行赋值
                    Field field = cust.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(cust,obj);
                }
            }
            System.out.println(cust);*/
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e){
            e.getErrorCode();
        } finally{
            try{
                if(is != null)
                    is.close();
                if(fos != null)
                    fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            JDBCUtil.release(conn,preStatement,resultSet);
        }
    }
}
