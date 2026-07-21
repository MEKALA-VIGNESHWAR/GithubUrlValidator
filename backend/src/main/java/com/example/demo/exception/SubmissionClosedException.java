package com.example.demo.exception;

public class SubmissionClosedException extends RuntimeException {
    public SubmissionClosedException(String message) { 
        super(message); 
    }
}