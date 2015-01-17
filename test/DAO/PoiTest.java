package DAO;

import org.hibernate.Session;
import org.junit.Test;
import src.persistence.dao.impl.PoiDAOImpl;
import src.persistence.models.Poi;
import src.persistence.models.PoiType;
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
    //    clearDB();

        Poi poi = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        poiDAO.savePoi(poi);

        Poi poiDb = (Poi) session
                .createQuery("from  Poi  poi where id = :poiID")
                .setInteger("poiID", poi.getId()).uniqueResult();

        assertThat(poi).isEqualTo(poiDb);
    }

    @Test
    public void testGetPoi() {
     //   clearDB();

        Poi poi = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), PoiType.HISTORY, 0, 0.0, 0.0);
        poiDAO.savePoi(poi);

        Poi poiDb = poiDAO.getPoi(poi.getId());
        assertThat(poi).isEqualTo(poiDb);
    }

    @Test
    public void testGetNullPoi() {
   //     clearDB();

        Poi poi = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        poiDAO.savePoi(poi);

        Poi poiDb = poiDAO.getPoi(poi.getId() + 1);
        assertThat(poiDb).isEqualTo(null);
        assertThat(poiDb == null).isEqualTo(true);
    }

    @Test
    public void testGetAllPois() {
      //  clearDB();

        Poi poi1 = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        Poi poi2 = new Poi(true, 0, "Second bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        poiDAO.savePoi(poi1);
        poiDAO.savePoi(poi2);

        List<Poi> poiDb = poiDAO.getAllPois();
        assertThat(poiDb.size()).isEqualTo(2);
    }

    @Test
    public void testGetAllPoisByCost() {
     //   clearDB();

        int maxCost = 500;

        Poi poi1 = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 800, 0.0, 0.0);
        Poi poi2 = new Poi(true, 0, "Second bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 50, 0.0, 0.0);
        Poi poi3 = new Poi(true, 0, "Second bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 350, 0.0, 0.0);
        poiDAO.savePoi(poi1);
        poiDAO.savePoi(poi2);
        poiDAO.savePoi(poi3);

        List<Poi> poiDb = poiDAO.getAllPoisByCost(maxCost);
        /*System.out.println(poiDb);*/
        assertThat(poiDb.size()).isEqualTo(2);
    }

    @Test
    public void testGetAllPoisByRating() {
       // clearDB();

        double minRating = 2.5;

        Poi poi1 = new Poi(true, 0, "Main bridge", 3.0, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        Poi poi2 = new Poi(true, 0, "Second bridge", 1.1, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        Poi poi3 = new Poi(true, 0, "Second bridge", 0.0, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        poiDAO.savePoi(poi1);
        poiDAO.savePoi(poi2);
        poiDAO.savePoi(poi3);

        List<Poi> poiDb = poiDAO.getAllPoisByRating(minRating);
        System.out.println(poiDb);
        assertThat(poiDb.size()).isEqualTo(1);
    }

    @Test
    public void testGetAllPoisByType() {
    //    clearDB();

        Poi poi1 = new Poi(true, 0, "Main bridge", 0.8, new Time(1, 0, 0), PoiType.HISTORY, 0, 0.0, 0.0);
        Poi poi2 = new Poi(true, 0, "Second bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        Poi poi3 = new Poi(true, 0, "Second bridge", 0.8, new Time(1, 0, 0), PoiType.CULTURE, 0, 0.0, 0.0);
        poiDAO.savePoi(poi1);
        poiDAO.savePoi(poi2);
        poiDAO.savePoi(poi3);

        List<Poi> poiDb = poiDAO.getAllPoisByType(PoiType.HISTORY);
        /*System.out.println(poiDb);*/
        assertThat(poiDb.size()).isEqualTo(1);
    }
}

