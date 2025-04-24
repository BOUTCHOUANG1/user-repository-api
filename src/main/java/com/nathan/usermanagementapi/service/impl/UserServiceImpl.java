
package com.nathan.usermanagementapi.service.impl;

import com.nathan.usermanagementapi.dto.SignupRequest;
import com.nathan.usermanagementapi.dto.UpdateUserRequest;
import com.nathan.usermanagementapi.dto.UserDto;
import com.nathan.usermanagementapi.exception.ResourceNotFoundException;
import com.nathan.usermanagementapi.model.User;
import com.nathan.usermanagementapi.repository.UserRepository;
import com.nathan.usermanagementapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface.
 *
 * This class provides the business logic for user management operations.
 */
@Service // Marks this class as a Spring service component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository; // Repository for database operations

    @Autowired
    private PasswordEncoder passwordEncoder; // Encoder for password hashing

    /**
     * Creates a new user from the registration request data.
     * The password is encrypted using BCrypt before storing.
     *
     * @param signupRequest DTO containing user registration information
     * @return DTO of the created user
     */
    @Override
    @Transactional // Ensures database operations are executed in a transaction
    public UserDto createUser(SignupRequest signupRequest) {
        // Create new user
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());

        // Encrypt the password before storing
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Convert and return as DTO
        return mapToDto(savedUser);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return List of all user DTOs
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Convert each user to DTO
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return DTO of the requested user
     * @throws ResourceNotFoundException if user does not exist
     */
    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapToDto(user);
    }

    /**
     * Updates an existing user with the provided data.
     * Only fields that are provided in the request will be updated.
     *
     * @param id The ID of the user to update
     * @param updateUserRequest DTO containing the update information
     * @return DTO of the updated user
     * @throws ResourceNotFoundException if user does not exist
     */
    @Override
    @Transactional
    public UserDto updateUser(Long id, UpdateUserRequest updateUserRequest) {
        // Find the user by ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update name if provided
        if (updateUserRequest.getName() != null) {
            user.setName(updateUserRequest.getName());
        }

        // Update email if provided and different from current
        if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().equals(user.getEmail())) {
            // Check if email already exists
            if (userRepository.existsByEmail(updateUserRequest.getEmail())) {
                throw new IllegalArgumentException("Email is already in use");
            }
            user.setEmail(updateUserRequest.getEmail());
        }

        // Update password if provided
        if (updateUserRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        // Save the updated user
        User updatedUser = userRepository.save(user);

        return mapToDto(updatedUser);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete
     * @throws ResourceNotFoundException if user does not exist
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        // Check if user exists
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Delete the user
        userRepository.delete(user);
    }

    /**
     * Helper method to map User entity to UserDto.
     * This prevents sensitive information (like password) from being exposed.
     *
     * @param user The user entity to convert
     * @return DTO representation of the user
     */
    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}