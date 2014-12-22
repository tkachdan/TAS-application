package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import src.persistence.dao.PoiDAO;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.models.Poi;
import src.service.PoiService;
import src.service.impl.PoiServiceImpl;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    //TODO: can be put id to checkbox for future proceeding
    public static Result renderIndexLogined() {
        if (session().isEmpty()) {
            return controllers.Login.renderLogin();
        }
        StringBuffer buf = new StringBuffer();
        String str;
        if (session().isEmpty()) {
            return controllers.Application.index();
        } else {
            PoiService poiService = new PoiServiceImpl();
            List<Poi> poiList = poiService.getAllPois();
            int checkboxId = 1;
            for (Poi p : poiList) {
                // str += "<tr>";
                buf.append("<tr>");
                //str += " <td> <input type=\"checkbox\" id=" + checkboxId + " name=\"" + checkboxId + "\" value=\"checked\" /> </td>";
                buf.append(" <td> <input type=\"checkbox\" id=\" + checkboxId + \" name=\\\"\" + checkboxId + \"\" value=\"checked\" /> </td>");
                // str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                //        + p.getCost() + "</td>";
                buf.append("<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                        + p.getCost() + "</td>");
                //str += "</tr>";
                buf.append("</tr>");
                checkboxId++;
            }
            str = buf.toString();
            return ok(indexLogined.render(str));
        }
    }

    public static Result renderTrip() {
        if (session().isEmpty()) {
            return ok(login.render(form(Login.LoginForm.class)));
        }
        Map<String, String> data = Form.form().bindFromRequest().data();
        PoiDAO poiDAO = new PoiDAOImpl();

        List<Poi> poiList = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            //TODO: Redo with Service @Krasotin
            int id = Integer.parseInt(entry.getKey());
            Poi poi = poiDAO.getPoi(Integer.parseInt(entry.getKey()));
            poiList.add(poi);
            Logger.info(poi.getName());
        }
        String str;
        StringBuffer buf = new StringBuffer();
        List<Coordinates> coordList = new ArrayList<>();
        for (Poi p : poiList) {
            Coordinates c = new Coordinates();
            c.lat = p.getLatitude();
            c.lon = p.getLongtitude();

            coordList.add(c);

            //str += "<tr>";
            buf.append("<tr>");
            //str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
            //        + p.getCost() + "</td>";
            buf.append("<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                    + p.getCost() + "</td>");
            //str += "</tr>";
            buf.append("</tr>");
        }
        str = buf.toString();

        JsonNode json = Json.toJson(coordList);
        return ok(trip.render(str, json));
    }

    public static Result renderAdd() {
        return ok(add.render());
    }

    public static Result renderEdit() {
        return ok(edit.render());
    }

    public static class Coordinates {

        double lat;
        double lon;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

}
