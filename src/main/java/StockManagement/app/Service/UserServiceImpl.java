package StockManagement.app.Service;

import StockManagement.app.Model.User;
import StockManagement.app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public User addUser(User user){
        // Şifreyi hash'le
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Hash'lenmiş şifreyi set et
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUser(int id){
        return userRepository.findById(id).get();
    }
    public void deleteUser(int id){
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
    }
    public User updateUser(User user){
        return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username); // Kullanıcıyı username'e göre arıyoruz
    }
    public void deleteByName(User user) {
        userRepository.delete(user); // Kullanıcıyı veritabanından siliyoruz
    }

}
