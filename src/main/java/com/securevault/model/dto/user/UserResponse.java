package com.securevault.model.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.securevault.model.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends BaseResponse {

    private Integer id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private String accountStatus;
    private String kycStatus;
    private LocalDateTime createdAt;
    private Double availableAmount;
    private Double pendingAmount;
    private Integer walletVersion;
}
