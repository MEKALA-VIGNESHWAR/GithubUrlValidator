package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/support", "/api/v1/support"})
public class SupportController {

    @PostMapping("/tickets")
    public ResponseEntity<Map<String, Object>> createTicket(@RequestBody Map<String, String> request) {
        String email = request.getOrDefault("email", "anonymous@user.com");
        String subject = request.getOrDefault("subject", "No subject");
        String message = request.getOrDefault("message", "");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Support ticket received. Our team will contact you shortly.");
        response.put("ticketId", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
