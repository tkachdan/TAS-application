import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import src.persistence.dao.UserDAO;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.User;
import src.persistence.utils.HibernateUtils;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


/**
*
* Simple (JUnit) tests that can call viewAll parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {
   public static Session session = new HibernateUtils().getSessionFactory().openSession();
    static UserDAO userDAO = new UserDAOImpl();


    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    @Test
    public void daoTest() {
        User user = new User("test", "simple", "firstname", "lastname", "pass", "username");
        userDAO.saveUser(user);

        List userResults = session.createQuery("from User ").list();

        User userRes = (User) userResults.get(userResults.size() - 1);

        Assert.assertEquals(user.getFirstName(), userRes.getFirstName());

    }


   /* @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }


    //String designation, String email,
    //String firstName, String lastName,
    //String password, String username
    @Test
     public void daoTest(){
        User user = new User("test","test@test.tet", "firstname", "lastname", "pass", "username");
        userDAO.saveUser(user);

        List userResults = session.createQuery("from User ").list();

        User userRes = (User) userResults.get(userResults.size() - 1);

        Assert.assertEquals(user.getFirstName(),userRes.getFirstName());

    }

    @Test
    public void getByEmail(){
        User user = new User("test","test", "firstname", "lastname", "pass", "username");
        userDAO.saveUser(user);

        User user1 = userDAO.getUserByEmail("test@test.tet");

        Assert.assertEquals(user.getUsername(),user1.getUsername());

    }

    @Test
    public void loginTest(){
        User user = new User("test","test@test.tet", "firstname", "lastname", "pass", "username");
        userDAO.saveUser(user);

        UserService userService = new UserServiceImpl();
        boolean result = userService.login("test@test.tet","pass");

        System.out.println(result);
        Assert.assertEquals(result,true);

    }*/






}
