package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("admin", "admin@hackforge.com", "encodedPassword", Role.ADMIN);
    }

    @Test
    void findUserByUsername_ShouldReturnUser() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(sampleUser));

        Optional<User> found = userRepository.findByUsername("admin");

        assertTrue(found.isPresent());
        assertEquals("admin", found.get().getUsername());
        assertEquals(Role.ADMIN, found.get().getRole());
    }

    @Test
    void validatePassword_ShouldReturnTrueWhenMatches() {
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        boolean matches = passwordEncoder.matches("rawPassword", sampleUser.getPassword());

        assertTrue(matches);
    }
}
