package com.retail.rewards.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.rewards.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * Returns 403 responses for forbidden access.
 */
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorResponse.builder()
                            .timestamp(LocalDateTime.now())
                            .status(HttpServletResponse.SC_FORBIDDEN)
                            .error("Forbidden")
                            .message("Access denied")
                            .path(request.getRequestURI())
                            .build());
        } catch (Exception ignored) {
        }
    }
}
