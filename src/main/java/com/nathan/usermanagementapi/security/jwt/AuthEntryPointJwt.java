package com.nathan.usermanagementapi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * Handles unauthorized access attempts by responding with a JSON error message.
     *
     * <p>
     * This method is triggered anytime an unauthenticated user requests a secured HTTP resource
     * and an AuthenticationException is thrown. It logs the error message and sends back an
     * HTTP 401 Unauthorized responses along with a JSON body containing details about the
     * unauthorized error, including status, error, message, and the request path.
     * </p>
     *
     * @param request       the HttpServletRequest that resulted in an AuthenticationException
     * @param response      the HttpServletResponse to send the error response
     * @param authException the exception that caused the invocation
     * @throws IOException if an input or output exception occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
