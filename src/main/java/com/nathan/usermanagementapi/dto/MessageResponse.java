package com.nathan.usermanagementapi.dto;

import lombok.AllArgsConstructor; // Lombok annotation to generate an all-args constructor.
import lombok.Data; // Lombok annotation to generate getters, setters, toString, and other boilerplate code.
import lombok.NoArgsConstructor; // Lombok annotation to generate a no-args constructor.

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Represents a message response object.
 * This class holds a message to be sent as a response.
 */
public class MessageResponse {

    private String message; // The message content to be sent in the response.

}
