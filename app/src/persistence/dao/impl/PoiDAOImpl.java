package src.persistence.dao.impl;

import org.hibernate.Session;
import src.persistence.dao.PoiDAO;
import src.persistence.models.Poi;
import src.persistence.utils.HibernateUtils;

import java.util.List;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public class PoiDAOImpl implements PoiDAO {
    Session session = new HibernateUtils().getSessionFactory().openSession();

    @Override
    public void savePoi(Poi poi) {

    }

    @Override
    public Poi getPoi(int id) {
        return null;
    }

    @Override
    public List<Poi> getAllPois() {
        session.beginTransaction();
        List pois = session.createQuery("FROM Poi ").list();

        return pois;
    }

    @Override
    public void updatePoi(Poi poi) {

    }

    @Override
    public void deletePoi(int id) {

    }
}
