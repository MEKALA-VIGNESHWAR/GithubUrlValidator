package com.example.demo.dto.response;

import com.example.demo.enums.Role;

public class AuthResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private String email;
    private Role role;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String accessToken, String username, String email, Role role) {
        this.accessToken = accessToken;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
