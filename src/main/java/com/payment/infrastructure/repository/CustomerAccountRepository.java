package com.payment.infrastructure.repository;

import com.payment.infrastructure.entity.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    Optional<CustomerAccount> findCustomerAccountByAccountNumberAndAccountType(String accountNumber, String accountType);
}