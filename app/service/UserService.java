package service;

import persistence.models.User;

/**
 * Created by tkachdan on 08-Dec-14.
 */
public interface UserService {

    public boolean login(String username,String password);

}
