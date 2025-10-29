package com.securevault.dao;

import com.securevault.model.entity.user.AccountStatus;
import com.securevault.model.entity.user.User;
import com.securevault.util.rowMapper.UsersRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserDao {

    @Autowired private JdbcTemplate jdbcTemplate;

    public User getUserByEmail(final String email) {
        final String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new UsersRowMapper(), email);
    }

    public void saveUser(final User user) {
        final String sql = """
                    INSERT INTO users (
                        full_name,\s
                        email,\s
                        password_hash,\s
                        kyc_status,\s
                        phone_number,\s
                        account_status,\s
                        created_at, \s
                        role
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        int update = jdbcTemplate.update(sql, user.getFullName(), user.getEmail(), user.getPassword(),
                user.getKycStatus().name(), user.getPhoneNumber(), user.getAccountStatus().name(),
                user.getCreatedAt(), user.getRole().name());

        if (update > 0) {
            log.info("User with email {} saved successfully.", user.getEmail());
        } else {
            throw new RuntimeException("Failed to save user with email " + user.getEmail());
        }
    }

    public void updateAccountStatus(final AccountStatus status, final String email) {
        final String sql = "UPDATE users SET account_status = ? WHERE email = ?";
        int update = jdbcTemplate.update(sql, status.name(), email);
        if (update > 0) {
            log.info("User with email {} account status updated to {}.", email, status);
        } else {
            throw new RuntimeException("Failed to update account status for user with email " + email);
        }
    }

    public void savePasswordResetToken(final String email, final String token) {
        final String sql = "INSERT INTO password_reset_tokens (email, token, created_at) VALUES (?, ?, NOW())";
        final int update = jdbcTemplate.update(sql, email, token);
        if (update > 0) {
            log.info("Password reset token persisted for email: {}", email);
        } else {
            throw new RuntimeException("Failed to persist password reset token for email: " + email);
        }
    }

    public void updatePassword(final String email, final String newPasswordHash) {
        final String sql = "UPDATE users SET password_hash = ? WHERE email = ?";
        final int update = jdbcTemplate.update(sql, newPasswordHash, email);
        if (update > 0) {
            log.info("Password updated successfully for email: {}", email);
        } else {
            throw new RuntimeException("Failed to update password for email: " + email);
        }
    }

}
