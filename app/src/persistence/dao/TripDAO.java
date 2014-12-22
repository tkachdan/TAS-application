package src.persistence.dao;

import src.persistence.models.Trip;

import java.util.List;

/**
 * Created by Krasotin on 11.12.14.
 */
public interface TripDAO {

    public void saveTrip(Trip trip);

    public Trip getTrip(int id);

    public List<Trip> getAllTrips();

    public void updateTrip(Trip trip);

    public void deleteTrip(int id);
}
