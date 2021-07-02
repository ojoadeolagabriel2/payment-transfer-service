package com.payment.api.validator;

import com.payment.domain.error.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Set.of;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class AccountInformationValidator {

    private static final Integer MINIMUM_ACCOUNT_LENGTH = 5; // assumed min length for the purpose of test
    private static final Set<String> allowedAccountType = of("current", "savings");

    public void validate(String accountNumber, String accountType) {
        if (isEmpty(accountNumber) || accountNumber.length() < MINIMUM_ACCOUNT_LENGTH)
            throw new BadRequestException("invalid account number");
        if (isEmpty(accountType) || !allowedAccountType.contains(accountType))
            throw new BadRequestException("invalid account type, ensure either current or savings type");
    }
}