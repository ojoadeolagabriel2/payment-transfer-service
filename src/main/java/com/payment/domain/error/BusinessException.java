package com.payment.domain.error;

public class BusinessException extends RuntimeException {
    public BusinessException(String description) {
        super(description);
    }

    public BusinessException(String description, Exception ex) {
        super(description, ex);
    }
}
