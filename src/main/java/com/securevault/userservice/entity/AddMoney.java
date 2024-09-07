package com.securevault.userservice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMoney {

    private String username;
    private int accountNumber;
    private double amount;

}
