package Service;

import org.junit.Test;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.PoiType;
import src.persistence.models.Trip;
import src.persistence.models.TripStatus;
import src.service.impl.TripServiceImpl;

import java.sql.Time;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Krasotin on 11.12.14.
 */
public class TestTripServiceImpl {
    static PoiDAOImpl poiDAO = new PoiDAOImpl();
    static TripDAOImpl tripDAO = new TripDAOImpl();
    static TripServiceImpl tripService = new TripServiceImpl();

    public static void clearDB() {
        List<Trip> tripDb = tripDAO.getAllTrips();
        for (Trip trip : tripDb)
            tripDAO.deleteTrip(trip.getId());
    }

    @Test
    public void testAddPoi() {
        clearDB();

        Poi poi1 = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        Poi poi2 = new Poi(true, 0, "Second bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        Poi poi3 = new Poi(true, 0, "Third bridge", 0.8, new Time(1, 0, 0), PoiType.HISTORY, 0, 0.0, 0.0);
        poiDAO.savePoi(poi3);
        Trip trip = new Trip("test", 0);

        tripService.addPoi(trip, poi1);
        tripService.addPoi(trip, poi2);
        tripService.addPoi(trip, poi3);

        Trip tripDb = tripDAO.getTrip(trip.getId());
        System.out.println(trip);
        System.out.println(tripDb);
        assertThat(tripDb.getPois().size()).isEqualTo(3);
    }

    @Test
    public void testChangeStatus() {
        Trip trip = new Trip("test", 0);
        tripService.changeTripStatus(trip, TripStatus.PENDING);

        Trip tripDb = tripDAO.getTrip(trip.getId());
        assertThat(tripDb.getTripStatus()).isEqualTo(TripStatus.PENDING);

        tripService.changeTripStatus(trip, TripStatus.ACCEPTED);
        tripDb = tripDAO.getTrip(trip.getId());
        assertThat(tripDb.getTripStatus()).isEqualTo(TripStatus.ACCEPTED);

    }
}
