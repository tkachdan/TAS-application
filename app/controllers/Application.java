package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.*;
import src.service.PoiService;
import src.service.impl.PoiServiceImpl;
import src.service.impl.TripServiceImpl;
import src.service.impl.UserServiceImpl;
import views.html.*;

import java.util.*;

import static play.data.Form.form;

public class Application extends Controller {

    private static PoiServiceImpl poiService = new PoiServiceImpl();
    private static UserServiceImpl userService = new UserServiceImpl();
    private static UserDAOImpl userDAO = new UserDAOImpl();
    private static TripDAOImpl tripDAO = new TripDAOImpl();
    private static TripServiceImpl tripService = new TripServiceImpl();

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
            str += " <td> <input type=\"checkbox\" id=" + checkboxId + " name=\"" + p.getId() + "\" value=\"checked\" /> </td>";
            str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                    + p.getCost() + "</td>";
            str += "</tr>";
            checkboxId++;
        }
        return ok(indexLogined.render(str));
    }

    private static Result returnDefaultIndexLogined() {

        List<Poi> poiList = new ArrayList<>();
        poiList.addAll(poiService.getAllPois());
        String str = "";
        int checkboxId = 1;
        for (Poi p : poiList) {
            str += "<tr>";
            str += " <td> <input type=\"checkbox\" id=" + checkboxId + " name=\"" + p.getId() + "\" value=\"checked\" /> </td>";
            str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                    + p.getCost() + "</td>";
            str += "</tr>";
            checkboxId++;
        }
        return ok(indexLogined.render(str));
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
        int poiNumber = 0;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            Poi poi = poiService.getPoiById(Integer.parseInt(entry.getKey()));
            poiList.add(poi);
            poisString += poi.getId() + "-";
            Logger.info(poi.getName());
            poiNumber++;
        }

        if (poiNumber < 2) {
            return returnDefaultIndexLogined();
        }
        if (poisString.length() > 1)
            poisString = poisString.substring(0, poisString.length() - 1);

        String str = "";
        List<Coordinates> coordList = new ArrayList<>();
        int checkBoxId = 1;

        List<String> trip_array = new ArrayList<>();
        String string_trip = "";
        for (Poi p : poiList) {
            Coordinates c = new Coordinates();
            c.lat = p.getLatitude();
            c.lon = p.getLongtitude();

            coordList.add(c);

            str += "<tr>";
            str += " <td> <input type=\"checkbox\" id=" + checkBoxId + " name=\"" + p.getId() + "\" value=\"checked\" /> </td>";
            str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                    + p.getCost() + "</td>";
            str += "</tr>";

            checkBoxId++;

            trip_array.add(p.getName());
        }

        string_trip = String.join(" - ", trip_array);

        JsonNode json = Json.toJson(coordList);
        return ok(trip.render(str, json, poisString, "", string_trip));
    }

    public static Result renderCart() throws Exception {
        if (session().isEmpty()) {
            return ok(login.render(form(Login.LoginForm.class)));
        }

        Map<String, String> data = Form.form().bindFromRequest().data();
        String poisString = "";
        LinkedList<String> poisToDelete = new LinkedList<>();
        String poiId = "";
        boolean edit = false;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            System.out.println(entry);
            if (entry.getKey().equals("button")) {
                if (entry.getValue().equals("remove")) {
                    edit = true;
                }
            }
            poisString = entry.getValue();
            if (entry.getValue().equals("checked")) {
                poiId += entry.getKey();
                poiId += "-";
                poisToDelete.add(poiId);
                poiId = "";
            }
        }

        if (edit) {

            List<Coordinates> coordList = new ArrayList<>();
            String str = "";
            int checkBoxId = 1;

            String deletedPois = "";
            for (String s : poisToDelete) {
                System.out.println(s);
                deletedPois += s;
            }

            System.out.println("poiStr : \"" + poisString + "\"");

            for (String s : poisToDelete) {
                System.out.println("replacing with: " + s);
                poisString = poisString.replace(s, "");
            }
            System.out.println("poiStr : \"" + poisString + "\"");

            if (deletedPois.equals(poisString) || poisString == "" || deletedPois.isEmpty()) {

                for (Poi p : poiService.getPoisFromString(poisString)) {
                    Coordinates c = new Coordinates();
                    c.lat = p.getLatitude();
                    c.lon = p.getLongtitude();

                    coordList.add(c);

                    str += "<tr>";
                    str += " <td> <input type=\"checkbox\" id=" + checkBoxId + " name=\"" + p.getId() + "\" value=\"checked\" /> </td>";
                    str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                            + p.getCost() + "</td>";
                    str += "</tr>";
                    checkBoxId++;
                }

                JsonNode json = Json.toJson(coordList);
                return badRequest(trip.render(str, json, poisString, "", ""));
            }
            if (!poisToDelete.isEmpty()) {
                String a = poisToDelete.getLast().substring(0, poisToDelete.getLast().length() - 1);
                a += "-";
                poisToDelete.removeLast();
                poisToDelete.add(a);
            }

            for (Poi p : poiService.getPoisFromString(poisString)) {
                Coordinates c = new Coordinates();
                c.lat = p.getLatitude();
                c.lon = p.getLongtitude();

                coordList.add(c);

                str += "<tr>";
                str += " <td> <input type=\"checkbox\" id=" + checkBoxId + " name=\"" + p.getId() + "\" value=\"checked\" /> </td>";
                str += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                        + p.getCost() + "</td>";
                str += "</tr>";
                checkBoxId++;
            }

            JsonNode json = Json.toJson(coordList);

            String str2 = "";
            for (Poi p : poiService.getPoisFromString(deletedPois)) {
                str2 += "<tr>";
                str2 += " <td> <input type=\"checkbox\" id=" + checkBoxId + " name=\"" + p.getId() + "\" value=\"checked\" /> </td>";
                str2 += "<td>" + p.getId() + "</td> <td>" + p.getName() + "</td> <td>" + p.getType() + "</td> <td>"
                        + p.getCost() + "</td>";
                str2 += "</tr>";
                checkBoxId++;
            }

            return ok(trip.render(str, json, poisString, str2, ""));
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

            String poisChecked = "";
            for (Poi poi : POIsInTrip) {
                str += "id: " + poi.getId() + " name: " + poi.getName() + "<br>";
                poisChecked += poi.getId() + "=checked&";
            }
            str += "</td>" + "<td>" + trip.getCost() + "</td>" + "<td>" + trip.getTripStatus() + "</td>";
            if (trip.getTripStatus() == TripStatus.PAID || trip.getCost() == 0) {
                String pathToPdf = String.valueOf(trip.getId()) + ".pdf";
                str += "<td><a href=\"http://localhost:8888/TAS/" + pathToPdf + "\">Get trip overview</a></td>";
            } else {
                str += "<td> <a href=\"/payTrip?id=" + trip.getId() + " \"   >Pay for trip</a></td>";
            }
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
        Trip trip = tripService.getTripById(Integer.valueOf(tripIdString));

        int i = 1;
        for (Poi poi : trip.getPois()) {
            str += "<p>" + i + ". POI — " + poi.getName() + "</p>";
            i++;
        }

        str += "<p>Total cost: <i>" + trip.getCost() + "</i></p>";

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
        Trip trip = tripService.getTripById(Integer.valueOf(tripIdString));
        System.out.println("before - " + trip);
        trip.setTripStatus(TripStatus.PAID);
        tripDAO.updateTrip(trip);
        System.out.println("after - " + trip);

        String str = "";
        return ok(paymentIsOk.render(str));
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
