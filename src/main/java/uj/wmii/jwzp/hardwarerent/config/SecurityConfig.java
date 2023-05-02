package uj.wmii.jwzp.hardwarerent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.MyUserPrincipal;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;

import java.io.Console;

@Configuration
@EnableWebSecurity

public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.headers().frameOptions().disable();
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/products", "/products/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .defaultSuccessUrl("/products")
                .and().logout()
                .logoutUrl("/logout")
                .and().httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            MyUser user = userRepository.findByUsername(username);
            if (user != null) return new MyUserPrincipal(user);
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

}
