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
    @Override
    public void saveTrip(Trip trip) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.save(trip);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Trip getTrip(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        Trip trip = (Trip) session
                .createQuery("from Trip trip where id = :tripID")
                .setInteger("tripID", id).uniqueResult();

        session.close();
        if (trip == (null))
            return null;
        else
            return trip;
    }

    @Override
    public void updateTrip(Trip trip) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.update(trip);
        session.getTransaction().commit();
    }

    @Override
    public void deleteTrip(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        Trip trip = (Trip) session.load(Trip.class, id);
        session.delete(trip);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Trip> getAllTrips() {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        List trips = session.createQuery("FROM Trip ").list();
        session.close();
        return trips;
    }
}
