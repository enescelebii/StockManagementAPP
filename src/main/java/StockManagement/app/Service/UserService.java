package StockManagement.app.Service;

import StockManagement.app.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User addUser(User user);
    public void deleteUser(int id);
    public User updateUser(User user);
    public User getUser(int id);
    public List<User> getAllUsers();
    public Optional<User> findByUsername(String username);
    public void deleteByName(User user);
}
