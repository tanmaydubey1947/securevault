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
    private int userId;
    private int transactionId;
    private LocalDateTime createdAt;
}

