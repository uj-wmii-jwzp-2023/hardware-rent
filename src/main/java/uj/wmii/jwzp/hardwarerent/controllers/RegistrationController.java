package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.RegistrationForm;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;

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
    public void processRegistration(RegistrationForm form) {
        registerUser(form.toUser(encoder));
    }
    public void registerUser(MyUser user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
