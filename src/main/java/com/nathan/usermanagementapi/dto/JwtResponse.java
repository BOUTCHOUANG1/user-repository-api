package com.nathan.usermanagementapi.dto;

// Represents a JwtResponse object that contains JWT authentication details.
import lombok.AllArgsConstructor; // Lombok annotation to generate an all-args constructor.
import lombok.Data; // Lombok annotation to generate getters, setters, and other boilerplate code.
import lombok.NoArgsConstructor; // Lombok annotation to generate a no-args constructor.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    // The JWT token received upon successful authentication.
    private String token;

    // The type of token, defaulted to "Bearer" for Bearer token authentication.
    private String type = "Bearer";

    // The unique identifier associated with the user.
    private Long id;

    // The name of the user.
    private String name;

    // The email address of the user.
    private String email;

    /**
     * Constructs a JwtResponse with the provided token, user ID, name, and email.
     *
     * @param token The JWT token.
     * @param id The user ID.
     * @param name The user's name.
     * @param email The user's email address.
     */
    public JwtResponse(String token, Long id, String name, String email) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
    }
}