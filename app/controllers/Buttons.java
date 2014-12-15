package controllers;

import play.mvc.Controller;

/**
 * Created by Pauer on 11. 12. 2014.
 */
public class Buttons extends Controller{

    public static String loginButton(){
        if(session().isDirty){
            return "none";
        }else{
            return "block";
        }
    }

    public static String logoutButton(){
        if(session().isDirty){
            return "block";
        }else{
            return "none";
        }
    }

    public static String adminButton(){
        if(session().get("designation").equals("Administrator")){
            return "block";
        }else{
            return "none";
        }
    }

    public static String redirect(){
        if(!session().isEmpty()){
            return "";
        }else{
            return "refresh";
        }
    }

    public static String redirectAdmin(){
        if(session()==null || session().get("designation").equals("Administrator")){
            return "";
        }else{
            return "refresh";
        }
    }

    public static String email(){
        return session().get("email");
    }
}
