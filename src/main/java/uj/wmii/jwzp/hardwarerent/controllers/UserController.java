package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;

import java.util.Collection;

public class UserController {
    private final MyUserDetailsService myUserDetailsService;

    public UserController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }
    public UserDetails getUserByUsername(String username)
    {
        return myUserDetailsService.loadUserByUsername(username);
    }
    public Collection<? extends GrantedAuthority> getAuthorities(MyUser user)
    {
        return myUserDetailsService.getAuthorities(user.getRoles());
    }
}
