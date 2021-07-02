package com.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.api.command.TransferRequestCommand;
import com.payment.api.dto.AccountDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void givenValidTransferRequest_OnPost_ReturnValidTransferResponse_And_200() {
        final var REQUEST_BUILDER =
                request(HttpMethod.POST, "/transfer")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON);

        mockMvc
                .perform(REQUEST_BUILDER.content(aTransferRequestCommand("a@y.com",
                        "1000000011",
                        "current",
                        "1000000021",
                        "savings",
                        100.0
                )))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":\"00\",\"message\":\"Transfer request successful\"}"));
    }

    @SneakyThrows
    @Test
    void givenInsufficientFunds_OnPost_ReturnInsufficientFunds_And_400() {
        final var REQUEST_BUILDER =
                request(HttpMethod.POST, "/transfer")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON);

        mockMvc
                .perform(REQUEST_BUILDER.content(aTransferRequestCommand("a@y.com",
                        "1000000011",
                        "current",
                        "1000000021",
                        "savings",
                        1000001.0 // exceeds balance of 1,000,000 by 1 unit
                )))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"03\",\"message\":\"Insufficient funds\"}"));
    }

    @SneakyThrows
    @Test
    void givenUnknownSourceAccount_OnPost_ReturnAccountNotFound_And_400() {
        final var REQUEST_BUILDER =
                request(HttpMethod.POST, "/transfer")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON);

        mockMvc
                .perform(REQUEST_BUILDER.content(aTransferRequestCommand("a@y.com",
                        "10000000112",
                        "current",
                        "1000000021",
                        "savings",
                        100.0
                )))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"03\",\"message\":\"Account 10000000112 with type current not found\"}"));
    }

    @SneakyThrows
    private String aTransferRequestCommand(String senderEmail,
                                           String sourceAccountNumber,
                                           String sourceAccountType,
                                           String targetAccountNumber,
                                           String targetAccountType,
                                           Double requestAmount
    ) {
        return objectMapper.writeValueAsString(TransferRequestCommand
                .builder()
                .requestReferenceId(UUID.randomUUID().toString())
                .senderEmail(senderEmail)
                .requestDate(LocalDateTime.now())
                .sourceAccountDto(AccountDto.builder().accountNumber(sourceAccountNumber).accountType(sourceAccountType).build())
                .targetAccountDto(AccountDto.builder().accountNumber(targetAccountNumber).accountType(targetAccountType).build())
                .requestAmount(requestAmount)
                .build());
    }
}