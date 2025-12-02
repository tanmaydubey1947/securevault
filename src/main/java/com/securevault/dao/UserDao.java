package com.securevault.dao;

import com.securevault.model.entity.Wallet;
import com.securevault.model.entity.transaction.Transaction;
import com.securevault.model.entity.user.AccountStatus;
import com.securevault.model.entity.user.User;
import com.securevault.util.rowMapper.TransactionRowMapper;
import com.securevault.util.rowMapper.UsersRowMapper;
import com.securevault.util.rowMapper.WalletRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public void openUserWallet(final String email) {
        final String sql = "Insert INTO wallets (user_email) VALUES (?)";
        final int update = jdbcTemplate.update(sql, email);
        if (update > 0) {
            log.info("Wallet opened successfully for user with email: {}", email);
        } else {
            throw new RuntimeException("Failed to open wallet for user with email: " + email);
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

    public Wallet getWalletByEmail(final String email) {
        final String sql = "SELECT * FROM wallets WHERE user_email = ?";
        return jdbcTemplate.queryForObject(sql, new WalletRowMapper(), email);
    }

    public void updateUserRole(final String email, final String role) {
        final String sql = "UPDATE users SET role = ? WHERE email = ?";
        int update = jdbcTemplate.update(sql, role, email);
        if (update > 0) {
            log.info("User with email {} role updated to {}.", email, role);
        } else {
            throw new RuntimeException("Failed to update role for user with email " + email);
        }
    }

    public List<Transaction> getCreditTransactions(final String email) {
        final String query = "SELECT * FROM transactions where receiver = ?";
        return jdbcTemplate.query(query, new TransactionRowMapper(), email);
    }

    public List<Transaction> getDebitTransactions(final String email) {
        final String query = "SELECT * FROM transactions where sender = ?";
        return jdbcTemplate.query(query, new TransactionRowMapper(), email);
    }

    public List<User> getAllUsers() {
        final String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UsersRowMapper());
    }

    public List<Wallet> getAllWallets() {
        final String sql = "SELECT * FROM wallets";
        return jdbcTemplate.query(sql, new WalletRowMapper());
    }
}
