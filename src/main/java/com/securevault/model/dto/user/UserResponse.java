package com.securevault.model.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.securevault.model.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends BaseResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private String accountStatus;
    private String kycStatus;
}
