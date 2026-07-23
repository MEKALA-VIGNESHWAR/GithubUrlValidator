package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "HackForge Enterprise Operating System API");
        status.put("status", "UP");
        status.put("version", "1.0.0");
        status.put("swaggerDocs", "/swagger-ui.html");
        status.put("authEndpoints", "/api/auth");
        status.put("submissionsApi", "/api/submissions");
        status.put("hackathonsApi", "/api/hackathons");
        status.put("tasksApi", "/api/tasks");
        status.put("globalSearchApi", "/api/search?q=");
        return ResponseEntity.ok(status);
    }
}
