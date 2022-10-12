package jdbc_Test.exer;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-11 23:37
 * @description:   针对于数据库中学生成绩表的映射表
 **/
public class ExamStudent {
    private int id;
    private int type;
    private String idCard;
    private String examCard;
    private String name;
    private String location;
    private int grade;

    public ExamStudent() {

    }

    public ExamStudent(int id, int type, String idCard, String examCard, String name, String location, int grade) {
        this.id = id;
        this.type = type;
        this.idCard = idCard;
        this.examCard = examCard;
        this.name = name;
        this.location = location;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "ExamStudent{" +
                "id=" + id +
                ", type=" + type +
                ", idCard='" + idCard + '\'' +
                ", examCard='" + examCard + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", Grade=" + grade +
                '}';
    }
}
