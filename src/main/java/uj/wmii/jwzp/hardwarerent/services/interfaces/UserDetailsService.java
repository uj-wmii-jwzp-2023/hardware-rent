package uj.wmii.jwzp.hardwarerent.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
