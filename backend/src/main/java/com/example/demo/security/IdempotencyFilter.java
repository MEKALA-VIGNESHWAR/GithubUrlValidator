package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyFilter.class);
    private static final String IDEMPOTENCY_HEADER = "X-Idempotency-Key";

    private final StringRedisTemplate redisTemplate;

    public IdempotencyFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String idempotencyKey = request.getHeader(IDEMPOTENCY_HEADER);

        if (idempotencyKey != null && !idempotencyKey.isBlank() && isWriteMethod(request.getMethod())) {
            String redisKey = "idempotency:" + idempotencyKey;

            try {
                Boolean isNewKey = redisTemplate.opsForValue().setIfAbsent(redisKey, "PROCESSING", Duration.ofMinutes(10));

                if (Boolean.FALSE.equals(isNewKey)) {
                    log.warn("Duplicate request detected for Idempotency Key: {}", idempotencyKey);
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Duplicate Request\", \"message\": \"A request with this X-Idempotency-Key is already processed or currently executing.\", \"idempotencyKey\": \"" + idempotencyKey + "\"}");
                    return;
                }
            } catch (Exception e) {
                log.warn("Redis unavailable for idempotency key check. Proceeding with filter chain: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isWriteMethod(String method) {
        return "POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method);
    }
}
