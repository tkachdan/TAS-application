package DAO;

//import org.h2.engine.Session;

import org.hibernate.Session;
import org.junit.Test;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.User;
import src.persistence.utils.HibernateUtils;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by krasotin on 11-Dec-14.
 */
public class UserTest {
    //static org.hibernate.Session session = Service.getSession();

    static Session session = new HibernateUtils().getSessionFactory().openSession();
    static UserDAOImpl userDAO = new UserDAOImpl();

    public static void clearDB() {
        List<User> usersDb = userDAO.getAllUsers();
        for (User user : usersDb)
            userDAO.deleteUser(user.getUserId());
    }

    @Test
    public void testSaveUser() {
        clearDB();

        User user = new User("", "junitTest1@mail.cz", "pavel", "cejka", "pass", "pavcejka");
        userDAO.saveUser(user);

        User userDb = (User) session
                .createQuery("from User user where id = :userID")
                .setInteger("userID", user.getUserId()).uniqueResult();

        assertThat(user).isEqualTo(userDb);
    }

    @Test
    public void testGetUser() {
        clearDB();

        User user = new User("", "junitTest2@mail.cz", "pavel", "cejka", "pass", "pavcejka");
        userDAO.saveUser(user);

        User userDb = userDAO.getUser(user.getUserId());
        assertThat(user).isEqualTo(userDb);
    }

    @Test
    public void testGetNullUser() {
        clearDB();

        User user = new User("", "test@mail.cz", "pavel", "cejka", "pass", "pavcejka");
        userDAO.saveUser(user);

        User userDb = userDAO.getUser(user.getUserId() + 1);
        assertThat(userDb).isEqualTo(null);
        assertThat(userDb == null).isEqualTo(true);
    }

    @Test
    public void testGetUserByEmail() {
        clearDB();

        User user = new User("", "test@mail.cz", "pavel", "cejka", "pass", "pavcejka");
        userDAO.saveUser(user);

        User userDb = userDAO.getUserByEmail(user.getEmail());
        assertThat(user).isEqualTo(userDb);
    }

    @Test
    public void testGetAllUsers() {
        clearDB();

        User user1 = new User("", "test1@mail.cz", "pavel", "cejka", "pass", "pavcejka");
        User user2 = new User("", "test2@mail.cz", "pavel", "cejka", "pass", "pavcejka");
        userDAO.saveUser(user1);
        userDAO.saveUser(user2);

        List<User> usersDb = userDAO.getAllUsers();
        assertThat(usersDb.size()).isEqualTo(2);
    }
}
