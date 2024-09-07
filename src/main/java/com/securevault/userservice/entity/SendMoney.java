package com.securevault.userservice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMoney {

    private int fromAccountNumber;
    private int toAccountNumber;
    private double amount;
    private String username;

}
