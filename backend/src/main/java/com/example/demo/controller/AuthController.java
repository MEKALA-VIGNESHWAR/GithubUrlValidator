package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequestDTO;
import com.example.demo.dto.request.RegisterRequestDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully.");
        return ResponseEntity.ok(response);
    }
}
