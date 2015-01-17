package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.Trip;
import src.persistence.models.User;
import src.service.impl.PoiServiceImpl;
import src.service.impl.TripServiceImpl;
import views.html.editTrip;

import java.util.Set;

import static play.data.Form.form;

/**
 * Created by Krasotin on 17.01.15.
 */
public class TripController extends Controller {
    private static TripDAOImpl tripDAO = new TripDAOImpl();
    private static UserDAOImpl userDAO = new UserDAOImpl();
    private static PoiServiceImpl poiService = new PoiServiceImpl();
    private static TripServiceImpl tripService = new TripServiceImpl();
    private static TripForm tripForm = new TripForm();

    public static Result renderEditTrip() {
        return ok(editTrip.render(form(TripForm.class)));
    }

    public static TripForm getTripForm() {
        return tripForm;
    }

    public static Result editTrip() {
        if (session().isEmpty()) {
            return controllers.Login.renderLogin();
        }

        if (!session().get("designation").equals("Administrator")) {
            return Application.renderIndexLogined();
        }

        Form<TripForm> form = form(TripForm.class).bindFromRequest();
        tripForm = form.get();

        if (tripForm.button.equals("delete")) {
            System.out.println("Delete action");
            if (tripForm.id > 0) {
                return ActionDel(tripForm);
            } else {
                return badRequest(editTrip.render(form(TripForm.class)));
            }
        } else {
            Trip trip;
            if ((trip = tripService.getTripById(tripForm.id)) != null) {
                if (tripForm.button.equals("get")) {
                    tripForm.id = trip.getId();
                    tripForm.name = trip.getName();
                    return ok(editTrip.render(form(TripForm.class)));
                }
                if (tripForm.button.equals("edit")) {
                    System.out.println("******** EDIT *********");
                    System.out.println(tripForm.name);
                    System.out.println("******** EDIT *********");

                    trip.setName(tripForm.name);
                    tripDAO.updateTrip(trip);

                    return ok(editTrip.render(form(TripForm.class)));
                } else {
                    return badRequest(editTrip.render(form(TripForm.class)));
                }
            }
            return badRequest(editTrip.render(form(TripForm.class)));
        }
    }

    private static Result ActionDel(TripForm form) {
        int id = form.id;
        Trip trip = tripService.getTripById(id);
        //tripDAO.deleteTrip(id);

        String mail = session().get("email");
        User user = userDAO.getUserByEmail(session().get("email"));

        Set<Trip> userTrips = user.getTrips();
        Trip tripToDelete = new Trip();
        for (Trip userTrip : userTrips)
            if (userTrip.getId() == id)
                tripToDelete = userTrip;

        userTrips.remove(tripToDelete);
        user.setTrips(userTrips);
        userDAO.updateUser(user);
        
        tripDAO.deleteTrip(id);
        return ok(editTrip.render(form(TripForm.class)));
    }

    public static class TripForm {
        public int id;
        public String name;
        public String button;

        public TripForm() {
            this.name = "";
            this.id = 0;
        }

        public void clean() {
            this.name = "";
            this.id = 0;
        }
    }
}
