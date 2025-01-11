package StockManagement.app.Model;

import StockManagement.app.Model.Enums.UserRoleEnum;
import StockManagement.app.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Kullanıcı zaten varsa ekleme yapma
        String username = "enes";
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("123")); // Şifreyi encode edin
            user.getRoles().add(UserRoleEnum.ROLE_ADMIN); // Kullanıcı rolü ayarla
            userRepository.save(user);

            System.out.println("Default admin user created: " + username);
        } else {
            System.out.println("Default admin user already exists.");
        }
    }


}
