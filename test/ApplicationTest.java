import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.Session;
import org.junit.*;

import persistence.dao.UserDAO;
import persistence.dao.impl.UserDAOImpl;
import persistence.models.User;
import persistence.utils.HibernateUtils;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;
import service.UserService;
import service.impl.UserServiceImpl;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


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

    }






}
