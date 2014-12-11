package src.service.impl;

import src.persistence.dao.PoiDAO;
import src.persistence.dao.impl.PoiDAOImpl;
import src.service.PoiService;

import java.util.List;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public class PoiServiceImpl implements PoiService {
    protected static PoiDAO poiDAO = new PoiDAOImpl();

    @Override
    public List getAllPois() {
        return poiDAO.getAllPois();
    }
}
