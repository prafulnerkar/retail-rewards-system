package com.retail.rewards.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.rewards.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * Returns 401 responses for unauthenticated access.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorResponse.builder()
                            .timestamp(LocalDateTime.now())
                            .status(HttpServletResponse.SC_UNAUTHORIZED)
                            .error("Unauthorized")
                            .message("Authentication required")
                            .path(request.getRequestURI())
                            .build());
        } catch (Exception ignored) {
        }
    }
}
