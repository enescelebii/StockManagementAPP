package StockManagement.app.Controller;

import StockManagement.app.DTO.UserDTO;
import StockManagement.app.Model.Enums.UserRoleEnum;
import StockManagement.app.Model.User;
import StockManagement.app.Service.UserServiceImpl;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    // Kullanıcıları veritabanından çekip döndürür
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userServiceImpl.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content döner
        } else {
            return ResponseEntity.ok(users); // 200 OK ve kullanıcı listesi JSON olarak döner
        }
    }

    // Kullanıcı ekleme
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Geçersiz kullanıcı verisi!");
        }

        // Gelen rollerin doğruluğunu kontrol edin
        for (UserRoleEnum role : userDTO.getRole()) {
            if (!EnumUtils.isValidEnum(UserRoleEnum.class, role.name())) {
                return ResponseEntity.badRequest().body("Geçersiz rol: " + role);
            }
        }


        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRoles(userDTO.getRole());

        userServiceImpl.addUser(user);

        return new ResponseEntity<>("Kullanıcı başarıyla eklendi", HttpStatus.CREATED); // Yeni eklenen user için 201 döndürülüyor
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") int id) {
        userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
