package StockManagement.app.Model;

import StockManagement.app.Model.Enums.UserRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User { //

    @Id
    @GeneratedValue
    private int id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 1, message = "Password must be at least 1 characters")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // Roller için birden fazla değer desteklenir
    @Enumerated(EnumType.STRING) // Enum değerlerini String olarak sakla
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role") // Bu sütun enum değerini saklar
    private Set<UserRoleEnum> roles = new HashSet<>();
}
