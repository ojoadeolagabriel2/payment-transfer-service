package com.payment.domain.error;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String description) {
        super(description);
    }
}
