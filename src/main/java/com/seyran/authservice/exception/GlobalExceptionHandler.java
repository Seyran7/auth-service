package com.seyran.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        ErrorResponse errorResponse =ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException e) {
       ErrorResponse response =ErrorResponse.builder()
               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
               .message("Something went wrong")
               .timestamp(LocalDateTime.now())
               .build();
       return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse errorResponse= ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Something went wrong")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
