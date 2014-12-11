package src.persistence.dao;

import src.persistence.models.Poi;

import java.util.List;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public interface PoiDAO {
    public void savePoi(Poi poi);

    public Poi getPoi(int id);

    public List<Poi> getAllPois();

    public void updatePoi(Poi poi);

    public void deletePoi(int id);
}
