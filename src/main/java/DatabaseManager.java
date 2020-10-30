import hibernate.HibernateUtil;
import hibernate.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.xml.crypto.Data;
import java.util.*;

public class DatabaseManager {

    private static DatabaseManager instance = null;
    private Session session = null;

    private DatabaseManager(){
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public boolean createUser(String userName, String password){
        List<User> result = getAllRegisteredUsers();

        for(User u : result){
            if(u.getUserName().equals(userName)){
                return false;
            }

        }

        this.session.beginTransaction();
        User u = new User(userName, password);
        session.save(u);
        session.getTransaction().commit();


        return true;
    }

    public long getUserId(String userName) {
        List<User> result = getAllRegisteredUsers();

        for (User u : result) {
            if (u.getUserName().equals(userName)) {
                return u.getId();
            }
        }

        return 0;
    }

    public User getUser(String userName, String password) {
        List<User> result = getAllRegisteredUsers();

        for (User u : result) {
            if (u.getUserName().equals(userName) && u.getPassWord().equals(password)) {
                return u;
            }
        }

        return null;
    }

    public List<User> getAllRegisteredUsers(){
        CriteriaBuilder builder = this.session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        criteria.from(User.class);
        List<User> allUsers = this.session.createQuery(criteria).getResultList();

        return allUsers;
    }

}


