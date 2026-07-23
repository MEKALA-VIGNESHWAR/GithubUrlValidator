package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ErrorResponse(400, "Validation Failed", errors.toString(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidSubmissionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(InvalidSubmissionException ex) {
        return new ErrorResponse(400, "Bad Request", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(SubmissionClosedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleClosed(SubmissionClosedException ex) {
        return new ErrorResponse(403, "Forbidden", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse(404, "Not Found", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(ConflictException ex) {
        return new ErrorResponse(409, "Conflict", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentials(BadCredentialsException ex) {
        return new ErrorResponse(401, "Unauthorized", "Invalid email or password", LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return new ErrorResponse(403, "Access Denied", "You do not have permission to access this resource.", LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGlobalException(Exception ex) {
        logger.error("Unhandled internal server exception:", ex);
        return new ErrorResponse(500, "Internal Server Error", "An internal error occurred. Please contact support.", LocalDateTime.now());
    }
}
