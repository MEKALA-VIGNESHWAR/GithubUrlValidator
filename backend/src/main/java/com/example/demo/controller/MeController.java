package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.enums.SubmissionStatus;
import com.example.demo.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MeController {

    private final UserRepository userRepository;
    private final HackathonRepository hackathonRepository;
    private final SubmissionRepository submissionRepository;
    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;
    private final CertificateRepository certificateRepository;

    public MeController(UserRepository userRepository,
                        HackathonRepository hackathonRepository,
                        SubmissionRepository submissionRepository,
                        TaskRepository taskRepository,
                        NotificationRepository notificationRepository,
                        CertificateRepository certificateRepository) {
        this.userRepository = userRepository;
        this.hackathonRepository = hackathonRepository;
        this.submissionRepository = submissionRepository;
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
        this.certificateRepository = certificateRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            return userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username).orElse(null));
        }
        return null;
    }

    @GetMapping("/me/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        User user = getCurrentUser();
        Map<String, Object> res = new HashMap<>();

        if (user == null) {
            res.put("totalHackathons", hackathonRepository.count());
            res.put("activeProjects", submissionRepository.countByStatus(SubmissionStatus.APPROVED) + submissionRepository.countByStatus(SubmissionStatus.PENDING));
            res.put("totalSubmissions", submissionRepository.count());
            res.put("pendingTasks", taskRepository.count());
            res.put("userRole", "GUEST");
            res.put("hasRegisteredEvents", false);
            res.put("hasProjects", false);
            return ResponseEntity.ok(res);
        }

        List<Submission> userSubmissions = submissionRepository.findAll().stream()
                .filter(s -> (s.getEmail() != null && s.getEmail().equalsIgnoreCase(user.getEmail())) ||
                             (s.getLeaderName() != null && s.getLeaderName().equalsIgnoreCase(user.getUsername())) ||
                             (s.getTeamName() != null && s.getTeamName().equalsIgnoreCase(user.getUsername())))
                .collect(Collectors.toList());

        List<Hackathon> allEvents = hackathonRepository.findAll();
        List<TaskEntity> userTasks = taskRepository.findAll().stream()
                .filter(t -> t.getAssigneeName() != null && t.getAssigneeName().equalsIgnoreCase(user.getUsername()))
                .collect(Collectors.toList());

        res.put("userRole", user.getRole().name());
        res.put("username", user.getUsername());
        res.put("email", user.getEmail());
        res.put("totalHackathons", allEvents.size());
        res.put("userProjectsCount", userSubmissions.size());
        res.put("approvedProjectsCount", userSubmissions.stream().filter(s -> s.getStatus() == SubmissionStatus.APPROVED).count());
        res.put("pendingTasksCount", userTasks.size());
        res.put("hasRegisteredEvents", !allEvents.isEmpty());
        res.put("hasProjects", !userSubmissions.isEmpty());
        res.put("userProjects", userSubmissions);
        res.put("userTasks", userTasks);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/me/events")
    public ResponseEntity<List<Hackathon>> getMyEvents() {
        return ResponseEntity.ok(hackathonRepository.findAll());
    }

    @GetMapping("/me/projects")
    public ResponseEntity<List<Submission>> getMyProjects() {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.ok(submissionRepository.findAll());
        }

        List<Submission> userSubmissions = submissionRepository.findAll().stream()
                .filter(s -> (s.getEmail() != null && s.getEmail().equalsIgnoreCase(user.getEmail())) ||
                             (s.getLeaderName() != null && s.getLeaderName().equalsIgnoreCase(user.getUsername())) ||
                             (s.getTeamName() != null && s.getTeamName().equalsIgnoreCase(user.getUsername())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userSubmissions);
    }

    @GetMapping("/me/archived")
    public ResponseEntity<List<Submission>> getMyArchived() {
        List<Submission> approved = submissionRepository.findAll().stream()
                .filter(s -> s.getStatus() == SubmissionStatus.APPROVED)
                .collect(Collectors.toList());

        return ResponseEntity.ok(approved);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<Map<String, Object>> getMyProfile() {
        User user = getCurrentUser();
        Map<String, Object> res = new HashMap<>();
        if (user == null) {
            res.put("username", "Guest");
            res.put("role", "PARTICIPANT");
            return ResponseEntity.ok(res);
        }

        res.put("id", user.getId());
        res.put("username", user.getUsername());
        res.put("email", user.getEmail());
        res.put("fullName", user.getFullName());
        res.put("role", user.getRole().name());
        res.put("university", user.getUniversity());
        res.put("skills", user.getSkills());
        res.put("bio", user.getBio());
        res.put("profilePicture", user.getProfilePicture());
        res.put("githubUrl", user.getGithubUrl());
        res.put("linkedinUrl", user.getLinkedinUrl());
        res.put("resumeUrl", user.getResumeUrl());
        res.put("portfolioUrl", user.getPortfolioUrl());
        res.put("country", user.getCountry());

        return ResponseEntity.ok(res);
    }

    @GetMapping("/admin/my-events")
    public ResponseEntity<List<Hackathon>> getAdminEvents() {
        return ResponseEntity.ok(hackathonRepository.findAll());
    }

    @GetMapping("/judge/my-reviews")
    public ResponseEntity<List<Submission>> getJudgeReviews() {
        return ResponseEntity.ok(submissionRepository.findAll());
    }
}
