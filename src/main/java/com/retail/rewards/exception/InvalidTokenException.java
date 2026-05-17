package com.retail.rewards.exception;

/**
 * Signals JWT validation failure.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
