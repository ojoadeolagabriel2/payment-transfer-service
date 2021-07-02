package com.payment.configuration;

import com.payment.api.dto.ApiError;
import com.payment.domain.error.AccountNotFoundException;
import com.payment.domain.error.BadRequestException;
import com.payment.domain.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.payment.domain.valueobject.ResponseCode.*;
import static org.springframework.http.ResponseEntity.badRequest;

@Slf4j
@ControllerAdvice
public class ErrorManagementConfiguration {

    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<ApiError> handleValidationException(BadRequestException ex) {
        log.error(ex.getMessage());
        return badRequest().body(ApiError.builder().code(BAD_REQUEST.getCode()).message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<ApiError> handleValidationException(BusinessException ex) {
        log.error(ex.getMessage());
        return badRequest().body(ApiError.builder().code(PROCESSING_ERROR.getCode()).message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {AccountNotFoundException.class})
    protected ResponseEntity<ApiError> handleValidationException(AccountNotFoundException ex) {
        log.error(ex.getMessage());
        return badRequest().body(ApiError.builder().code(PROCESSING_ERROR.getCode()).message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ApiError> handleValidationException(Exception ex) {
        log.error(ex.getMessage());
        return badRequest().body(ApiError.builder().code(UNKNOWN_ERROR.getCode()).message(ex.getMessage()).build());
    }
}