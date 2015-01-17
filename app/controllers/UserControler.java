package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import src.persistence.dao.UserDAO;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.User;
import views.html.user;

import static play.data.Form.form;

/**
 * Created by Pauer on 12. 12. 2014.
 */
public class UserControler extends Controller {

    private static String err = "";
    public static UserForm mem = new UserForm();

    /**
     * rendering page for user editing
     *
     * @return edit user page
     */
    public static Result renderUser() {
        if (session().isEmpty()) {
            return controllers.Login.renderLogin();
        }
        if (!session().get("designation").equals("Administrator")) {
            return Application.renderIndexLogined();
        }
        err = "";
        return ok(user.render(form(UserForm.class)));

    }

    /**
     * based on which button the admin has pressed he can add/delete/edit users in database
     *
     * @return edit user page
     */
    public static Result editUser() {
        if (session().isEmpty()) {
            return controllers.Login.renderLogin();
        }
        if (!session().get("designation").equals("Administrator")) {
            return Application.renderIndexLogined();
        }
        Form<UserForm> userForm = form(UserForm.class).bindFromRequest();
        UserForm form = userForm.get();
        mem.clean();
        err = "";
        if (form.but.equals("Add")) {
            if (form.email.isEmpty() || form.password.isEmpty()) {
                err = "Musíte zadat Email a heslo!";
                mem = form;
                return badRequest(user.render(form(UserForm.class)));
            } else {
                return ActionAdd(form);
            }
        }
        if (form.but.equals("Del")) {
            if (!form.id.isEmpty()) {
                return ActionDel(form);
            } else {
                err = "Musíte zadat Id!";
                mem = form;
                return badRequest(user.render(form(UserForm.class)));
            }
        }
        if (form.but.equals("Upd")) {
            if (form.email.isEmpty() || form.password.isEmpty() || form.id.isEmpty()) {
                err = "Musíte zadat Id, Email a heslo!";
                mem = form;
                return badRequest(user.render(form(UserForm.class)));
            } else {
                return ActionUpd(form);
            }
        }
        if (form.but.equals("Get")) {
            if (!form.id.isEmpty()) {
                return ActionGet(form);
            } else {
                err = "Musíte zadat Id!";
                mem = form;
                return badRequest(user.render(form(UserForm.class)));
            }
        }
        return ok(user.render(form(UserForm.class)));
    }

    /*
    * adding new user to the database
    * */
    private static Result ActionAdd(UserForm form) {
        UserDAO dao = new UserDAOImpl();
        User us = new User(form.designation, form.email, form.firstname, form.lastname, form.password, form.username);
        dao.saveUser(us);
        mem.designation = form.designation;
        mem.email = form.email;
        mem.firstname = form.firstname;
        mem.lastname = form.lastname;
        mem.username = form.username;
        mem.password = form.password;
        mem.id = Integer.toString(dao.getUserByEmail(form.email).getUserId());
        err = "Uživatel přidán";
        return ok(user.render(form(UserForm.class)));
    }

    /*
    * changing users attributes
    * */
    private static Result ActionUpd(UserForm form) {
        int id = Integer.parseInt(form.id);
        UserDAO dao = new UserDAOImpl();
        User nuser = dao.getUser(id);
        if (nuser == null) {
            err = "Uživatel neexistuje!";
            mem = form;
            return badRequest(user.render(form(UserForm.class)));
        } else {
            nuser.setDesignation(form.designation);
            nuser.setEmail(form.email);
            nuser.setFirstName(form.firstname);
            nuser.setLastName(form.lastname);
            nuser.setPassword(form.password);
            nuser.setUsername(form.username);
            dao.updateUser(nuser);
            mem.designation = form.designation;
            mem.email = form.email;
            mem.firstname = form.firstname;
            mem.lastname = form.lastname;
            mem.username = form.username;
            mem.password = form.password;
            mem.id = form.id;
            err = "Uživatel upraven";
            return ok(user.render(form(UserForm.class)));
        }
    }

    /*
    * pull the user from the database and fill the textfields with his attributes
    * */
    private static Result ActionGet(UserForm form) {
        int id = Integer.parseInt(form.id);
        UserDAO dao = new UserDAOImpl();
        User nuser = dao.getUser(id);
        if (nuser == null) {
            err = "Uživatel neexistuje!";
            mem = form;
            return badRequest(user.render(form(UserForm.class)));
        } else {
            mem.designation = nuser.getDesignation();
            mem.email = nuser.getEmail();
            mem.firstname = nuser.getFirstName();
            mem.lastname = nuser.getLastName();
            mem.username = nuser.getUsername();
            mem.password = nuser.getPassword();
            mem.id = form.id;
            return ok(user.render(form(UserForm.class)));
        }
    }

    /**
     * deleting userr from database based on his ID
     *
     * @param form form from page
     * @return user edit page
     */
    private static Result ActionDel(UserForm form) {
        int id = Integer.parseInt(form.id);
        UserDAO dao = new UserDAOImpl();
        User nuser = dao.getUser(id);
        if (nuser == null) {
            err = "Uživatel neexistuje!";
            mem = form;
            return badRequest(user.render(form(UserForm.class)));
        } else {
            dao.deleteUser(id);
            err = "Uživatel smazán!";
            return ok(user.render(form(UserForm.class)));
        }
    }

    public static class UserForm {

        public String id;
        public String email;
        public String password;
        public String username;
        public String firstname;
        public String lastname;
        public String designation;
        public String but;

        public UserForm() {
            this.designation = "Administrator";
            this.email = "";
            this.firstname = "";
            this.lastname = "";
            this.password = "";
            this.username = "";
            this.id = "";
        }

        public void clean() {
            this.designation = "Administrator";
            this.email = "";
            this.firstname = "";
            this.lastname = "";
            this.password = "";
            this.username = "";
            this.id = "";
        }

        public String getSelDesignation(String option) {
            if (option.equals(this.designation)) {
                return "selected=\"\"";
            } else {
                return "";
            }
        }
    }

    /**
     * error string
     *
     * @return
     */
    public static String getButErr() {
        if (err.isEmpty()) {
            return "none";
        } else {
            return "block";
        }
    }

    public static String getErr() {
        return err;
    }

    public static UserForm getUF() {
        return mem;
    }

}
