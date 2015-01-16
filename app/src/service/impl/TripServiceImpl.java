package src.service.impl;

import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.Trip;
import src.persistence.models.TripStatus;
import src.service.TripService;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Krasotin on 11.12.14.
 */
public class TripServiceImpl implements TripService {

    static UserDAOImpl userDAO = new UserDAOImpl();
    static PoiDAOImpl poiDAO = new PoiDAOImpl();
    static TripDAOImpl tripDAO = new TripDAOImpl();

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

        tripDAO.saveTrip(newTrip);
        return newTrip;
    }
}
