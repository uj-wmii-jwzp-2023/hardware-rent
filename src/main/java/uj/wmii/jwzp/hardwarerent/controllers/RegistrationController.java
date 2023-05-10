package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.RegistrationForm;
import uj.wmii.jwzp.hardwarerent.data.Role;
import uj.wmii.jwzp.hardwarerent.repositories.RoleRepository;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping("/register")
public class RegistrationController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = encoder;
    }

    @PostMapping
    public ResponseEntity<?> processRegistration(RegistrationForm form) {
        try
        {
            if(userRepository.findByUsername(form.getUsername()) != null)
            {return ResponseEntity.status(404).body("User with this username is already existed");}

            var userRole =  roleRepository.findByName("ROLE_USER");
            MyUser createdUser = form.toUserWithoutRoles();
            createdUser.setRoles(Arrays.asList(userRole));

            var userRegistered =  registerUser(createdUser);
            if (userRegistered == null)
                return ResponseEntity.status(404).body("Error while registering new user");

        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // return error response
        }

        return ResponseEntity.ok().body("user registered successfully");

    }
    public MyUser registerUser(MyUser user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
