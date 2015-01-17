package src.persistence.dao.impl;

import org.hibernate.Session;
import src.persistence.dao.TripDAO;
import src.persistence.models.Trip;
import src.persistence.utils.HibernateUtils;

import java.util.List;

/**
 * Created by Krasotin on 11.12.14.
 */
public class TripDAOImpl implements TripDAO {

    /**
     * saving new trip to database
     * @param trip ew want to save
     */
    @Override
    public void saveTrip(Trip trip) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.save(trip);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * get trip from database
     * @param id of desire trip
     * @return trip model from dtabase
     */
    @Override
    public Trip getTrip(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        Trip trip = (Trip) session
                .createQuery("from Trip trip where id = :tripID")
                .setInteger("tripID", id).uniqueResult();

        session.close();

        if (trip == (null)) {
            return null;
        }

        if (trip.getPois().size() == 0) {
            trip.setPois(null);
        }

        return trip;
    }

    /**
     * updating trip in database
     * @param trip we want to change
     */
    @Override
    public void updateTrip(Trip trip) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.update(trip);
        session.getTransaction().commit();
    }

    /**
     * deleting trip from database based on id
     * @param id of trip we want to delete
     */
    @Override
    public void deleteTrip(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        Trip trip = (Trip) session.load(Trip.class, id);
        session.delete(trip);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * get all trips from databse
     * @return list of all trips in databse
     */
    @Override
    public List<Trip> getAllTrips() {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        List trips = session.createQuery("FROM Trip ").list();
        session.close();
        return trips;
    }
}
