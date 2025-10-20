package com.securevault.dao;

import com.securevault.entity.user.User;
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

}
