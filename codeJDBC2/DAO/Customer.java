package DAO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-14 12:05
 * @description:
 **/
public class Customer {
    private int id;
    private String name;
    private String email;
    private Date birth;
    private String photo;   //封装图片的路径

    public Customer() {

    }

    public Customer(int id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public Customer(int id, String name, String email, Date birth, String photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public FileInputStream getPhoto() {
        if (photo != null) {
            try {
                return new FileInputStream(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }
}
