package src.service;

import src.persistence.models.Poi;
import src.persistence.models.PoiType;

import java.util.List;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public interface PoiService {

    public void upadtePoi(Poi poi);

    public void addPoi(Poi poi);

    public Poi getPoiById(int id);

    public List<Poi> getAllPoisByCost(int maxCost);

    public List<Poi> getAllPoisByRating(int minimalRating);

    public List<Poi> getAllPoisByType(PoiType type);

    public List<Poi> getAllPoisByTypes(List<PoiType> types);

    public List<Poi> getAllPois();
}