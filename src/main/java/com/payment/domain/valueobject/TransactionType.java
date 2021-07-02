package com.payment.domain.valueobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    DEBIT("DR"),
    CREDIT("CR");

    private final String code;
}
