package com.securevault.model.entity.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    private int transactionId;
    private String sender;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private double amount;
    private String receiver;
    private String bankReference;
    private String idempotencyKey;
    private LocalDateTime createdAt;

}
