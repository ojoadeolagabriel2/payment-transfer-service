package com.payment.domain.valueobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS("00", "Transfer request successful"),
    DUPLICATE_REQUEST("01", "Duplicate request reference detected"),
    BAD_REQUEST("02", "Bad request reference detected"),
    PROCESSING_ERROR("03", "Could not process"),
    UNKNOWN_ERROR("04", "Unknown error"),
    ACCOUNT_NOT_FOUND("05", "Account not found");

    private final String code;
    private final String message;
}
