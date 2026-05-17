package com.retail.rewards.exception;

/**
 * Signals an authentication failure.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
