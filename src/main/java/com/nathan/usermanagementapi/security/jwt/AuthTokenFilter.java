package com.nathan.usermanagementapi.security.jwt;

import com.nathan.usermanagementapi.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that is responsible for extracting the JWT token from the request
 * and authenticating the user based on the token.
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * The service that is responsible for validating the JWT token and
     * retrieving the user details from the database.
     */
    private final JwtUtils jwtUtils;

    /**
     * The service that is responsible for retrieving the user details from
     * the database based on the username.
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructor that takes the jwtUtils and userDetailsService as parameters.
     * @param jwtUtils the service that is responsible for validating the JWT
     *                 token and retrieving the user details from the database.
     * @param userDetailsService the service that is responsible for retrieving
     *                            the user details from the database based on
     *                            the username.
     */
    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * The main method of the filter that is responsible for extracting the
     * JWT token from the request and authenticating the user based on the
     * token.
     * @param request the request that contains the JWT token.
     * @param response the response that is sent back to the client.
     * @param filterChain the filter chain that is used to continue the
     *                    processing of the request.
     * @throws ServletException if an error occurs during the processing of
     * the request.
     * @throws IOException if an error occurs during the reading of the
     *                     request or the writing of the response.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Method that is responsible for extracting the JWT token from the request.
     * @param request the request that contains the JWT token.
     * @return the JWT token that is extracted from the request or null if the
     * token is not found.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}