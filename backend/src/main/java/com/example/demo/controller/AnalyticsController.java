package com.example.demo.controller;

import com.example.demo.entity.Submission;
import com.example.demo.enums.SubmissionStatus;
import com.example.demo.repository.SubmissionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final SubmissionRepository submissionRepository;

    public AnalyticsController(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAnalytics() {
        List<Submission> all = submissionRepository.findAll();
        long total = all.size();
        long approved = all.stream().filter(s -> s.getStatus() == SubmissionStatus.APPROVED).count();
        long pending = all.stream().filter(s -> s.getStatus() == SubmissionStatus.PENDING).count();
        long rejected = all.stream().filter(s -> s.getStatus() == SubmissionStatus.REJECTED).count();

        double approvalRate = total > 0 ? (double) approved / total * 100 : 0.0;

        Map<String, Long> categoryDistribution = all.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getCategory() != null ? s.getCategory() : "General",
                        Collectors.counting()
                ));

        Map<String, Object> data = new HashMap<>();
        data.put("totalSubmissions", total);
        data.put("approvedCount", approved);
        data.put("pendingCount", pending);
        data.put("rejectedCount", rejected);
        data.put("approvalRate", Math.round(approvalRate * 10.0) / 10.0);
        data.put("categoryDistribution", categoryDistribution);
        data.put("weeklyTrends", Map.of("Week 1", 12, "Week 2", 24, "Week 3", 36));
        data.put("topColleges", List.of("MIT", "Stanford", "Oxford", "IIT"));
        data.put("topTechnologies", List.of("React", "Spring Boot", "PostgreSQL", "TensorFlow"));

        return ResponseEntity.ok(data);
    }
}
