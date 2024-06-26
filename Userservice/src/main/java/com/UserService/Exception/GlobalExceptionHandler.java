package com.UserService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice // Indicates that this class provides centralized exception handling for all Controller classes
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler  {

    // Exception handler for ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage()+ request.getDescription(true), HttpStatus.NOT_FOUND);
    }

    // Exception handler for other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandled(Exception ex, WebRequest request) {
        return new ResponseEntity<>("not found"+ request.getDescription(true), HttpStatus.NOT_FOUND);
    }
}
