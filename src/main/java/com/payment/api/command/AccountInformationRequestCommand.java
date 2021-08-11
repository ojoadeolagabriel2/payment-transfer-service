package com.payment.api.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountInformationRequestCommand {
    @JsonProperty("account_number")
    String accountNumber;
    @JsonProperty("account_type")
    String accountType;
}
