package com.payment.domain.service;

import com.payment.domain.model.Account;
import com.payment.infrastructure.entity.CustomerAccountTransaction;
import com.payment.infrastructure.repository.CustomerAccountRepository;
import com.payment.infrastructure.repository.CustomerAccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.payment.domain.valueobject.TransactionType.CREDIT;
import static com.payment.domain.valueobject.TransactionType.DEBIT;
import static java.util.Optional.of;

@Component
@RequiredArgsConstructor
public class AccountService {

    private final CustomerAccountTransactionRepository accountTransactionRepository;
    private final CustomerAccountRepository repository;

    /**
     * Get account information
     *
     * @param accountNumber customer account number
     * @param accountType   customer account type
     * @return account summary
     */
    public Optional<Account> getAccountByNumberAndType(String accountNumber, String accountType) {
        var possibleAccount = repository.findCustomerAccountByAccountNumberAndAccountType(accountNumber, accountType);
        if (possibleAccount.isPresent()) {
            var account = possibleAccount.get();

            double balance = getAccountBalance(account.getId());
            return of(Account
                    .builder()
                    .accountNumber(account.getAccountNumber())
                    .accountType(account.getAccountType())
                    .balance(balance)
                    .ledgerBalance(account.getLastBalance())
                    .build());
        }
        return Optional.empty();
    }

    @Transactional
    public double getAccountBalance(long accountId) {
        var entries = accountTransactionRepository.findAllByCustomerAccountId(accountId);
        var totalCredits = entries
                .stream()
                .filter(c -> CREDIT.getCode().equals(c.getType())).map(CustomerAccountTransaction::getAmount)
                .reduce(0.0, Double::sum);

        var totalDebits = entries
                .stream()
                .filter(c -> DEBIT.getCode().equals(c.getType())).map(CustomerAccountTransaction::getAmount)
                .reduce(0.0, Double::sum);

        return totalCredits - totalDebits;
    }
}