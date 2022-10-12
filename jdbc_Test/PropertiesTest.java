package jdbc_Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-29 10:02
 * @description: /----Properties：常用来处理配置文件。key和value都是String
 **/
public class PropertiesTest {
    public static void main(String[] args) throws Exception {
        FileInputStream files = null;
        try {
            Properties pros = new Properties();
             files = new FileInputStream("JDBC/jdbc");
            pros.load(files);

            String name = pros.getProperty("username");
            String password = pros.getProperty("password");

            System.out.println("name: " + name + "  password: " + password);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (files != null){
                files.close();
            }
        }
    }
}
