package desmond.example.HNGProject2.controller;

import desmond.example.HNGProject2.dto.LoginRequest;
import desmond.example.HNGProject2.dto.SignupRequest;
import desmond.example.HNGProject2.exception.SuccessResponse;
import desmond.example.HNGProject2.exception.validation.Errors;
import desmond.example.HNGProject2.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> signUpUser(@RequestBody SignupRequest signupRequest, final HttpServletRequest request) {
        Object response = userService.saveUser(signupRequest);

        if (response instanceof Errors) {
            // Handle validation errors
            return ResponseEntity.unprocessableEntity().body(((Errors) response).getErrors());
        } else if (response instanceof SuccessResponse) {
            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (response instanceof ResponseStatusException){
            // Handle when duplicate email
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Duplicate email or user ID");
        } else {
            // Handle unexpected case, though ideally shouldn't occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected response type");
        }
    }


    @PostMapping("/auth/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody LoginRequest loginRequest) {
        SuccessResponse successResponse = userService.logInUser(loginRequest);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> getUsers(@PathVariable Long id){
        SuccessResponse successResponse = userService.getUser(id);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }


}