package com.securevault.model.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
