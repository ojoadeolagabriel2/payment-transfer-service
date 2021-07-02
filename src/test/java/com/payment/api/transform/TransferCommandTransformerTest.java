package com.payment.api.transform;

import com.payment.api.command.TransferRequestCommand;
import com.payment.api.dto.AccountDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransferCommandTransformerTest {
    TransferCommandTransformer testCandidate = new TransferCommandTransformer();

    @Test
    void onToDomain_GivenShortAccountNumber_RaiseBadRequestException() {
        var result = testCandidate.toDomain(aTransferRequestCommand("a@y.com",
                "12345",
                "savings",
                "12346",
                "savings",
                100.0
        ));

        assertEquals("a@y.com", result.getSenderEmail());
        assertEquals("12345", result.getSourceAccount().getAccountNumber());
        assertEquals("savings", result.getSourceAccount().getAccountType());
        assertEquals("12346", result.getTargetAccount().getAccountNumber());
        assertEquals("savings", result.getTargetAccount().getAccountType());
        assertEquals(100.0, result.getAmount());
        assertNotNull(result.getRequestReferenceId());
    }

    @SneakyThrows
    private TransferRequestCommand aTransferRequestCommand(String senderEmail,
                                                           String sourceAccountNumber,
                                                           String sourceAccountType,
                                                           String targetAccountNumber,
                                                           String targetAccountType,
                                                           Double requestAmount
    ) {
        return TransferRequestCommand
                .builder()
                .requestReferenceId(UUID.randomUUID().toString())
                .senderEmail(senderEmail)
                .requestDate(LocalDateTime.now())
                .sourceAccountDto(AccountDto.builder().accountNumber(sourceAccountNumber).accountType(sourceAccountType).build())
                .targetAccountDto(AccountDto.builder().accountNumber(targetAccountNumber).accountType(targetAccountType).build())
                .requestAmount(requestAmount)
                .build();
    }
}