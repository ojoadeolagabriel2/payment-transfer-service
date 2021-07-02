package com.payment.domain.service;

import com.payment.domain.error.BusinessException;
import com.payment.domain.model.TransferProcessResponse;
import com.payment.domain.model.TransferRequest;
import com.payment.infrastructure.entity.CustomerAccountTransaction;
import com.payment.infrastructure.repository.CustomerAccountRepository;
import com.payment.infrastructure.repository.CustomerAccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.payment.domain.valueobject.ResponseCode.SUCCESS;
import static com.payment.domain.valueobject.TransactionType.DEBIT;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class TransferService {

    private static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds";

    private final AccountService accountService;
    private final CustomerAccountRepository accountRepository;
    private final CustomerAccountTransactionRepository accountTransactionRepository;

    @Transactional
    public TransferProcessResponse processTransfer(TransferRequest request) {
        var sourceAccount = request.getSourceAccount();

        var possibleAccount = accountRepository.findCustomerAccountByAccountNumberAndAccountType(
                sourceAccount.getAccountNumber(),
                sourceAccount.getAccountType());

        if (possibleAccount.isPresent()) {
            var account = possibleAccount.get();

            // ensure sufficient balance for transfer
            var currentBalance = accountService.getAccountBalance(account.getId());
            if (request.getAmount() > currentBalance)
                throw new BusinessException(INSUFFICIENT_FUNDS_MESSAGE);

            // create transaction debit history against source account
            var transfer = new CustomerAccountTransaction();
            transfer.setAmount(request.getAmount());
            transfer.setCustomerAccountId(account.getId());
            transfer.setType(DEBIT.getCode());
            transfer.setCreated(now());
            transfer.setModified(now());
            accountTransactionRepository.save(transfer);

            // update transaction ledger
            currentBalance = accountService.getAccountBalance(account.getId());
            account.setLastBalance(currentBalance);
            accountRepository.save(account);

            return TransferProcessResponse
                    .builder()
                    .code(SUCCESS.getCode())
                    .responseMessage(SUCCESS.getMessage())
                    .build();
        } else
            throw new BusinessException(format("Account %s with type %s not found", sourceAccount.getAccountNumber(), sourceAccount.getAccountType()));
    }
}
