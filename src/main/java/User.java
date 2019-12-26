public class User {
    private long id;
    public static long userCounter = 1;
    private String userName;
    private String passWord;

    public User(String userName, String password){
        id += userCounter;
        userCounter++;
        this.userName = userName;
        this.passWord = password;

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
