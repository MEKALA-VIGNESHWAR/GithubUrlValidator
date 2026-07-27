package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User profile not found: " + username)));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateProfile(@PathVariable String username, @RequestBody Map<String, String> body) {
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User profile not found: " + username)));

        if (body.containsKey("email")) {
            String newEmail = body.get("email");
            if (newEmail != null && !newEmail.trim().isEmpty() && !newEmail.equalsIgnoreCase(user.getEmail())) {
                if (userRepository.existsByEmail(newEmail)) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Error: Email is already in use by another user!"));
                }
                user.setEmail(newEmail);
            }
        }
        if (body.containsKey("fullName")) user.setFullName(body.get("fullName"));
        if (body.containsKey("bio")) user.setBio(body.get("bio"));
        if (body.containsKey("skills")) user.setSkills(body.get("skills"));
        if (body.containsKey("githubUrl")) user.setGithubUrl(body.get("githubUrl"));
        if (body.containsKey("linkedinUrl")) user.setLinkedinUrl(body.get("linkedinUrl"));
        if (body.containsKey("resumeUrl")) user.setResumeUrl(body.get("resumeUrl"));
        if (body.containsKey("portfolioUrl")) user.setPortfolioUrl(body.get("portfolioUrl"));
        if (body.containsKey("country")) user.setCountry(body.get("country"));
        if (body.containsKey("university")) user.setUniversity(body.get("university"));
        if (body.containsKey("profilePicture")) user.setProfilePicture(body.get("profilePicture"));

        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }
}
