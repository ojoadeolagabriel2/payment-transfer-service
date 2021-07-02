package com.payment.api.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.payment.api.dto.AccountDto;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
@JsonInclude(Include.NON_NULL)
public class TransferRequestCommand {
    @JsonProperty("request_reference_id")
    String requestReferenceId;

    @JsonProperty("sender_email")
    String senderEmail;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("request_date")
    LocalDateTime requestDate;

    @JsonProperty("source_account")
    AccountDto sourceAccountDto;

    @JsonProperty("target_account")
    AccountDto targetAccountDto;

    @JsonProperty("request_amount")
    Double requestAmount;
}
