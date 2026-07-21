package com.example.demo.service;

import com.example.demo.enums.SubmissionStatus;

public interface EmailService {
    void sendSubmissionConfirmationEmail(String toEmail, String teamName, String projectTitle);
    void sendSubmissionStatusEmail(String toEmail, String teamName, String projectTitle, SubmissionStatus status);
}
