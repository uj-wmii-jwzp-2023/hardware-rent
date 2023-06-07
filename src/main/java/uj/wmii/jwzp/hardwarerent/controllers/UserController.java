package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.UserProfile;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;

import java.math.BigDecimal;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    public UserController(MyUserDetailsService myUserDetailsService, UserRepository userRepository) {
        this.myUserDetailsService = myUserDetailsService;
        this.userRepository = userRepository;
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

    @PatchMapping("/cash")
    public ResponseEntity addSomeCash(@RequestParam BigDecimal cash, Authentication authentication) {
        try {
            MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());
            if (user == null)
                return ResponseEntity.badRequest().body("Error while searching user form authentication data");

            user.setCash(cash);
            userRepository.save(user);
            return ResponseEntity.ok().body("Added cash successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("internal server error");
        }
    }

    @PatchMapping("/username")
    public ResponseEntity changeUsername(@RequestParam String username, Authentication authentication) {
        try {
            MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());
            if (user == null)
                return ResponseEntity.badRequest().body("Error while searching user from authentication data");

            user.setUsername(username);
            userRepository.save(user);
            return ResponseEntity.ok().body("Username changed successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("internal server error");
        }
    }

    @PatchMapping("/password")
    public ResponseEntity changePassword(@RequestParam String password, Authentication authentication) {
        try {
            MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());
            if (user == null)
                return ResponseEntity.badRequest().body("Error while searching user from authentication data");

            user.setPassword(password);
            userRepository.save(user);
            return ResponseEntity.ok().body("Password changed successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("internal server error");
        }
    }
    public UserDetails getUserByUsername(String username) {
        return myUserDetailsService.loadUserByUsername(username);
    }

}
