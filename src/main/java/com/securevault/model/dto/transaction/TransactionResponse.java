package com.securevault.model.dto.transaction;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private Long transactionId;
    private Long accountId;
    private Double amount;
    private String transactionType; // e.g., "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    private String description;
    private String timestamp;
}
