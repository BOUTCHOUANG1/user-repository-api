package com.nathan.usermanagementapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception thrown when a requested resource is not found.
 * This exception is automatically mapped to HTTP 404 (Not Found) status.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Maps this exception to HTTP 404 status
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new resource not found exception with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}