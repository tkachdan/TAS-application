package DAO;

import org.hibernate.Session;
import org.junit.Test;
import src.persistence.dao.impl.TripDAOImpl;
import src.persistence.models.Trip;
import src.persistence.utils.HibernateUtils;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public class TripTest {
    static Session session = new HibernateUtils().getSessionFactory().openSession();
    static TripDAOImpl tripDAO = new TripDAOImpl();

    public static void clearDB() {
        List<Trip> tripDb = tripDAO.getAllTrips();
        for (Trip trip : tripDb)
            tripDAO.deleteTrip(trip.getId());
    }

    @Test
    public void testSaveTrip() {
      //  clearDB();

        Trip trip = new Trip("test", 0, null);
        tripDAO.saveTrip(trip);

        Trip tripDb = (Trip) session
                .createQuery("from  Trip  trip where id = :tripID")
                .setInteger("tripID", trip.getId()).uniqueResult();

        assertThat(trip.getId()).isEqualTo(tripDb.getId());
        assertThat(trip.getName()).isEqualTo(tripDb.getName());
    }

    @Test
    public void testUpdateTrip() {
//        clearDB();

        Trip trip = new Trip("test", 0, null);
        tripDAO.saveTrip(trip);

        trip.setName("testUpdated");
        tripDAO.updateTrip(trip);

        Trip tripDb = (Trip) session
                .createQuery("from  Trip  trip where id = :tripID")
                .setInteger("tripID", trip.getId()).uniqueResult();

        //System.out.println(tripDb.getName());
        assertThat(trip.getName()).isEqualTo(tripDb.getName());
    }

    @Test
    public void testGetTrip() {
  //      clearDB();

        Trip trip = new Trip("test", 0, null);
        tripDAO.saveTrip(trip);

        Trip tripDb = tripDAO.getTrip(trip.getId());
        assertThat(trip).isEqualTo(tripDb);
    }

    @Test
    public void testGetNullTrip() {
    //    clearDB();

        Trip trip = new Trip("test", 0, null);
        tripDAO.saveTrip(trip);

        Trip poiDb = tripDAO.getTrip(trip.getId() + 1);
        assertThat(poiDb).isEqualTo(null);
        assertThat(poiDb == null).isEqualTo(true);
    }

    @Test
    public void testGetAllTrips() {
        clearDB();

        Trip poi1 = new Trip("test1", 0, null);
        Trip poi2 = new Trip("test2", 0, null);
        tripDAO.saveTrip(poi1);
        tripDAO.saveTrip(poi2);

        List<Trip> usersDb = tripDAO.getAllTrips();
        assertThat(usersDb.size()).isEqualTo(2);
    }
}
