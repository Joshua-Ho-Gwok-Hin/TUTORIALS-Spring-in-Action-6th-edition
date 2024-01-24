package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation
        .authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web
        .builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tacos.data.UserRepository;
import tacos.User;

@Configuration
//@EnableGlobalAuthentication
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/design", "/orders").hasRole("USER")
                        .anyRequest().permitAll())
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage("/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .loginProcessingUrl("/login")
                        )
                .logout(logoutConfigurer -> logoutConfigurer
//                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(UserRepository userRepo) {
//        return username -> {
//            User user = userRepo.findByUsername(username);
//            if (user != null) {
//                return user;
//            }
//            throw new UsernameNotFoundException(
//                    "User '" + username + "' not found");
//        };
//    }

}
