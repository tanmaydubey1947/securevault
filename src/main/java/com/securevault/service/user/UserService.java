package com.securevault.service.user;

import com.securevault.model.dto.BaseResponse;
import com.securevault.model.dto.transaction.TransactionResponse;
import com.securevault.model.dto.user.UserRequest;
import com.securevault.model.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse register(UserRequest request);

    UserResponse getUserDetails();

    UserResponse verifyUser(String token);

    UserResponse generateResetToken(UserRequest request);

    UserResponse resetCredentials(UserRequest request);

    List<TransactionResponse> getUserTransactions();

    BaseResponse changeRole(UserRequest request);

    List<UserResponse> getAllUsers();
}
