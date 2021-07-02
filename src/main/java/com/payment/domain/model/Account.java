package com.payment.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Account {
    private final String accountNumber;
    private final String accountType;
    private final Double balance;
    private final Double ledgerBalance;
}
