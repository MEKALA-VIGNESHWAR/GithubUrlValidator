package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User profile not found: " + username));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateProfile(@PathVariable String username, @RequestBody Map<String, String> body) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User profile not found: " + username));

        if (body.containsKey("fullName")) user.setFullName(body.get("fullName"));
        if (body.containsKey("bio")) user.setBio(body.get("bio"));
        if (body.containsKey("skills")) user.setSkills(body.get("skills"));
        if (body.containsKey("githubUrl")) user.setGithubUrl(body.get("githubUrl"));
        if (body.containsKey("linkedinUrl")) user.setLinkedinUrl(body.get("linkedinUrl"));
        if (body.containsKey("resumeUrl")) user.setResumeUrl(body.get("resumeUrl"));
        if (body.containsKey("portfolioUrl")) user.setPortfolioUrl(body.get("portfolioUrl"));
        if (body.containsKey("country")) user.setCountry(body.get("country"));
        if (body.containsKey("university")) user.setUniversity(body.get("university"));

        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }
}
