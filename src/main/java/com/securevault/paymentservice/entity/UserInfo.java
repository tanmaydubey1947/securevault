package com.securevault.paymentservice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private int accountNumber;
    private String name;
    private String profession;
    private double accountBalance;
    private String accountType;
    private String username;
    private String password;
    private String roles;
}
