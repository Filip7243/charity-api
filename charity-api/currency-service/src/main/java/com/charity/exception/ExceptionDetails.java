package com.charity.exception;

import java.time.ZonedDateTime;

public record ExceptionDetails(String message, String details, ZonedDateTime timestamp) {

    public ExceptionDetails {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
        if (details == null || details.isBlank()) {
            throw new IllegalArgumentException("Details cannot be null or blank");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
    }
}
