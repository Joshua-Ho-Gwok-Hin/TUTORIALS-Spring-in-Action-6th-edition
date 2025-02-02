package tacos.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import tacos.User;
import tacos.data.UserRepository;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorise -> authorise
                        .requestMatchers("/design", "/orders")
                        .hasRole("USER")
                        .anyRequest().permitAll()
                )
                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login"))
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/"))
                // Make H2-Console non-secured; for debug purposes
                .csrf(csrfConfigurer -> csrfConfigurer.ignoringRequestMatchers("/h2-console/**"))
                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .headers(headerConfigurer -> headerConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);
            if (user != null) {
                return user;
            }
            throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
        };
    }


//    Original SFC code
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeRequests()
//                .mvcMatchers("/design", "/orders").hasRole("USER")
//                .anyRequest().permitAll()
//
//                .and()
//                .formLogin()
//                .loginPage("/login")
//
//                .and()
//                .logout()
//                .logoutSuccessUrl("/")
//
//                // Make H2-Console non-secured; for debug purposes
//                .and()
//                .csrf()
//                .ignoringAntMatchers("/h2-console/**")
//
//                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()
//
//                .and()
//                .build();
//    }

//  @Bean
//  public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//    List<UserDetails> usersList = new ArrayList<>();
//    usersList.add(new User(
//            "buzz", encoder.encode("password"),
//            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
//    usersList.add(new User(
//            "woody", encoder.encode("password"),
//            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
//    return new InMemoryUserDetailsManager(usersList);
//  }

}
