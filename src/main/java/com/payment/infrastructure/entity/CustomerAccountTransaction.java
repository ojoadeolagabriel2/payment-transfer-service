package com.payment.infrastructure.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "customer_account_transaction")
public class CustomerAccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "customer_account_id")
    private long customerAccountId;

    @Column(name = "type")
    private String type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "modified")
    private LocalDateTime modified;

    @Column(name = "created")
    private LocalDateTime created;
}
