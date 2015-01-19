package src.service.impl;

import com.pdfjet.*;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.Trip;
import src.persistence.models.TripStatus;
import src.service.TripService;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Set;

/**
 * Created by Krasotin on 11.12.14.
 */
public class TripServiceImpl implements TripService {

    static UserDAOImpl userDAO = new UserDAOImpl();
    static PoiDAOImpl poiDAO = new PoiDAOImpl();
    static TripDAOImpl tripDAO = new TripDAOImpl();

    static PoiServiceImpl poiService = new PoiServiceImpl();

    @Override
    public void addPoi(Trip trip, Poi poi) {
        Trip tripDb = tripDAO.getTrip(trip.getId());
        Poi poiDb = poiDAO.getPoi(poi.getId());

        if (poiDb == null) {
            poiDAO.savePoi(poi);
        } else {
            poi = poiDb;
        }

        if (tripDb == null) {
            tripDAO.saveTrip(trip);
        } else {
            trip = tripDb;
            //tripDAO.updateTrip(trip);
        }

        trip.addPoi(poi);
        tripDAO.updateTrip(trip);
    }

    @Override
    public void changeTripStatus(Trip trip, TripStatus tripStatus) {
        Trip tripDb = tripDAO.getTrip(trip.getId());

        if (tripDb == null) {
            tripDAO.saveTrip(trip);
        } else {
            trip = tripDb;
        }

        trip.setTripStatus(tripStatus);
        tripDAO.updateTrip(trip);
    }

    public Trip createTripFromPoisString(String poisString) {
        Set<Poi> newTripPOIS = poiService.getPoisFromString(poisString);

        //create trip from poi
        Trip newTrip = new Trip();
        newTrip.setCost(0);
        newTrip.setTripStatus(TripStatus.NOTPAID);
        for (Poi poi : newTripPOIS) {
            newTrip.addPoi(poi);
            newTrip.setCost(newTrip.getCost() + poi.getCost());
        }

        if (newTrip.getCost() == 0)
            newTrip.setTripStatus(TripStatus.PAID);

        System.out.println(newTrip);
        System.out.println("============");

        tripDAO.saveTrip(newTrip);
        return newTrip;
    }

    public Trip getTripById(int id) {
        return tripDAO.getTrip(id);
    }

    public void printTripDataToPdf(Trip trip, String path, String documentName) throws Exception {
        String finalPath;
        if (path.length() > 0)
            finalPath = path + "/" + documentName;
        else
            finalPath = documentName;

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream(finalPath)));

        String header = trip.getName();

        Font fontHeader = new Font(pdf, CoreFont.HELVETICA_BOLD);
        fontHeader.setSize(21);
        Font fontTotalCost = new Font(pdf, CoreFont.HELVETICA_BOLD);
        fontHeader.setSize(21);
        fontTotalCost.setItalic(true);
        Font fontText = new Font(pdf, CoreFont.HELVETICA_BOLD);
        fontText.setSize(14);


        Page page = new Page(pdf, Letter.PORTRAIT);

        TextLine text = new TextLine(fontHeader);
        text.setText("Trip id: " + String.valueOf(trip.getId()));
        text.setLocation(45f, 100f);
        text.drawOn(page);

        Set<Poi> poiInTrip = trip.getPois();
        String pois = "";
        float y = 150f;
        for (Poi poi : poiInTrip) {
            pois = poi.getName() + " for $" + poi.getCost();

            text = new TextLine(fontText, pois);
            text.setLocation(45f, y);
            y += 25f;
            text.drawOn(page);
        }

        text = new TextLine(fontTotalCost, "Total cost $" + String.valueOf(trip.getCost()));
        //y += 25f;
        text.setLocation(45f, y);
        text.drawOn(page);


        pdf.close();
    }
}
