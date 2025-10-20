package com.securevault.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String fullName;
    private String email;
    private String passwordHash;
    private KycStatus kycStatus;
    private String phoneNumber;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
    private Role role;

}
