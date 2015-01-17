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
import src.persistence.models.PoiType;
import src.service.PoiService;
import src.service.impl.PoiServiceImpl;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

public class Application extends Controller {

    /**
     * rendering index page
     *
     * @return index page
     */
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    /**
     * rendering main page
     *
     * @return main page
     */
    public static Result renderIndexLogined() {
        System.out.println("!! INDEX LOGINED !!");

        String str = "";
        if (session().isEmpty()) {
            return controllers.Application.index();
        }
        Map<String, String> data = Form.form().bindFromRequest().data();

        List<PoiType> types = new ArrayList<>();
        int minimalAge = 0;
        int maximalCost = 0;



        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getKey().toLowerCase().equals("age")) {
                minimalAge = Integer.parseInt(entry.getValue());
            }
            if (entry.getKey().toLowerCase().equals("cost")) {
                maximalCost = Integer.parseInt(entry.getValue());
            }
            switch (entry.getKey()) {
                case "HISTORY":
                    types.add(PoiType.HISTORY);
                    break;
                case "CULTURE":
                    types.add(PoiType.CULTURE);
                    break;
                case "EDUCATION":
                    types.add(PoiType.EDUCATION);
                    break;
                case "ART":
                    types.add(PoiType.ART);
                    break;
            }
        }
        PoiService poiService = new PoiServiceImpl();

        List<Poi> poiList = new ArrayList<>();
        List<Poi> toDelete = new ArrayList<>();
        if (types.isEmpty()) {
            poiList = poiService.getAllPois();
        } else {
            poiList.addAll(poiService.getAllPoisByTypes(types));
        }

        for (Poi poi : poiList) {
            if (poi.getMinimalAge() < minimalAge || poi.getCost() > maximalCost) {
                toDelete.add(poi);
            }
        }
        poiList.removeAll(toDelete);

        int checkboxId = 1;
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

    public static Result renderEditGenratedTrip() {

        System.out.println("!! GENERATED !!");
//        if (session().isEmpty()) {
//            System.out.println("HERE");
//
//            return ok(login.render(form(Login.LoginForm.class)));
//        }
//        System.out.println("HERE");
        String str = "";
//        Map<String, String> data = Form.form().bindFromRequest().data();
//        if (data.entrySet().isEmpty()) {
//            System.out.println("!!!! EMPTY !!!");
//        }
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//            System.out.println(entry.toString());
//        }
//
//
//        // JsonNode json = Json.toJson(coordList);
//        //   return ok(trip.render(str, json));
        List<Coordinates> coordList = new ArrayList<>();
        Coordinates c = new Coordinates();
        c.lat = 50;
        c.lon = 14;
        coordList.add(c);
        JsonNode json = Json.toJson(coordList);
        return ok(trip.render(str, json));
    }

    /**
     * rendering google map with users path between POIs and with detailed description of the path
     *
     * @return page with list of choosen pois , google map with path description
     */
    public static Result renderTrip() {
        if (session().isEmpty()) {
            return ok(login.render(form(Login.LoginForm.class)));
        }
        System.out.println("!! RENDER TRIP !!");
        Map<String, String> data = Form.form().bindFromRequest().data();
        PoiDAO poiDAO = new PoiDAOImpl();

        List<Poi> poiList = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            System.out.println(entry.toString());

            int id = Integer.parseInt(entry.getKey());
            Poi poi = poiDAO.getPoi(Integer.parseInt(entry.getKey()));
            poiList.add(poi);
            Logger.info(poi.getName());
        }

        String str = "";
        int checkboxId = 1;

        List<Coordinates> coordList = new ArrayList<>();
        for (Poi p : poiList) {
            Coordinates c = new Coordinates();
            c.lat = p.getLatitude();
            c.lon = p.getLongtitude();

            coordList.add(c);

            str += "<tr>";
            str += " <td> <input type=\"checkbox\" id=" + checkboxId + " name=\"" + checkboxId + "\" value=\"checked\" /> </td>";
            str += "<td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                    + p.getCost() + "</td>";
            str += "</tr>";
        }
        JsonNode json = Json.toJson(coordList);
        return ok(trip.render(str, json));
    }

    /**
     * @return page with advertisement
     */
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
