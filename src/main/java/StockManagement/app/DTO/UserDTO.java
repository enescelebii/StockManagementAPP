package StockManagement.app.DTO;

import StockManagement.app.Model.Enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data // Getter, Setter, toString, equals, hashCode metodlarını otomatik olarak oluşturur
@NoArgsConstructor // Parametresiz constructor oluşturur
@AllArgsConstructor // Parametreli constructor oluşturur
public class UserDTO {
    private int id;
    private String username;
    private String password;
    private Set<UserRoleEnum> role; // User role
}
