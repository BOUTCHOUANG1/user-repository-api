package com.nathan.usermanagementapi.controller;

import com.nathan.usermanagementapi.dto.JwtResponse;
import com.nathan.usermanagementapi.dto.LoginRequest;
import com.nathan.usermanagementapi.dto.MessageResponse;
import com.nathan.usermanagementapi.dto.SignupRequest;
import com.nathan.usermanagementapi.dto.UserDto;
import com.nathan.usermanagementapi.repository.UserRepository;
import com.nathan.usermanagementapi.security.jwt.JwtUtils;
import com.nathan.usermanagementapi.security.services.UserDetailsImpl;
import com.nathan.usermanagementapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication operations.
 * This includes user registration (signup) and login functionalities.
 * Authentication endpoints are accessible without prior authentication.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for user authentication operations including registration and login")
public class AuthController {

    /**
     * Authentication manager for user login validation.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Repository for checking if user email already exists.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * JWT utility for token generation and validation.
     */
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * User service for creating new users.
     */
    @Autowired
    private UserService userService;

    /**
     * Authenticates a user and returns a JWT token.
     * This endpoint validates user credentials and generates a JWT token if valid.
     *
     * @param loginRequest DTO containing login credentials (email and password)
     * @return ResponseEntity containing JWT token and user details if authentication is successful
     */
    @PostMapping("/login")
    @Operation(
            summary = "Authenticate a user",
            description = "Validates user credentials (email and password) and returns a JWT token for authorized access.",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request (missing required fields or validation failed)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
            )
    })
    public ResponseEntity<?> authenticateUser(
            @Parameter(description = "Login credentials including email and password", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {

        // Authenticate user with provided credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Get user details from authenticated user
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Return JWT token and user details
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
        ));
    }

    /**
     * Registers a new user in the system.
     * This endpoint creates a new user account with the provided details.
     *
     * @param signupRequest DTO containing user registration details (name, email, password)
     * @return ResponseEntity with success message or error details
     */
    @PostMapping("/signup")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with name, email, and password. The password is encrypted before storing.",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - Email already in use or validation failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
            )
    })
    public ResponseEntity<?> registerUser(
            @Parameter(description = "Signup details including name, email, and password", required = true)
            @Valid @RequestBody SignupRequest signupRequest) {

        // Check if email is already in use
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user account
        UserDto userDto = userService.createUser(signupRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}