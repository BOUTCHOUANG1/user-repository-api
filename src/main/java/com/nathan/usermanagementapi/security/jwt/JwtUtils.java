package com.nathan.usermanagementapi.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Component class responsible for managing JWT operations such as token generation, retrieval of
 * username from token, and token validation.
 */
@Component // Indicates that this class is a Spring-managed component.
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}") // Injects the JWT secret key from application properties.
    private String jwtSecret;

    @Value("${app.jwt.expiration}") // Injects the JWT expiration time in milliseconds from application properties.
    private int jwtExpirationMs;

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authentication the authentication object containing the user's details.
     * @return a signed JWT token as a String.
     * @throws RuntimeException if an error occurs during token creation.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        try {
            return JWT.create()
                    .withSubject(userPrincipal.getUsername())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs))
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (JWTCreationException exception) {
            logger.error("JWT token creation failed: {}", exception.getMessage());
            throw new RuntimeException("Error creating JWT token", exception);
        }
    }

    /**
     * Retrieves the username from the provided JWT token.
     *
     * @param token the JWT token from which to extract the username.
     * @return the username associated with the token.
     * @throws RuntimeException if the token verification fails.
     */
    public String getUsernameFromJwtToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            logger.error("JWT verification failed: {}", exception.getMessage());
            throw new RuntimeException("Error verifying JWT token", exception);
        }
    }

    /**
     * Validates the provided JWT token.
     *
     * @param authToken the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(authToken);
            return true;
        } catch (JWTVerificationException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}