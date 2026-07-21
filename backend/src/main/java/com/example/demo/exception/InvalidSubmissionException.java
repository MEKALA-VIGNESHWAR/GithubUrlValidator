package com.example.demo.exception;

public class InvalidSubmissionException extends RuntimeException {
    public InvalidSubmissionException(String message) { 
        super(message); 
    }
}