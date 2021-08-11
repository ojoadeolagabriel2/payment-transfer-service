package com.payment.api;

import com.payment.api.command.TransferRequestCommand;
import com.payment.api.dto.TransferResponseDto;
import com.payment.api.transform.TransferCommandTransformer;
import com.payment.api.validator.AccountInformationValidator;
import com.payment.domain.error.BadRequestException;
import com.payment.domain.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/transfer", "/transfer/v1"})
public class TransferController {

    private final TransferService transferService;
    private final TransferCommandTransformer transformer;
    private final AccountInformationValidator validator;

    @Operation(summary = "Process transfer request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instruction processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transfer instruction")
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransferResponseDto> processTransfer(@RequestBody TransferRequestCommand requestCommand) {
        try {
            validateRequest(requestCommand);

            log.info("processing transfer: {}", requestCommand.getRequestReferenceId());
            var response = transferService.processTransfer(transformer.toDomain(requestCommand));

            return ok(TransferResponseDto
                    .builder()
                    .statusCode(response.getCode())
                    .message(response.getResponseMessage())
                    .build());
        } catch (BadRequestException exception) {
            log.error("Could not complete transfer request: {}", requestCommand.getRequestReferenceId());
            throw new BadRequestException(exception.getMessage());
        }
    }

    private void validateRequest(@NonNull TransferRequestCommand requestCommand) {
        log.info("validating transfer request {}", requestCommand.getRequestReferenceId());
        validator.validate(
                requestCommand.getSourceAccountDto().getAccountNumber(),
                requestCommand.getSourceAccountDto().getAccountType()
        );
        log.info("completed request validation successfully for {}", requestCommand.getRequestReferenceId());
    }
}
