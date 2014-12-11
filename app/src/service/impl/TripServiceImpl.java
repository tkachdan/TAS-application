package src.service.impl;

import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.Trip;
import src.persistence.models.TripStatus;
import src.service.TripService;

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

        if (poiDb == null)
            poiDAO.savePoi(poi);
        else
            poi = poiDb;

        if (tripDb == null)
            tripDAO.saveTrip(trip);
        else
            trip = tripDb;

        trip.addPoi(poi);
        tripDAO.updateTrip(trip);

        Trip tripDb2 = tripDAO.getTrip(trip.getId());
    }

    @Override
    public void changeTripStatus(Trip trip, TripStatus tripStatus) {
        Trip tripDb = tripDAO.getTrip(trip.getId());

        if (tripDb == null)
            tripDAO.saveTrip(trip);
        else
            trip = tripDb;

        trip.setTripStatus(tripStatus);
        tripDAO.updateTrip(trip);
    }
}
