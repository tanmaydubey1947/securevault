package com.securevault.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    private int id;
    private String userEmail;
    private double availableAmount;
    private double pendingAmount;
    private int version;
    private LocalDateTime createdAt;

}
