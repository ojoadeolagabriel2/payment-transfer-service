package com.payment.api.validator;

import com.payment.domain.error.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountInformationValidatorTest {

    AccountInformationValidator validatorTestCandidate = new AccountInformationValidator();

    @Test
    void onValidate_GivenShortAccountNumber_RaiseBadRequestException() {
        Exception exception = assertThrows(BadRequestException.class, () -> validatorTestCandidate.validate("1234", "savings"));

        assertEquals("invalid account number", exception.getMessage());
    }

    @Test
    void onValidate_GivenInvalidAccountType_RaiseBadRequestException() {
        Exception exception = assertThrows(BadRequestException.class, () -> validatorTestCandidate.validate("12345", "saving"));

        assertEquals("invalid account type, ensure either current or savings type", exception.getMessage());
    }
}