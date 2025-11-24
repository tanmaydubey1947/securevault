package com.securevault.service.user;

import com.securevault.model.dto.BaseResponse;
import com.securevault.model.dto.user.UserRequest;
import com.securevault.model.dto.user.UserResponse;

public interface UserService {

    UserResponse register(UserRequest request);

    UserResponse getUserDetails();

    UserResponse verifyUser(String token);

    UserResponse generateResetToken(UserRequest request);

    UserResponse resetCredentials(UserRequest request);

    UserResponse getUserTransactions();

    BaseResponse changeRole(UserRequest request);
}
