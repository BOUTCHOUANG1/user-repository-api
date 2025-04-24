package com.nathan.usermanagementapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user update requests.
 * Contains the fields that can be updated for a user.
 * All fields are optional - only provided fields will be updated.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    /**
     * User's name (optional)
     */
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    /**
     * User's email (optional)
     */
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * User's password (optional)
     */
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;
}
