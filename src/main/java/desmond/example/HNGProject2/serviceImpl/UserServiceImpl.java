package desmond.example.HNGProject2.serviceImpl;



import desmond.example.HNGProject2.Repository.OrganisationRepository;
import desmond.example.HNGProject2.Repository.UserRepository;
import desmond.example.HNGProject2.dto.DataResponse;
import desmond.example.HNGProject2.dto.LoginRequest;
import desmond.example.HNGProject2.dto.SignupRequest;
import desmond.example.HNGProject2.dto.SignupResponse;
import desmond.example.HNGProject2.enums.Role;
import desmond.example.HNGProject2.exception.ErrorResponse;
import desmond.example.HNGProject2.exception.SuccessResponse;
import desmond.example.HNGProject2.exception.UserNotVerifiedException;
import desmond.example.HNGProject2.exception.validation.Errors;
import desmond.example.HNGProject2.model.Organisation;
import desmond.example.HNGProject2.model.User;
import desmond.example.HNGProject2.model.UserValidator;
import desmond.example.HNGProject2.services.UserService;
import desmond.example.HNGProject2.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final OrganisationRepository organisationRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserValidator userValidator, OrganisationRepository organisationRepository){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
        this.organisationRepository = organisationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not Found"));
    }

    @Transactional
    public Object saveUser(SignupRequest signupRequest) {
        Errors validationResponse = userValidator.validateUser(signupRequest);
        if (validationResponse != null) {

            return validationResponse;
        }


        User existingUserByEmail = userRepository.findUserByEmail(signupRequest.getEmail());
        if (existingUserByEmail != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Duplicate email or user ID");
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPhone(signupRequest.getPhone());
        user.setEmail(signupRequest.getEmail());
        user.setUserRole(Role.USER);

        User createdUser = userRepository.save(user);

        Optional<User> createdUserCheck = userRepository.findByEmail(user.getEmail());
        if (createdUserCheck.isEmpty()) {
            throw new UserNotVerifiedException("Registration unsuccessful");
        }

        Organisation organisation = new Organisation();
        String orgName = createdUser.getFirstName() + "'s Organisation";
        organisation.setName(orgName);

        Set<User> users = new HashSet<>();
        users.add(createdUser);
        organisation.setUsers(users);

        Organisation createdOrg = organisationRepository.save(organisation);

        // Associate the user with the organisation (assuming a bidirectional relationship)
        createdUser.getOrganisationList().add(createdOrg);
        userRepository.save(createdUser); // Update the user to include the organisation


        String accessToken = jwtUtil.createJwt.apply(createdUser);


        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setStatus("success");
        successResponse.setMessage("Registration Successful");

        SignupResponse signupResponse = new SignupResponse();
        signupResponse.setUserId(createdUser.getUserId());
        signupResponse.setFirstName(createdUser.getFirstName());
        signupResponse.setLastName(createdUser.getLastName());
        signupResponse.setEmail(createdUser.getEmail());
        signupResponse.setPassword(createdUser.getPassword());
        signupResponse.setPhone(createdUser.getPhone());

        DataResponse data = new DataResponse();
        data.setAccessToken(accessToken);
        data.setUser(signupResponse);

        successResponse.setData(data);

        return successResponse;

    }


    public SuccessResponse logInUser(LoginRequest loginRequest) throws ErrorResponse {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = loadUserByUsername(loginRequest.getEmail());
        User user = userRepository.findUserByEmail(loginRequest.getEmail());


        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            throw new UserNotVerifiedException("Authentication unsuccessful");
        }


        String accessToken = jwtUtil.createJwt.apply((User) authentication.getPrincipal());

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setStatus("success");
        successResponse.setMessage("Login Successful");

        SignupResponse signupResponse = new SignupResponse();
        signupResponse.setUserId(user.getUserId());
        signupResponse.setFirstName(user.getFirstName());
        signupResponse.setLastName(user.getLastName());
        signupResponse.setEmail(user.getEmail());
        signupResponse.setPassword(user.getPassword());
        signupResponse.setPhone(user.getPhone());

        DataResponse data = new DataResponse();
        data.setAccessToken(accessToken);
        data.setUser(signupResponse);

        successResponse.setData(data);

        return successResponse;
    }



    @Transactional()
    public SuccessResponse getUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String username = authentication.getName();
        User loggedInUser = userRepository.findUserByEmail(username);
        User user = userRepository.findUserByUserId(id);

        if(!loggedInUser.equals(user)) {
            throw new UserNotVerifiedException("User is not authenticated");
        }

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setStatus("success");
        successResponse.setMessage("User details gotten successfully");

        SignupResponse signupResponse = new SignupResponse();
        signupResponse.setUserId(user.getUserId());
        signupResponse.setFirstName(user.getFirstName());
        signupResponse.setLastName(user.getLastName());
        signupResponse.setEmail(user.getEmail());
        signupResponse.setPassword(user.getPassword());
        signupResponse.setPhone(user.getPhone());

        DataResponse data = new DataResponse();
        data.setUser(signupResponse);

        successResponse.setData(data);

        return successResponse;
    }


}
