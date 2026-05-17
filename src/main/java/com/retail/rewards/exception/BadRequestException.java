package com.retail.rewards.exception;

/**
 * Signals a client-side request problem.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
