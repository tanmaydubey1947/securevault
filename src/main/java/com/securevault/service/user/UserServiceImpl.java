package com.securevault.service.user;

import com.securevault.dao.UserDao;
import com.securevault.model.dto.user.UserRequest;
import com.securevault.model.dto.user.UserResponse;
import com.securevault.model.entity.user.Role;
import com.securevault.model.entity.user.User;
import com.securevault.service.auth.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.securevault.model.entity.user.AccountStatus.ACTIVE;
import static com.securevault.model.entity.user.AccountStatus.PENDING_VERIFICATION;
import static com.securevault.model.entity.user.KycStatus.PENDING;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired private UserDao userDao;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    @Override
    public UserResponse register(final UserRequest request) {
        User user = buildUserFromRequest(request);
        userDao.saveUser(user);
        log.info("Registered new user with email: {}", user.getEmail());
        sendTokenToUser(user.getEmail());
        return constructUserResponse(user);
    }

    @Override
    public UserResponse getUserDetails(UserRequest request) {
        if(request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email must be provided to fetch user details.");
        }
        User user = userDao.getUserByEmail(request.getEmail());
        log.info("Fetched details for user with email: {}", user.getEmail());
        return constructUserResponse(user);
    }

    @Override
    public UserResponse verifyUser(final String token) {
        validateToken(token);
        final String email = jwtService.extractUsername(token);
        userDao.updateAccountStatus(ACTIVE, email);
        sendVerifiedMail(email);
        final UserResponse response = new UserResponse();
        response.setMessage("User verification completed successfully.");
        return response;
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
                .createdAt(LocalDateTime.now())
                .build();
    }

    private UserResponse constructUserResponse(final User user) {
        UserResponse response = new UserResponse();
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setAccountStatus(user.getAccountStatus().name());
        response.setKycStatus(user.getKycStatus().name());
        response.setMessage("Successfully processed user details.");
        return response;
    }

    private void validateToken(final String token) {
        if(token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Verification token must be provided.");
        }

        if(jwtService.isTokenExpired(token)) {
            throw new IllegalArgumentException("Verification token has expired.");
        }
    }

    private void sendTokenToUser(String email) {
        String token = jwtService.generateVerificationToken(email);
        log.info("Sending verification email to {} with token: {}", email, token); //TODO: Need to remove
        //TODO: Implement actual email sending logic here
    }

    private void sendVerifiedMail(String email) {
        log.info("Sending account verified email to {}", email);
        //TODO: Implement actual email sending logic here
    }
}
