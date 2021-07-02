package com.payment.api;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerTest {

    private static final String SHORT_ACCOUNT_NUMBER = "1234";
    private static final String VALID_SOURCE_ACCOUNT_NUMBER = "1000000011";
    private static final String VALID_SOURCE_ACCOUNT_TYPE = "current";

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void givenShortAccountNumber_OnCall_ReturnInvalid_Account_Number_And_400() {
        final var REQUEST_BUILDER =
                request(HttpMethod.GET, "/account/{account_number}/type/{account_type}", SHORT_ACCOUNT_NUMBER, "savings")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON);

        mockMvc
                .perform(REQUEST_BUILDER.content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"02\",\"message\":\"invalid account number\"}"));
    }

    @SneakyThrows
    @Test
    void givenInvalidAccountType_OnCall_ReturnInvalid_Account_Type_And_400() {
        final var REQUEST_BUILDER =
                request(HttpMethod.GET, "/account/{account_number}/type/{account_type}", VALID_SOURCE_ACCOUNT_NUMBER, "saving")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON);

        mockMvc
                .perform(REQUEST_BUILDER.content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"02\",\"message\":\"invalid account type, ensure either current or savings type\"}"));
    }

    @SneakyThrows
    @Test
    void givenAccountInformationNotRegistered_OnCall_ReturnAccountNotFound_And_400() {
        final var REQUEST_BUILDER =
                request(HttpMethod.GET, "/account/{account_number}/type/{account_type}", VALID_SOURCE_ACCOUNT_NUMBER, "savings")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON);

        mockMvc
                .perform(REQUEST_BUILDER.content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"03\",\"message\":\"Account not found, please check senders account number or account type\"}"));
    }

    @SneakyThrows
    @Test
    void givenValidAccountInformation_OnCall_ReturnAccount_And_200() {
        final var REQUEST_BUILDER =
                request(HttpMethod.GET, "/account/{account_number}/type/{account_type}", VALID_SOURCE_ACCOUNT_NUMBER, VALID_SOURCE_ACCOUNT_TYPE)
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON);

        mockMvc
                .perform(REQUEST_BUILDER.content(""))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"account_number\":\"1000000011\",\"account_type\":\"current\",\"balance\":1000000.0,\"ledger_balance\":0.0}"));
    }
}