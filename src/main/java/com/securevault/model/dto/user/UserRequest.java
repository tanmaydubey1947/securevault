package com.securevault.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private int otp;
    private String newPassword;
    private String confirmPassword;
    private String token;
}
