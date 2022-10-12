package jdbc_Test.CRUD.Instances;

import java.util.Date;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-30 10:42
 * @description:
 **/
public class Customer {
    private int id;
    private String name;
    private String email;
    private Date birth;

    private Customer() {
    }

    private Customer(int id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    private static Customer instance = null;

    public static Customer getInstance() {
        if (instance == null) {
            synchronized (Customer.class) {
                if (instance == null) {
                    instance = new Customer();
                }
            }
        }
        return instance;
    }

    public static Customer getInstance(int id, String name, String email, Date birth) {
        if (instance == null) {
            synchronized (Customer.class) {
                if (instance == null) {
                    instance = new Customer(id,name,email,birth);
                }
            }
        }
        return instance;
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

    public Date getDate() {
        return birth;
    }

    public void setDate(Date birth) {
        this.birth = birth;
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
