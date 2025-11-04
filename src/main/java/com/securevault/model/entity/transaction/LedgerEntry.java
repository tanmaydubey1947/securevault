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
public class LedgerEntry {

    private int ledgerEntryId;
    private int transactionId;
    private String email;
    private EntryType entryType;
    private double amount;
    private LocalDateTime createdAt;
}
