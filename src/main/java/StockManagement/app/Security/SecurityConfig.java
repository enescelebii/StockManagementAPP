package StockManagement.app.Security;

import StockManagement.app.Model.Enums.UserRoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http// Önyüz kontrolü için form gönderimlerinde CSRF'yi devre dışı bırakıyoruz
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**" , "/static/**").permitAll()
                        .requestMatchers("/adminstock").hasRole("ADMIN") // Sadece adminlere izin ver
                        .requestMatchers("/").hasAnyRole("ADMIN", "CUSTOMER") // Hem admin hem de customer erişebilir
                        .anyRequest().authenticated()// Diğer tüm istekler için giriş zorunlu
                )
                .formLogin(form -> form
                        .loginPage("/login") // Kullanıcı giriş sayfası
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/home", true) // Giriş başarılı olursa /home yönlendir
                        .permitAll() // Herkes giriş sayfasına erişebilir
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Çıkış yapmak için URL
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "customCookieName")
                        .logoutSuccessUrl("/login?logout") // Çıkış sonrası yönlendirme
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

}
