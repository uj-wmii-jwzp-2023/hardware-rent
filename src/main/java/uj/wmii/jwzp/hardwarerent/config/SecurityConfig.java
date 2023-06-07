package uj.wmii.jwzp.hardwarerent.config;


import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.sun.net.httpserver.HttpsParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@CrossOrigin
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final RsaKeyProperties rsaKeys;

    public SecurityConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http ) throws Exception {

        System.out.println(http);
        http.headers().frameOptions().disable();
        http.
                cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/archived").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/products/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/orders/me").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/orders/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/orders").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/orders/me/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/orders/all/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/orders/{orderId}/{orderDetailId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/orders/orderDetail").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/orders/me/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/orders/all/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user/profile").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/user/cash").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/user/username").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/user/password").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                //.and().formLogin()
                //.defaultSuccessUrl("/products").and()
                .and().oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //w
                .httpBasic(withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MyUserDetailsService userService) {
        return username -> {
            MyUser user = userService.loadUserByUsername(username);
            if (user != null) return new MyUser(user);
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }
    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}
