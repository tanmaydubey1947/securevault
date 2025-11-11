package com.securevault.model.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private String senderMail;
    private String receiverMail;
    private Double amount;
    private String transactionType; // e.g., "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    private String description;
}
