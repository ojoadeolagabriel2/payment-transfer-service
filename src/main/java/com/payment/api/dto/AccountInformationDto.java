package com.payment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountInformationDto {
    @JsonProperty("account_number")
    String accountNumber;
    @JsonProperty("account_type")
    String accountType;
    @JsonProperty("balance")
    Double liveBalance;
    @JsonProperty("ledger_balance")
    Double ledgerBalance;
}
