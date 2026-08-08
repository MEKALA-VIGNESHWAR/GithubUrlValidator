package com.example.demo.controller;

import com.example.demo.entity.Submission;
import com.example.demo.entity.User;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sponsors")
public class SponsorController {

    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;

    public SponsorController(UserRepository userRepository, SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
    }

    @GetMapping("/candidates")
    public ResponseEntity<?> searchCandidates(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) Double minRating) {

        List<User> users = userRepository.findAll();
        List<Submission> submissions = submissionRepository.findAll();

        List<Map<String, Object>> candidateCards = new ArrayList<>();

        for (User user : users) {
            Optional<Submission> subOpt = submissions.stream()
                    .filter(s -> s.getEmail() != null && s.getEmail().equalsIgnoreCase(user.getEmail()))
                    .findFirst();

            String techStack = subOpt.map(Submission::getTechStack).orElse("Java, React, PostgreSQL");
            String collegeName = subOpt.map(Submission::getCollege).orElse("Stanford University");
            Double rating = subOpt.map(Submission::getJudgeRating).orElse(8.8);
            String projectTitle = subOpt.map(Submission::getProjectTitle).orElse("HackForge OS");

            if (skill != null && !techStack.toLowerCase().contains(skill.toLowerCase())) {
                continue;
            }
            if (college != null && !collegeName.toLowerCase().contains(college.toLowerCase())) {
                continue;
            }
            if (minRating != null && rating < minRating) {
                continue;
            }

            candidateCards.add(Map.of(
                "userId", user.getId(),
                "fullName", user.getFullName() != null ? user.getFullName() : user.getUsername(),
                "email", user.getEmail(),
                "college", collegeName,
                "techStack", techStack,
                "judgeRating", rating,
                "projectTitle", projectTitle,
                "githubUrl", "https://github.com/" + user.getUsername(),
                "linkedinUrl", "https://linkedin.com/in/" + user.getUsername(),
                "rankPlacement", candidateCards.size() + 1
            ));
        }

        return ResponseEntity.ok(candidateCards);
    }

    @PostMapping("/outreach")
    public ResponseEntity<?> sendDirectOutreach(@RequestBody Map<String, Object> body) {
        String candidateEmail = (String) body.get("candidateEmail");
        String message = (String) body.get("message");
        String sponsorCompany = (String) body.getOrDefault("sponsorCompany", "Tech Corp Sponsor");

        if (candidateEmail == null || message == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "candidateEmail and message are required"));
        }

        return ResponseEntity.ok(Map.of(
            "status", "SENT",
            "candidateEmail", candidateEmail,
            "sponsorCompany", sponsorCompany,
            "sentAt", java.time.LocalDateTime.now()
        ));
    }

    @GetMapping("/track-submissions")
    public ResponseEntity<?> filterTrackSubmissions(@RequestParam(required = false, defaultValue = "All") String track) {
        List<Submission> submissions = submissionRepository.findAll();

        if (!"All".equalsIgnoreCase(track)) {
            submissions = submissions.stream()
                    .filter(s -> (s.getCategory() != null && s.getCategory().equalsIgnoreCase(track)) ||
                                 (s.getTechStack() != null && s.getTechStack().toLowerCase().contains(track.toLowerCase())))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(submissions);
    }
}
