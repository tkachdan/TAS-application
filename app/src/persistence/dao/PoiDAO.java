package src.persistence.dao;

import src.persistence.models.Poi;
import src.persistence.models.PoiType;

import java.util.List;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public interface PoiDAO {

    public void savePoi(Poi poi);

    public Poi getPoi(int id);

    public void updatePoi(Poi poi);

    public void deletePoi(int id);

    public List<Poi> getAllPoisByCost(int maxCost);

    public List<Poi> getAllPoisByRating(double minimalRating);

    public List<Poi> getAllPoisByType(PoiType type);

    public List<Poi> getAllPoisByTypes(List<PoiType> types);

    public List<Poi> getAllPois();
}
