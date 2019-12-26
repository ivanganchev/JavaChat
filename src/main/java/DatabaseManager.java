import java.util.*;

public class DatabaseManager {
    private static List<User> allUsers = new ArrayList<>();


    public static boolean createUser(String userName, String password){
        for(User u : allUsers){
            if(u.getUserName().equals(userName)){
                return false;
            }
        }

        allUsers.add(new User(userName, password));
        return true;
    }

    public static long getUserId(String userName){
        long id = 0;

        for(User u : allUsers){
            if(u.getUserName().equals(userName)){
                id = u.getId();
            }
        }

        return id;
    }

}


