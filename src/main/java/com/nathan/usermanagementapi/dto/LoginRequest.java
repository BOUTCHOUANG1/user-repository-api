package com.nathan.usermanagementapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a login request with user credentials.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    // The email address of the user, cannot be blank.
    @NotBlank
    private String email;

    // The password of the user, cannot be blank.
    @NotBlank
    private String password;
}