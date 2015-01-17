package Cart;

import org.junit.Test;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.PoiType;
import src.persistence.models.Trip;
import src.persistence.models.User;
import src.service.impl.TripServiceImpl;

import java.sql.Time;

/**
 * Created by Krasotin on 13.01.15.
 */
public class TestAddingTripToCart {
    private static UserDAOImpl userDAO = new UserDAOImpl();
    private static PoiDAOImpl poiDAO = new PoiDAOImpl();
    private static TripDAOImpl tripDAO = new TripDAOImpl();
    private static TripServiceImpl tripService = new TripServiceImpl();

    @Test
    public void testAddTripToCart() {
        User user = new User("Administrator", "test@test.test", "Tester", "Testovic", "pass", "testusername");
        //userDAO.saveUser(user);

        Poi poi1 = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        Poi poi2 = new Poi(true, 0, "Second bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        Poi poi3 = new Poi(true, 0, "Third bridge", 0.8, new Time(1, 0, 0), PoiType.HISTORY, 0, 0.0, 0.0);

        Trip trip = new Trip("test", 0);


        tripService.addPoi(trip, poi1);
        tripService.addPoi(trip, poi2);
        tripService.addPoi(trip, poi3);
        //Trip tripDb = tripDAO.getTrip(trip.getId());

        trip = tripDAO.getTrip(trip.getId());
        user.addTrip(trip);
        userDAO.updateUser(user);


        System.out.println(trip);
        /*System.out.println("--");
        System.out.println(tripDb);*/

    }
}
