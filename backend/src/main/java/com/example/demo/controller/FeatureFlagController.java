package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/flags", "/api/v1/flags"})
public class FeatureFlagController {

    @GetMapping
    public ResponseEntity<Map<String, Boolean>> getFeatureFlags() {
        Map<String, Boolean> flags = new HashMap<>();
        flags.put("ENABLE_AI_REVIEWS", true);
        flags.put("ENABLE_CERTIFICATE_GENERATION", true);
        flags.put("ENABLE_LIVE_LEADERBOARD", true);
        flags.put("ENABLE_OAUTH2_LOGIN", true);
        flags.put("MAINTENANCE_MODE", false);
        return ResponseEntity.ok(flags);
    }
}
