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
    //Session session = new HibernateUtils().getSessionFactory().openSession();

    @Override
    public void savePoi(Poi poi) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.save(poi);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Poi getPoi(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        Poi poi = (Poi) session
                .createQuery("from Poi poi where id = :poiID")
                .setInteger("poiID", id).uniqueResult();

        session.close();
        if (poi == (null))
            return null;
        else
            return poi;
    }

    @Override
    public void updatePoi(Poi poi) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.update(poi);
        session.getTransaction().commit();
    }

    @Override
    public void deletePoi(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        Poi poi = (Poi) session.load(Poi.class, id);
        session.delete(poi);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Poi> getAllPois() {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        List pois = session.createQuery("FROM Poi ").list();
        session.close();
        return pois;
    }
}
