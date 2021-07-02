package com.payment.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class TransferRequest {
    String requestReferenceId;
    String senderEmail;
    LocalDateTime requestDate;
    Account sourceAccount;
    Account targetAccount;
    Double amount;
}