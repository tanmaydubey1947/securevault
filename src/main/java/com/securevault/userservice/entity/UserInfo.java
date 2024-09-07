package com.securevault.userservice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue
    private int accountNumber;
    private String name;
    private String profession;
    private double accountBalance;
    private String accountType;
    private String username;
    private String password;
    private String roles; // Ex: ROLE_USER,ROLE_ADMIN
}
