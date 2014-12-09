package service.impl;

import persistence.dao.UserDAO;
import persistence.dao.impl.UserDAOImpl;
import persistence.models.User;
import service.UserService;

/**
 * Created by tkachdan on 08-Dec-14.
 */
public class UserServiceImpl implements UserService {
    protected static UserDAO userDAO = new UserDAOImpl();

    @Override
    public boolean login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user == null)
            return false;
        else {
           if(user.getPassword().equals(password)){
               return true;
           }else{
               return false;
           }
        }

    }
}
