package com.payment.api;

import com.payment.api.dto.AccountInformationDto;
import com.payment.api.validator.AccountInformationValidator;
import com.payment.domain.error.AccountNotFoundException;
import com.payment.domain.service.AccountService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.payment.domain.error.common.ErrorMessage.ACCOUNT_NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account/")
public class AccountController {

    private final AccountService accountService;
    private final AccountInformationValidator validator;

    @Operation(summary = "Fetch account information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched account information successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to fetch account information, bad request"),
    })
    @Timed(description = "time interval processing instruction")
    @GetMapping(path = "/{account_number}/type/{account_type}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountInformationDto> getAccountInformation(
            @PathVariable("account_number") String accountNumber,
            @PathVariable("account_type") String accountType) {

        validator.validate(accountNumber, accountType);

        log.info("fetching account information for {}:{}", accountNumber, accountType);
        var possibleAccount = accountService.getAccountByNumberAndType(accountNumber, accountType);
        var account = possibleAccount.orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        return ok(AccountInformationDto
                .builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .liveBalance(account.getBalance())
                .ledgerBalance(account.getLedgerBalance())
                .build());
    }
}
