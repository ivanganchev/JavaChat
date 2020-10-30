package hibernate;

import javax.persistence.*;

@Entity
@Table(name = "users",schema = "chatdata" ,uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "USERNAME") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    //public static long userCounter = 1;
    @Column(name = "USERNAME", unique = true, nullable = false, length = 100)
    private String userName;
    @Column(name = "PASSWORD", unique = false, nullable = false, length = 100)
    private String passWord;

    public User(String userName, String password){
//        id += userCounter;
//        userCounter++;
        this.userName = userName;
        this.passWord = password;

    }

    public User(){

    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }






}
