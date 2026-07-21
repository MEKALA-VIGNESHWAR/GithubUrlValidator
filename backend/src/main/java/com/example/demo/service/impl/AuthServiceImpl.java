package com.example.demo.service.impl;

import com.example.demo.dto.request.LoginRequestDTO;
import com.example.demo.dto.request.RegisterRequestDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.InvalidSubmissionException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByUsernameIgnoreCase(dto.getUsername().trim())) {
            throw new ConflictException("Username '" + dto.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail().trim())) {
            throw new ConflictException("Email '" + dto.getEmail() + "' is already registered.");
        }

        User user = UserMapper.toEntity(dto);
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);
        String token = jwtUtils.generateToken(savedUser.getUsername(), savedUser.getRole().name());

        return UserMapper.toAuthResponseDTO(savedUser, token);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsernameOrEmail().trim(), dto.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidSubmissionException("Invalid username/email or password.");
        }

        User user = userRepository.findByUsernameOrEmail(dto.getUsernameOrEmail().trim(), dto.getUsernameOrEmail().trim())
                .orElseThrow(() -> new InvalidSubmissionException("User not found."));

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
        return UserMapper.toAuthResponseDTO(user, token);
    }
}
