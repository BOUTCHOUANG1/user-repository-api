package com.nathan.usermanagementapi.service;

import com.nathan.usermanagementapi.dto.SignupRequest;
import com.nathan.usermanagementapi.dto.UpdateUserRequest;
import com.nathan.usermanagementapi.dto.UserDto;

import java.util.List;

/**
 * Service interface for user management operations.
 *
 * This interface defines the methods for user CRUD operations:
 * - Creating a new user
 * - Retrieving users (all users or a specific user)
 * - Updating an existing user
 * - Deleting a user
 */
public interface UserService {

    /**
     * Creates a new user from registration request data.
     *
     * @param signupRequest DTO containing user registration information
     * @return DTO of the created user
     */
    UserDto createUser(SignupRequest signupRequest);

    /**
     * Retrieves all users in the system.
     *
     * @return List of all user DTOs
     */
    List<UserDto> getAllUsers();

    /**
     * Retrieves a specific user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return DTO of the requested user
     * @throws com.nathan.usermanagementapi.exception.ResourceNotFoundException if user does not exist
     */
    UserDto getUserById(Long id);

    /**
     * Updates an existing user with the provided data.
     *
     * @param id The ID of the user to update
     * @param updateUserRequest DTO containing the update information
     * @return DTO of the updated user
     * @throws com.nathan.usermanagementapi.exception.ResourceNotFoundException if user does not exist
     */
    UserDto updateUser(Long id, UpdateUserRequest updateUserRequest);

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete
     * @throws com.nathan.usermanagementapi.exception.ResourceNotFoundException if user does not exist
     */
    void deleteUser(Long id);
}