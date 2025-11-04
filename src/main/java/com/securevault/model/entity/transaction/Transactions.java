package com.securevault.model.entity.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transactions {

    private Long transactionId;
    private Long accountId;
    private Double amount;
    private String transactionType; // e.g., "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    private String description;
    private String timestamp;

}
