package src.service.impl;

import src.persistence.dao.PoiDAO;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.PoiType;
import src.service.PoiService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<Poi> getPoisFromString(String poisString) {
        String[] poisIdString = poisString.split("-");
        int[] poisIdInt = new int[poisIdString.length];
        Set<Poi> poiSet = new HashSet<>();

        for (int i = 0; i < poisIdString.length; i++) {
            System.out.println(poisIdString[i]);
            poisIdInt[i] = Integer.valueOf(poisIdString[i]);
            poiSet.add(poiDAO.getPoi(poisIdInt[i]));
            System.out.println("int POI " + poisIdInt[i]);
        }
        System.out.println("POIs set " + poiSet);
        System.out.println("============");

        return poiSet;
    }
}