package com.securevault.service.user;

import com.securevault.dao.UserDao;
import com.securevault.event.notification.NotificationProducer;
import com.securevault.exception.custom.UserNotFoundException;
import com.securevault.model.dto.notification.NotificationRequest;
import com.securevault.model.dto.transaction.TransactionResponse;
import com.securevault.model.dto.user.UserRequest;
import com.securevault.model.dto.user.UserResponse;
import com.securevault.model.entity.Wallet;
import com.securevault.model.entity.transaction.Transaction;
import com.securevault.model.entity.user.Role;
import com.securevault.model.entity.user.User;
import com.securevault.service.auth.JwtService;
import com.securevault.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.securevault.model.entity.user.AccountStatus.ACTIVE;
import static com.securevault.model.entity.user.AccountStatus.PENDING_VERIFICATION;
import static com.securevault.model.entity.user.KycStatus.PENDING;
import static com.securevault.model.entity.user.Role.ROLE_USER;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired private UserDao userDao;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private NotificationProducer notificationProducer;

    @Override
    public UserResponse register(final UserRequest request) {
        final User user = buildUserFromRequest(request);
        userDao.saveUser(user);
        log.info("Registered new user with email: {}", user.getEmail());
        sendTokenToUser(user.getEmail());

        final UserResponse response = new UserResponse();
        response.setMessage("Please verify your email to activate your account.");
        return response;
    }

    @Override
    public UserResponse getUserDetails() {
        final String email = Util.INSTANCE.getCurrentEmail();
        final User user = userDao.getUserByEmail(email);
        final Wallet wallet = userDao.getWalletByEmail(email);
        log.info("Fetched details for user with email: {}", email);
        return constructUserResponse(user, wallet);
    }

    @Override
    public UserResponse verifyUser(final String token) {//TODO: Check if user is already verified
        validateToken(token);
        final String email = jwtService.extractUsername(token);
        userDao.updateAccountStatus(ACTIVE, email);
        userDao.openUserWallet(email);
        sendVerifiedMail(email);
        final UserResponse response = new UserResponse();
        response.setMessage("User verification completed successfully.");
        return response;
    }

    @Override
    public UserResponse generateResetToken(final UserRequest request) {//TODO: Same token can be used to reset multiple times within validity
        final String email = request.getEmail();
        final String token = jwtService.generatePasswordResetToken(email);
        checkIfUserExists(email);
//        userDao.savePasswordResetToken(email, token);
        log.info("Generated password reset token for user with email: {}", email);
        sendPasswordResetToken(email, token);
        final UserResponse response = new UserResponse();
        response.setMessage("Password reset token generated successfully.");
        notifyCredentialsReset(email);
        return response;
    }

    @Override
    public UserResponse resetCredentials(final UserRequest request) {
        validateResetRequest(request);
        final String token = request.getToken();
        final String email = jwtService.extractUsername(token);
        final String passwordHash = passwordEncoder.encode(request.getNewPassword());
        userDao.updatePassword(email, passwordHash);

        final UserResponse response = new UserResponse();
        response.setMessage("Password reset successfully.");
        return response;
    }

    @Override
    public List<TransactionResponse> getUserTransactions() {
        final String email = Util.INSTANCE.getCurrentEmail();
        final List<Transaction> creditTransactions = userDao.getCreditTransactions(email);
        final List<Transaction> debitTransactions = userDao.getDebitTransactions(email);
        log.info("Fetched transactions for user with email: {}", email);
        final TransactionResponse response = new TransactionResponse();
        return null;
    }

    @Override
    public UserResponse changeRole(final UserRequest request) {
        final String email = request.getEmail();
        checkIfUserExists(email);
        final String newRole = request.getRole();
        log.info("Changing role for user {} to {}", email, newRole);
        userDao.updateUserRole(email, newRole);
        final UserResponse response = new UserResponse();
        response.setMessage("User role updated successfully.");
        return response;
    }


    private User buildUserFromRequest(final UserRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(getRole(request.getRole()))
                .accountStatus(PENDING_VERIFICATION)
                .kycStatus(PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private UserResponse constructUserResponse(final User user, final Wallet wallet) {
        UserResponse response = new UserResponse();
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setAccountStatus(user.getAccountStatus().name());
        response.setKycStatus(user.getKycStatus().name());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setAvailableAmount(wallet.getAvailableAmount());
        response.setPendingAmount(wallet.getPendingAmount());
        response.setWalletVersion(wallet.getVersion());
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

    private void sendTokenToUser(final String email) {
        String token = jwtService.generateVerificationToken(email);

        final String url = "http://localhost:8080/user/verifyUser?token=" + token;
        log.info("Sending verification email to {} with token: {} and url: {}",
                email, token, url); //TODO: Need to remove this log in production
        final String message = "Please verify your account using the link: " + url;

        final NotificationRequest request = new NotificationRequest();
        request.setSubject("Account Verification");
        request.setMessage(message);
        request.setRecipient(email);
        //TODO: notificationProducer.processNotification(request);
    }

    private void sendVerifiedMail(final String email) {
        log.info("Sending account verified email to {}", email);
        final NotificationRequest request = new NotificationRequest();
        request.setSubject("Account Verified");
        request.setMessage("Your account has been successfully verified.");
        request.setRecipient(email);
        notificationProducer.processNotification(request);
    }

    private void sendPasswordResetToken(final String email, final String token) {
        final String url = "http://localhost:5500/pages/reset-password.html?token=" + token;
        final String message = "Please reset your password using the link: " + url;
        log.info("Password reset URL: {}", url); //TODO: Remove in production

        final NotificationRequest request = new NotificationRequest();
        request.setSubject("Password Reset");
        request.setMessage(message);
        request.setRecipient(email);
        notificationProducer.processNotification(request);
    }

    private void checkIfUserExists(final String email) {
        try {
            final User user = userDao.getUserByEmail(email);
            if(user == null) {
                throw new UserNotFoundException("No user found with email: " + email);
            }
        } catch (final Exception e) {
            throw new UserNotFoundException("No user found with email: " + email);
        }
    }

    private void validateResetRequest(final UserRequest request) {
        validateToken(request.getToken());

        if(request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("New password must be provided.");
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match.");
        }
    }

    private void notifyCredentialsReset(final String email) {
        log.info("Notifying user {} about password reset", email);
        final NotificationRequest request = new NotificationRequest();
        request.setSubject("Password Reset Successful");
        request.setMessage("Your password has been successfully reset.");
        request.setRecipient(email);
        notificationProducer.processNotification(request);
    }

    private Role getRole(final String role) {
        if(role != null) {
            return Role.valueOf(role);
        }
        return ROLE_USER;
    }
}
