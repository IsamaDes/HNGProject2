package desmond.example.HNGProject2.services;


import desmond.example.HNGProject2.dto.LoginRequest;
import desmond.example.HNGProject2.dto.SignupRequest;
import desmond.example.HNGProject2.exception.ErrorResponse;
import desmond.example.HNGProject2.exception.SuccessResponse;

public interface UserService {
    Object saveUser(SignupRequest signupRequest) throws Throwable;
    SuccessResponse logInUser(LoginRequest loginRequest) throws ErrorResponse;
    SuccessResponse getUser(Long id);
}