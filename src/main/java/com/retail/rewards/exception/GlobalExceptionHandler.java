package com.retail.rewards.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Centralized exception handling for the REST layer.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex,
                                                                                  HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler({BadRequestException.class, ConstraintViolationException.class, BindException.class,
            MethodArgumentNotValidException.class, DataIntegrityViolationException.class})
    public org.springframework.http.ResponseEntity<ErrorResponse> handleBadRequest(Exception ex,
                                                                                   HttpServletRequest request) {
        List<String> details = null;
        if (ex instanceof MethodArgumentNotValidException) {
            details = ((MethodArgumentNotValidException) ex).getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
        } else if (ex instanceof BindException) {
            details = ((BindException) ex).getBindingResult().getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
        } else if (ex instanceof ConstraintViolationException) {
            details = ((ConstraintViolationException) ex).getConstraintViolations()
                    .stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.toList());
        }

        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, details);
    }

    @ExceptionHandler({UnauthorizedException.class, BadCredentialsException.class, InvalidTokenException.class})
    public org.springframework.http.ResponseEntity<ErrorResponse> handleUnauthorized(Exception ex,
                                                                                      HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    public org.springframework.http.ResponseEntity<ErrorResponse> handleForbidden(Exception ex,
                                                                                 HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage(), request, null);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex,
                                                                                       HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleGeneric(Exception ex,
                                                                               HttpServletRequest request) {
        log.error("Unhandled exception", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request, null);
    }

    private org.springframework.http.ResponseEntity<ErrorResponse> build(HttpStatus status,
                                                                         String message,
                                                                         HttpServletRequest request,
                                                                         List<String> details) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .details(details)
                .build();
        return new org.springframework.http.ResponseEntity<>(response, status);
    }
}
