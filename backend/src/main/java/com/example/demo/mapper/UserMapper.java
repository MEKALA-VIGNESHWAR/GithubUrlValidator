package com.example.demo.mapper;

import com.example.demo.dto.request.RegisterRequestDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.entity.User;

public class UserMapper {

    public static User toEntity(RegisterRequestDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }

    public static AuthResponseDTO toAuthResponseDTO(User user, String token) {
        if (user == null) return null;
        AuthResponseDTO dto = new AuthResponseDTO();
        dto.setAccessToken(token);
        dto.setTokenType("Bearer");
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
