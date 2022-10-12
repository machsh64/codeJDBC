package jdbc_Test.CRUD.Instances;

import java.math.BigDecimal;

/**
 * @program: code2
 * @author: Ren
 * @create: 2022-09-30 15:39
 * @description:
 **/
public class Products {
    private String id;
    private int vid;
    private String name;
    private BigDecimal price;
    private String desc;

    public Products() {
    }

    public Products(String id, int vid, String name, BigDecimal price, String desc) {
        this.id = id;
        this.vid = vid;
        this.name = name;
        this.price = price;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Products{" +
                "id=" + id +
                ", vid=" + vid +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                '}';
    }
}
