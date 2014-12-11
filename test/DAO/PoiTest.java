package DAO;

import org.hibernate.Session;
import org.junit.Test;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.models.Poi;
import src.persistence.utils.HibernateUtils;

import java.sql.Time;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by tkachdan on 11-Dec-14.
 */
public class PoiTest {
    static Session session = new HibernateUtils().getSessionFactory().openSession();
    static PoiDAOImpl poiDAO = new PoiDAOImpl();

    public static void clearDB() {
        List<Poi> poiDb = poiDAO.getAllPois();
        for (Poi poi : poiDb)
            poiDAO.deletePoi(poi.getId());
    }

    @Test
    public void testSavePoi() {
        clearDB();

        Poi poi = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), "common", 0, 0.0, 0.0);
        poiDAO.savePoi(poi);

        Poi poiDb = (Poi) session
                .createQuery("from  Poi  poi where id = :poiID")
                .setInteger("poiID", poi.getId()).uniqueResult();

        assertThat(poi).isEqualTo(poiDb);
    }

    @Test
    public void testGetPoi() {
        clearDB();

        Poi poi = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), "common", 0, 0.0, 0.0);
        poiDAO.savePoi(poi);

        Poi poiDb = poiDAO.getPoi(poi.getId());
        assertThat(poi).isEqualTo(poiDb);
    }

    @Test
    public void testGetNullPoi() {
        clearDB();

        Poi poi = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), "common", 0, 0.0, 0.0);
        poiDAO.savePoi(poi);

        Poi poiDb = poiDAO.getPoi(poi.getId() + 1);
        assertThat(poiDb).isEqualTo(null);
        assertThat(poiDb == null).isEqualTo(true);
    }

    @Test
    public void testGetAllPois() {
        clearDB();

        Poi poi1 = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), "common", 0, 0.0, 0.0);
        Poi poi2 = new Poi(true, 0, "Second bridge", 0.8, new Time(1, 0, 0), "common", 0, 0.0, 0.0);
        poiDAO.savePoi(poi1);
        poiDAO.savePoi(poi2);

        List<Poi> usersDb = poiDAO.getAllPois();
        assertThat(usersDb.size()).isEqualTo(2);
    }
}

