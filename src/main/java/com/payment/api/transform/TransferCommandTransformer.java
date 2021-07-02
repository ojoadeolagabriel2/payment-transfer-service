package com.payment.api.transform;

import com.payment.api.command.TransferRequestCommand;
import com.payment.domain.model.Account;
import com.payment.domain.model.TransferRequest;
import org.springframework.stereotype.Service;

@Service
public class TransferCommandTransformer {
    public TransferRequest toDomain(TransferRequestCommand command) {
        return TransferRequest
                .builder()
                .senderEmail(command.getSenderEmail())
                .amount(command.getRequestAmount())
                .requestDate(command.getRequestDate())
                .requestReferenceId(command.getRequestReferenceId())
                .sourceAccount(Account
                        .builder()
                        .accountNumber(command.getSourceAccountDto().getAccountNumber())
                        .accountType(command.getSourceAccountDto().getAccountType())
                        .build())
                .targetAccount(Account
                        .builder()
                        .accountNumber(command.getTargetAccountDto().getAccountNumber())
                        .accountType(command.getTargetAccountDto().getAccountType())
                        .build())
                .build();
    }
}