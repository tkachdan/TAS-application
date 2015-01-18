package src.service.impl;

import src.persistence.dao.UserDAO;
import src.persistence.dao.impl.UserDAOImpl;
import src.persistence.models.User;
import src.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tkachdan on 08-Dec-14.
 */
public class UserServiceImpl implements UserService {

    protected static UserDAO userDAO = new UserDAOImpl();

    @Override
    public boolean login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return false;
        } else {
            if (user.getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public List<User> getAllUsers() { return userDAO.getAllUsers(); }
}
