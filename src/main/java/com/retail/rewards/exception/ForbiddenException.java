package com.retail.rewards.exception;

/**
 * Signals an authorization failure.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
