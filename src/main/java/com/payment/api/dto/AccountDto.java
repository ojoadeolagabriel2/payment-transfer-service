package com.payment.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Getter
@Builder
@Jacksonized
@JsonInclude(NON_NULL)
public class AccountDto {
    @JsonProperty("account_number")
    private final String accountNumber;

    @JsonProperty("account_type")
    private final String accountType;
}
