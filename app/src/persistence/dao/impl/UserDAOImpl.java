package src.persistence.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import src.persistence.dao.UserDAO;
import src.persistence.models.User;
import src.persistence.utils.HibernateUtils;

import java.util.List;

/**
 * Created by tkachdan on 08-Dec-14.
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public void saveUser(User user) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public User getUser(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        User user = (User) session
                .createQuery("from User user where id = :userID")
                .setInteger("userID", id).uniqueResult();

        session.close();
        //session.getTransaction().commit();
        if (user == (null)) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        if (!session.isOpen()) {
            session.beginTransaction();
        }
        User user = null;

        Query query = session.createQuery("FROM User u WHERE u.email = :emailParam");
        query.setParameter("emailParam", email);
        List<User> users = query.list();
        session.close();

        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public void updateUser(User user) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
    }

    @Override
    public void deleteUser(int id) {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        User user = (User) session.load(User.class, id);
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = new HibernateUtils().getSessionFactory().openSession();

        session.beginTransaction();
        List users = session.createQuery("FROM User ").list();
        session.close();
        return users;
    }

    public static String getDesignation(String email) {
        UserDAO userDAO = new UserDAOImpl();
        return userDAO.getUserByEmail(email).getDesignation();
    }
}
