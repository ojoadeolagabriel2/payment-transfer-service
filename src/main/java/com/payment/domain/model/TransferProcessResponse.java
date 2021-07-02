package com.payment.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransferProcessResponse {
    String code;
    String responseMessage;
}
