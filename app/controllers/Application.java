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
import src.persistence.models.*;
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

        Map<String, String> data = Form.form().bindFromRequest().data();
        String poisString = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            poisString = entry.getValue();
        }

        User user = userDAO.getUserByEmail(session().get("email"));

        if (poisString.length() > 0) {
            Trip newTrip = tripService.createTripFromPoisString(poisString);
            user.addTrip(newTrip);
            userDAO.updateUser(user);
            System.out.println("updated usesr " + user);
            System.out.println("============");
        }

        Set<Trip> userTrips = user.getTrips();
        String str = "";
        for (Trip trip : userTrips) {
            Set<Poi> POIsInTrip = trip.getPois();
            str += "<tr>";
            str += "<td>" + trip.getId() + "</td>" + "<td>";
            for (Poi poi : POIsInTrip)
                str += "id: " + poi.getId() + " name: " + poi.getName() + "<br>";
            str += "</td>" + "<td>" + trip.getCost() + "</td>" + "<td>" + trip.getTripStatus() + "</td>";
            if (trip.getTripStatus() == TripStatus.PAID) {
                str += "<td>You've already paid for this trip!</td>";
            } else {
                str += "<td> <a href=\"/payTrip?id=" + trip.getId() + " \"   >Pay for trip</a></td>";
            }
            str += " <td> <input type=\"checkbox\" id=" + trip.getId() + " name=ToDel\"" + trip.getId() + "\" value=\"checked\" /> </td>";


            str += "</tr>";
        }

        return ok(cart.render(str));
    }

    public static Result renderPayment() {
        if (session().isEmpty()) {
            return ok(login.render(form(Login.LoginForm.class)));
        }

        String str = "";
        Map<String, String> data = Form.form().bindFromRequest().data();
        String tripIdString = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            tripIdString = entry.getValue();
        }
        Trip trip = tripDAO.getTrip(Integer.valueOf(tripIdString));

        int i = 1;
        for (Poi poi : trip.getPois()) {
            str += "<p>" + i + ". POI — " + poi.getName() + "</p>";
            i++;
        }

        str += "<h4>Total cost: <i>" + trip.getCost() + "</i></h4>";

        return ok(payTrip.render(str, String.valueOf(trip.getId())));
    }

    public static Result renderPaymentIsOk() {
        if (session().isEmpty()) {
            return ok(login.render(form(Login.LoginForm.class)));
        }

        Map<String, String> data = Form.form().bindFromRequest().data();
        String tripIdString = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            tripIdString = entry.getValue();
        }
        Trip trip = tripDAO.getTrip(Integer.valueOf(tripIdString));
        System.out.println("before - " + trip);
        trip.setTripStatus(TripStatus.PAID);
        tripDAO.updateTrip(trip);
        System.out.println("after - " + trip);

        String str = "";
        return ok(paymentIsOk.render(str));
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
