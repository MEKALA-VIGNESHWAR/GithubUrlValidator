package com.example.demo.service;

import com.example.demo.dto.request.LoginRequestDTO;
import com.example.demo.dto.request.RegisterRequestDTO;
import com.example.demo.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
}
