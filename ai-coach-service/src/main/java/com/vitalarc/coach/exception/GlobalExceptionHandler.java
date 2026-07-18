package com.vitalarc.coach.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(ErrorResponse.ofValidation(fieldErrors));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleUpstreamFailure(RestClientException ex) {
        // Covers both workout-service being unreachable and the Claude API failing -
        // either way the client gets a clean 502, not a raw stack trace.
        return ResponseEntity.status(502)
                .body(ErrorResponse.of(502, "Bad Gateway", "A dependent service is currently unavailable. Please try again shortly."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(500, "Internal Server Error", "Something went wrong. Please try again."));
    }
}