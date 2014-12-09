package persistence.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import persistence.dao.UserDAO;
import persistence.models.User;
import persistence.utils.HibernateUtils;

import java.util.List;

/**
 * Created by tkachdan on 08-Dec-14.
 */
public class UserDAOImpl implements UserDAO {
    Session session = new HibernateUtils().getSessionFactory().openSession();

    @Override
    public void saveUser(User user) {
        Session session = new HibernateUtils().getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }


    @Override
    public User getUser(int id) {
        session.beginTransaction();
        User user = null;

        user = (User) session
                .createQuery("from User user where id = :userID")
                .setInteger("userID", id).uniqueResult();

        session.getTransaction().commit();

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        if (!session.isOpen())
            session.beginTransaction();
        User user = null;

        Query query = session.createQuery("FROM User u WHERE u.email = :emailParam");
        query.setParameter("emailParam", email);

        List<User> users = query.list();
        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }


    @Override
    public List<User> getAllUsers() {
        session.beginTransaction();
        List users = session.createQuery("FROM User ").list();

        return users;
    }

    @Override
    public void updateUser(User user) {
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
    }

    @Override
    public void deleteUser(int id) {
        session.beginTransaction();

        User user = (User) session.load(User.class, id);
        session.delete(user);
        session.getTransaction().commit();
    }
}
