package com.example.demo.service.impl;

import com.example.demo.dto.response.AnalyticsResponseDTO;
import com.example.demo.entity.Submission;
import com.example.demo.enums.SubmissionStatus;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.service.AnalyticsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final SubmissionRepository repository;

    public AnalyticsServiceImpl(SubmissionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = "analyticsCache", key = "'globalAnalytics'")
    public AnalyticsResponseDTO getAnalytics() {
        long total = repository.count();
        long approved = repository.countByStatus(SubmissionStatus.APPROVED);
        long pending = repository.countByStatus(SubmissionStatus.PENDING);
        long rejected = repository.countByStatus(SubmissionStatus.REJECTED);

        List<Submission> allSubmissions = repository.findAll();

        Map<String, Long> topTechnologies = allSubmissions.stream()
                .filter(s -> s.getTechStack() != null && !s.getTechStack().trim().isEmpty())
                .collect(Collectors.groupingBy(s -> s.getTechStack().trim(), Collectors.counting()));

        Map<String, Long> topColleges = allSubmissions.stream()
                .filter(s -> s.getCollege() != null && !s.getCollege().trim().isEmpty())
                .collect(Collectors.groupingBy(s -> s.getCollege().trim(), Collectors.counting()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Long> trends = allSubmissions.stream()
                .filter(s -> s.getSubmittedAt() != null)
                .collect(Collectors.groupingBy(s -> s.getSubmittedAt().format(formatter), Collectors.counting()));

        AnalyticsResponseDTO response = new AnalyticsResponseDTO(total, approved, pending, rejected);
        response.setTopTechnologies(topTechnologies);
        response.setTopColleges(topColleges);
        response.setSubmissionTrends(trends);

        return response;
    }
}
