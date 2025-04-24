package com.nathan.usermanagementapi.security.services;

import com.nathan.usermanagementapi.model.User;
import com.nathan.usermanagementapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class responsible for loading user details from the database.
 * Implements the UserDetailsService interface of Spring Security.
 * 
 * @author Nathan
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * The user repository that provides the user data.
     * Autowired by Spring, so it will be injected when the application context is loaded.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a user by their email.
     * 
     * @param email the email of the user to load
     * @return a UserDetails object representing the user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the user by their email
        User user = userRepository.findByEmail(email)
                // If the user is not found, throw an exception
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Build a UserDetails object from the user
        return UserDetailsImpl.build(user);
    }
}