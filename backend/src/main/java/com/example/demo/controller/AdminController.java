package com.example.demo.controller;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.Submission;
import com.example.demo.enums.SubmissionStatus;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.SubmissionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SubmissionRepository submissionRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public AdminController(SubmissionRepository submissionRepository, FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/users/promote")
    public ResponseEntity<?> promoteUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String roleStr = request.get("role");

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            user.setRole(Role.valueOf(roleStr.toUpperCase()));
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "User role updated successfully", "username", username, "newRole", user.getRole().name()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid role specified"));
        }
    }

    @PatchMapping("/submissions/{id}/approve")
    public ResponseEntity<?> approveSubmission(@PathVariable Long id) {
        Submission sub = submissionRepository.findById(id).orElse(null);
        if (sub == null) return ResponseEntity.notFound().build();
        sub.setStatus(SubmissionStatus.APPROVED);
        submissionRepository.save(sub);
        return ResponseEntity.ok(sub);
    }

    @PatchMapping("/submissions/{id}/reject")
    public ResponseEntity<?> rejectSubmission(@PathVariable Long id) {
        Submission sub = submissionRepository.findById(id).orElse(null);
        if (sub == null) return ResponseEntity.notFound().build();
        sub.setStatus(SubmissionStatus.REJECTED);
        submissionRepository.save(sub);
        return ResponseEntity.ok(sub);
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> addFeedback(@RequestBody Feedback feedback) {
        Feedback saved = feedbackRepository.save(feedback);
        Submission sub = submissionRepository.findById(feedback.getSubmissionId()).orElse(null);
        if (sub != null) {
            sub.setJudgeComment(feedback.getComment());
            submissionRepository.save(sub);
        }
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv() {
        List<Submission> list = submissionRepository.findAll();
        StringBuilder csv = new StringBuilder("ID,TeamName,ProjectTitle,Category,Status,Stars,Forks\n");
        for (Submission s : list) {
            csv.append(s.getId()).append(",")
               .append("\"").append(s.getTeamName()).append("\",")
               .append("\"").append(s.getProjectTitle()).append("\",")
               .append(s.getCategory() != null ? s.getCategory() : "General").append(",")
               .append(s.getStatus()).append(",")
               .append(s.getStars() != null ? s.getStars() : 0).append(",")
               .append(s.getForks() != null ? s.getForks() : 0).append("\n");
        }

        byte[] content = csv.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=hackforge_submissions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(content);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf() {
        // PDF text-based export summary report
        List<Submission> list = submissionRepository.findAll();
        StringBuilder pdfText = new StringBuilder("=== HACKFORGE SUBMISSIONS SUMMARY REPORT ===\n\n");
        for (Submission s : list) {
            pdfText.append("Team: ").append(s.getTeamName())
                   .append(" | Project: ").append(s.getProjectTitle())
                   .append(" | Category: ").append(s.getCategory())
                   .append(" | Status: ").append(s.getStatus()).append("\n");
        }

        byte[] content = pdfText.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=hackforge_report.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(content);
    }
}
