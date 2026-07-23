package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TenantContextFilter tenantContextFilter;
    private final RateLimitingFilter rateLimitingFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Value("${app.cors.allowed-origins:http://localhost:5173,http://localhost:3000}")
    private String allowedOrigins;

    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          TenantContextFilter tenantContextFilter,
                          RateLimitingFilter rateLimitingFilter,
                          OAuth2SuccessHandler oAuth2SuccessHandler,
                          OAuth2FailureHandler oAuth2FailureHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.tenantContextFilter = tenantContextFilter;
        this.rateLimitingFilter = rateLimitingFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With", "X-Tenant-Id", "X-Idempotency-Key"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "X-Idempotency-Key"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints & Static assets
                        .requestMatchers("/", "/error", "/index.html", "/static/**", "/assets/**", "/favicon.ico", "/uploads/**").permitAll()
                        .requestMatchers("/api/auth/**", "/api/v1/auth/**", "/api/v1/support/**", "/api/v1/flags/**").permitAll()
                        .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/ws/**", "/oauth2/**", "/login/oauth2/code/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/submissions/**", "/api/v1/submissions/**", "/api/hackathons/**", "/api/v1/hackathons/**", "/api/analytics/**", "/api/v1/analytics/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/submissions", "/api/v1/submissions").permitAll()
                        // Role-based protected endpoints
                        .requestMatchers("/api/admin/**", "/api/v1/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/hackathons", "/api/v1/hackathons").hasAnyRole("ADMIN", "ORGANIZER", "EVENT_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/hackathons/**", "/api/v1/hackathons/**").hasAnyRole("ADMIN", "ORGANIZER", "EVENT_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/submissions/**", "/api/v1/submissions/**").hasAnyRole("ADMIN", "JUDGE")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .xssProtection(Customizer.withDefaults())
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; font-src 'self' data:;"))
                );

        http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tenantContextFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
