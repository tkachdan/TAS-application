package persistence.dao;

import persistence.models.User;

import java.util.List;

/**
 * Created by tkachdan on 08-Dec-14.
 */
public interface UserDAO {
    public void saveUser(User team);

    public User getUser(int id);

    public User getUserByEmail(String email);

    public List<User> getAllUsers();

    public void updateUser(User user);

    public void deleteUser(int id);
}
