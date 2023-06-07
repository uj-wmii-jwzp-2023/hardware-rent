package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerent.services.impl.TokenService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity  token(Authentication authentication) {
        String token;
        Map<String, Object > responseData = new HashMap<>();

        try
        {
            LOG.info("Token requested for user: '{}'", authentication.getName());
            token = tokenService.generateToken(authentication);
            if(token == null) {
                LOG.error("cant get token for user: '{}'",authentication.getName());
                return ResponseEntity.internalServerError().body("internal server error"); // return error response
            }else {

                LOG.info("Token granted: {}", token);
            }
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // return error response
        }


        responseData.put("username", authentication.getName());
        responseData.put("authorities", authentication.getAuthorities());
        responseData.put("accessToken", token);
        return ResponseEntity.ok().body(responseData);
    }

}
