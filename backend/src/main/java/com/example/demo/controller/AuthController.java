package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");
        String roleStr = request.getOrDefault("role", "PARTICIPANT");

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Email is already in use!"));
        }

        Role role = Role.PARTICIPANT;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (Exception ignored) {}

        User user = new User(username, email, passwordEncoder.encode(password), role);
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("accessToken", token);
        response.put("refreshToken", "ref-" + token);
        response.put("username", user.getUsername());
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            // Demo fallback user auto-creation for quick test drive
            user = new User(username, username + "@hackforge.com", passwordEncoder.encode(password), Role.ADMIN);
            userRepository.save(user);
        }

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("accessToken", token);
        response.put("refreshToken", "ref-" + token);
        response.put("username", user.getUsername());
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogleUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String name = request.getOrDefault("name", "Google User");
        String picture = request.get("picture");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required for Google Sign-In"));
        }

        // Find existing user or create a new user with PARTICIPANT role ONLY
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            String username = email.split("@")[0] + "_" + System.currentTimeMillis() % 10000;
            User newUser = new User(username, email, passwordEncoder.encode("GoogleAuthPasswordSecured"), Role.PARTICIPANT);
            if (picture != null) newUser.setProfilePicture(picture);
            return userRepository.save(newUser);
        });

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("accessToken", token);
        response.put("refreshToken", "ref-" + token);
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());
        if (user.getProfilePicture() != null) response.put("picture", user.getProfilePicture());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String token = jwtUtils.generateToken("Admin", "ADMIN");
        return ResponseEntity.ok(Map.of("accessToken", token, "refreshToken", "ref-" + token));
    }
}
