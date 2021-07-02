package com.payment.domain.service;

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
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    CustomerAccountTransactionRepository accountTransactionRepository;
    @Mock
    CustomerAccountRepository repository;

    AccountService service;

    @BeforeEach
    public void init() {
        log.info("startup");
        service = new AccountService(accountTransactionRepository, repository);
    }

    @Test
    void onGetAccountBalance_GivenCustomerHasCreditAndDebitActions_ReturnValidBalance() {
        when(repository.findCustomerAccountByAccountNumberAndAccountType("12345", "savings"))
                .thenReturn(of(getCustomerAccount()));
        when(accountTransactionRepository.findAllByCustomerAccountId(1L))
                .thenReturn(getCustomerAccountTransactions());

        var result = service.getAccountByNumberAndType("12345", "savings");
        Assertions.assertNotNull(result.orElseGet(null));
    }

    @Test
    void onGetAccountByNumberAndType_GivenCustomerHasCreditAndDebitActions_And_ValidAccount_ReturnAccount() {
        when(accountTransactionRepository.findAllByCustomerAccountId(12345L))
                .thenReturn(getCustomerAccountTransactions());

        var result = service.getAccountBalance(12345);
        Assertions.assertEquals(100, result);
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