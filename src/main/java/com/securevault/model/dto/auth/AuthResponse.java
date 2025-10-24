package com.securevault.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.securevault.model.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse extends BaseResponse {

    private String msg;
    private String token;
    private int expiresIn;
    private String refreshToken;
}
