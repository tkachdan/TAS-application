package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import src.persistence.models.Poi;
import src.service.PoiService;
import src.service.impl.PoiServiceImpl;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    //TODO: can be put id to checkbox for future proceeding
    public static Result renderIndexLogined() {
        String str = "";
        if (session().isEmpty()) {
            return ok(index.render("index"));
        } else {
            PoiService poiService = new PoiServiceImpl();
            List<Poi> poiList = poiService.getAllPois();
            int checkboxId = 0;
            for (Poi p : poiList) {
                str += "<tr>";
                str += " <td> <input type=\"checkbox\" id=" + checkboxId + " name=\"" + checkboxId + "\" value=\"checked\" /> </td>";
                str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                        + p.getCost() + "</td>";
                str += "</tr>";
                checkboxId++;
            }
            return ok(indexLogined.render(str));
        }
    }

    public static Result renderTrip() {
        Map<String, String> data = Form.form().bindFromRequest().data();
        List<Poi> pois = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {

            System.out.println(entry.getKey() + "/" + entry.getValue());

        }

        /*String str = Form.form().bindFromRequest().get("0");
        System.out.println("DATA:" + str);*/
        return ok(trip.render("trip"));
    }

    public static Result renderAdd() {
        return ok(add.render());
    }

    public static Result renderEdit() {
        return ok(edit.render());
    }



}
