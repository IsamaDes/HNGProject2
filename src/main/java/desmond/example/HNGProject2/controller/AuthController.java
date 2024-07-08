package desmond.example.HNGProject2.controller;

import desmond.example.HNGProject2.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public ResponseEntity<String> homePage(){
        String welcome = authService.WelcomeMessage();
        return new ResponseEntity<>(welcome, HttpStatus.OK);
    }

}