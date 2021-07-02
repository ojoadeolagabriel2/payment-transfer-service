package com.payment.domain.error;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String description) {
        super(description);
    }
}
