package com.payment.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransferResponseDto {
    String statusCode;
    String message;
}
