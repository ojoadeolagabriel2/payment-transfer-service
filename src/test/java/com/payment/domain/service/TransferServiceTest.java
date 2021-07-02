package com.payment.domain.service;

import com.payment.infrastructure.repository.CustomerAccountRepository;
import com.payment.infrastructure.repository.CustomerAccountTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    CustomerAccountTransactionRepository accountTransactionRepository;
    @Mock
    CustomerAccountRepository repository;
    @Mock
    AccountService service;

    TransferService transferService;

    @BeforeEach
    public void init() {
        log.info("startup");
        transferService = new TransferService(service, repository, accountTransactionRepository);
    }
}