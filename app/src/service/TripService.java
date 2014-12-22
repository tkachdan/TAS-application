package src.service;

import src.persistence.models.Poi;
import src.persistence.models.Trip;
import src.persistence.models.TripStatus;

/**
 * Created by Krasotin on 11.12.14.
 */
public interface TripService {

    public void addPoi(Trip trip, Poi poi);

    public void changeTripStatus(Trip trip, TripStatus tripStatus);
}
