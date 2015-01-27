package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import src.service.UserService;
import src.service.impl.UserServiceImpl;
import src.persistence.dao.impl.UserDAOImpl;
import views.html.index;
import views.html.login;
import views.html.logout;

import static play.data.Form.form;

/**
 * Created by tkachdan on 09-Dec-14.
 */
public class Login extends Controller {

    /**
     *
     * @return login page
     */
    public static Result renderLogin() {
        if (!session().isEmpty()) {
            return Login.logout();
        }
        return ok(login.render(form(LoginForm.class)));

    }

    /**
     * authenticate if the entered user is in the database
     * @return if YES the user will be redirected to the main page
     *         if NO the user stays on this page oan has to ester  correct email and username
     */
    public static Result authenticate() {

        Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
        LoginForm form = loginForm.get();
        String password = form.password;
        String email = form.email;
        System.out.println("***************************");
        System.out.println(email);
        System.out.println(password);
        System.out.println("***************************");

        UserService userService = new UserServiceImpl();
        boolean result = userService.login(email, password);

        if (result == true) {
            System.out.println("Login successful!");
            session().clear();
            session("email", form.email);   //Vytvoření a uložení potřebných dat do session (email bude nahrazen usernamem)
            session().put("designation", UserDAOImpl.getDesignation(form.email));    //Designation je potřeba pro zobrazování tlačítek administrátora
            return redirect(routes.Application.renderIndexLogined());
        } else {
            System.out.println("Login ERROR!");
            return badRequest(login.render(loginForm));
        }
    }

    /**
     *
     * @return logout page
     */
    public static Result logout() {
        session().clear();
        System.out.println("User has been logged out");
        //return ok(logout.render("index"));
        return controllers.Login.renderLogin();
    }

    public static class LoginForm {

        public String email;
        public String password;

    }

}