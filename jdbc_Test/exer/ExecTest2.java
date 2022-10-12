package jdbc_Test.exer;

import JDBC.JDBCUtil;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Scanner;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-11 22:39
 * @description:
 *
 */
public class ExecTest2 {
    /**创立数据库表examstudent，表结构如下：
     * 字段名          说明       类型
     * FlowID         流水号     int(10)
     * Type           四级/六级  int(5)
     * IDCard         身份证号码  varchar(18)
     * ExamCard       准考证号码  varchar(15)
     * StudentName    学生姓名   varchar(20)
     * Location       区域       varchar(20)
     * Grade          成绩       int(10)
     **/
    @Test
    public void CreatTable() {
        Connection conn = null;

        conn = JDBCUtil.getConnection("test");
        String sql = "CREATE TABLE examstudent (" +
                "FlowID int(10) COMMENT '流水号' AUTO_INCREMENT," +
                "Type int(5) COMMENT '四级/六级' NOT NULL," +
                "IDCard varchar(18) COMMENT '身份证号码' NOT NULL," +
                "ExamCard varchar(15) COMMENT '准考证号码' NOT NULL," +
                "StudentName varchar(20) COMMENT '学生姓名' NOT NULL," +
                "Location varchar(20) COMMENT '区域' DEFAULT NULL," +
                "Grade int(10) COMMENT '成绩' DEFAULT NULL,PRIMARY KEY(FlowID)) COMMENT '四级成绩表';";

        JDBCUtil.update(conn, sql);

        JDBCUtil.release(conn,null,null);
    }

    /**
     * 向表中插入数据
     */
    @Test
    public void insertDetail() {
        Scanner scan = new Scanner(System.in);

        Connection conn = null;
        conn = JDBCUtil.getConnection("test");
        String sql = "INSERT INTO examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade) " +
                     "VALUES(?,?,?,?,?,?)";
        System.out.print("请输入考试项目(4/6): ");
        int type = scan.nextInt();
        System.out.print("请输入身份证号码: ");
        String IDCard = scan.next();
        System.out.print("请输入准考证号码: ");
        String ExamCard = scan.next();
        System.out.print("请输入姓名: ");
        String StudentName = scan.next();
        System.out.print("请输入区域: ");
        String Location = scan.next();
        System.out.print("请输入成绩: ");
        int Grade = scan.nextInt();

        System.out.println(JDBCUtil.update(conn, sql, type, IDCard, ExamCard, StudentName, Location, Grade) > 0 ? "上传成功" : "上传失败");

        JDBCUtil.release(conn,null,null);
    }

    /**
     * 建立Java程序，要求输入身份证号或准考证号可以查询到学身份的基本信息
     */
    @Test
    public void QueryDetail() {
        Scanner scan = new Scanner(System.in);
        Connection conn = null;
        PreparedStatement preStatement = null;
        ResultSet resultset = null;
        String sql = null;
        String pre = null;

        try {
            conn = JDBCUtil.getConnection("test");
            System.out.println("请选择你要输入的类型\na 身份证号\nb 准考证号");
            String str = scan.next();
            sql = "SELECT `FlowID` `id`,`Type` `type`,`IDCard` `idCard`,`ExamCard` `examCard`," +
                    "`StudentName` `name`,`Location` `location`,`Grade` `grade` " +
                    "FROM examstudent " +
                    "WHERE";

            //sql对输入类型的多种方式
            if ("a".equals(str)) {
                sql += " `IDCard` = ?";
                System.out.println("请输入你的身份证号码: ");
                pre = scan.next();
            } else if ("b".equals(str)) {
                sql += " `ExamCard` = ?";
                System.out.println("请输入你的准考证号码: ");
                pre = scan.next();
            } else {
                System.out.println("您的输入有误！请重新进入程序");
            }

            //调用工具类实现update操作
            ExamStudent stu = JDBCUtil.getInstance(ExamStudent.class,conn,sql,pre);

            //手写的底层实现逻辑代码
            /*//填充占位符
            preStatement = conn.prepareStatement(sql);
            preStatement.setObject(1, pre);

            //获取查询结果集
            resultset = preStatement.executeQuery();
            ResultSetMetaData rsmd = resultset.getMetaData();
            //获取表的列数
            int columnCount = rsmd.getColumnCount();
            //创建表的对象
            ExamStudent stu = null;
            //遍历表的列数
            while (resultset.next()) {
                stu = new ExamStudent();
                //处理列中每一个值，给stu指定对象属性赋值
                for (int i = 0; i < columnCount; i++) {
                    //获取列的标签名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //获取列值
                    Object columnValue = resultset.getObject(i + 1);
                    //获取表的运行时类对象
                    Class<? extends ExamStudent> clazz = stu.getClass();
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(stu, columnValue);
                }
            }*/
            if (stu != null) {
                System.out.println("====查询结果====");
                System.out.println("流水号：" + stu.getId());
                System.out.println("4级/6级：" + stu.getType());
                System.out.println("身份证号：" + stu.getIdCard());
                System.out.println("准考证号：" + stu.getExamCard());
                System.out.println("学生姓名：" + stu.getName());
                System.out.println("区域：" + stu.getLocation());
                System.out.println("成绩：" + stu.getGrade());
            } else {
                System.out.println("查无此人！请重新进入程序");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //JDBCUtil.release(conn, preStatement, resultset);
            JDBCUtil.release(conn, null, null);
        }
    }

    /**
     * 完成学生信息的删除功能
     */
    @Test
    public void DeleteDetail() {
        Connection conn = null;
        PreparedStatement preStatement = null;
        Scanner scan = new Scanner(System.in);

        try {
            conn = JDBCUtil.getConnection("test");
            String sql = "DELETE FROM examstudent " +
                         "WHERE ExamCard = ?";

            //调用工具方法实现操作
            while(true) {
                System.out.println("请输入学生的考号：");
                String ExamCard = scan.next();
                if (JDBCUtil.update(conn,sql,ExamCard) > 0) {
                    System.out.println("删除成功!");
                    break;
                } else {
                    System.out.println("查无此人，请重新输入!");
                }
            }

            //手写的底层操作实现逻辑
            /*while(true) {
                System.out.println("请输入学生的考号：");
                String ExamCard = scan.next();

                preStatement = conn.prepareStatement(sql);
                preStatement.setObject(1,ExamCard);

                if (preStatement.executeUpdate() > 0) {
                    System.out.println("删除成功!");
                    break;
                } else {
                    System.out.println("查无此人，请重新输入!");
                }
            }
        }catch (SQLException e){
            e.getErrorCode();*/
        }finally {
            //JDBCUtil.release(conn,preStatement,null);
            JDBCUtil.release(conn,null,null);
        }

    }
}
