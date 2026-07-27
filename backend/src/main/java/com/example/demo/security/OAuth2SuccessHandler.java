package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.enums.AuthProvider;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public OAuth2SuccessHandler(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String sub = oAuth2User.getAttribute("sub");
        String picture = oAuth2User.getAttribute("picture");

        String redirectBase = (frontendUrl != null && !frontendUrl.isBlank()) ? frontendUrl : "http://localhost:5173";

        if (email == null) {
            response.sendRedirect(redirectBase + "/?error=Email_Not_Found_From_Google");
            return;
        }

        // Find existing user or auto-register with default PARTICIPANT role
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            String username = email.split("@")[0] + "_" + System.currentTimeMillis() % 10000;
            User newUser = new User(username, email, passwordEncoder.encode("OAuth2SecuredPassword"), Role.PARTICIPANT);
            newUser.setProvider(AuthProvider.GOOGLE);
            newUser.setProviderId(sub);
            newUser.setProfilePicture(picture);
            return userRepository.save(newUser);
        });

        String accessToken = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = "ref-" + accessToken;

        String targetUrl = UriComponentsBuilder.fromUriString(redirectBase + "/")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("username", user.getUsername())
                .queryParam("email", user.getEmail())
                .queryParam("role", user.getRole().name())
                .queryParam("picture", user.getProfilePicture() != null ? user.getProfilePicture() : "")
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
