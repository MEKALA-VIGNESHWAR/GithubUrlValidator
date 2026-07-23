package com.example.demo.controller;

import com.example.demo.entity.Hackathon;
import com.example.demo.entity.Submission;
import com.example.demo.entity.User;
import com.example.demo.repository.HackathonRepository;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SubmissionRepository submissionRepository;
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;

    public SearchController(SubmissionRepository submissionRepository,
                            HackathonRepository hackathonRepository,
                            UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.hackathonRepository = hackathonRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> globalSearch(@RequestParam("q") String query) {
        Map<String, Object> results = new HashMap<>();
        String q = query.toLowerCase();

        List<Submission> matchingProjects = submissionRepository.findAll().stream()
                .filter(s -> (s.getProjectTitle() != null && s.getProjectTitle().toLowerCase().contains(q)) ||
                             (s.getTeamName() != null && s.getTeamName().toLowerCase().contains(q)) ||
                             (s.getTechStack() != null && s.getTechStack().toLowerCase().contains(q)))
                .collect(Collectors.toList());

        List<Hackathon> matchingHackathons = hackathonRepository.findAll().stream()
                .filter(h -> (h.getTitle() != null && h.getTitle().toLowerCase().contains(q)) ||
                             (h.getOrganizer() != null && h.getOrganizer().toLowerCase().contains(q)))
                .collect(Collectors.toList());

        List<User> matchingUsers = userRepository.findAll().stream()
                .filter(u -> (u.getUsername() != null && u.getUsername().toLowerCase().contains(q)) ||
                             (u.getEmail() != null && u.getEmail().toLowerCase().contains(q)))
                .collect(Collectors.toList());

        results.put("projects", matchingProjects);
        results.put("hackathons", matchingHackathons);
        results.put("users", matchingUsers);
        results.put("totalResults", matchingProjects.size() + matchingHackathons.size() + matchingUsers.size());

        return ResponseEntity.ok(results);
    }
}
