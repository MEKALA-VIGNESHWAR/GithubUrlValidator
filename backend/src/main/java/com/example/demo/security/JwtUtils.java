package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret:8f3c4e1b7d5a3f6e8c9b2a4d7e1f3a5b8c9d2e4f7a1b3c5e8f9a2b4c6d8e0f12}")
    private String jwtSecret;

    @Value("${app.jwt.refresh-secret:9a2f8c4e1b7d5a3f6e8c9b2a4d7e1f3a5b8c9d2e4f7a1b3c5e8f9a2b4c6d8e99}")
    private String refreshSecret;

    @Value("${app.jwt.expiration-ms:900000}") // Default 15 minutes
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms:604800000}") // Default 7 days
    private long refreshExpirationMs;

    private SecretKey getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(jwtSecret))
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getSigningKey(refreshSecret))
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getUsernameFromRefreshToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(refreshSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey(jwtSecret)).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey(refreshSecret)).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }
}
