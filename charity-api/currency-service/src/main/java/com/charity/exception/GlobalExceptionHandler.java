package com.charity.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidDataException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(getExceptionDetails(exception, request), BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Object> handleInternalServerError(Exception exception, WebRequest request) {
        return new ResponseEntity<>(getExceptionDetails(exception, request), INTERNAL_SERVER_ERROR);
    }

    public ExceptionDetails getExceptionDetails(Exception exception, WebRequest request) {
        return new ExceptionDetails(
                exception.getMessage(), request.getDescription(false), ZonedDateTime.now());
    }
}
