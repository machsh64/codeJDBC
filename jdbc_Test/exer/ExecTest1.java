package jdbc_Test.exer;

import JDBC.JDBCUtil;
import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-11 21:45
 * @description:  从控制台向数据库的表customers中插入一条数据，表结构如下
 *    id  name  email  birth  photo
 **/
public class ExecTest1 {
    public static void main(String[] args) {
        Connection conn = null;
        FileInputStream fis = null;
        Scanner scan = new Scanner(System.in);
        try{
            conn = JDBCUtil.getConnection("test");
            String sql = "INSERT INTO customers(name,email,birth,photo) VALUES(?,?,?,?)";

            System.out.println("请输入姓名：");
            String name = scan.next();
            System.out.println("请输入邮箱：");
            String email = scan.next();
            System.out.println("请输入生日日期(yyyy-MM-dd)：");
            String date = scan.next();  //日期格式为yyyy-MM-dd，则可自动转换为sql格式日期  隐式转换
            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date date = new java.sql.Date(sdf.parse(str).getTime());*/
            System.out.println("请输入需要上传的照片地址：已自动上传");
            fis = new FileInputStream("jdbc_test\\exer\\IOFile\\Photo.jpg");

            System.out.println(JDBCUtil.update(conn, sql, name, email, date, fis) >0 ? "上传成功" : "上传失败");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(fis != null)
                    fis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2() {
        Connection conn = null;
        PreparedStatement preStatement = null;


    }

    @Test
    public void test() {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try{
            bis = new BufferedInputStream(new FileInputStream("D:\\machs\\Pictures\\album\\1bb97b639347543f0e2e60f0a54f010e1d846af9_raw.jpg"));
            bos = new BufferedOutputStream(new FileOutputStream("jdbc_test\\exer\\IOFile\\Photo.jpg"));

            int len;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1){
                bos.write(buffer,0,len);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
