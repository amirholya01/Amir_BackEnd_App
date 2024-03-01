package com.amir.controller;

import com.amir.data.request.LoginRequest;
import com.amir.data.request.SignupRequest;
import com.amir.data.response.JwtResponse;
import com.amir.data.response.ResponseMessage;
import com.amir.domain.Role;
import com.amir.domain.User;
import com.amir.enums.ERole;
import com.amir.repository.RoleRepository;
import com.amir.repository.UserRepository;
import com.amir.security.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;



    // Endpoint for user authentication

    @CrossOrigin
    @PostMapping("/signin")
    @Operation(summary = "Authenticate user")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Successfully authenticated",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<?> authenticateUser(@Parameter(description = "Login credentials", required = true)@RequestBody LoginRequest loginRequest){

        // Authenticate user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtProvider.generateJwtToken(authentication);

        // Get UserDetails from authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Initialize result as error
        String result = "error";

        // Check if JWT is generated successfully
        if(!jwt.isEmpty()){
            result = "success";
        }
        String id = null;

        // Fetch user details from database
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if(user.isPresent()){
            id = user.get().getId();

            // Check if user is inactive
            if(user.get().getActive() == 2){
                result = "notActive";
            }
        }

        // Return JWT response
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities(), result, id));
    }


    // Endpoint for user registration


    @CrossOrigin
    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = ResponseMessage.class)))
    })
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest){
        // Check if username is empty or null
        if(signupRequest.getUsername().isEmpty() || signupRequest.getUsername() == null){
            System.out.println("Username is not allowed to be empty");
        }

        // Check if user already exists with the provided email or username
        if(userRepository.existsByEmail(signupRequest.getEmail()) || userRepository.existsByUsername(signupRequest.getUsername())){
            Optional<User> user = userRepository.findByUsername(signupRequest.getUsername());
        }

        // Create a new user entity with provided details
        User user = new User(
                signupRequest.getUsername(),
                encoder.encode(signupRequest.getPassword()),
                signupRequest.getEmail(),
                signupRequest.getActive()
        );

        // Set roles for the user
        Set<Role> roles = new HashSet<>();
        if(signupRequest.getRoles() != null){
            Set<String> strRoles = signupRequest.getRoles();
            strRoles.forEach(role -> {
                switch (role){
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role pmRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(pmRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(userRole);
                }
            });
        }else {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            roles.add(userRole);
        }

        // Set roles for the user and save to repository
        user.setAuthorities(roles);
        userRepository.save(user);

        // Return success response
        return new ResponseEntity<>(
                new ResponseMessage("User " + signupRequest.getUsername() + " is registered successfully!"),
                HttpStatus.OK);
    }


}
