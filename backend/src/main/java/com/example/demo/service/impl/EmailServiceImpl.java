package com.example.demo.service.impl;

import com.example.demo.enums.SubmissionStatus;
import com.example.demo.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    public EmailServiceImpl(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSubmissionConfirmationEmail(String toEmail, String teamName, String projectTitle) {
        String subject = "Hackathon Project Submission Confirmed - " + projectTitle;
        String content = "<h2>Submission Received!</h2>"
                + "<p>Hello <b>" + teamName + "</b>,</p>"
                + "<p>Your hackathon project <b>" + projectTitle + "</b> has been successfully submitted and is under review.</p>"
                + "<p>Thank you for participating!</p>";

        sendEmail(toEmail, subject, content);
    }

    @Override
    public void sendSubmissionStatusEmail(String toEmail, String teamName, String projectTitle, SubmissionStatus status) {
        String subject = "Hackathon Submission Update - " + projectTitle;
        String content = "<h2>Project Status Update</h2>"
                + "<p>Hello <b>" + teamName + "</b>,</p>"
                + "<p>Your submission for <b>" + projectTitle + "</b> has been updated to: <b>" + status.name() + "</b>.</p>";

        sendEmail(toEmail, subject, content);
    }

    private void sendEmail(String toEmail, String subject, String htmlBody) {
        if (mailSender == null) {
            log.info("[MOCK EMAIL] To: {} | Subject: {} | Body: {}", toEmail, subject, htmlBody);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }
}
