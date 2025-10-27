package com.securevault.service.user;

import com.securevault.dao.UserDao;
import com.securevault.event.notification.producer.NotificationProducer;
import com.securevault.model.dto.notification.NotificationRequest;
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
    @Autowired private NotificationProducer notificationProducer;

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

        final String url = "http://localhost:8080/user/verifyUser?token=" + token;
        log.info("Sending verification email to {} with token: {} and url: {}",
                email, token, url); //TODO: Need to remove this log in production
        final String message = "Please verify your account using the link: " + url;

        final NotificationRequest request = new NotificationRequest();
        request.setSubject("Account Verification");
        request.setMessage(message);
        request.setRecipient(email);
        notificationProducer.processNotification(request);
    }

    private void sendVerifiedMail(String email) {
        log.info("Sending account verified email to {}", email);
        final NotificationRequest request = new NotificationRequest();
        request.setSubject("Account Verified");
        request.setMessage("Your account has been successfully verified.");
        request.setRecipient(email);
        notificationProducer.processNotification(request);
    }
}
