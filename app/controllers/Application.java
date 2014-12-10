package controllers;

import persistence.models.Bar;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.add;
import views.html.edit;
import views.html.index;
import views.html.viewAll;

import java.util.List;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result renderAdd() {
        return ok(add.render());
    }

    public static Result renderEdit() {
        return ok(edit.render());
    }



    public static Result addBar() {

        Bar bar = Form.form(Bar.class).bindFromRequest().get();
        bar.save();
        //System.out.println("NOOO");
        return redirect(routes.Application.index());
    }

    public static Result getBars() {
        //first param String.class -> primary key
        List<Bar> bars = new Model.Finder(String.class, Bar.class).all();
        java.util.Collections.sort(bars);
        String str = "";

        for (Bar b : bars) {
            str += "<tr>";
            str += "<td>" + b.id + "</td> <td>" + b.name + "</td>";
            str += "</tr>";

        }
        return ok(viewAll.render(str));


    }



    public static Result deleteAll(){
        List<Bar> bars = new Model.Finder(String.class, Bar.class).all();
        for(Bar b : bars) {
            b.delete();
        }

        return redirect(routes.Application.index());
    }


    public static Result editBar() {
        System.out.println("YEE");
        Bar bar = Form.form(Bar.class).bindFromRequest().get();

        System.out.println(bar.id + " " + bar.name);

        Bar.update(bar.id, bar);


        return redirect(routes.Application.index());


    }

}
