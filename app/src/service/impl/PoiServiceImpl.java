package src.service.impl;

import src.persistence.dao.PoiDAO;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.PoiType;
import src.service.PoiService;

import java.util.List;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public class PoiServiceImpl implements PoiService {
    protected static final PoiDAO poiDAO = new PoiDAOImpl();

    @Override
    public void upadtePoi(Poi poi) {
        poiDAO.updatePoi(poi);
    }

    @Override
    public void addPoi(Poi poi) {
        poiDAO.savePoi(poi);
    }

    @Override
    public Poi getPoiById(int id) {
        return poiDAO.getPoi(id);
    }

    @Override
    public List<Poi> getAllPoisByCost(int maxCost) {
        return poiDAO.getAllPoisByCost(maxCost);
    }

    @Override
    public List<Poi> getAllPoisByRating(int minimalRating) {
        return poiDAO.getAllPoisByRating(minimalRating);
    }

    @Override
    public List<Poi> getAllPoisByType(PoiType type) {
        return poiDAO.getAllPoisByType(type);
    }

    @Override
    public List<Poi> getAllPois() {
        return poiDAO.getAllPois();
    }
}