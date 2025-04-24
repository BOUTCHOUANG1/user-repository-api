package com.nathan.usermanagementapi.controller;

import com.nathan.usermanagementapi.dto.MessageResponse;
import com.nathan.usermanagementapi.dto.UpdateUserRequest;
import com.nathan.usermanagementapi.dto.UserDto;
import com.nathan.usermanagementapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user operations.
 * This includes listing, retrieving, updating, and deleting users.
 * All endpoints in this controller require JWT authentication.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "API for user CRUD operations - requires authentication. First login via /api/auth/login to get a token, then click 'Authorize' button and input the token as 'Bearer your-token-here'")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    /**
     * User service for operations on user data.
     */
    @Autowired
    private UserService userService;

    /**
     * Retrieves all users in the system.
     * This endpoint returns a list of all registered users.
     *
     * @return ResponseEntity containing list of all user DTOs
     */
    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all registered users in the system. Requires authentication.",
            tags = {"User Management"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of users retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions",
                    content = @Content
            )
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Retrieves a specific user by ID.
     * This endpoint returns the details of a user identified by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return ResponseEntity containing the requested user's details
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a specific user's details by their unique identifier. Requires authentication.",
            tags = {"User Management"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found - No user exists with the provided ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions",
                    content = @Content
            )
    })
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Updates an existing user with provided data.
     * This endpoint allows updating a user's name, email, and/or password.
     *
     * @param id The ID of the user to update
     * @param updateUserRequest DTO containing the fields to update
     * @return ResponseEntity containing the updated user's details
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update user",
            description = "Updates a user's information (name, email, and/or password). " +
                    "Only provided fields will be updated. Requires authentication.",
            tags = {"User Management"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - Invalid input data or validation failed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found - No user exists with the provided ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions",
                    content = @Content
            )
    })
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID of the user to update", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated user details - only include fields that need to be updated", required = true)
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {

        UserDto updatedUser = userService.updateUser(id, updateUserRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Deletes a user by ID.
     * This endpoint permanently removes a user from the system.
     *
     * @param id The ID of the user to delete
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Permanently removes a user from the system. Requires authentication.",
            tags = {"User Management"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found - No user exists with the provided ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions",
                    content = @Content
            )
    })
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true, example = "1")
            @PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }
}