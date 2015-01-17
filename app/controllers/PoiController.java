package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import src.persistence.models.Poi;
import src.persistence.models.PoiType;
import src.service.PoiService;
import src.service.impl.PoiServiceImpl;
import views.html.createPoi;
import views.html.editPoi;

import java.sql.Time;

import static play.data.Form.form;

/**
 * Created by tkachdan on 18-Dec-14.
 */
public class PoiController extends Controller {

    private static PoiService poiService = new PoiServiceImpl();
    private static POIForm poiForm = new POIForm();

    public static Result renderEditPoi() {
        return ok(editPoi.render(form(POIForm.class)));
    }

    public static Result renderCreatePoi() {
        return ok(createPoi.render(form(POIForm.class)));
    }

    public static POIForm getPoiForm() {
        return poiForm;
    }

    public static Result editPoi() {

        if (session().isEmpty()) {
            return controllers.Login.renderLogin();
        }

        if (!session().get("designation").equals("Administrator")) {
            return Application.renderIndexLogined();
        }

        Form<POIForm> form = form(POIForm.class).bindFromRequest();
        poiForm = form.get();
        Poi poi;
        if ((poi = poiService.getPoiById(poiForm.id)) != null) {
            if (poiForm.button.equals("get")) {
                poiForm.accesibility = poi.getAccesibility();
                poiForm.cost = poi.getCost();
                poiForm.id = poi.getId();
                poiForm.latitude = poi.getLatitude();
                poiForm.longtitude = poi.getLongtitude();
                poiForm.minimalAge = poi.getMinimalAge();
                poiForm.name = poi.getName();
                poiForm.rating = poi.getRating();
                poiForm.requiredTime = poi.getRequiredTime();
                poiForm.type = poi.getType();
                return ok(editPoi.render(form(POIForm.class)));
            }
            if (poiForm.button.equals("edit")) {

                System.out.println("******** EDIT *********");
                System.out.println(poiForm.accesibility);
                System.out.println(poiForm.cost);
                System.out.println(poiForm.latitude);
                System.out.println(poiForm.longtitude);
                System.out.println(poiForm.name);
                System.out.println(poiForm.rating);
                //      System.out.println(poiForm.requiredTime);
                System.out.println(poiForm.type);
                System.out.println(poiForm.minimalAge);
                System.out.println("******** EDIT *********");

                poi.setAccesibility(poiForm.accesibility);
                poi.setCost(poiForm.cost);
                poi.setId(poiForm.id);
                poi.setLatitude(poiForm.latitude);
                poi.setLongtitude(poiForm.longtitude);
                poi.setMinimalAge(poiForm.minimalAge);
                poi.setName(poiForm.name);
                poi.setType(poiForm.type);
                poiService.upadtePoi(poi);

                return ok(editPoi.render(form(POIForm.class)));
            } else {
                return badRequest(editPoi.render(form(POIForm.class)));
            }
        }
        return badRequest(editPoi.render(form(POIForm.class)));
    }

    public static Result createPoi() {

        if (session().isEmpty()) {
            return controllers.Login.renderLogin();
        }
        if (!session().get("designation").equals("Administrator")) {
            return Application.renderIndexLogined();
        }

        Form<POIForm> form = form(POIForm.class).bindFromRequest();
        poiForm = form.get();

        if (poiForm.name == null
                || poiForm.cost < 0
                || poiForm.latitude < 0
                || poiForm.longtitude < 0
                || poiForm.minimalAge < 0) {
            return badRequest(createPoi.render(form(POIForm.class)));
        }
        System.out.println("******** CREATE *********");
        System.out.println("ACCESS : " + poiForm.accesibility);
        System.out.println("COST : " + poiForm.cost);
        System.out.println("LAT : " + poiForm.latitude);
        System.out.println("LON : " + poiForm.longtitude);
        System.out.println("NAME : " + poiForm.name);
        System.out.println("RATING : " + poiForm.rating);
        //      System.out.println(poiForm.requiredTime);
        System.out.println("TYPE : " + poiForm.type);
        System.out.println("MIN AGE : " + poiForm.minimalAge);
        System.out.println("******** CREATE *********");
        Poi poi = new Poi(poiForm.accesibility, poiForm.minimalAge, poiForm.name, poiForm.rating, new Time(System.currentTimeMillis()), poiForm.type, poiForm.cost, poiForm.latitude, poiForm.longtitude);
        poiService.addPoi(poi);
        System.out.println(poi.getName());
        return ok(createPoi.render(form(POIForm.class)));
    }

    public static class POIForm {

        public int id;
        public boolean accesibility;
        public int minimalAge;
        public String name;
        public double rating = 0;
        public Time requiredTime = new Time(System.currentTimeMillis());
        public PoiType type;
        public int cost;
        public double latitude;
        public double longtitude;
        public String button;
    }
}
