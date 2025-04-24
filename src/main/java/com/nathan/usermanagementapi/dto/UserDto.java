/**
 * Represents a Data Transfer Object (DTO) for a user.
 * This class holds user-related information for transferring data between layers.
 */
package com.nathan.usermanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The UserDto class represents a user data transfer object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The date when the user was created.
     */
    private Date createdAt;

    /**
     * The date when the user was last updated.
     */
    private Date updatedAt;
}