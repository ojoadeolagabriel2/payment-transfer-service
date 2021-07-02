package com.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.api.command.TransferRequestCommand;
import com.payment.api.dto.AccountDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = ANY)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class E2ETransferControllerTest {

    private static final String VALID_SOURCE_ACCOUNT_NUMBER = "1000000011";
    private static final String VALID_SOURCE_ACCOUNT_TYPE = "current";

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

        final var BALANCE_REQUEST_BUILDER =
                request(HttpMethod.GET, "/account/{account_number}/type/{account_type}", VALID_SOURCE_ACCOUNT_NUMBER, VALID_SOURCE_ACCOUNT_TYPE)
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON);

        // confirm opening balance is 1000000.0
        mockMvc
                .perform(BALANCE_REQUEST_BUILDER.content(""))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"account_number\":\"1000000011\",\"account_type\":\"current\",\"balance\":1000000.0,\"ledger_balance\":0.0}"));

        // initiate transfer of 100.0
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

        // confirm new live and ledger balance is 999900.0 (less 100.0)
        mockMvc
                .perform(BALANCE_REQUEST_BUILDER.content(""))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"account_number\":\"1000000011\",\"account_type\":\"current\",\"balance\":999900.0,\"ledger_balance\":999900.0}"));

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