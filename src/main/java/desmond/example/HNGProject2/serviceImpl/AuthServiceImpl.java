package desmond.example.HNGProject2.serviceImpl;

import desmond.example.HNGProject2.services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Override
    public String WelcomeMessage() {
        return "Welcome";
    }
}
