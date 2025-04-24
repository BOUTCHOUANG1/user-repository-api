package com.nathan.usermanagementapi.security;

import com.nathan.usermanagementapi.security.jwt.AuthEntryPointJwt;
import com.nathan.usermanagementapi.security.jwt.AuthTokenFilter;
import com.nathan.usermanagementapi.security.jwt.JwtUtils;
import com.nathan.usermanagementapi.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security.
 *
 * This class sets up the security configuration for the application, including:
 * - Authentication provider setup
 * - Password encoding
 * - Security filter chain configuration
 * - JWT authentication filter
 * - Authorization rules for endpoints
 */
@Configuration
@EnableWebSecurity // Enables web security
@EnableMethodSecurity // Enables method-level security like @PreAuthorize
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Creates the JWT authentication filter bean.
     * This filter intercepts each request to check for valid JWT tokens
     * and sets up authentication in the security context if a valid token is found.
     *
     * @return The JWT authentication filter
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    /**
     * Creates the authentication provider bean.
     * This provider uses the custom UserDetailsService for user authentication
     * and the password encoder for password validation.
     *
     * @return The DAO authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Set the custom UserDetailsService
        authProvider.setUserDetailsService(userDetailsService);
        // Set the password encoder
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Creates the authentication manager bean.
     * This manager handles authentication requests and delegates to the appropriate provider.
     *
     * @param authConfig The authentication configuration
     * @return The authentication manager
     * @throws Exception If an error occurs during manager creation
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Creates the password encoder bean.
     * BCrypt is a strong hashing function designed for password storage.
     *
     * @return The BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain.
     * This method defines how security is applied to HTTP requests, including:
     * - CSRF protection
     * - Exception handling
     * - Session management
     * - Authorization rules
     * - Authentication provider
     * - JWT filter
     *
     * @param http The HttpSecurity to configure
     * @return The built security filter chain
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross Site Request Forgery) since we're using JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Configure exception handling for unauthorized requests
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

                // Use stateless sessions (required for JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure authorization rules
                .authorizeHttpRequests(auth ->
                        auth
                                // Permit access to authentication endpoints without authentication
                                .requestMatchers("/api/auth/**").permitAll()

                                // Permit access to Swagger/OpenAPI endpoints without authentication
                                .requestMatchers("/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/webjars/**").permitAll() // Additional Swagger resources
                                .requestMatchers("/v3/api-docs/**").permitAll() // OpenAPI documentation

                                // Require authentication for all other requests
                                .anyRequest().authenticated()
                );

        // Add the authentication provider
        http.authenticationProvider(authenticationProvider());

        // Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // Build and return the security filter chain
        return http.build();
    }
}