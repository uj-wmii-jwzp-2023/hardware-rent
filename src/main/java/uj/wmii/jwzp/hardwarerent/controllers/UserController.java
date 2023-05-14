package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.UserProfile;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;

import java.util.Collection;
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private final MyUserDetailsService myUserDetailsService;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    public UserController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }
    @GetMapping("/profile")
    public ResponseEntity getProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            UserProfile userProfile = myUserDetailsService.getUserProfileByUsername(username);
            LOG.info("Returned userProfile with username: "+ userProfile.getUsername());
            return ResponseEntity.ok().body(userProfile);
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }

    }
    public UserDetails getUserByUsername(String username)
    {
        return myUserDetailsService.loadUserByUsername(username);
    }

}
