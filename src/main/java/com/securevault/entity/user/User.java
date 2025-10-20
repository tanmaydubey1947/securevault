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
    private String password;
    private String phoneNumber;
    private KycStatus kycStatus;
    private AccountStatus accountStatus;
    private Role role;
    private LocalDateTime createdAt;

}
