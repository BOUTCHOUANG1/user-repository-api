package com.nathan.usermanagementapi.repository;

import com.nathan.usermanagementapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a User by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the User if found, empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a User with the given email address exists.
     *
     * @param email the email address to check
     * @return true if a User exists with the email, false otherwise
     */
    Boolean existsByEmail(String email);
}
