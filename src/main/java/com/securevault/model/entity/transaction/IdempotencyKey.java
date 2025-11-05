package com.securevault.model.entity.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyKey {

    private String idempotencyKey;
    private String userEmail;
    private int transactionId;
    private LocalDateTime createdAt;
}

