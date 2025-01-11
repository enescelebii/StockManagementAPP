package StockManagement.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageUrlController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String homePage() {
        return "Home";
    }

    @GetMapping("/users")
    public String usersPage() {
        return "users";
    }
}
