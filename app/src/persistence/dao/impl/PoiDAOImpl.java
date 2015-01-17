package src.persistence.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import src.persistence.dao.PoiDAO;
import src.persistence.models.Poi;
import src.persistence.models.PoiType;
import src.persistence.utils.HibernateUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public class PoiDAOImpl implements PoiDAO {
    //Session session = new HibernateUtils().getSessionFactory().openSession();

    /**
     * saving new poi to database
     * @param poi POI model
     */
    @Override
    public void savePoi(Poi poi) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.save(poi);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * getin poi from database
     * @param id of desired POI
     * @return poi model
     */
    @Override
    public Poi getPoi(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        Poi poi = (Poi) session
                .createQuery("from Poi poi where id = :poiID")
                .setInteger("poiID", id).uniqueResult();

        session.close();
        if (poi == (null)) {
            return null;
        } else {
            return poi;
        }
    }

    /***
     * update poi
     * @param poi
     */
    @Override
    public void updatePoi(Poi poi) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.update(poi);
        session.getTransaction().commit();
    }

    /**
     * deleting poi from database
     * @param id of desire poi
     */
    @Override
    public void deletePoi(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        Poi poi = (Poi) session.load(Poi.class, id);
        session.delete(poi);
        session.getTransaction().commit();
        session.close();
    }

    /**
     *
     * @param maxCost - maximal cost of poi
     * @return list of filtred pois by cost
     */
    @Override
    public List<Poi> getAllPoisByCost(int maxCost) {
        /*Query query = session.createQuery("SELECT r FROM Team t JOIN t.teamMembers r WHERE t.id = :teamIdParam");*/

        Session session = new HibernateUtils().getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("FROM Poi t WHERE t.cost <= :costParam");
        query.setParameter("costParam", maxCost);
        List<Poi> pois = query.list();

        session.close();
        return pois;
    }

    /**
     *
     * @param minimalRating
     * @return list of filtred pois by rating
     */
    @Override
    public List<Poi> getAllPoisByRating(double minimalRating) {
        Session session = new HibernateUtils().getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("FROM Poi t WHERE t.rating >= :ratingParam");
        query.setParameter("ratingParam", minimalRating);
        List<Poi> pois = query.list();

        session.close();
        return pois;
    }

    /**
     *
     * @param types
     * @return
     */
    @Override
    public List<Poi> getAllPoisByTypes(List<PoiType> types) {
        Session session = new HibernateUtils().getSessionFactory().openSession();
        session.beginTransaction();
        List<Poi> pois = new ArrayList<>();

        for (PoiType type : types) {
            Query query = session.createQuery("FROM Poi t WHERE t.type = :typeParam");
            query.setParameter("typeParam", type);
            pois.addAll(query.list());
        }

        session.close();
        return pois;
    }

    /**
     *
     * @param type of poi
     * @return list of filtred pois by type
     */
    @Override
    public List<Poi> getAllPoisByType(PoiType type) {
        Session session = new HibernateUtils().getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("FROM Poi t WHERE t.type = :typeParam");
        query.setParameter("typeParam", type);
        List<Poi> pois = query.list();

        session.close();
        return pois;
    }

    /**
     *
     * @return list of all pois in database
     */
    @Override
    public List<Poi> getAllPois() {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        List<Poi> pois = session.createQuery("FROM Poi ").list();
        session.close();
        return pois;
    }
}
