package com.payment.domain.service;

import com.payment.domain.error.BusinessException;
import com.payment.domain.model.Account;
import com.payment.domain.model.TransferRequest;
import com.payment.infrastructure.entity.CustomerAccount;
import com.payment.infrastructure.entity.CustomerAccountTransaction;
import com.payment.infrastructure.repository.CustomerAccountRepository;
import com.payment.infrastructure.repository.CustomerAccountTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.payment.domain.valueobject.TransactionType.CREDIT;
import static com.payment.domain.valueobject.TransactionType.DEBIT;
import static java.time.LocalDateTime.now;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

    @Test
    void onProcessTransfer_WhenBalanceAvailable_ReturnSuccess() {
        final String sourceAccountNumber = "12345";
        final String sourceAccountType = "current";

        when(repository.findCustomerAccountByAccountNumberAndAccountType(sourceAccountNumber, sourceAccountType))
                .thenReturn(of(getCustomerAccount()));

        when(service.getAccountBalance(1L))
                .thenReturn(100.0);

        var response = transferService.processTransfer(TransferRequest
                .builder()
                .requestReferenceId(randomUUID().toString())
                .senderEmail("a@y.com")
                .amount(50.0)
                .sourceAccount(Account.builder().accountNumber(sourceAccountNumber).accountType(sourceAccountType).build())
                .targetAccount(Account.builder().accountNumber("12346").accountType("savings").build())
                .build());

        Assertions.assertEquals("00", response.getCode());
    }

    @Test
    void onProcessTransfer_WhenBalanceInsufficient_ReturnInsufficientFunds() {
        final String sourceAccountNumber = "12345";
        final String sourceAccountType = "current";

        when(repository.findCustomerAccountByAccountNumberAndAccountType(sourceAccountNumber, sourceAccountType))
                .thenReturn(of(getCustomerAccount()));

        when(service.getAccountBalance(1L))
                .thenReturn(49.0);

        var exception = assertThrows(BusinessException.class, () -> transferService.processTransfer(TransferRequest
                .builder()
                .requestReferenceId(randomUUID().toString())
                .senderEmail("a@y.com")
                .amount(50.0)
                .sourceAccount(Account.builder().accountNumber(sourceAccountNumber).accountType(sourceAccountType).build())
                .targetAccount(Account.builder().accountNumber("12346").accountType("savings").build())
                .build()));

        Assertions.assertEquals("Insufficient funds", exception.getMessage());
    }

    private CustomerAccount getCustomerAccount() {
        var account = new CustomerAccount();
        account.setId(1);
        account.setFirstName("adeola");
        account.setMiddleName("gabby");
        account.setLastName("ola");
        account.setAccountNumber("12345");
        account.setAccountType("savings");
        account.setCreated(now());
        account.setModified(now());
        account.setLastBalance(0.0);
        account.setUserId("1");
        return account;
    }

    private List<CustomerAccountTransaction> getCustomerAccountTransactions() {
        var list = new ArrayList<CustomerAccountTransaction>();

        var customerAccountTransaction = new CustomerAccountTransaction();
        customerAccountTransaction.setId(1);
        customerAccountTransaction.setCustomerAccountId(12345);
        customerAccountTransaction.setCreated(now());
        customerAccountTransaction.setModified(now());
        customerAccountTransaction.setType(CREDIT.getCode());
        customerAccountTransaction.setAmount(300.0);
        list.add(customerAccountTransaction);

        var customerAccountTransaction2 = new CustomerAccountTransaction();
        customerAccountTransaction2.setId(2);
        customerAccountTransaction2.setCustomerAccountId(12346);
        customerAccountTransaction2.setCreated(now());
        customerAccountTransaction2.setModified(now());
        customerAccountTransaction2.setType(DEBIT.getCode());
        customerAccountTransaction2.setAmount(200.0);
        list.add(customerAccountTransaction2);

        return list;
    }
}