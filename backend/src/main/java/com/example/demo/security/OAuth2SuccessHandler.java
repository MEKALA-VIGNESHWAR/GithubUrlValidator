package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.getOrDefault("name", attributes.get("login"));

        if (email == null) {
            email = attributes.get("login") + "@github.user";
        }

        log.info("OAuth2 Social Login Success for user: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            user = new User();
            user.setUsername(email.split("@")[0]);
            user.setEmail(email);
            user.setFullName(name != null ? name : "Social User");
            user.setPassword("OAUTH2_FEDERATED_USER");
            user.setRole(Role.PARTICIPANT);
            user = userRepository.save(user);
        }

        String accessToken = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

        // Redirect to React frontend dashboard with tokens
        String redirectUrl = String.format("http://localhost:5173/auth/callback?accessToken=%s&refreshToken=%s&username=%s&role=%s",
                accessToken, refreshToken, user.getUsername(), user.getRole().name());

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
