package com.securevault.service.user;

import com.securevault.dao.UserDao;
import com.securevault.model.dto.user.UserRequest;
import com.securevault.model.dto.user.UserResponse;
import com.securevault.model.entity.user.Role;
import com.securevault.model.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.securevault.model.entity.user.AccountStatus.PENDING_VERIFICATION;
import static com.securevault.model.entity.user.KycStatus.PENDING;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired private UserDao userDao;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(final UserRequest request) {
        User user = buildUserFromRequest(request);
        userDao.saveUser(user);
        log.info("Registered new user with email: {}", user.getEmail());
        return constructUserResponse(user);
    }


    private User buildUserFromRequest(final UserRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.valueOf(request.getRole()))
                .accountStatus(PENDING_VERIFICATION)
                .kycStatus(PENDING)
                .build();
    }

    private UserResponse constructUserResponse(final User user) {
        UserResponse response = new UserResponse();
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setAccountStatus(user.getAccountStatus().name());
        response.setKycStatus(user.getKycStatus().name());
        response.setMessage("User registered successfully.");
        return response;
    }
}
