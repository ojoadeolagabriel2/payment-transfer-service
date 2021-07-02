package com.payment.infrastructure.repository;

import com.payment.infrastructure.entity.CustomerAccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;

public interface CustomerAccountTransactionRepository extends JpaRepository<CustomerAccountTransaction, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<CustomerAccountTransaction> findAllByCustomerAccountId(Long accountId);
}