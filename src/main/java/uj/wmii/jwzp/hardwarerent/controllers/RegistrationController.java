package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerent.data.RegistrationForm;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;

import java.io.Serializable;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping
    public ResponseEntity processRegistration(RegistrationForm form) {
        var userAdded = userRepository.save(form.toUser(encoder));
        if (userAdded == null)
            return ResponseEntity.status(404).body("Error while adding new user");
        return ResponseEntity.ok().body(userAdded);
    }
}
