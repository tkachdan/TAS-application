package controllers;


import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import service.UserService;
import service.impl.UserServiceImpl;
import views.html.login;

import static play.data.Form.form;

/**
 * Created by tkachdan on 09-Dec-14.
 */
public class Login extends Controller {

    public static Result renderLogin(){

        return ok(login.render(form(LoginForm.class)));

    }

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
        boolean result = userService.login(email,password);
        if(result == true) {
            System.out.println("Login successful!");
            session().clear();
            session("email", form.email);
            return redirect(routes.Application.index());
        }
        else{
            System.out.println("Login ERROR!");
            return badRequest(login.render(loginForm));
        }

        /*User user = Form.form(User.class).bindFromRequest().get();
        List<User> allUsers = new Model.Finder(String.class, User.class).all();
        for(User u : allUsers){
            if((u.getUsername() == user.getUsername()) || (u.getPassword() == user.getPassword())){
                return ok(index.render("Wasgood."));
            }
        }
*/

    }

    public static class LoginForm {

        public String email;
        public String password;

    }
}
