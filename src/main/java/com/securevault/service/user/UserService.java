package com.securevault.service.user;

import com.securevault.model.dto.user.UserRequest;
import com.securevault.model.dto.user.UserResponse;

public interface UserService {

    UserResponse register(UserRequest request);

    UserResponse getUserDetails(UserRequest request);

    UserResponse verifyUser(String token);

    UserResponse generateResetToken(UserRequest request);

    UserResponse resetCredentials(UserRequest request);

}
