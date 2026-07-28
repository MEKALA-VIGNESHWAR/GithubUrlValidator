package com.example.demo.controller;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/auth", "/api/v1/auth"})
public class AuthController {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuditLogService auditLogService;

    public AuthController(UserRepository userRepository,
                          RefreshTokenRepository refreshTokenRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.auditLogService = auditLogService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> request) {
        String username = request.get("username") != null ? request.get("username").toString() : null;
        String email = request.get("email") != null ? request.get("email").toString() : null;
        String password = request.get("password") != null ? request.get("password").toString() : null;
        String roleStr = request.get("role") != null ? request.get("role").toString() : "PARTICIPANT";

        if (username == null || email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username, email and password are required"));
        }

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

        String accessToken = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
        String refreshTokenStr = jwtUtils.generateRefreshToken(user.getUsername());

        RefreshToken refreshToken = new RefreshToken(user.getId(), refreshTokenStr, LocalDateTime.now().plusNanos(jwtUtils.getRefreshExpirationMs() * 1_000_000));
        refreshTokenRepository.save(refreshToken);

        auditLogService.logAction("USER_REGISTER", user.getUsername(), "User", user.getId(), "User registered successfully");

        Map<String, Object> response = new HashMap<>();
        response.put("token", accessToken);
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshTokenStr);
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, Object> request) {
        String username = request.get("username") != null ? request.get("username").toString() :
                         (request.get("usernameOrEmail") != null ? request.get("usernameOrEmail").toString() : "");
        String password = request.get("password") != null ? request.get("password").toString() : "";

        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username/email or password."));
        }

        String accessToken = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
        String refreshTokenStr = jwtUtils.generateRefreshToken(user.getUsername());

        RefreshToken refreshToken = new RefreshToken(user.getId(), refreshTokenStr, LocalDateTime.now().plusNanos(jwtUtils.getRefreshExpirationMs() * 1_000_000));
        refreshTokenRepository.save(refreshToken);

        auditLogService.logAction("USER_LOGIN", user.getUsername(), "User", user.getId(), "User authenticated successfully");

        Map<String, Object> response = new HashMap<>();
        response.put("token", accessToken);
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshTokenStr);
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, Object> request) {
        String refreshTokenStr = request.get("refreshToken") != null ? request.get("refreshToken").toString() : null;
        if (refreshTokenStr == null || !jwtUtils.validateRefreshToken(refreshTokenStr)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired refresh token"));
        }

        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshTokenStr).orElse(null);
        if (tokenEntity == null || tokenEntity.isRevoked() || tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Refresh token has been revoked or expired"));
        }

        String username = jwtUtils.getUsernameFromRefreshToken(refreshTokenStr);
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }

        String newAccessToken = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "refreshToken", refreshTokenStr));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody(required = false) Map<String, Object> request) {
        if (request != null && request.containsKey("refreshToken")) {
            String refreshTokenStr = request.get("refreshToken").toString();
            refreshTokenRepository.findByToken(refreshTokenStr).ifPresent(rt -> {
                rt.setRevoked(true);
                refreshTokenRepository.save(rt);
            });
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
