package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.PoiType;
import src.persistence.models.Trip;
import src.persistence.models.User;
import src.service.PoiService;
import src.service.impl.PoiServiceImpl;
import src.service.impl.TripServiceImpl;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static play.data.Form.form;

public class Application extends Controller {
    private static PoiDAOImpl poiDAO = new PoiDAOImpl();
    private static UserDAOImpl userDAO = new UserDAOImpl();
    private static TripDAOImpl tripDAO = new TripDAOImpl();
    private static TripServiceImpl tripService = new TripServiceImpl();

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    //TODO: can be put id to checkbox for future proceeding
    public static Result renderIndexLogined() {
        if (session().isEmpty()) {
            return controllers.Login.renderLogin();
        }

        String str = "";
        if (session().isEmpty()) {
            return controllers.Application.index();
        } else {
            Map<String, String> data = Form.form().bindFromRequest().data();
            String type = null;
            for (Map.Entry<String, String> entry : data.entrySet()) {
                type = entry.getValue();
            }

            PoiService poiService = new PoiServiceImpl();
            List<Poi> poiList;
            if (type != null) {
                PoiType poiType = PoiType.valueOf(type);
                poiList = poiService.getAllPoisByType(poiType);
            } else {
                poiList = poiService.getAllPois();
            }

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
    }

    public static Result renderTrip() {
        if (session().isEmpty()) {
            return ok(login.render(form(Login.LoginForm.class)));
        }

        //get user
        String userEmail = session().get("email");
        User user = userDAO.getUserByEmail(userEmail);
        System.out.println(userEmail);
        System.out.println(user);

        //pois id for parsing at cart
        String poisString = "";

        Map<String, String> data = Form.form().bindFromRequest().data();
        List<Poi> poiList = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            int id = Integer.parseInt(entry.getKey());
            Poi poi = poiDAO.getPoi(Integer.parseInt(entry.getKey()));
            poiList.add(poi);
            poisString += poi.getId() + "-";
            Logger.info(poi.getName());
        }
        if (poisString.length() > 1)
            poisString = poisString.substring(0, poisString.length() - 1);

        String str = "";
        List<Coordinates> coordList = new ArrayList<>();

        for (Poi p : poiList) {
            Coordinates c = new Coordinates();
            c.lat = p.getLatitude();
            c.lon = p.getLongtitude();

            coordList.add(c);

            str += "<tr>";
            str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                    + p.getCost() + "</td>";
            str += "</tr>";
        }

        JsonNode json = Json.toJson(coordList);
        return ok(trip.render(str, json, poisString));
    }

    public static Result renderCart() {
        if (session().isEmpty()) {
            return ok(login.render(form(Login.LoginForm.class)));
        }

        /*System.out.println("Get POIs from GET request");
        Map<String, String> data = Form.form().bindFromRequest().data();
        String poisString = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            poisString = entry.getValue();
        }

        String[] poisIdString = poisString.split("-");
        int[] poisIdInt = new int[poisIdString.length];
        Set<Poi> newTripPOIS = new HashSet<>();

        for (int i = 0; i < poisIdString.length; i++) {
            System.out.println(poisIdString[i]);
            poisIdInt[i] = Integer.valueOf(poisIdString[i]);
            newTripPOIS.add(poiDAO.getPoi(poisIdInt[i]));
            System.out.println("int POI " + poisIdInt[i]);
        }
        System.out.println("POIs set " + newTripPOIS);
        System.out.println("============");
        */
        //get user data
        System.out.println("Get user data");
        String userEmail = session().get("email");
        User user = userDAO.getUserByEmail(userEmail);
        System.out.println("user mail " + userEmail);
        System.out.println("user " + user);
        System.out.println("============");
        /*
        //create trip from poi
        Trip newTrip = new Trip();
        newTrip.setCost(0);
        newTrip.setTripStatus(TripStatus.NOTPAID);
        for (Poi poi : newTripPOIS) {
            newTrip.addPoi(poi);
            newTrip.setCost(newTrip.getCost() + poi.getCost());
        }
        System.out.println(newTrip);
        System.out.println("============");

        //assign trip to user
        tripDAO.saveTrip(newTrip);*/

        Map<String, String> data = Form.form().bindFromRequest().data();
        String poisString = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            poisString = entry.getValue();
        }
        Trip newTrip = tripService.createTripFromPoisString(poisString);

        user.addTrip(newTrip);
        userDAO.updateUser(user);
        System.out.println(user);
        System.out.println("============");

        String poiStrTable = "";
        List<Poi> poiList = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            poiStrTable = entry.getValue();
        }
        System.out.println(poiStrTable);


        Set<Trip> userTrips = user.getTrips();

        String str = "";
        for (Trip trip : userTrips) {
            Set<Poi> POIsInTrip = trip.getPois();
            str += "<tr>";
            str += "<td>" + trip.getId() + "</td>" + "<td>";
            for (Poi poi : POIsInTrip)
                str += "id: " + poi.getId() + " name: " + poi.getName() + "<br>";
            //str += "<td>" + trip.getPois().toString() + "</td>";
            str += "</td>" + "<td>" + trip.getCost() + "</td>" + "<td>" + trip.getTripStatus() + "</td>";
            //str += " <td> <input type=\"checkbox\" id=" + checkboxId + " name=\"" + checkboxId + "\" value=\"checked\" /> </td>";
            /*str += "<td><form action=\"pay\"> <input type=\"submit\"id=" + trip.getId() +  "value=\"Pay\"></td>";*/
            str += "<td> <a href=\"/pay_trip?id=" + trip.getId() + " \"   >pay for trip id = " + trip.getId() + "</a></td>";
            str += " <td> <input type=\"checkbox\" id=" + trip.getId() + " name=ToDel\"" + trip.getId() + "\" value=\"checked\" /> </td>";


            str += "</tr>";
        }


        return ok(cart.render(str));
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
