package com.example.demo.controller;

import com.example.demo.dto.response.AnalyticsResponseDTO;
import com.example.demo.service.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AnalyticsResponseDTO getAnalytics() {
        return analyticsService.getAnalytics();
    }
}
