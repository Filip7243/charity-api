package com.charity.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Object> handleBadRequestException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(getExceptionDetails(exception, request), BAD_REQUEST);
    }

    @ExceptionHandler({PdfGenerationException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleInternalServerError(Exception exception, WebRequest request) {
        return new ResponseEntity<>(getExceptionDetails(exception, request), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResourceNotFound.class})
    public ResponseEntity<Object> handleNotFoundException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(getExceptionDetails(exception, request), NOT_FOUND);
    }

    public ExceptionDetails getExceptionDetails(Exception exception, WebRequest request) {
        return new ExceptionDetails(
                exception.getMessage(), request.getDescription(false), ZonedDateTime.now());
    }
}
