package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
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
        var userRegistered =  registerUser(form.toUser(encoder));
        if (userRegistered == null)
            return ResponseEntity.status(404).body("Error while registering new user");
        return ResponseEntity.ok().body(userRegistered);

    }
    public MyUser registerUser(MyUser user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
