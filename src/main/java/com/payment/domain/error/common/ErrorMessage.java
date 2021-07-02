package com.payment.domain.error.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {
    public static final String ACCOUNT_NOT_FOUND = "Account not found, please check senders account number or account type";
}
