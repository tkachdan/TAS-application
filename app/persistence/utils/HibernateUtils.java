package persistence.utils;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by tkachdan on 15-Nov-14.
 * <p>
 * get configured SessionFactory
 */
public class HibernateUtils {
    private SessionFactory ourSessionFactory;

    public SessionFactory getSessionFactory() {
        try {
            ourSessionFactory = new Configuration().
                    configure().
                    buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
        return ourSessionFactory;

    }
// INSERT INTO TABLE (ID, NAME, SURNAME) VALUES (1,'asd', 'gfdfg);
}
