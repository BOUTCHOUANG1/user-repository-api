package com.nathan.usermanagementapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a signup request with user details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    // Name of the user, must be between 3 and 50 characters.
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    // Email address of the user.
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    // Password of the user, must be between 6 and 40 characters.
    private String password;
}