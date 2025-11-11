package com.securevault.model.dto.transaction;

import com.securevault.model.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse extends BaseResponse {

    private int transactionId;
    private Long accountId;
    private Double amount;
    private String transactionType; // e.g., "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    private String description;
    private String timestamp;
}
