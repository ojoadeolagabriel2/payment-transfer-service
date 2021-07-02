package com.payment.infrastructure.repository;

import com.payment.infrastructure.entity.CustomerAccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAccountTransactionRepository extends JpaRepository<CustomerAccountTransaction, Long> {
    List<CustomerAccountTransaction> findAllByCustomerAccountId(Long accountId);
}